package com.example.highhopes.service;

import java.util.Map;

public class UrlShortenerServiceImpl implements UrlShortenerService {
    private Map<String, String> database;
    private Map<String, Integer> statistics;

    @Override
    public Map<String, Integer> viewActiveLinksStatistics() {

        return statistics;
    }

    @Override
    public Map<String, Integer> viewAllLinksStatistics() {

        return statistics;
    }

    @Override
    public String createNewLink(String originalUrl) {

        String shortUrl = generateShortUrl();
        database.put(shortUrl, originalUrl);
        return shortUrl;
    }

    @Override
    public void deleteLink(String shortUrl) {

        database.remove(shortUrl);
    }

    private String generateShortUrl() {

        return "short.url";
    }
}
