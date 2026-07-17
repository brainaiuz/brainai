package com.workforcetrack.mobile.rpc.accounting;

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
 * User: Sancho
 * Date: 17.08.11
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "productList")
public class MProductsByTypeList {

    @XmlElement(name = "product")
    private List<MSelectItem> productList;

    public MProductsByTypeList() {

    }

    public MProductsByTypeList(SelectItem[] selectItems) {
        if (selectItems != null) {
            this.productList = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                this.productList.add(new MSelectItem(selectItem));
            }

        }
    }

    public List<MSelectItem> getProductList() {
        return productList;
    }

    public void setProductList(List<MSelectItem> productList) {
        this.productList = productList;
    }
}
