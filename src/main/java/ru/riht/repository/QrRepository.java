package ru.riht.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.riht.model.QrCode;
import ru.riht.model.projections.QrCodeProjection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface QrRepository extends JpaRepository<QrCode, UUID> {

    @Query("""
    SELECT q.id as id,
           q.url as url,
           q.imageData as imageData,
           q.urlId as urlId
    FROM QrCode q
    WHERE q.urlId = :urlId
    """)
    QrCodeProjection findByUrl(@Param("urlId")UUID urlId);

    @Modifying
    @Query(""" 
        DELETE FROM QrCode q WHERE q.urlId IN
        (SELECT l.id FROM Link l WHERE l.createdAt < :deletionDate)
        """)
    int deleteByUrlIdInOldLinks(@Param("deletionDate") LocalDateTime deletionDate);
}
