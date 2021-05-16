package org.example.coviddashboard.country.state.services;

import lombok.NonNull;
import org.example.coviddashboard.country.beans.OneDayData;
import org.example.coviddashboard.country.state.beans.State;
import org.example.coviddashboard.country.state.dao.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    public List<State> getDataForAllStates(int countryId, @NonNull Date date)
    {
        if(countryId <= 0)
            throw new IllegalArgumentException("Invalid country Id");

        return stateRepository.findByCountry_idAndDate(countryId, date);
    }

    public Iterable<State> getStateData(@NonNull String state) {

        if(state.isEmpty())
            throw new IllegalArgumentException("Invalid state");

        return stateRepository.findAllByName(state);
    }

    public List<OneDayData> getCountryDataDayWise()
    {
        return stateRepository.getCountryDataDayWise();
    }

    public OneDayData getCountrySummary(@NonNull Date date) {
        return stateRepository.getCountrySummary(date);
    }

    public State getStateDataForDate(@NonNull String state, @NonNull Date date) {
        if(state.isEmpty())
            throw new IllegalArgumentException("Invalid state");

        return stateRepository.findByNameAndDate(state, date);
    }

    public List<OneDayData> getStateDataDayWise(@NonNull String state) {
        if(state.isEmpty())
            throw new IllegalArgumentException("Invalid state");

        return stateRepository.getStateDataDayWise(state);
    }

}
