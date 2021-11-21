package com.example.string_matcher.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javax.persistence.*

@Entity
@Table(name = TextLocationEntity.TABLE_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class TextLocationEntity(
    val lineOffset: Int = 0,
    val charOffset: Int = 0
) {
    companion object {
        const val TABLE_NAME = "text_location"
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}