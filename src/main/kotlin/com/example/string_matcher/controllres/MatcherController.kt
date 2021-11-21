package com.example.string_matcher.controllres

import com.example.string_matcher.service.ReaderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController(value = "matcher")
class MatcherController(private val readerService: ReaderService) {
    @GetMapping("/read")
    fun readText(@RequestParam("url") url: String?) {
        return readerService.readFile(url)
    }
}