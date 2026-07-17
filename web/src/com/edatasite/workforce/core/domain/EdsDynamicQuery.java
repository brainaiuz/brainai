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


@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "dynamic_query")
public class EdsDynamicQuery extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "query_name")
    @Type(type = "text")
    private String query_name;

    @Column(name = "query_text")
    @Type(type = "text")
    private String query_text;

    public EdsDynamicQuery() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getQuery_name() {
        return query_name;
    }

    public void setQuery_name(String query_name) {
        this.query_name = query_name;
    }

    public String getQuery_text() {
        return query_text;
    }

    public void setQuery_text(String query_text) {
        this.query_text = query_text;
    }
}
