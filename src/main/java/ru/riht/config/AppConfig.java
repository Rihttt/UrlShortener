package ru.riht.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Configuration
public class AppConfig {

    @Value("${app.base-url}")
    private String baseUrl;

    public String getBaseUrl(HttpServletRequest request) {
        if (StringUtils.hasText(baseUrl)) {
            return baseUrl;
        }

        return ServletUriComponentsBuilder
                .fromRequestUri(request)
                .replacePath("")
                .build()
                .toUriString();
    }
}
