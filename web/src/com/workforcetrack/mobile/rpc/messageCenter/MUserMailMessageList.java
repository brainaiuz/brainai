package com.workforcetrack.mobile.rpc.messageCenter;

import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 03.09.11
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MUserMailMessageList {

    private String corporateEmail;
    private Integer totalCount;
    private ArrayList<MUserMailMessage> userMailMessage = new ArrayList<>();

    public MUserMailMessageList() {

    }

    public MUserMailMessageList(ListResult<Email> userMailMessageListResult) {
        if (userMailMessageListResult != null && userMailMessageListResult.getList().size() > 0) {
            userMailMessage = new ArrayList<>();
            for (Email mailMessage : userMailMessageListResult.getList()) {
                userMailMessage.add(new MUserMailMessage(mailMessage));
            }

            this.totalCount = userMailMessageListResult.getTotal();
        }
    }

    public MUserMailMessageList(ListResult<Email> userMailMessageListResult, String corporateEmail) {
        if (userMailMessageListResult != null && userMailMessageListResult.getList().size() > 0) {
            userMailMessage = new ArrayList<>();
            for (Email mailMessage : userMailMessageListResult.getList()) {
                userMailMessage.add(new MUserMailMessage(mailMessage));
            }

            this.totalCount = userMailMessageListResult.getTotal();
        }
        this.corporateEmail = corporateEmail;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getCorporateEmail() {
        return corporateEmail;
    }

    public void setCorporateEmail(String corporateEmail) {
        this.corporateEmail = corporateEmail;
    }

    public ArrayList<MUserMailMessage> getUserMailMessage() {
        return userMailMessage;
    }

    public void setUserMailMessage(ArrayList<MUserMailMessage> userMailMessage) {
        this.userMailMessage = userMailMessage;
    }
}
