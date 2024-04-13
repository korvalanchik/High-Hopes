package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import com.example.highhopes.user.UserRepository;
import com.example.highhopes.utils.NotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ShortLinkService {

    private final ShortLinkRepository shortLinkRepository;
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND = "User not found with username: ";

    public ShortLinkService(final ShortLinkRepository shortLinkRepository,
            final UserRepository userRepository) {
        this.shortLinkRepository = shortLinkRepository;
        this.userRepository = userRepository;
    }

    public List<ShortLinkDTO> findAll() {
        final List<ShortLink> shortLinks = shortLinkRepository.findAll(Sort.by("id"));
        return shortLinks.stream()
                .map(shortLink -> mapToDTO(shortLink, new ShortLinkDTO()))
                .toList();
    }

    public ShortLinkDTO get(final Long id) {
        return shortLinkRepository.findById(id)
                .map(shortLink -> mapToDTO(shortLink, new ShortLinkDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ShortLinkDTO shortLinkDTO) {
        final ShortLink shortLink = new ShortLink();
        mapToEntity(shortLinkDTO, shortLink);
        return shortLinkRepository.save(shortLink).getId();
    }

    public void update(final Long id, final ShortLinkDTO shortLinkDTO) {
        final ShortLink shortLink = shortLinkRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(shortLinkDTO, shortLink);
        shortLinkRepository.save(shortLink);
    }

    public void delete(final Long id) {
        shortLinkRepository.deleteById(id);
    }

    private ShortLinkDTO mapToDTO(final ShortLink shortLink, final ShortLinkDTO shortLinkDTO) {
        shortLinkDTO.setId(shortLink.getId());
        shortLinkDTO.setUserId(shortLink.getUser() != null ? shortLink.getUser().getId() : null);
//        shortLinkDTO.setUserId(shortLink.getUserId());
        shortLinkDTO.setOriginalUrl(shortLink.getOriginalUrl());
        shortLinkDTO.setShortUrl(shortLink.getShortUrl());
        shortLinkDTO.setCreationDate(shortLink.getCreationDate());
        shortLinkDTO.setExpiryDate(shortLink.getExpiryDate());
        shortLinkDTO.setStatus(shortLink.isActive());
        shortLinkDTO.setClicks(shortLink.getClicks());
        shortLinkDTO.setUser(shortLink.getUser() == null ? null : shortLink.getUser().getId());
        return shortLinkDTO;
    }

    private ShortLink mapToEntity(final ShortLinkDTO shortLinkDTO, final ShortLink shortLink) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(currentUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            shortLink.setUser(user);
        } else {
            throw new UsernameNotFoundException(USER_NOT_FOUND + currentUsername);
        }

        shortLink.setId(shortLinkDTO.getId());

//        shortLink.setUserId(shortLinkDTO.getUserId());
        shortLink.setOriginalUrl(shortLinkDTO.getOriginalUrl());
        shortLink.setShortUrl(shortLinkDTO.getShortUrl());
        shortLink.setCreationDate(shortLinkDTO.getCreationDate());
        shortLink.setExpiryDate(shortLinkDTO.getExpiryDate());
        shortLink.setActive(shortLinkDTO.getStatus());
        shortLink.setClicks(shortLinkDTO.getClicks());
        final User user = shortLinkDTO.getUser() == null ? null : userRepository.findById(shortLinkDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        shortLink.setUser(user);
        return shortLink;
    }

}
