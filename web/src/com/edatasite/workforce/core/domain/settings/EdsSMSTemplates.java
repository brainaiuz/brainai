package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.profile.client.rpc.SMSTemplateItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Azazello on 4/20/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "smstemplate")
public class EdsSMSTemplates extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private EdsReference module;

    @Column(name = "content")
    @Type(type = "text")
    private String content;

    private Boolean isDefault = false;
    private Boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsReference getModule() {
        return module;
    }

    public void setModule(EdsReference module) {
        this.module = module;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public SMSTemplateItem getRPC(SMSTemplateItem item) {
        item = item == null ? new SMSTemplateItem() : item;
        item.setObjectID(getObjectID());
        item.setName(getName());
        item.setContent(getContent());
        if (getModule() != null) {
            item.setModuleID(getModule().getObjectID());
            item.setModuleName(getModule().getName());
            item.setModuleCode(getModule().getCode());
        }
        item.setDefault(isDefault());
        return item;
    }
}
