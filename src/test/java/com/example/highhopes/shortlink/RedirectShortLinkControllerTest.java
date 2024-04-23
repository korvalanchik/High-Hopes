package com.example.highhopes.shortlink;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(Extension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RedirectShortLinkControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RedirectShortLinkController controller;

//    @Test
//    void testRedirectShortUrl() {
//        this.mockMvc.perform(get("/")).andDo().andExpect(status().is(OK))
//                .andExpect(content())
//    }
}
