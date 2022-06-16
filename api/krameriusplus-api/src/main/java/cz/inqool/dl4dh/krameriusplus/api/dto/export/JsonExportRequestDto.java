package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.JsonExportJobConfigDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class JsonExportRequestDto implements ExportRequestDto {

    @NotNull
    private String publicationId;

    @NotNull
    private JsonExportJobConfigDto config;
}
