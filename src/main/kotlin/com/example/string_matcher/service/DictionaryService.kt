package com.example.string_matcher.service

import com.example.string_matcher.dictionary.listOfKnownWords
import com.example.string_matcher.dictionary.listOfMonths
import com.example.string_matcher.models.response.CountriesResponse
import mu.KotlinLogging
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.exchange
import java.net.URI

@Service
class DictionaryService(
    private val countriesService: CountriesService,
    private val knownWordsService: KnownWordsService
) {
    private val logger = KotlinLogging.logger {}

    private val dictionaryApiPath = "https://api.dictionaryapi.dev/api/v2/entries/en"
    private val placesApiPath = "https://countriesnow.space/api/v0.1/countries"
    private val foundRegularWords = mutableSetOf<String>()
    private val restTemplateBuilder: RestTemplateBuilder = RestTemplateBuilder()

    init {
        knownWordsService.saveAll(listOfKnownWords)
        getPlaces()
    }

    fun searchIfWordHasNoDefinitionApi(word: String): Boolean {
        kotlin.runCatching {
            return if (getFromDictionary(word).statusCode == HttpStatus.NOT_FOUND) {
                true
            } else {
                foundRegularWords.add(word)
                return false
            }
        }.onFailure {
            return when (it) {
                is HttpClientErrorException.NotFound -> false
                is HttpClientErrorException.TooManyRequests -> {
                    logger.info { "going to sleep and start soon.. :)" }
                    Thread.sleep(100000L)
                    searchIfWordHasNoDefinitionApi(word)
                }
                else -> false
            }
        }.onSuccess {
            foundRegularWords.add(word)
            return false
        }
        logger.info { "not send request returning word as is: $word" }
        return false
    }

    fun searchHasDefinitionWithRepository(word: String): Boolean {
        return knownWordsService.getKnownWords().none { it.lowercase() == word.lowercase() }
                && countriesService.getCitiesFromRepository().none { it.lowercase().contains(word.lowercase()) }
                && listOfMonths.none { it.lowercase() == word.lowercase() }
                && foundRegularWords.none { it.lowercase() == word.lowercase() }
    }

    private fun getFromDictionary(word: String): ResponseEntity<String> {
        return restTemplateBuilder.build()
            .exchange(RequestEntity<String>(HttpMethod.GET, URI("$dictionaryApiPath/$word")))
    }

    private fun getPlaces() {
        val response = restTemplateBuilder.build()
                .exchange<CountriesResponse>(
                    RequestEntity<String>(
                        HttpMethod.GET,
                        URI(placesApiPath)
                    )
                ).body!!

        countriesService.saveAll(response)
    }

    fun getFoundedRegularWordsList(): Set<String> {
        return foundRegularWords
    }

    fun persistKnownFoundWords() {
        val inserted = knownWordsService.saveAll(foundRegularWords.toSet())
        foundRegularWords.removeAll(inserted)
    }
}