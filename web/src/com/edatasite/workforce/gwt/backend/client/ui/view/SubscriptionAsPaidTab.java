package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.SimpleUsagePlanItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.view.client.ProvidesKey;

/**
 * User: Ilhombek
 * Date: 24.08.2010
 * Time: 12:27:29
 */
public class SubscriptionAsPaidTab extends CustomTabWidget {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer companyID;
    private KpiDataGrid<SimpleUsagePlanItem> dataGrid;

    SubscriptionAsPaidTab(String tabName, Integer companyID) {
        super(tabName);
        this.companyID = companyID;
    }

    public static final ProvidesKey<SimpleUsagePlanItem> KEY_PROVIDER = item -> item != null ? item.getObjectID() : null;

    @Override
    public void initData() {
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        add(dataGrid);
        initTableColumns();
    }

    @Override
    public void viewShow() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_USAGE_PLAN_ADD_EDIT, SubscriptionAsPaidTab.this, (sender, args) -> getUsagePlanList());
        getUsagePlanList();
    }

    private void getUsagePlanList() {
        LoadingPanel.loading(true);
        BackendService.App.get().getUsagePlanListByCompany(companyID, new AbstractAsyncCallback<SimpleUsagePlanItem[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                caught.printStackTrace();
            }

            @Override
            public void success(SimpleUsagePlanItem[] result) {
                LoadingPanel.loading(false);
                dataGrid.supplyProvider(result);
                dataGrid.refresh();
            }
        });
    }

    private void initTableColumns() {
//        //ID
//        Column<SimpleUsagePlanItem, String> state = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.getObjectID() != null ? item.getObjectID().toString() : "";
//            }
//        };
//        state.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
//        dataGrid.addColumn(state, backendStrings.id());
//        dataGrid.setColumnWidth(state, 10, com.google.gwt.dom.client.Style.Unit.PCT);

//        //Company Id
//        Column<SimpleUsagePlanItem, String> amount = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.getCompanyID() != null ? item.getCompanyID().toString() : "";
//            }
//        };
//        dataGrid.addColumn(amount, backendStrings.companyID());
//        amount.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
//        dataGrid.setColumnWidth(amount, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //Start Date
        Column<SimpleUsagePlanItem, SafeHtml> startDate = new Column<SimpleUsagePlanItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(SimpleUsagePlanItem item) {
                return createHackHTML(item.getStartDate() != null ? DateUtils.format(item.getStartDate(), DateTimeFormat.getFormat("dd-MM-yyyy")) : "");
            }
        };
        dataGrid.addColumn(startDate, createHackHTML(wfmStrings.startDate()));
        dataGrid.setColumnWidth(startDate, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //End Date
        Column<SimpleUsagePlanItem, SafeHtml> enddate = new Column<SimpleUsagePlanItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(SimpleUsagePlanItem item) {
                return createHackHTML(item.getEndDate() != null ? DateUtils.format(item.getEndDate(), DateTimeFormat.getFormat("dd-MM-yyyy")) : "");
            }
        };
        dataGrid.addColumn(enddate, createHackHTML(wfmStrings.endDate()));
        dataGrid.setColumnWidth(enddate, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //Payment Status
        Column<SimpleUsagePlanItem, SafeHtml> paymentStatus = new Column<SimpleUsagePlanItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(SimpleUsagePlanItem item) {
                return createHackHTML(item.getStatus() != null ? item.getStatus() : "");
            }
        };
        dataGrid.addColumn(paymentStatus, createHackHTML(backendStrings.paymentStatus()));
        dataGrid.setCustomHeaderStyle(true);
        dataGrid.setColumnWidth(paymentStatus, 15, com.google.gwt.dom.client.Style.Unit.PCT);

