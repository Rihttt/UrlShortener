package ru.riht.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.riht.model.Link;

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

}