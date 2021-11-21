package com.example.string_matcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class StringMatcherApplication

fun main(args: Array<String>) {
    runApplication<StringMatcherApplication>(*args)
}