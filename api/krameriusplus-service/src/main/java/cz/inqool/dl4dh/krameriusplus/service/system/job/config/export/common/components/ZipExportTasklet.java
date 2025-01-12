package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.ZipArchiver;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.ZIPPED_FILE;

@Component
@StepScope
public class ZipExportTasklet extends ValidatedTasklet {

    /**
     * JobExecutionContext requires DIRECTORY key
     */
    @Override
    protected RepeatStatus executeValidatedTasklet(@NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) throws Exception {
        String directory = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString(DIRECTORY);
        Path directoryToZip = Path.of(directory);

        Path resultPath = Files.createFile(Path.of(chunkContext.getStepContext().getJobExecutionContext().get(DIRECTORY) + ".zip"));

        ZipArchiver zipArchiver = new ZipArchiver(resultPath);
        zipArchiver.zip(directoryToZip);

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(ZIPPED_FILE, resultPath.toString());

        return RepeatStatus.FINISHED;
    }

    @Override
    protected Set<String> getRequiredExecutionContextKeys() {
        return Set.of(DIRECTORY);
    }
}
