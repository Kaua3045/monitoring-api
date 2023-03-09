package com.kaua.monitoring.infrastructure.jobs.processor;

import com.kaua.monitoring.infrastructure.jobs.outputs.LinkJobOutput;
import com.kaua.monitoring.infrastructure.jobs.reader.LinkJobReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class UrlItemProcessor implements ItemProcessor<LinkJobReader, LinkJobOutput> {

    private static final Logger log = LoggerFactory.getLogger(UrlItemProcessor.class);

    @Override
    public LinkJobOutput process(final LinkJobReader item) throws Exception {
        final var executeDateVerify = item.getExecuteDate().atZone(ZoneId.of("America/Sao_Paulo")).toInstant();
        final var instantNowAtSp = Instant.now()
                .minus(2, ChronoUnit.MINUTES)
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .toInstant()
                .truncatedTo(ChronoUnit.SECONDS);

        if (executeDateVerify.isBefore(instantNowAtSp) && !item.isRepeat()) {
            return null;
        }

        final var url = new URL(item.getUrl());
        final var urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

        final var responseStatusCode = urlConnection.getResponseCode();
        final var responseMessage = urlConnection.getResponseMessage();

        urlConnection.disconnect();

        log.info("URL: {} response status -> {}", item.getUrl(), responseStatusCode);

        return new LinkJobOutput(
                UUID.randomUUID().toString().toLowerCase(),
                item.getId(),
                responseMessage,
                responseStatusCode
        );
    }
}
