package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDepartmentTree;
import com.edatasite.workforce.gwt.core.client.rpc.ChartNode;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dilsh0d on 18.03.16.
 */
public interface DepartmentTreeManager extends Manager<EdsDepartmentTree>  {

    void addChildTree(Integer newChildId,Integer parentId);
    
    void removeSubtreeFromParents(Integer team_id);

    Integer getParent(Integer childId);

    SelectItem getParentItem(Integer childId);

    SelectItem getParentItemByChildId(Integer childId);

    List<Integer> getChildList(Integer parentId);

    List<Integer> getAllChildList(Integer parentId);

    List<Integer> getTreeTeamLeadersList(Integer childId);

    List<ChartNode> getTeamGraph(Integer parentId, Integer locationId);

    LinkedList<SelectItem> getDepartments(Integer parentId);

    List<EdsDepartmentTree> getDepartmentTreeByDepartmentID(Integer departmentId);

    List<Integer> getAncestorsAndChildren(Integer departmentId);

    Integer getRootParentByLocation(Integer locationID);

    List<Object[]> getFullSubtreeData(Integer rootId,  Integer rootLocationId,  Integer locationId);

    List<EdsDepartmentTree> getDirectSubtreeByParent(Integer parentId);

    List<Object[]> getDepartmentWithDirectChildren(Integer rootId);

}
