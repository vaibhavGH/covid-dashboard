package org.example.coviddashboard.country.state.dao;

import org.example.coviddashboard.country.beans.OneDayData;
import org.example.coviddashboard.country.state.beans.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {

    Iterable<State> findBycountry_id(int countryId);

    List<State> findByCountry_idAndDate(int countryId, Date date);

    Iterable<State> findAllByName(String state);

    @Query("select new org.example.coviddashboard.country.beans.OneDayData(s.date as date, " +
            " sum(s.confirmed) as confirmed," +
            " sum(s.recovered) as recovered," +
            " sum(s.deceased) as deceased)" +
            " from state s " +
            " group by s.date order by s.date asc")
    List<OneDayData> getCountryDataDayWise();

    @Query("select new org.example.coviddashboard.country.beans.OneDayData(s.date as date, " +
            " sum(s.confirmed) as confirmed," +
            " sum(s.recovered) as recovered," +
            " sum(s.deceased) as deceased)" +
            " from state s where s.date = :date" +
            " group by s.date")
    OneDayData getCountrySummary(@Param("date") Date date);
/*
    @Query("select new org.example.coviddashboard.country.beans.OneDayData(s.date as date, " +
            " s.confirmed as confirmed," +
            " s.recovered as recovered," +
            " s.deceased as deceased)" +
            " from state s where s.name =:state and s.date = :date " +
            " ")
    OneDayData getLatestStateData(@Param("state")String state, @Param("date")Date date);*/

    State findByNameAndDate(String state, Date date);

    @Query("select new org.example.coviddashboard.country.beans.OneDayData(s.date as date, " +
            " sum(s.confirmed) as confirmed," +
            " sum(s.recovered) as recovered," +
            " sum(s.deceased) as deceased)" +
            " from state s where s.name =:state" +
            " group by s.date order by s.date asc")
    List<OneDayData> getStateDataDayWise(@Param("state")String state);
}
