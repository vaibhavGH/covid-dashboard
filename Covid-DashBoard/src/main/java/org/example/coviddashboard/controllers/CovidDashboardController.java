package org.example.coviddashboard.controllers;

import org.example.coviddashboard.country.beans.OneDayData;
import org.example.coviddashboard.country.beans.SeriesData;
import org.example.coviddashboard.country.services.CountryService;
import org.example.coviddashboard.country.state.beans.State;
import org.example.coviddashboard.country.state.beans.StateSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/covid19")
public class CovidDashboardController {

    @Autowired
    private CountryService countryService;

    @RequestMapping("/countries/{countryID}")
    public @ResponseBody Iterable<State> getAllLatestStateData(@PathVariable("countryID") int countryID)
    {
        return countryService.getLatestStateData(countryID);
    }
/*
    @RequestMapping(value = "/states/{state}",method = RequestMethod.GET)
    public @ResponseBody Iterable<State> getStateData(@PathVariable("state") String state)
    {
        return countryService.getStateData(state);
    }*/

    @RequestMapping(value = "/countries/{countryID}/states/{state}/latest",method = RequestMethod.GET)
    public @ResponseBody State getLatestStateData(@PathVariable("countryID") int countryID, @PathVariable("state") String state)
    {
        return countryService.getLatestStateData(countryID, state);
    }

    @RequestMapping(value = "/states/{state}/series",method = RequestMethod.GET)
    public @ResponseBody
    SeriesData getStateSeriesData(@PathVariable("state") String state)
    {
        return countryService.getStateSeriesData(state);

    }

    @RequestMapping(value = "/countries/{countryID}/states/summary",method = RequestMethod.GET)
    public @ResponseBody
    List<StateSummary> getStatesSummary(@PathVariable("countryID") int countryID)
    {
        return countryService.getStateSummary(countryID);
    }

    @RequestMapping(value = "/countries/series",method = RequestMethod.GET)
    public @ResponseBody
    SeriesData getStateData()
    {
        return countryService.getCountrySeriesData();
    }

    @RequestMapping(value = "/countries/{countryID}/summary",method = RequestMethod.GET)
    public @ResponseBody
    OneDayData getCountrySummary(@PathVariable("countryID") int countryID)
    {
        return countryService.getCountrySummary(countryID);
    }

}
