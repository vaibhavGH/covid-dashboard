package org.example.coviddashboard.country.state.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.coviddashboard.country.beans.Country;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name="state")
public class State {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "state_table_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "name")
    private String name;
    @Column(name = "on_date")
    private Date date;

    private int confirmed;
    private int recovered;
    private int deceased;


    public State() { }

    public State(String name, int confirmed, int recovered, int deceased) {
        this.name = name;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deceased = deceased;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getDeceased() {
        return deceased;
    }

    public void setDeceased(int deceased) {
        this.deceased = deceased;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getActive() {
        return this.confirmed - this.recovered - this.deceased;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return id == state.id && confirmed == state.confirmed && recovered == state.recovered && deceased == state.deceased && country.equals(state.country) && name.equals(state.name) && date.equals(state.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country, name, date, confirmed, recovered, deceased);
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", country=" + country +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", confirmed=" + confirmed +
                ", recovered=" + recovered +
                ", deceased=" + deceased +
                '}';
    }
}
