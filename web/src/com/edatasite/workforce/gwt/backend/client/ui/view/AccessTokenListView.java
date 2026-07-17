package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.ui.view.imageBundle.BackendImageBundle;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: Abdurakhmonov Farrukh
 * Date: 11/10/17
 * Time: 12:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessTokenListView extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private BackendImageBundle imageBundle = (BackendImageBundle) GWT.create(BackendImageBundle.class);
    private ListingPanel<ApiAccessToken> listingPanel;

    public AccessTokenListView() {
        super("accessTokens", backendStrings.accessTokenList());
    }

    @Override
    public String getIconStyle() {
        return "backend overFeatListView";
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.AccessTokenListPanel, drawColumns(), drawProvider(), drawDesigns());
        add(listingPanel);
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];
        //action
        columns[0] = new ColumnDefinitionConfig<ApiAccessToken, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ApiAccessToken rowValue) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                edit.setCommand(() -> new AccessTokenModal(rowValue, () -> listingPanel.reloadPage()));
                actionItemCount++;
                menuBar.addItem(edit);
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<ApiAccessToken, String>(wfmStrings.accessToken(), "token", 80) {
            @Override
            public String getCellValue(ApiAccessToken rowValue) {
                return rowValue.getToken();
            }
        };
        columns[2] = new ColumnDefinitionConfig<ApiAccessToken, String>(wfmStrings.description(), "description", 100) {
            @Override
            public String getCellValue(ApiAccessToken rowValue) {
                return rowValue.getDescription();
            }
        };
        columns[3] = new ColumnDefinitionConfig<ApiAccessToken, Widget>(wfmStrings.status(), "status", 30) {
            @Override
            public Widget getCellValue(ApiAccessToken rowValue) {
                Image image = new Image(rowValue.getBlocked() != null && rowValue.getBlocked() ? imageBundle.off() : imageBundle.on());
                image.setSize("18px", "18px");
                image.setTitle(rowValue.getBlocked() != null && rowValue.getBlocked() ? "BLOCKED" : "UNBLOCKED");

                FlowPanel statusPanel = new FlowPanel();
                statusPanel.add(image);
                return statusPanel;
            }
        };
        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        columns[4] = new ColumnDefinitionConfig<ApiAccessToken, String>(backendStrings.moduleCode(), "moduleCode", 50) {
            @Override
            public String getCellValue(ApiAccessToken rowValue) {
                return getModuleName(rowValue.getModuleCode());
            }
        };
        columns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columns;
    }

    private String getModuleName(String moduleCode) {
        if(Utils.isNullOrEmpty(moduleCode)){
            return "";
        }
        switch (moduleCode){
            case PermissionConstants.ACCOUNTING_MODULE:
                return "Accounts";
            case PermissionConstants.PM_MODULE:
                return "Projects";
            case PermissionConstants.HRMS_MODULE:
                return "Humans";
            case PermissionConstants.CRM_MODULE:
                return "Sales";
            case PermissionConstants.PAYROLL:
                return "Payroll";
            case PermissionConstants.DOCUMENTS_CONTEXT:
                return "Documents";
            case PermissionConstants.IPHONE_APPS:
                return "Iphone Apps";
            case PermissionConstants.ANDROID_APPS:
                return "Android Apps";
            default:
                return "All";
        }
    }

    private ListingPanelDesign drawDesigns() {
        return new ListingPanelDesign() {
            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton actionButton = getAddNewButton();
                actionButton.addClickHandler(event -> new AccessTokenModal(null, () -> listingPanel.reloadPage()));
                return actionButton;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage noItemsMessage = new DefaultNoItemsMessage(backendStrings.noSalesQuotesText() + " " + "generated Access Tokens");
                emptyDataTable.initEmptyDataTable(noItemsMessage);
            }
        };
    }

    private ListingRequestProvider<ApiAccessToken> drawProvider() {
        return (filterParams, objectListingCallback) -> {
            if (filterParams == null) {
                filterParams = new ListingFilterParameter();
            }
            BackendService.App.get().getAccessTokenList(filterParams, new AbstractAsyncCallback<ListResult<ApiAccessToken>>() {
                @Override
                public void failure(Throwable throwable) {
                    objectListingCallback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<ApiAccessToken> result) {
                    objectListingCallback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}