package cz.inqool.dl4dh.krameriusplus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.NameTagMetadata;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Token;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PageDto implements KrameriusModelAware, KrameriusDto<Page> {

    private String pid;

    @JsonProperty("root_pid")
    private String parentId;

    //TODO: change to enum?
    private String pageType;

    /**
     * Usually page number
     */
    private String title;

    private KrameriusModel model;

    /**
     * Page number with extracted from details field
     */
    private String pageNumber;

    /**
     * Integer representation of page number (removed every non-numeric character from pageNumber)
     */
    private int pageIndex;

    //TODO: change to enum?
    private String policy;

    private String textOcr;

    @JsonProperty("details")
    public void unpackDetails(Map<String, Object> details) {
        pageType = (String) details.get("type");
        pageNumber = ((String) details.get("pagenumber")).strip();
        try {
            pageIndex = RomanToNumber.romanToDecimal(pageNumber.replaceAll("[^IVXLCDM]", ""));
        } catch (IllegalArgumentException e) {
            try {
                pageIndex = Integer.parseInt(pageNumber.replaceAll("[^0-9]", ""));
            } catch (Exception ex) {
                // do nothing
            }
        }
    }

    @Override
    public Page toEntity() {
        Page page = new Page();
        page.setPid(pid);
        page.setParentId(parentId);
        page.setPageType(pageType);
        page.setPageNumber(pageNumber);
        page.setTitle(title);
        page.setPolicy(policy);
        page.setPageIndex(pageIndex);

        return page;
    }
}
