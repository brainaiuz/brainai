package com.edatasite.workforce.gwt.employee.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 23.08.2009
 * Time: 15:53:02
 * To change this template use File | Settings | File Templates.
 */
public class TeamEmployee implements IsSerializable {

    private String teamNames;
    private Integer[] objectId;

    public String getTeamNames() {
        return teamNames;
    }

    public void setTeamNames(String teamNames) {
        this.teamNames = teamNames;
    }

    public Integer[] getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer[] objectId) {
        this.objectId = objectId;
    }
}
