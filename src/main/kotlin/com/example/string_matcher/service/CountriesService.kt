package com.example.string_matcher.service

import com.example.string_matcher.dictionary.listOfCountriesAndCities
import com.example.string_matcher.entity.Cities
import com.example.string_matcher.entity.Countries
import com.example.string_matcher.models.response.CountriesResponse
import com.example.string_matcher.repository.CountriesRepository
import com.example.string_matcher.service.extensions.toStringSet
import org.springframework.stereotype.Service

@Service
class CountriesService(private val countriesRepository: CountriesRepository?) {
    private lateinit var countriesAndCities: Set<String>
    fun saveAll(countriesResponse: CountriesResponse) {
        val countriesMap = mutableMapOf<String, Countries>()
        countriesResponse.data.forEach { entry ->
            if (countriesMap.containsKey(entry.country)) {
                val cities = countriesMap[entry.country]!!.cities
                    .plus(entry.cities.map { city -> Cities(city) }.toSet())
                countriesMap[entry.country] = Countries(entry.country, cities)
            } else {
                countriesMap[entry.country] = Countries(
                    entry.country,
                    entry.cities.map { city -> Cities(city) }.toSet()
                )
            }
        }
        countriesAndCities =
            countriesRepository?.saveAll(countriesMap.map { Countries(it.key, it.value.cities) })?.toStringSet()
                ?: countriesResponse.countryAndCitiesList.plus(listOfCountriesAndCities)
    }

    fun getCitiesFromRepository(): Set<String> {
        return countriesRepository?.findAll()?.toStringSet() ?: countriesAndCities
    }
}