//        //Is Paid
//        Column<SimpleUsagePlanItem, String> isPaid = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return Boolean.toString(item.isPaid());
//            }
//        };
//        dataGrid.addColumn(isPaid, backendStrings.isPaid());
//        dataGrid.setColumnWidth(isPaid, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        //Storage
        Column<SimpleUsagePlanItem, SafeHtml> storage = new Column<SimpleUsagePlanItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(SimpleUsagePlanItem item) {
                return createHackHTML(item.getStorageCount() != null ? item.getStorageCount().toString() : "");
            }
        };
        storage.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        dataGrid.addColumn(storage, createHackHTML(wfmStrings.storage()));
        dataGrid.setColumnWidth(storage, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        //Storage Free
        Column<SimpleUsagePlanItem, SafeHtml> storageFree = new Column<SimpleUsagePlanItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(SimpleUsagePlanItem item) {
                return createHackHTML(item.getStorageFree() != null ? String.valueOf(item.getStorageFree()) : "");
            }
        };
        storageFree.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        dataGrid.addColumn(storageFree, createHackHTML(backendStrings.storageFree()));
        dataGrid.setColumnWidth(storageFree, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        //Users
        Column<SimpleUsagePlanItem, SafeHtml> users = new Column<SimpleUsagePlanItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(SimpleUsagePlanItem item) {
                return createHackHTML(item.getUserCount() != null ? item.getUserCount().toString() : "");
            }
        };
        users.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        dataGrid.addColumn(users, createHackHTML(wfmStrings.active() + " " + wfmStrings.users()));
        dataGrid.setColumnWidth(users, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        //ESS Users
        Column<SimpleUsagePlanItem, SafeHtml> essUsers = new Column<SimpleUsagePlanItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(SimpleUsagePlanItem item) {
                return createHackHTML(item.getEssUserCount() != null ? item.getEssUserCount().toString() : "");
            }
        };
        essUsers.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        dataGrid.addColumn(essUsers, createHackHTML("ESS Users"));
        dataGrid.setColumnWidth(essUsers, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        //No Access Users
        Column<SimpleUsagePlanItem, SafeHtml> noAccessUsers = new Column<SimpleUsagePlanItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(SimpleUsagePlanItem item) {
                return createHackHTML(item.getNonAccessUserCount() != null ? item.getNonAccessUserCount().toString() : "");
            }
        };
        noAccessUsers.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        dataGrid.addColumn(noAccessUsers, createHackHTML("No Access Users"));
        dataGrid.setColumnWidth(noAccessUsers, 15, com.google.gwt.dom.client.Style.Unit.PCT);

//        //Users Free
//        Column<SimpleUsagePlanItem, String> usersFree = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.getUsersFree() != null ? item.getUsersFree().toString() : "";
//            }
//        };
//        usersFree.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
//        dataGrid.addColumn(usersFree, backendStrings.usersFree());
//        dataGrid.setColumnWidth(usersFree, 15, com.google.gwt.dom.client.Style.Unit.PCT);


//        //Total Amount
//        Column<SimpleUsagePlanItem, String> totalAmount = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return String.valueOf(item.getTotalAmount());
//            }
//        };
//        totalAmount.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
//        dataGrid.addColumn(totalAmount, coreStrings.totalAmount());
//        dataGrid.setColumnWidth(totalAmount, 15, com.google.gwt.dom.client.Style.Unit.PCT);

//        if (Utils.getHostName().contains("smebu.com") || Utils.getHostName().contains("tjilo.com")) {
//            //Discount
//            Column<SimpleUsagePlanItem, String> discount = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//                @Override
//                public String getValue(SimpleUsagePlanItem item) {
//                    return String.valueOf(item.getDiscount());
//                }
//            };
//            discount.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
//            dataGrid.addColumn(discount, coreStrings.discountOnly());
//            dataGrid.setColumnWidth(discount, 15, com.google.gwt.dom.client.Style.Unit.PCT);
//        }
//        //Taxt
//        Column<SimpleUsagePlanItem, String> taxt = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return String.valueOf(item.getTax());
//            }
//        };
//        taxt.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
//        dataGrid.addColumn(taxt, wfmStrings.tax());
//        dataGrid.setColumnWidth(taxt, 15, com.google.gwt.dom.client.Style.Unit.PCT);
//        //Total Payable
//        Column<SimpleUsagePlanItem, String> totalPayable = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return String.valueOf(item.getTotalpayable());
//            }
//        };
//        totalPayable.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
//        dataGrid.addColumn(totalPayable, backendStrings.totalPayable());
//        dataGrid.setColumnWidth(totalPayable, 15, com.google.gwt.dom.client.Style.Unit.PCT);


