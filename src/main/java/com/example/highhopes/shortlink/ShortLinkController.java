package com.example.highhopes.shortlink;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{shortLink}")
public class ShortLinkController {
    private final ShortLinkService shortLinkService;

    public ShortLinkController(final ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @GetMapping()
    public ResponseEntity<String> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletRequest request,
                                                HttpServletResponse response) {
        String originalUrl = shortLinkService.getOriginalUrl(shortLink, request, response);

        if (originalUrl.equals("link not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(originalUrl);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(originalUrl);
    }
}