package com.example.string_matcher.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = Cities.TABLE_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Cities(
    val name: String = ""
) {
    companion object {
        const val TABLE_NAME = "cities"
    }

    @Id
    @GeneratedValue
    val id: Long = 0
}