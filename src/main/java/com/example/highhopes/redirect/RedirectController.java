package com.example.highhopes.redirect;

import com.example.highhopes.shortlink.GetOriginalUrlResponse;
import com.example.highhopes.shortlink.ShortLinkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/short")
public class RedirectController {
    private final ShortLinkService shortLinkService;

    public RedirectController(final ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @GetMapping("/{short}")
    public RedirectView redirectToOriginalServer(@PathVariable("short") String link) {

        GetOriginalUrlResponse originalUrl = shortLinkService.getOriginalUrl(link);
        shortLinkService.incrementClicks(link);

        return new RedirectView(originalUrl.getOriginalUrl());
    }
}
