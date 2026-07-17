package com.edatasite.workforce.core.domain.webhook;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.workflow.WebHookParameterType;

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
 * Date : 04.03.2025
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "web_hook_parameter")
public class EdsPublicWebHookParameter extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "web_hook_id")
    private EdsPublicWebHook webHook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private EdsPublicWebHookBody webHookBody;

    @Enumerated(EnumType.STRING)
    private WebHookParameterType type;

    public EdsPublicWebHookParameter() {
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

    public EdsPublicWebHook getWebHook() {
        return webHook;
    }

    public void setWebHook(EdsPublicWebHook webHook) {
        this.webHook = webHook;
    }

    public EdsPublicWebHookBody getWebHookBody() {
        return webHookBody;
    }

    public void setWebHookBody(EdsPublicWebHookBody webHookBody) {
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
        if (!(o instanceof EdsPublicWebHookParameter that)) return false;
        if (!super.equals(o)) return false;

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
