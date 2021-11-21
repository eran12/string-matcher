package com.example.string_matcher.models

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class TextLocation(
    val lineOffset: Int,
    val charOffset: Int
)
