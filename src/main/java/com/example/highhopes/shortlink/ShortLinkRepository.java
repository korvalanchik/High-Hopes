package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {

    ShortLink findFirstByUser(User user);

    @Query(nativeQuery = true, value = "SELECT * FROM short_links sl WHERE sl.short_url = :short_url")
    ShortLink findByShortLink(@Param("short_url") String shortLink);
}
