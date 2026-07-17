package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MEmailTemplateList {

    private List<MSelectItem> emailTemplate;

    public MEmailTemplateList() {

    }

    public MEmailTemplateList(SelectItem[] selectItems) {
        if (selectItems != null) {
            emailTemplate = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                emailTemplate.add(new MSelectItem(selectItem));
            }
        }
    }

    public List<MSelectItem> getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(List<MSelectItem> emailTemplate) {
        this.emailTemplate = emailTemplate;
    }
}
