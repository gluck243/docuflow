package com.gluck.docuflow

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/docs")
class DocumentController(private val documentRepository: DocumentRepository) {

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): Document {
        val doc = Document()
        doc.filename = file.originalFilename
        doc.contentType = file.contentType
        doc.size = file.size
        doc.uploadDate = LocalDateTime.now()
        return documentRepository.save(doc)
    }

    @GetMapping
    fun getAllDocs(): List<Document> {
        return documentRepository.findAll()
    }

}