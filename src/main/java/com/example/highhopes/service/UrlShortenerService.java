package com.example.highhopes.service;

import java.util.Map;

public interface UrlShortenerService {

    // Active Links
    Map<String, Integer> viewActiveLinksStatistics();

    // All Links
    Map<String, Integer> viewAllLinksStatistics();

    // Create Link
    String createNewLink(String originalUrl);

    // Delete link
    void deleteLink(String shorturl);
}
