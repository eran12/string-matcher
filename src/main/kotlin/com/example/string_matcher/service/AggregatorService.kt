package com.example.string_matcher.service

import com.example.string_matcher.models.FileModel
import com.example.string_matcher.models.TextLocation
import com.example.string_matcher.models.requests.AggregateRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

@Service
class AggregatorService(
    private val dictionaryService: DictionaryService,
    private val fileService: FileService
) {
    private val logger = KotlinLogging.logger {}
    private val namesMap = mutableMapOf<String, Set<TextLocation>>()
    private val isDone = AtomicBoolean(false)
    private val totalLinesCount = AtomicInteger(0)
    private val reduceSize = 50

    @Synchronized
    fun aggregateLines(request: AggregateRequest) {
        setNotDone()
        if (request.wordsMap.isNotEmpty()) {
            request.wordsMap.forEach {
                if (namesMap.containsKey(it.key)) {
                    namesMap[it.key] = namesMap[it.key]!!.plus(it.value)
                } else {
                    namesMap[it.key] = it.value
                }
            }
        }
        if (totalLinesCount.addAndGet(request.amountLinesRead) >= request.totalLines) {
            logger.info { totalLinesCount.get() }
            val filteredMap = filterMapWithApi()
            val nameKeys = namesMap.keys.toList()
            for (key in nameKeys) {
                if (!filteredMap.containsKey(key)) {
                    namesMap.remove(key)
                }
            }
            fileService.saveOne(
                FileModel(
                    request.filePath,
                    isDone = true,
                    words = namesMap.filter { it.value.isNotEmpty() })
            )
            setDone()
        } else {
            Thread.currentThread().interrupt()
        }
    }

    private fun filterMapWithApi(): Map<String, Set<TextLocation>> {
        logger.info { "${namesMap.size} to aggregate, starting to filter using dictionary api" }

        val filteredMap = namesMap.filter { dictionaryService.searchHasDefinitionWithRepository(it.key) }
//                this!!.filterNot { dictionaryService.searchIfWordHasNoDefinitionApi(it.key) }

        logger.info {
            "found regular words in API filtering: ${
                dictionaryService.getFoundedRegularWordsList().joinToString { "\"$it\"" }
            }"
        }
        logger.info {
            """
                |finished to filter with dictionary api
                | size after: ${filteredMap.size}
                | ${filteredMap.keys}
               """.trimMargin()
        }
        dictionaryService.persistKnownFoundWords()
        val sortedMap = filteredMap.toList().sortedBy { (k, v) -> v.size }
        return if (sortedMap.size > reduceSize) {
            sortedMap.subList(sortedMap.size.minus(reduceSize).minus(1), sortedMap.size.minus(1)).toMap()
        } else {
            sortedMap.toMap()
        }
    }

    fun getNamesMap(): MutableMap<String, Set<TextLocation>> {
        return namesMap
    }

    private fun setDone() {
        logger.info { "mark done" }
        isDone.set(true)
    }

    private fun setNotDone() {
        isDone.set(false)
    }

    fun isDone(): Boolean {
        return isDone.get()
    }
}