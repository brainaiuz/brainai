package com.workforcetrack.mobile.rpc.calendar;

import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.workforcetrack.api.base.APIRepresentation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/23/11
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "taskList")
public class MTaskList implements APIRepresentation {

    public static final String TOTAL_COUNT = "totalCount";
    public static final String ITEMS = "items";

    private Integer totalCount;
    private List<MTaskListItem> taskListItem;

    public MTaskList() {
    }

    public MTaskList(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public MTaskList(TaskList newTaskList) {
        if (newTaskList != null && newTaskList.getList() != null) {
            this.totalCount = newTaskList.getTotal();
            this.taskListItem = new ArrayList<>();
            for (TaskListItem taskListItem : newTaskList.getList()) {
                this.taskListItem.add(new MTaskListItem(taskListItem));
            }
        }
    }

    public MTaskList(List<MTaskListItem> taskListItem, Integer totalCount) {
        this.taskListItem = taskListItem;
        this.totalCount = totalCount;
    }

    public MTaskList(ListResult<ActivityItem> items) {
        if (items != null && items.getList() != null && items.getList().size() > 0) {
            totalCount = items.getTotal();
            taskListItem = new ArrayList<>();
            for (ActivityItem item : items.getList()) {
                taskListItem.add(new MTaskListItem(item));
            }
        }
    }

    public static List<MTaskListItem> getTaskListItems(TaskList taskList) {
        List<MTaskListItem> resultList = new ArrayList<>();
        if (taskList != null && taskList.getList() != null) {
            for (TaskListItem taskListItem : taskList.getList()) {
                resultList.add(new MTaskListItem(taskListItem));
            }
        }

        return resultList;
    }


    public List<Integer> getIDs() {
        List<Integer> objectIDs = new ArrayList<>();
        for (MTaskListItem item : taskListItem) {
            objectIDs.add(item.getObjectID());
        }
        return objectIDs;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MTaskListItem> getTaskListItem() {
        return taskListItem;
    }

    public void setTaskListItem(List<MTaskListItem> taskListItem) {
        this.taskListItem = taskListItem;
    }

    @Override
    public Map<String, Object> getAsMap(String... ignoreFields) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(TOTAL_COUNT, getTotalCount());
        resultMap.put(ITEMS, getTaskListItem());
        return resultMap;
    }

    @Override
    public List<String> getFieldsName() {
        String[] fields = new String[]{TOTAL_COUNT, ITEMS};
        return Arrays.asList(fields);
    }
}
