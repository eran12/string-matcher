package com.example.string_matcher.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = KnownWordsEntity.TABLE_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class KnownWordsEntity(
    @Id
    val word: String = ""
) {
    companion object {
        const val TABLE_NAME = "known_words"
    }
}