package com.workforcetrack.mobile.rpc.expense;

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
 * Date: 6/22/11
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "relatedProjectList")
public class MRelatedProjectList {

    @XmlElement(name = "relatedProject")
    private List<MSelectItem> relatedProjectList;

    public MRelatedProjectList() {
    }

    public MRelatedProjectList(SelectItem[] selectItems) {
        if (selectItems !=null) {
            this.relatedProjectList = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                this.relatedProjectList.add(new MSelectItem(selectItem));
            }
        }
    }

    public List<MSelectItem> getRelatedProjectList() {
        return relatedProjectList;
    }

    public void setRelatedProjectList(List<MSelectItem> relatedProjectList) {
        this.relatedProjectList = relatedProjectList;
    }
}
