package com.edatasite.workforce.gwt.payroll.server.app;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Aug 15, 2009
 * Time: 5:38:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeEarning {
    private int yesIndex;
    private int noIndex;
    private char yesLetter;
    private char noLetter;

    public int getYesIndex() {
        return yesIndex;
    }

    public void setYesIndex(int yesIndex) {
        this.yesIndex = yesIndex;
    }

    public int getNoIndex() {
        return noIndex;
    }

    public void setNoIndex(int noIndex) {
        this.noIndex = noIndex;
    }

    public char getYesLetter() {
        return yesLetter;
    }

    public void setYesLetter(char yesLetter) {
        this.yesLetter = yesLetter;
    }

    public char getNoLetter() {
        return noLetter;
    }

    public void setNoLetter(char noLetter) {
        this.noLetter = noLetter;
    }
}
