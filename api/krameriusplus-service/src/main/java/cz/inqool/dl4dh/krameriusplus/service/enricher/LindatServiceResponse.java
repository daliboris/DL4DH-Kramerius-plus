package cz.inqool.dl4dh.krameriusplus.service.enricher;

import lombok.Getter;
import lombok.Setter;

/**
 * Class for representing the response from Lindat web services
 *
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class LindatServiceResponse {

    private String model;

    private String[] acknowledgements;

    private String result;
}
