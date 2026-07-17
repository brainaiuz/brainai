package com.workforcetrack.mobile.rpc.contact;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
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
 * Date: 5/27/11
 * Time: 9:57 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "states")
public class MStateList {

    @XmlElement(name = "state")
    private List<MSelectItem> stateList;

    public MStateList() {
    }

    public MStateList(SelectItem[] selectItems) {
        if (selectItems != null) {
            this.stateList = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                this.stateList.add(new MSelectItem(selectItem));
            }
        }
    }

    public List<MSelectItem> getStateList() {
        return stateList;
    }

    public void setStateList(List<MSelectItem> stateList) {
        this.stateList = stateList;
    }
}
