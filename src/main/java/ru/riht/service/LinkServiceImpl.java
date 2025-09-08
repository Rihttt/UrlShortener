package ru.riht.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.riht.dto.LinkDto;
import ru.riht.model.Link;
import ru.riht.repository.LinkRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.riht.constants.ServiceConstants.SHORT_URL;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Transactional
    @Override
    public LinkDto getLink(String shortCode) {
        updateClickCount(shortCode);
        return LinkDto.builder()
                .originalUrl(linkRepository.findByShortCode(shortCode))
                .build();
    }

    @Override
    public Link shortenLink(String link, String customCode, String userId) {

        String shortCode = customCode == null ? createShortCode() : customCode;

        Link shortLink = Link.builder()
                .originalUrl(link)
                .shortCode(shortCode)
                .createdAt(LocalDateTime.now())
                .userId(UUID.fromString(userId))
                .build();

            return linkRepository.save(shortLink);
    }

    @Override
    public String getUserIdFromCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String userId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userId".equals(cookie.getName())) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }

        if (userId == null) {
            userId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("userId", userId);
            cookie.setMaxAge(365 * 24 * 60 * 60); // Срок действия - 1 год
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        return userId;
    }

    @Override
    public List<String> getUserLinks(String userId){

        List<String> userLinksShortCodes = linkRepository.findUserLinks(UUID.fromString(userId));
        return userLinksShortCodes.stream()
                .map(l -> SHORT_URL + l)
                .toList();
    }

    private String createShortCode() {
        int number = (int) (Math.random() * 9999999)+1000000;

        StringBuilder shortCode = new StringBuilder();
        while (number > 0) {
            int remainder = number % 62;
            shortCode.append(BASE62_CHARS.charAt(remainder));
            number /= 62;
        }
        return shortCode.reverse().toString();
    }

    private void updateClickCount(String shortCode) {
        linkRepository.updateClickCount(shortCode);
    }

}
