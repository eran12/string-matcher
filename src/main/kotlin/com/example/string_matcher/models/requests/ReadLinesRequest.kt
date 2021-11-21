package com.example.string_matcher.models.requests

data class ReadLinesRequest(
    val charsCount: IntRange,
    val linesCount: IntRange,
    val totalLines: Int,
    val lines: List<String>,
    val filePath: String
)
