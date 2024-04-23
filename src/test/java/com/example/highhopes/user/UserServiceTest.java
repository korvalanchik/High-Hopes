package com.example.highhopes.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.highhopes.shortlink.ShortLinkRepository;
import com.example.highhopes.utils.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        // Arrange
        UserDTO userDTO = new UserDTO(null, "user1", "password1");
        User user = new User(1L, "user1", "password1");
        when(passwordEncoder.encode("password1")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        Long createdId = userService.create(userDTO);

        // Assert
        assertEquals(1L, createdId);
    }

    @Test
    void testGet() {
        // Arrange
        User user = new User(1L, "user1", "password1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserDTO userDTO = userService.get(1L);

        // Assert
        assertEquals("user1", userDTO.getUsername());
    }

    @Test
    void testGetNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.get(1L));
    }

    @Test
    void testFindAll() {
        // Arrange
        User user1 = new User(1L, "user1", "password1");
        User user2 = new User(2L, "user2", "password2");
        when(userRepository.findAll(Sort.by("id"))).thenReturn(Stream.of(user1, user2).collect(Collectors.toList()));

        // Act
        List<UserDTO> users = userService.findAll();

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

}
