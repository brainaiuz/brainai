package com.edatasite.workforce.gwt.core.client.rpc.facet;


import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 03-Jun-2011
 * Time: 12:55:38
 */
public class FacetContentConfigure {
    /**
     * Must column count not equals in zero
     *
     * @return
     */
    private int howMuchColumn;
    /**
     * Facet Popup title name
     */
    private String facetFilterTitle;

    /**
     * Facet Filter Content Code Name. CodeName list size must not equals in zero
     */
    private ArrayList<String> showCodeNameList = new ArrayList<>();

    /**
     * Map key content code name and value content title name
     */
    private HashMap<String, String> contentTitle = new HashMap<>();
    /**
     * Map key content code name and value Solr Facet Fileds
     */
    private HashMap<String, FacetSolrField> showSolrFields = new HashMap<>();

    /**
     * Map key content code name and value Solr Facet Fileds
     */
    private HashMap<String, FacetSolrField> hideSolrFields = new HashMap<>();

    /**
     * Map key content code name andm value Solr Facet Fields
     */
    private ArrayList<SelectItem> solrDateFields = new ArrayList<>();

    private boolean isRangePanelEnabled = false;
    private boolean isDatePeriodPanelEnabled = true;


    public FacetContentConfigure(int howMuchColumn, String facetFilterTitle) {
        this.howMuchColumn = howMuchColumn;
        this.facetFilterTitle = facetFilterTitle;
    }

    /**
     * Facet Filter contents add codeName and contentTitle
     * You add content show add order
     *
     * @param codeName     Not equals space and each code name must unique name
     * @param contentTitle This is content title name
     */
    public void addContentConfigure(String codeName, String contentTitle, FacetFieldConfigure solrConfigureFields) {
        this.contentTitle.put(codeName, contentTitle);

        if (solrConfigureFields.isShowFacetConttentFilter()) {
            this.showCodeNameList.add(codeName);
            this.showSolrFields.put(codeName, solrConfigureFields.getFacetFieldConfigure());
        } else {
            this.hideSolrFields.put(codeName, solrConfigureFields.getFacetFieldConfigure());
        }
    }

    /**
     * Facet Filter contents add solrDateFiledName and listName
     * You add Filter by show add order
     *
     * @param solrDateFiledName Not equals space and each code name must unique name
     * @param listName          This is list box show name
     */
    public void addContentConfigureDateListBox(String solrDateFiledName, String listName) {
        this.solrDateFields.add(new SelectItem(solrDateFiledName.hashCode(), listName, solrDateFiledName));
    }

    public HashMap<String, String> getContentTitle() {
        return contentTitle;
    }

    public String getContentTitle(String codeName) {
        return contentTitle.get(codeName);
    }

    public int getHowMuchColumn() {
        return 2;//howMuchColumn TODO;
    }

    public String getFacetFilterTitle() {
        return facetFilterTitle;
    }

    public ArrayList<String> getShowCodeNameCloneList() {
        return (ArrayList<String>) showCodeNameList.clone();
    }

    public HashMap<String, FacetSolrField> getShowSolrCloneFields() {
        return (HashMap<String, FacetSolrField>) showSolrFields.clone();
    }

    public HashMap<String, FacetSolrField> getHideSolrCloneFields() {
        return (HashMap<String, FacetSolrField>) hideSolrFields.clone();
    }

    public ArrayList<SelectItem> getSolrDateFields() {
        return (ArrayList<SelectItem>) solrDateFields.clone();
    }

    public boolean isRangePanelEnabled() {
        return isRangePanelEnabled;
    }

    public void setRangePanelEnabled(boolean isRangePanelEnabled) {
        this.isRangePanelEnabled = isRangePanelEnabled;
    }

    public boolean isDatePeriodPanelEnabled() {
        return isDatePeriodPanelEnabled;
    }

    public void setDatePeriodPanelEnabled(boolean datePeriodPanelEnabled) {
        isDatePeriodPanelEnabled = datePeriodPanelEnabled;
    }
}
