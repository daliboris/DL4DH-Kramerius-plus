package cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paging {

    /**
     * Paging current page
     */
    private int page = 0;

    /**
     * Paging size
     */
    private int pageSize = 10;

    public static Paging of(int page, int pageSize) {
        return new Paging(page, pageSize);
    }
}
