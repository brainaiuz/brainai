package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/21/12
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseEmailTemplateData implements IsSerializable{
    private String messageType;
    private SelectItem[] templateList;

    public ExpenseEmailTemplateData() {
    }

    public ExpenseEmailTemplateData(String messageType, SelectItem[] templateList) {
        this.messageType = messageType;
        this.templateList = templateList;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public SelectItem[] getTemplateList() {
        return templateList;
    }

    public void setTemplateList(SelectItem[] templateList) {
        this.templateList = templateList;
    }
}
