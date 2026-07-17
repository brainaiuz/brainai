package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uea.UAEVatReturnAdjustmentModal;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Paragraph;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class GccVatReturnReport extends Composite {
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final WfmStrings wfmStirngs = WfmStrings.App.get();

    interface Template extends SafeHtmlTemplates {

        @Template("{0} <span>{1}</span>")
        SafeHtml describeBox(String title, String description);

        @Template("{0} <span class=\"asbutton2\">{1}</span><span>{2}</span>")
        SafeHtml describeNetBox(String title, String formula, String description);

        @Template("<span class=\"asbutton\">{0}</span> {1}")
        SafeHtml totalCell(String code, String value);
    }

    protected static Template template = GWT.create(Template.class);
    private static final GccVatReturnReportUiBinder ourUiBinder = GWT.create(GccVatReturnReportUiBinder.class);
    protected VatReturnServiceAsync vatReturnService = VatReturnService.App.get();

    @UiField
    HTMLPanel vatReturnPanel;
    @UiField
    Element salesThead;
    @UiField
    Element salesTbody;
    @UiField
    TableSectionElement expenseThead;
    @UiField
    TableSectionElement expenseTbody;
    @UiField
    TableSectionElement netTbody;

    @UiField
    Paragraph periodParagraph;
    @UiField
    MaterialLink markAsFile;
    @UiField
    Paragraph periodAsReportTitle;
    @UiField
    HeadingElement companyName;
    @UiField
    DivElement statusContainer;
    @UiField
    ParagraphElement statusText;

    protected Integer vatReturnId;
    private final View view;

    public GccVatReturnReport(Integer vatReturnId, View view) {
        this.vatReturnId = vatReturnId;
        this.view = view;

        HTMLPanel panel = ourUiBinder.createAndBindUi(this);
        initWidget(panel);

        //footer panel initializer
        {
            panel.add(new ViewFooter(new IFooteredView() {
                @Override
                public List<Widget> getFooterLeftSideWidgets() {
                    return null;
                }

                @Override
                public List<Widget> getFooterRightSideWidgets() {
                    List<Widget> widgets = new ArrayList<>();
                    WfmButton2 pdfButton = new WfmButton2(wfmStirngs.pdfVersion(), Constants.BTN_DEFAULT_OUTLINE);
                    pdfButton.addClickHandler(clickEvent -> generatePdfOrExcel(false));
                    widgets.add(pdfButton);

                    if (GccVatReturnReport.this instanceof UaeVatReturnReport) {
                        WfmButton2 excelButton = new WfmButton2(accountingStrings.printExcel(), Constants.BTN_DEFAULT_OUTLINE);
                        excelButton.addClickHandler(clickEvent -> generatePdfOrExcel(true));
                        widgets.add(excelButton);
                    }
                    return widgets;
                }
            }));
        }

        initialize();
    }

    private void initialize() {
        vatReturnService.getVatReturn(vatReturnId, new AsyncCallback<VatReturnItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(VatReturnItem vatReturnItem) {
                periodParagraph.setText(DateUtils.format(vatReturnItem.getFromDate().getNonConvertedDate()) + " To " + DateUtils.format(vatReturnItem.getToDate().getNonConvertedDate()));
                companyName.setInnerHTML(Utils.getCompanyName());
                periodAsReportTitle.setText("From " + DateUtils.format(vatReturnItem.getFromDate().getNonConvertedDate()) + " To " + DateUtils.format(vatReturnItem.getToDate().getNonConvertedDate()));
                statusText.setInnerHTML(vatReturnItem.getStatus().getName());

                boolean filed = AccountingConstants.VAT_RETURN_STATUS.FILED.equalsIgnoreCase(vatReturnItem.getStatus().getCode());
                markAsFile.setText(filed ? accountingStrings.markAsUnfiled() : accountingStrings.markAsFiled());
                markAsFile.addClickHandler(ch -> {
                    ch.preventDefault();

                    if (!filed && DateUtil.resetTime(new Date()).compareTo(vatReturnItem.getToDate().getNonConvertedDate()) <= 0) {
                        Info.show("Tax Return cannot be filed before the end of reporting period.", Info.Type.WARNING);
                        return;
                    }
                    new FileVatReturnModal(vatReturnItem, () -> {
                        view.closeTab();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VAT_RETURN_FILE_CHANGED, null, view);
                    });
                });

                if (filed) {
                    statusContainer.addClassName("green");
                    vatReturnService.hasUnfiledVatReturn(new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(Boolean hasUnfiled) {

                            if (hasUnfiled.booleanValue()) {
                                markAsFile.setVisible(false);
                            }
                        }
                    });
                }
            }
        });
        generateVatReturn(vatReturnId);
    }

    protected abstract void generateVatReturn(Integer vatReturnId);

    protected void createItemRow(Element container, String boxNumber, String description, TaxAmountItem taxAmountItem, VatReturnBox box) {
        createItemRow(container, boxNumber, description, taxAmountItem, box, true);
    }

    protected void createItemRow(Element container, String boxNumber, String description, TaxAmountItem taxAmountItem, VatReturnBox box, boolean drawTaxAmount) {
        Element tr = DOM.createTR();
        container.appendChild(tr);

        //box number
        Element td = DOM.createTD();
        td.setInnerHTML(boxNumber);
        tr.appendChild(td);

        //description
        td = DOM.createTD();
        td.setInnerHTML(description);
        tr.appendChild(td);

        //Taxable Amount
        td = DOM.createTD();

        if (taxAmountItem != null) {
            td.appendChild(getValueAsLink(taxAmountItem.getTaxableAmount(), box));
        }
        tr.appendChild(td);

        //Adjustment
        td = DOM.createTD();

        if (drawTaxAmount && taxAmountItem != null) {
            td.setInnerHTML(getValueAsString(taxAmountItem.getAdjustment()));
        }
        tr.appendChild(td);

        //Tax amount
        td = DOM.createTD();

        if (drawTaxAmount && taxAmountItem != null) {
            td.setInnerHTML(getValueAsString(taxAmountItem.getTaxAmount()));

            //Adjust things
            if (VatReturnBox.ae_box_6.equals(box)) {
                MaterialLink link = new MaterialLink();
                link.setText(wfmStirngs.adjust());
                link.setClass("insidetext");

                DOM.sinkEvents(link.getElement().cast(), Event.ONCLICK);
                DOM.setEventListener(link.getElement().cast(), event -> {
                    new UAEVatReturnAdjustmentModal(vatReturnId, new Command() {
                        @Override
                        public void execute() {
                            generateVatReturn(vatReturnId);
                        }
                    });
                });
                td.appendChild(link.getElement());
            }
        }
        tr.appendChild(td);
    }

    protected void createNetRow(Element container, String boxNumber, String description, BigDecimal value) {
        Element tr = DOM.createTR();
        container.appendChild(tr);

        //box number
        Element td = DOM.createTD();
        td.setInnerHTML(boxNumber);
        tr.appendChild(td);

        //description
        td = DOM.createTD();
        td.setInnerHTML(description);
        tr.appendChild(td);

        //Tax Amount
        td = DOM.createTD();
        td.setInnerHTML(getValueAsString(value));
        tr.appendChild(td);
    }

    protected Element getValueAsLink(BigDecimal value, VatReturnBox box) {
        MaterialLink link = new MaterialLink();
        link.setText(getValueAsString(value));
        link.setHref("#vattransaction|transaction/" + vatReturnId + "/" + box);
        return link.getElement();
    }

    protected String getValueAsString(BigDecimal value) {

        if (value == null) {
            return "";
        }
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return AccountingUtils.get().formatPrice(value);
        } else {
            return "(" + AccountingUtils.get().formatPrice(value.abs()) + ")";
        }
    }

    interface GccVatReturnReportUiBinder extends UiBinder<HTMLPanel, GccVatReturnReport> {
    }

    protected abstract void generatePdfOrExcel(boolean isExcel);

    @Override
    protected void onAttach() {
        super.onAttach();
        RootPanel.get().addStyleName("has-frame__info");
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        RootPanel.get().removeStyleName("has-frame__info");
    }
}
