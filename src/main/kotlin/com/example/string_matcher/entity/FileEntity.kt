package com.example.string_matcher.entity

import com.example.string_matcher.models.FileModel
import com.example.string_matcher.service.extensions.toWordsEntity
import com.fasterxml.jackson.annotation.JsonAutoDetect
import javax.persistence.*

@Entity
@Table(name = FileEntity.TABLE_NAME)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class FileEntity(
    @Id
    val path: String = "",
    @OneToMany(targetEntity = WordEntity::class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val wordsList: Collection<WordEntity> = emptySet(),
    val isDone: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "files"
    }

    constructor(file: FileModel) : this(
        path = file.filePath,
        isDone = file.isDone,
        wordsList = file.words.toWordsEntity(),
    )
}
