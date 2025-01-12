package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto.EnrichmentRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.enrichmentrequest.dto.EnrichmentRequestDto;

public interface EnrichmentFacade {

    /**
     * Create and run multiple plans with the given configuration. For every publicationId, a new plan will be created.
     * A plan consists of multiple jobs, that will be launched in the given order. For every configuration in the given
     * list of configurations in requestDto, a new job will be created and placed into the plan. Jobs will be executed
     * one after the other in the given order.
     *
     * @param createDto
     */
    EnrichmentRequestDto enrich(EnrichmentRequestCreateDto createDto);

    EnrichmentRequestDto find(String enrichmentRequestId);

    QueryResults<EnrichmentRequestDto> list(String name, String owner, int page, int pageSize);
}
