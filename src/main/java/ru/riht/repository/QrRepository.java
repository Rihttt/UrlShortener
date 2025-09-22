package ru.riht.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.riht.model.QrCode;
import ru.riht.model.projections.QrCodeDto;

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
    QrCodeDto findByUrl(@Param("urlId")UUID urlId);
}
