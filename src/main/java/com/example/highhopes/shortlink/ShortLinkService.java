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
        String resultLink = "Short link not found";
        String linkCookie = cookieUtils.findCookie(request, shortLink);
        if (linkCookie.equals("Not found")) {
            ShortLink linkDb = shortLinkRepository.findByShortLink(shortLink);
            if (linkDb != null) {
                response.addCookie(cookieUtils.createCookie(shortLink, linkDb.getOriginalUrl()));
                linkDb.setClicks(linkDb.getClicks() + 1);
                shortLinkRepository.save(linkDb);
                resultLink = linkDb.getOriginalUrl();
            }
        } else {
            incrementClicks(shortLink);
            resultLink = linkCookie;
        }
        return resultLink;
    }

    private void incrementClicks(String shortLink) {
        ShortLink link = shortLinkRepository.findByShortLink(shortLink);
        link.setClicks(link.getClicks() + 1);
        shortLinkRepository.save(link);
    }
}