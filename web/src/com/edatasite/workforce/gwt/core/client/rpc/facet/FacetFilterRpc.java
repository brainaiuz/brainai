package com.edatasite.workforce.gwt.core.client.rpc.facet;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetSettingRpc;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 02-Aug-2010
 * Time: 19:48:22
 */
public class FacetFilterRpc implements FacetFilterCutomField, IsSerializable {

    private String name;
    private Integer objectID;
    private boolean filterChanges = false;
    private boolean defaultFilter = false;
    private boolean publicFilter = false;
    private boolean applyFilter = false;
    private boolean favourFilter = false;
    private boolean isSystemFilter = false;
    private boolean isOverallSearch = false;

    private Date startDate;
    private Date endDate;
    private Integer more;
    private Integer less;
    private String selectedDateSolrCodeName;
    private Integer typeId;
    private ListPanelType type;
    private ArrayList<String> showFacetCodeName;
    private HashMap<String, String> customData;
    private HashMap<String, FacetSolrField> showSolrFieldMap;
    private HashMap<String, FacetSolrField> hideSolrFieldMap;
    private HashMap<String, FacetContentRpc> facetContentMap;
    private ArrayList<SelectItem> dateFilterByList;
    private HashMap<String, FacetSettingRpc> facetSettingMap;
    private Integer userID;
    private boolean HRMS;
    private boolean active;
    private boolean allEmployees;
    private Integer dateTermId;
    private Integer departmentId;

    public FacetFilterRpc() {
    }

    public FacetFilterRpc(ListPanelType type, HashMap<String, FacetSolrField> showSolrFieldMap, ArrayList<String> showFacetCodeName) {
        this.type = type;
        this.showSolrFieldMap = showSolrFieldMap;
        this.showFacetCodeName = showFacetCodeName;

        this.facetContentMap = new HashMap<>();

        for (String codeName : showFacetCodeName) {
            this.facetContentMap.put(codeName, new FacetContentRpc());
        }
    }

    public FacetFilterRpc(ArrayList<String> codeNameList, HashMap<String, FacetSolrField> showSolrFieldMap) {
        this.showFacetCodeName = codeNameList;
        this.facetContentMap = new HashMap<>();
        this.showSolrFieldMap = showSolrFieldMap;
        for (String aCodeNameList : codeNameList) {
            FacetContentRpc content = new FacetContentRpc();
            facetContentMap.put(aCodeNameList, content);
        }
    }

    public FacetFilterRpc(ArrayList<String> codeNameList, HashMap<String, FacetSolrField> showSolrFieldMap, HashMap<String, FacetSolrField> hideSolrFieldMap, ArrayList<SelectItem> dateFilterByList) {
        this.showFacetCodeName = codeNameList;
        this.facetContentMap = new HashMap<>();
        this.showSolrFieldMap = showSolrFieldMap;
        this.hideSolrFieldMap = hideSolrFieldMap;
        this.dateFilterByList = dateFilterByList;
        for (String aCodeNameList : codeNameList) {
            FacetContentRpc content = new FacetContentRpc();
            facetContentMap.put(aCodeNameList, content);
        }
    }

