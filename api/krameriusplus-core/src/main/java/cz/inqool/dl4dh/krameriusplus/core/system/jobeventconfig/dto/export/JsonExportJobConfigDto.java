package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class JsonExportJobConfigDto extends ExportJobConfigDto {

    private Params params = new Params();

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_JSON;
    }

    @Override
    public Map<String, Object> getJobParameters() {
        return createJobParameters();
    }
}