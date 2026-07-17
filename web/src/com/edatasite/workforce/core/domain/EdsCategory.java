package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Feb 13, 2009
 * Time: 12:09:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ecategory")
public class EdsCategory extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "categoryid")
    private Set<EdsCourse> courses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private EdsCategory parent;

    @Column(name = "deleted")
    private Boolean deleted = false;

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getObjectID() {
        return objectID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Set<EdsCourse> getCourses() {
        return courses;
    }

    public void setCourses(Set<EdsCourse> courses) {
        this.courses = courses;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsCategory getParent() {
        return parent;
    }

    public void setParent(EdsCategory parent) {
        this.parent = parent;
    }
}
