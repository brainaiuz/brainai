package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Sher
 * Date: 7/24/12
 * Time: 4:05 PM
 */
public class AppraisalsSettingsItem implements IsSerializable {

    private Integer objectId;
    private ArrayList<String> reviewers = new ArrayList<>();
    private boolean useCompetencies = false;
    private boolean useGoals = false;
    private boolean employeeRate = false;
    private double fromScale;
    private double toScale;
    private double stepSize;
    private HashMap<BigDecimal, String> customRates = new HashMap<>();
    private boolean customRateEnable;
    private ArrayList<AppraisalsScoreTypeItem> scoreTypeItems = new ArrayList<>();

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public ArrayList<String> getReviewers() {
        return reviewers;
    }

    public void setReviewers(ArrayList<String> reviewers) {
        this.reviewers = reviewers;
    }

    public void addReviewer(String roleCode) {
        if (!getReviewers().contains(roleCode)) {
            getReviewers().add(roleCode);
        }
    }

    public boolean isUseCompetencies() {
        return useCompetencies;
    }

    public void setUseCompetencies(boolean useCompetencies) {
        this.useCompetencies = useCompetencies;
    }

    public boolean isUseGoals() {
        return useGoals;
    }

    public void setUseGoals(boolean useGoals) {
        this.useGoals = useGoals;
    }

    public double getFromScale() {
        return fromScale;
    }

    public void setFromScale(double fromScale) {
        this.fromScale = fromScale;
    }

    public double getToScale() {
        return toScale;
    }

    public void setToScale(double toScale) {
        this.toScale = toScale;
    }

    public double getStepSize() {
        return stepSize;
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

    public HashMap<BigDecimal, String> getCustomRates() {
        return customRates;
    }

    public void setCustomRates(HashMap<BigDecimal, String> customRates) {
        this.customRates = customRates;
    }

    public boolean isCustomRateEnable() {
        return customRateEnable;
    }

    public void setCustomRateEnable(boolean customRateEnable) {
        this.customRateEnable = customRateEnable;
    }

    public boolean isEmployeeRate() {
        return employeeRate;
    }

    public void setEmployeeRate(boolean employeeRate) {
        this.employeeRate = employeeRate;
    }

    public ArrayList<AppraisalsScoreTypeItem> getScoreTypeItems() {
        return scoreTypeItems;
    }

    public void setScoreTypeItems(ArrayList<AppraisalsScoreTypeItem> scoreTypeItems) {
        this.scoreTypeItems = scoreTypeItems;
    }
}
