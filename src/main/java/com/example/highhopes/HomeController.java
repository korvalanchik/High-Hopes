package com.example.highhopes;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String index() {
        return "home/index";
    }

    @GetMapping("/index-shortener")
    public String indexShortener() {
        return "home/index-shortener";
    }

    @GetMapping("/index-user")
    public String indexUser() {
        return "home/index-user";
    }
}
