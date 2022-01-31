package cz.inqool.dl4dh.krameriusplus.domain.entity.digitalobject.publication;

import cz.inqool.dl4dh.krameriusplus.domain.repo.AbstractCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EnhancedPublicationRepository extends AbstractCustomRepository<Publication> {

    @Autowired
    public EnhancedPublicationRepository(MongoTemplate mongoTemplate) {
        super(Publication.class, mongoTemplate);
    }
}
