package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.AbstractStepFactory;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobStep.EXPORT_STEP;

@Component
public class ExportStep extends AbstractStepFactory {

    private final ExportTasklet tasklet;

    @Autowired
    public ExportStep(ExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(EXPORT_STEP)
                .tasklet(tasklet)
                .build();
    }
}
