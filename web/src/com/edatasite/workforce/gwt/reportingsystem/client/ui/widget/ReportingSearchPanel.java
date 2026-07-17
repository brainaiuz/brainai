package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.ui.html.OptGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Virus on 8/23/14.
 */
public class ReportingSearchPanel extends Composite {

    interface ReportingSearchPanelUiBinder extends UiBinder<HTMLPanel, ReportingSearchPanel> {
    }

    private static ReportingSearchPanelUiBinder ourUiBinder = GWT.create(ReportingSearchPanelUiBinder.class);
    @UiField
    MaterialComboBox<SelectListRpc> searchField;
    @UiField
    Element newReportOrAddFolderButton;

    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private String categoryName = null;

    @UiConstructor
    public ReportingSearchPanel() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        newReportOrAddFolderButton.setInnerHTML(reportingStrings.newReport());
        searchField.setWidth("500px");
        searchField.setPlaceholder(wfmStrings.searchTypeMessage());
        searchField.addSelectionHandler(event -> {
            SelectListRpc rpc = searchField.getSingleValue();
            if (rpc.isFakeReport()) {
                Utils.openURL(GWT.getHostPageBaseURL() + rpc.getTargetLink());
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("reporting|stepControl/" + rpc.getId() + "/savedreport/" + Utils.encrypt(rpc.getName()), rpc.getName(), rpc.getName());
            }
            searchField.setValues(new ArrayList<>());
        });
        loadData();
    }


    /*
    if (items != null && items.length > 0) {
            OptGroup group = new OptGroup(groupname);
            for (SelectItem item : items) {
                super.addItem(item.getName(), item, group);
                itemGroup.put(item, groupname);
            }
            super.addGroup(group);
        }
     */
    private void loadData() {
        ReportingService.App.get().searchReport(null, new AbstractAsyncCallback<SelectListRpc[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectListRpc[] result) {
                HashMap<Integer, OptGroup> groupMap = new HashMap<>();
                if (result != null) {
                    for (SelectListRpc item : result) {
                        OptGroup group;
                        boolean createGroup = true;
                        if (groupMap.containsKey(item.getCategoryId())) {
                            group = groupMap.get(item.getCategoryId());
                            createGroup = false;
                        } else {
                            group = new OptGroup(item.getCategory());
                            groupMap.put(item.getCategoryId(), group);
                        }
                        searchField.addItem(item.getName(), item, group);
                        if (createGroup) {
                            searchField.addGroup(group);
                        }
                    }
                }
                searchField.setValues(new ArrayList<>());
            }
        });
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
