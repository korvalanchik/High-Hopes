package com.example.highhopes.shortlink;

import lombok.Data;

@Data
public class GetOriginalUrlResponse {
    private Error error;
    private String originalUrl;

    public enum Error {
        OK,
        LINK_NOT_FOUND,
        LINK_NOT_ACTIVE
    }
}
