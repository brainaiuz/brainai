package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User : Akhror
 * Date : 10.01.2022
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "web_hook_parameter")
public class EdsWebHookParameter extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "web_hook_id")
    private EdsWorkflowWebHook webHook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private EdsWebHookBody webHookBody;

    @Enumerated(EnumType.STRING)
    private WebHookParameterType type;

    public EdsWebHookParameter() {
    }

    public EdsWebHookParameter(Integer objectID, String name, String value, EdsWorkflowWebHook webHook, EdsWebHookBody webHookBody, WebHookParameterType type) {
        this.objectID = objectID;
        this.name = name;
        this.value = value;
        this.webHook = webHook;
        this.webHookBody = webHookBody;
        this.type = type;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EdsWorkflowWebHook getWebHook() {
        return webHook;
    }

    public void setWebHook(EdsWorkflowWebHook webHook) {
        this.webHook = webHook;
    }

    public EdsWebHookBody getWebHookBody() {
        return webHookBody;
    }

    public void setWebHookBody(EdsWebHookBody webHookBody) {
        this.webHookBody = webHookBody;
    }

    public WebHookParameterType getType() {
        return type;
    }

    public void setType(WebHookParameterType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdsWebHookParameter)) return false;
        if (!super.equals(o)) return false;

        EdsWebHookParameter that = (EdsWebHookParameter) o;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
        if (getWebHook() != null ? !getWebHook().equals(that.getWebHook()) : that.getWebHook() != null) return false;
        if (getWebHookBody() != null ? !getWebHookBody().equals(that.getWebHookBody()) : that.getWebHookBody() != null)
            return false;
        if (getType() != that.getType()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getWebHook() != null ? getWebHook().hashCode() : 0);
        result = 31 * result + (getWebHookBody() != null ? getWebHookBody().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EdsWebHookParameter{" +
                "objectID=" + objectID +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", webHook=" + webHook +
                ", webHookBody=" + webHookBody +
                ", type=" + type +
                '}';
    }
}
