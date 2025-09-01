package ru.riht.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import ru.riht.service.LinkService;

@Controller
public class WebController {

    private final LinkService linkService;

    public WebController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/{shortCode}")
    public RedirectView getLink(@PathVariable String shortCode,
                                HttpServletResponse response) {

        String originalUrl = linkService.getLink(shortCode).getOriginalUrl();

        if (originalUrl != null) {
            linkService.updateClickCount(shortCode);
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(originalUrl);

            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return redirectView;
        } else {

            return null;
        }
    }
}
