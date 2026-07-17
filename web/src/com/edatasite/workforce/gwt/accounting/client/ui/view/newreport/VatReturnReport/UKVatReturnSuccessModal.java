/*
 * Copyright (c) 2023.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport;

import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Label;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

public class UKVatReturnSuccessModal extends KpiModal {
    private final Command closeTabCommand;

    public UKVatReturnSuccessModal(VatReturnItem vatReturnItem, Command closeTabCommand) {
        this.closeTabCommand = closeTabCommand;
        setWidth(500);
        Div step = new Div("progress-panel__step progress-panel__step--success");

        Div stepCircle = new Div("progress-panel__step-circle");
        Icon stepIcon = new Icon();
        stepIcon.getElement().setClassName("progress-panel__step-icon progress-panel__step-icon--success ficon--check-thin");
        stepCircle.add(stepIcon);

        Div title = new Div();
        title.getElement().setInnerText(wfmStrings.vatReturnHasBeenFiled());

        step.add(stepCircle);
        step.add(title);
        modalHeader.add(step);
        add(getRow("VAT Period", DateUtils.format(vatReturnItem.getFromDate()) + " - " + DateUtils.format(vatReturnItem.getToDate())));
        add(getRow("Filed On", DateUtils.format(vatReturnItem.getFiledOn())));
        addButton(new WfmButton2(wfmStrings.close(), clickEvent -> close()));
        open();
    }

    private GRow getRow(String title, String value) {
        GRow row = new GRow();
        row.setPadding(5);

        GColumn titleCol = new GColumn(GColumnEnum.COL_4);
        titleCol.setTextAlign(TextAlign.LEFT);

        Label titleLabel = new Label(title);
        titleLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        titleLabel.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        titleCol.add(titleLabel);

        GColumn valueCol = new GColumn(GColumnEnum.COL_8);

        Label valueLabel = new Label(value);
        valueLabel.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        valueCol.add(valueLabel);

        row.add(titleCol);
        row.add(valueCol);
        return row;
    }

    @Override
    public void close() {
        super.close();
        closeTabCommand.execute();
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VAT_RETURN_FILE_CHANGED, null, this);
    }
}
