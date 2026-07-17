package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnTransferObject;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VATSettingsItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.report.VatReturnReportDetailsBox;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxDatePeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.SectionBoxPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.addins.client.menubar.MaterialMenuBar;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;



/**
 * Created by admin on 19.09.2014.
 */
public class NewVatReturnReport extends Composite {
    interface VatReturnReportUiBinder extends UiBinder<HTMLPanel, NewVatReturnReport> {
    }

    private static final VatReturnReportUiBinder ourUiBinder = GWT.create(VatReturnReportUiBinder.class);


    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private VatReturnTransferObject vatReturnReport;
    private BigDecimal flatPercent;
    private VatReturnReportDetailsBox detailsBox;
    private MaterialLink pdfVersion;
    private MaterialLink portrait;
    private MaterialLink landscape;
    private DatePicker fromValue;
    private DatePicker toValue;

    @UiField
    HTMLPanel topPanel;
    @UiField
    SectionBoxPanel headerPanel;
    @UiField
    DivElement vatReturnReportSection;

    private boolean useAsOldOne = false;

    public NewVatReturnReport() {
        this(false);
    }

    public NewVatReturnReport(boolean useAsOldOne) {
        this.useAsOldOne = useAsOldOne;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        onInitialize();
    }

    public void onInitialize() {
        GBox groupBox = headerPanel.drawNewGroupBox();
        groupBox.setStyleUnited(true);
        groupBox.setStyleWidthFree(true);

        flatPercent = AccountingConstants.ZERO;

        GBoxDatePeriodItem datePeriodItem = new GBoxDatePeriodItem();
        Date date = new Date();
        fromValue = new DatePicker();
        fromValue.setDate(DateUtil.getMonthFirstDay(date));
        datePeriodItem.setStartBoxItem(wfmStrings.from(), fromValue);
        datePeriodItem.setStyleSplitRight(true);
        toValue = new DatePicker();
        toValue.setDate(DateUtil.getMonthLastDate(date));
        datePeriodItem.setDueBoxItem(wfmStrings.to(), toValue);
        headerPanel.addGroupBoxItem(datePeriodItem);

        if (!useAsOldOne) {
            WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
            saveButton.addClickHandler(event -> saveVatReturnReport());

            GBoxItem saveItem = headerPanel.addGroupBoxItem(null, saveButton);
            saveItem.setStyleSplitRight(true);
        }

        WfmButton2 updateButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        updateButton.addClickHandler(event -> getAndSetVatReturnReport());

        GBoxItem updateItem = headerPanel.addGroupBoxItem(null, updateButton);
        updateItem.setStyleSplitRight(true);
        exportSection();

        detailsBox = new VatReturnReportDetailsBox();

        if (!useAsOldOne) {
            AccountingService.App.get().getVatReturnDateInterval(new AsyncCallback<DateNonConvertable[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(DateNonConvertable[] dates) {
                    fromValue.setDate(dates[0].getNonConvertedDate());
                    toValue.setDate(dates[1].getNonConvertedDate());
                    toValue.synchronizeFromDate();
                    getAndSetVatReturnReport();
                }
            });
        } else {
            VatReturnService.App.get().getVATSettings(new AsyncCallback<VATSettingsItem>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(VATSettingsItem settingsItem) {
                    Date taxGenerateFrom = settingsItem.getTaxGenerationDate() != null ? settingsItem.getTaxGenerationDate().getNonConvertedDate() : new Date();
                    fromValue.setDate(DateUtil.getMonthFirstDay(taxGenerateFrom));
                    toValue.setDate(taxGenerateFrom);
                    toValue.setEnabled(false);
                    getAndSetVatReturnReport();
                }
            });
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEINVOICE_ADDED, NewVatReturnReport.this, (sender, args) -> getAndSetVatReturnReport());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, NewVatReturnReport.this, (sender, args) -> getAndSetVatReturnReport());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, NewVatReturnReport.this, (sender, args) -> getAndSetVatReturnReport());
    }

    private void saveVatReturnReport() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);
        AccountingService.App.get().saveVatReturnReport(Utils.getStartDateNC(fromValue.getDate()), Utils.getEndDateNC(toValue.getDate()), vatReturnReport, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(String message) {
                LoadingPanel.loading(false);
                if (message != null) {
                    Info.show(message, Info.Type.INFO);
                    return;
                }
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.vatReturn()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VAT_RETURN_REPORT_EFILED, null, NewVatReturnReport.this);
            }
        });
    }

    private void getAndSetVatReturnReport() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);
        AccountingService.App.get().getVatReturnReport(Utils.getStartDateNC(fromValue.getDate()), Utils.getEndDateNC(toValue.getDate()),
                flatPercent, new AbstractAsyncCallback<VatReturnTransferObject>() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    public void success(VatReturnTransferObject result) {
                        clearElementChild(vatReturnReportSection);
                        vatReturnReport = result;
                        vatReturnReport.setFrom(new DateNonConvertable(fromValue.getDate()));
                        vatReturnReport.setTo(new DateNonConvertable(toValue.getDate()));
                        detailsBox.setDetailItemsMap(vatReturnReport.getDetails());

                        ////VAT Return Details
                        String vatReturnDetailStr = accountingStrings.vatReturnDetails();
                        String vatSchemaStr = accountingStrings.vatScheme();
                        String vatCalcStr = wfmStrings.vatCalculations();
                        String vatDueStr = wfmStrings.vatDueThisPeriod();
                        String totalVatStr = accountingStrings.totalVATDue();
                        String vatReclaimedStr = accountingStrings.vatReclaimedOnCapitalPurchases();
                        String vatToPayStr = accountingStrings.vatToPayToCustoms();
                        String vatToReclaimStr = accountingStrings.vatToReclaimFromCustoms();
                        String salesAndPurchasesStr = accountingStrings.salesAndPurchasesExcludingVAT();
                        String vatReclaimedInThisPeriodStr = Utils.isArabicCompany() ? accountingStrings.vatReclaimedInThisPeriodGCC() : accountingStrings.vatReclaimedInThisPeriod();
                        String vatDueInThisPeriodStr = Utils.isArabicCompany() ? wfmStrings.vatDueInThisPeriodOnAcquisitionsGCC() : wfmStrings.vatDueInThisPeriodOnAcquisitions();
                        String enterAmountForVATStr = Utils.isArabicCompany() ? wfmStrings.enterAmountForVATOnAnyGoodsGCC() : wfmStrings.enterAmountForVATOnAnyGoods();
                        String totalValueOfSalesStr = Utils.isArabicCompany() ? accountingStrings.totalValueOfSalesAndAllGCC() : accountingStrings.totalValueOfSalesAndAll();
                        String totalValueOfPurchasesStr = Utils.isArabicCompany() ? accountingStrings.totalValueOfPurchasesAndAllOtherGCC() : accountingStrings.totalValueOfPurchasesAndAllOther();
                        String ecSuppliesAndPurchasesStr = Utils.isArabicCompany() ? wfmStrings.gccSuppliesAndPurchasesExcludingVAT() : wfmStrings.ecSuppliesAndPurchasesExcludingVAT();
                        String totalValueOfAllSuppliesStr = Utils.isArabicCompany() ? accountingStrings.totalValueOfAllSuppliesOfGoodsGCC() : accountingStrings.totalValueOfAllSuppliesOfGoods();
                        String totalValueOfAllAcquisitionsStr = Utils.isArabicCompany() ? accountingStrings.totalValueOfAllAcquisitionsOfGoodsGCC() : accountingStrings.totalValueOfAllAcquisitionsOfGoods();
                        String theAmountOfStr = accountingStrings.theAmountOfVATYou();
                        if (isCustomTaxEnabled()) {
                            vatReturnDetailStr = vatReturnDetailStr.replace("Vat", Utils.getCustomTaxName());
                            vatSchemaStr = vatSchemaStr.replace("Vat", Utils.getCustomTaxName());
                            vatCalcStr = vatCalcStr.replace("VAT", Utils.getCustomTaxName());
                            vatDueStr = vatDueStr.replace("VAT", Utils.getCustomTaxName());
                            enterAmountForVATStr = enterAmountForVATStr.replace("VAT", Utils.getCustomTaxName());
                            vatDueInThisPeriodStr = vatDueInThisPeriodStr.replace("VAT", Utils.getCustomTaxName());
                            totalVatStr = totalVatStr.replace("VAT", Utils.getCustomTaxName());
                            vatReclaimedStr = vatReclaimedStr.replace("VAT", Utils.getCustomTaxName());
                            vatReclaimedInThisPeriodStr = vatReclaimedInThisPeriodStr.replace("VAT", Utils.getCustomTaxName());
                            vatToPayStr = vatToPayStr.replace("VAT", Utils.getCustomTaxName());
                            vatToReclaimStr = vatToReclaimStr.replace("VAT", Utils.getCustomTaxName());
                            salesAndPurchasesStr = salesAndPurchasesStr.replace("VAT", Utils.getCustomTaxName());
                            totalValueOfSalesStr = totalValueOfSalesStr.replace("VAT", Utils.getCustomTaxName());
                            totalValueOfPurchasesStr = totalValueOfPurchasesStr.replace("VAT", Utils.getCustomTaxName());
                            ecSuppliesAndPurchasesStr = ecSuppliesAndPurchasesStr.replace("VAT", Utils.getCustomTaxName());
                            totalValueOfAllSuppliesStr = totalValueOfAllSuppliesStr.replace("VAT", Utils.getCustomTaxName());
                            totalValueOfAllAcquisitionsStr = totalValueOfAllAcquisitionsStr.replace("VAT", Utils.getCustomTaxName());
                            theAmountOfStr = theAmountOfStr.replace("VAT", Utils.getCustomTaxName());
                        }
                        Element tbodyDetails = createTableBody(vatReturnReportSection, vatReturnDetailStr);
                        createDetailsElement(tbodyDetails, wfmStrings.registrationNumber(), result.getRegistrationNumber());
                        createDetailsElement(tbodyDetails, vatSchemaStr, result.getVatScheme());
                        createDetailsElement(tbodyDetails, accountingStrings.periodCoveredBy(), getVatPeriodType(result.getPeriodCovered()));
                        createDetailsElement(tbodyDetails, wfmStrings.from(), DateUtils.format(fromValue.getDate()));
                        createDetailsElement(tbodyDetails, wfmStrings.to(), DateUtils.format(toValue.getDate()));
                        createDetailsElement(tbodyDetails, accountingStrings.thisReturnAndAnyPayment(),
                                result.getPaymentDueDate() != null ? DateUtils.format(result.getPaymentDueDate().getNonConvertedDate()) : " ");

                        //VAT Calculations
                        Element tbodyVatCalculations = createTableBody(vatReturnReportSection, vatCalcStr);
                        createtBoxElement(tbodyVatCalculations, vatDueStr, result.getVatOnSalesAndOutputs(), 1, true);
                        if (result.isFlatRate()) {
                            //flatRateAmount.setText(AccountingUtils.get().formatPrice(AccountingConstants.ZERO));
                            createFlatRateElement(tbodyVatCalculations, enterAmountForVATStr, 2);
                        } else {
                            createtBoxElement(tbodyVatCalculations, vatDueInThisPeriodStr, result.getVatFromECMemberStates(), 2, true);
                        }
                        createtBoxElement(tbodyVatCalculations, totalVatStr, result.getTotalVatDue(), 3, false);
                        if (result.isFlatRate()) {
                            createtBoxElement(tbodyVatCalculations, vatReclaimedStr, result.getVatOnPurchaseAndInputs(), 4, true);
                        } else {
                            createtBoxElement(tbodyVatCalculations, vatReclaimedInThisPeriodStr, result.getVatOnPurchaseAndInputs(), 4, true);
                        }
                        if (result.getVatToReclaimFromCustoms().compareTo(AccountingConstants.ZERO) >= 0) {
                            createtBoxElement(tbodyVatCalculations, "<b>" + vatToPayStr + "</b>", result.getVatToReclaimFromCustoms(), 5, false);
                        } else {
                            createtBoxElement(tbodyVatCalculations, "<b>" + vatToReclaimStr + "</b>", result.getVatToReclaimFromCustoms(), 5, false);
                        }
                        //Sales and Purchases Excluding VAT
                        Element tbodySalesPurchaseExcludingVat = createTableBody(vatReturnReportSection, salesAndPurchasesStr);
                        createtBoxElement(tbodySalesPurchaseExcludingVat, totalValueOfSalesStr,
                                result.getTotalSalesAndOutputs(), 6, true);
                        createtBoxElement(tbodySalesPurchaseExcludingVat, totalValueOfPurchasesStr,
                                result.getTotalPurchasesAndInputs(), 7, true);
                        //EC Supplies and Purchases Excluding VAT
                        Element tbodyEcSuppliesPurchases = createTableBody(vatReturnReportSection, ecSuppliesAndPurchasesStr);
                        createtBoxElement(tbodyEcSuppliesPurchases, totalValueOfAllSuppliesStr, result.getTotalSupplies(), 8, true);
                        createtBoxElement(tbodyEcSuppliesPurchases, totalValueOfAllAcquisitionsStr, result.getTotalAcquisitions(), 9, true);

                        if (vatReturnReport.isFlatRate()) {
                            createtBoxElement(tbodyEcSuppliesPurchases, "<b>" + theAmountOfStr + "</b>", result.getFlatRateSchemeVatDifference(), null, false);
                            calculateFlatRateChanges();
                        }
                        LoadingPanel.loading(false);
                        Utils.table__frame_affix_init();
                    }
                }
        );
    }

    private boolean validate() {
        int errors = 0;
        fromValue.removeStyleName(Constants.ERROR_FORM_STYLE);
        toValue.removeStyleName(Constants.ERROR_FORM_STYLE);

        if (!Validation.validateDate(fromValue)) {
            errors++;
        }
        if (!Validation.validateDate(toValue)) {
            errors++;
        }

        if (!Validation.validateDateOrder(fromValue, toValue)) {
            return false;
        }

        return errors == 0;
    }

    private Element createTableBody(Element element, String name) {
        Element div = DOM.createDiv();
        div.addClassName("table-responsive baseMargin_bottom_double");
        Element table = DOM.createTable();
        table.addClassName("table table--small-cells table-condensed table-bordered table_striped_cols table-hover table_report table_report_sections valign_middle table_leftIndex");
        table.setAttribute("cellspacing", "0");
        table.setAttribute("cellpadding", "0");
        div.appendChild(table);
        createHeader(table, name);
        Element tbody = DOM.createTBody();
        table.appendChild(tbody);
        element.appendChild(div);
        return tbody;
    }

    private void createDetailsElement(Element element, String name, String value) {
        Element tr = DOM.createTR();
        Element td1 = DOM.createTD();
        td1.setInnerHTML(name);
        Element td2 = DOM.createTD();
        Element td3 = DOM.createTD();
        td3.setInnerHTML(value);
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        element.appendChild(tr);
    }

    private void createFlatRateElement(Element element, String name, int box) {
        Element tr = DOM.createTR();

        Element td1 = DOM.createTD();
        td1.setInnerHTML(name);

        Element td2 = DOM.createTD();
        td2.addClassName(Constants.TEXT_RIGHT);
        td2.setAttribute("style", "padding-right:15px");
        td2.setInnerHTML("<b>" + box + "</b>");

        Element td3 = DOM.createTD();
        td3.addClassName(Constants.TEXT_RIGHT);
        td3.setInnerHTML(AccountingUtils.get().formatPrice(AccountingConstants.ZERO));

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        element.appendChild(tr);
    }

    private void createtBoxElement(Element element, String name, BigDecimal value, final Integer box, boolean linkable) {
        Element tr = DOM.createTR();
        Element td1 = DOM.createTD();
        td1.setInnerHTML(name);
        Element td2 = DOM.createTD();
        td2.addClassName(Constants.TEXT_RIGHT);
        td2.setAttribute("style", "padding-right:15px");
        td2.setInnerHTML(box != null ? ("<b>" + box + "</b>") : "");
        String valueAsString = value != null ? getValueAsString(value) : " ";
        Element td3 = DOM.createTD();
        td3.addClassName(Constants.TEXT_RIGHT);
        if (linkable) {
            td3.appendChild(getAsLink(valueAsString, box));
        } else {
            td3.setInnerHTML(valueAsString);
        }

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        element.appendChild(tr);
    }

    private void createHeader(Element element, String name) {
        Element thead = DOM.createTHead();
        thead.setClassName("point_affix_top");
        thead.setAttribute("point_affix_top_below_selector", ".filters-account-reports");

        Element tr = DOM.createTR();

        Element th = DOM.createTH();
        Element th2 = DOM.createTH();
        Element th3 = DOM.createTH();

        Element div = DOM.createDiv();
        div.setClassName("frame_affix_top");
        div.setAttribute("style", "min-width:300px;");
        div.setInnerHTML(name);

        Element div2 = DOM.createDiv();
        div2.setClassName("frame_affix_top");
        div2.setAttribute("style", "min-width:20px;");

        Element div3 = DOM.createDiv();
        div3.setClassName("frame_affix_top");
        div3.setAttribute("style", "min-width:150px;");

        th.addClassName("text-left");
        th.appendChild(div);
        th2.appendChild(div2);
        th3.appendChild(div3);

        tr.appendChild(th);
        tr.appendChild(th2);
        tr.appendChild(th3);

        element.appendChild(thead);
        thead.appendChild(tr);
    }

    private void calculateFlatRateChanges() {
        if (vatReturnReport != null) {
            //Box3 calculation
            vatReturnReport.setTotalVatDue(vatReturnReport.getVatOnSalesAndOutputs().add(vatReturnReport.getVatFromECMemberStates()));
            //Box5 calculation
            vatReturnReport.setVatToReclaimFromCustoms(vatReturnReport.getTotalVatDue().subtract(vatReturnReport.getVatOnPurchaseAndInputs()));
        }
    }

    private String getVatPeriodType(String periodTypeCode) {
        if (AccountingConstants.MONTHLY1.equals(periodTypeCode)) {
            return wfmStrings.monthly();
        } else if (AccountingConstants.QUARTERLY.equals(periodTypeCode)) {
            return wfmStrings.quarterly();
        } else if (AccountingConstants.ANNUAL.equals(periodTypeCode)) {
            return wfmStrings.annual();
        }
        return wfmStrings.custom();
    }

    private boolean isCustomTaxEnabled() {
        return Utils.getCustomTaxName() != null && !"".equals(Utils.getCustomTaxName());
    }

    private void clearElementChild(Element element) {
        Element child;
        while ((child = element.getFirstChildElement()) != null) {
            element.removeChild(child);
        }
    }

    private Element getAsLink(String text, final Integer box) {
        Element link = DOM.createAnchor();
        link.setInnerHTML(text);
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        DOM.setEventListener(link.cast(), event -> detailsBox.initForm(box));

        return link;
    }

    private String getValueAsString(BigDecimal value) {
        if (value == null) {
            return "";
        }
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return " " + AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    private void exportSection() {
        MaterialMenuBar showMenuBar = new MaterialMenuBar();
        showMenuBar.setClass("dropdown-kit--arrow--below");

        MaterialLink showLink = new MaterialLink();
        showLink.addStyleName("btn btn--white btn--icon");

        Icon ieIcon = new Icon();//import/export icon for listing top panel
        ieIcon.setClass("ficon--download-cloud");
        showLink.add(ieIcon);

        MaterialDropDown showMenuContainer = new MaterialDropDown(showLink);
        showMenuContainer.setClass("dropdown-content--2 dropdown-content--export");
        showMenuContainer.setBelowOrigin(true);
        showLink.add(showMenuContainer);

        pdfVersion = getPdfVersion();
        pdfVersion.ensureDebugId("pdf_button");

        Div wrapper = new Div("java-wrap");
        showMenuContainer.add(wrapper);

        MaterialLink pdfVersion = getPdfVersion();
        wrapper.add(pdfVersion);

        MaterialDropDown mdp = new MaterialDropDown(pdfVersion);
        mdp.setHover(true);
        mdp.setHoverable(true);

        mdp.add(NewVatReturnReport.this::getPortraitLink);
        mdp.add(NewVatReturnReport.this::getLandscapeLink);

        wrapper.add(mdp);

        setPDFListener();

        showMenuBar.add(showLink);

        Div div = new Div();
        new KpiToolTip(showMenuBar, wfmStrings.export(), Position.TOP);
        div.add(showMenuBar);
        headerPanel.addGroupBoxItem(0, null, div);
    }

    public MaterialLink getPdfVersion() {

        if (pdfVersion == null) {
            pdfVersion = new MaterialLink();
            MaterialIcon pdfIcon = new MaterialIcon();
            pdfIcon.setStylePrimaryName("ficon--file-pdf hasicon--left");
            pdfVersion.add(pdfIcon);
            pdfVersion.setText(wfmStrings.pdf());
        }
        return pdfVersion;
    }

    private MaterialLink getPortraitLink() {
        if (portrait == null) {
            portrait = new MaterialLink();
            portrait.setText(wfmStrings.portrait());
        }
        return portrait;
    }

    private MaterialLink getLandscapeLink() {
        if (landscape == null) {
            landscape = new MaterialLink();
            landscape.setText(wfmStrings.landscape());
        }
        return landscape;
    }

    public void setPDFListener() {
        getPortraitLink().addClickHandler((event) -> {
            sendPdfRequest(false);
        });
        getLandscapeLink().addClickHandler((event) -> {
            sendPdfRequest(true);
        });
    }

    private void sendPdfRequest(boolean landscape) {
        String pdfURL = CommandConstants.PDF_URL + "/vatReturnPDFHandler";
        ListingFilterParameter filter = new ListingFilterParameter();
        filter.setLandscape(landscape);

        filter.setStartDateNC(Utils.getStartDateNCForFilter(fromValue.getDate()));
        if (toValue.getDate() != null) {
            filter.setEndDateNC(Utils.getEndDateNCForFilter(toValue.getDate()));
        }
        filter.setMessageStatus(String.valueOf(flatPercent));
        HashMap<String, String> parametrs = filter.getRequestParams();
        Utils.sendPDFOrExcelRequest(topPanel, pdfURL, parametrs, "_blank");
    }

}