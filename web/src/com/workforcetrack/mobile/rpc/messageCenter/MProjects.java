package com.workforcetrack.mobile.rpc.messageCenter;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 14.09.11
 * Time: 11:00
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MProjects {

    private List<MSelectItem> project;

    public MProjects() {

    }

    public MProjects(SelectItem[] projectItems) {
        project = WebServiceUtils.getAsMSelectItemList(projectItems);
    }

    public List<MSelectItem> getProject() {
        return project;
    }

    public void setProject(List<MSelectItem> project) {
        this.project = project;
    }
}
