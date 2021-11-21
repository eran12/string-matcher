package com.example.string_matcher.service

import com.example.string_matcher.entity.KnownWordsEntity
import com.example.string_matcher.repository.KnownWordsRepository
import org.springframework.stereotype.Service

@Service
class WordService(private val wordsRepository: KnownWordsRepository) {
    fun insertWords(words: Set<String>): List<KnownWordsEntity> {
        return wordsRepository.saveAll(words.map { KnownWordsEntity(it) })
    }

    fun isWordExists(word: String): Boolean {
        return wordsRepository.findById(word).isEmpty
    }

    fun getWords(words: Set<String>): MutableList<KnownWordsEntity> {
        return wordsRepository.findAllById(words)
    }
}