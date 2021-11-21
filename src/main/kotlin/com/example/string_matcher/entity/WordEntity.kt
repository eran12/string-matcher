package com.example.string_matcher.entity

import com.fasterxml.jackson.annotation.JsonAutoDetect
import javax.persistence.*

@Entity
@Table(name = WordEntity.TABLE_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class WordEntity(
    val word: String = "",
    @OneToMany(targetEntity = TextLocationEntity::class, cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @Column(name = LOCATION_COLUMN_NAME)
    val locations: Collection<TextLocationEntity> = emptySet()
) {
    companion object {
        const val TABLE_NAME = "found_words"
        const val LOCATION_COLUMN_NAME = "found_words"
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}