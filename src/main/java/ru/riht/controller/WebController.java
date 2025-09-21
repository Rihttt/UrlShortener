package ru.riht.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ru.riht.model.Link;
import ru.riht.service.LinkService;
import ru.riht.model.projections.LinkDto;

import java.util.List;

import static ru.riht.constants.ServiceConstants.SHORT_URL;

@Controller
public class WebController {

    private final LinkService linkService;

    public WebController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {

        String userId = linkService.getUserIdFromCookie(request, response);
        List<LinkDto> userLinks = linkService.getUserLinks(userId);

        model.addAttribute("userLinks", userLinks);
        model.addAttribute("originalUrl", "");
        model.addAttribute("customCode", "");

        return "index";
    }

    @PostMapping("/shorten")
    public String shorten(@RequestParam String originalUrl,
                          @RequestParam String customCode,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        String userId = linkService.getUserIdFromCookie(request, response);

        String shortUrl ="";

        try {
            if(!customCode.isEmpty()) {
                linkService.shortenLink(originalUrl, customCode, userId);
                shortUrl = SHORT_URL + customCode;
                redirectAttributes.addFlashAttribute("shortUrl", shortUrl);
            }else{
                Link shortenedLink = linkService.shortenLink(originalUrl, customCode, userId);
                shortUrl = SHORT_URL + shortenedLink.getShortCode();
                redirectAttributes.addFlashAttribute("shortUrl", shortUrl);
            }
        } catch (DataIntegrityViolationException error) {
            DataIntegrityViolationException e = new DataIntegrityViolationException("Такой код уже существует: " + customCode);
            model.addAttribute("error", e.getMessage());
        }

        List<LinkDto> userLinks = linkService.getUserLinks(userId);

        model.addAttribute("userLinks", userLinks);
        model.addAttribute("originalUrl", originalUrl);
        model.addAttribute("shortUrl", shortUrl);
        return "redirect:/";
    }

    @GetMapping("/{shortCode}")
    public RedirectView getLink(@PathVariable String shortCode) {

        String originalUrl = linkService.getLink(shortCode);

        if (originalUrl != null) {
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(originalUrl);

            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            return redirectView;
        } else {

            return null;
        }
    }

}
