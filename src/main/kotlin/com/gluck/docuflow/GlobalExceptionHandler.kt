package com.gluck.docuflow

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(InvalidFileException::class)
    fun handleInvalidFile(ex: InvalidFileException): ResponseEntity<Map<String, Any>> {
        val errorResponse = mapOf(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Invalid File Type",
            "message" to (ex.message ?: "Unknown error")
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}