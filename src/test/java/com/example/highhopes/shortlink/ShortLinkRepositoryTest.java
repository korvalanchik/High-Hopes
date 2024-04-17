package com.example.highhopes.shortlink;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShortLinkRepositoryTest {

    @Autowired
    ShortLinkRepository shortLinkRepository;

    @Test
    void testFindByShortLinkWhenShortLinkValid() {
        String link = "abc123";
        ShortLink found = shortLinkRepository.findByShortLink(link);
        assertThat(found.getShortUrl()).isEqualTo(link);
    }

    @Test
    void testFindByShortLinkWhenShortLinkInvalid() {
        String invalidLink = "qwe23";
        ShortLink notFound = shortLinkRepository.findByShortLink(invalidLink);
        assertThat(notFound).isNull();
    }

    @Test
    void testFindByShortLinkWhenActiveFalse() {
        String invalidLink = "def456";
        ShortLink shortLinkNotActive = shortLinkRepository.findByShortLink(invalidLink);
        assertThat(shortLinkNotActive).isNull();
    }
}