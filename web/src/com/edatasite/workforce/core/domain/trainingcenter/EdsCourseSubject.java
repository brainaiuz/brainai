package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsCourseSubjectCustomFields;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 24.12.12
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "coursesubject")
public class EdsCourseSubject extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    @Type(type = "text")
    private String name;

    @Type(type = "text")
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private EdsCourseSubject parent;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "sorder")
    private Integer order = 0;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = "deleted = 'false'")
    private List<EdsCourseSubject> childList;


    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsCourseSubjectCustomFields customFields;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsCourseSubject getParent() {
        return parent;
    }

    public void setParent(EdsCourseSubject parent) {
        this.parent = parent;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<EdsCourseSubject> getChildList() {
        return childList;
    }

    public void setChildList(List<EdsCourseSubject> childList) {
        this.childList = childList;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
    public EdsCourseSubjectCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCourseSubjectCustomFields customFields) {
        this.customFields = customFields;
    }

}
