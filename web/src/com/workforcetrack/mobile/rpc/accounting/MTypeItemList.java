package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/13/11
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "typeItem")
public class MTypeItemList {

    private List<MTypeItem> typeItem;
    private Integer totalCount;

    public MTypeItemList() {

    }
    public MTypeItemList(TypeItem[] typeItems) {
        if (typeItems != null) {
            this.typeItem = new ArrayList<>();
            for (TypeItem typeItem : typeItems) {
                this.typeItem.add(new MTypeItem(typeItem));
            }
            this.totalCount = typeItems.length;
        }
    }

    public List<MTypeItem> getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(List<MTypeItem> typeItem) {
        this.typeItem = typeItem;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}