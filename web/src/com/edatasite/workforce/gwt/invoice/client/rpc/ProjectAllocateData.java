package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/4/11
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectAllocateData implements IsSerializable{
    private SelectItem crmAccount;
    private SelectItem project;
    private BigDecimal amount;

    public ProjectAllocateData() {
    }

    public SelectItem getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
