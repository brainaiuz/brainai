package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/6/11
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class MPriorityList {

    private List<MSelectItem> priority;

    public MPriorityList() {

    }

    public MPriorityList(SelectItem[] priorities) {
        if (priorities != null) {
            this.priority = new ArrayList<>();
            for (SelectItem selectItem : priorities) {
                this.priority.add(new MSelectItem(selectItem));
            }
        }
    }

    public MPriorityList(List<MSelectItem> priority) {
        this.priority = priority;
    }



    public List<MSelectItem> getPriority() {
        return priority;
    }

    public void setPriority(List<MSelectItem> priority) {
        this.priority = priority;
    }
}
