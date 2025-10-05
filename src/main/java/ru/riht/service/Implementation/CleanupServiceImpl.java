package ru.riht.service.Implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.riht.repository.LinkRepository;
import ru.riht.repository.QrRepository;
import ru.riht.service.CleanupService;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CleanupServiceImpl implements CleanupService {

    @Value("${app.cleanup.day-to-keep}")
    private int daysToKeep;

    private LinkRepository linkRepository;
    private QrRepository qrRepository;

    @Scheduled(cron = "${app.cleanup.cron}")
    @Override
    public void deleteOldRecords() {

        LocalDateTime deletionDate = LocalDateTime.now().minusDays(daysToKeep);
        int totDeleted = 0;

        do{
            int deletedQr = qrRepository.deleteByUrlIdInOldLinks(deletionDate);
            int deletedLink = linkRepository.deleteByCreatedAtBefore(deletionDate);
            totDeleted += deletedLink;

            if (deletedQr > 0 && deletedLink > 0) break;

        } while (true);
        System.out.println("Deleted: " + totDeleted + " records");
    }
}
