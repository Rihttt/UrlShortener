package ru.riht.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.riht.model.Link;
import ru.riht.service.LinkService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {

    private final LinkService linkService;

    @PostMapping("/shorten")
    public Link shorten(@RequestParam String url) {

        return linkService.shortenLink(url);
    }

}
