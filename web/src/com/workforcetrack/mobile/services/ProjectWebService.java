package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.base.MFacetFilter;
import com.workforcetrack.mobile.rpc.base.MFilterData;
import com.workforcetrack.mobile.rpc.base.MSelectItemList;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;
import com.workforcetrack.mobile.rpc.project.MClientList;
import com.workforcetrack.mobile.rpc.project.MProjectList;
import com.workforcetrack.mobile.rpc.project.MProjectListItem;
import com.workforcetrack.mobile.rpc.project.MProjectMemberList;
import com.workforcetrack.mobile.rpc.project.MProjectStatusList;
import com.workforcetrack.mobile.rpc.project.MStatusList;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/31/11
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectWebService {

    MStatusList getProjectStatuses();

    MProjectStatusList getStatuses();

    MClientList getClients(Integer projectID);

    MClientList getClients(MFilterParametrs mFilterParametrs, Integer projectID);

    MClientList getClients(MFilterParametrs mFilterParametrs);

    MClientList getClients();

    MProjectMemberList getProjectEmployees(Integer objectID);

    MProjectMemberList getProjectEmployeesWithTeams(Integer objectID);

    MProjectMemberList getProjectEmployeesWithTeams();

    MFilterData getFilterData();

    MFilterData getFilterData(MFacetFilter facetFilter);

    //CRUD operations
    MProjectList getList(MFilterParametrs mFilterParametrs);

    MProjectListItem get(Integer objectID);

    MProjectListItem edit(Integer objectID);

    Integer save(MProjectListItem mProjectItem);

    Boolean delete(Integer objectID);


    MSelectItemList lookUp(MFilterParametrs fp);

    MNumberData generateProjectNumber(Date date, Integer clientID);

    MNumberData generateProjectNumber(Date date);

}
