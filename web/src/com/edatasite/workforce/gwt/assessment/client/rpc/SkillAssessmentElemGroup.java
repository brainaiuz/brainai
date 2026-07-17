package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SkillAssessmentElemGroup implements IsSerializable {
    private Integer skillGroupId;
    private String skillGroupName;
    private SkillAssessmentElem[] skillRatingElems;
//	private List skillAssessmentElemsList = new ArrayList();

    public SkillAssessmentElemGroup() {

    }

    public String getSkillGroupName() {
        return skillGroupName;
    }

    public void setSkillGroupName(String skillGroupName) {
        this.skillGroupName = skillGroupName;
    }

    public SkillAssessmentElem[] getSkillRatingElems() {
        return skillRatingElems;
    }

    public void setSkillRatingElems(SkillAssessmentElem[] skillRatingElems) {
        this.skillRatingElems = skillRatingElems;
    }

    public Integer getSkillGroupId() {
        return skillGroupId;
    }

    public void setSkillGroupId(Integer skillGroupId) {
        this.skillGroupId = skillGroupId;
    }

//	public void addSkillAssessmentElem(SkillAssessmentElem elem){
//		skillAssessmentElemsList.add(elem);
//	}
//	public List getSkillAssessmentElemsList() {
//		return skillAssessmentElemsList;
//	}
}
