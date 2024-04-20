package com.example.highhopes.shortlink;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(value = "/api/shortLinks", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShortLinkResource {

    private final ShortLinkService shortLinkService;

    public ShortLinkResource(final ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @GetMapping
    public ResponseEntity<List<ShortLinkDTO>> getAllShortLinks() {
        return ResponseEntity.ok(shortLinkService.findAll());
    }


    @PostMapping()
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> createLink(@RequestBody @Valid final ShortLinkDTO shortLinkDTO) {
        Map<String, Object> response = new HashMap<>();
        String url = shortLinkDTO.getOriginalUrl();
        if (!checkLink(url)) {
            response.put("error", "Invalid URL");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        String shortURL =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() +
                        "/" +  generateURL(8);
//        addLink(url,shortURL);
        shortLinkDTO.setShortUrl(shortURL);
        shortLinkService.create(shortLinkDTO);
        response.put("error", "ok");
        response.put("short_url", shortURL);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private boolean checkLink(String url) {
        try {
            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");

            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1500);

            int responseCode = connection.getResponseCode();

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }


    public static String generateURL(int length) {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyz123456789";
        Random RANDOM = new Random();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateShortLink(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ShortLinkDTO shortLinkDTO) {
        shortLinkService.update(id, shortLinkDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteShortLink(@PathVariable(name = "id") final Long id) {
        shortLinkService.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/{shortLink}")
//    public ResponseEntity<GetOriginalUrlResponse> redirectToOriginalUrl(@PathVariable String shortLink,
//                                                                        HttpServletRequest request,
//                                                                        HttpServletResponse response) {
//        GetOriginalUrlResponse originalUrl = shortLinkService.getOriginalUrl(shortLink, request, response);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(originalUrl);
//    }

    @Cacheable(value = "resolveUrlCache", key = "#request.shortUrl")
    @PostMapping("/resolve")
    public ResponseEntity<GetOriginalUrlResponse> resolveShortUrl(@RequestBody ShortLinkDTO request) {
        String shortUrl = request.getShortUrl();

        if (shortUrl == null || shortUrl.isEmpty()) {
            GetOriginalUrlResponse errorResponse = new GetOriginalUrlResponse();
            errorResponse.setError(GetOriginalUrlResponse.Error.LINK_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }

        GetOriginalUrlResponse originalUrl = shortLinkService.getOriginalUrl(shortUrl);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(originalUrl);
    }

}
