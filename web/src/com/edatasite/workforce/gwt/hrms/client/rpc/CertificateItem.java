package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Khasan on 08.09.14.
 */
public class CertificateItem extends HasApprovers implements ListingCustomFields {

    public static final String NUMBER = "NUMBER";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    public static final String CERTIFICATE_TYPE = "CERTIFICATE_TYPE";
    public static final String ISSUED_DATE = "ISSUED_DATE";
    public static final String ISSUED_BY = "ISSUED_BY";
    public static final String CREATED_DATE = "CREATED_DATE";
    public static final String CREATED_BY = "CREATED_BY";
    public static final String NAME = "NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String APPROVER = "APPROVER";
    public static final String STATUS = "STATUS";


    private Integer objectId;
    private NumberData certificateNumber;
    private SelectItem certificateType;
    private SelectItem[] types;
    private SelectItem[] fields;
    private SelectItem employee;
    private String employeeCode;
    private Date updatedDate;
    private SelectItem updatedBy;
    private SelectItem createdBy;

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(SelectItem updatedBy) {
        this.updatedBy = updatedBy;
    }

    public SelectItem getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SelectItem createdBy) {
        this.createdBy = createdBy;
    }

    private String content;
    private String customHTMLcontent;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private ArrayList<CompanyCustomFieldItem> leaveRequestCustomFields;
    private String textBox1;
    private String textBox2;
    private String textBox3;
    private String textBox4;
    private String textBox5;
    private String textBox6;
    private String textBox7;
    private String textBox8;
    private String textBox9;
    private String textBox10;
    private String textBox11;
    private String textBox12;
    private String textBox13;
    private String textBox14;
    private String textBox15;
    private String textBox16;
    private String textBox17;
    private String textBox18;
    private String textArea1;
    private String textArea2;
    private String textArea3;
    private String textArea4;
    private String textArea5;
    private String textArea6;
    private String textArea7;
    private String textArea8;
    private String name;
    private String description;
    private Date creationDate;
    private SelectItem type;
    private Integer stepEmployeeId;
    private ArrayList<FileResource> ducumentList;
    private ArrayList<Integer> ducumentIds;
    private boolean pdfHeaderFooter;
    private String statusCode;
    private String formID;
    private Integer currentUserID;
    private String currentUserName;
    private boolean setupApproval;
    private boolean pdfPermission;
    private boolean deletePermission;
    private boolean editPermission;
    private boolean canApprove;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public NumberData getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(NumberData certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public SelectItem getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(SelectItem certificateType) {
        this.certificateType = certificateType;
    }

    public SelectItem[] getTypes() {
        return types;
    }

    public void setTypes(SelectItem[] types) {
        this.types = types;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCustomHTMLcontent() {
        return customHTMLcontent;
    }

    public void setCustomHTMLcontent(String customHTMLcontent) {
        this.customHTMLcontent = customHTMLcontent;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem[] getFields() {
        return fields;
    }

    public void setFields(SelectItem[] fields) {
        this.fields = fields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public ArrayList<CompanyCustomFieldItem> getLeaveRequestCustomFields() {
        return leaveRequestCustomFields;
    }

    public void setLeaveRequestCustomFields(ArrayList<CompanyCustomFieldItem> leaveRequestCustomFields) {
        this.leaveRequestCustomFields = leaveRequestCustomFields;
    }

    public void setTextBox1(String textBox1) {
        this.textBox1 = textBox1;
    }

    public String getTextBox1() {
        return textBox1;
    }

    public String getTextBox2() {
        return textBox2;
    }

    public void setTextBox2(String textBox2) {
        this.textBox2 = textBox2;
    }

    public String getTextBox3() {
        return textBox3;
    }

    public void setTextBox3(String textBox3) {
        this.textBox3 = textBox3;
    }

    public String getTextBox4() {
        return textBox4;
    }

    public void setTextBox4(String textBox4) {
        this.textBox4 = textBox4;
    }

    public void setTextArea1(String textArea1) {
        this.textArea1 = textArea1;
    }

    public void setTextArea2(String textArea2) {
        this.textArea2 = textArea2;
    }

    public String getTextArea1() {
        return textArea1;
    }

    public String getTextArea2() {
        return textArea2;
    }

    public String getTextArea3() {
        return textArea3;
    }

    public void setTextArea3(String textArea3) {
        this.textArea3 = textArea3;
    }

    public String getTextArea4() {
        return textArea4;
    }

    public void setTextArea4(String textArea4) {
        this.textArea4 = textArea4;
    }

    public String getTextArea5() {
        return textArea5;
    }

    public void setTextArea5(String textArea5) {
        this.textArea5 = textArea5;
    }

    public String getTextArea6() {
        return textArea6;
    }

    public void setTextArea6(String textArea6) {
        this.textArea6 = textArea6;
    }

    public String getTextArea7() {
        return textArea7;
    }

    public void setTextArea7(String textArea7) {
        this.textArea7 = textArea7;
    }

    public String getTextArea8() {
        return textArea8;
    }

    public void setTextArea8(String textArea8) {
        this.textArea8 = textArea8;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public SelectItem getType() {
        return type;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public Integer getStepEmployeeId() {
        return stepEmployeeId;
    }

    public void setStepEmployeeId(Integer stepEmployeeId) {
        this.stepEmployeeId = stepEmployeeId;
    }

    public void setDucumentList(ArrayList<FileResource> ducumentList) {
        this.ducumentList = ducumentList;
    }

    public ArrayList<FileResource> getDucumentList() {
        return ducumentList;
    }

    public ArrayList<Integer> getDucumentIds() {
        return ducumentIds;
    }

    public void setDucumentIds(ArrayList<Integer> ducumentIds) {
        this.ducumentIds = ducumentIds;
    }

    public boolean isPdfHeaderFooter() {
        return pdfHeaderFooter;
    }

    public void setPdfHeaderFooter(boolean pdfHeaderFooter) {
        this.pdfHeaderFooter = pdfHeaderFooter;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getTextBox5() {
        return textBox5;
    }

    public void setTextBox5(String textBox5) {
        this.textBox5 = textBox5;
    }

    public String getTextBox6() {
        return textBox6;
    }

    public void setTextBox6(String textBox6) {
        this.textBox6 = textBox6;
    }

    public String getTextBox7() {
        return textBox7;
    }

    public void setTextBox7(String textBox7) {
        this.textBox7 = textBox7;
    }

    public String getTextBox8() {
        return textBox8;
    }

    public void setTextBox8(String textBox8) {
        this.textBox8 = textBox8;
    }

    public String getTextBox9() {
        return textBox9;
    }

    public void setTextBox9(String textBox9) {
        this.textBox9 = textBox9;
    }

    public String getTextBox10() {
        return textBox10;
    }

    public void setTextBox10(String textBox10) {
        this.textBox10 = textBox10;
    }

    public String getTextBox11() {
        return textBox11;
    }

    public void setTextBox11(String textBox11) {
        this.textBox11 = textBox11;
    }

    public String getTextBox12() {
        return textBox12;
    }

    public void setTextBox12(String textBox12) {
        this.textBox12 = textBox12;
    }

    public String getTextBox13() {
        return textBox13;
    }

    public void setTextBox13(String textBox13) {
        this.textBox13 = textBox13;
    }

    public String getTextBox14() {
        return textBox14;
    }

    public void setTextBox14(String textBox14) {
        this.textBox14 = textBox14;
    }

    public String getTextBox15() {
        return textBox15;
    }

    public void setTextBox15(String textBox15) {
        this.textBox15 = textBox15;
    }

    public String getTextBox16() {
        return textBox16;
    }

    public void setTextBox16(String textBox16) {
        this.textBox16 = textBox16;
    }

    public String getTextBox17() {
        return textBox17;
    }

    public void setTextBox17(String textBox17) {
        this.textBox17 = textBox17;
    }

    public String getTextBox18() {
        return textBox18;
    }

    public void setTextBox18(String textBox18) {
        this.textBox18 = textBox18;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public Integer getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(Integer currentUserID) {
        this.currentUserID = currentUserID;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public boolean isSetupApproval() {
        return setupApproval;
    }

    public void setSetupApproval(boolean setupApproval) {
        this.setupApproval = setupApproval;
    }

    public boolean isPdfPermission() {
        return pdfPermission;
    }

    public void setPdfPermission(boolean pdfPermission) {
        this.pdfPermission = pdfPermission;
    }

    public boolean isDeletePermission() {
        return deletePermission;
    }

    public void setDeletePermission(boolean deletePermission) {
        this.deletePermission = deletePermission;
    }

    public boolean isEditPermission() {
        return editPermission;
    }

    public void setEditPermission(boolean editPermission) {
        this.editPermission = editPermission;
    }

    public Integer getCurrentApproverEmployeeID(){
        if(getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null){
            return getCurrentApprover().getExactEmployee().getId();
        }
        return null;
    }

    public String getCurrentApproverEmployeeName(){
        if(getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null){
            return getCurrentApprover().getExactEmployee().getName();
        }
        return null;
    }

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

}