//        //Paypal Status
//        Column<SimpleUsagePlanItem, String> paypalStatus = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return Boolean.toString(item.isPaypalStatus());
//            }
//        };
//        dataGrid.addColumn(paypalStatus, backendStrings.payPalStatus());
//        dataGrid.setColumnWidth(paypalStatus, 15, com.google.gwt.dom.client.Style.Unit.PCT);
//        //Is Currency GBP
//        Column<SimpleUsagePlanItem, String> isCurrencyGBP = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return Boolean.toString(item.isCurrencyGBP());
//            }
//        };
//        dataGrid.addColumn(isCurrencyGBP, backendStrings.isCurrencyGBP());
//        dataGrid.setColumnWidth(isCurrencyGBP, 15, com.google.gwt.dom.client.Style.Unit.PCT);
//        //Is UK Company
//        Column<SimpleUsagePlanItem, String> isUKCompany = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return Boolean.toString(item.isCompanyUk());
//            }
//        };
//        dataGrid.addColumn(isUKCompany, backendStrings.isUKCompany());
//        dataGrid.setColumnWidth(isUKCompany, 15, com.google.gwt.dom.client.Style.Unit.PCT);
//        //Is Deleted
//        Column<SimpleUsagePlanItem, String> isDeleted = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.isDeleted() != null ? Boolean.toString(item.isDeleted()) : "";
//            }
//        };
//        dataGrid.addColumn(isDeleted, backendStrings.isDeleted());
//        dataGrid.setColumnWidth(isDeleted, 15, com.google.gwt.dom.client.Style.Unit.PCT);
//        //Is Mobile
//        Column<SimpleUsagePlanItem, String> isMobile = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return Boolean.toString(item.isMobile());
//            }
//        };
//        dataGrid.addColumn(isMobile, backendStrings.isMobile());
//        dataGrid.setColumnWidth(isMobile, 15, com.google.gwt.dom.client.Style.Unit.PCT);
//        //Task Count
//        Column<SimpleUsagePlanItem, String> taskCount = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.getTaskCount() != null ? item.getTaskCount().toString() : "";
//            }
//        };
//        dataGrid.addColumn(taskCount, backendStrings.countTask());
//        dataGrid.setColumnWidth(taskCount, 15, com.google.gwt.dom.client.Style.Unit.PCT);
//        //Project Count
//        Column<SimpleUsagePlanItem, String> projectCount = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.getProjectCount() != null ? item.getProjectCount().toString() : "";
//            }
//        };
//        dataGrid.addColumn(projectCount, backendStrings.countProject());
//        dataGrid.setColumnWidth(projectCount, 15, com.google.gwt.dom.client.Style.Unit.PCT);

//        //Is Upgrade
//        Column<SimpleUsagePlanItem, String> isUpgrade = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.isUpgrade() != null ? Boolean.toString(item.isUpgrade()) : "";
//            }
//        };
//        dataGrid.addColumn(isUpgrade, backendStrings.isUpgrade());
//        dataGrid.setColumnWidth(isUpgrade, 15, com.google.gwt.dom.client.Style.Unit.PCT);

//        //Upgrade Payable
//        Column<SimpleUsagePlanItem, String> upgradePayable = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.getUpgradePayable() != null ? item.getUpgradePayable().toString() : "";
//            }
//        };
//        dataGrid.addColumn(upgradePayable, backendStrings.upgradePayable());
//        dataGrid.setColumnWidth(upgradePayable, 15, com.google.gwt.dom.client.Style.Unit.PCT);

//        //Message Sended
//        Column<SimpleUsagePlanItem, String> messageSended = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.isMessageSended() != null ? Boolean.toString(item.isMessageSended()) : "";
//            }
//        };
//        dataGrid.addColumn(messageSended, backendStrings.messageSended());
//        dataGrid.setColumnWidth(messageSended, 15, com.google.gwt.dom.client.Style.Unit.PCT);

//        //User Rate
//        Column<SimpleUsagePlanItem, String> userRate = new Column<SimpleUsagePlanItem, String>(new TextCell()) {
//            @Override
//            public String getValue(SimpleUsagePlanItem item) {
//                return item.getUserRate() != null ? String.valueOf(item.getUserRate()) : "";
//            }
//        };
//        dataGrid.addColumn(userRate, backendStrings.userRate());
//        dataGrid.setColumnWidth(userRate, 15, com.google.gwt.dom.client.Style.Unit.PCT);

//        if (Utils.adminOrDirector()) {
//            //Edit
//            Column<SimpleUsagePlanItem, String> edit = new Column<SimpleUsagePlanItem, String>(new SimpleLinkCell()) {
//                @Override
//                public String getValue(SimpleUsagePlanItem item) {
//                    return wfmStrings.edit();
//                }
//            };
//            edit.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//            edit.setFieldUpdater((index, object, value) -> {
//                String action = "usagePlanUpd|updateUsage/" + object.getObjectID() + "/" + companyID;
//                SinksContainerFactory.entryPoint.onHistoryChanged(action);
//            });
//            dataGrid.addColumn(edit, createHackHTML(wfmStrings.action()));
//            dataGrid.setColumnWidth(edit, 10, com.google.gwt.dom.client.Style.Unit.PCT);
//        }
    }

    private SafeHtml createHackHTML(String s) {
        final SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant("<div style=\"text-align: center;\">" + s + "</div>");
        return sb.toSafeHtml();
    }
}