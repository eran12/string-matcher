package com.example.string_matcher.configuration

import com.example.string_matcher.repository.FileRepository
import com.example.string_matcher.repository.KnownWordsRepository
import com.example.string_matcher.service.*
import com.example.string_matcher.workers.Matcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {

    @Bean
    fun getReaderService(matcher: Matcher, fileService: FileService): ReaderService {
        return ReaderService(matcher, fileService)
    }

    @Bean
    fun getDictionaryService(
        countriesService: CountriesService,
        knownWordsService: KnownWordsService
    ): DictionaryService {
        return DictionaryService(countriesService, knownWordsService)
    }

    @Bean
    fun getAggregatorService(dictionaryService: DictionaryService, fileService: FileService): AggregatorService {
        return AggregatorService(dictionaryService, fileService)
    }

    @Bean
    fun getMatcher(aggregatorService: AggregatorService): Matcher {
        return Matcher(aggregatorService)
    }

    @Bean
    fun getFileDao(fileRepository: FileRepository): FileService {
        return FileService(fileRepository)
    }

    @Bean
    fun getKnownWordsService(knownWordsRepository: KnownWordsRepository): KnownWordsService {
        return KnownWordsService(knownWordsRepository)
    }
}