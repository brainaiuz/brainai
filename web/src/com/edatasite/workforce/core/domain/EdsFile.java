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
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Feb 13, 2009
 * Time: 12:12:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "file")
public class EdsFile extends EdsObject {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "courseid")
    private EdsCourse cource;

    @ManyToOne
    @JoinColumn(name = "fileid")
    private EdsLesson lesson;// = new HashSet<EdsLesson>();

    public Integer getObjectID() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCourse getCource() {
        return cource;
    }

    public void setCource(EdsCourse cource) {
        this.cource = cource;
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

    public EdsLesson getLesson() {
        return lesson;
    }

    public void setLesson(EdsLesson lesson) {
        this.lesson = lesson;
    }
}
