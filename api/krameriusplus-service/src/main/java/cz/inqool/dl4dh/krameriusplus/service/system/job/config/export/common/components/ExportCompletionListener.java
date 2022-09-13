package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components;

import cz.inqool.dl4dh.krameriusplus.core.system.export.BulkExportStore;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportStore;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.ExportRequestStore;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanStore;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.JOB_EVENT_ID;

@Component
public class ExportCompletionListener implements JobExecutionListener {

    private final ExportStore exportStore;

    private final JobPlanStore jobPlanStore;

    private final ExportRequestStore exportRequestStore;

    @Autowired
    public ExportCompletionListener(ExportStore exportStore, BulkExportStore bulkExportStore, JobPlanStore jobPlanStore, ExportRequestStore exportRequestStore) {
        this.exportStore = exportStore;
        this.jobPlanStore = jobPlanStore;
        this.exportRequestStore = exportRequestStore;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // do nothing
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        String jobEventId = jobExecution.getJobParameters().getString(JOB_EVENT_ID);
        ExportRequest exportRequest = exportRequestStore.findByJobPlan(jobPlanStore.findByJobEvent(jobEventId).getId());

        if (jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
            Export export = exportStore.findByJobEvent(jobEventId);

            exportRequest.getExports().add(export);

            exportRequestStore.update(exportRequest);
        }
    }
}
