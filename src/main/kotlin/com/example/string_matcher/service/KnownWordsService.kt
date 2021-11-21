package com.example.string_matcher.service

import com.example.string_matcher.entity.KnownWordsEntity
import com.example.string_matcher.dictionary.listOfKnownWords
import com.example.string_matcher.repository.KnownWordsRepository
import com.example.string_matcher.service.extensions.toStringSet
import org.springframework.stereotype.Service

@Service
class KnownWordsService(private val knownWordsRepository: KnownWordsRepository?) {
    val knownWordsList: Set<String> = listOfKnownWords
    fun saveAll(words: Set<String>): Set<String> {
        return knownWordsRepository?.saveAll(words.map { KnownWordsEntity(it) })?.toStringSet() ?: listOfKnownWords
    }

    fun getKnownWords(): Set<String> {
        return knownWordsRepository?.findAll()?.toStringSet() ?: knownWordsList
    }
}