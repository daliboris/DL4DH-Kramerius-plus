package cz.inqool.dl4dh.krameriusplus.domain.entity.periodical;

import cz.inqool.dl4dh.krameriusplus.domain.dao.repo.PageRepository;
import cz.inqool.dl4dh.krameriusplus.domain.entity.ParentAware;
import cz.inqool.dl4dh.krameriusplus.domain.entity.Publication;
import cz.inqool.dl4dh.krameriusplus.domain.entity.page.Page;
import cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.domain.enums.KrameriusModel.PERIODICAL_ITEM;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
public class PeriodicalItem extends Publication implements ParentAware {

    private String date;

    private String issueNumber;

    private String partNumber;

    private String parentId;

    private String rootId;

    private int index;

    @Transient
    private List<Page> pages = new ArrayList<>();

    @Override
    public KrameriusModel getModel() {
        return PERIODICAL_ITEM;
    }

    @Override
    public void addPages(PageRepository pageRepository, Pageable pageable) {
        pages = pageRepository.findByParentIdOrderByIndexAsc(id, pageable);
    }
}
