package com.example.highhopes.shortlink;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortLinkServiceTest {

    @Mock
    private ShortLinkRepository shortLinkRepository;

    @InjectMocks
    private ShortLinkService shortLinkService;

    @Test
    void ShortLinkServiceFindAllReturnsListOfShortLinkDTO() {
        List<ShortLink> shortLinks = new ArrayList<>();
        shortLinks.add(new ShortLink());
        shortLinks.add(new ShortLink());
        when(shortLinkRepository.findAll(Sort.by("id"))).thenReturn(shortLinks);

        List<ShortLinkDTO> foundShortLinks = shortLinkService.findAll();

        assertThat(foundShortLinks).isNotEmpty();
        assertThat(foundShortLinks.size()).isEqualTo(shortLinks.size());
    }

    @Test
    void ShortLinkService_Get_ReturnsShortLinkDTO() {
        Long id = 1L;
        ShortLink shortLink = new ShortLink();
        when(shortLinkRepository.findById(id)).thenReturn(Optional.of(shortLink));

        ShortLinkDTO foundShortLink = shortLinkService.get(id);

        assertThat(foundShortLink).isNotNull();
    }

    @Test
    void testHandleShortLink() {
        ShortLink shortLinkDb = new ShortLink();
        shortLinkDb.setExpiryDate(OffsetDateTime.now().minusDays(1));

        GetOriginalUrlResponse originalUrlResponse = ReflectionTestUtils.invokeMethod(shortLinkService,
                "handleShortLink", shortLinkDb);

        assert originalUrlResponse != null;
        assertEquals(originalUrlResponse.getError(), GetOriginalUrlResponse.Error.LINK_NOT_ACTIVE);
    }

    @Test
    void testIncrementClicks() {
        ShortLink shortLink = new ShortLink();
        shortLink.setClicks(0);

        ReflectionTestUtils.invokeMethod(shortLinkService, "incrementClicks", shortLink);

        assertEquals(shortLink.getClicks(), 1);
    }
}
