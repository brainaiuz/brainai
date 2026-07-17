package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Oct 22, 2009
 * Time: 4:18:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TeamEmployees implements IsSerializable {

    private WfmTreeItem team;
    private LinkedList<WfmTreeItem> members;

    public TeamEmployees() {

    }

    public TeamEmployees(WfmTreeItem team, LinkedList<WfmTreeItem> members) {
        this.team = team;
        this.members = members;
    }

    public WfmTreeItem getTeam() {
        return team;
    }

    public void setTeam(WfmTreeItem team) {
        this.team = team;
    }

    public LinkedList<WfmTreeItem> getMembers() {
        return members;
    }

    public void setMembers(LinkedList<WfmTreeItem> members) {
        this.members = members;
    }
}
