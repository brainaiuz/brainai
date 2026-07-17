package com.edatasite.workforce.gwt.invoice.client.ui.view.components;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.TableBody;
import gwt.material.design.client.ui.html.TableDataCell;
import gwt.material.design.client.ui.html.TableFooter;
import gwt.material.design.client.ui.html.TableHead;
import gwt.material.design.client.ui.html.TableHeadCell;
import gwt.material.design.client.ui.html.TableRow;

import java.math.BigDecimal;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class ReceiptTable extends Composite {
    interface TotalTableUiBinder extends UiBinder<HTMLPanel, ReceiptTable> {
    }

    private static final TotalTableUiBinder ourUiBinder = GWT.create(TotalTableUiBinder.class);
    private static final Integer OVERALL_DISCOUNT_DEFAULT = -1;

    /**
     * Sub total container
     */
    @UiField
    TableHead subTotalBody;

    /**
     * Discount/Overall discount container
     */
    @UiField
    TableBody discountBody;
    @UiField
    Button discountSwitcher;
    @UiField
    TableRow defaultDiscountRow;
    @UiField
    HTML discountLabel;
    @UiField
    HTML discountValue;
    @UiField
    TableRow overallDiscountRow;
    @UiField(provided = true)
    DataListBox overallDiscountTypes;
    @UiField
    TextBox txtOverallDiscount;

    /**
     * Items total body contains the tax total, billable expense
     */
    @UiField
    TableBody itemsTotalBody;

    /**
     * Shipping total container
     * If there is a shipping cost then it will be appear
     */
    @UiField
    TableBody shippingBody;
    @UiField
    Div shippingMethodContainer;
    @UiField
    TableDataCell shippingMethodValueContainer;
    @UiField
    Div shippingAmountContainer;

    /**
     * Gross total container
     * total value in item currency
     * total value in base currency
     */
    @UiField
    TableHead grossTotalBody;

    /**
     * Payment body contains the payment of the item
     */
    @UiField
    TableBody paymentBody;

    /**
     * Due amount container of the item
     */
    @UiField
    TableFooter dueAmountBody;
    @UiField
    TableRow shippingTaxContainer;
    @UiField
    TableDataCell colspan2;

    private OverallDiscount overallDiscount;
    private ShippingMethodWidget shippingMethodWidget;

    private static final String BOX_WIDTH = "80px";

    public ReceiptTable() {
        this(false);
    }

    public ReceiptTable(boolean useOverallDiscount) {
        overallDiscountTypes = new DataListBox();

        initWidget(ourUiBinder.createAndBindUi(this));
        txtOverallDiscount.setEnabled(false);

        shippingBody.setVisible(false);
        paymentBody.setVisible(false);
        dueAmountBody.setVisible(false);

        colspan2.getElement().setAttribute("colspan", "2");

        //if you want to use the overall discount feature then it's for you
        if (useOverallDiscount) {
            overallDiscount = new OverallDiscount();
        } else {
            discountBody.removeFromParent();
        }
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

    public void addShippingTax(Widget label, Widget value) {
        shippingTaxContainer.clear();

        TableHeadCell shippingTaxLabel = new TableHeadCell();
        shippingTaxLabel.add(label);

        TableDataCell shippingTaxValue = new TableDataCell();
        shippingTaxValue.add(value);
        shippingTaxContainer.add(shippingTaxLabel);
        shippingTaxContainer.add(shippingTaxValue);
    }

    public void removeShippingBody() {
        shippingBody.removeFromParent();
    }

    public void addGrossItem(Widget label, Widget value) {
        grossTotalBody.add(generateRow(label, value));
    }

    public void addGrossItemWithStringValues(String label, String value) {
        HTML labelWidget = new HTML(label);
        HTML valueWidget = new HTML(value);

        labelWidget.getElement().getStyle().setTextAlign(Style.TextAlign.LEFT);
        labelWidget.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        labelWidget.getElement().getStyle().setFontSize(13.2, Style.Unit.PX);


        valueWidget.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        valueWidget.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        valueWidget.getElement().getStyle().setFontSize(13.2, Style.Unit.PX);

        addGrossItem(labelWidget, valueWidget);
    }

    public void addPaidItem(Widget label, Widget value) {
        paymentBody.setVisible(true);
        paymentBody.add(generateRow(label, value));
    }

    public void setDueAmount(Widget label, Widget value) {
        dueAmountBody.setVisible(true);
        dueAmountBody.clear();
        dueAmountBody.add(generateRow(label, value));
    }

    public void clear() {
        subTotalBody.clear();
        itemsTotalBody.clear();
        grossTotalBody.clear();
        paymentBody.clear();
        dueAmountBody.clear();

        shippingTaxContainer.clear();
    }

    public void clearTotalItems() {
        grossTotalBody.clear();
    }

    public OverallDiscount getOverallDiscount() {
        return overallDiscount;
    }

    /**
     * Set saved/existing overall discount values
     * @param value
     * @param type
     */
    public void setOverallDiscount(BigDecimal value, Integer type) {
        if (Constants.ONE_OFF_DISCOUNT.equals(type)) {
            overallDiscountTypes.setSelected(new SelectItem(type, WfmStrings.App.get().percentage()));
            txtOverallDiscount.setText(AccountingUtils.get().formatDiscount(value));
        } else if (Constants.ONE_OFF_FIXED_AMOUNT.equals(type)) {
            overallDiscountTypes.setSelected(new SelectItem(type, WfmStrings.App.get().fixedAmount()));
            txtOverallDiscount.setText(AccountingUtils.get().formatPrice(value));
            overallDiscount.setAmountInCurrency(value);
        }
        overallDiscount.onSwitchOnOffOverallDiscount();
    }

    public void initializeShipping(LookUp crmAccountLookUp, Widget shippingTotal) {
        shippingBody.setVisible(true);

        shippingMethodWidget = new ShippingMethodWidget((CrmAccountLookUp) crmAccountLookUp);
        shippingMethodContainer.add(shippingMethodWidget);
        shippingAmountContainer.add(shippingMethodWidget.getShippingAmountBox());
        shippingMethodWidget.getShippingAmountBox().setWidth(BOX_WIDTH);
        shippingMethodWidget.getShippingAmountBox().setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        shippingMethodWidget.addStyleName("form-lookup"); //https://i.imgur.com/ZGIzFpL.png
    }

    public ShippingMethodWidget getShippingMethodWidget() {
        return shippingMethodWidget;
    }

    private void enableOverallDiscountMode(boolean enable) {
        overallDiscountTypes.setEnabled(enable);
        txtOverallDiscount.setEnabled(enable && !OVERALL_DISCOUNT_DEFAULT.equals(overallDiscountTypes.getSelectedId()));
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

    public TableBody getItemsTotalBody() {
        return itemsTotalBody;
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

        /**
         * Currency listener
         */
        private Command currencyListener;

        OverallDiscount() {
            AccountingUtils utils = AccountingUtils.get();

            //Overall discount type initialize
            {
                overallDiscountTypes.setWithoutNullLabel(true);
                overallDiscountTypes.addListItem(new SelectItem(OVERALL_DISCOUNT_DEFAULT, AccountingStrings.App.get().overalDiscount()));
                overallDiscountTypes.addListItem(new SelectItem(Constants.ONE_OFF_DISCOUNT, WfmStrings.App.get().percentage()));
                overallDiscountTypes.addListItem(new SelectItem(Constants.ONE_OFF_FIXED_AMOUNT, WfmStrings.App.get().fixedAmount()));

                overallDiscountTypes.setSelected(new SelectItem(OVERALL_DISCOUNT_DEFAULT));
                enableOverallDiscountMode(false);

                overallDiscountTypes.addValueChangeHandler(ch -> {

                    if (overallDiscountTypes.getSelectedId() != null && overallDiscountTypes.getSelectedId() >= 0) {
                        txtOverallDiscount.setEnabled(true);

                        if (Constants.ONE_OFF_DISCOUNT.equals(overallDiscountTypes.getSelectedId())) {
                            txtOverallDiscount.setText(null);
                            txtOverallDiscount.getElement().setAttribute("placeholder", "0.00 %");
                        } else {
                            txtOverallDiscount.getElement().removeAttribute("placeholder");

                            if (currencyListener != null) {
                                currencyListener.execute();
                            }
                            amountInCurrency = utils.parseToBigDecimal(txtOverallDiscount.getText(), AccountingUtils.discountNumberFormat);
                        }
                    } else {
                        txtOverallDiscount.setEnabled(false);
                    }

                    if (listener != null) {
                        listener.execute();
                    }
                });
            }

            //overall discount box initialize
            {
                txtOverallDiscount.setWidth(BOX_WIDTH);
                Validation.addNumericKeyboardListener(txtOverallDiscount, AccountingUtils.calculationScale);
                txtOverallDiscount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                txtOverallDiscount.addKeyUpHandler(ku -> {
                    BigDecimal discountAmount = BigDecimal.ZERO;
                    try {
                        discountAmount = utils.parseToBigDecimal(txtOverallDiscount.getText(), AccountingUtils.discountNumberFormat);
                    } catch (NumberFormatException ex) {
                        txtOverallDiscount.setText(AccountingUtils.getDiscountZero());
                    } finally {

                        if (Constants.ONE_OFF_DISCOUNT.equals(overallDiscountTypes.getSelectedId()) && discountAmount.compareTo(AccountingConstants.HUNDRED) > 0) {
                            txtOverallDiscount.setText(txtOverallDiscount.getText().substring(0, AccountingUtils.getDiscountScale()));
                        }
                    }
                });
                txtOverallDiscount.addChangeHandler(event -> {

                    if (Constants.ONE_OFF_FIXED_AMOUNT.equals(overallDiscountTypes.getSelectedId()) && currencyListener != null) {
                        currencyListener.execute();
                        amountInCurrency = utils.parseToBigDecimal(txtOverallDiscount.getText(), AccountingUtils.discountNumberFormat);
                    }
                    if (listener != null) {
                        listener.execute();
                    }
                });
            }

            //overall discount switcher initialize
            {
                SvgIcon svgIcon = new SvgIcon(SvgEnum.repeat);
                discountSwitcher.getElement().appendChild(svgIcon.getElement());
                discountSwitcher.addClickHandler(ch -> {
                    onSwitchOnOffOverallDiscount();
                });
            }
        }

        public void setEnableDiscountSwitcher(boolean enableDiscountSwitcher) {
            discountSwitcher.setEnabled(enableDiscountSwitcher);
        }

        public void onSwitchOnOffOverallDiscount() {
            if (defaultDiscountRow.getStyleName().contains("active")) {
                $(defaultDiscountRow).insertAfter($(overallDiscountRow));
                defaultDiscountRow.removeStyleName("active");
                overallDiscountRow.addStyleName("active");
                enableOverallDiscountMode(true);
            } else {
                $(overallDiscountRow).insertAfter($(defaultDiscountRow));
                defaultDiscountRow.addStyleName("active");
                overallDiscountRow.removeStyleName("active");
                overallDiscountTypes.setSelected(OVERALL_DISCOUNT_DEFAULT);
                txtOverallDiscount.setText(null);
                txtOverallDiscount.getElement().removeAttribute("placeholder");
                amountInCurrency = BigDecimal.ZERO;
                enableOverallDiscountMode(false);
            }

            if (listener != null) {
                listener.execute();
            }
        }

        public boolean isEnabled() {
            return overallDiscountTypes.getSelectedId() != null && overallDiscountTypes.getSelectedId().intValue() >= 0;
        }

        /**
         * This method is using for calculation in multi currency
         * @return
         */
        public BigDecimal getAmountInCurrency() {
            return amountInCurrency;
        }

        public void setAmountInCurrency(BigDecimal amountInCurrency) {
            this.amountInCurrency = amountInCurrency;
        }

        public Integer getType() {
            return overallDiscountTypes.getSelectedId();
        }

        public BigDecimal getValue() {
            return AccountingUtils.parsePriceToBigDecimal(txtOverallDiscount.getText());
        }

        public TextBox getOverallDiscountBox() {
            return txtOverallDiscount;
        }

        public void setListener(Command listener) {
            this.listener = listener;
        }

        public void setCurrencyListener(Command currencyListener) {
            this.currencyListener = currencyListener;
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
