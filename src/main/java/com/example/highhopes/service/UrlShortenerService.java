package com.example.highhopes.service;

import java.util.Map;

public interface UrlShortenerService {

    Map<String, Integer> viewActiveLinksStatistics();

    Map<String, Integer> viewAllLinksStatistics();

    String createNewLink(String originalUrl);

    void deleteLink(String shorturl);
}
