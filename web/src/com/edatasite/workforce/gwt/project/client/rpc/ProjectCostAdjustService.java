package com.edatasite.workforce.gwt.project.client.rpc;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 29.11.2008
 * Time: 16:58:33
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectCostAdjustService {
    void updateWorkstreamEstimatedTime(Integer workstreamId, Integer oldValue, Integer newValue);
}
