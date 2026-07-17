package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.base.MFacetFilter;
import com.workforcetrack.mobile.rpc.base.MSelectItemList;
import com.workforcetrack.mobile.rpc.calendar.MTaskList;
import com.workforcetrack.mobile.rpc.calendar.MTaskListItem;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;
import com.workforcetrack.mobile.rpc.task.MPositionList;
import com.workforcetrack.mobile.rpc.task.MPriorityList;
import com.workforcetrack.mobile.rpc.task.MProjectItemList;
import com.workforcetrack.mobile.rpc.task.MTaskFilterData;
import com.workforcetrack.mobile.rpc.task.MTaskStatusList;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/23/11
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TaskWebService {

    MProjectItemList getProjects();

    MProjectItemList getUserProjects();

    MPriorityList getPriorities();

    MPositionList getAssigneesWithPositions(Integer projectID);

    // MPositionList getAssigneesWithPositions();

    MTaskStatusList getStatusList();

    MTaskFilterData getFilterData();

    MTaskFilterData getFilterData(MFacetFilter facetFilter);

    //CRUD
    MTaskList getList(MFilterParametrs mFilterParametrs);

    MTaskList getNewList(MFilterParametrs mFilterParametrs);

    MTaskListItem get(Integer objectID);

    MTaskListItem edit(Integer objectID);

    Integer save(MTaskListItem mTaskListItem);

    Integer saveWithReturnID(MTaskListItem item);

    Integer saveCrmTask(MTaskListItem item);

    Boolean delete(Integer objectID);

    Boolean deleteList(ArrayList<Integer> objectIDs);

    MTaskList getMKList(MFilterParametrs mFilterParametrs);

    MSelectItemList lookUp(MFilterParametrs fp);

    MNumberData generateTaskNumber(Integer projectID, Date date);

    MNumberData generateTaskNumber(Date date);
}
