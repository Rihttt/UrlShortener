package ru.riht.model.projections;

import java.time.LocalDateTime;
import java.util.UUID;

public interface LinkDto {

    UUID getId();
    String getOriginalUrl();
    String getShortUrl();
    LocalDateTime getCreatedAt();
    int getClickCount();
}
