package cz.inqool.dl4dh.krameriusplus.service.tei;

import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.export.TeiParams;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Qualifier("dummy")
public class DummyTeiConnector implements TeiConnector {

    @Override
    public String convertToTeiPage(Page page) {
        return "<xml>dummy tei body</xml>";
    }

    @Override
    public String convertToTeiHeader(Publication publication) {
        return "<xml>dummy tei header</xml>";
    }

    @SneakyThrows
    @Override
    public File merge(String teiHeader, List<String> teiPages, TeiParams params) {
        File file = File.createTempFile("download", "tmp");
        StreamUtils.copy("Test TEI full".getBytes(StandardCharsets.UTF_8), new FileOutputStream(file));

        return file;
    }
}
