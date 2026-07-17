//package com.edatasite.workforce.gwt.accounting.client.ui.view.report; TODO if you see this code after 2026-01-23 delete all code commented with task number: T101869
//
//import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
//import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
//import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
//import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
//import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnTransferObject;
//import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
//import com.edatasite.workforce.gwt.core.client.BaseListView;
//import com.edatasite.workforce.gwt.core.client.DateUtils;
//import com.edatasite.workforce.gwt.core.client.Utils;
//import com.edatasite.workforce.gwt.core.client.Validation;
//import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
//import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
//import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
//import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
//import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
//import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
//import com.edatasite.workforce.gwt.core.client.ui.Constants;
//import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
//import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
//import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
//import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
//import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
//import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
//import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
//import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
//import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
//import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
//import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
//import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
//import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
//import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
//import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
//import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.core.client.RunAsyncCallback;
//import com.google.gwt.dom.client.Style;
//import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.*;
//import gwt.material.design.client.ui.html.Span;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//
//
///**
// * Created by IntelliJ IDEA.
// * User: Sherzod
// * Date: 24.08.2010
// * Time: 11:23:20
// * To change this template use File | Settings | File Templates.
// */
//public class VatReturnReportsListView extends BaseListView implements Colapse, AccountingConstants, FittedContent {
//    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
//
//    //private ListPanel list;
//    private ListingPanel<VatReturnTransferObject> list;
//
//    public VatReturnReportsListView() {
//        super("vatReturns");
//        Utils.log("vot masala gde");
//        setDescription(property.getPlural((Utils.getCustomTaxName() != null && !"".equals(Utils.getCustomTaxName())) ? (Utils.getCustomTaxName() + " " + wfmStrings.reports()) : accountingStrings.vatReports()));
//    }
//
//    @Override
//    public String getIconStyle() {
//        return "accountMark manual-journals";  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    protected Widget onInitialize() {
//        Utils.log("init vat return reports list view");
//        list = new ListingPanel<>(ListPanelType.VatReportsListPanel, drawColumns(), (filterParametrs, callback) -> {
//            if (filterParametrs == null) {
//                filterParametrs = new ListingFilterParameter();
//            }
//            AccountingServiceAsync accountingService = AccountingService.App.get();
//
//            accountingService.getVatReturnReportList(filterParametrs, new AsyncCallback<ListResult<VatReturnTransferObject>>() {
//                @Override
//                public void onFailure(Throwable throwable) {
//                    //To change body of implemented methods use File | Settings | File Templates.
//                }
//
//                @Override
//                public void onSuccess(ListResult<VatReturnTransferObject> listResult) {
//                    callback.onSuccess(listResult);//To change body of implemented methods use File | Settings | File Templates.
//                }
//            });
//        }, new ListingPanelDesign() {
//
//            @Override
//            public ListingFacetFilter initFacetFilter() {
//                return null;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public ListingActionMenu initLeftTopActionMenu() {
//                return null;  //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        }
//        );
//
//
//        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_VAT_RETURN_REPORT_EFILED, VatReturnReportsListView.this, (sender, args) -> list.reloadPage());
//
////        super.setListingPanel(list);
////        super.display();
//        add(list);
//        list.reloadPage();
//        return null;
//    }
//
//    private ColumnDefinitionConfig[] drawColumns() {
//        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[12];
//        columns[0] = new ColumnDefinitionConfig<VatReturnTransferObject, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
//
//            @Override
//            public Anchor getCellValue(final VatReturnTransferObject item) {
//                int actionItemCount = 0;
//                MenuBar menuBar = new MenuBar(true);
//
//
//                if (item.getResponseContent() != null) {
//                    MenuPopItem viewResponse = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
//                    viewResponse.setCommand(() -> {
//                        final KpiModal dialogBox = new KpiModal();
//                        dialogBox.setTitle(wfmStrings.response());
//                        dialogBox.setWidth("310px");
//
//                        VerticalPanel panel = new VerticalPanel();
//                        panel.setWidth("300px");
//                        panel.setStyleName("workforce");
//
//                        ScrollPanel scrollPanel = new ScrollPanel();
//                        scrollPanel.setSize("300px", "200px");
//                        scrollPanel.add(new Label(item.getResponseContent()));
//                        panel.add(scrollPanel);
//
//                        Button closeButton = new Button(wfmStrings.close());
//                        closeButton.addClickHandler(event -> dialogBox.close());
//                        panel.add(closeButton);
//                        panel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_CENTER);
//                        dialogBox.add(panel);
//                        dialogBox.open();
//                    });
//                    actionItemCount++;
//                    menuBar.addItem(viewResponse);
//                }
//
//                if (SUBMISSION_SAVED.equals(item.getStatus())) {
//                    MenuPopItem payVAT = new MenuPopItem(item.getVatToReclaimFromCustoms().compareTo(ZERO) >= 0 ? accountingStrings.payVAT() : accountingStrings.reclaimVATFromCustoms(), "icon-task-small");
//                    payVAT.setCommand(() -> drawPayVATDialogBox(item));
//                    actionItemCount++;
//                    menuBar.addItem(payVAT);
//
//                }
//
//                if (SUBMISSION_PAID.equals(item.getStatus())) {
//                    MenuPopItem submitVatReport = new MenuPopItem(accountingStrings.efileToHMRC());
//                    submitVatReport.setCommand(() -> {
//                        LoadingPanel.loading(true);
//                        AccountingService.App.get().submitVatReturnReportToHMRC(item.getObjectID(), new AbstractAsyncCallback<String>() {
//                            public void failure(Throwable caught) {
//                                LoadingPanel.loading(false);
//                                Info.show(accountingStrings.infoMessage40(), Info.Type.WARNING);
//                            }
//
//                            public void success(String result) {
//                                LoadingPanel.loading(false);
//                                Info.show(accountingStrings.vatReturnReportReadyForSubmit(), Info.Type.WARNING);
//                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VAT_RETURN_REPORT_EFILED, null, VatReturnReportsListView.this);
//                            }
//                        });
//                    });
//                    actionItemCount++;
//                    menuBar.addItem(submitVatReport);
//
//                }
//
//                BigDecimal paidAmount = item.getPaidAmount() != null ? item.getPaidAmount() : ZERO;
//                MenuPopItem deleteLink = new MenuPopItem(wfmStrings.delete(), "icon-remove");
//                deleteLink.setCommand(() -> {
//                    WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
//                    message.setTitle(wfmStrings.warning());
//                    message.setMessage(wfmStrings.sureYouWantToDelete() );
//                    message.addCloseHandler(new CloseHandler() {
//                        @Override
//                        public void onSubmit() {
//                            AccountingService.App.get().deleteVatReturnReport(item.getObjectID(), new AbstractAsyncCallback<Void>() {
//                                public void failure(Throwable caught) {
//                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
//                                }
//
//                                public void success(Void result) {
//                                    list.reloadPage();
//                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.vatReturn()), Info.Type.INFO);
//                                }
//                            });
//                        }
//                    });
//                    message.open();
//                });
//                deleteLink.ensureDebugId("vat_report_delete");
//                actionItemCount++;
//                menuBar.addItem(deleteLink);
//
//                if (actionItemCount > 0) {
//                    final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
//                    toolItem.setWidget(menuBar);
//                    return toolItem.getAction();
//                }
//                return null;
//            }
//        };
//
//        columns[1] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(wfmStrings.date(), "date", 140) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return DateUtils.format(item.getFrom()) + " - " + DateUtils.format(item.getTo());
//            }
//        };
//
//        columns[2] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box1(), "box1", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getVatOnSalesAndOutputs());
//            }
//        };
//        columns[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[3] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box2(), "box2", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getVatFromECMemberStates());
//            }
//        };
//        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[4] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box3(), "box3", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getTotalVatDue());
//            }
//        };
//        columns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[5] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box4(), "box4", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getVatOnPurchaseAndInputs());
//            }
//        };
//        columns[5].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[6] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box5(), "box5", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getVatToReclaimFromCustoms());
//            }
//        };
//        columns[6].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[7] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box6(), "box6", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getTotalSalesAndOutputs());
//            }
//        };
//        columns[7].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[8] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box7(), "box7", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getTotalPurchasesAndInputs());
//            }
//        };
//        columns[8].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[9] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box8(), "box8", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getTotalSupplies());
//            }
//        };
//        columns[9].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[10] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(accountingStrings.box9(), "box9", 80) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                return AccountingUtils.get().formatPrice(item.getTotalAcquisitions());
//            }
//        };
//        columns[10].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//
//        columns[11] = new ColumnDefinitionConfig<VatReturnTransferObject, String>(wfmStrings.status(), "status", 90) {
//            @Override
//            public String getCellValue(VatReturnTransferObject item) {
//                if (SUBMISSION_COMPLETED.equals(item.getStatus())) {
//                    return wfmStrings.completed();
//                } else if (SUBMISSION_PENDING.equals(item.getStatus())) {
//                    return accountingStrings.waitingForSubmit();
//                } else if (SUBMISSION_FAILED.equals(item.getStatus())) {
//                    return wfmStrings.failed();
//                } else if (SUBMISSION_SAVED.equals(item.getStatus())) {
//                    return wfmStrings.saved();
//                } else if (SUBMISSION_PAID.equals(item.getStatus())) {
//                    return wfmStrings.paid();
//                }
//                return "";
//            }
//        };
//
//        return columns;
//
//    }
//
//    private void drawPayVATDialogBox(final VatReturnTransferObject vatReturnReport) {
//        if (vatReturnReport != null) {
//
//            final String vatPaymentType = vatReturnReport.getVatToReclaimFromCustoms().compareTo(ZERO) >= 0 ? VATRETURN_PAYMENT_PAYABLE : VATRETURN_PAYMENT_RECEIVABLE;
//            BigDecimal box5Amount = vatReturnReport.getVatToReclaimFromCustoms().abs();
//
//            final KpiModal dialogBox = new KpiModal();
//            dialogBox.setTitle(VATRETURN_PAYMENT_PAYABLE.equals(vatPaymentType) ? accountingStrings.payVAT() : accountingStrings.reclaimVATFromCustoms());
//            dialogBox.setWidth("400px");
//
//            final DatePicker date = new DatePicker();
//            final PaymentAccountsLookUp payAccountLookUp = new PaymentAccountsLookUp();
//            final TextBox amountPay = new TextBox();
//            amountPay.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
//            Validation.addNumericKeyboardListener(amountPay, 2);
//
//            String vatString = accountingStrings.hmCusExciseVAT();
//            if (Utils.isSaudiCompany()) {
//                vatString = accountingStrings.generalAuthorityForZakatAndTax();
//            } else if (Utils.isUAECompany()) {
//                vatString = accountingStrings.theFederalTaxAuthority();
//            }
//            FormGroup vatAgencyField = new FormGroup(accountingStrings.vatAgency(), new Span(vatString));
//
//            FormGroup payAccountField = new FormGroup(VATRETURN_PAYMENT_PAYABLE.equals(vatPaymentType) ? wfmStrings.paidFrom() : wfmStrings.paidTo(), payAccountLookUp, true);
//
//            FormGroup dateField = new FormGroup(accountingStrings.vatAgency(), date, true);
//
//            final BigDecimal balanceAmount = box5Amount.subtract(vatReturnReport.getPaidAmount() != null ? vatReturnReport.getPaidAmount() : ZERO);
//            FormGroup balanceField = new FormGroup(wfmStrings.balance(), new Span(AccountingUtils.get().formatPrice(balanceAmount)));
//
//            amountPay.setText(AccountingUtils.get().formatPrice(balanceAmount));
//            FormGroup amountField = new FormGroup(wfmStrings.amount(), amountPay, true);
//
//            final WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
//            WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel());
//            String finalVatString = vatString;
//            saveButton.addClickHandler(clickEvent -> {
//                int errors = 0;
//                if (!Validation.validateLookUpRequired(payAccountLookUp)) {
//                    errors++;
//                }
//                if (!Validation.validateDate(date)) {
//                    errors++;
//                }
//                if (!Validation.validateTextBoxRequired(amountPay)) {
//                    errors++;
//                }
//                if (errors > 0) {
//                    return;
//                }
//
//                BigDecimal amountForPay = AccountingUtils.get().parseToBigDecimal(amountPay.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP);
//                if (balanceAmount.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(amountForPay) < 0) {
//                    Info.show(accountingStrings.payAmountShouldNotBe(), Info.Type.WARNING);
//                    return;
//                }
//
//                LoadingPanel.loading(true);
//
//                saveButton.setEnabled(false);
//
//                PaymentData paymentData = new PaymentData();
//                paymentData.setNote(finalVatString);
//                paymentData.setPaymentAmount(amountForPay);
//                paymentData.setPaymentAccount(payAccountLookUp.getSelectedItem());
//                paymentData.setDate(new DateNonConvertable(date.getDate()));
//                paymentData.setExchangeRate(AccountingConstants.ONE);
//                paymentData.setRelatedObjectID(vatReturnReport.getObjectID());
//                paymentData.setType(vatPaymentType);
//
//                InvoiceService.App.get().savePayment(paymentData, new AsyncCallback<Integer>() {
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        LoadingPanel.loading(false);
//                        saveButton.setEnabled(true);
//                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
//                    }
//
//                    @Override
//                    public void onSuccess(Integer integer) {
//                        LoadingPanel.loading(false);
//                        saveButton.setEnabled(true);
//                        dialogBox.close();
//                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.payment()), Info.Type.INFO);
//                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VAT_RETURN_REPORT_EFILED, null, VatReturnReportsListView.this);
//                    }
//                });
//            });
//            cancelButton.addClickHandler(clickEvent -> dialogBox.close());
//
//            dialogBox.add(vatAgencyField);
//            dialogBox.add(payAccountField);
//            dialogBox.add(dateField);
//            dialogBox.add(balanceField);
//            dialogBox.add(amountField);
//            dialogBox.addButton(cancelButton);
//            dialogBox.addButton(saveButton);
//            dialogBox.open();
//        }
//    }
//
//    @Override
//    public FlowPanel getHelpContainer() {
//        FlowPanel panel = new FlowPanel();
//        panel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        Anchor anchor = new Anchor();
//        anchor.setText("+" + wfmStrings.moreReports());
//        anchor.addClickHandler((clickEvent -> {
//            if (Utils.hasPermission(PermissionConstants.REPORTING_SYSTEM) || Utils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
//                Utils.openURL(Utils.getHostURL() + Constants.ACCOUTING_REPORT);
//            } else {
//                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
//            }
//        }));
//        anchor.getElement().getStyle().setPaddingLeft(10, Style.Unit.PX);
//        panel.add(anchor);
//        return panel;
//    }
//
//    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
//        GWT.runAsync(new RunAsyncCallback() {
//
//            public void onFailure(Throwable caught) {
//                callback.onFailure(caught);
//            }
//
//            public void onSuccess() {
//                callback.onSuccess(onInitialize());
//            }
//        });
//    }
//
//    @Override
//    public String getPropertyCode() {
//        return "vatReturns";
//    }
//
//}
