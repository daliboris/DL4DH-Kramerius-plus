package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Setter
public class LastExecutionDetails {
    @JsonIgnore
    private Long lastExecutionId;

    private String lastExecutionFailure;

    @Enumerated(EnumType.STRING)
    private JobStatus lastExecutionStatus = JobStatus.CREATED;
}
