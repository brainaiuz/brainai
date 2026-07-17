package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "kanbanitemsettings")
public class EdsKanbanItemSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "settingsJSONData")
    @Type(type = "text")
    private String settingsJSONData;

    @Override
    public Integer getObjectID() {
        return null;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSettingsJSONData() {
        return settingsJSONData;
    }

    public void setSettingsJSONData(String settingsJSONData) {
        this.settingsJSONData = settingsJSONData;
    }


    /*
    * {
        "code":"TASK_NAME", "title":"Name", "selected":true, "relatedField":null
    },
    *
    *
    *
    * */


    /*[

    {
        "code":"NAME", "title":"Name", "width":25, "selected":true, "required":false, "disabled":false, "changed":
        false, "order":0
    },

    {
        "code":"DESCRIPTION", "title":"Description", "width":35, "selected":true, "required":false, "disabled":
        false, "changed":false, "order":1
    },

    {
        "code":"CREATED_DATE", "title":"Created Date", "width":10, "selected":true, "required":false, "disabled":
        false, "changed":false, "order":2
    },

    {
        "code":"FILE_SIZE", "title":"File Size", "width":10, "selected":true, "required":false, "disabled":
        false, "changed":false, "order":3
    },

    {
        "code":"DOWNLOAD", "title":"Download", "width":10, "selected":true, "required":false, "disabled":
        false, "changed":false, "order":4
    },

    {
        "code":"REMOVE", "title":"Remove", "width":10, "selected":true, "required":false, "disabled":false, "changed":
        false, "order":5
    }]*/
}
