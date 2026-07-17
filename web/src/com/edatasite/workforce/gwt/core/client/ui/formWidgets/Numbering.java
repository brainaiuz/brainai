package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import gwt.material.design.client.ui.MaterialPanel;

/**
 * User: Normurod Buriev
 * Date: 8/10/11
 * Time: 5:32 PM
 */
public class Numbering extends Composite implements Constants {

    private TextBox txtPrefix;
    private TextBox lastTxt;
    private TextBox txtNumber;

    private MaterialPanel pnlWrap;

    private NumberData numberData;

    private NumberFormat numberFormat;


    private boolean hidePrefixBox = false;
    private final boolean isIntegerNumber = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER);


    public Numbering(boolean hidePrefixBox) {
        this.hidePrefixBox = hidePrefixBox;
        initialize();
    }

    public Numbering() {
        initialize();
    }

    protected void initialize() {
        numberData = new NumberData();

        txtPrefix = new TextBox(true);
        txtPrefix.ensureDebugId("left-textBox");
        txtPrefix.setAlignment(TextBoxBase.TextAlignment.LEFT);
        txtPrefix.setWidth("122px");

        lastTxt = new TextBox(true);
        lastTxt.ensureDebugId("right-textBox");
        lastTxt.setAlignment(TextBoxBase.TextAlignment.LEFT);

        txtNumber = new TextBox(true);

        if (!Utils.isArabicLanguage()) {
            Validation.addNumericKeyboardListener(txtNumber);
        }

        initWidget(new InputGroup(txtPrefix, txtNumber, lastTxt));
    }

    public boolean validate(Boolean... txtNumberIsRequired) {
        int errors = 0;

        boolean isRequiredTxtNumber = true;
        if (txtNumberIsRequired != null && txtNumberIsRequired.length > 0 && txtNumberIsRequired[0] != null) {
            isRequiredTxtNumber = txtNumberIsRequired[0];
        }
        if (isRequiredTxtNumber) {
            if (txtNumber!=null && txtNumber.isVisible() && !Validation.validateTextBoxRequired(txtNumber)) {
                errors++;
            }
        }
        if (txtNumber!=null && !Utils.isArabicLanguage() && txtNumber.isVisible() && !Validation.validateNumber(txtNumber, null)) {
            errors++;
        }

        return errors == 0;
    }

    public NumberData getNumberData(Boolean noFormat) {
        String nbFirstString = txtPrefix.getValue();
        String nbr = txtNumber.getValue();
        String nbLastString = lastTxt.getValue();
        String strNbFirst = (nbFirstString != null && !nbFirstString.isEmpty()) ? txtPrefix.getValue() : "";
        String strNbr = (nbr != null && !nbr.isEmpty()) ? numberFormat.format(numberFormat.parse(txtNumber.getValue())) : "";
        if (noFormat) {
            strNbr = (nbr != null && !nbr.isEmpty()) ? txtNumber.getValue() : "";
        }
        String strNbrLast = (nbLastString != null && !nbLastString.isEmpty()) ? lastTxt.getValue() : "";

        if (isIntegerNumber) {
            numberData.setNumberString(nbr);
        } else {
            numberData.setNumberString(strNbFirst + strNbr + strNbrLast);
        }
        numberData.setSavedNumberFormula(("".equals(strNbFirst) ? "null" : strNbFirst) + SAV_NUM_DEL + ("".equals(strNbr) ? "null" : strNbr) + SAV_NUM_DEL + ("".equals(strNbrLast) ? "null" : strNbrLast));

        if (nbr != null && !nbr.isEmpty()) {
            numberData.setIntNumber((int) numberFormat.parse(txtNumber.getValue()));
        } else {
            numberData.setIntNumber(null);
        }

        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
        if (numberData != null) {
            setValue();
        }
    }

    public void setEnabled(boolean enabled) {
        txtPrefix.setEnabled(enabled);
        txtNumber.setEnabled(enabled);
        lastTxt.setEnabled(enabled);
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    private void setValue() {
        initializeNumberFormat(numberData.getNumberFormat());
        String numbering = "";
        String prefix = "";
        String lastString = "";

        if (hidePrefixBox || isIntegerNumber) {
            txtPrefix.removeFromParent();
        }

        if (numberData.getIntNumber() != null) {
            numbering = numberFormat.format(numberData.getIntNumber().doubleValue());

            if (isIntegerNumber && numberData.getNumberString() != null && !numberData.getNumberString().isEmpty()) {
                String textValue = numberData.getNumberString().replaceAll("\\D", "");
                txtNumber.setValue(textValue);
            } else {
                txtNumber.setValue(numbering);
            }
        }
        if (numberData.getFirstNumberString() != null && !"".equals(numberData.getFirstNumberString())) {
            txtPrefix.setValue(!isIntegerNumber ? numberData.getFirstNumberString() : "");
        } else if (numberData.getNumberString() != null && !"".equals(numberData.getNumberString())) {
            prefix = getPrefixValue(numbering);
            txtPrefix.setValue(prefix);
        }

        if (numberData.getLastNumberString() != null && !"".equals(numberData.getLastNumberString())) {
            lastString = numberData.getLastNumberString();
            lastTxt.setValue(lastString);
        } else {
            lastTxt.setVisible(hidePrefixBox);
        }
    }

    public String getPrefixValue(String numbering) {
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

    public TextBox getLastTxt() {
        return lastTxt;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }
}