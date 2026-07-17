package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.workforcetrack.mobile.rpc.base.MFacetFilter;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.contact.MContactCategoryList;
import com.workforcetrack.mobile.rpc.contact.MCountryList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/11/11
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "options")
public class MFilterParametrs implements Serializable {

    private Date startDate;
    private Date endDate;

    private String searchKey;

    //ListLoadConfig fields
    private Integer start;
    private Integer limit;
    private Integer sortDir; // 1 - Asc, 2 - Desc
    private String sortField;
    private Integer projectID;


    //ListingFilterParameter
    FacetFilterRpc facetFilter;

    //FacatFilterRPC for outlook, county, type
    private MCountryList countryList;
    private MContactCategoryList categoryList;

    //@XmlElement(name = "countryIDs")
    //private MCountryIDList countryIDList;

    @XmlElementWrapper(name = "countryIDs")
    @XmlElement(name = "countryID")
    List<Integer> countryIDList;

    @XmlElementWrapper(name = "contactCategoryIDs")
    @XmlElement(name = "contactCategoryID")
    List<Integer> contactCategoryIDList;

    private List<String> statusName;

    private Integer accountID;
    private Integer entityID;

    private MFacetFilter filter;

    //WFP News searchType
    private Integer searchType;
    private Integer type;

    //Employee location
    private Double latitude;
    private Double longitude;

    private String deviceID;
    private Integer employeeID;

    private String accountType;

    private String categoryType;

    private String startDateStr;

    private String endDateStr;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public MFacetFilter getFilter() {
        return filter;
    }

