package com.example.string_matcher.models.requests

import com.example.string_matcher.models.TextLocation

data class AggregateRequest(
    val wordsMap: Map<String, Set<TextLocation>>,
    val totalLines: Int,
    val amountLinesRead: Int,
    val filePath: String
)