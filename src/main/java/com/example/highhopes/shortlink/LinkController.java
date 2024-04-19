package com.example.highhopes.shortlink;


import com.example.highhopes.user.User;
import com.example.highhopes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/v1/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Link>> getAllNotes() {
        List<Link> links = linkService.listAll();
        return new ResponseEntity<>(links, HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<?> createLink(@RequestBody String url) {
        Map<String, Object> response = new HashMap<>();

        if (!checkLink(url)) {
            response.put("error", "Invalid URL");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        String shortURL =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() +
                      "/" +  generateURL(8);
        addLink(url,shortURL);

        response.put("error", "ok");
        response.put("short_url", shortURL);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<User> users = userService.listAll();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.getId();
            }
        }
        return -1L;

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

    private void addLink(String url , String shortURL){
        OffsetDateTime currentDate = OffsetDateTime.now();
        Link createLink = new Link();
        createLink.setUser(userService.getById(getUserId()));
        createLink.setOriginalUrl(url);
        createLink.setShortUrl(shortURL);
        createLink.setExpiryDate(currentDate.plusDays(7));
        createLink.setActive(true);
        createLink.setClicks(0);
        linkService.add(createLink);
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

    @GetMapping("/{id}")
    public ResponseEntity<Link> getNoteById(@PathVariable("id") Long id) {
        Link link = linkService.getById(id);
        if (link != null) {
            return new ResponseEntity<>(link, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
