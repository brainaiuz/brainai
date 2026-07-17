package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/20/12
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedTextBox extends TextBox {

    public String fieldCode;

    public ExtendedTextBox(String code) {
        super();
        this.fieldCode = code;
    }

    public BigDecimal getBigDecimalValue() {
        return AccountingUtils.get().parseToBigDecimal(super.getText());
    }

    public String getFieldCode() {
        return fieldCode;
    }
}
