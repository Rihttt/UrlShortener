package ru.riht.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "qr_codes")
public class QrCode {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    @Column(name = "url_id", nullable = false)
    private UUID urlId;

 }
