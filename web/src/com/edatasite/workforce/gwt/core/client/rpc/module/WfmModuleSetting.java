package com.edatasite.workforce.gwt.core.client.rpc.module;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.HashSet;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 10-May-2011
 * Time: 20:27:15
 */
public class WfmModuleSetting implements IsSerializable {

    private String rootId;
    private String width;
    private String height;
    private String moduleName;
    private String moduleStyle;
    private int wftListLimit = -1;
    private boolean isWftListPanelShowPaging = true;
    private boolean isCustomise = false;
    private boolean showHeader = true;
    private boolean showAllContainer = true;
    private boolean isRequestParam = false;
    private boolean isCloseWindow = false;
    private boolean isShowButtons = true;
    private boolean isShowSteps = true;

    private String activeSteps = "";
    private String activePagers = "";
    private Integer customDashboardId = 0;
    private String firstStep;
    private HashSet<String> historyProccessorName;
    private HashMap<String, String> params;
    private HashMap<String, WfmContainer> containers;

    private boolean enableWFTListing = false;

    public String getRootId() {
        if (rootId == null && !isRequestParam) {
            rootId = "main";
        }
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getWidth() {
        if (width == null) {
            width = "100%";
        }
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        if (height == null) {
            height = "100%";
        }
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setModuleStyle(String moduleStyle) {
        this.moduleStyle = moduleStyle;
    }

    public String getModuleStyle() {
        return moduleStyle;
    }

    public int getWftListLimit() {
        return wftListLimit;
    }

    public void setWftListLimit(String wftListLimit) {
        this.wftListLimit = wftListLimit != null && !"".equals(wftListLimit) ? Integer.parseInt(wftListLimit) : -1;
    }

    public boolean isWftListPanelShowPaging() {
        return isWftListPanelShowPaging;
    }

    public void setWftListPanelShowPaging(String wftListPanelShowPaging) {
        isWftListPanelShowPaging = wftListPanelShowPaging != null && !"".equals(wftListPanelShowPaging) ? Boolean.valueOf(wftListPanelShowPaging) : true;
    }

    public boolean isCustomise() {
        return isCustomise;
    }

    public void setCustomise(boolean customise) {
        isCustomise = customise;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(String showHeader) {
        this.showHeader = Boolean.valueOf(showHeader);
    }

    public boolean isShowAllContainer() {
        return showAllContainer;
    }

    public void setShowAllContainer(String showAllContainer) {
        this.showAllContainer = Boolean.valueOf(showAllContainer);
    }

    public boolean isRequestParam() {
        return isRequestParam;
    }

    public void setRequestParam(boolean requestParam) {
        isRequestParam = requestParam;
    }

    public boolean isCloseBrowserWindow() {
        return isCloseWindow;
    }

    public void setCloseWindow(Boolean closeWindow) {
        isCloseWindow = closeWindow;
    }

    public boolean isShowButtons() {
        return isShowButtons;
    }

    public void setShowButtons(String showButtons) {
        isShowButtons = Boolean.valueOf(showButtons);
    }

    public boolean isShowSteps() {
        return isShowSteps;
    }

    public void setShowSteps(String showSteps) {
        isShowSteps = Boolean.valueOf(showSteps);
    }

    public Integer getCustomDashboardId() {
        return customDashboardId;
    }

    public void setCustomDashboardId(String customDashboardId) {
        this.customDashboardId = Integer.valueOf(customDashboardId);
    }

    public String getFirstStep() {
        return firstStep;
    }

    public void setFirstStep(String firstStep) {
        this.firstStep = firstStep;
    }

    public String getActiveSteps() {
        return activeSteps;
    }

    public void setActiveSteps(String activeSteps) {
        this.activeSteps = activeSteps;
    }

    public String getActivePagers() {
        return activePagers;
    }

    public void setActivePagers(String activePagers) {
        this.activePagers = activePagers;
    }

    public HashSet<String> getHistoryProccessorName() {
        if (historyProccessorName == null) {
            historyProccessorName = new HashSet<>();
        }
        return historyProccessorName;
    }

    public void setHistoryProccessorName(HashSet<String> historyProccessorName) {
        this.historyProccessorName = historyProccessorName;
    }

    public HashMap<String, String> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public HashMap<String, WfmContainer> getContainers() {
        if (containers == null) {
            containers = new HashMap<>();
        }
        return containers;
    }

    public void setContainers(HashMap<String, WfmContainer> containers) {
        this.containers = containers;
    }

    public boolean enableWFTListing() {
        return enableWFTListing;
    }

    public void setEnableWFTListing(String enableWFTListing) {
        this.enableWFTListing = Boolean.valueOf(enableWFTListing);
    }

    /**
     * <i>... This is method checked add this container to root panel ...</i>
     * <br/>
     * <i>... Write by Developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created Date {19:46 11/05/2011} ...</i>
     * <br/>
     *
     * @param containerName
     * @return
     */
    public boolean isAddContainer(String containerName) {
        return !isCustomise || showAllContainer || getContainers().containsKey(containerName);
    }

    /**
     * <i>... This is method checked add this view to container ...</i>
     * <br/>
     * <i>... Write by Developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created Date {19:47 11/05/2011} ...</i>
     * <br/>
     *
     * @param containerName
     * @param viewName
     * @return
     */
    public boolean isAddView(String containerName, String viewName, Integer id) {
        if (isCustomise) {
            if (showAllContainer) {
                return true;
            } else if (getContainers().containsKey(containerName)) {
                return getContainers().get(containerName).isShowAllView() || getContainers().get(containerName).getSectionHistoryName().contains(viewName);
            } else if (getHistoryProccessorName().contains(containerName)) {
                if (isRequestParam) {
                    String replaceConName = replaceContainerName(containerName, id);
                    return getContainers().containsKey(replaceConName)
                            && (getContainers().get(replaceConName).isShowAllView() || getContainers().get(replaceConName).getSectionHistoryName().contains(viewName));
                }
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * <i>... This is method checked show left menu or not show ...</i>
     * <br/>
     * <i>... Write by Developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created Date {19:48 11/05/2011} ...</i>
     * <br/>
     *
     * @param containerName
     * @param id
     * @return
     */
    public boolean isShowContainerLeftMenu(String containerName, Integer id) {
        if (isCustomise) {
            String replaceConName = replaceContainerName(containerName, id);
            if (getContainers().containsKey(replaceConName)) {
                WfmContainer container = getContainers().get(replaceConName);
                if (container.isShowLeftMenu()) {
                    return true;
                } else {
                    if (container.getSectionHistoryName().size() == 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * <i>... This is method replace container name add id with space ...</i>
     * <br/>
     * <i>... Write by Developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created Date {22:18 22/06/2011} ...</i>
     * <br/>
     *
     * @param containerName
     * @param id
     * @return
     */
    private String replaceContainerName(String containerName, Integer id) {
        if (id != null) {
            return containerName.replaceAll(id.toString(), "");
        }
        return containerName;
    }


}
