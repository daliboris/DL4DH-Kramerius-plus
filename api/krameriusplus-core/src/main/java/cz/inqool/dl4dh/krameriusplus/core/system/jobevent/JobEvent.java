package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobEventConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.*;

/**
 * Custom entity representing an abstraction over spring's JobInstance fitted for Kramerius+
 */
@Getter
@Setter
@Entity
@ToString
public class JobEvent extends DatedObject {

    private String publicationId;

    @Column(unique = true)
    @NotNull
    private Long instanceId;

    @ManyToOne
    private JobEvent parent;

    @Embedded
    private ExecutionDetails details = new ExecutionDetails();

    @Embedded
    private JobEventConfig config;

    public boolean wasExecuted() {
        return details.getLastExecutionStatus() == JobStatus.COMPLETED;
    }

    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = new HashMap<>();
        jobParametersMap.put(JOB_EVENT_ID, id);
        jobParametersMap.put(PUBLICATION_ID, publicationId);
        jobParametersMap.put(KRAMERIUS_JOB, config.getKrameriusJob().name());
        jobParametersMap.putAll(config.getParameters());

        return jobParametersMap;
    }
}
