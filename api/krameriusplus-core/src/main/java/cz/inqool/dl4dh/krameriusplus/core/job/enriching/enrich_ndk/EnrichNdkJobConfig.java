package cz.inqool.dl4dh.krameriusplus.core.job.enriching.enrich_ndk;

import cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.CommonJobConfig;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob.ENRICH_NDK;
import static cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.JobStep.*;

@Configuration
public class EnrichNdkJobConfig extends CommonJobConfig {

    @Bean
    public Job enrichNdkJob() {
        return jobBuilderFactory.get(ENRICH_NDK.name())
                .listener(jobListener)
                .start(steps.get(PREPARE_PUBLICATION_NDK))
                .next(steps.get(ENRICH_PUBLICATION_NDK))
                .next(steps.get(PREPARE_PAGES_NDK))
                .next(steps.get(ENRICH_PAGES_NDK))
                .build();
    }
}
