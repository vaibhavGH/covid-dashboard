package org.example.coviddashboard.country.services;

import lombok.NonNull;
import org.example.coviddashboard.country.beans.Country;
import org.example.coviddashboard.country.beans.OneDayData;
import org.example.coviddashboard.country.beans.SeriesData;
import org.example.coviddashboard.country.dao.CountryRepository;
import org.example.coviddashboard.country.state.beans.State;
import org.example.coviddashboard.country.state.beans.StateSummary;
import org.example.coviddashboard.country.state.services.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CountryService {

    @Autowired
    private StateService stateService;

    @Autowired
    private CountryRepository countryRepository;

    public Iterable<State> getLatestStateData(int countryId)
    {
        Iterable<State> latestStateData = null;

        Optional<Country> countryOptional = countryRepository.findById(countryId);
        if(countryOptional.isPresent())
        {
            Country country = countryOptional.get();

            latestStateData = stateService.getDataForAllStates(country.getId(), country.getMaxDate());
        }
        return latestStateData;
    }

    public Iterable<State> getStateData(@NonNull String state) {
        if(state.isEmpty())
            throw new IllegalArgumentException("Invalid state");

        return stateService.getStateData(state);
    }

    public State getLatestStateData(int countryId, @NonNull String state) {

        if(state.isEmpty())
            throw new IllegalArgumentException("Invalid state");


        State latestStateData = null;

        Optional<Country> countryOptional = countryRepository.findById(countryId);
        List<StateSummary> statesSummary = null;
        if(countryOptional.isPresent()) {
            Country country = countryOptional.get();
            latestStateData = stateService.getStateDataForDate(state, country.getMaxDate());
        }
        return latestStateData;

    }

    public List<StateSummary> getStateSummary(int countryId) {


        Optional<Country> countryOptional = countryRepository.findById(countryId);
        List<StateSummary> statesSummary = null;
        if(countryOptional.isPresent())
        {
            Country country = countryOptional.get();


            List<State> latest = stateService.getDataForAllStates(country.getId(), country.getMaxDate());


            Calendar c = Calendar.getInstance();
            c.setTime(country.getMaxDate());
            c.add(Calendar.DATE, -1);
            List<State> old = stateService.getDataForAllStates(country.getId(), c.getTime());

            Map<String, State> map = latest.stream().collect(Collectors.toMap(State::getName, s -> s));


            statesSummary = new ArrayList<>();
            StateSummary stateSummary;
            State stateLatest;
            for(State state : old)
            {
                stateLatest = map.get(state.getName());
                stateSummary = new StateSummary(state.getName(), stateLatest.getConfirmed()-state.getConfirmed(),
                        stateLatest.getRecovered()-state.getRecovered(), stateLatest.getDeceased()-state.getDeceased(), stateLatest);

                statesSummary.add(stateSummary);
            }
            //statesSummary = stateService.getLatestDataForAllStates(country.getId(), country.getMaxDate());
        }
        return statesSummary;
    }

    public SeriesData getCountrySeriesData() {

        SeriesData countrySeriesData = new SeriesData();
        List<OneDayData> dataSeries = stateService.getCountryDataDayWise();

        dataSeries.forEach(d -> countrySeriesData.addSeriesData(d.getDate(), d.getConfirmed(), d.getRecovered(), d.getDeceased(), d.getActive()) );

        return countrySeriesData;
    }

    public OneDayData getCountrySummary(int countryId) {
        OneDayData countrySummary = null;
        Optional<Country> countryOptional = countryRepository.findById(countryId);
        if(countryOptional.isPresent()) {
            Country country = countryOptional.get();


            countrySummary = stateService.getCountrySummary(country.getMaxDate());
        }
        return countrySummary;
    }

    public SeriesData getStateSeriesData(@NonNull String state) {

        if(state.isEmpty())
            throw new IllegalArgumentException("Invalid state");

        SeriesData stateSeriesData = new SeriesData();
        List<OneDayData> dataSeries = stateService.getStateDataDayWise(state);

        dataSeries.forEach(d -> stateSeriesData.addSeriesData(d.getDate(), d.getConfirmed(), d.getRecovered(), d.getDeceased(), d.getActive()) );

        return stateSeriesData;
    }


}
