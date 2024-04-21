package com.example.highhopes.shortlink;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class ShortLinkResolveRequestDTO {

    @NotBlank(message = "Short URL must not be blank")
    private String shortUrl;

}