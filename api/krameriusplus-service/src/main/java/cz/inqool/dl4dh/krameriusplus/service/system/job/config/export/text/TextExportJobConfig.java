package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.text;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.ExportJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob.EXPORT_TEXT;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.*;

@Configuration
public class TextExportJobConfig extends ExportJobConfig {

    @Bean
    public Job exportTextJob() {
        return getJobBuilder()
                .start(stepContainer.getStep(PREPARE_PUBLICATION_METADATA))
                .next(stepContainer.getStep(PREPARE_EXPORT_DIRECTORY))
                .next(stepContainer.getStep(EXPORT_PAGES_TEXT))
                .next(stepContainer.getStep(ZIP_EXPORT))
                .next(stepContainer.getStep(CREATE_EXPORT))
                .next(stepContainer.getStep(CLEAN_UP_EXPORT))
                .build();
    }

    @Override
    public String getJobName() {
        return EXPORT_TEXT.name();
    }
}
