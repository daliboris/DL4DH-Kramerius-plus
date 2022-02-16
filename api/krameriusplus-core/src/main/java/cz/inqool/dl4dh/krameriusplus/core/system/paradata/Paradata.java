package cz.inqool.dl4dh.krameriusplus.core.system.paradata;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Metadata about metadata
 */
@Getter
@Setter
public abstract class Paradata {

    protected Instant requestSent;

    public abstract ExternalServiceType getExternalServiceType();
}