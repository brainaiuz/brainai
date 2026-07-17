package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.rpc.itemserials.ItemSerialService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialDetailItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Label;

public class ItemSerialView extends KpiModal {
    private Integer id;
    private String serial;
    private MaterialPanel panel;

    public ItemSerialView(Integer id, String serial) {
        this.id = id;
        this.serial = serial;
        initialize();
    }

    private void initialize() {
        setWidth(600);
        setTitle("Serial Number - " + serial);

        panel = new MaterialPanel();
        getContent().add(panel);
        getData();

        addButton(new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, e -> close()));
    }

    private void getData() {
        ItemSerialService.App.get().getSerial(id, new AsyncCallback<SerialItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn(throwable.getMessage());
            }

            @Override
            public void onSuccess(SerialItem result) {
                for (SerialDetailItem item : result.getItems()) {
                    String date = DateTimeFormat.getFormat(Utils.getShortDateFormat()).format(item.getTransactionDate().getDate());
                    HTML html = new HTML(item.getEntityType() + " #<a href='javascript:;'>" + item.getTransactionNumber() + "</a>");
                    html.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(item.getTransactionLink(), item.getTransactionNumber()));

                    GRow row = new GRow();
                    FormGroup formGroup1 = new FormGroup(new Label(item.getTransactionType()));
                    formGroup1.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);

                    FormGroup formGroup2 = new FormGroup(date, html);
                    row.add(new GColumn(GColumnEnum.COL_4, formGroup1));
                    row.add(new GColumn(GColumnEnum.COL_8, formGroup2));
                    panel.add(row);
                }
            }
        });
    }
}
