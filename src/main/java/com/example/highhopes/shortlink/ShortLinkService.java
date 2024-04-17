package com.example.highhopes.shortlink;

import com.example.highhopes.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkService {
    private final ShortLinkRepository shortLinkRepository;
    private final CookieUtils cookieUtils = new CookieUtils();

    public ShortLinkService(final ShortLinkRepository shortLinkRepository) {
        this.shortLinkRepository = shortLinkRepository;
    }

    public String getOriginalUrl(String shortLink, HttpServletRequest request, HttpServletResponse response) {
        String resultLink = "link not found";
        String linkCookie = cookieUtils.findCookie(request, shortLink);
        ShortLink link = shortLinkRepository.findByShortLink(shortLink);
        if (link != null) {
            if (linkCookie.equals("Not found")) {
                response.addCookie(cookieUtils.createCookie(shortLink, link.getOriginalUrl()));
                resultLink = link.getOriginalUrl();
            } else {
                resultLink = linkCookie;
            }
            incrementClicks(link);
        }
        return resultLink;
    }

    private void incrementClicks(ShortLink link) {
        link.setClicks(link.getClicks() + 1);
        shortLinkRepository.save(link);
    }
}