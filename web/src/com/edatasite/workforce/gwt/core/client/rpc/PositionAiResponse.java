package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;

public class PositionAiResponse implements Serializable {
    private String salaryBasis;
    private String positionDescription;
    private String jobRequirements;
    private String responsibilities;
    private String measuringEmployeePerformance;
    private String personalQualities;
    private String knowledge;

    public PositionAiResponse() {
    }

    public String getSalaryBasis() {
        return salaryBasis;
    }

    public void setSalaryBasis(String salaryBasis) {
        this.salaryBasis = salaryBasis;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public String getJobRequirements() {
        return jobRequirements;
    }

    public void setJobRequirements(String jobRequirements) {
        this.jobRequirements = jobRequirements;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibility) {
        this.responsibilities = responsibility;
    }

    public String getMeasuringEmployeePerformance() {
        return measuringEmployeePerformance;
    }

    public void setMeasuringEmployeePerformance(String measuringEmployeePerformance) {
        this.measuringEmployeePerformance = measuringEmployeePerformance;
    }

    public String getPersonalQualities() {
        return personalQualities;
    }

    public void setPersonalQualities(String personalQualities) {
        this.personalQualities = personalQualities;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }
}
