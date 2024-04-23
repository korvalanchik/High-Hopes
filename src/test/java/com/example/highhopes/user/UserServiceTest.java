package com.example.highhopes.user;

import com.example.highhopes.shortlink.ShortLinkRepository;
import com.example.highhopes.utils.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShortLinkRepository shortLinkRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreate() {
        UserDTO userDTO = new UserDTO(null, "user1", "password1");
        User user = new User(1L, "user1", "password1");
        when(passwordEncoder.encode("password1")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        Long createdId = userService.create(userDTO);

        assertEquals(1L, createdId);
    }

    @Test
    void testGet() {
        User user = new User(1L, "user1", "password1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.get(1L);

        assertEquals("user1", userDTO.getUsername());
    }

    @Test
    void testGetNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.get(1L));
    }

    @Test
    void testFindAll() {
        User user1 = new User(1L, "user1", "password1");
        User user2 = new User(2L, "user2", "password2");
        when(userRepository.findAll(Sort.by("id"))).thenReturn(Stream.of(user1, user2).collect(Collectors.toList()));

        List<UserDTO> users = userService.findAll();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

}
