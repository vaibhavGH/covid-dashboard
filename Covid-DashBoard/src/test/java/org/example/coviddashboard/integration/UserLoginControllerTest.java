package org.example.coviddashboard.integration;

import org.example.coviddashboard.CovidDashBoardApplication;
import org.example.coviddashboard.integration.config.H2JpaConfig;
import org.example.coviddashboard.security.beans.JwtResponse;
import org.example.coviddashboard.security.beans.MessageResponse;
import org.example.coviddashboard.users.beans.User;
import org.example.coviddashboard.users.dao.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CovidDashBoardApplication.class, H2JpaConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserLoginControllerTest {

    @LocalServerPort
    private int port;

    private static TestRestTemplate restTemplate = new TestRestTemplate();

    private static HttpHeaders headers = new HttpHeaders();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private static User user = new User("Test","Test123");

    @Before
    public void setUp()
    {

    }

    @Test
    public void testRegister()
    {
        User user1 = new User("Test1","");
        User user2 = new User("Test1","Test123");

        HttpEntity<User> userHttpEntity1 = new HttpEntity<>(user1, headers);
        HttpEntity<User> userHttpEntity2 = new HttpEntity<>(user2, headers);

        //Bad Request
        ResponseEntity<MessageResponse> responseEntity1 = restTemplate.exchange(
                createURLWithPort("/auth/register"), HttpMethod.POST, userHttpEntity1, MessageResponse.class);

        Assert.assertEquals(responseEntity1.getStatusCode(), HttpStatus.BAD_REQUEST);

        //Valid Registration
        ResponseEntity<MessageResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/auth/register"), HttpMethod.POST, userHttpEntity2, MessageResponse.class);

        Assert.assertTrue(responseEntity.getBody().getMessage().equals("User registered successfully!"));
    }

    @Test
    public void testLogin()
    {
        User user1 = new User("Someone","SomePass");
        HttpEntity<User> userHttpEntity1 = new HttpEntity<>(user1, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);

        //Invalid Login
        ResponseEntity<JwtResponse> responseEntity1 = restTemplate.exchange(
                createURLWithPort("/auth/login"), HttpMethod.POST, userHttpEntity1, JwtResponse.class);

        Assert.assertEquals(responseEntity1.getStatusCode(), HttpStatus.UNAUTHORIZED);

        //Register and login
        HttpEntity<User> userHttpEntity = new HttpEntity<>(user, headers);
        ResponseEntity<MessageResponse> responseEntityTemp = restTemplate.exchange(
                createURLWithPort("/auth/register"), HttpMethod.POST, userHttpEntity, MessageResponse.class);

        ResponseEntity<JwtResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/auth/login"), HttpMethod.POST, userHttpEntity, JwtResponse.class);

        JwtResponse jwtResponse = responseEntity.getBody();
        Assert.assertEquals(jwtResponse.getUsername(), "Test");
        Assert.assertNotNull(jwtResponse.getToken());
        System.out.println("Token*** : " + jwtResponse.getToken());

        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUZXN0IiwiaWF0IjoxNjE5ODExNTk0LCJleHAiOjE2MTk4OTc5OTR9.dBIRgzPr8-uD_8WlUQkh-F7W3QwOpFPK5cqYuoPAYTx4TfK9YhrHAWC5gzaSc_KfZtac7lWuJK8IY92fH0JnBQ";

        headers.add("Authorization", "Bearer " + token);
        ResponseEntity<MessageResponse> responseEntityTest = restTemplate.exchange(
                createURLWithPort("/auth/validateToken"), HttpMethod.GET, userHttpEntity, MessageResponse.class);

        Assert.assertEquals(responseEntityTest.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }
}
