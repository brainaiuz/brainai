package com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderService;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

import java.math.BigDecimal;

public class RentalOrderSideNavBox extends KpiSideNavBox {
    private Integer rowID;
    private RentalOrderItem item;
    private DateTimeWidget startTime;
    private DateTimeWidget endTime;
    private HTML duration;
    private HTML price;

    public RentalOrderSideNavBox(Integer rowID, RentalOrderItem item) {
        super(KpiSideNavBox.DEFAULT_WIDTH);
        this.rowID = rowID;
        this.item = item;
        initialize();
    }


    private void initialize() {
        Heading header = new Heading(HeadingSize.H4);
        header.setText("Rent a Product");
        addHeader(header);

        FlowPanel panel = new FlowPanel();

        FormGroup nameFormGroup = new FormGroup(wfmStrings.name(), new HTML(item.getRentalItem().getName()));
        panel.add(nameFormGroup);

        startTime = new DateTimeWidget(30);
        startTime.getTime().setPaddingLeft(6);
        startTime.getDateField().addValueChangeHandler(v -> calculateDuration());
        startTime.getTime().setChangeCommand(() -> calculateDuration());
        FormGroup fromFormGroup = new FormGroup(wfmStrings.from(), startTime);
        panel.add(fromFormGroup);

        endTime = new DateTimeWidget(30);
        endTime.getTime().setPaddingLeft(6);
        endTime.getDateField().addValueChangeHandler(v -> calculateDuration());
        endTime.getTime().setChangeCommand(() -> calculateDuration());
        FormGroup toFormGroup = new FormGroup(wfmStrings.to(), endTime);
        panel.add(toFormGroup);

        duration = new HTML("<b> 0 " + wfmStrings.hour() + "</b>");
        FormGroup durationFormGroup = new FormGroup(wfmStrings.duration(), duration);
        panel.add(durationFormGroup);

        price = new HTML(AccountingUtils.getZero());
        FormGroup priceFormGroup = new FormGroup(wfmStrings.price(), price);
        panel.add(priceFormGroup);

        addBody(panel);

        WfmButton2 addBtn = new WfmButton2(wfmStrings.add(), WfmButton2.BTN_PRIMARY);
        addBtn.addClickHandler(btnClick -> {
            addBtn.setEnabled(false);
            int errors = 0;
            if (!Validation.validateDateTime(startTime)) {
                errors++;
            }
            if (!Validation.validateDateTime(endTime)) {
                errors++;
            }

            if (errors > 0) {
                addBtn.setEnabled(true);
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                return;
            }

            RentalOrderPriceItem rentalOrderPriceItem = new RentalOrderPriceItem();
            rentalOrderPriceItem.setRowID(rowID);
            item.setDescription(DateUtils.formatInternalShort1(startTime.getDateTime()) + " -> " + " " + DateUtils.formatInternalShort1(endTime.getDateTime()));
            item.setQty(new BigDecimal(1));
            item.setFromDate(startTime.getDateTime());
            item.setToDate(endTime.getDateTime());
            item.setRentalDuration(duration.getText());

            rentalOrderPriceItem.setItem(item);
            remove();

            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_RENTAL_ORDER_CALCULATE_MIN_PRICE, rentalOrderPriceItem, RentalOrderSideNavBox.this);
        });

        addFooter(addBtn);
        show();
    }

    private void calculateDuration() {
        if (startTime.getDateTime() != null && endTime.getDateTime() != null) {
            if (endTime.getDateTime().after(startTime.getDateTime())) {
                long difference_In_Time = endTime.getDateTime().getTime() - startTime.getDateTime().getTime();
                long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
                long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
                long difference_In_Days = ((difference_In_Time / (1000 * 60 * 60 * 24)) % 365) % 7;
                long difference_In_Weeks = ((difference_In_Time / (1000 * 60 * 60 * 24)) % 365) / 7;
                String durationText = "<b>";
                if (difference_In_Weeks > 0) {
                    durationText += difference_In_Weeks == 1 ? difference_In_Weeks + " " + wfmStrings.week() : difference_In_Weeks + " " + wfmStrings.week();
                }
                if (difference_In_Days > 0) {
                    durationText += difference_In_Days == 1 ? " " + difference_In_Days + " " + wfmStrings.day() : " " + difference_In_Days + " " + wfmStrings.days();
                }
                if (difference_In_Hours > 0) {
                    durationText += difference_In_Hours == 1 ? " " + difference_In_Hours + " " + wfmStrings.hour() : " " + difference_In_Hours + " " + wfmStrings.hours();
                }
                if (difference_In_Minutes > 0) {
                    durationText += " " + difference_In_Minutes + " " + wfmStrings.minutes();
                }
                durationText += "</b>";
                duration.setHTML(durationText);

                calculateMinPrice();
            } else {
                Info.show("Start time should be after end time", Info.Type.WARNING);
            }
        }
    }

    private void calculateMinPrice() {
        RentalOrderService.App.get().calculateRentalMinPrice(item.getRentalItem().getId(), new DateNonConvertable(startTime.getDateTime()), new DateNonConvertable(endTime.getDateTime()), new AbstractAsyncCallback<BigDecimal>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BigDecimal result) {
                super.onSuccess(result);
                LoadingPanel.loading(false);
                item.setPrice(result);
                price.setHTML("<b>" + AccountingUtils.get().formatUnitPrice(result) + "</b>");
            }
        });
    }
}
