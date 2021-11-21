package com.example.string_matcher.workers

import com.example.string_matcher.models.TextLocation
import com.example.string_matcher.models.requests.AggregateRequest
import com.example.string_matcher.models.requests.ReadLinesRequest
import com.example.string_matcher.service.AggregatorService
import mu.KotlinLogging
import kotlin.concurrent.thread

class Matcher(private val aggregatorService: AggregatorService) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun searchInText(request: ReadLinesRequest) {
        thread {
            val wordsMap = mutableMapOf<String, Set<TextLocation>>()
            var charsCount = request.charsCount.first
            request.lines.forEachIndexed { lineIndex, line ->
                if (line.isNotBlank()) {
                    line.split(" ").forEach { word ->
                        val isFirstName = "\\b([A-Z][a-z ]+[ ]*)+".toRegex().matches(word)
                        if (word.length > 1 && isFirstName) {
                            val checkedWord = if (word.contains("'s")) {
                                word.substringAfterLast("'s")
                            } else word
                            if (wordsMap.containsKey(checkedWord)) {
                                wordsMap[checkedWord] = wordsMap[checkedWord]!!.plus(
                                    TextLocation(
                                        // adding 1 to lines count because file do not start at 0
                                        lineOffset = request.linesCount.first.plus(lineIndex.plus(1)),
                                        charOffset = charsCount
                                    )
                                )
                            } else {
                                wordsMap[checkedWord] = setOf(
                                    TextLocation(
                                        // adding 1 to lines count because file do not start at 0
                                        lineOffset = request.linesCount.first.plus(lineIndex.plus(1)),
                                        charOffset = charsCount
                                    )
                                )
                            }
                        }
                        // bump count and include spaces
                        charsCount += word.length + 1
                    }
                }
            }
            logger.info { wordsMap.keys }
            logger.info { "found in current range  ${wordsMap.size}" }
            aggregatorService.aggregateLines(
                AggregateRequest(
                    wordsMap = wordsMap,
                    totalLines = request.totalLines,
                    amountLinesRead = request.linesCount.last.minus(request.linesCount.first),
                    filePath = request.filePath
                )
            )
        }
    }
}