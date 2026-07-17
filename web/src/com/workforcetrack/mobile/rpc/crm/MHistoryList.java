package com.workforcetrack.mobile.rpc.crm;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 23.01.12
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MHistoryList {

    private Integer totalCount;
    private List<MHistoryListItem> historyItem;

    public MHistoryList() {
    }

    public MHistoryList(HistoryList historyList) {
        if (historyList != null && historyList.getResult() != null && historyList.getResult().length > 0) {
            totalCount = historyList.getTotalCount();
            historyItem = new ArrayList<>();
            for (HistoryListItem item : historyList.getResult()) {
                historyItem.add(new MHistoryListItem(item));
            }
        }
    }

    public static List<MHistoryListItem> convertToMobile(ArrayList<HistoryListItem> items) {
        if (items != null && items.size() > 0) {
            List<MHistoryListItem> notes = new ArrayList<>();
            for (HistoryListItem item : items) {
                notes.add(new MHistoryListItem(item));
            }
            return notes;
        }
        return null;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MHistoryListItem> getHistoryItem() {
        return historyItem;
    }

    public void setHistoryItem(List<MHistoryListItem> historyItem) {
        this.historyItem = historyItem;
    }
}
