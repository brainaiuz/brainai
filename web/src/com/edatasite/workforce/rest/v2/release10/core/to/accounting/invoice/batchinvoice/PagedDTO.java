package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.batchinvoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.List;

public class PagedDTO<T> extends ResponseData {

    private List<T> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PagedDTO() {
    }

    public PagedDTO(List<T> items, int page, int size, long totalElements, int totalPages, boolean last) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagedDTO)) return false;

        PagedDTO<?> pagedDTO = (PagedDTO<?>) o;

        if (page != pagedDTO.page) return false;
        if (size != pagedDTO.size) return false;
        if (totalElements != pagedDTO.totalElements) return false;
        if (totalPages != pagedDTO.totalPages) return false;
        if (last != pagedDTO.last) return false;
        if (items != null ? !items.equals(pagedDTO.items) : pagedDTO.items != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + page;
        result = 31 * result + size;
        result = 31 * result + (int) (totalElements ^ (totalElements >>> 32));
        result = 31 * result + totalPages;
        result = 31 * result + (last ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PagedDTO{" +
                "items=" + items +
                ", page=" + page +
                ", size=" + size +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                ", last=" + last +
                '}';
    }
}
