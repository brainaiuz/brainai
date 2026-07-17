package com.edatasite.workforce.gwt.profile.client.ui.view.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldArea;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * User: Normurod Buriev
 * Date: 5/28/12
 * Time: 5:20 PM
 */
public class PMCustomFieldsListView extends CustomFieldsListView {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();

    public PMCustomFieldsListView() {
        super("pmcustomfields", settingsStrings.pmCustomFields());
    }

    @Override
    public String getIconStyle() {
        return "icon-pm-cutim";
    }

    protected ListingRequestProvider<CompanyCustomFieldItem> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setEntityName(getPMCFEntityNames());
            profileService.getCustomFields(filterParameter, new AbstractAsyncCallback<ListResult<CompanyCustomFieldItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<CompanyCustomFieldItem> customFields) {
                    callback.onSuccess(customFields);
                }
            });
        };
    }

    protected ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newLocation = getAddNewButton();

                newLocation.addClickHandler(baseEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|add/add/" + CustomFieldArea.PM));

                return newLocation;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };

    }

    private String getPMCFEntityNames() {
        String eNames = "";
        eNames += "'" + ViewName.Project.name() + "'";
        eNames += ",'" + ViewName.Task.name() + "'";
        eNames += ",'" + ViewName.Issues.name() + "'";
        eNames += ",'" + ViewName.Contract.name() + "'";
        return eNames;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}