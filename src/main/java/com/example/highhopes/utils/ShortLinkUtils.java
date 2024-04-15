package com.example.highhopes.utils;

import com.example.highhopes.shortlink.ShortLink;
import com.example.highhopes.shortlink.ShortLinkRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;

public class ShortLinkUtils {
    private final ShortLinkRepository shortLinkRepository;
    public ShortLinkUtils(final ShortLinkRepository shortLinkRepository) {
        this.shortLinkRepository = shortLinkRepository;
    }
    public String findCookie(HttpServletRequest request, String shortLink) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> originalUrlCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(shortLink))
                    .findFirst();

            if (originalUrlCookie.isPresent()) {
                return originalUrlCookie.get().getValue();
            }
        }
        return "Not found";
    }

    public Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        cookie.setPath("/");
        return cookie;
    }
    public void incrementClicks(String shortLink) {
        ShortLink link = shortLinkRepository.findByShortLink(shortLink);
        link.setClicks(link.getClicks() + 1);
        shortLinkRepository.save(link);
    }
}
