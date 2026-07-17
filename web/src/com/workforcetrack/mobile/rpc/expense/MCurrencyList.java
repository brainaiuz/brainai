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
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "currencyList")
public class MCurrencyList {

    @XmlElement(name = "currency")
    private List<MSelectItem> currencyList;

    public MCurrencyList() {
    }

    public MCurrencyList(SelectItem[] selectItems) {
        if (selectItems != null) {
            this.currencyList = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                this.currencyList.add(new MSelectItem(selectItem));
            }
        }
    }

    public List<MSelectItem> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<MSelectItem> currencyList) {
        this.currencyList = currencyList;
    }
}