    public String[] getSolrFieldMapCodeList(String... removeAmount) {
        HashMap<String, FacetSolrField> temp = (HashMap<String, FacetSolrField>) getShowSolrFieldMap().clone();

        if (removeAmount != null && removeAmount.length > 0) {
            for (String s : removeAmount) {
                temp.remove(s);
            }
        }

        String[] solrFieldMapCodeList = new String[temp.keySet().size()];
        temp.keySet().toArray(solrFieldMapCodeList);
        return solrFieldMapCodeList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isFilterChanges() {
        return filterChanges;
    }

    public void setFilterChanges(boolean filterChanges) {
        this.filterChanges = filterChanges;
    }

    public boolean isDefaultFilter() {
        return defaultFilter;
    }

    public void setDefaultFilter(boolean defaultFilter) {
        this.defaultFilter = defaultFilter;
    }

    public boolean isApplyFilter() {
        return applyFilter;
    }

    public void setApplyFilter(boolean applyFilter) {
        this.applyFilter = applyFilter;
    }

    public boolean isSystemFilter() {
        return isSystemFilter;
    }

    public void setSystemFilter(boolean systemFilter) {
        isSystemFilter = systemFilter;
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

    public String getSelectedDateSolrCodeName() {
        return selectedDateSolrCodeName;
    }

    public void setSelectedDateSolrCodeName(String selectedDateSolrCodeName) {
        this.selectedDateSolrCodeName = selectedDateSolrCodeName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public boolean isFavourFilter() {
        return favourFilter;
    }

    public void setFavourFilter(boolean favourFilter) {
        this.favourFilter = favourFilter;
    }

    public Integer getLess() {
        return less;
    }

    public void setLess(Integer less) {
        this.less = less;
    }

    public Integer getMore() {
        return more;
    }

    public void setMore(Integer more) {
        this.more = more;
    }

    public ListPanelType getType() {
        if (type == null) {
            return null;
        }
        type.setId(typeId);
        return type;
    }

    public void setType(ListPanelType type) {
        this.type = type;
    }

    public HashMap<String, String> getCustomData() {
        if (customData == null) {
            customData = new HashMap<>();
        }
        return customData;
    }

    public void setCustomData(HashMap<String, String> customData) {
        this.customData = customData;
    }

    public HashMap<String, FacetContentRpc> getFacetContentMap() {
        if (facetContentMap == null) {
            facetContentMap = new HashMap<>();
        }
        return facetContentMap;
    }

    public void setFacetContentMap(HashMap<String, FacetContentRpc> facetContentMap) {
        this.facetContentMap = facetContentMap;
    }

    public ArrayList<String> getShowFacetCodeName() {
        if (showFacetCodeName == null) {
            showFacetCodeName = new ArrayList<>();
        }
        return showFacetCodeName;
    }

    public void setShowFacetCodeName(ArrayList<String> showFacetCodeName) {
        this.showFacetCodeName = showFacetCodeName;
    }

    public HashMap<String, FacetSolrField> getShowSolrFieldMap() {
        if (showSolrFieldMap == null) {
            showSolrFieldMap = new HashMap<>();
        }
        return showSolrFieldMap;
    }

    public void setShowSolrFieldMap(HashMap<String, FacetSolrField> showSolrFieldMap) {
        this.showSolrFieldMap = showSolrFieldMap;
    }

    public HashMap<String, FacetSolrField> getHideSolrFieldMap() {
        if (hideSolrFieldMap == null) {
            hideSolrFieldMap = new HashMap<>();
        }
        return hideSolrFieldMap;
    }

    public void setHideSolrFieldMap(HashMap<String, FacetSolrField> hideSolrFieldMap) {
        this.hideSolrFieldMap = hideSolrFieldMap;
    }

    public ArrayList<SelectItem> getDateFilterByList() {
        if (dateFilterByList == null) {
            dateFilterByList = new ArrayList<>();
        }
        return dateFilterByList;
    }

    public void setDateFilterByList(ArrayList<SelectItem> dateFilterByList) {
        this.dateFilterByList = dateFilterByList;
    }

    public HashMap<String, FacetSettingRpc> getFacetSettingMap() {
        if (facetSettingMap == null) {
            facetSettingMap = new HashMap<>();
        }
        return facetSettingMap;
    }

    public void setFacetSettingMap(HashMap<String, FacetSettingRpc> facetSettingMap) {
        this.facetSettingMap = facetSettingMap;
    }

    public void setCustomDataPut(String key, String value) {
        if (value != null && !"null".equals(value)) {
            getCustomData().put(key, value);
        }
    }

    public String getCustomDataValue(String key) {
        return getCustomData().get(key);
    }

    public String getSearchKey() {
        return getCustomData().get(SEARCHKEY);
    }

    public void setSearchKey(String searchValue) {
        getCustomData().put(SEARCHKEY, searchValue);
    }

    public boolean isPublicFilter() {
        return publicFilter;
    }

    public void setPublicFilter(boolean publicFilter) {
        this.publicFilter = publicFilter;
    }

    public void clear() {
        setName(null);
        setObjectID(null);
        setStartDate(null);
        setEndDate(null);
        for (String key : getFacetContentMap().keySet()) {
            getFacetContentMap().get(key).setSavedItems(null);
        }
    }

    public boolean isOverallSearch() {
        return isOverallSearch;
    }

    public void setOverallSearch(boolean overallSearch) {
        isOverallSearch = overallSearch;
    }

    public boolean isBuildShowFacetContent() {
        if (getFacetSettingMap().size() > 0) {
            for (String facetCodeName : getFacetSettingMap().keySet()) {
                if (getHideSolrFieldMap().containsKey(facetCodeName)) {
                    getShowSolrFieldMap().put(facetCodeName, getHideSolrFieldMap().get(facetCodeName));
                    if (!getFacetContentMap().containsKey(facetCodeName)) {
                        getFacetContentMap().put(facetCodeName, new FacetContentRpc());
                    }
                    if (!getShowFacetCodeName().contains(facetCodeName)) {
                        getShowFacetCodeName().add(facetCodeName);
                    }
                    getHideSolrFieldMap().remove(facetCodeName);

                }
            }
            ArrayList<String> removeFacetCodeName = new ArrayList<>();
            for (String facetCodeName : getShowSolrFieldMap().keySet()) {
                if (getFacetSettingMap().size() > 0 && !getFacetSettingMap().containsKey(facetCodeName)) {
                    getHideSolrFieldMap().put(facetCodeName, getShowSolrFieldMap().get(facetCodeName));
                    getFacetContentMap().remove(facetCodeName);
                    getShowFacetCodeName().remove(facetCodeName);
                    removeFacetCodeName.add(facetCodeName);
                }
            }
            for (String facetCodeName : removeFacetCodeName) {
                getShowSolrFieldMap().remove(facetCodeName);
            }
            return true;
        }
        return false;
    }


    public void removeHideFacetContent(String hideFacetCodeName) {
        getShowSolrFieldMap().put(hideFacetCodeName, getHideSolrFieldMap().get(hideFacetCodeName));
        getFacetContentMap().put(hideFacetCodeName, new FacetContentRpc());
        getShowFacetCodeName().add(hideFacetCodeName);
        getHideSolrFieldMap().remove(hideFacetCodeName);
    }

    public void removeShowFacetCodeName(String facetCodeName) {
        getHideSolrFieldMap().put(facetCodeName, getShowSolrFieldMap().get(facetCodeName));
        getFacetContentMap().remove(facetCodeName);
        getFacetSettingMap().remove(facetCodeName);
        getShowSolrFieldMap().remove(facetCodeName);
        getShowFacetCodeName().remove(facetCodeName);
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserID() {
        return userID;
    }

    public boolean isHRMS() {
        return HRMS;
    }

    public void setHRMS(boolean HRMS) {
        this.HRMS = HRMS;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAllEmployees() {
        return this.allEmployees;
    }

    public void setAllEmployees(final boolean allEmployees) {
        this.allEmployees = allEmployees;
    }

    public Integer getDateTermId() {
        return dateTermId;
    }

    public void setDateTermId(Integer dateTermId) {
        this.dateTermId = dateTermId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
