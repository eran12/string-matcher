package com.example.string_matcher.models

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
class FileModel(
    val filePath: String,
    val words: Map<String, Set<TextLocation>> = emptyMap(),
    val isDone: Boolean = false
) {
    fun copy(
        words: Map<String, Set<TextLocation>>,
        isDone: Boolean = this.isDone
    ): FileModel {
        return FileModel(filePath, words, isDone)
    }
}