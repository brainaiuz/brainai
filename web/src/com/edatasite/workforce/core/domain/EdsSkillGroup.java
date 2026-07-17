package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillGroupItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.CascadeType;
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
import java.util.Set;
import java.util.TreeSet;

/**
 * Skill Group Entity
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "skillgroup")
public class EdsSkillGroup extends EdsObject {

    private static final Logger log = LoggerFactory.getLogger(EdsSkillGroup.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localizeId")
    private EdsCustomFormLocalization localization;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "deleted")
    private Boolean deleted;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Where(clause = "deleted = 'false'")
    @JoinColumn(name = "groupid")
    private Set<EdsSkill> skills = new TreeSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentid")
    private EdsSkillGroup parent;

    // ------------------- Getters & Setters -------------------

    public Integer getObjectID() { return objectID; }

    public void setObjectID(Integer objectID) { this.objectID = objectID; }

    public String getName() {
        String result = "";
        if (getLocalization() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getLocalization().getNameLocalization(lang))) {
                result = getLocalization().getNameLocalization(lang);
            }
        }
        return (result != null && !result.isEmpty()) ? result : name;
    }

    public String getRealName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public Set<EdsSkill> getSkills() { return skills; }

    public void setSkills(Set<EdsSkill> skills) { this.skills = skills; }

    public Boolean getDeleted() { return deleted; }

    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public EdsCustomFormLocalization getLocalization() { return localization; }

    public void setLocalization(EdsCustomFormLocalization localization) { this.localization = localization; }

    public EdsSkillGroup getParent() { return parent; }

    public void setParent(EdsSkillGroup parent) { this.parent = parent; }

    public Integer getParentId() { return parent != null ? parent.getObjectID() : null; }

    // ------------------- RPC -------------------

    public SkillGroupItem getRpc() {
        SkillGroupItem skillGroupItem = new SkillGroupItem();
        skillGroupItem.setId(getObjectID());
        skillGroupItem.setName(getName());
        skillGroupItem.setCode(getCode());

        if (getParent() != null) {
            skillGroupItem.setParentId(getParent().getObjectID());
            skillGroupItem.setParentName(getParent().getName()); // <-- Parent name correctly set
        }

        if (getLocalization() != null) {
            skillGroupItem.setLocalization(getLocalization().getRPC());
            skillGroupItem.getLocalization().setLocalizedName(getName());
        }

        return skillGroupItem;
    }
}
