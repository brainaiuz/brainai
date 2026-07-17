package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/17/15
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollPdfPanel extends VerticalPanel {

    private SinglePayrunItem singlePayrunItem;
    private AdditionalPayment additionalPayment;
    private DataListBox dataListBox;

    public PayrollPdfPanel() {

    }

    public PayrollPdfPanel(SinglePayrunItem singlePayrunItem) {
        this.singlePayrunItem = singlePayrunItem;
        init();
    }

    public PayrollPdfPanel(AdditionalPayment additionalPayment) {
        this.additionalPayment = additionalPayment;
        init();
    }

    public void setPdfPanelData(SinglePayrunItem singlePayrunItem) {
        this.singlePayrunItem = singlePayrunItem;
        init();
    }

    public void setAdditionalPayment(AdditionalPayment additionalPayment) {
        this.additionalPayment = additionalPayment;
        init();
    }

    private void init() {
        FlowPanel flowPanel = new FlowPanel();
        dataListBox = new DataListBox();
        if (singlePayrunItem != null) {
            dataListBox.setItems(singlePayrunItem.getPdfTemplateList().getItems());
            if (singlePayrunItem.getPdfTemplateList().getDefaultTemplateID() != null && singlePayrunItem.getPdfTemplateID() == null) {
                dataListBox.setSelected(singlePayrunItem.getPdfTemplateList().getDefaultTemplateID());
            } else if (singlePayrunItem.getPdfTemplateID() != null) {
                dataListBox.setSelected(singlePayrunItem.getPdfTemplateID());
            }
        } else if (additionalPayment != null) {
            dataListBox.setItems(additionalPayment.getPdfTemplateList().getItems());
            if (additionalPayment.getPdfTemplateList().getDefaultTemplateID() != null && additionalPayment.getPdfTemplateID() == null) {
                dataListBox.setSelected(additionalPayment.getPdfTemplateList().getDefaultTemplateID());
            } else if (additionalPayment.getPdfTemplateID() != null) {
                dataListBox.setSelected(additionalPayment.getPdfTemplateID());
            }
        }
        flowPanel.add(dataListBox);
        add(flowPanel);
    }

    public Integer getSelectedTemplateID() {
        return dataListBox != null ? dataListBox.getSelectedId() : null;
    }
}
