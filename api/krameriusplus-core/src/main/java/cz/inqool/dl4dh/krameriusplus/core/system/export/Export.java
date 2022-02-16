package cz.inqool.dl4dh.krameriusplus.core.system.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.Instant;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias("export")
public class Export extends DomainObject {

    @DBRef
    @JsonIgnore
    private Publication publication;

    private FileRef fileRef;

    @Setter(AccessLevel.NONE)
    private Instant created;

    private Instant deleted;

    public Export() {
        this.created = Instant.now();
        this.id = java.util.UUID.randomUUID().toString();
    }

    public String getPublicationId() {
        return publication.getId();
    }

    public String getPublicationTitle() {
        return publication.getTitle();
    }
}