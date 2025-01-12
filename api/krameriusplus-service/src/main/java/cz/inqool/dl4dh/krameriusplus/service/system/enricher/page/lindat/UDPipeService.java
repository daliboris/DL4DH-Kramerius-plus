package cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ExternalServiceException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.udpipe.LinguisticMetadata;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.lindat.udpipe.Token;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.UDPipeEnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.dto.LindatServiceResponseDto;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.dto.UDPipeProcessDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.ExternalServiceException.ErrorCode.UD_PIPE_ERROR;
import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.SystemLogDetails.LogLevel.ERROR;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

/**
 * @author Norbert Bodnar
 */
@Service
@Slf4j
public class UDPipeService {

    private WebClient webClient;

    /**
     * Processes the input text content and sets tokens attribute on page.
     */
    public UDPipeProcessDto processPage(Page page) {
        UDPipeProcessDto result = new UDPipeProcessDto();

        String pageContent = page.getContent();
        if (pageContent == null || pageContent.isEmpty()) {
            return result;
        }

        LindatServiceResponseDto response = makeApiCall(pageContent);

        result.setTokens(parseResponseToTokens(response));
        result.setParadata(extractParadata(response));

        return result;
    }

    private LindatServiceResponseDto makeApiCall(String body) {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("data", body);

        try {
            LindatServiceResponseDto response = webClient.post().uri(uriBuilder -> uriBuilder
                    .queryParam("tokenizer", "ranges")
                    .queryParam("tagger")
                    .queryParam("parser")
                    .build()).body(BodyInserters.fromMultipartData("data", body))
                    .acceptCharset(StandardCharsets.UTF_8)
                    .retrieve()
                    .bodyToMono(LindatServiceResponseDto.class)
                    .block();

            notNull(response, () -> new ExternalServiceException("UDPipe did not return results", UD_PIPE_ERROR, ERROR));

            if (response.getResult() != null) {
                response.setResultLines(response.getResult().split("\n"));
            }

            return response;
        } catch (Exception e) {
            throw new ExternalServiceException(UD_PIPE_ERROR, e);
        }
    }

    private UDPipeEnrichmentParadata extractParadata(LindatServiceResponseDto response) {
        UDPipeEnrichmentParadata paradata = new UDPipeEnrichmentParadata();
        paradata.setModel(response.getModel());
        paradata.setGenerator(getGenerator(response.getResultLines()));
        paradata.setAcknowledgements(response.getAcknowledgements());
        paradata.setLicence(getLicence(response.getResultLines()));

        return paradata;
    }

    private String getLicence(String[] resultLines) {
        if (resultLines != null && resultLines.length > 2) {
            String thirdLine = resultLines[2];
            return thirdLine.split("=")[1].trim();
        }

        return null;
    }

    private String getGenerator(String[] resultLines) {
        if (resultLines != null && resultLines.length > 0) {
            String firstLine = resultLines[0];
            return firstLine.split("=")[1].trim();
        }

        return null;
    }

    private List<Token> parseResponseToTokens(LindatServiceResponseDto response) {
        String[] lines = response.getResultLines();

        int tokenIndex = 0;
        boolean skipNext = false;
        String previousMiscColumn = "";
        String previousContent = "";

        List<Token> result = new ArrayList<>();

        for (String line : lines) {
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            if (skipNext) {
                skipNext = false;
                continue;
            }

            String[] columns = line.split("\t");

            Token token = new Token();
            token.setContent(columns[1]);

            LinguisticMetadata metadata = new LinguisticMetadata();

            try {
                metadata.setPosition(Integer.parseInt(columns[0]));
            } catch (NumberFormatException e) {
                // metadata for this word are split into two lines
                log.trace("Error parsing output line number on: " + line);
                previousMiscColumn = columns[9];
                previousContent = columns[1];
                continue;
            }

            metadata.setLemma(parseColumn(columns[2]));
            metadata.setUPosTag(parseColumn(columns[3]));
            metadata.setXPosTag(parseColumn(columns[4]));
            metadata.setFeats(parseColumn(columns[5]));
            metadata.setHead(parseColumn(columns[6]));
            metadata.setDepRel(parseColumn(columns[7]));

            if (!previousMiscColumn.isEmpty()) {
                token.setContent(previousContent);
                previousContent = "";
                metadata.setMisc(previousMiscColumn);
                parseMisc(token, previousMiscColumn);
                previousMiscColumn = "";
                skipNext = true;
            } else {
                metadata.setMisc(parseColumn(columns[9]));
                parseMisc(token, columns[9]);
            }

            token.setLinguisticMetadata(metadata);

            token.setTokenIndex(tokenIndex++);
            result.add(token);
        }

        return result;
    }

    private void parseMisc(Token token, String miscColumn) {
        String[] miscs = miscColumn.split("\\|");

        for (String misc : miscs) {
            if (misc.startsWith("TokenRange")) {
                String[] tokenRanges = misc.split("=")[1].split(":");
                token.setStartOffset(Integer.parseInt(tokenRanges[0]));
                token.setEndOffset(Integer.parseInt(tokenRanges[1]));
                break;
            }
        }
    }

    private String parseColumn(String columnValue) {
        if (columnValue == null || columnValue.isEmpty()) {
            log.trace("Missing column value in response");
            return null;
        }

        if (columnValue.equals("_")) {
            return null;
        }

        return columnValue;
    }

    @Resource(name = "udPipeWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
