package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 30-Aug-2010
 * Time: 15:08:48
 */
public abstract class ListingPanelDesign {

    abstract public ListingFacetFilter initFacetFilter();

    public HorizontalPanel initTopToolBarWidgets() {
        return null;
    }

    public ActionButton initTopToolBarNew() {
        return null;
    }

    public ActionButton initTopToolBarMore() {
        return null;
    }

    public ActionButton initTopToolBarPrint() {
        return null;
    }

    public ActionButton initTopToolBarMoreActions() {
        return null;
    }

    public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {

    }

    public ActionButton initializationReloadButton() {
        return null;
    }

    /*public void initBottomToolBarWidgets2(ExportImportOption exportOption, MaterialDropDown menu) {

    }*/

    abstract public ListingActionMenu initLeftTopActionMenu();

    abstract public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable);

    public boolean isShowCustomiseButton() {
        return true; //default return TRUE;
    }

    public boolean isShowDeleteButton() {
        return false; //default return false;
    }

    public boolean isEditCustomFieldCell() {
        return false; //default return FALSE;
    }

    public boolean isShowResetButton() {
        return true; //default return TRUE;
    }

    public Integer getTypeParentId() {
        return null;// If you are uses created Listing panel in child panel that must give him typeParentId
    }

    public Widget getAddAdditionalPanel() {
        return null;
    }

    public boolean isShowTableHeaders(){
        return true;
    }

    public String[] oddEvenRowColors(){
        return null;
    }

    public String getPageScrollTableId(){
        return null;
    }

    public boolean hasKanbanView() {
        return false;
    }

    public Widget getFirstAdditionalPanel() {
        return null;
    }
}
