package com.workforcetrack.api.base;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.login.MCompanyItem;
import com.workforcetrack.mobile.rpc.login.MCompanyList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 16.05.12
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
@JsonPropertyOrder({"totalCount", "items"})
public class APISelectItemList {

    private List<APISelectItem> items;
    private Integer totalCount;

    public APISelectItemList() {
    }

    public APISelectItemList(MCompanyList companyList) {
        if (companyList != null && companyList.getCompanyListItems() != null && companyList.getCompanyListItems().size() > 0) {
            for (MCompanyItem companyItem : companyList.getCompanyListItems()) {
                getItems().add(new APISelectItem(companyItem.getObjectID(), companyItem.getCompanyName(), companyItem.getDescription()));
            }
            setTotalCount(companyList.getCompanyListItems().size());
        }
    }

    public APISelectItemList(List<MSelectItem> selectItems) {
        if (selectItems != null && selectItems.size() > 0) {
            for (MSelectItem selectItem : selectItems) {
                getItems().add(new APISelectItem(selectItem));
            }
            setTotalCount(selectItems.size());
        }
    }

    public APISelectItemList(SelectItem[] selectItems) {
        if (selectItems != null && selectItems.length > 0) {
            for (SelectItem selectItem : selectItems) {
                getItems().add(new APISelectItem(selectItem));
            }
            setTotalCount(selectItems.length);
        }
    }

    public static APISelectItemList getAPISelectItemList(List<SelectItem> selectItems) {
        APISelectItemList itemList = new APISelectItemList();
        if (selectItems != null && selectItems.size() > 0) {
            for (SelectItem item : selectItems) {
                itemList.getItems().add(new APISelectItem(item));
            }
            itemList.setTotalCount(selectItems.size());
        }
        return itemList;
    }

    public static SelectItem[] getSelectItem(List<APISelectItem> items) {
        List<SelectItem> selectItems = new ArrayList<>();
        if (items != null && items.size() > 0) {
            for (APISelectItem item : items) {
                SelectItem selectItem = new SelectItem(item.getObjectID(), item.getName(), item.getDescription());
                selectItem.setSelected(item.getSelected() != null ? item.getSelected() : false);
                selectItem.setNewItem(item.getNewItem());
                selectItems.add(selectItem);
            }
        }
        return selectItems.toArray(new SelectItem[]{});
    }

    public List<APISelectItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<APISelectItem> items) {
        this.items = items;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
