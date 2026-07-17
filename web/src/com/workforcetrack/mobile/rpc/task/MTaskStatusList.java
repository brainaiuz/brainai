package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/12/11
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MTaskStatusList {

    private List<MSelectItem> status;

    public MTaskStatusList() {
    }

    public MTaskStatusList(SelectItem[] statusList) {
        if (statusList !=null){
            this.status = new ArrayList<>();
            for (SelectItem selectItem : statusList) {
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


