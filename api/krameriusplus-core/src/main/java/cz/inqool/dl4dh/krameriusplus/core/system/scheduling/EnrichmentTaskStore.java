package cz.inqool.dl4dh.krameriusplus.core.system.scheduling;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.DomainStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class EnrichmentTaskStore extends DomainStore<EnrichmentTask> {

    @Autowired
    public EnrichmentTaskStore(MongoOperations mongoOperations) {
        super(EnrichmentTask.class, mongoOperations);
    }
}