package com.edatasite.workforce.gwt.core.client.ui.resourceUtil;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.List;

/**
 * Created by FARRUH on 04-Apr-16.
 */
public class ResourceUtilFilterPopup extends KpiModal {

    private WfmStrings wfmStrings = WfmStrings.App.get();
    private ResourceUtilizationView resUtilView;
    private MaterialPanel mainPanel;
    private CustomList positionListBox;
    private KpiCheckBox showOnlyFilledCells;
    private KpiCheckBox showEmployee;
    private WfmButton2 resetButton;
    private WfmButton2 applyButton;
    private SelectItem[] allPositions;
    List<SelectItem> selectItems;

    public ResourceUtilFilterPopup(ResourceUtilizationView resUtilView) {
        this.resUtilView = resUtilView;
        initialize();
    }

    public void initialize() {
        setDismissible(false);
        setCloseButton(true);
        mainPanel = new MaterialPanel();

        positionListBox = new CustomList(Design.CHECK, true);
        positionListBox.ensureDebugId("pisitionList");
        positionListBox.setSearchText(wfmStrings.search());

        mainPanel.add(positionListBox);

        //Show only filled cells
        FlowPanel showOnlyFilledPanel = new FlowPanel();
        showOnlyFilledCells = new KpiCheckBox(wfmStrings.showFilledCells());
        showOnlyFilledCells.setEnabled(true);
        showOnlyFilledPanel.add(showOnlyFilledCells);

        mainPanel.add(showOnlyFilledPanel);

        //Show only .. employees
        FlowPanel showEmployeePanel = new FlowPanel();
        showEmployee = new KpiCheckBox(wfmStrings.showActiveEmployees());
        showEmployee.setEnabled(true);
        showEmployeePanel.add(showEmployee);

        mainPanel.add(showEmployeePanel);

        // Reset button
        resetButton = new WfmButton2(wfmStrings.reset(), WfmButton2.BTN_DEFAULT);
        resetButton.addClickHandler(clickEvent -> {
            initPositionListWidget(allPositions);
            positionListBox.setCheckAllItems(false);
            showOnlyFilledCells.setChecked(false);
            showEmployee.setChecked(false);
        });
        // Apply button
        applyButton = new WfmButton2(wfmStrings.apply());
        applyButton.addClickHandler(clickEvent -> {
            resUtilView.filterButton.setEnabled(true);
            StringBuilder positionIdsAsString = new StringBuilder();
            selectItems = positionListBox.getSelectItems();

            resUtilView.setNoPosition(false);
            for (SelectItem selectItem : selectItems) {
                if (selectItem.getId().equals(0)) {
                    resUtilView.setNoPosition(true);
                } else {
                    if (positionIdsAsString.length() == 0) {
                        positionIdsAsString = new StringBuilder(selectItem.getId().toString());
                    } else {
                        positionIdsAsString.append(",").append(selectItem.getId().toString());
                    }
                }
            }
            resUtilView.setPositionIds(positionIdsAsString.toString());
            resUtilView.setShowOnlyFilledCells(showOnlyFilledCells.isChecked());
            resUtilView.setShowActiveUsers(showEmployee.isChecked());

            resUtilView.getOverAllData();
            close();
        });

        add(mainPanel);
        addButton(resetButton);
        addButton(applyButton);

        setWidth(400);
    }

    private void initPositionListWidget(SelectItem[] positions) {
        SelectItem all = new SelectItem(0, "<b>" + wfmStrings.selectAll() + "</b>");
        if (positionListBox.getItems() != null) {
            positionListBox.removeItems();
        }

        boolean hasList = positions != null && positions.length > 0;
        if (hasList) {
            CustomListItem checkAll = new CustomListItem(all);
            positionListBox.add(checkAll);
            checkAll.addValueChangeHandler(booleanValueChangeEvent -> positionListBox.setCheckAllItems(booleanValueChangeEvent.getValue()));
        }

        SelectItem noPositionItem = new SelectItem();
        noPositionItem.setId(0);
        noPositionItem.setName("N/A");
        CustomListItem item = new CustomListItem(noPositionItem);
        positionListBox.add(item);

        if (hasList) {
            for (SelectItem positionItem : positions) {
                item = new CustomListItem(positionItem);
                positionListBox.add(item);
            }
        }

        if (selectItems != null) {
            for (SelectItem previouslySelectedItems : selectItems) {
                positionListBox.setCheckedItem(previouslySelectedItems, true);
            }
        }
    }

    @Override
    public void open() {
        super.open();

        getPositionsList();
    }

    private void getPositionsList() {
        LoadingPanel.loading(true, this);
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setSortField("position title");//sort by name
        AllInOneService.App.get().getPositionListAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            public void success(SelectItem[] positions) {
                if (allPositions == null) { // bu list filterni reset qilganda kk bo'ladi
                    allPositions = positions;
                }
                initPositionListWidget(positions);

                LoadingPanel.loading(false);
            }
        });
    }
}
