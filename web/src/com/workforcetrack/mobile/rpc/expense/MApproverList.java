package com.workforcetrack.mobile.rpc.expense;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/22/11
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "apporoverList")
public class MApproverList {

    @XmlElement(name = "approver")
    private List<MSelectItem> approvers;
    @XmlElement(name = "approver2")
    private List<MSelectItem> approvers2;

    public List<MSelectItem> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<MSelectItem> approvers) {
        this.approvers = approvers;
    }

    public MApproverList() {

    }

    public MApproverList(SelectItem[] selectItems) {
        if (selectItems != null) {
            this.approvers = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                this.approvers.add(new MSelectItem(selectItem));
            }
        }
    }

    public MApproverList(SelectItem[] approvers1, SelectItem[] approvers2) {
        this.approvers = WebServiceUtils.getAsMSelectItemList(approvers1);
        this.approvers2 = WebServiceUtils.getAsMSelectItemList(approvers2);
    }

}
