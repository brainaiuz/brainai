package com.edatasite.workforce.gwt.project.server.app;

import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostAdjustService;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 28.11.2008
 * Time: 18:48:33
 * To change this template use File | Settings | File Templates.
 */
public class ProjectCostAdjustServiceImpl implements ProjectCostAdjustService {

    private WorkStreamManager workStreamManager;

    public void updateWorkstreamEstimatedTime(Integer workstreamId, Integer oldValue, Integer newValue) {

//
//          EdsWorkStream workstream = workStreamManager.get(workstreamId);
//                 task.setParentWS(workstream);
//                 workstream.updateEstimatedTime(taskEstimatedTime - task.getEstimatedTime());

    }

}
