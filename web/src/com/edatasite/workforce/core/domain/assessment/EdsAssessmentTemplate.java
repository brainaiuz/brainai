package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Iskandar
 * Date: 03-Aug-2007
 * Time: 17:56:57
 * To change this template use File | Settings | File Templates.
 */


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "assessmentTemplate")
public class EdsAssessmentTemplate extends EdsObject {

    public static final Integer DEFAULT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private EdsUser user;

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentTemplateId")
    @OrderBy
    private List<EdsAssessmentTemplateSkill> assessmentTemplateSkills = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "template_department",
            joinColumns = {@JoinColumn(name = "template_id")},
            inverseJoinColumns = {@JoinColumn(name = "department_id")})
    private Set<EdsDepartment> departments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private EdsEmployee owner;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void addAssessmentTemplateSkill(EdsAssessmentTemplateSkill assessmentTemplateSkill) {
        assessmentTemplateSkill.setAssessmentTemplate(this);
        getAssessmentTemplateSkills().add(assessmentTemplateSkill);
    }

    public void removeAssessmentTemplateSkill(EdsAssessmentTemplateSkill assessmentTemplateSkill) {
        assessmentTemplateSkill.setAssessmentTemplate(null);
        getAssessmentTemplateSkills().remove(assessmentTemplateSkill);
    }

    //initiate assessment strictly by template

    public EdsEmployeeAssessment createEmployeeAssessment(EdsEmployee employee) {
        EdsEmployeeAssessment employeeAssessment = new EdsEmployeeAssessment();
        employeeAssessment.setEmployee(employee);
        for (EdsAssessmentTemplateSkill assessmentTemplateSkill : getAssessmentTemplateSkills()) {
            EdsSkillRating skillRating = new EdsSkillRating();
            skillRating.setSkill(assessmentTemplateSkill.getSkill());
            skillRating.setShowSlider(assessmentTemplateSkill.getShowSlider());
            skillRating.setWeight(assessmentTemplateSkill.getWeight());
            employeeAssessment.getSkillAssessment().addSkillRating(skillRating);
        }
        return employeeAssessment;
    }

    public List<EdsAssessmentTemplateSkill> getAssessmentTemplateSkills() {
        return assessmentTemplateSkills;
    }

    public void setAssessmentTemplateSkills(
            List<EdsAssessmentTemplateSkill> assessmentTemplateSkills) {
        this.assessmentTemplateSkills = assessmentTemplateSkills;
    }

    @Column(name = "deleted")
    private Boolean deleted = false;

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<EdsDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<EdsDepartment> departments) {
        this.departments = departments;
    }

    public EdsEmployee getOwner() {
        return owner;
    }

    public void setOwner(EdsEmployee owner) {
        this.owner = owner;
    }
}
