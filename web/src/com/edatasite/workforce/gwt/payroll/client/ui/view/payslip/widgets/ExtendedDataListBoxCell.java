package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.ui.PayrollContants;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * User: satimov
 * Date: 2/5/18 3:35 PM
 */
public class ExtendedDataListBoxCell extends DataListBox implements CustomCellInterface, Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private KpiModal notePopup;
    private TextArea rejectionNote;
    private WfmButton2 saveButton;
    private WfmButton2 closeButton;
    private boolean approved;

    public ExtendedDataListBoxCell() {
        super();
        this.init();
    }

    private void init() {
        this.setWithoutNullLabel(false);
        this.setNullLabel(wfmStrings.pending());
        this.addListItem(new SelectItem(PayrollContants.LIST_APPROVED_ID, wfmStrings.approved()));
        this.addListItem(new SelectItem(PayrollContants.LIST_REJECTED_ID, wfmStrings.rejected()));

        final VerticalPanel vp = new VerticalPanel();
        final HorizontalPanel hp = new HorizontalPanel();

        this.notePopup = new KpiModal();
        this.notePopup.setWidth("300px");
        this.notePopup.setTitle(wfmStrings.rejectionReason());
        this.rejectionNote = new TextArea();
        this.rejectionNote.setHeight("100px");
        this.rejectionNote.setWidth("300px");

        this.saveButton = new WfmButton2(wfmStrings.save());
        this.saveButton.addClickHandler(event -> notePopup.close());

        this.closeButton = new WfmButton2(wfmStrings.close());
        this.closeButton.addClickHandler(event -> notePopup.close());
        hp.setSpacing(20);
        hp.add(saveButton);
        hp.add(closeButton);
        vp.add(rejectionNote);
        vp.add(hp);
        this.notePopup.add(vp);
    }

    private void showPopup() {
        notePopup.open();
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getRejectionNote() {
        return rejectionNote.getText();
    }

    @Override
    public String getDisplayValue() {
        return getSelectedItem() != null ? getSelectedItem().getName() : getNullLabel();
    }

    @Override
    public void setItemValue(Object value) {
        setSelected((Integer) value);
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }

}