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
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

public class SubscriptionListView extends BaseListView implements Constants {
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<SubscriptionItem> list;
    private ListingFilterParameter fp;

    public SubscriptionListView() {
        super(SUBSCRIPTION_LIST);
        setDescription(wfmStrings.subscriptions());
    }

    public SubscriptionListView(Integer employeeId) {
        this();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setEmployeeId(employeeId);
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

        column = new ColumnDefinitionConfig<SubscriptionItem, Anchor>(wfmStrings.action(), "SUBSCRIPTION", LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(SubscriptionItem item) {

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("subscription|summary/" + item.getId(), item.getEmployee().getName()));
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

        column = new ColumnDefinitionConfig<SubscriptionItem, String>(wfmStrings.employee(), Constants.EMPLOYEE_NAME, 150) {
            @Override
            public String getCellValue(SubscriptionItem item) {
                return item.getEmployee() != null ? item.getEmployee().getName() : null;
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionItem, String>(hrmsStrings.vendors(), SubscriptionItem.VENDOR, 150) {
            @Override
            public String getCellValue(SubscriptionItem item) {
                return item.getVendor() != null ? item.getVendor().getName() : null;
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionItem, String>(wfmStrings.startDate(), Constants.STARTDATE_NC, 100) {
            @Override
            public String getCellValue(SubscriptionItem item) {
                return item.getStartDate() != null ? DateUtils.format(item.getStartDate()) : null;
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionItem, String>(wfmStrings.endDate(), Constants.ENDDATE_NC, 100) {
            @Override
            public String getCellValue(SubscriptionItem item) {
                return item.getEndDate() != null ? DateUtils.format(item.getEndDate()) : null;
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionItem, Integer>(hrmsStrings.limit(), SubscriptionItem.LIMIT, 70) {
            @Override
            public Integer getCellValue(SubscriptionItem item) {
                return item.getLimit();
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionItem, String>(hrmsStrings.limitFrequency(), SubscriptionItem.LIMIT_FREQUENCY, 70) {
            @Override
            public String getCellValue(SubscriptionItem item) {
                return item.getLimitFrequency();
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionItem, Integer>(hrmsStrings.breakDuration(), Constants.BREAK, 70) {
            @Override
            public Integer getCellValue(SubscriptionItem item) {
                return item.getBreakDuration();
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        column = new ColumnDefinitionConfig<SubscriptionItem, String>(hrmsStrings.breakType(), SubscriptionItem.BREAK_TYPE, 70) {
            @Override
            public String getCellValue(SubscriptionItem item) {
                return item.getBreakType();
            }
        };
        column.setMinimumColumnWidth(70);
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[0]);
    }

    private ListingRequestProvider<SubscriptionItem> getListData() {
        return (fp, callback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            HrmsService.App.get().getSubscriptionList(fp, new AsyncCallback<ListResult<SubscriptionItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(ListResult<SubscriptionItem> result) {
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
                addNew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("subscription|add/add"));
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.currentlyNoSubscriptionsYet());
                message.setTextBeforeLink(hrmsStrings.youCanStartAddingSubscriptionByClicking());
                message.setHref("subscription|add/add");
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void deleteItem(Integer id) {
        LoadingPanel.loading(true);
        HrmsService.App.get().deleteSubscriptionItem(id, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Void result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.subscriptions()), Info.Type.INFO);
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
