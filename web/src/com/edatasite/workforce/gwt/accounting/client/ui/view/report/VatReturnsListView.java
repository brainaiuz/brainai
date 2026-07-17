package com.edatasite.workforce.gwt.accounting.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.GenerateVATCustomPeriodModal;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VATSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.ui.view.accounting.uk.HMRCAuthorizationModal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;


public class VatReturnsListView extends BaseListView implements Colapse, AccountingConstants, FittedContent {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel<VatReturnItem> list;
    private VATSettingsItem settingsItem;

    public VatReturnsListView() {
        super("vatReturns");
        setDescription(property.getPlural((Utils.getCustomTaxName() != null && !"".equals(Utils.getCustomTaxName())) ? (Utils.getCustomTaxName() + " " + wfmStrings.reports()) : accountingStrings.vatReports()));
    }

    @Override
    public String getIconStyle() {
        return "accountMark manual-journals";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Widget onInitialize() {
        VatReturnService.App.get().getVATSettings(new AsyncCallback<VATSettingsItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(VATSettingsItem vatSettingsItem) {
                settingsItem = vatSettingsItem;
                initListing();
            }
        });
        return null;
    }

    private void initListing() {
        list = new ListingPanel<>(ListPanelType.VatReturnsListPanel, drawColumns(), (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            VatReturnServiceAsync vatReturnService = VatReturnService.App.get();
            vatReturnService.getVatReturnList(filterParametrs, Utils.getFraudPreventionData(), new AsyncCallback<ListResult<VatReturnItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    list.loading(false);
                    Info.warn(throwable.getMessage());
                }

                @Override
                public void onSuccess(ListResult<VatReturnItem> listResult) {
                    callback.onSuccess(listResult);
                }
            });
        }, new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                if (!settingsItem.isSubmitVatManually() && !settingsItem.isHmrcAuthorized()) {
                    emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage() {
                        @Override
                        public VerticalPanel getWholeMessage() {
                            VerticalPanel verticalPanel = new VerticalPanel();
                            verticalPanel.add(new Label(accountingStrings.needToAuthorizeKpiAtHMRC()));
                            SimpleLink simpleLink = new SimpleLink(accountingStrings.authorizeAtHMRC());
                            simpleLink.addClickHandler(clickEvent -> new HMRCAuthorizationModal());
                            verticalPanel.add(simpleLink);
                            return verticalPanel;
                        }
                    });
                }
            }

            @Override
            public ActionButton initTopToolBarMoreActions() {
                if (settingsItem.isSubmitVatManually()) {
                    ActionButton generateVAT = new ActionButton(accountingStrings.generateVATReturn(), ActionButton.Type.BUTTON);
                    generateVAT.ensureDebugId("generateVAT");
                    generateVAT.addClickHandler(clickEvent -> VatReturnService.App.get().getVATSettings(new AsyncCallback<VATSettingsItem>() {
                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(VATSettingsItem vatSettingsItem) {

                            if (!vatSettingsItem.hasUnfiledReturn()) {
                                generateVATReturn(vatSettingsItem);
                            } else {
                                Info.show("You cannot generate new Tax Returns until you file your previous Tax Return in Kpi", Info.Type.WARNING);
                            }
                        }
                    }));

                    return generateVAT;
                }
                return null;
            }
        }
        );
        add(list);
        list.reloadPage();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VAT_RETURN_FILE_CHANGED, VatReturnsListView.this, (sender, args) -> list.reloadPage());
    }

    private ColumnDefinitionConfig[] drawColumns() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<ColumnDefinitionConfig>();
        ColumnDefinitionConfig column;
        if (settingsItem.isSubmitVatManually()) {
            column = new ColumnDefinitionConfig<VatReturnItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

                @Override
                public Anchor getCellValue(final VatReturnItem item) {

                    if (item.isRemovable()) {
                        MenuBar menuBar = new MenuBar(true);
                        MenuPopItem remove = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                        remove.setCommand(() -> {
                            VatReturnService.App.get().deleteVatReturn(item.getObjectID(), new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(Void aVoid) {
                                    list.reloadPage();
                                }
                            });
                        });
                        menuBar.addItem(remove);
                        ToolItem toolItem = new ToolItem(1);
                        toolItem.setWidget(menuBar);
                        return toolItem.getAction();
                    }
                    return null;
                }
            };
            columns.add(column);
            column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
            column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        }

        column = new ColumnDefinitionConfig<VatReturnItem, Widget>(wfmStrings.status(), "status", 150) {
            @Override
            public Widget getCellValue(VatReturnItem item) {
                return new SimpleLink(item.getStatus() != null ? item.getStatus().getName() : "", "vatreturn|vatReturn/" + item.getObjectID(), "VAT-" + DateUtils.monthFullFormat.format(item.getToDate().getNonConvertedDate()) + " - " + DateUtils.yearFormat.format(item.getToDate().getNonConvertedDate()));
            }
        };
        column.addColor(new ColumnColor("Filed", "r", "007DE7"));
        column.addColor(new ColumnColor("Open", "r", "2BBF57"));
        column.setIsClickable(true);
        columns.add(column);

        column = new ColumnDefinitionConfig<VatReturnItem, String>("Tax Return", "date", 250) {
            @Override
            public String getCellValue(VatReturnItem item) {
                return DateUtils.monthFullFormat.format(item.getToDate().getNonConvertedDate()) + " - " + DateUtils.yearFormat.format(item.getToDate().getNonConvertedDate());
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<VatReturnItem, String>("Filed On", "filedon", 150) {
            @Override
            public String getCellValue(VatReturnItem item) {
                return (item.getFiledOn() != null) ? DateUtils.format(item.getFiledOn().getNonConvertedDate()) : "-";
            }
        };
        columns.add(column);

        if (!settingsItem.isSubmitVatManually()) {
            column = new ColumnDefinitionConfig<VatReturnItem, String>("Due", "due", 150) {
                @Override
                public String getCellValue(VatReturnItem item) {
                    return Optional.ofNullable(item.getDue()).map(date -> DateUtils.format(date.getNonConvertedDate())).orElse("-");
                }
            };
            columns.add(column);
        }

        column = new ColumnDefinitionConfig<VatReturnItem, String>("Payable Tax Total", "ttpayable", 150) {
            @Override
            public String getCellValue(VatReturnItem item) {
                return AccountingUtils.get().formatPrice(item.getPayableTaxTotal());
            }
        };
        columns.add(column);

        column = new ColumnDefinitionConfig<VatReturnItem, String>("Reclaimable Tax Total", "ttreclaimable", 150) {
            @Override
            public String getCellValue(VatReturnItem item) {
                return AccountingUtils.get().formatPrice(item.getReclaimableTaxTotal());
            }
        };
        columns.add(column);

        if (!Utils.isUKCompany()) {
            column = new ColumnDefinitionConfig<VatReturnItem, String>("Balance Due", "balance", 150) {
                @Override
                public String getCellValue(VatReturnItem item) {
                    if (item.getPayableTaxTotal() != null && item.getReclaimableTaxTotal() != null) {
                        return AccountingUtils.get().formatPrice(item.getPayableTaxTotal().subtract(item.getReclaimableTaxTotal()));
                    }
                    return "";
                }
            };
            columns.add(column);
        }

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private void generateVATReturn(VATSettingsItem vatSettingsItem) {

        if (TAX_PERIOD.MONTHLY.equals(vatSettingsItem.getTaxPeriod().getCode())) {
            Date fromDate = DateUtil.resetTime(vatSettingsItem.getLastTaxGeneratedDate() != null ? DateUtil.addDays(vatSettingsItem.getLastTaxGeneratedDate().getNonConvertedDate(), 1) : vatSettingsItem.getTaxGenerationDate().getNonConvertedDate());
            Date toDate = DateUtil.getMonthLastDate(fromDate);
            VatReturnService.App.get().createVatReturn(new DateNonConvertable(fromDate), new DateNonConvertable(toDate), new AsyncCallback<VatReturnItem>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(VatReturnItem item) {
                    list.reloadPage();
                    SinksContainerFactory.entryPoint.onHistoryChanged("vatreturn|vatReturn/" + item.getObjectID());
                }
            });
        } else {
            new GenerateVATCustomPeriodModal(vatSettingsItem, new Command() {
                @Override
                public void execute() {
                    list.reloadPage();
                }
            });
        }
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

    @Override
    public String getPropertyCode() {
        return "vatReturnsSaudiOrUae";
    }

}
