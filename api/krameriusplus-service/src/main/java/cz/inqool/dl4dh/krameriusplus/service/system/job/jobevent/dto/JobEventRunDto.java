package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.dto;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO used for communicating data with ActiveMQ Queue.
 */
@Getter
@Setter
public class JobEventRunDto {

    private String id;

    private Instant created;

    private Long instanceId;

    private Long lastExecutionId;

    private KrameriusJob krameriusJob;

    private Map<String, Object> jobParametersMap = new HashMap<>();
}