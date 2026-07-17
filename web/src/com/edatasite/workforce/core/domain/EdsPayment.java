package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;
import java.util.Date;
//import com.edatasite.workforce.gwt.payroll.client.rpc.payment.PaymentListItem;

/**
 * Created by IntelliJ IDEA.
 * User: said
 * Date: May 15, 2007
 * Time: 12:27:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payment")
public class EdsPayment extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    private String description;

    private Date paymentdate;

    private Float units;

    private Double rate;

    private Double amount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "payslipId")
    private EdsPayslip payslip;

//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "payment_categoryId")
//    private EdsPaymentCategory paymentCategory;

//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "methodId")
//    private EdsPaymentMethod method;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private EdsProject project;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;

//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "typeId")
//    private EdsPaymentType type;

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

    public Date getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(Date paymentdate) {
        this.paymentdate = paymentdate;
    }

    public Float getUnits() {
        return units;
    }

    public void setUnits(Float units) {
        this.units = units;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public EdsPayslip getPayslip() {
        return payslip;
    }

    public void setPayslip(EdsPayslip payslip) {
        this.payslip = payslip;
    }

//    public EdsPaymentCategory getPaymentCategory() {
//        return paymentCategory;
//    }
//
//    public void setPaymentCategory(EdsPaymentCategory paymentCategory) {
//        this.paymentCategory = paymentCategory;
//    }

//    public EdsPaymentMethod getMethod() {
//        return method;
//    }
//
//    public void setMethod(EdsPaymentMethod method) {
//        this.method = method;
//    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

//    public EdsPaymentType getType() {
//        return type;
//    }
//
//    public void setType(EdsPaymentType type) {
//        this.type = type;
//    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    /*public PaymentListItem createPaymentListItem(){
    	PaymentListItem paymentListItem = new PaymentListItem();
    	paymentListItem.setId(getObjectID());
    	paymentListItem.setName(getName());        
    	paymentListItem.setDescription(getDescription());  
    	if(getPaymentdate() != null)
    		paymentListItem.setPaymentdate(new Date(getPaymentdate().getTime()));        
        paymentListItem.setUnits(getUnits());        
        paymentListItem.setRate(getRate());        
        paymentListItem.setAmount(getAmount());        	
        if(getPayslip() != null){
	        paymentListItem.setPayslipId(getPayslip().getObjectID());
//	        paymentListItem.setPayslipName(getPayslip().getName());
	        paymentListItem.setPayslipDateFrom(new Date(getPayslip().getDatefrom().getTime()));
	        paymentListItem.setPayslipDateTo(new Date(getPayslip().getDateto().getTime()));
        }
        if(getPaymentCategory() != null){
	        paymentListItem.setPaymentCategoryId(getPaymentCategory().getObjectID());
	        paymentListItem.setPaymentCategoryName(getPaymentCategory().getName());
	        paymentListItem.setTaxable(getPaymentCategory().getTaxable());
        }
        if(getMethod() != null){
	        paymentListItem.setMethodId(getMethod().getObjectID());
	        paymentListItem.setMethodName(getMethod().getName());
        }
        if(getProject() != null){
	        paymentListItem.setProjectId(getProject().getObjectID());
	        paymentListItem.setProjectName(getProject().getName());
        }
        if(getEmployee() != null){
	        paymentListItem.setEmployeeId(getEmployee().getObjectID());
	        paymentListItem.setEmployeeFirstName(getEmployee().getFirstName());
	        paymentListItem.setEmployeeMiddleName(getEmployee().getMiddleName());
	        paymentListItem.setEmployeeLastName(getEmployee().getLastName());
	        paymentListItem.setEmployeeName(getEmployee().getName());
        }
        if(getType() != null){
	        paymentListItem.setTypeId(getType().getObjectID());
	        paymentListItem.setTypeName(getType().getName());
        }
        
    	return paymentListItem;
    }*/
}
