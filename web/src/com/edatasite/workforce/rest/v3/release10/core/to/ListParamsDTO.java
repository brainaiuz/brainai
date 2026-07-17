package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListParamsDTO {

    private String searchText;
    private List<ListingFilterDTO> filters;
    private Map<String, Object> customFilters;

    //selected date(might contains {createdDate, startDate, dueDate, etc.}) column for filter
    private String dateColumn;
    @JsonAlias({"startDate", "from"})
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonAlias({"endDate", "to"})
//    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private String sortBy;
    @Pattern(regexp = "ASC|DESC", message = "value of the sortAs must be ASC as Ascending or DESC as Descending")
    private String sortAs;
    private Integer currentPage;
    private Integer start = 0;
    @Size(max = 500)
    private Integer limit = 50;
    private ArrayList<Integer> objectIds;
    private String language;
    private String category;
    private String statusCode;
    private String relationType;
    private Integer relationID;
    private Integer columnMetadataId;
    private String accountType;

    public ListParamsDTO() {
    }

    public ListParamsDTO(String searchText, List<ListingFilterDTO> filters, Map<String, Object> customFilters, String dateColumn, Date startDate, Date endDate, String sortBy, String sortAs, Integer start, Integer limit, ArrayList<Integer> objectIds) {
        this.searchText = searchText;
        this.filters = filters;
        this.customFilters = customFilters;
        this.dateColumn = dateColumn;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sortBy = sortBy;
        this.sortAs = sortAs;
        this.start = start;
        this.limit = limit;
        this.objectIds = objectIds;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<ListingFilterDTO> getFilters() {
        return filters;
    }

    public void setFilters(List<ListingFilterDTO> filters) {
        this.filters = filters;
    }

    public Map<String, Object> getCustomFilters() {
        return customFilters;
    }

    public void setCustomFilters(Map<String, Object> customFilters) {
        this.customFilters = customFilters;
    }

    public String getDateColumn() {
        return dateColumn;
    }

    public void setDateColumn(String dateColumn) {
        this.dateColumn = dateColumn;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortAs() {
        return sortAs;
    }

    public void setSortAs(String sortAs) {
        this.sortAs = sortAs;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Integer getRelationID() {
        return relationID;
    }

    public void setRelationID(Integer relationID) {
        this.relationID = relationID;
    }

    public Integer getColumnMetadataId() {
        return columnMetadataId;
    }

    public void setColumnMetadataId(Integer columnMetadataId) {
        this.columnMetadataId = columnMetadataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListParamsDTO)) return false;

        ListParamsDTO that = (ListParamsDTO) o;

        if (searchText != null ? !searchText.equals(that.searchText) : that.searchText != null) return false;
        if (filters != null ? !filters.equals(that.filters) : that.filters != null) return false;
        if (customFilters != null ? !customFilters.equals(that.customFilters) : that.customFilters != null)
            return false;
        if (dateColumn != null ? !dateColumn.equals(that.dateColumn) : that.dateColumn != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (sortBy != null ? !sortBy.equals(that.sortBy) : that.sortBy != null) return false;
        if (sortAs != null ? !sortAs.equals(that.sortAs) : that.sortAs != null) return false;
        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        if (limit != null ? !limit.equals(that.limit) : that.limit != null) return false;
        return objectIds != null ? objectIds.equals(that.objectIds) : that.objectIds == null;
    }

    @Override
    public int hashCode() {
        int result = searchText != null ? searchText.hashCode() : 0;
        result = 31 * result + (filters != null ? filters.hashCode() : 0);
        result = 31 * result + (customFilters != null ? customFilters.hashCode() : 0);
        result = 31 * result + (dateColumn != null ? dateColumn.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (sortBy != null ? sortBy.hashCode() : 0);
        result = 31 * result + (sortAs != null ? sortAs.hashCode() : 0);
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (objectIds != null ? objectIds.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ListParamsDTO{" +
                "searchText='" + searchText + '\'' +
                ", filters=" + filters +
                ", customFilters=" + customFilters +
                ", dateColumn='" + dateColumn + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", sortBy='" + sortBy + '\'' +
                ", sortAs='" + sortAs + '\'' +
                ", start=" + start +
                ", limit=" + limit +
                ", objectIds=" + objectIds +
                '}';
    }

    public ArrayList<Integer> getObjectIds() {
        return objectIds;
    }

    public void setObjectIds(ArrayList<Integer> objectIds) {
        this.objectIds = objectIds;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
