package com.example.ejob.data.model;

public class Skill {
    String skillId;
    String skillName;
    String industryName;

    public Skill() {

    }

    public Skill(String skillId, String skillName, String industryName) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.industryName = industryName;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
}
