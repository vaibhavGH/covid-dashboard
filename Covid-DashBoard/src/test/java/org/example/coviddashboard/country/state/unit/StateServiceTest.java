package org.example.coviddashboard.country.state.unit;

import org.example.coviddashboard.country.beans.OneDayData;
import org.example.coviddashboard.country.state.beans.State;
import org.example.coviddashboard.country.state.dao.StateRepository;
import org.example.coviddashboard.country.state.services.StateService;
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
public class StateServiceTest {

    private static State state11 = new State("MH", 100, 100, 10);
    private static State state12 = new State("MH", 80, 80, 8);
    private static State state21 = new State("KL", 80, 80, 8);
    private static State state22 = new State("KL", 70, 70, 7);

    private static Date getDate(int year, int month, int day)
    {
        return new GregorianCalendar(year, month, day).getTime();
    }

    private static OneDayData oneDayData1 = new OneDayData(getDate(2021, 4, 27)
            ,100, 100, 10);
    private  static OneDayData oneDayData2 = new OneDayData(getDate(2021, 4, 26)
            ,80, 80, 8);

    static {
        state11.setDate(getDate(2021, 4, 27));
        state12.setDate(getDate(2021, 4, 26));
        state21.setDate(getDate(2021, 4, 27));
        state22.setDate(getDate(2021, 4, 26));
    }


    @Autowired
    private StateService stateService;

    @MockBean
    private StateRepository stateRepository;



    @Before
    public void setUp() {
        initializeMockedStateRepository(stateRepository);
    }

    public static void initializeMockedStateRepository(StateRepository stateRepository) {
        Mockito.when(stateRepository.findByCountry_idAndDate(Mockito.anyInt(), Mockito.isA(Date.class)))
                .thenReturn(Arrays.asList(state11, state21));
        Mockito.when(stateRepository.findAllByName(Mockito.anyString()))
                .thenReturn(Arrays.asList(state11, state12));
        Mockito.when(stateRepository.getCountryDataDayWise())
                .thenReturn(Arrays.asList(oneDayData1, oneDayData2));
        Mockito.when(stateRepository.getCountrySummary(Mockito.isA(Date.class)))
                .thenReturn(oneDayData1);
        Mockito.when(stateRepository.findByNameAndDate(Mockito.anyString(), Mockito.isA(Date.class)))
                .thenReturn(state11);
        Mockito.when(stateRepository.getStateDataDayWise(Mockito.anyString()))
                .thenReturn(Arrays.asList(oneDayData1, oneDayData2));
    }

    @Test
    public void testGetDataForAllStates()
    {
        Assert.assertThrows(Exception.class, ()-> stateService.getDataForAllStates(-1, null));
        Assert.assertThrows(Exception.class, ()-> stateService.getDataForAllStates(1, null));
        Assert.assertThrows(Exception.class, ()-> stateService.getDataForAllStates(-1,
                Calendar.getInstance().getTime()));

        List<State> states = stateService.getDataForAllStates(1, Calendar.getInstance().getTime());
        Assert.assertNotNull(states);
        Assert.assertFalse(states.isEmpty());

    }

    @Test
    public void testGetStateData()
    {
        Assert.assertThrows(Exception.class, ()-> stateService.getStateData(""));
        Assert.assertThrows(Exception.class, ()-> stateService.getStateData(null));

        Iterable<State> states = stateService.getStateData("MH");
        Assert.assertNotNull(states);
        Assert.assertTrue(states.iterator().hasNext());
    }

    @Test
    public void testGetCountryDataDayWise()
    {
        List<OneDayData> oneDayDataList = stateService.getCountryDataDayWise();

        Assert.assertNotNull(oneDayDataList);
        Assert.assertFalse(oneDayDataList.isEmpty());
    }

    @Test
    public void testGetCountrySummary()
    {
        Assert.assertThrows(Exception.class, () -> stateService.getCountrySummary(null));

        OneDayData summary = stateService.getCountrySummary(Calendar.getInstance().getTime());
        Assert.assertNotNull(summary);
    }

    @Test
    public void testGetLatestStateData()
    {
        Assert.assertThrows(Exception.class, ()-> stateService.getStateDataForDate(null, null));
        Assert.assertThrows(Exception.class,
                ()-> stateService.getStateDataForDate("", Calendar.getInstance().getTime()));
        Assert.assertThrows(Exception.class, ()-> stateService.getStateDataForDate("MH", null));

        State latestStateData = stateService.getStateDataForDate("MH", Calendar.getInstance().getTime());

        Assert.assertNotNull(latestStateData);
    }

    @Test
    public void testGetStateDataDayWise()
    {
        Assert.assertThrows(Exception.class, ()->stateService.getStateData(null));
        Assert.assertThrows(Exception.class, ()->stateService.getStateData(""));

        List<OneDayData> oneDayDataList = stateService.getStateDataDayWise("MH");
        Assert.assertNotNull(oneDayDataList);
        Assert.assertFalse(oneDayDataList.isEmpty());
    }

    public void cleanUp () { }


}
