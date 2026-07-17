package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;

import javax.persistence.Column;
import javax.persistence.Entity;
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
 * Date: Feb 19, 2009
 * Time: 10:57:56 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "lesson")
public class EdsLesson extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "fileid")
    private Set<EdsFile> files = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "courseid")
    private EdsCourse course;

    @Column(name = "lessonnumber")
    private Integer number;

    @Column(name = "deleted")
    private Boolean deleted = false;

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCourse getCourse() {
        return course;
    }

    public void setCourse(EdsCourse course) {
        this.course = course;
    }

    public Set<EdsFile> getFiles() {
        return files;
    }

    public void setFiles(Set<EdsFile> files) {
        this.files = files;
    }
}
