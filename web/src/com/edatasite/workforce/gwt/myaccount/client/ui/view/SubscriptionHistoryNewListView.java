package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.myaccount.client.bundles.MyaccountImageBundles;
import com.edatasite.workforce.gwt.myaccount.client.localization.MyAccountStrings;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Dec 4, 2008
 * Time: 5:11:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class SubscriptionHistoryNewListView extends BaseListView {
    private static final MyAccountStrings myAccountStrings = MyAccountStrings.App.get();
    private ListingPanel listPanel;

    private final String cmd = "cmd";                       //_xclick-subscriptions;
    private final String business = "business";             //sales@workforcetrack.com
    private final String currency_code = "currency_code";   //USD
    private final String amount = "amount";                 //25$
    private final String item_name = "item_name";           //
    private final String item_number = "item_number";       //
    private final String custom = "custom";                 //
    private final String taxX = "tax";                       //
    private final String a3 = "a3";                          //5.00
    private final String p3 = "p3";                          //1  (1,3,6,12 - Months)
    private final String t3 = "t3";                          //M (Month)
    private final String src = "src";                       //1,2,3 (Limit the number of billing cycles.)
    private final String cancel_return = "cancel_return";
    private final String returnT = "return";

    public SubscriptionHistoryNewListView() {
        super("usageHistory", myAccountStrings.paymentHistory());
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new ListingPanel(ListPanelType.UsageHistoryListPanel, getColumnConfig(), getListProvider(), getListDesign());
        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/usageHistoryListPDFHandler";
            listPanel.callListPDF(pdfURL, listPanel.getFilterParametrs());
        });

        listPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadUsageHistoryListExcel";
            listPanel.callListExcel(excelURL, listPanel.getFilterParametrs());
        });

        add(listPanel);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ColumnDefinitionConfig[] columnConfig = new ColumnDefinitionConfig[8];

        columnConfig[0] = new ColumnDefinitionConfig<UsagePlanItem, String>(wfmStrings.apps(), UsagePlanItem.MODULES, 200) {
            @Override
            public String getCellValue(UsagePlanItem item) {
                final StringBuilder columnValue = new StringBuilder();
                if (item.isAccountsModule()) {
                    columnValue.append(wfmStrings.accounts());
                }
                if (item.isSalesModule()) {
                    if (columnValue.length() > 0) {
                        columnValue.append(", ");
                    }
                    columnValue.append(wfmStrings.sales());
                }
                if (item.isHumansModule()) {
                    if (columnValue.length() > 0) {
                        columnValue.append(", ");
                    }
                    columnValue.append(wfmStrings.hrms());
                }
                if (item.isProjectModule()) {
                    if (columnValue.length() > 0) {
                        columnValue.append(", ");
                    }
                    columnValue.append(wfmStrings.projects());
                }

                if (item.isPayrollModule()) {
                    if (columnValue.length() > 0) {
                        columnValue.append(", ");
                    }
                    columnValue.append(wfmStrings.payroll());
                }
                return columnValue.toString();
            }
        };
        columnConfig[0].setMinimumColumnWidth(200);
        columnConfig[0].setMaximumColumnWidth(250);

        columnConfig[1] = new ColumnDefinitionConfig<UsagePlanItem, String>(wfmStrings.startDate(), UsagePlanItem.START_DATE, 50) {
            @Override
            public String getCellValue(UsagePlanItem item) {
                if (item.getStartDate() != null) {
                    return DateUtils.getDateFormatShort(item.getStartDate());
                } else {
                    return "N/A";
                }
            }
        };
        columnConfig[1].setMinimumColumnWidth(50);
        columnConfig[1].setMaximumColumnWidth(100);

        columnConfig[2] = new ColumnDefinitionConfig<UsagePlanItem, String>(wfmStrings.endDate(), UsagePlanItem.END_DATE, 50) {
            @Override
            public String getCellValue(UsagePlanItem item) {
                if (item.getEndDate() != null) {
                    return DateUtils.getDateFormatShort(item.getEndDate());
                } else {
                    return "N/A";
                }
            }
        };
        columnConfig[2].setMinimumColumnWidth(50);
        columnConfig[2].setMaximumColumnWidth(100);

        columnConfig[3] = new ColumnDefinitionConfig<UsagePlanItem, String>(wfmStrings.status(), UsagePlanItem.STATUS, 80) {
            @Override
            public String getCellValue(UsagePlanItem item) {
                return item.getStatus() + " (" + item.getPeriodType() + ")";
            }
        };
        columnConfig[3].setMinimumColumnWidth(80);
        columnConfig[3].setMaximumColumnWidth(120);


        columnConfig[4] = new ColumnDefinitionConfig<UsagePlanItem, Integer>(myAccountStrings.fullUsers(), UsagePlanItem.USERS, 70) {
            @Override
            public Integer getCellValue(UsagePlanItem item) {
                return item.getUserCount();
            }
        };
        columnConfig[4].setMinimumColumnWidth(30);
        columnConfig[4].setMaximumColumnWidth(120);

        columnConfig[5] = new ColumnDefinitionConfig<UsagePlanItem, Integer>(myAccountStrings.essUsers(), UsagePlanItem.ESS_USERS, 70) {
            @Override
            public Integer getCellValue(UsagePlanItem item) {
                return item.getEssUserCount();
            }
        };
        columnConfig[5].setMinimumColumnWidth(30);
        columnConfig[5].setMaximumColumnWidth(120);

        columnConfig[6] = new ColumnDefinitionConfig<UsagePlanItem, Integer>(myAccountStrings.nonUser(), UsagePlanItem.NOACCESS_USERS, 70) {
            @Override
            public Integer getCellValue(UsagePlanItem item) {
                return item.getNonAccessUserCount();
            }
        };
        columnConfig[6].setMinimumColumnWidth(30);
        columnConfig[6].setMaximumColumnWidth(120);

        columnConfig[7] = new ColumnDefinitionConfig<UsagePlanItem, Widget>(wfmStrings.action(), UsagePlanItem.ACTION, 50) {
            @Override
            public Widget getCellValue(UsagePlanItem item) {

                Date now = new Date();
                if (item!=null && item.getStartDate()!=null && item.getEndDate()!=null && now.after(item.getStartDate()) && now.before(item.getEndDate())
                        && item.getPeriodType()!=null && !item.getPeriodType().equalsIgnoreCase("Free Trial") ) {
                    final String currencyValue = item.isCurrencyGBP() ? "GBP" : Utils.getCurrencyCODEbyHOST();
                    SimpleLink payButton = new SimpleLink(!item.isPaid() ? myAccountStrings.payNow() : myAccountStrings.upgradeNow());
                    payButton.addClickHandler(event -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("usageplan");
                        /*String contextPath = "https://" + Utils.getPayPalLink() + "?";
                        Utils.redirect(contextPath + cmd + "=_xclick-subscriptions&"
                                       + business + "=" + Utils.getPayPalAccount() + "&"         //      sales@workforcetrack.com
                                       + currency_code + "=" + currencyValue + "&"
                                       + amount + "=" + *//*numberFormat.format*//*getNumberFormatWithBigDecimal(item.getTotalAmount()) + "&"  ////test "tot"
                                       + taxX + "=" + *//*numberFormat.format*//*getNumberFormatWithBigDecimal(item.getTax()) + "&"  ////test "taxC"
                                       + item_name + "=" + Utils.getProductName() + " - " + item.getService() + ".&"
                                       + item_number + "=1&"
                                       + a3 + "=" + *//*numberFormat.format*//*getNumberFormatWithBigDecimal(item.getTotalAmount()) + "&"       ////test "tot"
                                       + p3 + "=" + item.getUsageMonth() + "&"
                                       + t3 + "=M&"
                                       + src + "=1&"
                                       + (item.isPaypalStatus() ? "modify=2&" : "")
                                       + custom + "=" + Utils.getEncryptedCompanyID() + Constants.SUBSCRIPTION_ADD + item.getObjectID() + "&"
                                       + returnT + "=" + Utils.getHostURL() + "Myaccount.html" + "&"
                                       + cancel_return + "=" + Utils.getHostURL() + "Myaccount.html");*/
                    });
                    return payButton;
                } else {
                    return new HTML("");
                }
            }
        };
        columnConfig[7].setMinimumColumnWidth(50);
        columnConfig[7].setMaximumColumnWidth(100);
        columnConfig[7].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        return columnConfig;
    }

    private ListingRequestProvider<UsagePlanItem> getListProvider() {
        return (listingFilterParameter, listingCallback) -> {

            MyAccountService.App.get().getUsagePlans(listingFilterParameter, new AbstractAsyncCallback<ListResult<UsagePlanItem>>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(ListResult<UsagePlanItem> usagePlanItemListResult) {
                    listingCallback.onSuccess(usagePlanItemListResult);
                }
            });
        };
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {

                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return -1;
                    }
                };
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

            }
        };
    }

    public String getIconStyle() {
        return null;
    }

    public ImageResource getIconImage() {
        return MyaccountImageBundles.App.get().subscriptionHistory();
    }

    private String getNumberFormatWithBigDecimal(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).toString();
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
