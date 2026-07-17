package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.customfields.EdsCourseCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Feb 13, 2009
 * Time: 12:03:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "course")
public class EdsCourse extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    @Type(type = "text")
    private String name;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    private String aliasName;

    private Integer validity;

    private Integer duration;

    @Type(type = "text")
    private String otherPreRequisites;

    private boolean isExamRequired = true;

    private boolean OPITO = false;

    private boolean medicalClearance = false;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subjectId")
    private EdsCourseSubject subject;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    private Boolean deleted = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "course_instructor",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "instructor_id")})
    private List<EdsEmployee> instructors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "course_preRequisites",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "preRequisite_id")})
    private List<EdsCourse> preRequisite = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "courserequirements",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "reservationcategory_id")})
    private List<EdsReference> courseRequirements = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.LAZY)
    @Where(clause = "deleted = 'false'")
    @OrderBy("objectID")
    private List<EdsCoursePrice> coursePrices = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsCourseCustomFields customFields;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<EdsCourse> getPreRequisite() {
        return preRequisite != null ? preRequisite : new ArrayList<>();
    }

    public String getPreRequisiteAsString() {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        if (preRequisite != null && preRequisite.size() > 0) {
            for (EdsCourse course : preRequisite) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(", ");
                }

                builder.append(course.getIntNumber() != null ? course.getIntNumber() : "").append(" ").append(course.getName());
            }

            return builder.toString();
        }
        return null;
    }

    public void setPreRequisite(List<EdsCourse> preRequisite) {
        this.preRequisite = preRequisite;
    }

    public boolean isExamRequired() {
        return isExamRequired;
    }

    public void setExamRequired(boolean examRequired) {
        isExamRequired = examRequired;
    }

    public List<EdsEmployee> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<EdsEmployee> instructors) {
        this.instructors = instructors;
    }

    public boolean isOPITO() {
        return OPITO;
    }

    public void setOPITO(boolean OPITO) {
        this.OPITO = OPITO;
    }

    public boolean isMedicalClearance() {
        return medicalClearance;
    }

    public void setMedicalClearance(boolean medicalClearance) {
        this.medicalClearance = medicalClearance;
    }

    public List<EdsReference> getCourseRequirements() {
        return courseRequirements;
    }

    public void setCourseRequirements(List<EdsReference> reservationCategories) {
        this.courseRequirements = reservationCategories;
    }

    public List<EdsCoursePrice> getCoursePrices() {
        return coursePrices;
    }

    public void addCoursePrices(EdsCoursePrice coursePrice) {
        getCoursePrices().add(coursePrice);
    }

    public void setCoursePrices(List<EdsCoursePrice> coursePrices) {
        this.coursePrices = coursePrices;
    }

    public String getOtherPreRequisites() {
        return otherPreRequisites;
    }

    public void setOtherPreRequisites(String otherPreRequisites) {
        this.otherPreRequisites = otherPreRequisites;
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

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public EdsCourseSubject getSubject() {
        return subject;
    }

    public void setSubject(EdsCourseSubject subject) {
        this.subject = subject;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsCourseCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCourseCustomFields customFields) {
        this.customFields = customFields;
    }

    public CourseItem getRPC() {
        CourseItem item = new CourseItem();
        item.setObjectID(getObjectID());
        if (getSubject() != null && !getSubject().getDeleted()) {
            String subjectName = null;
            if (getSubject().getParent() != null) {
                subjectName = getSubject().getParent().getName() + " ";
                subjectName += getSubject().getName();
            } else {
                subjectName = getSubject().getName();
            }


            item.setSubject(new SelectItem(getSubject().getObjectID(), subjectName));
        }
        item.setNumberData(new NumberData(getNumber(), getIntNumber()));
        item.setCourseName(getName());
        item.setDescription(getDescription());
        item.setDuration(getDuration());
        item.setValidity(getValidity());
        item.setOtherPreRequisites(getOtherPreRequisites());

        HashMap<Integer, BigDecimal> coursePerLocationPrices = new HashMap<>();
        HashMap<Integer, BigDecimal> coursePerLocationStopFees = new HashMap<>();
        if (getCoursePrices() != null && getCoursePrices().size() > 0) {
            for (EdsCoursePrice coursePrice : getCoursePrices()) {
                EdsLocation c = coursePrice.getLocation();
                if (!coursePerLocationPrices.containsKey(c.getObjectID())) {
                    coursePerLocationPrices.put(c.getObjectID(), coursePrice.getPrice());
                    coursePerLocationStopFees.put(c.getObjectID(), coursePrice.getStopFee());
                }
            }
        }
        item.setPricePerLocationStudent(coursePerLocationPrices);
        item.setStopFeePerLocationStudent(coursePerLocationStopFees);

        if (getPreRequisite() != null) {
            ArrayList<SelectItem> items = new ArrayList<>();
            for (EdsCourse course : getPreRequisite()) {
                items.add(new SelectItem(course.getObjectID(), course.getName()));
            }
            item.setPreRequisite(items.toArray(new SelectItem[]{}));
        }

        ArrayList<SelectItem> rcList = new ArrayList<>();
        for (EdsReference rc : getCourseRequirements()) {
            rcList.add(new SelectItem(rc.getObjectID(), rc.getName()));
        }
        item.setCourseRequirements(rcList.toArray(new SelectItem[]{}));

        return item;
    }
}
