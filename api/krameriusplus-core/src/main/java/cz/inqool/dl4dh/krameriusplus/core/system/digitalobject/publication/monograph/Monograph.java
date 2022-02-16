package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.monograph;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.KrameriusModel;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Object representing a Monograph. Monographs contain either monograph units as children, or directly pages. Can
 * not contain both
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@TypeAlias(KrameriusModel.MONOGRAPH)
@Document(collection = "publications")
public class Monograph extends Publication {

    private String partNumber;

    private String partTitle;

    private String donator;
}
