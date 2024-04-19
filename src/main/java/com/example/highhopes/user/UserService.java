package com.example.highhopes.user;

import com.example.highhopes.shortlink.ShortLink;
import com.example.highhopes.shortlink.ShortLinkRepository;
import com.example.highhopes.utils.NotFoundException;
import com.example.highhopes.utils.ReferencedWarning;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ShortLinkRepository shortLinkRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository,
                       final ShortLinkRepository shortLinkRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.shortLinkRepository = shortLinkRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setDateCreated(user.getDateCreated());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setDateCreated(OffsetDateTime.now());
        user.setLastUpdated(OffsetDateTime.now());
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ShortLink userShortLink = shortLinkRepository.findFirstByUser(user);
        if (userShortLink != null) {
            referencedWarning.setKey("user.shortLink.user.referenced");
            referencedWarning.addParam(userShortLink.getId());
            return referencedWarning;
        }
        return null;
    }

}
