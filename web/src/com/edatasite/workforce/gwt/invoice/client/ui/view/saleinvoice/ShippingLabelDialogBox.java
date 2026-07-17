package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AccountingImageBundle;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.ShippingLabelData;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.USPSPackage;
import com.edatasite.workforce.gwt.invoice.client.rpc.usps.USPSPostage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/30/12
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingLabelDialogBox extends KpiModal implements AccountingConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private AccountingImageBundle imageBundle = (AccountingImageBundle) GWT.create(AccountingImageBundle.class);

    private DateTimeFormat shipDateFormat = DateTimeFormat.getFormat("dd-MMM-yyyy");

    private TextBox fromZipTxtBox, toZipTxtBox;
    private HTML fromZipErrorLabel, toZipErrorLabel;
    private DatePicker mailingDatePicker;
    private DataListBox mailingTimeBox;

    private FlexTable settingsTable;
    private FlexTable ratesTable;
    private FlexTable rateItemsTable;
    private ExtraParatemersPanel extraParatemersPanel;
    private VerticalPanel mainPanel;

    private Integer selectedServiceType;

    private Integer invoiceID;

    public ShippingLabelDialogBox(Integer invoiceID) {
        this.invoiceID = invoiceID;
        initialize();
    }

    private void initialize() {
        setTitle(accountingStrings.shippingLabel());
        setWidth("600px");

        initializeSettingsTable();
        initializeRatesTable();

        InvoiceService.App.get().getShippingLabelData(invoiceID, new AsyncCallback<ShippingLabelData>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(ShippingLabelData result) {
                fromZipTxtBox.setText(result.getFromZip());
                toZipTxtBox.setText(result.getToZip());
//                if (result.getMailingDate() != null) {
//                    mailingDatePicker.setDate(result.getMailingDate().getNonConvertedDate());
//                }

                if (result.getServiceType() != null) {
                    FlexTable servicesTable = (FlexTable) settingsTable.getWidget(2, 0);
                    ServiceItem serviceItem = (ServiceItem) servicesTable.getWidget(0, result.getServiceType());
                    serviceItem.setSelected();
                    extraParatemersPanel.setData(result);
                }
            }
        });

        mainPanel = new VerticalPanel();
        mainPanel.add(settingsTable);

        add(mainPanel);
    }

    private void initializeSettingsTable() {
        fromZipTxtBox = new TextBox();
        toZipTxtBox = new TextBox();

        fromZipErrorLabel = new HTML();
        toZipErrorLabel = new HTML();

        mailingDatePicker = new DatePicker();
        mailingTimeBox = new DataListBox();
        mailingTimeBox.setWithoutNullLabel(true);

        SelectItem[] timeItems = new SelectItem[24];
        NumberFormat numberFormat = NumberFormat.getFormat("00");
        for (int i = 0; i < 24; i++) {
            timeItems[i] = new SelectItem(i, accountingMessages.afterTime(numberFormat.format(i) + ":00"));
        }
        mailingTimeBox.setItems(timeItems);
        mailingTimeBox.setSelected(8);

        settingsTable = new FlexTable();

        FlexTable zipCodeTable = new FlexTable();
        zipCodeTable.setWidget(0, 0, new HTML("<b>" + accountingStrings.enterZipCodes() + "</b>"));
        zipCodeTable.setWidget(1, 0, new HTML(accountingStrings.fromZIPCode()));
        zipCodeTable.setWidget(1, 1, fromZipTxtBox);
        zipCodeTable.setWidget(1, 2, new HTML(accountingStrings.toZIPCode()));
        zipCodeTable.setWidget(1, 3, toZipTxtBox);
        zipCodeTable.setWidget(2, 1, fromZipErrorLabel);
        zipCodeTable.setWidget(2, 3, toZipErrorLabel);

        zipCodeTable.setCellSpacing(10);

        FlexTable enterMailingTable = new FlexTable();
        enterMailingTable.setWidget(0, 0, new HTML("<b>" + accountingStrings.enterMailing() + "</b>"));
        enterMailingTable.setWidget(1, 0, new HTML(wfmStrings.date()));
        enterMailingTable.setWidget(1, 1, mailingDatePicker);
        enterMailingTable.setWidget(1, 2, new HTML(wfmStrings.time()));
        enterMailingTable.setWidget(1, 3, mailingTimeBox);
        enterMailingTable.setCellSpacing(10);

        FlexTable servicesTable = new FlexTable();
        servicesTable.setWidget(0, 0, new ServiceItem(POSTCARD, accountingStrings.postCard(), imageBundle.shippingPostcard()));
        servicesTable.setWidget(0, 1, new ServiceItem(LETTER, accountingStrings.letter(), imageBundle.shippingLetter()));
        servicesTable.setWidget(0, 2, new ServiceItem(LARGE_ENVELOPE, accountingStrings.largeEnvelope(), imageBundle.shippingLargeEnvelope()));
        servicesTable.setWidget(0, 3, new ServiceItem(PACKAGE, accountingStrings.getPropertyPackage(), imageBundle.shippingPackage()));
        servicesTable.setWidget(0, 4, new ServiceItem(LARGE_PACKAGE, accountingStrings.largePackage(), imageBundle.shippingLargePackage()));

        extraParatemersPanel = new ExtraParatemersPanel();
        servicesTable.setWidget(1, 0, extraParatemersPanel);
        servicesTable.getFlexCellFormatter().setColSpan(1, 0, 5);

        servicesTable.setCellSpacing(10);

        WfmButton2 continueButton = new WfmButton2(accountingStrings.getPropertyContinue(), (ClickHandler) event -> {
            if (!validate()) {
                return;
            }
            ShippingLabelData shippingLabelData = new ShippingLabelData();
            shippingLabelData.setInvoiceID(invoiceID);
            shippingLabelData.setFromZip(fromZipTxtBox.getText());
            shippingLabelData.setToZip(toZipTxtBox.getText());
            if (mailingDatePicker.getDate() != null) {
                shippingLabelData.setShipDate(shipDateFormat.format(mailingDatePicker.getDate()));
            }
            shippingLabelData.setServiceType(selectedServiceType);

            shippingLabelData = extraParatemersPanel.getData(shippingLabelData);

            InvoiceService.App.get().getUSPSRates(shippingLabelData, new AsyncCallback<USPSPackage[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    WfmWindow.alert(accountingStrings.errorOccuredWhileGettingRates());
                }

                @Override
                public void onSuccess(USPSPackage[] result) {
                    rateItemsTable.removeAllRows();
                    int inc = 0;
                    for (USPSPackage aResult : result) {
                        ArrayList<USPSPostage> postages = aResult.getPostages();
                        for (USPSPostage uspsPostage : postages) {
                            RadioButton radioButton = new KpiRadioButton("shipPostage" + invoiceID.toString(), "");
                            rateItemsTable.setWidget(inc, 0, radioButton);
                            rateItemsTable.setWidget(inc, 1, new USPSPostageHTML(uspsPostage.getMailService(), uspsPostage));
                            if (uspsPostage.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                                rateItemsTable.setWidget(inc, 2, new HTML(uspsPostage.getAmount().toString()));
                            } else {
                                rateItemsTable.setWidget(inc, 2, new HTML("N/A"));
                            }
                            rateItemsTable.getFlexCellFormatter().setHorizontalAlignment(inc, 2, HasHorizontalAlignment.ALIGN_RIGHT);
                            inc++;
                        }
                    }

                    mainPanel.clear();
                    mainPanel.add(ratesTable);
                }
            });
        });

        settingsTable.setWidget(0, 0, zipCodeTable);
        settingsTable.setWidget(1, 0, enterMailingTable);
        settingsTable.setWidget(2, 0, servicesTable);
        settingsTable.setWidget(3, 0, continueButton);
        settingsTable.getFlexCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_CENTER);
        settingsTable.setCellSpacing(10);
    }

    public boolean validate() {
        int errors = 0;

        if (!validateZipCodeField(fromZipTxtBox, fromZipErrorLabel)) {
            errors++;
        }
        if (!validateZipCodeField(toZipTxtBox, toZipErrorLabel)) {
            errors++;
        }
        if (selectedServiceType == null) {
            WfmWindow.alert(accountingStrings.pleaseSelectShape());
            errors++;
        }

        if (!extraParatemersPanel.validateRequiredFields()) {
            errors++;
        }

        return errors <= 0;
    }

    private boolean validateZipCodeField(TextBox textBox, HTML errorLabel) {
        int errors = 0;
        if (textBox.getText().trim().length() == 0) {
            errorLabel.setHTML("<font color='red'>" + accountingMessages.pleaseEnterZIPCode(" ") + "</font>");
            errors++;
        }
        if (textBox.getText().trim().length() != 5) {
            errorLabel.setHTML("<font color='red'>" + accountingMessages.pleaseEnterZIPCode(" " + accountingMessages.digit("5") + " ") + "</font>");
            errors++;
        }
        if (errors > 0) {
            addTxtBoxErrorHandler(textBox, errorLabel);
            return false;
        }

        return true;
    }

    private void addTxtBoxErrorHandler(TextBox textBox, final HTML errorLabel) {
        textBox.addChangeHandler(event -> errorLabel.setHTML(""));
    }

    private void initializeRatesTable() {

        ratesTable = new FlexTable();
        rateItemsTable = new FlexTable();
        rateItemsTable.setCellPadding(5);
        rateItemsTable.getColumnFormatter().setWidth(0, "40px");
        rateItemsTable.getColumnFormatter().setWidth(1, "390px");
        rateItemsTable.getColumnFormatter().setWidth(2, "150px");

        WfmButton2 backButton = new WfmButton2(wfmStrings.back(), (ClickHandler) event -> {
            mainPanel.clear();
            mainPanel.add(settingsTable);
        });

        WfmButton2 confirmButton = new WfmButton2(wfmStrings.confirm(), (ClickHandler) event -> {
            USPSPostage uspsPostage = null;

            Integer postagesRowCount = rateItemsTable.getRowCount();
            for (int i = 0; i < postagesRowCount; i++) {
                RadioButton rButton = (RadioButton) rateItemsTable.getWidget(i, 0);
                USPSPostageHTML postageHTML = (USPSPostageHTML) rateItemsTable.getWidget(i, 1);
                if (rButton.getValue()) {
                    uspsPostage = postageHTML.getPostage();
                }
            }

            if (uspsPostage == null) {
                WfmWindow.alert(accountingStrings.pleaseSelectService());
                return;
            }

            HashMap<String, String> parametersMap = new HashMap<>();
            parametersMap.put("invoiceID", invoiceID == null ? "" : String.valueOf(invoiceID));
            parametersMap.put("fromZip", fromZipTxtBox.getText());
            parametersMap.put("toZip", toZipTxtBox.getText());
            parametersMap.put("serviceName", uspsPostage.getMailService());

            parametersMap.put("weightInOunces", extraParatemersPanel.getWeightInOunces().toString());
            Utils.sendPDFOrExcelRequest(mainPanel, CommandConstants.PDF_URL + "/shippingLabelPDFHandler", parametersMap, "_blank");
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);

        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setSize("600px", "300px");
        scrollPanel.add(rateItemsTable);

        ratesTable.setWidget(0, 0, scrollPanel);
        ratesTable.setWidget(1, 0, buttonPanel);
        ratesTable.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
    }

    public class ServiceItem extends VerticalPanel {
        private RadioButton radioButton;
        private Integer serviceType;

        public ServiceItem(final Integer serviceType, String name, ImageResource imagePrototype) {
            this.serviceType = serviceType;
            radioButton = new KpiRadioButton("service", name);
            radioButton.addClickHandler(event -> setSelected());

            Image image = new Image(imagePrototype);
            add(image);
            add(radioButton);
            setCellHeight(image, "60px");
            setSpacing(10);
        }

        public void setSelected() {
            radioButton.setValue(true);
            selectedServiceType = serviceType;
            extraParatemersPanel.onPackageTypeChange();
        }
    }

    public class USPSPostageHTML extends HTML {
        private USPSPostage postage;

        public USPSPostageHTML(String html, USPSPostage postage) {
            super(html);
            this.postage = postage;
        }

        public USPSPostage getPostage() {
            return postage;
        }
    }

    public class ExtraParatemersPanel extends VerticalPanel {

        private FlexTable weightTable;
        private FlexTable volumeTable;

        private TextBox poundsTxtBox;
        private TextBox ouncesTxtBox;
        private TextBox lengthTxtBox;
        private TextBox heightTxtBox;
        private TextBox widthTxtBox;
        private TextBox girthTxtBox;

        private KpiCheckBox nonRectangularCheckBox;

        private HTML poundsOuncesErrorLabel, lengthErrorLabel, heightErrorLabel, widthErrorLabel, girthErrorLabel;

        public ExtraParatemersPanel() {
            poundsTxtBox = new TextBox();
            ouncesTxtBox = new TextBox();
            lengthTxtBox = new TextBox();
            heightTxtBox = new TextBox();
            widthTxtBox = new TextBox();
            girthTxtBox = new TextBox();

            poundsOuncesErrorLabel = new HTML();
            lengthErrorLabel = new HTML();
            heightErrorLabel = new HTML();
            widthErrorLabel = new HTML();
            girthErrorLabel = new HTML();

            final HTML girthLabel = new HTML(accountingStrings.girth());

            girthTxtBox.setVisible(false);
            girthErrorLabel.setVisible(false);
            girthLabel.setVisible(false);

            nonRectangularCheckBox = new KpiCheckBox(accountingStrings.nonRectangular());
            nonRectangularCheckBox.addValueChangeHandler(booleanValueChangeEvent -> {
                boolean visible = nonRectangularCheckBox.getValue();
                girthTxtBox.setVisible(visible);
                girthErrorLabel.setVisible(visible);
                girthLabel.setVisible(visible);
            });

            poundsTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            ouncesTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            lengthTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            heightTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            widthTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            girthTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
            Validation.addNumericKeyboardListener(poundsTxtBox, 0);
            Validation.addNumericKeyboardListener(ouncesTxtBox, 0);
            Validation.addNumericKeyboardListener(lengthTxtBox, 0);
            Validation.addNumericKeyboardListener(heightTxtBox, 0);
            Validation.addNumericKeyboardListener(widthTxtBox, 0);
            Validation.addNumericKeyboardListener(girthTxtBox, 0);


            weightTable = new FlexTable();
            weightTable.setWidget(0, 0, new HTML(accountingStrings.pounds()));
            weightTable.setWidget(0, 1, poundsTxtBox);
            weightTable.setWidget(0, 2, new HTML(accountingStrings.ounces()));
            weightTable.setWidget(0, 3, ouncesTxtBox);
            weightTable.setWidget(1, 1, poundsOuncesErrorLabel);
            weightTable.getFlexCellFormatter().setColSpan(1, 1, 3);

            volumeTable = new FlexTable();
            volumeTable.setWidget(0, 0, new HTML(accountingStrings.length()));
            volumeTable.setWidget(0, 1, lengthTxtBox);
            volumeTable.setWidget(0, 2, new HTML(wfmStrings.height()));
            volumeTable.setWidget(0, 3, heightTxtBox);
            volumeTable.setWidget(0, 4, new HTML(wfmStrings.width()));
            volumeTable.setWidget(0, 5, widthTxtBox);

            volumeTable.setWidget(1, 1, lengthErrorLabel);
            volumeTable.setWidget(1, 3, heightErrorLabel);
            volumeTable.setWidget(1, 5, widthErrorLabel);

            volumeTable.setWidget(2, 0, nonRectangularCheckBox);
            volumeTable.setWidget(2, 1, girthLabel);
            volumeTable.setWidget(2, 2, girthTxtBox);
            volumeTable.setWidget(3, 3, girthErrorLabel);
            volumeTable.getFlexCellFormatter().setColSpan(2, 0, 2);

            weightTable.setCellSpacing(10);
            volumeTable.setCellSpacing(10);
            weightTable.setVisible(false);
            volumeTable.setVisible(false);

            add(weightTable);
            add(volumeTable);
        }

        public void onPackageTypeChange() {
            weightTable.setVisible(!POSTCARD.equals(selectedServiceType));
            volumeTable.setVisible(LARGE_PACKAGE.equals(selectedServiceType));
        }

        public void setData(ShippingLabelData result) {
            if (result.getPounds() != null) {
                poundsTxtBox.setText(result.getPounds().toString());
            }
            if (result.getOunces() != null) {
                ouncesTxtBox.setText(result.getOunces().toString());
            }
            if (result.getLength() != null) {
                lengthTxtBox.setText(result.getLength().toString());
            }
            if (result.getHeight() != null) {
                heightTxtBox.setText(result.getHeight().toString());
            }
            if (result.getWidth() != null) {
                widthTxtBox.setText(result.getWidth().toString());
            }
        }

        public ShippingLabelData getData(ShippingLabelData shippingLabelData) {
            shippingLabelData.setContainer(nonRectangularCheckBox.getValue() ? "NONRECTANGULAR" : "RECTANGULAR");
            if (!"".equals(poundsTxtBox.getText().trim())) {
                shippingLabelData.setPounds(Integer.parseInt(poundsTxtBox.getText().trim()));
            }
            if (!"".equals(ouncesTxtBox.getText().trim())) {
                shippingLabelData.setOunces(Integer.parseInt(ouncesTxtBox.getText().trim()));
            }
            if (!"".equals(lengthTxtBox.getText().trim())) {
                shippingLabelData.setLength(Integer.parseInt(lengthTxtBox.getText().trim()));
            }
            if (!"".equals(heightTxtBox.getText().trim())) {
                shippingLabelData.setHeight(Integer.parseInt(heightTxtBox.getText().trim()));
            }
            if (!"".equals(widthTxtBox.getText().trim())) {
                shippingLabelData.setWidth(Integer.parseInt(widthTxtBox.getText().trim()));
            }
            if (!"".equals(girthTxtBox.getText().trim())) {
                shippingLabelData.setGirth(Integer.parseInt(girthTxtBox.getText().trim()));
            }
            return shippingLabelData;
        }

        public boolean validateRequiredFields() {
            int errors = 0;
            if (selectedServiceType != null && !POSTCARD.equals(selectedServiceType)) {
                if (poundsTxtBox.getText().trim().length() == 0 && ouncesTxtBox.getText().trim().length() == 0) {
                    poundsOuncesErrorLabel.setHTML("<font color='red'>" + accountingStrings.pleaseEnterPoundsOrOunces() + "</font>");
                    addTxtBoxErrorHandler(poundsTxtBox, poundsOuncesErrorLabel);
                    errors++;
                } else {
                    Integer ounces = 0;
                    if (poundsTxtBox.getText().trim().length() != 0) {
                        ounces = Integer.parseInt(poundsTxtBox.getText().trim()) * 16;
                    }
                    if (ouncesTxtBox.getText().trim().length() != 0) {
                        ounces = ounces + Integer.parseInt(ouncesTxtBox.getText().trim());
                    }

                    if (ounces == 0) {
                        poundsOuncesErrorLabel.setHTML("<font color='red'>" + accountingStrings.pleaseEnterPoundsOrOunces() + "</font>");
                        addTxtBoxErrorHandler(poundsTxtBox, poundsOuncesErrorLabel);
                        errors++;
                    }
                }


                if (LARGE_PACKAGE.equals(selectedServiceType)) {
                    if (lengthTxtBox.getText().trim().length() == 0) {
                        lengthErrorLabel.setHTML("<font color='red'>" + accountingMessages.pleaseEnter(accountingStrings.length()) + "</font>");
                        addTxtBoxErrorHandler(lengthTxtBox, lengthErrorLabel);
                        errors++;
                    } else if (Integer.parseInt(lengthTxtBox.getText().trim()) == 0) {
                        lengthErrorLabel.setHTML("<font color='red'>" + accountingMessages.cannotBeZero(accountingStrings.length()) + "</font>");
                        addTxtBoxErrorHandler(lengthTxtBox, lengthErrorLabel);
                        errors++;
                    }
                    if (heightTxtBox.getText().trim().length() == 0) {
                        heightErrorLabel.setHTML("<font color='red'>" + accountingMessages.pleaseEnter(wfmStrings.height()) + "</font>");
                        addTxtBoxErrorHandler(heightTxtBox, heightErrorLabel);
                        errors++;
                    } else if (Integer.parseInt(heightTxtBox.getText().trim()) == 0) {
                        heightErrorLabel.setHTML("<font color='red'>" + accountingMessages.cannotBeZero(wfmStrings.height()) + "</font>");
                        addTxtBoxErrorHandler(heightTxtBox, heightErrorLabel);
                        errors++;
                    }
                    if (widthTxtBox.getText().trim().length() == 0) {
                        widthErrorLabel.setHTML("<font color='red'>" + accountingMessages.pleaseEnter(wfmStrings.width()) + "</font>");
                        addTxtBoxErrorHandler(widthTxtBox, widthErrorLabel);
                        errors++;
                    } else if (Integer.parseInt(widthTxtBox.getText().trim()) == 0) {
                        widthErrorLabel.setHTML("<font color='red'>" + accountingMessages.cannotBeZero(wfmStrings.width()) + "</font>");
                        addTxtBoxErrorHandler(widthTxtBox, widthErrorLabel);
                        errors++;
                    }

                    if (nonRectangularCheckBox.getValue()) {
                        if (girthTxtBox.getText().trim().length() == 0) {
                            girthErrorLabel.setHTML("<font color='red'>" + accountingMessages.pleaseEnter(accountingStrings.girth()) + "</font>");
                            addTxtBoxErrorHandler(girthTxtBox, girthErrorLabel);
                            errors++;
                        } else if (Integer.parseInt(girthTxtBox.getText().trim()) == 0) {
                            girthErrorLabel.setHTML("<font color='red'>" + accountingMessages.cannotBeZero(accountingStrings.girth()) + "</font>");
                            addTxtBoxErrorHandler(girthTxtBox, girthErrorLabel);
                            errors++;
                        }
                    }
                }
            }

            return errors == 0;
        }

        public Integer getWeightInOunces() {
            Integer weightInOunces = 0;
            if (!"".equals(poundsTxtBox.getText().trim())) {
                weightInOunces = Integer.parseInt(poundsTxtBox.getText().trim()) * 16;
            }
            if (!"".equals(ouncesTxtBox.getText().trim())) {
                weightInOunces = weightInOunces + Integer.parseInt(ouncesTxtBox.getText().trim());
            }
            return weightInOunces;
        }
    }
}
