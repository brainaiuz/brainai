package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/21/11
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "statusList")
public class MStatusList {

    private List<MSelectItem> status;


    public MStatusList() {
    }

    public MStatusList(SelectItem[] selectItems) {
        if (selectItems != null) {
            this.status = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                this.status.add(new MSelectItem(selectItem));
            }
        }
    }

    public List<MSelectItem> getStatus() {
        return status;
    }

    public void setStatus(List<MSelectItem> status) {
        this.status = status;
    }
}
