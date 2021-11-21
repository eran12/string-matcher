package com.example.string_matcher.service.extensions

import com.example.string_matcher.entity.Countries
import com.example.string_matcher.entity.KnownWordsEntity

fun MutableMap<String, Countries>.toStringSet(): Set<String> {
    return this.flatMap {
        it.value.cities.mapTo(HashSet()) { city -> city.name }.plus(it.key)
    }.toSet()
}

fun List<Countries>.toStringSet(): Set<String> {
    return this.flatMap {
        it.cities.mapTo(HashSet()) { city -> city.name }.plus(it.country)
    }.toSet()
}
