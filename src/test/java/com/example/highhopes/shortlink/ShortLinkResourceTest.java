package com.example.highhopes.shortlink;

import com.nimbusds.jose.shaded.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShortLinkResource.class)
@AutoConfigureMockMvc(addFilters = false)
class ShortLinkResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortLinkService shortLinkService;

    @Test
    void getAllUsersShortLinksTest() throws Exception {
        ShortLinkDTO shortLinkDTO = new ShortLinkDTO();
        shortLinkDTO.setId(1L);
        shortLinkDTO.setOriginalUrl("http://example.com");
        shortLinkDTO.setShortUrl("example.com/short");

        List<ShortLinkDTO> shortLinks = new ArrayList<>();
        shortLinks.add(shortLinkDTO);
        given(shortLinkService.findAll()).willReturn(shortLinks);

        mockMvc.perform(get("/api/shortLinks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(shortLinks.size())))
                .andExpect(jsonPath("$[0].id").value(shortLinks.get(0).getId()))
                .andExpect(jsonPath("$[0].originalUrl").value(shortLinks.get(0).getOriginalUrl()))
                .andExpect(jsonPath("$[0].shortUrl").value(shortLinks.get(0).getShortUrl()));
    }
    @Test
    void createLinkValidUrlSuccess() throws Exception {
        ShortLinkCreateRequestDTO requestDTO = new ShortLinkCreateRequestDTO();
        requestDTO.setOriginalUrl("http://example.com");
        String shortUrl = "example.com/short";

        given(shortLinkService.create(requestDTO)).willReturn(shortUrl);

        mockMvc.perform(post("/api/shortLinks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.error").value("ok"))
                .andExpect(jsonPath("$.short_url").value(shortUrl));
    }

    @Test
    void createLinkInvalidUrlReturnsError() throws Exception {
        ShortLinkCreateRequestDTO requestDTO = new ShortLinkCreateRequestDTO();
        requestDTO.setOriginalUrl("invalid-url");

        mockMvc.perform(post("/api/shortLinks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Invalid URL"));
    }

    @Test
    void createLinkExistingShortUrlReturnsError() throws Exception {
        ShortLinkCreateRequestDTO requestDTO = new ShortLinkCreateRequestDTO();
        requestDTO.setOriginalUrl("http://example.com");

        given(shortLinkService.getLinksByShortLink(any())).willReturn(new ShortLink());

        mockMvc.perform(post("/api/shortLinks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Url already exists"));
    }

    @Test
    void deleteShortLinkSuccessfulDeletion() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/shortLinks/{id}", id))
                .andExpect(status().isNoContent());

        verify(shortLinkService, times(1)).delete(id);
    }

    @Test
    void getActiveShortLinksSuccessful() throws Exception {
        ShortLinkDTO shortLinkDTO = new ShortLinkDTO();
        shortLinkDTO.setId(1L);
        shortLinkDTO.setUserId(1L);
        shortLinkDTO.setClicks(5);
        shortLinkDTO.setStatus(true);
        shortLinkDTO.setCreationDate(OffsetDateTime.now());
        shortLinkDTO.setExpiryDate(OffsetDateTime.now().plusDays(3));
        shortLinkDTO.setOriginalUrl("http://example.com");
        shortLinkDTO.setShortUrl("example.com/short");

        List<ShortLinkDTO> activeShortLinks = new ArrayList<>();
        activeShortLinks.add(0, shortLinkDTO);

        given(shortLinkService.getActiveShortLinks()).willReturn(activeShortLinks);

        mockMvc.perform(get("/api/shortLinks/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(activeShortLinks.size())));

        verify(shortLinkService, times(1)).getActiveShortLinks();
    }

    @Test
    void getActiveShortLinks_NoData() throws Exception {
        given(shortLinkService.getActiveShortLinks()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/shortLinks/active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(shortLinkService, times(1)).getActiveShortLinks();
    }

    @Test
    void getAllShortLinksSuccessful() throws Exception {
        ShortLinkDTO shortLinkDTO = new ShortLinkDTO();
        shortLinkDTO.setId(1L);
        shortLinkDTO.setUserId(1L);
        shortLinkDTO.setClicks(5);
        shortLinkDTO.setStatus(true);
        shortLinkDTO.setCreationDate(OffsetDateTime.now());
        shortLinkDTO.setExpiryDate(OffsetDateTime.now().plusDays(3));
        shortLinkDTO.setOriginalUrl("http://example.com");
        shortLinkDTO.setShortUrl("example.com/short");

        List<ShortLinkDTO> allShortLinks = new ArrayList<>();
        allShortLinks.add(0, shortLinkDTO);
        given(shortLinkService.getAllShortLinks()).willReturn(allShortLinks);

        mockMvc.perform(get("/api/shortLinks/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(allShortLinks.size())));

        verify(shortLinkService, times(1)).getAllShortLinks();
    }

    @Test
    void getAllShortLinks_NoData() throws Exception {
        given(shortLinkService.getAllShortLinks()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/shortLinks/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(shortLinkService, times(1)).getAllShortLinks();
    }

    private String asJsonString(final Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}