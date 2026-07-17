package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User : Akhror on 05/08/2021
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "extension_script")
public class EdsExtensionScript extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private String name;
    @Type(type = "text")
    private String script;
    @Column(name = "deleted")
    private Boolean deleted = false;

    public EdsExtensionScript() {
    }

    public EdsExtensionScript(Integer objectID, String name, String script, Boolean deleted) {
        this.objectID = objectID;
        this.name = name;
        this.script = script;
        this.deleted = deleted;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
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

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdsExtensionScript)) return false;
        if (!super.equals(o)) return false;

        EdsExtensionScript that = (EdsExtensionScript) o;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getScript() != null ? !getScript().equals(that.getScript()) : that.getScript() != null) return false;
        if (getDeleted() != null ? !getDeleted().equals(that.getDeleted()) : that.getDeleted() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getScript() != null ? getScript().hashCode() : 0);
        result = 31 * result + (getDeleted() != null ? getDeleted().hashCode() : 0);
        return result;
    }
}
