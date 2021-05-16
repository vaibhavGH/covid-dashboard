package org.example.coviddashboard.country.dao;

import org.example.coviddashboard.country.beans.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryRepository extends CrudRepository<Country, Integer> {
}
