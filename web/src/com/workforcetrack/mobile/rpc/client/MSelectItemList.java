package com.workforcetrack.mobile.rpc.client;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/27/11
 * Time: 9:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MSelectItemList {

    Integer totalCount;
    List<MSelectItem> ownerItem;

    public MSelectItemList() {

    }

    public MSelectItemList(List<MSelectItem> mSelectItems) {

        this.ownerItem = mSelectItems;
        this.totalCount = (mSelectItems != null) ? mSelectItems.size() : 0;
    }

    public MSelectItemList(List<MSelectItem> mSelectItems, Integer totalCount) {
        this.totalCount = totalCount;
        this.ownerItem = mSelectItems;
    }

    public MSelectItemList(SelectItem[] selectItems) {
        if (selectItems != null) {
             this.totalCount = selectItems.length;
             if (selectItems != null && selectItems.length > 0) {
                this.ownerItem = new ArrayList<>();
                for (SelectItem selectItem : selectItems) {
                    this.ownerItem.add(new MSelectItem(selectItem));
                }
             }
        }
        else {
            this.totalCount = null;
            this.ownerItem = null;

        }
    }
    public ArrayList<MSelectItem> convert (SelectItem[] selectItems) {
        this.totalCount = selectItems.length;
        if (selectItems != null && selectItems.length > 0) {
            this.ownerItem = new ArrayList<>();
            for (SelectItem selectItem : selectItems) {
                this.ownerItem.add(new MSelectItem(selectItem));
            }
        }
        return (ArrayList<MSelectItem>) this.ownerItem;
    }
    public MSelectItemList(MSelectItem[] mSelectItems) {
        if (mSelectItems != null && mSelectItems.length > 0) {
            this.totalCount = mSelectItems.length;
            this.ownerItem = Arrays.asList(mSelectItems);
        }
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MSelectItem> getSelectItems() {
        return ownerItem;
    }

    public void setSelectItems(List<MSelectItem> selectItems) {
        this.ownerItem = selectItems;
    }
}
