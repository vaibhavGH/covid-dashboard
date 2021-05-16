package org.example.coviddashboard.country.unit;

import org.example.coviddashboard.country.beans.Country;
import org.example.coviddashboard.country.beans.OneDayData;
import org.example.coviddashboard.country.beans.SeriesData;
import org.example.coviddashboard.country.dao.CountryRepository;
import org.example.coviddashboard.country.services.CountryService;
import org.example.coviddashboard.country.state.beans.State;
import org.example.coviddashboard.country.state.beans.StateSummary;
import org.example.coviddashboard.country.state.dao.StateRepository;
import org.example.coviddashboard.country.state.services.StateService;
import org.example.coviddashboard.country.state.unit.StateServiceTest;
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

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Autowired
    private StateService stateService;

    @MockBean
    private CountryRepository countryRepository;

    @MockBean
    private StateRepository stateRepository;


    private List<OneDayData> dataSeries = new ArrayList<>();



    private Country country = new Country("India", getDate(2021, 4, 27));
    private List<State> states = new ArrayList<>();

    private Date getDate(int year, int month, int day)
    {
        return new GregorianCalendar(year, month, day).getTime();
    }

    @Before
    public void setUp(){
        country.setId(1);
        StateServiceTest.initializeMockedStateRepository(stateRepository);
        Mockito.when(countryRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(country));
    }

    @Test
    public void testGetStateData()
    {
        Assert.assertThrows(Exception.class, ()-> countryService.getStateData(null));
        Assert.assertThrows(Exception.class, ()-> countryService.getStateData(""));

        Iterable<State> states = countryService.getStateData("MH");

        Assert.assertNotNull(states);
        Assert.assertTrue(states.iterator().hasNext());
    }

    @Test
    public void testCountrySeriesData()
    {
        SeriesData countrySeriesData = countryService.getCountrySeriesData();
        Assert.assertNotNull(countrySeriesData);
    }

    @Test
    public void tesGetCountrySummary()
    {
        OneDayData countrySummary = countryService.getCountrySummary(country.getId());
        Assert.assertNotNull(countrySummary);
    }

    @Test
    public void testGetAllLatestStateData()
    {
        Iterable<State> latestStateData = countryService.getLatestStateData(country.getId());

        Assert.assertNotNull(latestStateData);
        Assert.assertTrue(latestStateData.iterator().hasNext());
    }

    @Test
    public void testGetStateSummary()
    {
        List<StateSummary> stateSummary = countryService.getStateSummary(country.getId());
        Assert.assertNotNull(stateSummary);
        Assert.assertFalse(stateSummary.isEmpty());
    }

    @Test
    public void testGetStateSeriesData()
    {
        Assert.assertThrows(Exception.class, ()->countryService.getStateSeriesData(null));
        Assert.assertThrows(Exception.class, ()->countryService.getStateSeriesData(""));

        SeriesData stateSeriesData = countryService.getStateSeriesData("MH");
        Assert.assertNotNull(stateSeriesData);
    }

    @Test
    public void testGetAllLatestStateDataForState()
    {
        Assert.assertThrows(Exception.class, ()->countryService.getLatestStateData(country.getId(),null));
        Assert.assertThrows(Exception.class, ()->countryService.getLatestStateData(country.getId(),""));

        State latestStateData = countryService.getLatestStateData(country.getId(),"MH");
        Assert.assertNotNull(latestStateData);
    }

    @After
    public void cleanUp(){

    }

}
