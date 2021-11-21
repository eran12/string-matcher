package com.example.string_matcher.repository

import com.example.string_matcher.entity.Countries
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CountriesRepository : JpaRepository<Countries, String>