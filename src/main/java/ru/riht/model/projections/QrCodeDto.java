package ru.riht.model.projections;

import java.util.UUID;

public interface QrCodeDto {
    UUID getId();
    String getUrl();
    byte[] getImageData();
    UUID getUrlId();
}
