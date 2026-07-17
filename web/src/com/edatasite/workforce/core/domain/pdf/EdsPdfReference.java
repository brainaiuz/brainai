package com.edatasite.workforce.core.domain.pdf;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.10.2010
 * Time: 17:19:30
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "pdfreference")
public class EdsPdfReference extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
