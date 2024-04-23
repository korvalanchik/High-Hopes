package com.example.highhopes.shortlink;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.Mockito.*;

@WebMvcTest(ShortLinkResource.class)
class ShortLinkResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortLinkService shortLinkService; // Using @MockBean to mock the service

    @InjectMocks
    private ShortLinkResource shortLinkResource;

    @Test
    @WithMockUser(username = "User", password = "User")
    void testGetAllUsersShortLinks() throws Exception {
        when(shortLinkService.findAll()).thenReturn(Collections.emptyList());

        Authentication auth = new UsernamePasswordAuthenticationToken("User", "$2a$10$OSYyd.176WpWCeIh4Vy2Le9Rw49F1TdasGJbXI2KLzSIvkDh5PUpq");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/shortLinks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));

        verify(shortLinkService, times(1)).findAll();
    }

}
