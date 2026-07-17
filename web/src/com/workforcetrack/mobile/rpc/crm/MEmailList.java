package com.workforcetrack.mobile.rpc.crm;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 24.01.12
 * Time: 14:44
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MEmailList {

    private Integer totalCount;
    private List<MEmailItem> emailItem;

    public MEmailList() {

    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MEmailItem> getEmailItem() {
        return emailItem;
    }

    public void setEmailItem(List<MEmailItem> emailItem) {
        this.emailItem = emailItem;
    }
}
