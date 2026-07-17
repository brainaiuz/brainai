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
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "countryList")
public class MCountryList {

    @XmlElement(name = "country")
    private List<MSelectItem> countryList;

    public MCountryList() {
    }

    public MCountryList(SelectItem[] selectItems) {
        if (selectItems != null) {
            this.countryList = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                this.countryList.add(new MSelectItem(selectItem));
            }

        }
    }

    public List<MSelectItem> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<MSelectItem> countryList) {
        this.countryList = countryList;
    }
}
