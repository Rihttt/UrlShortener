package ru.riht.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.riht.model.Link;
import ru.riht.model.projections.LinkProjection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {
    @Query("""
        SELECT l.originalUrl
        FROM Link l
        WHERE l.shortCode = :shortCode
    """)
    String findByShortCode(
            @Param("shortCode") String shortCode
    );

    @Modifying
    @Query("""
        UPDATE Link l
        SET l.clickCount = l.clickCount + 1
        WHERE l.shortCode = :shortCode
    """)
    void updateClickCount(
            @Param("shortCode") String shortCode
    );

    @Modifying
    @Query("""
        UPDATE Link l
            SET l.qrCodeId = :qrCodeId
        WHERE l.id = :urlId
    """)
    void updateQr(
            @Param("urlId") UUID urlId,
            @Param("qrCodeId") UUID qrCodeId
    );

    @Query("""
        SELECT l.id as id,
        l.originalUrl as originalUrl,
        concat(:shortUrlPrefix,l.shortCode) as shortUrl,
        l.createdAt as createdAt,
        l.clickCount as clickCount
        FROM Link l
        WHERE l.userId = :userId
        ORDER BY l.createdAt DESC
    """)
    List<LinkProjection> findUserLinks(
            @Param("userId")UUID userId,
            @Param("shortUrlPrefix") String shortUrlPrefix
    );

    @Modifying
    @Query("""
        DELETE FROM Link l WHERE l.createdAt < :deletionDate
    """)
    int deleteByCreatedAtBefore(@Param("deletionDate") LocalDateTime deletionDate);
}