package org.example.coviddashboard.country.beans;

import java.util.Date;
import java.util.Objects;

public class OneDayData {

    private Date date;
    private long confirmed;
    private long recovered;
    private long deceased;

    public OneDayData() {
    }

    public OneDayData(Date date, long confirmed, long recovered, long deceased) {
        this.date = date;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deceased = deceased;
    }

    public OneDayData(Date date, int confirmed, int recovered, int deceased) {
        this.date = date;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deceased = deceased;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(long confirmed) {
        this.confirmed = confirmed;
    }

    public long getRecovered() {
        return recovered;
    }

    public void setRecovered(long recovered) {
        this.recovered = recovered;
    }

    public long getDeceased() {
        return deceased;
    }

    public void setDeceased(long deceased) {
        this.deceased = deceased;
    }

    public long getActive()
    {
        return this.confirmed - this.recovered - this.deceased;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OneDayData)) return false;
        OneDayData that = (OneDayData) o;
        return confirmed == that.confirmed && recovered == that.recovered && deceased == that.deceased
                && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, confirmed, recovered, deceased);
    }

    @Override
    public String toString() {
        return "OneDayData{" +
                "date=" + date +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", deceased=" + deceased +
                '}';
    }
}
