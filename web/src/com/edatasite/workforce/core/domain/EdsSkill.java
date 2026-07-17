package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplateSkill;
import com.edatasite.workforce.core.domain.assessment.EdsSkillRating;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: izaynutdinov
 * Date: 30.04.2007
 * Time: 9:14:02
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "skill")
public class EdsSkill extends EdsObject {

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
    @JoinColumn(name = "groupId")
    private EdsSkillGroup group;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "skill")
    private final List<EdsEmployeeSkills> employeeSkills = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "skill")
    private final List<EdsProfileSkill> profileSkills = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "skill")
    private final List<EdsAssessmentTemplateSkill> templateSkills = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "skill")
    private final List<EdsSkillRating> skillRatings = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "defaultWeight")
    private Double defaultWeight = 0d;

    @Column(name = "deleted")
    private Boolean deleted = false;

    private Date lastUpdateDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nameLocalizeId")
    private EdsCustomFormLocalization nameLocalize;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discriptionLocalizeId")
    private EdsCustomFormLocalization discriptionLocalize;

    public EdsSkillGroup getGroup() {
        return group;
    }

    public void setGroup(EdsSkillGroup group) {
        this.group = group;
    }

    public String getName() {
        String result = "";
        if (getNameLocalize() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getNameLocalize().getNameLocalization(lang))) {
                result = getNameLocalize().getNameLocalization(lang);
            }
        }
        return result != null && !result.isEmpty() ? result : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        String result = "";
        if (getDiscriptionLocalize() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getDiscriptionLocalize().getNameLocalization(lang))) {
                result = getDiscriptionLocalize().getNameLocalization(lang);
            }
        }
        return result != null && !result.isEmpty() ? result : description;
    }

    public String getRealName() {
        return name;
    }

    public String getRealDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDefaultWeight() {
        return defaultWeight;
    }

    public void setDefaultWeight(Double defaultWeight) {
        this.defaultWeight = defaultWeight;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<EdsEmployeeSkills> getEmployeeSkills() {
        return employeeSkills;
    }

    public List<EdsProfileSkill> getProfileSkills() {
        return profileSkills;
    }

    public List<EdsAssessmentTemplateSkill> getTemplateSkills() {
        return templateSkills;
    }

    public List<EdsSkillRating> getSkillRatings() {
        return skillRatings;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescriptionCode() {
        return code + "_DESCRIPTION";
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public EdsCustomFormLocalization getNameLocalize() {
        return nameLocalize;
    }

    public void setNameLocalize(EdsCustomFormLocalization nameLocalizeId) {
        this.nameLocalize = nameLocalizeId;
    }

    public EdsCustomFormLocalization getDiscriptionLocalize() {
        return discriptionLocalize;
    }

    public void setDiscriptionLocalize(EdsCustomFormLocalization discriptionLocalizeId) {
        this.discriptionLocalize = discriptionLocalizeId;
    }
}
