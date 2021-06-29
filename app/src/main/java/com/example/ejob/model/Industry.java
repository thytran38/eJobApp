package com.example.ejob.model;

public class Industry {
    String industryId;;
    String industryName;

    public Industry() {

    }

    public Industry(String industryId, String industryName) {
        this.industryId = industryId;
        this.industryName = industryName;
    }

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }
}
