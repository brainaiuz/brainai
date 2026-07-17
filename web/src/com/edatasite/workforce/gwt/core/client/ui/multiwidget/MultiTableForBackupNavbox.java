package com.edatasite.workforce.gwt.core.client.ui.multiwidget;

import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;

public class MultiTableForBackupNavbox extends MultiTableNewUI {
    public Command saveButtonListener;
    public MultiTableRow row;
    public boolean isRemovableRow = true;
    public Command minusButtonClick;
    private boolean isFromBackupForm = true;


    public MultiTableForBackupNavbox(int i, boolean canRemoveSingleRow, MultiTableWidgets multiTableWidgets, SimpleLink... extraLinks) {
        super(i, canRemoveSingleRow, multiTableWidgets, extraLinks);
    }

    public MultiTableForBackupNavbox(MultiTableWidgets multiTableWidgets, boolean canRemoveSingleRow, Object multiTableWidgets1) {
        super(multiTableWidgets);
    }

    @Override
    public void removeAllRows() {
        super.removeAllRows();
    }

    @Override
    protected void addMinusButton(MultiTableRow tableRow) {
        if (isFromBackupForm) {
            WfmButton2 button = new WfmButton2("", WfmButton2.BTN_WHITE);
            button.addStyleName("btn--icon");
            button.add(new SvgIcon(SvgEnum.minus));
            button.addClickHandler(event ->
            {
                if (minusButtonClick != null) {
                    setRow(tableRow);
                    minusButtonClick.execute();
                }
                if (isRemovableRow) {
                    removeFromTableRow(tableRow);
                }
            });
            new KpiToolTip(button, "remove");
            button.setVisible(false);
            tableRow.addToRight(button);
        }
    }

    public void isVisibleMinusAndPlus(boolean val) {
        isFromBackupForm = val;
    }

    @Override
    protected void addPlusButton(MultiTableRow tableRow) {
        if (isFromBackupForm) {
            super.addPlusButton(tableRow);
        }
    }


    @Override
    public void setSaveButtonListener(Command saveButtonListener) {
        this.saveButtonListener = saveButtonListener;
    }

    public void setRow(MultiTableRow row) {
        this.row = row;
    }

    public void setMinusButtonClick(Command minusButtonClick) {
        this.minusButtonClick = minusButtonClick;
    }
}
