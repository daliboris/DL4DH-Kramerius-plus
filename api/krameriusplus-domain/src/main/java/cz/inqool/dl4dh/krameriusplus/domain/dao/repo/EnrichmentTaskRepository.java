package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface EnrichmentTaskRepository extends MongoRepository<EnrichmentTask, String> {

    List<EnrichmentTask> findEnrichmentTaskByState(EnrichmentTask.State state);

    List<EnrichmentTask> findEnrichmentTaskByRootPublicationId(String rootPublicationId);
}