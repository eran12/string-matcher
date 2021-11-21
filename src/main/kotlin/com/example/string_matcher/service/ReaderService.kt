package com.example.string_matcher.service

import com.example.string_matcher.models.FileModel
import com.example.string_matcher.models.requests.ReadLinesRequest
import com.example.string_matcher.workers.Matcher
import mu.KotlinLogging
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import java.net.URI

@Service
class ReaderService(private val matcher: Matcher, private val fileService: FileService) {
    companion object {
        private val logger = KotlinLogging.logger { }
        const val linesCount = 1000
    }

    fun readFile(url: String?) {
        val filePair = readText(url)
        val lines = filePair.second
        val totalLines = lines.size.minus(1)
        val roundedRepeatTimes = lines.size.div(linesCount)
        val repeatTimes = if (roundedRepeatTimes.times(linesCount) < totalLines) roundedRepeatTimes.plus(1)
        else roundedRepeatTimes
        var currentCharsCount = 0
        fileService.saveOne(FileModel(filePath = filePair.first))

        repeat(repeatTimes) {
            val startRange = linesCount.times(it)
            val endRange = if (it == repeatTimes.minus(1)) totalLines else startRange.plus(linesCount)
            val linesRange = IntRange(start = startRange, endInclusive = endRange)
            val readiedLines = lines.slice(linesRange)
            val startCharsCount = currentCharsCount
            readiedLines.forEach { line -> currentCharsCount += line.length }
            matcher.searchInText(
                ReadLinesRequest(
                    charsCount = IntRange(startCharsCount, currentCharsCount),
                    linesCount = linesRange,
                    totalLines = totalLines,
                    lines = readiedLines,
                    filePath = filePair.first
                )
            )
        }
    }

    private fun readText(url: String?): Pair<String, List<String>> {
        return if ((url == null) || (url.lowercase() == "null")) {
            fileService.getResourceFilePath().toString() to fileService.getResourceFilePath().toFile().readLines()
        } else if (url.contains("http") && url.contains("txt")) {
            url to makeHttpCall(url)
        } else {
            throw RuntimeException("invalid url pattern: $url")
        }
    }

    private fun makeHttpCall(url: String): List<String> {
        val response = RestTemplateBuilder().build()
            .getForEntity(URI(url), String::class.java)
        return response.body.toString().split("\n")
    }
}
