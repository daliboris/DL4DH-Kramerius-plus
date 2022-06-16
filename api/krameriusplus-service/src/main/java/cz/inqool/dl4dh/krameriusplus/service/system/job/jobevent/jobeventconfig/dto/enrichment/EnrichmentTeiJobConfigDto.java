package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.enrichment;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EnrichmentTeiJobConfigDto extends EnrichmentJobConfigDto {

    private final KrameriusJob krameriusJob = KrameriusJob.ENRICHMENT_TEI;

    @Override
    public Map<String, Object> getJobParameters() {
        return super.createJobParameters();
    }
}
