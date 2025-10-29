package ru.riht.controller

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import ru.riht.model.Link
import org.springframework.ui.Model
import ru.riht.config.AppConfig
import ru.riht.model.projections.LinkProjection
import ru.riht.model.projections.QrCodeProjection
import ru.riht.service.CookieAwareRequestStub
import ru.riht.service.LinkService
import org.springframework.mock.web.MockHttpServletResponse
import ru.riht.service.QrCodeService
import spock.lang.*


class WebControllerSpec extends Specification{
    def linkService = Mock(LinkService)
    def qrCodeService = Mock(QrCodeService)
    def appConfig = Mock(AppConfig)
    WebController webController = new WebController(linkService,qrCodeService,appConfig)

    def "main page"(){
        given:
            def request = new CookieAwareRequestStub()
            def response = new MockHttpServletResponse()
            def model = Mock(Model)

            def userId = "user123"
            def userLinks = [
                    Stub(LinkProjection) {
                    }
            ]

            linkService.getUserIdFromCookie(request, response) >> userId
            linkService.getUserLinks(userId) >> userLinks
        when:
            def viewName = webController.index(request, response, model)
        then:
            viewName == "index"

            1 * model.addAttribute("userLinks", userLinks)
            1 * model.addAttribute("originalUrl", "")
            1 * model.addAttribute("customCode", "")

    }

    def "call of Post mapping shorten"(){
        given:
            def request = new CookieAwareRequestStub()
            def response = new MockHttpServletResponse()
            def redirectAttributes = Mock(RedirectAttributes)
            def model = Mock(Model)

            def link = Stub(Link){
                shortCode >> "shortCode"
            }
            def originalUrl = "orig"
            def userId = "user123"
            def userLinks = [
                    Stub(LinkProjection) {
                    }
            ]
            linkService.shortenLink(originalUrl, customCode, userId) >> link
            linkService.getUserIdFromCookie(request, response) >> userId
            linkService.getUserLinks(userId) >> userLinks
            appConfig.getBaseUrl(request) >> "localhost/"
        when:
            def viewName = webController.shorten(
                    originalUrl,
                    customCode,
                    request,
                    response,
                    redirectAttributes,
                    model)
        then:
            viewName == "redirect:/"

            1 * redirectAttributes.addFlashAttribute("shortUrl", shortUrl)
            1 * model.addAttribute("userLinks", userLinks)
            1 * model.addAttribute("originalUrl", originalUrl)
            1 * model.addAttribute("shortUrl", shortUrl)
        where:
        customCode    || shortUrl
        ""            || "localhost/shortCode"
        "customCode"  || "localhost/customCode"
    }

    def "call of Post mapping shorten should throw exception when two links in db have same shortcode"(){
        given:
            def request = new CookieAwareRequestStub()
            def response = new MockHttpServletResponse()
            def redirectAttributes = Mock(RedirectAttributes)
            def model = Mock(Model)
            def originalUrl = "orig"
            def customCode ="custom"
            linkService.shortenLink(_,_,_) >> {throw new DataIntegrityViolationException("Такой код уже существует: custom")}
        when:
            def viewName = webController.shorten(
                    originalUrl,
                    customCode,
                    request,
                    response,
                    redirectAttributes,
                    model)
        then:
            1 * model.addAttribute("error", "Такой код уже существует: custom")
            viewName == "redirect:/"
            0 * redirectAttributes.addFlashAttribute(_, _)
    }

    def "redirect with short link"(){
        given:
            def shortCode = "short"
            linkService.getLink(shortCode) >> originalUrl
        when:
            def result = webController.getLink(shortCode)
        then:
            if (originalUrl != null){
                result != null
                result.url == originalUrl
                result.statusCode == HttpStatus.MOVED_PERMANENTLY
            }else{
                result == null
            }

        where:
        originalUrl   || _
        null          || _
        "originalUrl" || _
    }

    def "get qrCode for link"(){
        given:
            def qrRequestMock = Mock(WebController.QrRequest){
                getUrl() >> "url"
                getUrlId() >> UUID.randomUUID()
            }
            def qrCode = Stub(QrCodeProjection){
                getImageData() >> new byte[0]
            }

            qrCodeService.getOrCreateQrCode(qrRequestMock.getUrl(),qrRequestMock.getUrlId()) >> qrCode
        when:
            def response = webController.getQrAsJson(qrRequestMock)
        then:
            response.statusCode == HttpStatus.OK
            response.body != null
            response.body.imageData.startsWith("data:image/png;base64,")
            response.body.respUrl == "url"
    }

    def "should return 500 when qr generation fails"() {
        given:
            def qrRequestMock = Stub(WebController.QrRequest) {
                getUrl() >> "https://example.com"
                getUrlId() >> UUID.randomUUID()
            }

            qrCodeService.getOrCreateQrCode(_, _) >> { throw new RuntimeException("QR generation failed") }

        when:
            def response = webController.getQrAsJson(qrRequestMock)

        then:
            response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
            response.body == "Ошибка генерации QR"
    }

}
