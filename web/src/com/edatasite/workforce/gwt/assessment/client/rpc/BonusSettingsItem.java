package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * User: Sher
 * Date: 8/4/12
 * Time: 2:07 PM
 */
public class BonusSettingsItem implements IsSerializable {

    //---------------- bonus settings status ---------------//
    public static final String _BONUS_SETTINGS_STATUS = "_BONUS_SETTINGS_STATUS";
    public static final String BONUS_SETTINGS_SUBMIT_FOR_APPROVE = "BONUS_SETTINGS_SUBMIT_FOR_APPROVE";
    public static final String BONUS_SETTINGS_APPROVED = "BONUS_SETTINGS_APPROVED";
    public static final String BONUS_SETTINGS_REJECTED = "BONUS_SETTINGS_REJECTED";
    public static final String BONUS_SETTINGS_DRAFT = "BONUS_SETTINGS_DRAFT";

    private Integer objectId;
    private SelectItem validityPeriod;
    private String budgetId;
    private Double budgetAmount;
    private SelectItem status;
    private String statusCode;
    private HashMap<String, ScoreItem> scoreItemHashMap = new HashMap<>();
    /**
     * HR Manager can enable/disable forced distribution ranking percentage calculation in settings.
     * When the forced distribution ranking is enabled: Department Head should not be able to send Period Appraisal to HR for approval if scores are not in the forced distribution ranking percentage.
     * When forced distribution ranking is disabled: Department head can send the period appraisal for approval if scores are not in the range of forced distribution ranking percentages.
     */
    private boolean enableForcedDistributionRanking = false;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public SelectItem getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(SelectItem validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public Double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public HashMap<String, ScoreItem> getScoreItemHashMap() {
        return scoreItemHashMap;
    }

    public void setScoreItemHashMap(HashMap<String, ScoreItem> scoreItemHashMap) {
        this.scoreItemHashMap = scoreItemHashMap;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isEnableForcedDistributionRanking() {
        return enableForcedDistributionRanking;
    }

    public void setEnableForcedDistributionRanking(boolean enableForcedDistributionRanking) {
        this.enableForcedDistributionRanking = enableForcedDistributionRanking;
    }

    private Double getMaxScore() {
        Double maxScore = 0d;
        for (ScoreItem scoreItem : scoreItemHashMap.values()) {
            if (maxScore < scoreItem.getToScore().doubleValue()) {
                maxScore = scoreItem.getToScore().doubleValue();
            }
        }
        return maxScore;
    }

    public ScoreItem getScoreItem(double average) {
        double maxScore = getMaxScore();
        long rounded = Math.round(average);
        if (rounded > maxScore) {
            rounded = (long) maxScore;
        }
        for (ScoreItem scoreItem : getScoreItemHashMap().values()) {
            if (rounded >= scoreItem.getFromScore().doubleValue() && rounded <= scoreItem.getToScore().doubleValue()) {
                return scoreItem;
            }
        }
        return null;
    }
}