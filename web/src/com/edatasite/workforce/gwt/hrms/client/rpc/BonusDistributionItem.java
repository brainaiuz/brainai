package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.assessment.client.rpc.BonusSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/23/12
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class BonusDistributionItem implements IsSerializable {

    //---------------- distribution status ---------------//
    public static final String _BONUS_DISTRIBUTION_STATUS = "_BONUS_DISTRIBUTION_STATUS";
    public static final String NOT_DISTRIBUTED = "NOT_DISTRIBUTED";
    public static final String DISTRIBUTED = "DISTRIBUTED";
    public static final String REDISTRIBUTED = "REDISTRIBUTED";

    //---------------- bonus distribution approval status ---------------//
    public static final String _BONUS_DISTRIBUTION_APPROVAL_STATUS = "_BONUS_DISTRIBUTION_APPROVAL_STATUS";
    public static final String BONUS_DISTRIBUTION_DRAFT = "BONUS_DISTRIBUTION_DRAFT";
    public static final String BONUS_DISTRIBUTION_APPROVED = "BONUS_DISTRIBUTION_APPROVED";

    //Steps
    public enum BonusDistributionStep {
        STEP1("step1"),
        STEP2("step2"),
        STEP3("step3");

        private String stepName;

        BonusDistributionStep(String stepName) {
            this.stepName = stepName;
        }

        public String getStepName() {
            return stepName;
        }
    }

    private Integer objectId;
    private Integer validityPeriodId;
    private Integer departmentId;
    private String departmentName;
    private BonusSettingsItem bonusSettingsItem;
    private ArrayList<EligibleEmployeeItem> eligibleEmployeeItemList = new ArrayList<>();
    private ArrayList<Integer> selectedEmployeeIdList = new ArrayList<>();
    private SelectItem[] distributionStatusItems;
    private SelectItem distributionStatus;
    private Double remainingAmount = 0d;
    private BonusDistributionStep bonusDistributionStep;
    private String approvalStatus;
    private SelectItem validityPeriod;
    private Double companyBalance;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getValidityPeriodId() {
        return validityPeriodId;
    }

    public void setValidityPeriodId(Integer validityPeriodId) {
        this.validityPeriodId = validityPeriodId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public ArrayList<EligibleEmployeeItem> getEligibleEmployeeItemList() {
        return eligibleEmployeeItemList;
    }

    public void setEligibleEmployeeItemList(ArrayList<EligibleEmployeeItem> eligibleEmployeeItemList) {
        this.eligibleEmployeeItemList = eligibleEmployeeItemList;
    }

    public BonusSettingsItem getBonusSettingsItem() {
        return bonusSettingsItem;
    }

    public void setBonusSettingsItem(BonusSettingsItem bonusSettingsItem) {
        this.bonusSettingsItem = bonusSettingsItem;
    }

    public SelectItem[] getDistributionStatusItems() {
        return distributionStatusItems;
    }

    public void setDistributionStatusItems(SelectItem[] distributionStatusItems) {
        this.distributionStatusItems = distributionStatusItems;
    }

    public SelectItem getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(SelectItem distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public SelectItem getDistributionStatusItemByCode(String code) {
        for (SelectItem status : distributionStatusItems) {
            if (status.getDescription().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BonusDistributionStep getBonusDistributionStep() {
        if (bonusDistributionStep == null) {
            bonusDistributionStep = BonusDistributionStep.STEP1;
        }
        return bonusDistributionStep;
    }

    public void setBonusDistributionStep(BonusDistributionStep bonusDistributionStep) {
        this.bonusDistributionStep = bonusDistributionStep;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public ArrayList<Integer> getSelectedEmployeeIdList() {
        return selectedEmployeeIdList;
    }

    public void setSelectedEmployeeIdList(ArrayList<Integer> selectedEmployeeIdList) {
        this.selectedEmployeeIdList = selectedEmployeeIdList;
    }

    public SelectItem getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(SelectItem validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Double getCompanyBalance() {
        return companyBalance;
    }

    public void setCompanyBalance(Double companyBalance) {
        this.companyBalance = companyBalance;
    }
}
