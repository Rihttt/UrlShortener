package ru.riht.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ru.riht.dto.LinkDto;
import ru.riht.model.Link;

import java.util.List;

public interface LinkService {

    Link shortenLink(String link, String customCode, String userId);

    LinkDto getLink(String link);

    String getUserIdFromCookie(HttpServletRequest request,
                               HttpServletResponse response);

    List<String> getUserLinks(String userId);
}
