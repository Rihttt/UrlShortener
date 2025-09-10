package ru.riht.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LinkDto {

    private UUID id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private int clickCount;
}
