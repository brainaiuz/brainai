package com.edatasite.workforce.gwt.crm.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 14, 2009
 * Time: 8:35:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class SolutionCaseItem implements IsSerializable {
    private Integer objectId;
    private String caseNumber;
    private String subject;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
