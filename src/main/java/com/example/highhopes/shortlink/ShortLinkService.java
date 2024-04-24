package com.example.highhopes.shortlink;

import com.example.highhopes.user.User;
import com.example.highhopes.user.UserRepository;
import com.example.highhopes.utils.CookieUtils;
import com.example.highhopes.utils.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.example.highhopes.shortlink.GetOriginalUrlResponse.Error.OK;


@Service
public class ShortLinkService {

    @Value("${shortlink.generate.length}")
    private int shortLinkLength;

    private final ShortLinkRepository shortLinkRepository;
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND = "User not found with username: ";
    private final CookieUtils cookieUtils = new CookieUtils();

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

    @Transactional
    public String create(final ShortLinkCreateRequestDTO shortLinkCreateRequestDTO) {
        final ShortLink shortLink = new ShortLink();
        mapToEntityCreate(shortLinkCreateRequestDTO, shortLink);
        return shortLinkRepository.save(shortLink).getShortUrl();
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
        shortLinkDTO.setOriginalUrl(shortLink.getOriginalUrl());
        shortLinkDTO.setShortUrl(shortLink.getShortUrl());
        shortLinkDTO.setCreationDate(shortLink.getCreationDate());
        shortLinkDTO.setExpiryDate(shortLink.getExpiryDate());
        shortLinkDTO.setStatus(shortLink.isActive());
        shortLinkDTO.setClicks(shortLink.getClicks());
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
            throw new NotFoundException(USER_NOT_FOUND + currentUsername);
        }

        shortLink.setId(shortLinkDTO.getId());

        shortLink.setOriginalUrl(shortLinkDTO.getOriginalUrl());
        shortLink.setShortUrl(shortLinkDTO.getShortUrl());
        shortLink.setCreationDate(shortLinkDTO.getCreationDate());
        shortLink.setExpiryDate(OffsetDateTime.now().plusDays(7));
        shortLink.setActive(shortLinkDTO.getStatus());
        shortLink.setClicks(shortLinkDTO.getClicks());
        return shortLink;
    }

    private ShortLink mapToEntityCreate(final ShortLinkCreateRequestDTO shortLinkCreateRequestDTO, final ShortLink shortLink) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(currentUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            shortLink.setUser(user);
        } else {
            throw new NotFoundException(USER_NOT_FOUND + currentUsername);
        }

        String shortURL =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() +
                        "/sl/" +  generateURL(shortLinkLength);
        shortLink.setShortUrl(shortURL);

        shortLink.setOriginalUrl(shortLinkCreateRequestDTO.getOriginalUrl());
        shortLink.setCreationDate(OffsetDateTime.now());
        shortLink.setExpiryDate(OffsetDateTime.now().plusDays(7));
        shortLink.setActive(true);
        shortLink.setClicks(0);
        return shortLink;
    }

    public static String generateURL(int length) {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyz123456789";
        Random RANDOM = new Random();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public GetOriginalUrlResponse getOriginalUrlResponse(String shortLink,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {
        String linkCookie = cookieUtils.findCookie(request, shortLink);
        String shortURL =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() +
                        "/sl/" + shortLink;
        ShortLink shortLinkDb = shortLinkRepository.findByShortLink(shortURL);

        GetOriginalUrlResponse originalUrlResponse = handleShortLink(shortLinkDb);

        if (!originalUrlResponse.getError().equals(OK)){
            return originalUrlResponse;
        }

        if (linkCookie == null || linkCookie.equals("Not found")) {
            response.addCookie(cookieUtils.createCookie(shortLink, shortLinkDb.getOriginalUrl()));
            originalUrlResponse.setOriginalUrl(shortLinkDb.getOriginalUrl());
        } else {
            originalUrlResponse.setOriginalUrl(linkCookie);
        }

        incrementClicks(shortLinkDb);

        return originalUrlResponse;
    }

    private GetOriginalUrlResponse handleShortLink(ShortLink shortLinkDb) {
        GetOriginalUrlResponse originalUrlResponse = new GetOriginalUrlResponse();
        if (shortLinkDb == null) {
            originalUrlResponse.setError(GetOriginalUrlResponse.Error.LINK_NOT_FOUND);
        } else if (!shortLinkDb.getExpiryDate().isAfter(OffsetDateTime.now()) || !shortLinkDb.isActive()) {
            originalUrlResponse.setError(GetOriginalUrlResponse.Error.LINK_NOT_ACTIVE);
            shortLinkDb.setActive(false);
            shortLinkRepository.save(shortLinkDb);
        } else {
            originalUrlResponse.setError(OK);
        }
        return originalUrlResponse;
    }

    private void incrementClicks(final ShortLink shortLink) {
        shortLink.setClicks(shortLink.getClicks() + 1);
        shortLinkRepository.save(shortLink);
    }

    public List<ShortLinkDTO> getActiveShortLinks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(currentUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<ShortLink> activeShortLinks = shortLinkRepository.findAllActiveShortLinks(user.getId());
            return convertToDTO(activeShortLinks);
        } else {
            throw new NotFoundException(USER_NOT_FOUND + currentUsername);
        }

    }

    public List<ShortLinkDTO> getAllShortLinks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(currentUsername);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<ShortLink> allShortLinks = shortLinkRepository.findByUserId(user.getId());
            return convertToDTO(allShortLinks);
        } else {
            throw new NotFoundException(USER_NOT_FOUND + currentUsername);
        }

    }

    private List<ShortLinkDTO> convertToDTO(List<ShortLink> shortLinks) {
        List<ShortLinkDTO> shortLinkDTOs = new ArrayList<>();
        for (ShortLink shortLink : shortLinks) {
            ShortLinkDTO shortLinkDTO = new ShortLinkDTO();
            shortLinkDTO.setId(shortLink.getId());
            shortLinkDTO.setOriginalUrl(shortLink.getOriginalUrl());
            shortLinkDTO.setShortUrl(shortLink.getShortUrl());
            shortLinkDTO.setCreationDate(shortLink.getCreationDate());
            shortLinkDTO.setExpiryDate(shortLink.getExpiryDate());
            shortLinkDTO.setStatus(shortLink.isActive());
            shortLinkDTO.setClicks(shortLink.getClicks());
            shortLinkDTO.setUserId(shortLink.getUser().getId());
            shortLinkDTOs.add(shortLinkDTO);
        }
        return shortLinkDTOs;
    }

    public ShortLink getLinksByShortLink(String originalUrl){
        return shortLinkRepository.findByOriginalLink(originalUrl);
    }
}
