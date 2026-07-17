package com.edatasite.workforce.gwt.invoice.client.rpc;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jun 4, 2009
 * Time: 5:40:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceNumberData implements Serializable {

    private String prefix = "";
    private boolean withDate;
    private boolean withClient;
    private boolean withProject;
    private String fourDigitNumber;

    private String date = "";
    private String clientCode = "";
    private String projectCode = "";

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

    public boolean isWithClient() {
        return withClient;
    }

    public void setWithClient(boolean withClient) {
        this.withClient = withClient;
    }

    public boolean isWithProject() {
        return withProject;
    }

    public void setWithProject(boolean withProject) {
        this.withProject = withProject;
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

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getInvoiceNumber() {
        return (prefix != null ? prefix : "") + date + (clientCode != null ? clientCode : "") + (projectCode != null ? projectCode : "") + fourDigitNumber;
    }

    private String getTemplate() {
        return (prefix != null ? prefix : "") + date + (clientCode != null ? clientCode : "") + (projectCode != null ? projectCode : "");
    }

    public Integer parseFourDigitNumber(String numberText) {
        String template = getTemplate();
        numberText = numberText.trim();
        if (numberText.startsWith(template)) {
            try {
                return Integer.parseInt(numberText.substring(template.length()));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
