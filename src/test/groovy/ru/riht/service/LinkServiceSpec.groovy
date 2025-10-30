package ru.riht.service

import jakarta.servlet.http.Cookie
import org.springframework.mock.web.MockHttpServletResponse
import ru.riht.model.Link
import ru.riht.repository.LinkRepository
import ru.riht.service.Implementation.LinkServiceImpl
import spock.lang.*

import java.time.LocalDateTime


class LinkServiceSpec extends Specification{

    LinkRepository linkRepository = Mock(LinkRepository)
    LinkServiceImpl linkService = new LinkServiceImpl(linkRepository)

    def userID = "75460aa6-e6c8-40ca-a534-2ab2bf71d305"
    def testCode = "testCode"

    def "getting link from repository"(){
        given:
            Link link = Link.builder()
                    .originalUrl("testUrl")
                    .shortCode(testCode)
                    .createdAt(LocalDateTime.now())
                    .userId(UUID.fromString(userID))
                    .build()
            linkRepository.findByShortCode(testCode) >> link.originalUrl
        when:
            String result = linkService.getLink(testCode)
        then:
            1 * linkRepository.updateClickCount(testCode)
            result == link.originalUrl
    }

    def "shortening link and saving to repository with or without customCode "(){
        given:
            String customCode = "myCode"
            String emptyCustomCode = ""
            linkRepository.save(_)>>{Link savedLink ->
                return savedLink
            }
        when: "without CustomCode"
            Link result = linkService.shortenLink("testUrl", emptyCustomCode,userID)
        then:
            result.shortCode != null
            result.shortCode.length() > 0
            result.originalUrl == "testUrl"
            result.userId == UUID.fromString(userID)
            result.createdAt != null

        when: "with CustomCode"
             Link result1 = linkService.shortenLink("testUrl", customCode,userID)
        then:
            result1.shortCode != null
            result1.shortCode == customCode
            result1.originalUrl == "testUrl"
            result1.userId == UUID.fromString(userID)
            result1.createdAt != null
    }

    def "should return existing userId from cookie"() {
        given:
        def request = new CookieAwareRequestStub()
        def response = new MockHttpServletResponse()

        request.setCookies(new Cookie("userId", "existing-user-id-123"),
                new Cookie("ideaCookie", "someID"),
                new Cookie("sessionId", "somSessionId"))

        when:
        def result = linkService.getUserIdFromCookie(request, response)

        then:
        result == "existing-user-id-123"
        response.cookies.size() == 0
    }

    def "should add new userId cookie if non existent"() {
        given:
        def request = new CookieAwareRequestStub()
        def response = new MockHttpServletResponse()

        request.setCookies(
                new Cookie("ideaCookie", "someID"),
                new Cookie("sessionId", "someSessionId"))

        when:
        def result = linkService.getUserIdFromCookie(request, response)

        then:
        result != null
        result.length() > 0
        response.cookies.size() == 1
        with(response.cookies[0]) {
            name == "userId"
            value == result
            maxAge == 365 * 24 * 60 * 60
            path == "/"
        }
    }

}
