package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ListingFilterDTO {
    @NotNull
    @JsonAlias({"filterCodeName", "fieldCode"})
    private String filterCodeName;
    @JsonAlias({"items", "values"})
    private List<IdCode> items;

    public ListingFilterDTO() {
    }

    public ListingFilterDTO(String filterCodeName, List<IdCode> items) {
        this.filterCodeName = filterCodeName;
        this.items = items;
    }

    public String getFilterCodeName() {
        return filterCodeName;
    }

    public void setFilterCodeName(String filterCodeName) {
        this.filterCodeName = filterCodeName;
    }

    public List<IdCode> getItems() {
        return items;
    }

    public void setItems(List<IdCode> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListingFilterDTO)) return false;

        ListingFilterDTO that = (ListingFilterDTO) o;

        if (filterCodeName != null ? !filterCodeName.equals(that.filterCodeName) : that.filterCodeName != null)
            return false;
        if (items != null ? !items.equals(that.items) : that.items != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filterCodeName != null ? filterCodeName.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ListingFilterDTO{" +
                "filterCodeName='" + filterCodeName + '\'' +
                ", items=" + items +
                '}';
    }
}
