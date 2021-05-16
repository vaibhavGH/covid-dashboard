package org.example.coviddashboard.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.coviddashboard.CovidDashBoardApplication;
import org.example.coviddashboard.country.beans.Country;
import org.example.coviddashboard.country.beans.OneDayData;
import org.example.coviddashboard.country.beans.SeriesData;
import org.example.coviddashboard.country.dao.CountryRepository;
import org.example.coviddashboard.country.state.beans.State;
import org.example.coviddashboard.country.state.beans.StateSummary;
import org.example.coviddashboard.country.state.dao.StateRepository;
import org.example.coviddashboard.integration.config.H2JpaConfig;
import org.example.coviddashboard.security.beans.JwtResponse;
import org.example.coviddashboard.security.beans.MessageResponse;
import org.example.coviddashboard.users.beans.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CovidDashBoardApplication.class, H2JpaConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CovidDashBoardTest {



    @LocalServerPort
    private int port;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private StateRepository stateRepository;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TestRestTemplate restTemplate = new TestRestTemplate();
    private static final HttpHeaders headers = new HttpHeaders();
    private static JwtResponse jwtResponse;

    private static Country country = new Country("India", getDate(2021, 4, 27));

    private static int numberOfStates;
    private static int dataForDays;

    private static final List<List<State>> states = new ArrayList();
    private static final List<StateSummary> stateSummaries = new ArrayList<>();

    private static Date getDate(int year, int month, int day) {
        return new GregorianCalendar(year, month, day).getTime();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private static boolean once = false;
    
    @Before
    public void setUp() {
        if(once)
            return;

        //Register and login
        User user = new User("Test", "Test123");
        HttpEntity<User> userHttpEntity = new HttpEntity<>(user, headers);
        ResponseEntity<MessageResponse> responseEntityTemp = restTemplate.exchange(
                createURLWithPort("/auth/register"), HttpMethod.POST, userHttpEntity, MessageResponse.class);

        ResponseEntity<JwtResponse> responseEntity = restTemplate.exchange(
                createURLWithPort("/auth/login"), HttpMethod.POST, userHttpEntity, JwtResponse.class);

        jwtResponse = responseEntity.getBody();


        //Save Data
        country = countryRepository.save(country);

        List<State> stateData = new ArrayList<>();
        State state11 = new State("MH", 100, 100, 10);
        State state12 = new State("MH", 80, 80, 8);
        stateData.addAll(Arrays.asList(state11, state12));
        states.add(stateData);

        stateData = new ArrayList<>();
        State state21 = new State("KL", 100, 80, 8);
        State state22 = new State("KL", 90, 70, 7);
        stateData.addAll(Arrays.asList(state21, state22));
        states.add(stateData);

        state11.setDate(getDate(2021, 4, 27));
        state12.setDate(getDate(2021, 4, 26));
        state21.setDate(getDate(2021, 4, 27));
        state22.setDate(getDate(2021, 4, 26));

        state11.setCountry(country);
        state12.setCountry(country);
        state21.setCountry(country);
        state22.setCountry(country);

        stateRepository.saveAll(Arrays.asList(state11, state12, state21, state22));

        numberOfStates = states.size();
        dataForDays = states.get(0).size();

        //create state summaries
        for(int i=0; i<numberOfStates; i++)
        {
            StateSummary stateSummary = new StateSummary(
                    states.get(i).get(0).getName(),
                    states.get(i).get(0).getConfirmed() - states.get(i).get(1).getConfirmed(),
                    states.get(i).get(0).getRecovered() - states.get(i).get(1).getRecovered(),
                    states.get(i).get(0).getDeceased() - states.get(i).get(1).getDeceased(),
                    states.get(i).get(0)
            );
            stateSummaries.add(stateSummary);
        }

        headers.add("Authorization", "Bearer " + jwtResponse.getToken());

        once = true;
    }

    @Test
    public void testGetAllLatestStateData() {
        //without header
        ResponseEntity<?> responseEntity1 = restTemplate.exchange(
                createURLWithPort("/covid19/countries/" + country.getId()), HttpMethod.POST,
                new HttpEntity<>(new HttpHeaders()), Object.class);

        Assert.assertEquals(responseEntity1.getStatusCode(), HttpStatus.UNAUTHORIZED);

        //valid request
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<List> responseEntity = restTemplate.exchange(
                createURLWithPort("/covid19/countries/" + country.getId()), HttpMethod.POST, httpEntity, List.class);

        List<State> responseStates = responseEntity.getBody();
        Assert.assertEquals(states.size(), numberOfStates);

        for (int i = 0; i < numberOfStates; i++) {
            State resultState = mapper.convertValue(responseStates.get(i), State.class);
            State expectedState = states.get(i).get(0);

            Assert.assertEquals(resultState, expectedState);
        }
    }
/*
    @Test
    public void testGetStateData() {
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<List> responseEntity = restTemplate.exchange(
                createURLWithPort("/covid19/states/" + states.get(0).get(0).getName()), HttpMethod.GET, httpEntity,
                List.class);

        List<State> states = responseEntity.getBody();
        Assert.assertEquals(states.size(), dataForDays);

        for (int i = 0; i < dataForDays; i++) {
            State resultState = mapper.convertValue(states.get(i), State.class);
            State expectedState = states.get(0).get(i);

            Assert.assertEquals(resultState, expectedState);
        }
    }*/

    @Test
    public void testGetLatestStateData() {
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<State> responseEntity = restTemplate.exchange(
                createURLWithPort("/covid19/countries/" + country.getId() + "/states/" + states.get(0).get(0).getName() + "/latest"), HttpMethod.GET, httpEntity,
                State.class);

        /*int confirmed = 0, recovered = 0, deceased = 0;
        for(int i=0; i<dataForDays; i++)
        {
            State state = states.get(0).get(i);

            confirmed = confirmed + state.getConfirmed();
            recovered = recovered + state.getRecovered();
            deceased = deceased + state.getDeceased();
        }
        OneDayData oneDayData = new OneDayData(null, confirmed, recovered, deceased);*/

        State responseState = responseEntity.getBody();

        Assert.assertEquals(responseState, states.get(0).get(0));
    }

    @Test
    public void testGetStateSeriesData() {
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<SeriesData> responseEntity = restTemplate.exchange(
                createURLWithPort("/covid19/states/" + states.get(0).get(0).getName() + "/series"), HttpMethod.GET, httpEntity,
                SeriesData.class);

        SeriesData responseSeriesData = responseEntity.getBody();

        SeriesData expectedSeriesData = new SeriesData();
        for(int i=dataForDays-1; i>=0; i--)
        {
            State state = states.get(0).get(i);

            expectedSeriesData.addSeriesData(state.getDate(), state.getConfirmed(), state.getRecovered(),
                    state.getDeceased(), state.getConfirmed() - (state.getRecovered() + state.getDeceased() ));
        }

        Assert.assertEquals(responseSeriesData, expectedSeriesData);
    }

    @Test
    public void testGetStatesSummary()
    {
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<List> responseEntity = restTemplate.exchange(
                createURLWithPort("/covid19/countries/" + country.getId() + "/states/summary"), HttpMethod.GET,
                httpEntity, List.class);

        List<StateSummary> stateSummariesResponse = responseEntity.getBody();

        for(int i=0; i< stateSummariesResponse.size(); i++)
        {
            StateSummary responseStateSummary = mapper.convertValue(stateSummariesResponse.get(i), StateSummary.class);

            Assert.assertEquals(responseStateSummary, stateSummaries.get(i));
        }
    }

    @Test
    public void testGetStateData()
    {
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<SeriesData> responseEntity = restTemplate.exchange(
                createURLWithPort("/covid19/countries/series"), HttpMethod.GET,
                httpEntity, SeriesData.class);

        SeriesData responseSeriesData = responseEntity.getBody();

        SeriesData seriesDataExpected = new SeriesData();

        for(int i=dataForDays-1; i>=0 ; i--)
        {
            Date date = null;
            int confirmed = 0, recovered = 0, deceased = 0, active =0;
            for (int j = 0; j < numberOfStates; j++) {
                State state = states.get(j).get(i);
                if(date == null)
                    date = state.getDate();

                confirmed +=  state.getConfirmed();
                recovered +=  state.getRecovered();
                deceased +=  state.getDeceased();
                active += state.getActive();
            }

            seriesDataExpected.addSeriesData(date, confirmed, recovered, deceased, active);
        }

        Assert.assertEquals(responseSeriesData, seriesDataExpected);
    }

    @Test
    public void testGetCountrySummary()
    {
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<OneDayData> responseEntity = restTemplate.exchange(
                createURLWithPort("/covid19/countries/"+ country.getId()+"/summary"), HttpMethod.GET,
                httpEntity, OneDayData.class);

        OneDayData  responseOneDayData = responseEntity.getBody();

        Date date = null;
        int confirmed = 0, recovered = 0, deceased = 0;
        for (int j = 0; j < numberOfStates; j++) {
            State state = states.get(j).get(0);
            if(date == null)
                date = state.getDate();

            confirmed +=  state.getConfirmed();
            recovered +=  state.getRecovered();
            deceased +=  state.getDeceased();
        }

        OneDayData expectedOneDayData = new OneDayData(date, confirmed, recovered, deceased);

        Assert.assertEquals(responseOneDayData, expectedOneDayData);
    }
    /*

   @RequestMapping(value = "/countries/{countryID}/summary",method = RequestMethod.GET)
    public @ResponseBody
    OneDayData getCountrySummary(@PathVariable("countryID") int countryID)
    {
        return countryService.getCountrySummary(countryID);
    }

    */
    @After
    public void cleanUp() {

    }
}
