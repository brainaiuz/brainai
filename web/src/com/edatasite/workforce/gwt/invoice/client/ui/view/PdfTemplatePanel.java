package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityResponse;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/27/13
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PdfTemplatePanel extends VerticalPanel {
    private WfmStrings wfmStrings = WfmStrings.App.get();

    private NewInvoice newInvoice;
    private NewManualTransaction newManualTransaction;
    private ReceivePaymentData paymentData;
    private DataListBox dataListBox;


    public PdfTemplatePanel(NewInvoice newInvoice) {
        this.newInvoice = newInvoice;
        init();
    }

    public PdfTemplatePanel(NewManualTransaction newManualTransaction) {
        this.newManualTransaction = newManualTransaction;
        init();
    }

    public PdfTemplatePanel(ReceivePaymentData paymentData) {
        this.paymentData = paymentData;
        init();
    }

    private void init() {
        FlowPanel flowPanel = new FlowPanel();
        dataListBox = new DataListBox();
        if (newInvoice != null) {
            dataListBox.setItems(newInvoice.getPdfTemplateList().getItems());
            if (newInvoice.getPdfTemplateList().getDefaultTemplateID() != null && newInvoice.getPdfTemplateID() == null) {
                dataListBox.setSelected(newInvoice.getPdfTemplateList().getDefaultTemplateID());
            } else if (newInvoice.getPdfTemplateID() != null) {
                dataListBox.setSelected(newInvoice.getPdfTemplateID());
            }
        } else if (newManualTransaction != null) {
            dataListBox.setItems(newManualTransaction.getPdfTemplateList().getItems());
            if (newManualTransaction.getPdfTemplateList().getDefaultTemplateID() != null && newManualTransaction.getPdfTemplateID() == null) {
                dataListBox.setSelected(newManualTransaction.getPdfTemplateList().getDefaultTemplateID());
            } else if (newManualTransaction.getPdfTemplateID() != null) {
                dataListBox.setSelected(newManualTransaction.getPdfTemplateID());
            }
        } else if (paymentData != null) {
            dataListBox.setItems(paymentData.getPdfTemplateList().getItems());
            if (paymentData.getPdfTemplateList().getDefaultTemplateID() != null && paymentData.getPdfTemplateID() == null) {
                dataListBox.setSelected(paymentData.getPdfTemplateList().getDefaultTemplateID());
            } else if (paymentData.getPdfTemplateID() != null) {
                dataListBox.setSelected(paymentData.getPdfTemplateID());
            }
        }

        dataListBox.addValueChangeHandler(event -> dataListBox.removeStyleName("x-form-invalid"));

        flowPanel.add(dataListBox);
        add(flowPanel);
    }

    /**
     * Validate against when list contains any data and if any of them is selected
     *
     * @return
     */
    public ValidityResponse validate() {
        ValidityResponse result = new ValidityResponse();
        if (dataListBox != null && dataListBox.getItems().length > 0) {
            if (!Validation.validateListBoxRequired(dataListBox)) {
                result.addErrorMessage(wfmStrings.pleaseSelectPdfTemplate());
            }
        }
        return result;
    }

    public Integer getSelectedTemplateID() {
        return dataListBox.getSelectedId();
    }

    public DataListBox getDataListBox() {
        return dataListBox;
    }
}
