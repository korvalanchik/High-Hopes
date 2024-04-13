package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {

    ShortLink findFirstByUser(User user);

}
