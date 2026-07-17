package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.AccessLogListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 12.06.2009
 * Time: 22:32:47
 * To change this template use File | Settings | File Templates.
 */
public class AccessLogListView extends BaseListView {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<AccessLogListItem> list;
    private static final BackendStrings backendStrings = BackendStrings.App.get();


    public AccessLogListView() {
        super("accessloglistview", backendStrings.accessLog());
    }
    public String getIconStyle() {
        return "backend accessLogListView";
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.AccessLogListPanel, drawColumns(), provider(), designer());

        list.getPdfVersion().setVisible(false);
        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadAccessLogListViewExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            list.callListExcel(excelURL, filterParametrs);
        });

        AccessLogListQuickView accessLogListQuickView = new AccessLogListQuickView(this);
        list.setQuickViewPanel(accessLogListQuickView);
        super.setQuickViewPanel(accessLogListQuickView);
        super.setListingPanel(list);
        super.display();
        return null;
    }

    private ListingPanelDesign designer() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage emptyMessage = new DefaultNoItemsMessage(backendStrings.currentlyThereAreNoAnyItems());
                emptyDataTable.initEmptyDataTable(emptyMessage);
            }
        };
    }

    private ListingRequestProvider<AccessLogListItem> provider() {
        return (filterParametrs, callback) -> {
            BackendServiceAsync accessLogService = BackendService.App.get();
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            accessLogService.getAccessLog(filterParametrs, new AbstractAsyncCallback<ListResult<AccessLogListItem>>() {
                public void success(ListResult<AccessLogListItem> result) {
                    callback.onSuccess(result);
                }

                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }
            });
        };
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        //Company ID
        columns[0] = new ColumnDefinitionConfig<AccessLogListItem, Integer>(backendStrings.companyID(), AccessLogListItem.COMPANY_ID, 65) {

            @Override
            public Integer getCellValue(AccessLogListItem item) {
                return item.getCompanyid();
            }
        };
        columns[0].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Company Name
        columns[1] = new ColumnDefinitionConfig<AccessLogListItem, String>(wfmStrings.companyName(), AccessLogListItem.COMPANY_NAME, 150) {

            @Override
            public String getCellValue(AccessLogListItem item) {
                return item.getCompanyName();
            }
        };
        //Users
        columns[2] = new ColumnDefinitionConfig<AccessLogListItem, String>(wfmStrings.user(), AccessLogListItem.USERS, 100) {

            @Override
            public String getCellValue(AccessLogListItem item) {
                return item.getUserName();
            }
        };
        //Email
        columns[3] = new ColumnDefinitionConfig<AccessLogListItem, String>(wfmStrings.email(), AccessLogListItem.EMAIL, 150) {

            @Override
            public String getCellValue(AccessLogListItem item) {
                return item.getEmail();
            }
        };
        //Date Accessed
        columns[4] = new ColumnDefinitionConfig<AccessLogListItem, Date>(backendStrings.dateAccessed(), AccessLogListItem.DATE_ACCESSED, 150) {

            @Override
            public Date getCellValue(AccessLogListItem item) {
                return item.getLastAccessDate();
            }
        };
        columns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        //Browser Type
        columns[5] = new ColumnDefinitionConfig<AccessLogListItem, String>(backendStrings.browser(), AccessLogListItem.BROWSER_TYPE_VERSION, 100) {

            @Override
            public String getCellValue(AccessLogListItem item) {
                return item.getBrowserType();
            }
        };
        //IP
        columns[6] = new ColumnDefinitionConfig<AccessLogListItem, String>(wfmStrings.ipaddress(), AccessLogListItem.IP, 100) {

            @Override
            public String getCellValue(AccessLogListItem item) {
                return item.getClientIpAddress();
            }
        };
        columns[6].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columns;
    }

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