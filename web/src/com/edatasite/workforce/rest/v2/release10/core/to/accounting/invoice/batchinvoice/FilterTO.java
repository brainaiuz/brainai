package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.batchinvoice;

/**
 * Created by Dilsh0d Madrahimov on 1/18/2019.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterTO implements Serializable {
    private Date fromDate;
    private Date toDate;
    private Integer page;
    private Integer size;
    private String sorted_column;
    private String sort_type;
    private String entity_type;

    public FilterTO() {
    }

    public FilterTO(Date fromDate, Date toDate, Integer page, Integer size, String sorted_column, String sort_type, String entity_type) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.page = page;
        this.size = size;
        this.sorted_column = sorted_column;
        this.sort_type = sort_type;
        this.entity_type = entity_type;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSorted_column() {
        return sorted_column;
    }

    public void setSorted_column(String sorted_column) {
        this.sorted_column = sorted_column;
    }

    public String getSort_type() {
        return sort_type;
    }

    public void setSort_type(String sort_type) {
        this.sort_type = sort_type;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterTO)) return false;

        FilterTO filterTO = (FilterTO) o;

        if (fromDate != null ? !fromDate.equals(filterTO.fromDate) : filterTO.fromDate != null) return false;
        if (toDate != null ? !toDate.equals(filterTO.toDate) : filterTO.toDate != null) return false;
        if (page != null ? !page.equals(filterTO.page) : filterTO.page != null) return false;
        if (size != null ? !size.equals(filterTO.size) : filterTO.size != null) return false;
        if (sorted_column != null ? !sorted_column.equals(filterTO.sorted_column) : filterTO.sorted_column != null)
            return false;
        if (sort_type != null ? !sort_type.equals(filterTO.sort_type) : filterTO.sort_type != null) return false;
        if (entity_type != null ? !entity_type.equals(filterTO.entity_type) : filterTO.entity_type != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fromDate != null ? fromDate.hashCode() : 0;
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0);
        result = 31 * result + (page != null ? page.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (sorted_column != null ? sorted_column.hashCode() : 0);
        result = 31 * result + (sort_type != null ? sort_type.hashCode() : 0);
        result = 31 * result + (entity_type != null ? entity_type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FilterTO{" +
                "fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", page=" + page +
                ", size=" + size +
                ", sorted_column='" + sorted_column + '\'' +
                ", sort_type='" + sort_type + '\'' +
                ", entity_type='" + entity_type + '\'' +
                '}';
    }
}
