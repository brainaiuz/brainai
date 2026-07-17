package com.edatasite.workforce.gwt.core.client.ui.view;

import java.io.Serializable;

/**
 * User: Dilshod Madrahimov
 * Date: 6/11/2015
 * Time: 3:18:35 PM
 */
public class BankTransferNumberData implements Serializable {

    private String prefix = "";
    private boolean withDate;
    private String fourDigitNumber;
    private String date = "";
    private String numberString;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isWithDate() {
        return withDate;
    }

    public void setWithDate(boolean withDate) {
        this.withDate = withDate;
    }

    public String getFourDigitNumber() {
        return fourDigitNumber;
    }

    public void setFourDigitNumber(String fourDigitNumber) {
        this.fourDigitNumber = fourDigitNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getTransferNumber() {
        return (prefix != null ? prefix : "") + fourDigitNumber + (withDate ? "-" + date : "");
    }

    public String getTemplate() {
        return (prefix != null ? prefix : "") + (fourDigitNumber != null ? fourDigitNumber : "") + ((date != null && !"".equals(date)) ? "-" + date : "");
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }

    public Integer parseNumber(String numberText) {
        String _prefix = prefix != null ? prefix : "";
        numberText = numberText.trim();
        if (numberText.startsWith(_prefix)) {
            try {
                String _suffix = (date != null && !"".equals(date)) ? "-" + date : "";
                return Integer.parseInt(numberText.substring(_prefix.length(), numberText.length() - _suffix.length()));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
