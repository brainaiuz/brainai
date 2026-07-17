package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCHtmlTemplates;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 11/19/12
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceGeneratorView extends CustomForm2 implements FittedContent, Constants {
    public static TCStrings tcStrings = TCStrings.App.get();

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private WfmButton2 generateInvoice;
    private WfmButton2 reGenerateInvoice;
    private FormGroup startDateWidget;
    private FormGroup endDateWidget;

    public InvoiceGeneratorView() {
        super("invoicegenerator");
        setDescription(property.getPlural(tcStrings.invoiceGenerator()));
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initGeneratorForm();
        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initGeneratorForm() {
        startDatePicker = new DatePicker(true);
        startDatePicker.addStyleName("form-group__content");
        startDatePicker.addChangeHandler(changeEvent -> {
            startDatePicker.setDate(DateUtil.getMonthFirstDay(startDatePicker.getDate()));
            endDatePicker.setDate(DateUtil.getMonthLastDate(startDatePicker.getDate()));
        });
        startDateWidget = new FormGroup("Period", startDatePicker);
        startDateWidget.addStyleName(DEFAULT_WIDTH);
        startDateWidget.setMarginRight(10);

        endDatePicker = new DatePicker(true);
        endDatePicker.addStyleName("form-group__content");
        endDatePicker.addChangeHandler(changeEvent -> {
            endDatePicker.setDate(DateUtil.getMonthLastDate(endDatePicker.getDate()));
            startDatePicker.setDate(DateUtil.getMonthFirstDay(endDatePicker.getDate()));
        });
        endDateWidget = new FormGroup("End", endDatePicker);
        endDateWidget.addStyleName(DEFAULT_WIDTH);


        HorizontalPanel pnlPeriodContainer = new HorizontalPanel();
        pnlPeriodContainer.addStyleName("width-limiter");
        pnlPeriodContainer.add(startDateWidget);
        pnlPeriodContainer.add(endDateWidget);


        MaterialPanel mainPanel = new MaterialPanel();
        mainPanel.addStyleName("content-box content-box--white");
        mainPanel.add(new HTML(TCHtmlTemplates.getInstance().invoiceGenerationInfo()));
        mainPanel.add(pnlPeriodContainer);
        mainPanel.add(createFooter());

        add(mainPanel);
        addWidgetsToForm();
        show();
    }

    @Override
    protected void addButtons() {
        generateInvoice = new WfmButton2("Generate Invoice", WfmButton2.BTN_PRIMARY, (ClickHandler) event -> {
            enableButton(false);

            if (validate()) {
                generateInvoices();
            } else {
                enableButton(true);
            }
        });

        reGenerateInvoice = new WfmButton2("Regenerate Invoice", WfmButton2.BTN_PRIMARY, (ClickHandler) event -> {
            enableButton(false);

            if (validate()) {
                reGenerateInvoices();
            } else {
                enableButton(true);
            }
        });

        addButton(generateInvoice);
        addButton(reGenerateInvoice);
    }

    @Override
    protected void getDataToFillFields() {

    }

    @Override
    protected String getFormID() {
        return "INVOICE_GENERATOR_FORM";
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }
    private void addWidgetsToForm() {
        addTitleField(INFORMATION, wfmStrings.information());
        //1.1
        addField(INVOICE_GENERATOR.PERIOD, startDatePicker, getTitle(Property.get("PERIOD", wfmStrings.period()), true));
        //2.1
        addField(INVOICE_GENERATOR.END,endDatePicker, getTitle(wfmStrings.end(), true));


    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return InvoiceGeneratorView.this.getFooterRightSideWidgets();
            }
        });
    }


    private List<Widget> getFooterRightSideWidgets() {
        generateInvoice = new WfmButton2("Generate Invoice", WfmButton2.BTN_PRIMARY, (ClickHandler) event -> {
            enableButton(false);

            if (validate()) {
                generateInvoices();
            } else {
                enableButton(true);
            }
        });

        reGenerateInvoice = new WfmButton2("Regenerate Invoice", WfmButton2.BTN_PRIMARY, (ClickHandler) event -> {
            enableButton(false);

            if (validate()) {
                reGenerateInvoices();
            } else {
                enableButton(true);
            }
        });

        List<Widget> list = new ArrayList<>();
        Div div = new Div();
        div.add(generateInvoice);
        list.add(reGenerateInvoice);
        list.add(div);
        return list;
    }

    private void generateInvoices() {
        final ListingFilterParameter fp = new ListingFilterParameter();
        Date startDate = (Date) startDatePicker.getDate().clone();
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        fp.setStartDate(startDate);

        Date endDate = (Date) endDatePicker.getDate().clone();
        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);
        fp.setEndDate(endDate);

        LoadingPanel.loading(true);
        TCService.App.get().checkGeneratorSchedule(new DateNonConvertable(startDate), new DateNonConvertable(endDate), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
            }

            @Override
            public void onSuccess(Integer scheduleID) {
                if (scheduleID == -1) {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    Info.get().show("Invoice generator is in process for this preiod, please try it in 5 min.!", Info.Type.WARNING);
                } else {
                    fp.setCaseID(scheduleID);
                    TCService.App.get().scheduleGenerateInvoice(scheduleID, new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            enableButton(true);
                            throwable.printStackTrace();
                            Info.get().show("Generate invoices process is failed!", Info.Type.WARNING);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            LoadingPanel.loading(false);
                            enableButton(true);
                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
                            messageBox.setWidth(560);
                            //messageBox.setSize(560, 150);
                            messageBox.setTitle("Generate invoices process");
                            messageBox.setMessage("Your Invoices are being generated." +
                                    " This may take a while depending on the size of your data." +
                                    " You will receive a confirmation email upon completion of  process or you can check it within 5 minutes.");
                            messageBox.open();
                        }
                    });
                }
            }
        });

    }

    private void reGenerateInvoices() {
        final ListingFilterParameter fp = new ListingFilterParameter();
        Date startDate = (Date) startDatePicker.getDate().clone();
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        fp.setStartDate(startDate);

        Date endDate = endDatePicker.getDate();
        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);
        fp.setEndDate(endDate);

        LoadingPanel.loading(true);
        TCService.App.get().checkGeneratorSchedule(new DateNonConvertable(startDate), new DateNonConvertable(endDate), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
            }

            @Override
            public void onSuccess(Integer scheduleID) {
                if (scheduleID == -1) {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    Info.get().show("Invoice generator is in process for this preiod, please try it in 5 min.!", Info.Type.WARNING);
                } else {
                    fp.setCaseID(scheduleID);

                    TCService.App.get().scheduleRegenerateInvoice(scheduleID, new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            enableButton(true);
                            throwable.printStackTrace();
                            Info.get().show("Regenerate invoices process is failed!", Info.Type.WARNING);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            LoadingPanel.loading(false);
                            enableButton(true);

                            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, true);
//                            messageBox.setSize(560, 150);
                            messageBox.setWidth(560);
                            messageBox.setTitle("Regenerate invoices process");
                            messageBox.setMessage("Your Invoices are being regenerated." +
                                    " This may take a while depending on the size of your data." +
                                    " You will receive a confirmation email upon completion of  process or you can check it within 5 minutes.");
                            messageBox.open();
                        }
                    });
                }
            }
        });
    }

    private String errorMessage;

    private boolean validate() {
        int errors = 0;

        if (!Validation.validateDate(startDatePicker)) {
            errors++;
        }

        if (!Validation.validateDate(endDatePicker)) {
            errors++;
        }

        if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {

            if (startDatePicker.getDate().after(endDatePicker.getDate())) {
                errorMessage = "End Date should be after Start Date.";
                errors++;
            }

            long diff = (endDatePicker.getDate().getTime() - startDatePicker.getDate().getTime()) / 60000;
            diff = 1 + diff / (60 * 24);

            if (diff > 31) {
                errors++;
                errorMessage = "Sorry, you can select maximum of 1 month period for invoice generation!";
            }

        }

        if (errors > 0) {
            if (errorMessage == null) {
                Info.get().show("Period is required fields!", Info.Type.WARNING);
            } else {
                Info.get().show(errorMessage, Info.Type.WARNING);
            }
        }
        return errors == 0;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void enableGeneratorButton(boolean b) {
        generateInvoice.setEnabled(b);
        reGenerateInvoice.setEnabled(b);
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
    @Override
    public String getPropertyCode() {
        return "invoicegenerator";
    }
}
