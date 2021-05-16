package org.example.coviddashboard.users.unit;

import org.example.coviddashboard.users.beans.User;
import org.example.coviddashboard.users.dao.UserRepository;
import org.example.coviddashboard.users.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user = new User("Vaibhav", "test");

    @Before
    public void setUp() {

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.save(user)).thenReturn(user);

       /* Mockito.doAnswer(invocationOnMock -> {
            User user = invocationOnMock.getArgument(0, User.class);
            Assert.assertTrue("Invalid Username", user.getUsername() != null
                    && !user.getUsername().isEmpty());

            Assert.assertTrue("Invalid Password", user.getPassword() != null
                    && !user.getPassword().isEmpty());

            return null;
        }).when(userRepository).save(Mockito.isA(User.class));

        Mockito.doThrow(new RuntimeException()).when(userRepository).save(Mockito.isNull());*/

    }

    @Test
    public void testUserFetch() {
        Optional<User> user =  userService.findUserByUsername("Vaibhav");

        //fetch valid user
        Assert.assertTrue("Failed to fetch user", user.isPresent() && user.get().getUsername().equals("Vaibhav") &&
                        user.get().getPassword().equals("test"));

        //fetch invalid user
        user =  userService.findUserByUsername("Someone");
        Assert.assertTrue("Unavailable user fetched", !user.isPresent() );
        Assert.assertThrows(Exception.class, ()->userService.findUserByUsername(null));
        Assert.assertThrows(Exception.class, ()->userService.findUserByUsername(""));

    }

    @Test
    public void testUserExists() {
        boolean userPresent = userService.existsByUsername("Vaibhav");


        //Valid user
        Assert.assertTrue("User exist check failed", userPresent);

        //Invalid user
        userPresent = userService.existsByUsername("Someone");
        Assert.assertTrue("Invalid User exist check failed", !userPresent);
        Assert.assertThrows(Exception.class, ()->userService.existsByUsername(null));
        Assert.assertThrows(Exception.class, ()->userService.existsByUsername(""));
    }


    @Test
    public void testSave()
    {
        Assert.assertThrows(Exception.class, ()->userService.save(null));

        User user1 = new User(null, "pass");
        User user2 = new User("", "pass");
        User user3 = new User("User3", null);
        User user4 = new User("User4", "");

        Assert.assertThrows(Exception.class, ()->userService.save(user1));
        Assert.assertThrows(Exception.class, ()->userService.save(user2));
        Assert.assertThrows(Exception.class, ()->userService.save(user3));
        Assert.assertThrows(Exception.class, ()->userService.save(user4));

        Assert.assertEquals(userService.save(user),user);
    }

    @After
    public void cleanUp() {

    }
}
