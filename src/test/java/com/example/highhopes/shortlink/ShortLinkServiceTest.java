package com.example.highhopes.shortlink;

import com.example.highhopes.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.highhopes.shortlink.GetOriginalUrlResponse.Error.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
class ShortLinkServiceTest {
    GetOriginalUrlResponse originalUrlResponse;

    ShortLinkService shortLinkService;
    HttpServletRequest request;

    HttpServletResponse response;

    ShortLinkRepository shortLinkRepository;
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        originalUrlResponse = new GetOriginalUrlResponse();
        shortLinkService = new ShortLinkService(shortLinkRepository, userRepository);
    }

    @Test
    void testGetOriginalUrlWhenValidShortLink() {
        GetOriginalUrlResponse validResponse = new GetOriginalUrlResponse();
        validResponse.setError(OK);
        validResponse.setOriginalUrl("http://localhost:8080/api/abcd1234");

        String validLink = "abcd1234";

        originalUrlResponse = shortLinkService.getOriginalUrl(validLink, request, response);
        assertEquals(originalUrlResponse, validResponse);
    }
}
