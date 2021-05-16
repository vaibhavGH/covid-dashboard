package org.example.coviddashboard.country.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SeriesData {

    private List<Date> dateList = new ArrayList<>();
    private List<Long> confirmedList = new ArrayList<>();
    private List<Long> recoveredList = new ArrayList<>();
    private List<Long> deceasedList = new ArrayList<>();
    private List<Long> activeList = new ArrayList<>();



    public void addSeriesData (Date date, long confirmed, long recovered, long deceased, long active)
    {
        this.dateList.add(date);
        this.confirmedList.add(confirmed);
        this.recoveredList.add(recovered);
        this.deceasedList.add(deceased);
        this.activeList.add(active);

    }

    public List<Long> getConfirmedList() {
        return confirmedList;
    }

    public List<Long> getRecoveredList() {
        return recoveredList;
    }

    public List<Long> getDeceasedList() {
        return deceasedList;
    }

    public List<Long> getActiveList() {
        return activeList;
    }

    public List<Date> getDateList() {
        return dateList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeriesData)) return false;
        SeriesData that = (SeriesData) o;
        return dateList.equals(that.dateList) && confirmedList.equals(that.confirmedList) &&
                recoveredList.equals(that.recoveredList) && deceasedList.equals(that.deceasedList) &&
                activeList.equals(that.activeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateList, confirmedList, recoveredList, deceasedList, activeList);
    }

    @Override
    public String toString() {
        return "SeriesData{" +
                "dateList=" + dateList +
                ", confirmedList=" + confirmedList +
                ", recoveredList=" + recoveredList +
                ", deceasedList=" + deceasedList +
                ", activeList=" + activeList +
                '}';
    }
}