    public void setFilter(MFacetFilter filter) {
        this.filter = filter;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public List<String> getStatusName() {
        return statusName;
    }

    public void setStatusName(List<String> statusName) {
        this.statusName = statusName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public MFilterParametrs() {
    }

    public MFilterParametrs(String searchKey, Integer start, Integer limit) {
        this.searchKey = searchKey;
        this.start = start;
        this.limit = limit;
    }

    public ListingFilterParameter convertToFilterParametrs() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(this.startDate);
        fp.setEndDate(this.endDate);
        fp.setProjectId(this.projectID);
        fp.setSearchKey(this.searchKey);
        fp.setStart(this.start == null ? 0 : this.start);
        fp.setLimit(this.limit == null ? 0 : this.limit);
        fp.setSortDir(this.sortDir);
        fp.setSortField(this.sortField);
        fp.setType(this.type);
        fp.setAccountType(this.accountType);
        fp.setCategory(this.categoryType);
        fp.setStartDateStr(this.startDateStr);
        fp.setEndDateStr(this.endDateStr);
        //fp.setSearchType(this.searchType);

        return fp;
    }


    public ListingFilterParameter convertToListingFilterParameter(ListingFilterParameter lfp) {
        if (lfp == null)
            lfp = new ListingFilterParameter();
        lfp.setStartDate(this.startDate);
        lfp.setEndDate(this.endDate);

        lfp.setSearchKey(this.searchKey);
        lfp.setStart(this.start != null ? getStart() : 0);
        lfp.setLimit(this.limit != null ? getLimit() : 0);
        lfp.setSortField(this.sortField);
        lfp.setFacetFilter(this.facetFilter);
        lfp.setProjectId(WebServiceUtils.getNotZeroValue(getProjectID()));
        lfp.setType(WebServiceUtils.getNotZeroValue(getType()));
        lfp.setAccountID(WebServiceUtils.getNotZeroValue(getAccountID()));
        lfp.setFromMobile(true);
        return lfp;
    }

    public ListLoadConfig convertToListLoadConfig() {
        ListLoadConfig listLoadConfig = new ListLoadConfig();
        if (this.start != null)
            listLoadConfig.setStart(this.start);
        if (this.limit != null) {
            listLoadConfig.setLimit(this.limit);
        }
        if (this.sortDir != null) {
            listLoadConfig.setSortDir(this.sortDir);
        } else {
            listLoadConfig.setSortDir(1);
        }
        listLoadConfig.setSortField(this.sortField);

        return listLoadConfig;
    }

    public static boolean convert(ListingFilterParameter filterParametrs, MFilterParametrs mFilterParametrs, boolean fromFilterParametrs) {
        if (filterParametrs == null || mFilterParametrs == null)
            return false;
        try {

            if (fromFilterParametrs) {

                mFilterParametrs.setStartDate(filterParametrs.getStartDate());
                mFilterParametrs.setEndDate(filterParametrs.getEndDate());

                mFilterParametrs.setSearchKey(filterParametrs.getSearchKey());
                mFilterParametrs.setStart(filterParametrs.getStart());
                mFilterParametrs.setLimit(filterParametrs.getLimit());
                mFilterParametrs.setSortDir(filterParametrs.getSortDir());
                mFilterParametrs.setSortField(filterParametrs.getSortField());

                mFilterParametrs.setFacetFilter(filterParametrs.getFacetFilter());
            } else {
                filterParametrs.setStartDate(mFilterParametrs.getStartDate());
                filterParametrs.setEndDate(mFilterParametrs.getEndDate());

                filterParametrs.setSearchKey(mFilterParametrs.getSearchKey());
                filterParametrs.setStart(mFilterParametrs.getStart());
                filterParametrs.setLimit(mFilterParametrs.getLimit());
                filterParametrs.setSortDir(mFilterParametrs.getSortDir());
                filterParametrs.setSortField(mFilterParametrs.getSortField());

                filterParametrs.setFacetFilter(mFilterParametrs.getFacetFilter());
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean convertToListingFilterParameter(ListingFilterParameter filterParametrs, MFilterParametrs mFilterParametrs, boolean fromFilterParametrs) {
        if (filterParametrs == null || mFilterParametrs == null)
            return false;
        try {

            if (fromFilterParametrs) {

                mFilterParametrs.setStartDate(filterParametrs.getStartDate());
                mFilterParametrs.setEndDate(filterParametrs.getEndDate());

                mFilterParametrs.setSearchKey(filterParametrs.getSearchKey());
                mFilterParametrs.setStart(filterParametrs.getStart());
                mFilterParametrs.setLimit(filterParametrs.getLimit());
                mFilterParametrs.setSortDir(filterParametrs.getSortDir());
                mFilterParametrs.setSortField(filterParametrs.getSortField());

                mFilterParametrs.setFacetFilter(filterParametrs.getFacetFilter());
            } else {
                filterParametrs.setStartDate(mFilterParametrs.getStartDate());
                filterParametrs.setEndDate(mFilterParametrs.getEndDate());

                filterParametrs.setSearchKey(mFilterParametrs.getSearchKey());
                filterParametrs.setStart(mFilterParametrs.getStart());
                filterParametrs.setLimit(mFilterParametrs.getLimit());
                filterParametrs.setSortDir(mFilterParametrs.getSortDir());
                filterParametrs.setSortField(mFilterParametrs.getSortField());

                filterParametrs.setFacetFilter(mFilterParametrs.getFacetFilter());
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean convertToListLoadConfig(ListLoadConfig listLoadConfig, MFilterParametrs mFilterParametrs, boolean fromListLoadConfig) {
        if (listLoadConfig == null || mFilterParametrs == null)
            return false;
        try {

            if (fromListLoadConfig) {

                mFilterParametrs.setStart(listLoadConfig.getStart());
                mFilterParametrs.setLimit(listLoadConfig.getLimit());
                mFilterParametrs.setSortDir(listLoadConfig.getSortDir());
                mFilterParametrs.setSortField(listLoadConfig.getSortField());
            } else {
                listLoadConfig.setStart(mFilterParametrs.getStart());
                listLoadConfig.setLimit(mFilterParametrs.getLimit());
                listLoadConfig.setSortDir(mFilterParametrs.getSortDir() != null ? mFilterParametrs.getSortDir() : 0);
                listLoadConfig.setSortField(mFilterParametrs.getSortField());

            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Integer> getContactCategoryIDList() {
        return contactCategoryIDList;
    }

    public void setContactCategoryIDList(List<Integer> contactCategoryIDList) {
        this.contactCategoryIDList = contactCategoryIDList;
    }

    public List<Integer> getCountryIDList() {
        return countryIDList;
    }

    public void setCountryIDList(List<Integer> countryIDList) {
        this.countryIDList = countryIDList;
    }

    /*

        public MCountryIDList getCountryIDList() {
            return countryIDList;
        }

        public void setCountryIDList(MCountryIDList countryIDList) {
            this.countryIDList = countryIDList;
        }
    */

    public MContactCategoryList getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(MContactCategoryList categoryList) {
        this.categoryList = categoryList;
    }

    public MCountryList getCountryList() {
        return countryList;
    }

    public void setCountryList(MCountryList countryList) {
        this.countryList = countryList;
    }

    public FacetFilterRpc getFacetFilter() {
        return facetFilter;
    }

    public void setFacetFilter(FacetFilterRpc facetFilter) {
        this.facetFilter = facetFilter;
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

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
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

    public Integer getSortDir() {
        return sortDir;
    }

    public void setSortDir(Integer sortDir) {
        this.sortDir = sortDir;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }
}
