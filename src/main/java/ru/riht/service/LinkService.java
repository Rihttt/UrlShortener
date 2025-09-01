package ru.riht.service;

import ru.riht.dto.LinkDto;
import ru.riht.model.Link;

public interface LinkService {

    Link shortenLink(String link);

    LinkDto getLink(String link);

    void updateClickCount(String shortCode);
}
