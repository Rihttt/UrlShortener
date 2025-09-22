package ru.riht.service;

import com.google.zxing.WriterException;
import ru.riht.model.projections.QrCodeDto;

import java.io.IOException;
import java.util.UUID;

public interface QrCodeService {

    QrCodeDto getOrCreateQrCode(String url, UUID urlId) throws IOException, WriterException;

}
