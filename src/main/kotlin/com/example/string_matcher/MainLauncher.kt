package com.example.string_matcher

import com.example.string_matcher.service.*
import com.example.string_matcher.workers.Matcher
import mu.KotlinLogging

class MainLauncher

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val countriesService = CountriesService(null)
    val knownWordsService = KnownWordsService(null)
    val fileService = FileService(null)
    val dictionaryService = DictionaryService(countriesService, knownWordsService)
    val aggregatorService = AggregatorService(dictionaryService, fileService)
    val matcher = Matcher(aggregatorService)
    val readerService = ReaderService(matcher, fileService)
    readerService.readFile(null)
    val logger = KotlinLogging.logger { }
    logger.info { "main" }
    var shouldWaiteForAggregating = aggregatorService.isDone().not()
    while (shouldWaiteForAggregating) {
        Thread.sleep(10000L)
        if (aggregatorService.isDone()) {
            shouldWaiteForAggregating = false
            logger.info { "done and found: ${aggregatorService.getNamesMap()}" }
            logger.info { "done and found: ${aggregatorService.getNamesMap().values.map { it.size }.joinToString()}" }
            logger.info { "done and found: ${aggregatorService.getNamesMap().size}" }
            logger.info { "done reading all lines now filtering with API" }
            val end = System.currentTimeMillis()
            logger.info { "done filter text completely" }
            logger.info { "time: ${end - start}" }
        }
    }
}