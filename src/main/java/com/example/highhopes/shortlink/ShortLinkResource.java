package com.example.highhopes.shortlink;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
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

    @GetMapping("/{id}")
    public ResponseEntity<ShortLinkDTO> getShortLink(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(shortLinkService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createShortLink(
            @RequestBody @Valid final ShortLinkDTO shortLinkDTO) {
        final Long createdId = shortLinkService.create(shortLinkDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
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

}
