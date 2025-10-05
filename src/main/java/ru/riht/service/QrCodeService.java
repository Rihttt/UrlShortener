package ru.riht.service;

import com.google.zxing.WriterException;
import ru.riht.model.projections.QrCodeProjection;

import java.io.IOException;
import java.util.UUID;

public interface QrCodeService {

    QrCodeProjection getOrCreateQrCode(String url, UUID urlId) throws IOException, WriterException;

}
