package com.edatasite.workforce.gwt.core.server.usps;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/25/12
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class USPSDeliveryConfirmation {
    public static final Integer PRIMARY_CONTACT_IS_NOT_EXIST = -1;

    private String option;
    private String imageParameters;
    private String fromName;
    private String fromFirm;
    private String fromAddress1;
    private String fromAddress2;
    private String fromCity;
    private String fromState;
    private String fromZip5;
    private String fromZip4;
    private String toName;
    private String toFirm;
    private String toAddress1;
    private String toAddress2;
    private String toCity;
    private String toState;
    private String toZip5;
    private String toZip4;
    private String weightInOunces;
    private String serviceType;
    private String poZipCode;
    private String imageType = "PDF";
    private String labelDate;
    private String customerReferenceNo;
    private String senderName;
    private String senderEMail;
    private String recipientName;
    private String recipientEMail;

    private Integer validationStatus;


    public USPSDeliveryConfirmation() {
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getImageParameters() {
        return imageParameters;
    }

    public void setImageParameters(String imageParameters) {
        this.imageParameters = imageParameters;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
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

    public String getFromZip4() {
        return fromZip4;
    }

    public void setFromZip4(String fromZip4) {
        this.fromZip4 = fromZip4;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
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

    public String getToZip4() {
        return toZip4;
    }

    public void setToZip4(String toZip4) {
        this.toZip4 = toZip4;
    }

    public String getWeightInOunces() {
        return weightInOunces;
    }

    public void setWeightInOunces(String weightInOunces) {
        this.weightInOunces = weightInOunces;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPoZipCode() {
        return poZipCode;
    }

    public void setPoZipCode(String poZipCode) {
        this.poZipCode = poZipCode;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getLabelDate() {
        return labelDate;
    }

    public void setLabelDate(String labelDate) {
        this.labelDate = labelDate;
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

    public String toXML(){
        StringBuilder sb = new StringBuilder();
        sb.append("<DeliveryConfirmationV3.0Request USERID=\"" + USPSWebService.USPS_USER_ID + "\">");
        sb.append("<Option>" + option + "</Option>");
        sb.append("<ImageParameters />");
        sb.append("<FromName>" + fromName + "</FromName>");
        sb.append("<FromFirm>" + fromFirm + "</FromFirm>");
        sb.append("<FromAddress1>" + fromAddress1 + "</FromAddress1>");
        sb.append("<FromAddress2>" + fromAddress2 + "</FromAddress2>");
        sb.append("<FromCity>" + fromCity + "</FromCity>");
        sb.append("<FromState>" + fromState + "</FromState>");
        sb.append("<FromZip5>" + fromZip5 + "</FromZip5>");
        sb.append("<FromZip4 />");
        sb.append("<ToName>" + toName + "</ToName>");
        sb.append("<ToFirm>" + toFirm + "</ToFirm>");
        sb.append("<ToAddress1>" + toAddress1 + "</ToAddress1>");
        sb.append("<ToAddress2>" + toAddress2 + "</ToAddress2>");
        sb.append("<ToCity>" + toCity + "</ToCity>");
        sb.append("<ToState>" + toState + "</ToState>");
        sb.append("<ToZip5/>");
        sb.append("<ToZip4 />");
        sb.append("<WeightInOunces>" + weightInOunces + "</WeightInOunces>");
        sb.append("<ServiceType>" + serviceType + "</ServiceType>");
        sb.append("<POZipCode />");
        sb.append("<ImageType>" + imageType + "</ImageType>");
        sb.append("<LabelDate />");
        sb.append("<CustomerRefNo>" + customerReferenceNo + "</CustomerRefNo>");
        sb.append("<AddressServiceRequested>TRUE</AddressServiceRequested>");
        sb.append("<SenderName>" + senderName + "</SenderName>");
        sb.append("<SenderEMail>" + senderEMail + "</SenderEMail>");
        sb.append("<RecipientName>" + recipientName + "</RecipientName>");
        sb.append("<RecipientEMail>" + recipientEMail + "</RecipientEMail>");
        sb.append("</DeliveryConfirmationV3.0Request>");

        return sb.toString();
    }


}
