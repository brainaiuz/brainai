package com.edatasite.workforce.gwt.assessment.server.struct;


public class SkillAsKey {
    public Integer skillId;
    public String skillName;
    public String skillDescription;
    public Double overalRate;
    public Integer keySkillRatingId;
    public String employeeComment;
    public String managerComment;
    public Double employeeRating;
    public Double managerRating;
    public Boolean rateable;

    public SkillAsKey(Integer skillId) {
        this.skillId = skillId;
    }

    public SkillAsKey(Integer skillId, String skillName, String skillDescription) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.skillDescription = skillDescription;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SkillAsKey other)) {
            return false;
        }
        return skillId.equals(other.skillId);
    }

    public int hashCode() {
        return skillId;
    }

}

