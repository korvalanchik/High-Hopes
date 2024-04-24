package com.example.highhopes.shortlink;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShortLinkRepositoryTest {

    @Autowired
    ShortLinkRepository shortLinkRepository;

    @Test
    void testFindByShortLinkWhenShortLinkValid() {
        String link = "http://localhost:8080/sl/abc123";
        ShortLink found = shortLinkRepository.findByShortLink(link);
        assertThat(found.getShortUrl()).isEqualTo(link);
    }

    @Test
    void testFindByShortLinkWhenShortLinkInvalid() {
        String invalidLink = "http://localhost:8080/sl/qwefdf457";
        ShortLink notFound = shortLinkRepository.findByShortLink(invalidLink);
        assertThat(notFound).isNull();
    }

    @Test
    void testFindByOriginalLinkWhenLinkValid() {
        String link = "https://app.slack.com/client/T060L949SJY/C06TVJGDCPK";
        ShortLink found = shortLinkRepository.findByOriginalLink(link);
        assertThat(found.getOriginalUrl()).isEqualTo(link);
    }
    @Test
    void testFindByOriginalLinkWhenLinkInvalid() {
        String invalidLink = "http://localhost:8080/sl/afcd1234";
        ShortLink notFound = shortLinkRepository.findByOriginalLink(invalidLink);
        assertThat(notFound).isNull();
    }

    @Test
    void testFindAllActiveShortLinksValid() {
        Long numberShortLink = 2L;
        String validLinkOne = "http://localhost:8080/sl/def456";
        String validLinkTwo = "http://localhost:8080/sl/qwe457";
        List<ShortLink> shortLinks = shortLinkRepository.findAllActiveShortLinks(1L);
        assertThat(numberShortLink).isEqualTo(shortLinks.size());
        assertThat(shortLinks.get(0).getShortUrl()).isEqualTo(validLinkOne);
        assertThat(shortLinks.get(1).getShortUrl()).isEqualTo(validLinkTwo);
    }

    @Test
    void testFindAllActiveShortLinksInvalid() {
        Long numberShortLinkValid = 0L;
        List<ShortLink> shortLinks = shortLinkRepository.findAllActiveShortLinks(3L);
        assertThat(numberShortLinkValid).isEqualTo(shortLinks.size());
    }
}