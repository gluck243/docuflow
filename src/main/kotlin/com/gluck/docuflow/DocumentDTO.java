package com.gluck.docuflow;

import java.time.LocalDateTime;

public record DocumentDTO(
        Long id,
        String filename,
        Long size,
        LocalDateTime uploadData
) {}
