package mini.integration.lib.module;

import lombok.Getter;
import lombok.Setter;
import mini.integration.lib.module.util.ObjectMapperLib;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse {
    @Setter
    private List<?> data;
    private long size;
    private int page;
    private long totalElement;
    private long totalPage;
    private boolean isLast;
    private boolean isFirst;
    private boolean isEmpty;
    private boolean hasNext;
    private boolean hasPrevious;

    private PageResponse(Page<?> page, Class<?> t) {
        this.data = ObjectMapperLib.mapList(page.getContent(), t, ObjectMapperLib.ObjectMapperRule.UNRESTRICTED);
        this.size = page.getSize();
        this.page = page.getNumber();
        this.totalElement = page.getTotalElements();
        this.totalPage = page.getTotalPages();
        this.isLast = page.isLast();
        this.isFirst = page.isFirst();
        this.isEmpty = page.isEmpty();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }

    public static PageResponse of(Page<?> page, Class<?> t) {
        return new PageResponse(page, t);
    }
}
