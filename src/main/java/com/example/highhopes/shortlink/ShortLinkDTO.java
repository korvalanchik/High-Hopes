package com.example.highhopes.shortlink;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;


@Getter
@Setter
public class ShortLinkDTO {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Size(max = 255)
    private String originalUrl;

    @Size(max = 255)
    private String shortUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime creationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime expiryDate;

    private Boolean status;

    private Integer clicks;

}
