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

    public GetOriginalUrlResponse getOriginalUrl(String shortLink, HttpServletRequest request,
                                                 HttpServletResponse response) {
        GetOriginalUrlResponse originalUrlResponse = new GetOriginalUrlResponse();
        String linkCookie = cookieUtils.findCookie(request, shortLink);
        ShortLink linkDb = shortLinkRepository.findByShortLink(shortLink);

        if (linkDb != null) {
            if (linkCookie.equals("Not found")) {
                response.addCookie(cookieUtils.createCookie(shortLink, linkDb.getOriginalUrl()));
                originalUrlResponse.setOriginalUrl(linkDb.getOriginalUrl());
            } else {
                originalUrlResponse.setOriginalUrl(linkCookie);
            }
            originalUrlResponse.setError(GetOriginalUrlResponse.Error.OK);
            incrementClicks(linkDb);
        } else {
            originalUrlResponse.setError(GetOriginalUrlResponse.Error.LINK_NOT_FOUND);
        }

        return originalUrlResponse;
    }

    private void incrementClicks(ShortLink link) {
        link.setClicks(link.getClicks() + 1);
        shortLinkRepository.save(link);
    }
}