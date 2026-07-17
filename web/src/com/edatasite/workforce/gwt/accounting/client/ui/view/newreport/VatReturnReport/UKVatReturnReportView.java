package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.UKVatReturn;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Paragraph;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UKVatReturnReportView extends Composite {

    interface UKVatReturnReportViewUiBinder extends UiBinder<HTMLPanel, UKVatReturnReportView> {
    }

    private static UKVatReturnReportView.UKVatReturnReportViewUiBinder uiBinder = GWT.create(UKVatReturnReportView.UKVatReturnReportViewUiBinder.class);
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected VatReturnServiceAsync vatReturnService = VatReturnService.App.get();

    @UiField
    Paragraph periodParagraph;
    @UiField
    HeadingElement companyName;
    @UiField
    Paragraph periodAsReportTitle;
    @UiField
    ParagraphElement statusText;
    @UiField
    MaterialLink fileButton;
    @UiField
    DivElement statusContainer;
    @UiField
    ParagraphElement taxBasis;
    @UiField
    HTMLPanel tableContainer;

    private Integer vatReturnId;
    private final View view;

    public UKVatReturnReportView(Integer vatReturnId, View view) {
        this.vatReturnId = vatReturnId;
        HTMLPanel panel = uiBinder.createAndBindUi(this);
        initWidget(panel);
        addFooter(panel);
        initialize();
        loadData();
        this.view = view;
    }

    private void initialize() {
        fileButton.setVisible(false);
        fileButton.setText(accountingStrings.submitToHMRC());
        taxBasis.setInnerHTML(Utils.isVATCashBased() ? wfmStrings.cash() : accountingStrings.accrual());
        companyName.setInnerHTML(Utils.getCompanyName());
    }

    private void loadData() {
        vatReturnService.getVatReturn(vatReturnId, new AsyncCallback<VatReturnItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(VatReturnItem vatReturnItem) {
                periodParagraph.setText(DateUtils.format(vatReturnItem.getFromDate().getNonConvertedDate()) + " To " + DateUtils.format(vatReturnItem.getToDate().getNonConvertedDate()));
                periodAsReportTitle.setText("From " + DateUtils.format(vatReturnItem.getFromDate().getNonConvertedDate()) + " To " + DateUtils.format(vatReturnItem.getToDate().getNonConvertedDate()));
                statusText.setInnerHTML(vatReturnItem.getStatus().getName());
                boolean filed = AccountingConstants.VAT_RETURN_STATUS.FILED.equalsIgnoreCase(vatReturnItem.getStatus().getCode());
                if (!filed) {
                    fileButton.setVisible(true);
                    fileButton.addClickHandler(click -> fileReport(vatReturnItem));
                } else {
                    statusContainer.addClassName("green");
                }
            }
        });
        vatReturnService.generateVatReturn(vatReturnId, new AsyncCallback<UKVatReturn>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(throwable.getMessage());
            }

            @Override
            public void onSuccess(UKVatReturn ukVatReturn) {
                for (Map.Entry<VatReturnBox, BigDecimal> entry : ukVatReturn.getValuesMap().entrySet()) {
                    setRowAmount(entry.getKey(), entry.getValue());
                }
            }
        });
    }

    private void setRowAmount(VatReturnBox rowId, BigDecimal amount) {
        Element amountCol = tableContainer.getElementById(rowId.name());
        if (VatReturnBox.BOX_3.equals(rowId) || VatReturnBox.BOX_5.equals(rowId)) {
            amountCol.setInnerHTML(AccountingUtils.get().formatPrice(amount));
        } else {
            Anchor amountAsLink = new Anchor(AccountingUtils.get().formatPrice(amount));
            amountAsLink.setHref("#vattransaction|transaction/" + vatReturnId + "/" + rowId);
            amountCol.appendChild(amountAsLink.getElement());
        }
    }

    private void fileReport(VatReturnItem vatReturnItem) {
        LoadingPanel.loading(true);
        new UKFileVatReturnModal(() -> VatReturnService.App.get().fileUkVatReturn(vatReturnItem.getObjectID(), Utils.getFraudPreventionData(), new AsyncCallback<VatReturnItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(throwable.getMessage());
            }

            @Override
            public void onSuccess(VatReturnItem item) {
                LoadingPanel.loading(false);
                new UKVatReturnSuccessModal(item, view::closeTab);
            }
        }));
    }


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

    private void addFooter(HTMLPanel panel) {
        panel.add(new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                List<Widget> widgets = new ArrayList<>();
                WfmButton2 pdfButton = new WfmButton2(wfmStrings.pdfVersion(), Constants.BTN_DEFAULT_OUTLINE);
                pdfButton.addClickHandler(clickEvent -> generatePdf(panel));
                widgets.add(pdfButton);

                WfmButton2 excelButton = new WfmButton2(accountingStrings.printExcel(), Constants.BTN_DEFAULT_OUTLINE);
                excelButton.addClickHandler(clickEvent -> generateExcel(panel));
                widgets.add(excelButton);
                return widgets;
            }
        }));
    }

    private void generatePdf(HTMLPanel panel) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(vatReturnId);
        String pdfURL = CommandConstants.PDF_URL + "/ukVatReturnPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, filterParameter.getRequestParams(), "_blank");
    }

    private void generateExcel(HTMLPanel panel) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(vatReturnId);
        String excelURL = CommandConstants.COMMON_URL + "/ukVatReturnExcelHandler";
        Utils.sendPDFOrExcelRequest(panel, excelURL, filterParameter.getRequestParams(), "_blank");
    }
}
