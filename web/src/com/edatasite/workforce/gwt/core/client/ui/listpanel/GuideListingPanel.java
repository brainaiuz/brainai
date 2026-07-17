package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GuideListingPanel extends ListingPanel {
    private ListingGuidePanel listingGuidePanel;
    private boolean forceToShow;
    private ListPanelType panelType;
    private boolean showFlag = true;

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, ListingFilterParameter listingFilterParameter) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, listingFilterParameter);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, Integer customFieldCategoryID) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, customFieldCategoryID);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, Integer customFieldCategoryID, Integer stepID) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, customFieldCategoryID, stepID);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, ListingFilterParameter listingFilterParameter) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, listingFilterParameter);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, boolean hasAdditionalInformation) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, hasAdditionalInformation);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, boolean hasAdditionalInformation, boolean showCustomize) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, hasAdditionalInformation, showCustomize);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, boolean hasAdditionalInformation, boolean showCustomize, boolean showAddInFilter, boolean showFilters) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, hasAdditionalInformation, showCustomize, showAddInFilter, showFilters);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, int customPageSize) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, customPageSize);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, int customPageSize, boolean isShowFooter, Integer customFieldCategoryID, Integer stepID) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, customPageSize, isShowFooter, customFieldCategoryID, stepID);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, SelectionGrid.SelectionPolicy selectionPolicy, int customPageSize, boolean isShowFooter, Integer customFieldCategoryID, Integer stepID, boolean hasKanban) {
        super(type, null, columnConfigs, listingRequestProvider, listingPanelDesign, selectionPolicy, customPageSize, isShowFooter, customFieldCategoryID, stepID, hasKanban);
        this.panelType = type;
    }

    public GuideListingPanel(ListPanelType type, CustomColumnDefinitionConfig[] columnConfigs, ListingRequestProvider listingRequestProvider, GuideListingPanelDesign listingPanelDesign, boolean showCustomize, boolean showAddInFilter, boolean showFilters) {
        super(type, columnConfigs, listingRequestProvider, listingPanelDesign, showCustomize, showAddInFilter, showFilters);
        this.panelType = type;
    }

    @Override
    protected void onPanelSettingsLoaded(ListPanelToolRpc settings) {
        super.onPanelSettingsLoaded(settings);
        ListPanelGuideSettingsRPC guideSettings = settings.getGuideSettings();
        if (guideSettings != null) {
            listingGuidePanel = new ListingGuidePanel(guideSettings, ((GuideListingPanelDesign) listingPanelDesign).getAddNewItemCommand(), ((GuideListingPanelDesign) listingPanelDesign).getUploadButtonCommand());
            forceToShow = guideSettings.isForceItToShow();
        }
    }

    @Override
    protected void onDataLoaded(ListResult dataList) {
        super.onDataLoaded(dataList);
        layoutPanel.clear();

        boolean noFilter = getFacetFilterSavedList() == null || getFacetFilterSavedList().getSelectedId() == null;
        boolean noData = noFilter && (dataList.getTotal() == null || dataList.getTotal().equals(0));
        boolean firstTimeData = false;

        if (forceToShow && dataList.getTotal() != null) {
            firstTimeData = dataList.getTotal().equals(1);
        }

        if (showFlag && (null != listingGuidePanel && (firstTimeData || noData))) {
            layoutPanel.add(listingGuidePanel);
            if (forceToShow) {
                CommonService.App.get().unForceGuideListingPanelVisibility(panelType, new AsyncCallback<Void>() {
                    public void onFailure(Throwable throwable) {
                    }

                    public void onSuccess(Void aVoid) {
                    }
                });
                forceToShow = false;
            }
        } else {
            addTableToLayout();
        }
        showFlag = false;
    }
}
