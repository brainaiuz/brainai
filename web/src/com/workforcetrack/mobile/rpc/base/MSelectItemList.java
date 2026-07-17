package com.workforcetrack.mobile.rpc.base;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 14.09.11
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MSelectItemList {

    private Integer totalCount;
    private List<MSelectItem> item;

    public MSelectItemList() {
    }

    public MSelectItemList(SelectItem[] selectItems, Integer totalCount) {
        item = WebServiceUtils.getAsMSelectItemList(selectItems);
        this.totalCount = totalCount;
    }

    public MSelectItemList(SelectItem[] selectItems) {
        item = WebServiceUtils.getAsMSelectItemList(selectItems);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MSelectItem> getItem() {
        return item;
    }

    public void setItem(List<MSelectItem> item) {
        this.item = item;
    }


}
