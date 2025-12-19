package com.gluck.docuflow;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepository repository;
    private final VectorStore vectorStore;

    public DocumentService(DocumentRepository repository, VectorStore vectorStore) {
        this.repository = repository;
        this.vectorStore = vectorStore;
    }

    public DocumentDTO upload(MultipartFile file) {

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf"))
            throw new InvalidFileException("Only PDF files are allowed. You have submitted " + contentType);

        Document doc = new Document();
        doc.setFilename(file.getOriginalFilename());
        doc.setContentType(file.getContentType());
        doc.setSize(file.getSize());
        doc.setUploadDate(LocalDateTime.now());
        Document saved = repository.save(doc);

        try {
            var pdfResource = new ByteArrayResource(file.getBytes());
            var reader = new PagePdfDocumentReader(pdfResource);
            List<org.springframework.ai.document.Document> rawChunks = reader.get();
            List<org.springframework.ai.document.Document> cleanChunks = rawChunks.stream()
                    .filter(chunk -> chunk.getContent() != null && !chunk.getContent().isBlank())
                    .map(chunk -> new org.springframework.ai.document.Document(chunk.getContent(), Map.of()))
                    .toList();
            var splitter = new TokenTextSplitter(300, 30, 5, 10000, true);
            List<org.springframework.ai.document.Document> splitChunks = splitter.apply(cleanChunks);

            // Store in Vector DB
            vectorStore.add(splitChunks);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process AI embedding", e);
        }

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
