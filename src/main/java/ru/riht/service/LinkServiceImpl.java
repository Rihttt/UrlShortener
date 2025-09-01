package ru.riht.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.riht.dto.LinkDto;
import ru.riht.model.Link;
import ru.riht.repository.LinkRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Override
    public LinkDto getLink(String shortCode) {

        return LinkDto.builder()
                .originalUrl(linkRepository.findByShortCode(shortCode))
                .build();
    }

    @Override
    public Link shortenLink(String link) {
        Link shortLink = Link.builder()
                .originalUrl(link)
                .shortCode(createShortCode())
                .createdAt(LocalDateTime.now())
                .build();
        return linkRepository.save(shortLink);
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

    @Transactional
    public void updateClickCount(String shortCode) {
        linkRepository.updateClickCount(shortCode);
    }

}
