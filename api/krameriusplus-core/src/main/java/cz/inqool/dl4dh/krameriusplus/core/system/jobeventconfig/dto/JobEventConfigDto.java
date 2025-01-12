package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class JobEventConfigDto {

    @Schema(hidden = true)
    public abstract KrameriusJob getKrameriusJob();

    @Schema(hidden = true)
    public abstract Map<String, Object> toJobParametersMap();
}
