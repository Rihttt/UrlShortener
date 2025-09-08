package ru.riht.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.riht.model.Link;
import ru.riht.service.LinkService;

@Controller
public class WebController {

    private final LinkService linkService;

    public WebController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/shorten")
    public String shorten(@RequestParam String originalUrl,
                          @RequestParam String customCode,
                          Model model) {
        String shortUrl ="";

        try {
            if(customCode != null) {
                linkService.shortenLink(originalUrl, customCode);
                shortUrl = "http://localhost:8080/" + customCode;
            }else{
                Link shortenedLink = linkService.shortenLink(originalUrl, customCode);
                shortUrl = "http://localhost:8080/" + shortenedLink.getShortCode();
            }
        } catch (DataIntegrityViolationException error) {
            DataIntegrityViolationException e = new DataIntegrityViolationException("Такой код уже существует: " + customCode);
            model.addAttribute("error", e.getMessage());
        }


        model.addAttribute("originalUrl", originalUrl);
        model.addAttribute("shortUrl", shortUrl);
        return "index";
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
