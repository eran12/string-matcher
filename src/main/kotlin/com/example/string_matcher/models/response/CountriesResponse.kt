package com.example.string_matcher.models.response

data class CountriesResponse(
    val error: Boolean,
    val msg: String,
    val data: Set<Country>,
    val countryAndCitiesList: Set<String> = data.flatMap { it.cities.plus(it.country) }.toSet(),
)

data class Country(
    val country: String,
    val cities: Set<String>
)