package ru.riht.service.Implementation;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.riht.model.QrCode;
import ru.riht.model.projections.QrCodeProjection;
import ru.riht.repository.LinkRepository;
import ru.riht.repository.QrRepository;
import ru.riht.service.QrCodeService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {


    private final QrRepository qrRepository;

    private final LinkRepository linkRepository;

    @Transactional
    @Override
    public QrCodeProjection getOrCreateQrCode(String url, UUID urlId) throws IOException, WriterException {

        QrCodeProjection existing = qrRepository.findByUrl(urlId);
        if(existing != null) {
            return existing;
        }

        QrCode qrCode = QrCode.builder()
                .url(url)
                .imageData(generateQrCodeImage(url, 200,200))
                .urlId(urlId)
                .build();
        System.out.println(Arrays.toString(qrCode.getImageData()));
        qrRepository.save(qrCode);
        linkRepository.updateQr(urlId, qrCode.getId());

        return qrRepository.findByUrl(qrCode.getUrlId());
    }

    private byte[] generateQrCodeImage(String url, int width, int height) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }


}
