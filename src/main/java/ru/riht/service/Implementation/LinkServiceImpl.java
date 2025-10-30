package ru.riht.service.Implementation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ru.riht.model.Link;
import ru.riht.repository.LinkRepository;
import ru.riht.model.projections.LinkProjection;
import ru.riht.service.LinkService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.riht.constants.ServiceConstants.SHORT_URL;

@Service
@RequiredArgsConstructor
public class  LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Transactional
    @Override
    public String getLink(String shortCode) {
        updateClickCount(shortCode);
        return linkRepository.findByShortCode(shortCode);

    }

    @Override
    public Link shortenLink(String link, String customCode, String userId) {

        String shortCode = customCode.isEmpty() ? createShortCode() : customCode;

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
    public List<LinkProjection> getUserLinks(String userId){

        return linkRepository.findUserLinks(UUID.fromString(userId), SHORT_URL);
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
