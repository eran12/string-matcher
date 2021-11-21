package com.example.string_matcher.repository

import com.example.string_matcher.entity.FileEntity
import com.example.string_matcher.entity.WordEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : JpaRepository<FileEntity, String> {
    @Query("UPDATE FileEntity SET isDone=true WHERE path=?1")
    fun updateDone(id: String): FileEntity

    @Query("UPDATE FileEntity SET wordsList=?2 WHERE path=?1")
    fun updateWords(id: String, wordsList: Set<WordEntity>): FileEntity
}