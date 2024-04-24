package com.example.highhopes.jwt;

import com.example.highhopes.user.User;
import com.example.highhopes.user.UserDTO;
import com.example.highhopes.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/api/auth")
@RestController
@Tag(name = "Authentication", description = "The Authentication API. Contains operations like change password, forgot password, login, logout, etc.")
public class AuthenticationApi {

    private final UserDetailsService userDetailsService;
    private final JwtEncoder encoder;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthenticationApi(UserDetailsService userDetailsService, JwtEncoder encoder,
                             PasswordEncoder passwordEncoder, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Operation(summary = "User Authentication", description = "Authenticate the user and return a JWT token if the user is valid.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", examples = @io.swagger.v3.oas.annotations.media.ExampleObject(value = "{\n" + "  \"username\": \"User\",\n"
      + "  \"password\": \"User\"\n" + "}", summary = "User Authentication Example")))
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
            String token = generateToken(userDetails);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("X-AUTH-TOKEN", token);
            return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_JSON).body("{\"token\":\"" + token + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body("Invalid username or password");
        }
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> createUser(@RequestBody @Valid final UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();
        String password = userDTO.getPassword();

        if (password == null || password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*")) {
            response.put("error", "Invalid password. It must be at least 8 characters long and contain at least one digit, one uppercase letter, and one lowercase letter");
            return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(response);
        }
        if (!userService.isUsernameUnique(userDTO.getUsername())) {
            response.put("error", "Username is already taken");
            return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(response);
        }

        response.put("error", "OK");
        final Long createdId = userService.create(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    private String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        long expiry = 36000L;
        String scope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(userDetails.getUsername())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
