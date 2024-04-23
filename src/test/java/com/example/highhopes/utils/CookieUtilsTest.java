package com.example.highhopes.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CookieUtilsTest {
    CookieUtils cookieUtils;
    HttpServletRequest request;
    @BeforeEach
    void beforeEach() {
        cookieUtils = new CookieUtils();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void testFindCookieWhenCookieExists() {
        Cookie cookie = new Cookie("testCookie", "testValue");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String foundValue = cookieUtils.findCookie(request, "testCookie");

        assertEquals("testValue", foundValue);
    }

    @Test
    void testFindCookieWhenCookieDoesNotExist() {
        when(request.getCookies()).thenReturn(null);

        String foundValue = cookieUtils.findCookie(request, "nonExistentCookie");
        assertEquals("Not found", foundValue);
    }

    @Test
    void testCreateCookie() {
        Cookie cookie = cookieUtils.createCookie("testCookie", "testValue");

        assertEquals("testCookie", cookie.getName());
        assertEquals("testValue", cookie.getValue());
        assertEquals(24 * 60 * 60, cookie.getMaxAge());
        assertEquals("/", cookie.getPath());
    }
}