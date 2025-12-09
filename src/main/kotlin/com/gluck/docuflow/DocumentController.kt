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
class DocumentController(private val  documentService: DocumentService) {

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): DocumentDTO {
        return documentService.upload(file)
    }

    @GetMapping
    fun getAllDocs(): List<DocumentDTO> {
        return documentService.findAll()
    }

}