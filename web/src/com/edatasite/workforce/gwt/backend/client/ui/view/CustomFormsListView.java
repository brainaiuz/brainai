package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 10.01.13
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public class CustomFormsListView extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final BackendServiceAsync backendService = BackendService.App.get();
    private ListingPanel<LayoutRPC> listing;
    private SchemaLookUp schemaLookUp;

    public CustomFormsListView() {
        super("customformlistview", wfmStrings.customForms());
    }

    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        listing = new ListingPanel<>(ListPanelType.CustomFieldsListPanel, getColumns(), getListData(), getDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOM_FORM_ADD, CustomFormsListView.this, (sender, args) -> listing.reloadPage());
        add(listing);
        return null;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<LayoutRPC, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final LayoutRPC item) {
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "edit-icon", () -> SinksContainerFactory.entryPoint.onHistoryChanged("customform|add/add/" + item.getObjectID() + "/" + schemaLookUp.getSelectedItemID()));
                menuBar.addItem(edit);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(1);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<LayoutRPC, String>(wfmStrings.title(), "title", 150) {
            @Override
            public String getCellValue(LayoutRPC item) {
                return item.getTitle();
            }
        };
        column.setMinimumColumnWidth(70);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<LayoutRPC, String>(wfmStrings.formName(), "formID", 150) {
            @Override
            public String getCellValue(LayoutRPC item) {
                return item.getFormID();
            }
        };
        column.setMinimumColumnWidth(70);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<LayoutRPC, String>(wfmStrings.active(), "active", 50) {
            @Override
            public String getCellValue(LayoutRPC item) {
                return item.isActive() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(35);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<LayoutRPC, String>(wfmStrings.type(), "type", 70) {
            @Override
            public String getCellValue(LayoutRPC item) {
                String type = "";
                if (item.isAddForm()) {
                    type += "Add";
                }
                if (item.isEditForm()) {
                    type += type.length() > 0 ? ", Edit" : "Edit";
                }
                if (item.isViewForm()) {
                    type += type.length() > 0 ? ", View" : "View";
                }
                if (item.isImportForm()) {
                    type += type.length() > 0 ? ", Import" : "Import";
                }
                return type;
            }
        };
        column.setMinimumColumnWidth(35);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<LayoutRPC> getListData() {
        return (filterParameter, callback) -> {
            filterParameter = filterParameter == null ? new ListingFilterParameter() : filterParameter;
            filterParameter.setCompanyID(schemaLookUp.getSelectedItemID());
            backendService.getCustomForms(filterParameter, new AbstractAsyncCallback<ListResult<LayoutRPC>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<LayoutRPC> customform) {
                    callback.onSuccess(customform);
                }
            });
        };
    }

    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customform|add/add//" + schemaLookUp.getSelectedItemID()));
                return addNew;
            }

            @Override
            public Widget getAddAdditionalPanel() {
                schemaLookUp = new SchemaLookUp();
                schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> listing.reloadPage());
                return schemaLookUp;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }
        };
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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
