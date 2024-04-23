package com.example.highhopes.shortlink;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sl")
public class ShortLinkRedirectController {

    private final ShortLinkService shortLinkService;

    public ShortLinkRedirectController(final ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @GetMapping("/{shortLink}")
    public String redirectShortUrl(@PathVariable String shortLink,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        GetOriginalUrlResponse url = shortLinkService.getOriginalUrlResponse(shortLink, request, response);

        if (!url.getError().equals(GetOriginalUrlResponse.Error.OK)) {
            return "redirect:/error";
        }

        return "redirect:" + url.getOriginalUrl();
    }
}
