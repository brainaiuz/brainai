package com.edatasite.workforce.rest.v3.release10.core.to;

import java.util.List;

public class PagedResultDTO<T> {

    private List<T> items;
    private long totalCount;

    public PagedResultDTO(List<T> items, long totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public long getTotalCount() {
        return totalCount;
    }
}
