package com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.TableBody;
import gwt.material.design.client.ui.html.TableDataCell;
import gwt.material.design.client.ui.html.TableHead;
import gwt.material.design.client.ui.html.TableHeadCell;
import gwt.material.design.client.ui.html.TableRow;

import java.math.BigDecimal;

public class RentalOrderReceiptTable extends Composite {
    private static TotalTableUiBinder ourUiBinder = GWT.create(TotalTableUiBinder.class);
    @UiField
    TableHead subTotalBody;
    @UiField
    TableRow defaultDiscountRow;
    @UiField
    TableBody itemsTotalBody;
    @UiField
    TableHead grossTotalBody;

    public RentalOrderReceiptTable() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setSubtotalItem(Widget label, Widget value) {
        subTotalBody.clear();
        subTotalBody.add(generateRow(label, value));
    }

    public void setDiscountItem(Widget label, Widget value) {
        defaultDiscountRow.clear();

        TableHeadCell tdLabel = new TableHeadCell();
        tdLabel.add(label);

        TableDataCell tdValue = new TableDataCell();
        tdValue.add(value);

        defaultDiscountRow.add(tdLabel);
        defaultDiscountRow.add(tdValue);
    }

    public void addItem(Widget label, Widget value) {
        itemsTotalBody.add(generateRow(label, value));
    }

    public void addGrossItem(Widget label, Widget value) {
        grossTotalBody.add(generateRow(label, value));
    }

    public void clear() {
        subTotalBody.clear();
        itemsTotalBody.clear();
        grossTotalBody.clear();
    }

    public TableRow generateRow(Widget label, Widget value) {
        TableRow tr = new TableRow();

        TableHeadCell tdLabel = new TableHeadCell();
        tdLabel.add(label);

        TableDataCell tdValue = new TableDataCell();
        tdValue.add(value);

        tr.add(tdLabel);
        tr.add(tdValue);

        return tr;
    }

    interface TotalTableUiBinder extends UiBinder<HTMLPanel, RentalOrderReceiptTable> {
    }

    public class OverallDiscount {
        /**
         * these fields used for only FIXED AMOUNT type of discount
         */
        /**
         * discount currency
         */
        private Integer currencyId;
        /**
         * discount exchange rate
         */
        private BigDecimal exchangeRate = BigDecimal.ONE;
        /**
         * amount in discount currency
         */
        private BigDecimal amountInCurrency = BigDecimal.ZERO;

        /**
         * Overall discount listener
         */
        private Command listener;


        public BigDecimal getAmountInCurrency() {
            return amountInCurrency;
        }

        public void setListener(Command listener) {
            this.listener = listener;
        }

        public Integer getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(Integer currencyId) {
            this.currencyId = currencyId;
        }

        public BigDecimal getExchangeRate() {
            return exchangeRate;
        }

        public void setExchangeRate(BigDecimal exchangeRate) {
            this.exchangeRate = exchangeRate;
        }
    }
}
