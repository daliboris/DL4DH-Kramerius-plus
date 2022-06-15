package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.PublicationService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.ZIPPED_FILE;

@Component
@StepScope
public class CreateExportTasklet implements Tasklet {

    private final FileService fileService;

    private final ExportService exportService;

    private final PublicationService publicationService;

    @Autowired
    public CreateExportTasklet(FileService fileService, ExportService exportService, PublicationService publicationService) {
        this.fileService = fileService;
        this.exportService = exportService;
        this.publicationService = publicationService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String path = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString(ZIPPED_FILE);
        Path zippedFile = Path.of(path);

        String publicationId = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getString(PUBLICATION_ID);
        String publicationTitle = publicationService.getTitle(publicationId);

        FileRef fileRef;

        try (InputStream is = new FileInputStream(zippedFile.toFile())) {
            fileRef = fileService.create(is,
                    Files.size(zippedFile),
                    zippedFile.getFileName().toString(),
                    "application/zip");
        }

        ExportCreateDto exportCreateDto = new ExportCreateDto();
        exportCreateDto.setPublicationId(publicationId);
        exportCreateDto.setPublicationTitle(publicationTitle);
        exportCreateDto.setFileRef(fileRef);

        exportService.create(exportCreateDto);

        return RepeatStatus.FINISHED;
    }
}