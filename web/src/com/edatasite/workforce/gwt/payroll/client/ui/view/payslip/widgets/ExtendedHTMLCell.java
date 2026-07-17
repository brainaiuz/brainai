package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.user.client.ui.HTML;

import java.math.BigDecimal;

/**
 * User: Satimov Murad
 * Date: 2/5/18 8:28 PM
 */
public class ExtendedHTMLCell extends HTML implements CustomCellInterface {
    private Integer objectId;
    private BigDecimal amount;

    public ExtendedHTMLCell() {
        super();
    }

    public ExtendedHTMLCell(String html) {
        super(html);
    }

    @Override
    public String getDisplayValue() {
        return getHTML();
    }

    @Override
    public void setItemValue(Object value) {
    }

    @Override
    public void setItemFocus(boolean focused) {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
