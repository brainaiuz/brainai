package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.*;

/**
 * User: Normurod Buriev
 * Date: 8/10/11
 * Time: 5:32 PM
 */
public class ProductNumbering extends Composite implements Constants {

    private TextBox txtPrefix;

    private TextBox txtNumber;

    private NumberData numberData;

    private NumberFormat numberFormat;

    private static final int wrapSpacing = 0;

    public ProductNumbering() {
        initialize();
    }

    protected void initialize() {
        numberData = new NumberData();

        txtPrefix = new TextBox(true);
        txtPrefix.ensureDebugId("left-textBox");
        txtPrefix.setAlignment(TextBoxBase.TextAlignment.RIGHT);

        txtNumber = new TextBox(true);
        txtNumber.getElement().getStyle().setMarginLeft(2, Style.Unit.PX);

        HorizontalPanel pnlWrap = new HorizontalPanel();
        pnlWrap.addStyleName("inFieldTable");
        pnlWrap.add(txtPrefix);
        pnlWrap.add(txtNumber);
        pnlWrap.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        pnlWrap.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        pnlWrap.setSpacing(wrapSpacing);

        initWidget(pnlWrap);
    }

    public boolean validate() {

        return true;
    }

    public NumberData getNumberData(Boolean noFormat) {
        String nbFirstString = txtPrefix.getValue();
        String nbr = txtNumber.getValue();
        String strNbFirst = (nbFirstString != null && !nbFirstString.isEmpty()) ? txtPrefix.getValue() : "";
        String strNbr = (nbr != null && !nbr.isEmpty()) ? txtNumber.getValue() : "";
        if (noFormat) {
            strNbr = (nbr != null && !nbr.isEmpty()) ? txtNumber.getValue() : "";
        }

        numberData.setNumberString(strNbFirst + strNbr);
        numberData.setSavedNumberFormula(("".equals(strNbFirst) ? "null" : strNbFirst) + SAV_NUM_DEL + ("".equals(strNbr) ? "null" : strNbr));

        if (nbr != null && !nbr.isEmpty() && txtNumber.getValue().matches("^\\d+")) {
            numberData.setIntNumber((int) numberFormat.parse(txtNumber.getValue()));
        } else {
            numberData.setIntNumber(null);
        }

        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
        setValue();
    }

    public void setEnabled(boolean enabled) {
        txtPrefix.setEnabled(enabled);
        txtNumber.setEnabled(enabled);
    }

    public void setWidth(int width) {
        width -= wrapSpacing;
        if (Utils.isChrome()) {
            width -= 2;
        }
        width -= 7;

        int prefixWidth = width / 3;
        int lasTextWidth = width / 3;
        int numberWidth = width - prefixWidth - lasTextWidth;
        if (Utils.isIE()) {
            prefixWidth -= 1;
            lasTextWidth -= 2;
            numberWidth -= 2;
        }
        txtPrefix.setWidth(prefixWidth + "px");

        txtNumber.setWidth(numberWidth + "px");
    }

    private void setValue() {
        initializeNumberFormat(numberData.getNumberFormat());
        String numbering = "";
        String prefix = "";
        String lastString = "";

        if (numberData.getIntNumber() != null) {
            numbering = numberFormat.format(numberData.getIntNumber().doubleValue());
            txtNumber.setValue(numbering);
        }
        if (numberData.getFirstNumberString() != null && !"".equals(numberData.getFirstNumberString())) {
            txtPrefix.setValue(numberData.getFirstNumberString());
        } else if (numberData.getNumberString() != null && !"".equals(numberData.getNumberString())) {
            prefix = getPrefixValue(numbering);
            txtPrefix.setValue(prefix);
        }


        if (numberData.getLastNumberString() != null && !"".equals(numberData.getLastNumberString())) {
            lastString = numberData.getLastNumberString();
        }

    }

    private String getPrefixValue(String numbering) {
        if (numbering == null || numbering.isEmpty()) {
            return numberData.getNumberString();
        }

        String numberField = String.valueOf((int) numberFormat.parse(numbering));
        if (numberData.getNumberString().lastIndexOf(numbering) != -1) {
            int endIndex = numberData.getNumberString().lastIndexOf(numbering);
            return numberData.getNumberString().substring(0, endIndex);
        } else if (numberData.getNumberString().lastIndexOf(numberField) != -1) {
            int numEndIndex = numberData.getNumberString().lastIndexOf(numberField) + numberField.length();
            int prefEndIndex = numEndIndex - numberFormat.format(numberFormat.parse(numbering)).length();
            return prefEndIndex != -1 ? numberData.getNumberString().substring(0, prefEndIndex) : numberData.getNumberString();
        } else {
            return numberData.getNumberString();
        }
    }

    private void initializeNumberFormat(String objectNumberFormat) {
        String numbering = "";
        if (objectNumberFormat.contains(WIDGET_NUMBERS)) {
            String[] firstString = objectNumberFormat.split("/");
            for (String value : firstString) {
                String[] split = value.split(":");
                if (split[0].equals(WIDGET_NUMBERS)) {
                    numbering = split[1];
                }
            }
        } else if (objectNumberFormat.contains(SAV_NUM_DEL)) {
            String[] firstString = objectNumberFormat.split(SAV_NUM_DEL);
            numbering = firstString[1];
        } else {
            int splitterIndex = objectNumberFormat.lastIndexOf("_");
            numbering = objectNumberFormat.substring(splitterIndex + 1);
        }
        if (numbering != null && numbering.length() > 0) {
            StringBuilder nf = new StringBuilder();
            for (int i = 0; i < numbering.length(); i++) {
                nf.append("0");
            }

            numberFormat = NumberFormat.getFormat(nf.toString());
        } else {
            numberFormat = NumberFormat.getFormat("0000");
        }
    }

    public TextBox getTxtNumber() {
        return txtNumber;
    }

    public TextBox getTxtPrefix() {
        return txtPrefix;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }
}