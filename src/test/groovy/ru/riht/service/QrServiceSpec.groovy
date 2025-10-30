package ru.riht.service

import ru.riht.repository.LinkRepository
import ru.riht.repository.QrRepository
import ru.riht.service.Implementation.QrCodeServiceImpl
import ru.riht.model.projections.QrCodeProjection
import spock.lang.Specification

class QrServiceSpec extends Specification{
    LinkRepository linkRepository = Mock(LinkRepository)
    QrRepository qrRepository = Mock(QrRepository)
    QrCodeServiceImpl qrCodeService = new QrCodeServiceImpl(qrRepository,linkRepository)

    private static byte[] mockImageData = [1, 2, 3] as byte[]

    def "should return existing QR code if found by urlId"() {
        given:
            def url = "url"
            def urlId = UUID.randomUUID()
            def existingQr = Stub(QrCodeProjection) {
                getId() >> UUID.randomUUID()
                getUrl() >> url
                getImageData() >> mockImageData
                getUrlId() >> urlId
            }
            qrRepository.findByUrl(urlId) >> existingQr
        when:
            def result = qrCodeService.getOrCreateQrCode(url, urlId)
        then:
            result == existingQr
            0 * qrRepository.save(_)
            0 * linkRepository.updateQr(_, _)
    }

    def "should create new QR code if not exists"() {
        given:
            def url = "url"
            def urlId = UUID.randomUUID()

            def savedQr = Stub(QrCodeProjection) {
                getId() >> UUID.randomUUID()
                getUrl() >> url
                getImageData() >> mockImageData
                getUrlId() >> urlId
            }
        when:
            def result = qrCodeService.getOrCreateQrCode(url, urlId)
        then:
            result.urlId == urlId
            result.url == url
            result.imageData == mockImageData
            1 * qrRepository.findByUrl(urlId) >> null
            1 * qrRepository.save(_)
            1 * linkRepository.updateQr(_,_)
            1 * qrRepository.findByUrl(urlId) >> savedQr
    }
}
