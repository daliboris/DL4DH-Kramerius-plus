package cz.inqool.dl4dh.krameriusplus.service.filler.dataprovider.streams;

import cz.inqool.dl4dh.alto.Alto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Norbert Bodnar
 */
@Service
public class StreamProvider {

    private final WebClient webClient;

    @Autowired
    public StreamProvider(@Value("${kramerius.api:https://kramerius.mzk.cz}") String krameriusApi) {
        this.webClient = WebClient.create(krameriusApi + "/search/api/v5.0/item");
    }

    public String getTextOcr(String pageId) {
        String textOcr = getStreamAsString(pageId, StreamType.TEXT_OCR);

        if (textOcr != null) {
            textOcr = normalizeText(textOcr);
        }

        return textOcr;
    }

    public Alto getAlto(String pageId) {
        String altoAsString = webClient.get()
                .uri("/{pageId}/streams/ALTO", pageId)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (altoAsString == null) {
            throw new IllegalArgumentException("Page with ID=" + pageId + " does not contain ALTO stream.");
        }

        return JAXB.unmarshal(new StringReader(altoAsString), Alto.class);
    }

    private String normalizeText(String textOcr) {
        return textOcr.replace("\uFEFF", "")
                .replaceAll("-\r\n", "")
                .replaceAll("-\n", "")
                .replaceAll("\r\n", " ")
                .replaceAll("\n", " ");

    }

    public String getStreamAsString(String digitalObjectId, StreamType streamType) {
        return webClient.get()
                .uri("/{digitalObjectId}/streams/{streamId}", digitalObjectId, streamType.getStreamId())
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Getter
    public enum StreamType {
        TEXT_OCR("TEXT_OCR"),
        ALTO("ALTO");

        private final String streamId;

        StreamType(String streamId) {
            this.streamId = streamId;
        }
    }
}
