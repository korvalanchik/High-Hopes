package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import com.example.highhopes.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShortLinkServiceTest {

    @Mock
    private ShortLinkRepository shortLinkRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShortLinkService shortLinkService;

    @BeforeEach
    public void setUp() {
        // Mocking the Authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        // Setting up the SecurityContextHolder with the mock Authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testFindAll() {
        // Mock data
        ShortLink shortLink1 = new ShortLink();
        shortLink1.setId(1L);
        ShortLink shortLink2 = new ShortLink();
        shortLink2.setId(2L);
        List<ShortLink> shortLinks = Arrays.asList(shortLink1, shortLink2);

        // Stubbing repository method
        when(shortLinkRepository.findAll(Sort.by("id"))).thenReturn(shortLinks);

        // Call the service method
        List<ShortLinkDTO> result = shortLinkService.findAll();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void testCreate() {
        // Mock data
        ShortLinkCreateRequestDTO requestDTO = new ShortLinkCreateRequestDTO();
        requestDTO.setOriginalUrl("http://example.com");
        User user = new User();
        user.setId(1L);
        Optional<User> userOptional = Optional.of(user);

        // Stubbing repository method
        when(userRepository.findByUsername(anyString())).thenReturn(userOptional);
        when(shortLinkRepository.save(any(ShortLink.class))).thenAnswer(invocation -> {
            ShortLink shortLink = invocation.getArgument(0);
            shortLink.setId(1L);
            return shortLink;
        });

        // Call the service method
        String shortUrl = shortLinkService.create(requestDTO);

        // Verify the result
        assertEquals("/random_short_url", shortUrl); // Adjust the expected value accordingly
    }

    // Add more test methods for other service methods
}
