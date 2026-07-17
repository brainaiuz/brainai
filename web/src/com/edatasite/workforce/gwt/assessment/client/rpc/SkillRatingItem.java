package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SkillRatingItem implements IsSerializable {

    private boolean rateable;
    private String skillDescription;
    private Integer skillId;
    private String skillName;
    private Integer skillRatingId;
	private Double skillWeight;

    public boolean isRateable() {
        return rateable;
    }

    public void setRateable(boolean rateable) {
        this.rateable = rateable;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getSkillRatingId() {
        return skillRatingId;
    }

    public void setSkillRatingId(Integer skillRatingId) {
        this.skillRatingId = skillRatingId;
    }

	public Double getSkillWeight() {
		return skillWeight;
	}

	public void setSkillWeight(Double skillWeight) {
		this.skillWeight = skillWeight;
	}
}
