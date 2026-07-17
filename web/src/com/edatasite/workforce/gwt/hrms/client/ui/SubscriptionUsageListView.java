package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
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
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.SubscriptionItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.SubscriptionUsageItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

public class SubscriptionUsageListView extends BaseListView implements Constants {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<SubscriptionUsageItem> list;
    private ListingFilterParameter fp;

    public SubscriptionUsageListView() {
        super(USAGE_LIST);
        setDescription(hrmsStrings.usages());
    }
    @Override
    public String getIconStyle() {
        return null;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        list = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListData(), getListingDesign());

        add(list);
        return null;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.SubscriptionListPanel;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig column;

        column = new ColumnDefinitionConfig<SubscriptionUsageItem, Anchor>(wfmStrings.action(), "USAGE", LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(SubscriptionUsageItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("usage|summary/" + item.getId(), item.getEmployee().getName()));
                actionItemCount++;
                menuBar.addItem(summary);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-task-small");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("subscription|add/add/" + item.getId()));
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                delete.setCommand(() -> deleteItem(item.getId()));
                actionItemCount++;
                menuBar.addItem(delete);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionUsageItem, String>(wfmStrings.employee(), Constants.EMPLOYEE_NAME, 150) {
            @Override
            public String getCellValue(SubscriptionUsageItem item) {
                return item.getEmployee() != null ? item.getEmployee().getName() : null;
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionUsageItem, String>(hrmsStrings.vendors(), SubscriptionItem.VENDOR, 150) {
            @Override
            public String getCellValue(SubscriptionUsageItem item) {
                return item.getVendor() != null ? item.getVendor().getName() : null;
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionUsageItem, String>(wfmStrings.contact(), Constants.CONTACT, 100) {
            @Override
            public String getCellValue(SubscriptionUsageItem item) {
                return item.getContact() != null ? item.getContact().getName() : null;
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionUsageItem, String>(wfmStrings.date(), Constants.DATE, 100) {
            @Override
            public String getCellValue(SubscriptionUsageItem item) {
                return item.getDate() != null ? DateUtils.formatInternal(item.getDate().getDate()) : null;
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[0]);
    }

    private ListingRequestProvider<SubscriptionUsageItem> getListData() {
        return (fp, callback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            HrmsService.App.get().getUsageList(fp, new AsyncCallback<ListResult<SubscriptionUsageItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<SubscriptionUsageItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("usage|add/add"));
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.currentlyNoUsagesYet());
                message.setTextBeforeLink(hrmsStrings.youCanStartAddingUsagesByClicking());
                message.setHref("usage|add/add");
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void deleteItem(Integer id) {
        LoadingPanel.loading(true);
        HrmsService.App.get().deleteUsageItem(id, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Void result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), hrmsStrings.usages()), Info.Type.INFO);
                refresh();
            }
        });
    }

    public void refresh() {
        fp.setStart(0);
        list.reloadPage();
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
