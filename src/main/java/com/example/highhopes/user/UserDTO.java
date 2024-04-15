package com.example.highhopes.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

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

}
