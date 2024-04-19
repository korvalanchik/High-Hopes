package com.example.highhopes.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {
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
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        return cookie;
    }
}