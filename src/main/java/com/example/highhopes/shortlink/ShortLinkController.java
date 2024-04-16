package com.example.highhopes.shortlink;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/{shortLink}")
public class ShortLinkController {
    private final ShortLinkService shortLinkService;

    public ShortLinkController(final ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @GetMapping()
    public String redirectToOriginalUrl(@PathVariable String shortLink, HttpServletRequest request,
                                        HttpServletResponse response) {
        String originalUrl = shortLinkService.getOriginalUrl(shortLink, request, response);
        return "redirect:" + originalUrl;
    }
}