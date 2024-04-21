package com.example.highhopes.shortlink;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ShortLinkCreateResponseDTO {

    private String shortUrl;
    private OffsetDateTime creationDate;
    private OffsetDateTime expiryDate;
    private Boolean status;
    private Integer clicks;

}