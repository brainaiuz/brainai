package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.crm.client.ui.CrmAccountCoreListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VendorListView extends CrmAccountCoreListView implements Constants {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public VendorListView() {
        super(VENDOR_LIST);
        setDescription(hrmsStrings.vendors());
    }

    @Override
    protected ListingPanel initializeList() {
        return null;
    }

    @Override
    protected ListPanelType getListPanelType() {
        return null;
    }

    @Override
    protected CustomColumnDefinitionConfig[] getColumns() {
        return new CustomColumnDefinitionConfig[0];
    }

    @Override
    protected <T extends CrmAccountItem> ListingRequestProvider<T> getRequestProvider() {
        return null;
    }

    @Override
    protected ActionButton initializeTopMenuNew() {
        return null;
    }

    @Override
    protected boolean hasImportButton() {
        return false;
    }

    @Override
    protected String getImporterLink() {
        return null;
    }

    @Override
    protected void saveAccountsCellValue(CrmAccountItem rowValue, String columnCodeName) {

    }

    @Override
    protected ImportTypeEnum getImportType() {
        return null;
    }

    @Override
    protected String getPDFExporterLink() {
        return null;
    }

    @Override
    protected String getExcelExporterLink() {
        return null;
    }

    @Override
    protected FacetContentConfigure getContentConfigure() {
        return null;
    }

    @Override
    protected FacetCallbackProvider getFacetFilterCallbackProvider() {
        return null;
    }

    @Override
    protected void deleteSelection() {

    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected VerticalPanel getEmptyDataTable() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
