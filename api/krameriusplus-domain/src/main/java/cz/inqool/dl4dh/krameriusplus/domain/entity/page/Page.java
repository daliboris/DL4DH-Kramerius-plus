package cz.inqool.dl4dh.krameriusplus.domain.entity.page;

import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ParentAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.NameTagParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.OCRParadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.paradata.UDPipeParadata;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.PAGE;

/**
 * Object representing a page.
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document(collection = "pages")
public class Page extends DomainObject implements ParentAware {

    /**
     * Id of parent object. Multiple object types can contain pages, for example monographs, monographUnits or
     * periodicalItems
     */
    private String parentId;

    private String rootId;

    private List<Token> tokens = new ArrayList<>();

    private String policy;

    /**
     * Usually page number
     */
    private String title;

    /**
     * TODO: cleanup incoming data, turn into enum
     */
    private String pageType;

    /**
     * Storing it as a string for page numbers like "[1a]"
     */
    private String pageNumber;

    /**
     * Number of elements of type IllustrationType on page. Should represent the number of illustration on the page
     * recognized by the OCR. Information is obtained from ALTO format.
     */
    private int numberOfIllustrations = 0;

    @Indexed
    private int index;

    private NameTagMetadata nameTagMetadata;

    @Transient
    private OCRParadata ocrParadata;

    @Transient
    private UDPipeParadata udPipeParadata;

    @Transient
    private NameTagParadata nameTagParadata;

    private String teiBody;

    @Override
    public KrameriusModel getModel() {
        return PAGE;
    }
}