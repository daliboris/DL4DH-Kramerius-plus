package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.DELIMITER;

@Getter
@Setter
public class CsvExportJobConfigDto extends ExportJobConfigDto {

    @Schema(description = "Delimiter used to generate export.", defaultValue = ",")
    private String delimiter = ",";

    private Params params = new Params();

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_CSV;
    }

    @Override
    public Map<String, Object> getJobParameters() {
        Map<String, Object> parameters = createJobParameters();
        parameters.put(DELIMITER, delimiter);

        return parameters;
    }
}