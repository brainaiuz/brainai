package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.base.APIRepresentation;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/23/11
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "projectItem")
public class MProjectItem extends MSelectItem implements APIRepresentation {

    private boolean isManager = false;

    public MProjectItem() {

    }

    public MProjectItem(Integer objectID, String name) {
        super(objectID, name);
    }

    public MProjectItem(ProjectItem projectItem) {
        if (projectItem != null) {
            this.isManager = projectItem.isManager();
            this.setObjectID(projectItem.getId());
            this.setName(projectItem.getName());
            this.setDescription(projectItem.getDescription());
        }
    }

    public static boolean convert(ProjectItem projectItem, MProjectItem mProjectItem, boolean fromProjectItem) {
        if (projectItem == null || mProjectItem == null)
            return false;

        try {
            if (fromProjectItem) {
                mProjectItem.setObjectID(projectItem.getId());
                mProjectItem.setName(projectItem.getName());
                mProjectItem.setDescription(projectItem.getDescription());
                mProjectItem.setManager(projectItem.isManager());
            } else {

                projectItem.setId(mProjectItem.getObjectID());
                projectItem.setName(mProjectItem.getName());
                projectItem.setDescription(mProjectItem.getDescription());
                projectItem.setManager(mProjectItem.isManager());
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean isManager) {
        this.isManager = isManager;
    }

    @Override
    public Map<String, Object> getAsMap(String... ignoreFields) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(APIConstants.OBJECT_ID, getObjectID());
        resultMap.put(APIConstants.NAME, getName());
        resultMap.put(APIConstants.DESCRIPTION, getDescription());
        resultMap.put("manager", isManager);
        if (ignoreFields != null && ignoreFields.length > 0) {
            for (String ignoreField : ignoreFields) {
                if (ignoreField != null) {
                    resultMap.remove(ignoreField);
                }
            }
        }
        return resultMap;
    }

    @Override
    public List<String> getFieldsName() {
        String[] fieldsName = new String[]{"objectID", "name", "description", "manager"};
        return Arrays.asList(fieldsName);
    }
}
