//package com.example.highhopes.shortlink;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/resolve/**")
//public class RedirectShortLinkController {
//
//    private final ShortLinkService shortLinkService;
//
//    public RedirectShortLinkController(final ShortLinkService shortLinkService) {
//        this.shortLinkService = shortLinkService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ShortLinkDTO>> getAllUsersShortLinks() {
//        return ResponseEntity.ok(shortLinkService.findAll());
//    }
//
//    @GetMapping
//    public ResponseEntity<GetOriginalUrlResponse> resolveShortUrl(HttpServletRequest request, HttpServletResponse response) {
//
//        GetOriginalUrlResponse originalUrl = shortLinkService.getOriginalUrl(request, response);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(originalUrl);
//    }
//
//}
