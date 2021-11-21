package com.example.string_matcher.service

import com.example.string_matcher.entity.FileEntity
import com.example.string_matcher.models.FileModel
import com.example.string_matcher.models.TextLocation
import com.example.string_matcher.repository.FileRepository
import com.example.string_matcher.service.extensions.toFileModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.nio.file.Paths

@Service
class FileService(private val fileRepository: FileRepository?) {
    private val projectPath: Path = Paths.get("").toAbsolutePath()
    private val filePath: Path = Paths.get(projectPath.toString(), "src", "main", "resources", "static", "big.txt")
    private var file: FileModel? = FileModel(filePath = filePath.toString(), words = emptyMap())
    fun saveOne(fileModel: FileModel) {
        if (fileRepository == null) {
            this.file = fileModel
        } else {
            fileRepository.save(FileEntity(fileModel))
        }
    }

    fun getOne(filePath: String?): ResponseEntity<FileModel> {
        val path = filePath ?: this.filePath.toString()
        return if (fileRepository == null) {
            ResponseEntity.ok(file)
        } else {
            if (fileRepository.existsById(path)) {
                ResponseEntity.ok(fileRepository.getById(path).toFileModel())
            } else {
                ResponseEntity.notFound().build()
            }
        }
    }

    fun updateDone(filePath: String, words: Map<String, Set<TextLocation>>) {
        if (fileRepository == null) {
            file = FileModel(filePath = filePath, isDone = true, words = words)
        } else {
            if (fileRepository.existsById(filePath)) {
                fileRepository.updateDone(filePath)
            }
        }
    }

    fun getResourceFilePath(): Path {
        return filePath
    }
}