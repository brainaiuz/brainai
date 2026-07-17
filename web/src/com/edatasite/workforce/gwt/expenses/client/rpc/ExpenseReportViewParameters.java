package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/19/12
 * Time: 4:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseReportViewParameters implements IsSerializable {
    private Integer objectID;
    private Integer projectID;
    private Integer externalObjectID;
    private Integer externalFormID;
    private Integer employeeID;
    private Integer opportunityID;
    private Integer purchaseOrderID;
    private Integer saleOrderId;

    public ExpenseReportViewParameters() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getExternalObjectID() {
        return externalObjectID;
    }

    public void setExternalObjectID(Integer externalObjectID) {
        this.externalObjectID = externalObjectID;
    }

    public Integer getExternalFormID() {
        return externalFormID;
    }

    public void setExternalFormID(Integer externalFormID) {
        this.externalFormID = externalFormID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public Integer getPurchaseOrderID() {
        return purchaseOrderID;
    }
    public Integer getSaleOrderId() {
        return saleOrderId;
    }

    public void setSaleOrderId(Integer clientId) {
        this.saleOrderId = clientId;
    }


    public void setPurchaseOrderID(Integer purchaseOrderID) {
        this.purchaseOrderID = purchaseOrderID;
    }
}
