package com.example.string_matcher.service.extensions

import com.example.string_matcher.entity.FileEntity
import com.example.string_matcher.entity.TextLocationEntity
import com.example.string_matcher.entity.WordEntity
import com.example.string_matcher.models.FileModel
import com.example.string_matcher.models.TextLocation

fun FileEntity.toFileModel(): FileModel {
    return FileModel(
        filePath = this.path,
        isDone = isDone,
        words = this.wordsList.associate {
            it.word to it.locations.mapTo(HashSet()) { location ->
                TextLocation(
                    location.lineOffset,
                    location.charOffset
                )
            }
        }
    )
}

fun TextLocation.toLocationEntity(): TextLocationEntity {
    return TextLocationEntity(
        lineOffset = lineOffset,
        charOffset = charOffset
    )
}

fun Set<TextLocation>.toLocationEntities(): Set<TextLocationEntity> {
    return this.mapTo(HashSet()) {
        it.toLocationEntity()
    }
}

fun Map<String, Set<TextLocation>>.toWordsEntity(): Set<WordEntity> {
    return this.filter { it.value.isNotEmpty() }.mapTo(HashSet()) {
        WordEntity(it.key, it.value.toLocationEntities())
    }.filter { it.locations.isNotEmpty() }.toSet()
}