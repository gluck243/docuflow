package com.gluck.docuflow;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public DocumentDTO upload(MultipartFile file) {
        Document doc = new Document();
        doc.setFilename(file.getOriginalFilename());
        doc.setContentType(file.getContentType());
        doc.setSize(file.getSize());
        doc.setUploadDate(LocalDateTime.now());
        Document saved = repository.save(doc);
        return new DocumentDTO(saved.getId(), saved.getFilename(), saved.getSize(), saved.getUploadDate());
    }

    public List<DocumentDTO> findAll() {
        return repository.findAll().stream()
                .map(doc -> new DocumentDTO(
                        doc.getId(),
                        doc.getFilename(),
                        doc.getSize(),
                        doc.getUploadDate()
                )).collect(Collectors.toList());
    }

}
