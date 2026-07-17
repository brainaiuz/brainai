package com.edatasite.workforce.rest.v3.release10.core.to;

import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PatchRequestDto {
    @NotNull(message = "Id is required")
    private Integer id;
    private List<CustomFieldRequest> columns;

    public PatchRequestDto() {
    }

    public PatchRequestDto(Integer id, List<CustomFieldRequest> columns) {
        this.id = id;
        this.columns = columns;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CustomFieldRequest> getColumns() {
        return columns;
    }

    public void setColumns(List<CustomFieldRequest> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatchRequestDto)) return false;

        PatchRequestDto that = (PatchRequestDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (columns != null ? !columns.equals(that.columns) : that.columns != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PatchRequestDto{" +
                "id=" + id +
                ", columns=" + columns +
                '}';
    }
}
