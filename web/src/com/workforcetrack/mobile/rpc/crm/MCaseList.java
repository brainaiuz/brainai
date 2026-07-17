package com.workforcetrack.mobile.rpc.crm;

import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 23.01.12
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MCaseList {

    private Integer totalCount;
    private List<MCaseItem> caseItem;

    public MCaseList() {

    }

    public MCaseList(CaseList caseList) {
        if (caseList != null && caseList.getList() != null && caseList.getList().size() > 0) {
            totalCount = caseList.getTotal();
            caseItem = new ArrayList<>();
            for (CaseItem item : caseList.getList()) {
                caseItem.add(new MCaseItem(item));
            }
        }
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MCaseItem> getCaseItem() {
        return caseItem;
    }

    public void setCaseItem(List<MCaseItem> caseItem) {
        this.caseItem = caseItem;
    }
}
