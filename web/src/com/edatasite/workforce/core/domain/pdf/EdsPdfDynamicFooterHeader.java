package com.edatasite.workforce.core.domain.pdf;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "dynamic_footer_header")
public class EdsPdfDynamicFooterHeader extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    @Type(type = "text")
    private String value;

    @Column(name = "enable")
    private Boolean isEnable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "templateid")
    private EdsPdfTemplateSettings template;

    @Override
    public Integer getObjectID() {
        return null;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EdsPdfTemplateSettings getTemplate() {
        return template;
    }

    public void setTemplate(EdsPdfTemplateSettings template) {
        this.template = template;
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }



}
