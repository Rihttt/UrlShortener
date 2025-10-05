package ru.riht.model.projections;

import java.time.LocalDateTime;
import java.util.UUID;

public interface LinkProjection {

    UUID getId();
    String getOriginalUrl();
    String getShortUrl();
    LocalDateTime getCreatedAt();
    int getClickCount();
}
