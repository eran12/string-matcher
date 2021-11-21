package com.example.string_matcher.controllres

import com.example.string_matcher.service.FileService
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController(value = "files")
class FileController(private val fileService: FileService) {
    @GetMapping("/getNames")
    fun getFile(@RequestParam("filePath") filePath: String?): ResponseEntity<*> {
        return fileService.getOne(filePath)
    }
}