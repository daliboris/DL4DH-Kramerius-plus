package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import org.apache.commons.io.FileUtils;
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
public class CleanUpExportTasklet extends ValidatedTasklet {

    /**
     * JobExecutionContext requires DIRECTORY and ZIPPED_FILE keys
     */
    @Override
    protected RepeatStatus executeValidatedTasklet(@NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) throws Exception {
        String path = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString(DIRECTORY);
        Path directory = Path.of(path);

        path = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().getString(ZIPPED_FILE);
        Path zippedFile = Path.of(path);

        Files.delete(zippedFile);
        FileUtils.deleteDirectory(directory.toFile());

        return RepeatStatus.FINISHED;
    }

    @Override
    protected Set<String> getRequiredExecutionContextKeys() {
        return Set.of(DIRECTORY, ZIPPED_FILE);
    }
}
