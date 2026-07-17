package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Nov-2010
 * Time: 21:32:42
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "addviewsettings")
public class EdsAddViewSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "addTaskViewJson", length = 2048)
    private String addTaskViewJson;

    @Column(name = "addMultiTaskViewJson", length = 2048)
    private String addMultiTaskViewJson;

    @Column(name = "addProjectViewJson", length = 2048)
    private String addProjectViewJson;

    @Column(name = "addContactViewJson", length = 2048)
    private String addContactViewJson;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getAddTaskViewJson() {
        return addTaskViewJson;
    }

    public void setAddTaskViewJson(String addTaskViewJson) {
        this.addTaskViewJson = addTaskViewJson;
    }

    public String getAddMultiTaskViewJson() {
        return addMultiTaskViewJson;
    }

    public void setAddMultiTaskViewJson(String addMultiTaskViewJson) {
        this.addMultiTaskViewJson = addMultiTaskViewJson;
    }

    public String getAddProjectViewJson() {
        return addProjectViewJson;
    }

    public void setAddProjectViewJson(String addProjectViewJson) {
        this.addProjectViewJson = addProjectViewJson;
    }

    public String getAddContactViewJson() {
        return addContactViewJson;
    }

    public void setAddContactViewJson(String addContactViewJson) {
        this.addContactViewJson = addContactViewJson;
    }

    /**
     * Saved Json Data Parse To List<View Column Code>
     *
     * @param viewAddFields
     * @return
     */
    public List<String> getAddViewFieldsPosition(ViewAddFiledsCodeName viewAddFields) {
        if (ViewAddFiledsCodeName.TaskAdd.equals(viewAddFields)) {
            return WfmJsonUtils.jsonDataConvertToCollectionList(getAddTaskViewJson());
        } else if (ViewAddFiledsCodeName.MultiTaskAdd.equals(viewAddFields)) {
            return WfmJsonUtils.jsonDataConvertToCollectionList(getAddMultiTaskViewJson());
        } else if (ViewAddFiledsCodeName.ProjectAdd.equals(viewAddFields)) {
            return WfmJsonUtils.jsonDataConvertToCollectionList(getAddProjectViewJson());
        } else if (ViewAddFiledsCodeName.ContactAdd.equals(viewAddFields)) {
            return WfmJsonUtils.jsonDataConvertToCollectionList(getAddContactViewJson());
        }
        return null;
    }

    /**
     * List<View Column Code> saved to json data
     *
     * @param viewAddFields
     * @param onlyViewShowfieldCodeName
     */
    public void saveFieldParams(ViewAddFiledsCodeName viewAddFields, List<String> onlyViewShowfieldCodeName) {
        if (ViewAddFiledsCodeName.TaskAdd.equals(viewAddFields)) {
            setAddTaskViewJson(WfmJsonUtils.collectionListConvertToJsonData(onlyViewShowfieldCodeName));
        } else if (ViewAddFiledsCodeName.MultiTaskAdd.equals(viewAddFields)) {
            setAddMultiTaskViewJson(WfmJsonUtils.collectionListConvertToJsonData(onlyViewShowfieldCodeName));
        } else if (ViewAddFiledsCodeName.ProjectAdd.equals(viewAddFields)) {
            setAddProjectViewJson(WfmJsonUtils.collectionListConvertToJsonData(onlyViewShowfieldCodeName));
        } else if (ViewAddFiledsCodeName.ContactAdd.equals(viewAddFields)) {
            setAddContactViewJson(WfmJsonUtils.collectionListConvertToJsonData(onlyViewShowfieldCodeName));
        }
    }
}
