package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: izaynutdinov
 * Date: 09.07.2007
 * Time: 10:15:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ProfileSkill")
public class EdsProfileSkill extends EdsObject {

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
    @JoinColumn(name = "profileId")
    private EdsEmployeeProfile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skillId")
    private EdsSkill skill;

    public EdsEmployeeProfile getProfile() {
        return profile;
    }

    public void setProfile(EdsEmployeeProfile profile) {
        this.profile = profile;
    }

    public EdsSkill getSkill() {
        return skill;
    }

    public void setSkill(EdsSkill skill) {
        this.skill = skill;
    }

    public boolean equals(Object o) {
        if (!(o instanceof EdsProfileSkill)) {
            return false;
        }
        EdsProfileSkill profileSkill = (EdsProfileSkill) o;
        return getProfile().equals(profileSkill.getProfile()) && getSkill().equals(profileSkill.getSkill());
    }

    public int hashCode() {
        return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
