package com.edatasite.workforce.gwt.reportingsystem.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.RepRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingCategoryRPC;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.ReportingFolder;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit.AddEditReportingFolder;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit.NewReportPopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportingHome extends View {

    interface ReportinHomeUiBinder extends UiBinder<HTMLPanel, ReportingHome> {
    }

    private static ReportinHomeUiBinder ourUiBinder = GWT.create(ReportinHomeUiBinder.class);
    private HTMLPanel folders;
    private ReportingCategoryRPC category;
    private int i = 0;

    public ReportingHome(ReportingCategoryRPC category) {
        super("reportingHome" + category.getId(), category.getName());
        this.category = category;
        setAddNew(() -> NewReportPopup.getInstance(category).show());
    }

    @Override
    protected Widget onInitialize() {
        folders = ourUiBinder.createAndBindUi(this);
        add(folders);
        loadingData();
        initListeners();
        return null;
    }

    private void initListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.REPORTING_REPOT_SAVED, (sender, args) -> {
            if (category.getId().equals(Integer.valueOf(String.valueOf(args)))) {
                loadingData();
            } else {
                loadingData();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REPORTING_FOLDER_SAVED, ReportingHome.this, (sender, args) -> loadingData());
    }

    @Override
    public String getIconStyle() {
        return "ficon--report";
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private void loadingData() {
        LoadingPanel.loading(true);
        ReportingService.App.get().queryForReportsByCategory(category.getId(), new AsyncCallback<RepRpc>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(RepRpc result) {
                LoadingPanel.loading(false);
                folders.clear();
                i = 0;
                for (FolderRpc folderRpc : result.getFolders().values()) {
                    ReportingFolder folder = new ReportingFolder(folderRpc, category.getId() == 0);
                    addReportVisible(folder);
                    folders.add(folder);
                }
                if (category.getId() == 0)
                    loadingEmptyFolder();
            }
        });
    }

    private void loadingEmptyFolder() {
        ReportingService.App.get().getFolders(null, new AsyncCallback<FolderRpc[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(FolderRpc[] result) {
                i = 1;
                for (FolderRpc folderRpc : result) {
                    if (folderRpc.getReports().isEmpty() && category.getId() == folderRpc.getCategoryId()) {
                        ReportingFolder folder = new ReportingFolder(folderRpc);
                        addReportVisible(folder);
                        folders.add(folder);
                    }
                }
            }
        });
    }

    public void changeFavourityTable(SelectListRpc rpc) {
        //check/uncheck report
        NodeList elements = Utils.getElementsByName("report_" + rpc.getId());
        if (elements != null) {
            for (int j = 0; j < elements.getLength(); j++) {
                Element element = elements.getItem(j).cast();
                ((InputElement) element.cast()).setChecked(rpc.isFavourited());
            }
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        NewReportPopup.getInstance(category);
        AddEditReportingFolder.categoryId = category.getId();
    }

    private void addReportVisible(ReportingFolder folder){
        if (i == 0) {
            folder.setVisible(true);
        } else {
            folder.setVisible(false);
        }
        i++;
        folder.addCollapsible();
    }
}