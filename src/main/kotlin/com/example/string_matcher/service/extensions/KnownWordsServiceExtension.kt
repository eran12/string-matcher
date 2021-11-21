package com.example.string_matcher.service.extensions

import com.example.string_matcher.entity.KnownWordsEntity

fun List<KnownWordsEntity>.toStringSet(): Set<String> {
    return this.map { it.word }.toSet()
}