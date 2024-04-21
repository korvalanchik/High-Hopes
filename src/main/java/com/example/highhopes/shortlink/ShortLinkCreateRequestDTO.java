package com.example.highhopes.shortlink;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShortLinkCreateRequestDTO {

    @NotBlank(message = "Original URL must not be blank")
    @Size(max = 255, message = "Original URL length must be less than or equal to 255 characters")
    private String originalUrl;

}