package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoContentExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class DownloadPagesAltoProcessor implements ItemProcessor<Page, Page> {

    private final StreamProvider streamProvider;

    private final AltoMapper altoMapper;

    @Autowired
    public DownloadPagesAltoProcessor(StreamProvider streamProvider, AltoMapper altoMapper) {
        this.streamProvider = streamProvider;
        this.altoMapper = altoMapper;
    }

    @Override
    public Page process(@NonNull Page item) {
        Alto alto = streamProvider.getAlto(item.getId());

        if (alto == null) {
            log.debug("No ALTO for page {}", item.getId());
            return null;
        }

        AltoDto altoDto = altoMapper.toAltoDto(alto);
        new AltoContentExtractor(item).enrichPage(altoDto);

        return item;
    }
}
