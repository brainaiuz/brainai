package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.enums.ContextCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "context", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
public class EdsContext extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private ContextCode code;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public ContextCode getContextType() {
        return code;
    }

    public void setContextType(ContextCode contextType) {
        this.code = contextType;
    }

}
