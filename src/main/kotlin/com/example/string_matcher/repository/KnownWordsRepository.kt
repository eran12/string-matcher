package com.example.string_matcher.repository

import com.example.string_matcher.entity.KnownWordsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KnownWordsRepository: JpaRepository<KnownWordsEntity, String> {
}