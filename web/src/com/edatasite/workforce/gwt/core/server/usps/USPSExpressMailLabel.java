package com.edatasite.workforce.gwt.core.server.usps;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/30/12
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class USPSExpressMailLabel {
    private String fromFirstName;
    private String fromLastName;
    private String fromFirm;
    private String fromAddress1;
    private String fromAddress2;
    private String fromCity;
    private String fromState;
    private String fromZip5;
    private String fromPhone;
    private String toFirstName;
    private String toLastName;
    private String toFirm;
    private String toAddress1;
    private String toAddress2;
    private String toCity;
    private String toState;
    private String toZip5;
    private String toPhone;
    private String weighInOunces;
    private String shipDate;
    private Boolean sundayHolidayDelivery;
    private String customerReferenceNo;
    private String senderName;
    private String senderEMail;
    private String recipientName;
    private String recipientEMail;

    private Integer validationStatus;

    public USPSExpressMailLabel() {
    }

    public String getFromFirstName() {
        return fromFirstName;
    }

    public void setFromFirstName(String fromFirstName) {
        this.fromFirstName = fromFirstName;
    }

    public String getFromLastName() {
        return fromLastName;
    }

    public void setFromLastName(String fromLastName) {
        this.fromLastName = fromLastName;
    }

    public String getFromFirm() {
        return fromFirm;
    }

    public void setFromFirm(String fromFirm) {
        this.fromFirm = fromFirm;
    }

    public String getFromAddress1() {
        return fromAddress1;
    }

    public void setFromAddress1(String fromAddress1) {
        this.fromAddress1 = fromAddress1;
    }

    public String getFromAddress2() {
        return fromAddress2;
    }

    public void setFromAddress2(String fromAddress2) {
        this.fromAddress2 = fromAddress2;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getFromZip5() {
        return fromZip5;
    }

    public void setFromZip5(String fromZip5) {
        this.fromZip5 = fromZip5;
    }

    public String getFromPhone() {
        return fromPhone;
    }

    public void setFromPhone(String fromPhone) {
        this.fromPhone = fromPhone;
    }

    public String getToFirstName() {
        return toFirstName;
    }

    public void setToFirstName(String toFirstName) {
        this.toFirstName = toFirstName;
    }

    public String getToLastName() {
        return toLastName;
    }

    public void setToLastName(String toLastName) {
        this.toLastName = toLastName;
    }

    public String getToFirm() {
        return toFirm;
    }

    public void setToFirm(String toFirm) {
        this.toFirm = toFirm;
    }

    public String getToAddress1() {
        return toAddress1;
    }

    public void setToAddress1(String toAddress1) {
        this.toAddress1 = toAddress1;
    }

    public String getToAddress2() {
        return toAddress2;
    }

    public void setToAddress2(String toAddress2) {
        this.toAddress2 = toAddress2;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public String getToZip5() {
        return toZip5;
    }

    public void setToZip5(String toZip5) {
        this.toZip5 = toZip5;
    }

    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
    }

    public String getWeighInOunces() {
        return weighInOunces;
    }

    public void setWeighInOunces(String weighInOunces) {
        this.weighInOunces = weighInOunces;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public Boolean getSundayHolidayDelivery() {
        return sundayHolidayDelivery;
    }

    public void setSundayHolidayDelivery(Boolean sundayHolidayDelivery) {
        this.sundayHolidayDelivery = sundayHolidayDelivery;
    }

    public String getCustomerReferenceNo() {
        return customerReferenceNo;
    }

    public void setCustomerReferenceNo(String customerReferenceNo) {
        this.customerReferenceNo = customerReferenceNo;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEMail() {
        return senderEMail;
    }

    public void setSenderEMail(String senderEMail) {
        this.senderEMail = senderEMail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientEMail() {
        return recipientEMail;
    }

    public void setRecipientEMail(String recipientEMail) {
        this.recipientEMail = recipientEMail;
    }

    public Integer getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(Integer validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        sb.append("<ExpressMailLabelRequest USERID=\"" + USPSWebService.USPS_USER_ID + "\">");
        sb.append("<Option />");
        sb.append("<Revision />");
        sb.append("<EMCAAccount />");
        sb.append("<EMCAPassword />");
        sb.append("<ImageParameters />");
        sb.append("<FromFirstName>"+fromFirstName+"</FromFirstName>");
        sb.append("<FromLastName>"+fromLastName+"</FromLastName>");
        sb.append("<FromFirm>"+fromFirm+"</FromFirm>");
        sb.append("<FromAddress1>"+fromAddress1+"</FromAddress1>");
        sb.append("<FromAddress2>"+fromAddress2+"</FromAddress2>");
        sb.append("<FromCity>"+fromCity+"</FromCity>");
        sb.append("<FromState>"+fromState+"</FromState>");
        sb.append("<FromZip5>" + fromZip5 + "</FromZip5>");
        sb.append("<FromZip4 />");
        sb.append("<FromPhone>"+fromPhone+"</FromPhone>");
        sb.append("<ToFirstName>"+toFirstName+"</ToFirstName>");
        sb.append("<ToLastName>"+toLastName+"</ToLastName>");
        sb.append("<ToFirm>"+toFirm+"</ToFirm>");
        sb.append("<ToAddress1>"+toAddress1+"</ToAddress1>");
        sb.append("<ToAddress2>"+toAddress2+"</ToAddress2>");
        sb.append("<ToCity>"+toCity+"</ToCity>");
        sb.append("<ToState>"+toState+"</ToState>");
        sb.append("<ToZip5/>");
        sb.append("<ToZip4 />");
        sb.append("<ToPhone>" + toPhone + "</ToPhone>");
        sb.append("<WeightInOunces />");
        sb.append("<ShipDate>0/0/00</ShipDate>");
        sb.append("<FlatRate>false</FlatRate>");
        sb.append("<SundayHolidayDelivery>false</SundayHolidayDelivery>");
        sb.append("<StandardizeAddress>true</StandardizeAddress>");
        sb.append("<WaiverOfSignature>true</WaiverOfSignature>");
        sb.append("<NoHoliday>false</NoHoliday>");
        sb.append("<NoWeekend>false</NoWeekend>");
        sb.append("<SeparateReceiptPage>false</SeparateReceiptPage>");
        sb.append("<POZipCode />");
        sb.append("<FacilityType>DDU</FacilityType>");
        sb.append("<ImageType>PDF</ImageType>");
        sb.append("<LabelDate>0/0/00</LabelDate>");
        sb.append("<CustomerRefNo>" + customerReferenceNo + "</CustomerRefNo>");
        sb.append("<SenderName>"+senderName+"</SenderName>");
        sb.append("<SenderEMail>"+senderEMail+"</SenderEMail>");
        sb.append("<RecipientName>"+recipientName+"</RecipientName>");
        sb.append("<RecipientEMail>"+recipientEMail+"</RecipientEMail>");
        sb.append("<HoldForManifest>N</HoldForManifest>");
        sb.append("<CommercialPrice>false</CommercialPrice>");
        sb.append("<InsuredAmount>0.0</InsuredAmount>");
        sb.append("</ExpressMailLabelRequest>");

        return sb.toString();
    }
}
