package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {

    ShortLink findFirstByUser(User user);
    @Query(nativeQuery = true, value = "SELECT * FROM short_links sl WHERE sl.short_url = :short_url AND status = true")
    ShortLink findByShortLink(@Param("short_url") String shortLink);

    @Query(nativeQuery = true, value = "SELECT * FROM short_links sl WHERE sl.original_url = :original_url")
    ShortLink findByOriginalLink(@Param("original_url") String shortLink);

    @Query("SELECT sl FROM ShortLink sl WHERE sl.active = true AND sl.user.id = :userId")
    List<ShortLink> findAllActiveShortLinks(@Param("userId") Long userId);

    List<ShortLink> findByUserId(Long userId);
}
