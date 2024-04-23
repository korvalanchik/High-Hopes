package com.example.highhopes.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter

public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    private OffsetDateTime dateCreated;

    @NotNull
    private OffsetDateTime lastUpdated;

    public UserDTO() {
        this.dateCreated = OffsetDateTime.now();
        this.lastUpdated = OffsetDateTime.now();
    }

    public UserDTO(Long o, String user1, String password1) {
    }
}
