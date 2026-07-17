package com.finnetlimited.reportservice.core.client.module;

import com.edatasite.workforce.gwt.core.client.ArrayUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Jul 4, 2011
 * Time: 7:46:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingModuleSettings implements IsSerializable {
    private Boolean isShowButtons;
    private Boolean isShowSteps;
    private Integer customDashboardId;
    private Boolean isShowAllSteps;
    private ArrayList<String> activeSteps;
    private ArrayList<String> activePagers;
    private String firstStep;
    private Boolean isCustomise;
    private Boolean enableWFTListing;
    private int wftListLimit = -1;
    private boolean isWftListPanelShowPaging = true;

    private Boolean runFromUrl = false;

    public ReportingModuleSettings() {
        isShowButtons = true;
        isShowSteps = true;
        customDashboardId = 9;
        isShowSteps = true;
        isCustomise = false;
        isShowAllSteps = true;
        enableWFTListing = false;
        runFromUrl = false;
        activeSteps = new ArrayList<>();
        activeSteps.add("AddDataTypeReport");
        activeSteps.add("AddTypeReport");
        activeSteps.add("AddColumnsReport");
        activeSteps.add("AddSummariesReport");
        activeSteps.add("AddChartReport");
//        activeSteps.add("AddOrderReport");
        activeSteps.add("AddFilterReport");
        activeSteps.add("ReportList");
//        activeSteps.add("AddReportToDashboard");
        activeSteps.add("AddGroupingReport");
    }

    public Boolean getShowButtons() {
        return isShowButtons;
    }

    public void setShowButtons(Boolean showButtons) {
        isShowButtons = showButtons;
    }

    public Boolean getShowSteps() {
        return isShowSteps;
    }

    public void setShowSteps(Boolean showSteps) {
        isShowSteps = showSteps;
    }

    public Integer getCustomDashboardId() {
        return customDashboardId;
    }

    public void setCustomDashboardId(Integer customDashboardId) {
        this.customDashboardId = customDashboardId;
    }

    public Boolean getShowAllSteps() {
        return isShowAllSteps;
    }

    public ArrayList<String> getActiveSteps() {
        return activeSteps;
    }

    public void setActiveSteps(String activeStepsString) {
        if (!"".equals(activeStepsString)) {
            if (activeStepsString.toLowerCase().equals("all")) {
                isShowAllSteps = true;
            } else {
                isShowAllSteps = false;
                activeSteps = ArrayUtils.asList(activeStepsString.split(","));
            }
        }
    }


    public ArrayList<String> getActivePagers() {
        return activePagers;
    }

    public void setActivePagers(String activePagersString) {
        if (activePagersString.toLowerCase().equals("all") || "".equals(activePagersString)) {
            activePagers = new ArrayList<>();
            activePagers.add("top");
            activePagers.add("bottom");
        } else {
            isShowAllSteps = false;
            activePagers = ArrayUtils.asList(activePagersString.split(","));
        }

    }

    public String getFirstStep() {
        return firstStep;
    }

    public void setFirstStep(String firstStep) {
        this.firstStep = firstStep;
    }

    public Boolean getCustomise() {
        return isCustomise;
    }

    public void setCustomise(Boolean customise) {
        isCustomise = customise;
    }

    public Boolean enableWFTListing() {
        return enableWFTListing;
    }

    public void setEnableWFTListing(Boolean enableWFTListing) {
        this.enableWFTListing = enableWFTListing;
    }

    public Boolean getRunFromUrl() {
        return runFromUrl;
    }

    public void setRunFromUrl(Boolean runFromUrl) {
        this.runFromUrl = runFromUrl;
    }

    public int getWftListLimit() {
        return wftListLimit;
    }

    public void setWftListLimit(int wftListLimit) {
        this.wftListLimit = wftListLimit;
    }

    public boolean isWftListPanelShowPaging() {
        return isWftListPanelShowPaging;
    }

    public void setWftListPanelShowPaging(boolean wftListPanelShowPaging) {
        isWftListPanelShowPaging = wftListPanelShowPaging;
    }
}
