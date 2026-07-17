package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/31/11
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MProjectMemberList {

    List<MProjectMember> projectMember;

    public MProjectMemberList() {
    }

    public MProjectMemberList(ProjectMember[] projectMembers) {
        if (projectMembers != null) {
            this.projectMember = new ArrayList<>();

            for (ProjectMember projectMember : projectMembers) {
                this.projectMember.add(new MProjectMember(projectMember));
            }
        }
    }

    public List<MProjectMember> getProjectMember() {
        return projectMember;
    }

    public void setProjectMember(List<MProjectMember> projectMember) {
        this.projectMember = projectMember;
    }


}
