package com.edatasite.workforce.core.domain.workflow;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User : Akhror
 * Date : 17.01.2022
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "web_hook_response")
public class EdsWebHookResponse extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "response")
    @Type(type = "text")
    private String response;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "web_hook_id")
    private EdsWorkflowWebHook webHook;

    @Column(name = "created_date")
    private Date createdDate = new Date();

    @Column(name = "type")
    private String type;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "status")
    private String status;

    @Column(name = "body")
    @Type(type = "text")
    private String body;


    public EdsWebHookResponse() {
    }

    public EdsWebHookResponse(Integer objectID, String response, EdsWorkflowWebHook webHook, Date createdDate, String type, Integer typeId, EdsWorkflowRule workflow, String status) {
        this.objectID = objectID;
        this.response = response;
        this.webHook = webHook;
        this.createdDate = createdDate;
        this.type = type;
        this.typeId = typeId;
        this.status = status;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public EdsWorkflowWebHook getWebHook() {
        return webHook;
    }

    public void setWebHook(EdsWorkflowWebHook webHook) {
        this.webHook = webHook;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdsWebHookResponse)) return false;
        if (!super.equals(o)) return false;

        EdsWebHookResponse that = (EdsWebHookResponse) o;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getResponse() != null ? !getResponse().equals(that.getResponse()) : that.getResponse() != null)
            return false;
        if (getWebHook() != null ? !getWebHook().equals(that.getWebHook()) : that.getWebHook() != null) return false;
        return getCreatedDate() != null ? getCreatedDate().equals(that.getCreatedDate()) : that.getCreatedDate() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getResponse() != null ? getResponse().hashCode() : 0);
        result = 31 * result + (getWebHook() != null ? getWebHook().hashCode() : 0);
        result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EdsWebHookResponse{" +
                "objectID=" + objectID +
                ", response='" + response + '\'' +
                ", webHook=" + webHook +
                ", createdDate=" + createdDate +
                '}';
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
