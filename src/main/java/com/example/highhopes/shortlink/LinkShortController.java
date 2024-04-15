package com.example.highhopes.shortlink;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LinkShortController {
    private final ShortLinkService shortLinkService;

    public LinkShortController(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @GetMapping("/{shortLink}")
    public String redirectToOriginalUrl(@PathVariable String shortLink, HttpServletRequest request,
                                        HttpServletResponse httpServletResponse) {
        String originalUrlCookie = shortLinkService.findCookie(request, shortLink);
        if (originalUrlCookie.equals("Not found")) {
            String originalUrl = shortLinkService.getOriginalUrl(httpServletResponse, shortLink);
            return "redirect:" + originalUrl;
        }
        return "redirect:" + originalUrlCookie;
    }
}