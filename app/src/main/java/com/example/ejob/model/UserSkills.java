package com.example.ejob.model;

import java.util.List;

public class UserSkills {
    String userId;
    List<Skill> skillList;

    public UserSkills() {
    }

    public UserSkills(String userId, List<Skill> skillList) {
        this.userId = userId;
        this.skillList = skillList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<Skill> skillList) {
        this.skillList = skillList;
    }
}
