package org.example.coviddashboard.country.state.beans;

import java.util.Objects;

public class StateSummary {

    private String name;
    //change
    private long newCases;
    private long newRecovered;
    private long newDeceased;

    //latest state data
    private State state;

    public StateSummary() {
    }

    public StateSummary(String name, long newCases, long newRecovered, long newDeceased, State state) {
        this.name = name;
        this.newCases = newCases;
        this.newRecovered = newRecovered;
        this.newDeceased = newDeceased;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNewCases() {
        return newCases;
    }

    public void setNewCases(long newCases) {
        this.newCases = newCases;
    }

    public long getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(long newRecovered) {
        this.newRecovered = newRecovered;
    }

    public long getNewDeceased() {
        return newDeceased;
    }

    public void setNewDeceased(long newDeceased) {
        this.newDeceased = newDeceased;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateSummary)) return false;
        StateSummary that = (StateSummary) o;
        return newCases == that.newCases && newRecovered == that.newRecovered && newDeceased == that.newDeceased && name.equals(that.name) && state.equals(that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, newCases, newRecovered, newDeceased, state);
    }

    @Override
    public String toString() {
        return "StateSummary{" +
                "name='" + name + '\'' +
                ", newCases=" + newCases +
                ", newRecovered=" + newRecovered +
                ", newDeceased=" + newDeceased +
                ", state=" + state +
                '}';
    }
}
