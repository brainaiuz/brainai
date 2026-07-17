package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit.AddEditReportingFolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;

import java.util.List;

public class ReportingFolder extends Composite {
    interface ReportingCategoryUiBinder extends UiBinder<HTMLPanel, ReportingFolder> {
    }

    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private static ReportingCategoryUiBinder ourUiBinder = GWT.create(ReportingCategoryUiBinder.class);

    @UiField
    Div panelHeading;
    @UiField
    Div figureToggle;
    @UiField
    Div reportItems;
    @UiField
    Heading headingTitle;
    @UiField
    MaterialIcon icon;
    @UiField
    Div headingDescription;
    @UiField
    Div figureActions;
    private boolean isVisible;
    private HTMLPanel panel;
    private FolderRpc folderRpc;

    public ReportingFolder(FolderRpc folderRpc, boolean favourite) {
        panel = ourUiBinder.createAndBindUi(this);
        initWidget(panel);
        if (folderRpc.getIcon() != null && !folderRpc.getIcon().isEmpty()) {
            icon.setClass(folderRpc.getIcon());
        } else {
            icon.setClass("ficon--report-custom");
        }
        headingTitle.setText(folderRpc.getName());
        headingDescription.getElement().setInnerText(folderRpc.getDescription() != null ? folderRpc.getDescription() : "");
        if (!favourite && Utils.hasPermission(PermissionConstants.REPORTING_ADD_EDIT_FOLDER)) {
            Div editButton = new Div("btn btn--edit btn--circle mod--edit");
            Icon deleteIcon = new Icon();
            deleteIcon.setStyleName("ficon--edit");
            editButton.add(deleteIcon);
            figureActions.add(editButton);
            editButton.addClickHandler(clickEvent -> {
                LoadingPanel.loading(true);
                ReportingService.App.get().getFolder(folderRpc.getId(), new AsyncCallback<FolderRpc>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(FolderRpc folderRpc) {
                        AddEditReportingFolder addFolderPopup = new AddEditReportingFolder(folderRpc);
                        addFolderPopup.center();
                    }
                });
            });
        }
        this.folderRpc = folderRpc;
        initEventListeners(favourite);
        addReports();
    }


    //Constructor for search
    public ReportingFolder(FolderRpc folderRpc) {
        panel = ourUiBinder.createAndBindUi(this);
        initWidget(panel);
        if (folderRpc.getIcon() != null && !folderRpc.getIcon().isEmpty()) {
            icon.setClass(folderRpc.getIcon());
        } else {
            icon.setClass("ficon--report-custom");
        }
        headingTitle.setText(folderRpc.getName());
        headingDescription.getElement().setInnerText(folderRpc.getDescription() != null ? folderRpc.getDescription() : "");
        this.folderRpc = folderRpc;
        initEventListeners(false);
        addReports();
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    private void initEventListeners(boolean favourite) {
        // Collapsible folder

        if (favourite) {
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_REPORTING_FAVOURITY, ReportingFolder.this, ((sender, args) -> {
                if (sender instanceof ReportingReportItem) {
                    SelectListRpc data = ((ReportingReportItem) sender).getReportItem();
                    reportItems.add(new ReportingReportItem(data));
                }
            }));
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REMOVE_REPORTING_FAVOURITY, ReportingFolder.this, (sender, args) -> {
                if (args instanceof SelectListRpc) {
                    for (Widget widget : reportItems.getChildrenList()) {
                        SelectListRpc report = ((ReportingReportItem) widget).getReportItem();
                        if (report.getId().equals(((SelectListRpc) args).getId())) {
                            widget.removeFromParent();
                            break;
                        }
                    }
                }
            });
        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.REMOVE_REPORT, ReportingFolder.this, (sender, args) -> {
            if (args instanceof Integer) {
                for (Widget widget : reportItems.getChildrenList()) {
                    SelectListRpc report = ((ReportingReportItem) widget).getReportItem();
                    if (report.getId().equals(args)) {
                        widget.removeFromParent();
                    }
                }
            }
        });
    }

    public void setReports(List<SelectListRpc> reports) {//written for ReportingSearach widget
        reportItems.clear();
        if (reports == null || reports.size() == 0) {
            Div figureH = new Div("figure-h figure-h--info");
            Div figureImage = new Div("figure-image");
            Div caption = new Div("figure-caption");
            Heading title = new Heading(HeadingSize.H3);
            title.addStyleName("panel-reporting__title");
            title.setText(reportingStrings.searchEmptyMessage());
            Div actions = new Div("figure-actions file--reportingFolder");
            figureH.add(figureToggle);
            figureH.add(figureImage);
            figureH.add(caption);
            caption.add(title);
            figureH.add(actions);
            reportItems.add(figureH);
        } else {
            for (SelectListRpc rpc : reports) {
                reportItems.add(new ReportingReportItem(rpc));
            }
        }
    }

    public void addCollapsible(){
        reportItems.setVisible(isVisible);
        panelHeading.setStyle("cursor:pointer");
        if (!isVisible){
            figureToggle.addStyleName("active");
        }

        panelHeading.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (reportItems.isVisible()) {
                    figureToggle.addStyleName("active");
                } else {
                    figureToggle.removeStyleName("active");
                }
                reportItems.setVisible(!reportItems.isVisible());
            }
        });
    }
    public void addReports() {
        for (SelectListRpc rpc : folderRpc.getReports()) {
            reportItems.add(new ReportingReportItem(rpc));
        }
    }

    /**
     * <button type="button" class="btn btn--default btn-block hasicon--center">
     * <i class="ficon--menu"></i>
     * <span>View All</span>
     * </button>
     */
}
