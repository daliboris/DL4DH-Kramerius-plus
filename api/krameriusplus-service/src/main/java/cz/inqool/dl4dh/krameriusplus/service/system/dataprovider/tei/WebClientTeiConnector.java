package cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.TeiExportParams;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei.header.TeiHeaderFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@Primary
public class WebClientTeiConnector implements TeiConnector {

    private WebClient webClient;

    private final ObjectMapper objectMapper;

    private RestTemplate restTemplate;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS");

    @Autowired
    public WebClientTeiConnector(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convertToTeiPage(Page page) {
        try {
            return webClient.post().uri(uriBuilder -> uriBuilder.path("/convert/page").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(page))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (JsonProcessingException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.SERIALIZING_ERROR, e);
        } catch (WebClientResponseException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.TEI_ERROR, e);
        }
    }

    @Override
    public String convertToTeiHeader(Publication publication) {
        try {
            return webClient.post().uri(uriBuilder -> uriBuilder.path("/convert/header").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(TeiHeaderFactory.createHeaderInput(publication)))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (JsonProcessingException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.SERIALIZING_ERROR, e);
        } catch (WebClientResponseException e) {
            throw new EnrichingException(EnrichingException.ErrorCode.TEI_ERROR, e);
        } catch (WebClientRequestException e) {
            throw new EnrichingException("TEI Converter not found", EnrichingException.ErrorCode.TEI_ERROR, e);
        }
    }

    @Override
    public File merge(InputStream teiHeader, List<InputStream> teiPages, TeiExportParams params, Path outputFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(List.of(MediaType.APPLICATION_XML));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("header", new MultipartInputStreamFileResource(teiHeader, "header"));

        for (InputStream teiPage : teiPages) {
            body.add("page[]", new MultipartInputStreamFileResource(teiPage, "page.xml"));
        }

        params.getUdPipeParams().forEach(param -> body.add("UDPipe", param.getName()));
        params.getNameTagParams().forEach(param -> body.add("NameTag", param.getName()));
        params.getAltoParams().forEach(param -> body.add("ALTO", param.getName()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try (OutputStream out = new FileOutputStream(outputFile.toString())) {
            return restTemplate.execute("/merge", HttpMethod.POST,
                    restTemplate.httpEntityCallback(requestEntity),
                    clientHttpResponse -> {
                        StreamUtils.copy(clientHttpResponse.getBody(), out);
                        return outputFile.toFile();
                    });
        } catch (IOException exception) {
            throw new UncheckedIOException("Failed to write merged TEI file", exception);
        }
    }

    @Resource(name = "teiWebClient")
    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
