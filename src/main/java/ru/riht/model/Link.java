package ru.riht.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "links")
public class Link {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "original_url", unique = true, nullable = false)
    private String originalUrl;

    @Column(name = "short_code", unique = true, nullable = false)
    private String shortCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "click_count", nullable = false)
    private int clickCount;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "qrcode_id")
    private UUID qrCodeId;
}
