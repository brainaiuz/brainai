package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

public class ReportMetaWidget extends Composite {


    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    interface ReportItemUiBinder extends UiBinder<Widget, ReportMetaWidget> {
    }

    @UiField
    Span reportName;
    @UiField
    Div figureActions;
    @UiField
    Div selectReport;
    private final SelectItem reportItem;
    private Command selectionHandler;
    private Command deleteHandler;
    private static final ReportItemUiBinder ourUiBinder = GWT.create(ReportItemUiBinder.class);

    public ReportMetaWidget(SelectItem reportItem) {
        this.reportItem = reportItem;
        initWidget(ourUiBinder.createAndBindUi(this));
        init();
    }

    private void init() {
        reportName.setText(reportItem.getName());
        selectReport.addClickHandler((event) -> {
            if (selectionHandler != null) {
                selectionHandler.execute();
            }
            SinksContainerFactory.entryPoint.onHistoryChanged("reporting|stepControl/" + reportItem.getId() + "/template");
        });

        if (reportItem.isNewItem()) {
            Div deleteButton = new Div("btn btn--circle btn--icon mod--delete");
            Icon deleteIcon = new Icon();
            deleteIcon.setStyleName("ficon--trash");
            deleteButton.add(deleteIcon);
            figureActions.add(deleteButton);
            deleteButton.addClickHandler(clickEvent -> {
                LoadingPanel.loading(true);
                ReportingService.App.get().deleteReportingXMLTemplateFromCompany(reportItem.getId(), new AsyncCallback<Void>() {
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                        if (deleteHandler != null) {
                            deleteHandler.execute();
                        }
                    }
                });
            });
        }
    }

    public void setSelectionHandler(Command selectionHandler) {
        this.selectionHandler = selectionHandler;
    }

    public void setDeleteHandler(Command deleteHandler) {
        this.deleteHandler = deleteHandler;
    }
}
