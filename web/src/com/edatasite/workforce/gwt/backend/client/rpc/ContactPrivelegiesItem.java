package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 13.08.2010
 * Time: 17:46:55
 * To change this template use File | Settings | File Templates.
 */
public class ContactPrivelegiesItem implements IsSerializable {

    private Integer companyID;
    private String companyLoginLink;
    private String companyName;
    private boolean showOrHide;
    private Boolean yesOrNo;
    private Boolean showEmployeePDFFooter = true;

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Boolean isYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(Boolean yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    public String getCompanyLoginLink() {
        return companyLoginLink;
    }

    public void setCompanyLoginLink(String companyLoginLink) {
        this.companyLoginLink = companyLoginLink;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isShowOrHide() {
        return showOrHide;
    }

    public void setShowOrHide(boolean showOrHide) {
        this.showOrHide = showOrHide;
    }

    public void setShowEmployeePDFFooter(Boolean showEmployeePDFFooter) {
        this.showEmployeePDFFooter = showEmployeePDFFooter;
    }

    public Boolean getShowEmployeePDFFooter() {
        return showEmployeePDFFooter;
    }
}
