package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import com.example.highhopes.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(ShortLinkResource.class)
@AutoConfigureMockMvc
public class ShortLinkServiceTest {

    @Mock
    private ShortLinkRepository shortLinkRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShortLinkService shortLinkService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFindAll() {
        ShortLink shortLink1 = new ShortLink();
        shortLink1.setId(1L);
        ShortLink shortLink2 = new ShortLink();
        shortLink2.setId(2L);
        List<ShortLink> shortLinks = Arrays.asList(shortLink1, shortLink2);

        when(shortLinkRepository.findAll(Sort.by("id"))).thenReturn(shortLinks);

        List<ShortLinkDTO> result = shortLinkService.findAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

}
