package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class ReportingReportItem extends Composite {
    interface FigureWidgetUiBinder extends UiBinder<Div, ReportingReportItem> {
    }

    private static final FigureWidgetUiBinder ourUiBinder = GWT.create(FigureWidgetUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final ReportingStrings reportingStrings = ReportingStrings.App.get();
    @UiField
    Div descriptionField;
    @UiField
    Heading title;
    @UiField
    Div figureImage;
    @UiField
    Div figureActions;
    private final Div self;
    private final SelectListRpc reportItem;

    ReportingReportItem(final SelectListRpc reportItem) {
        self = ourUiBinder.createAndBindUi(this);
        initWidget(self);
        this.reportItem = reportItem;
        toggleActve(reportItem.isFavourited());
        title.setText(reportItem.getName());
        descriptionField.getElement().setInnerText(reportItem.getDescription() != null ? reportItem.getDescription() : "");

        if (!reportItem.isLibrary() && Utils.hasPermission(PermissionConstants.REPORTING_DELETE_BUTTON)) {
            Div deleteButton = new Div("btn btn--circle btn--icon mod--delete");
            Icon deleteIcon = new Icon();
            deleteIcon.setStyleName("ficon--trash");
            deleteButton.add(deleteIcon);
            figureActions.add(deleteButton);
            deleteButton.addClickHandler((event) -> {
                WfmMessageBox deleteMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, reportingStrings.areYouSureWantoDeleteThisReport()
                        , new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        ReportingService.App.get().deleteReport(reportItem.getId(), new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                Info.warn(wfmStrings.error());
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.REMOVE_REPORT, reportItem.getId(), ReportingReportItem.this);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.report()));
                            }
                        });

                    }
                });
                deleteMessageBox.setTitle(wfmStrings.confirmation());
                deleteMessageBox.open();
                event.stopPropagation();
            });
        }

        initHandler();
    }

    private void initHandler() {
        self.addClickHandler((event) -> {
            if (reportItem.isFakeReport()) {
                Utils.openURL(GWT.getHostPageBaseURL() + reportItem.getTargetLink());
            } else {
                SinksContainerFactory.entryPoint.onHistoryChanged("reporting|stepControl/" + reportItem.getId() + "/savedreport/" + Utils.encrypt(reportItem.getName()), reportItem.getName(), reportItem.getName());
            }
        });
        figureImage.addClickHandler((event) -> {
            event.stopPropagation();
            ReportingService.App.get().createFavouriteReportTemplate(reportItem.getId(), new AsyncCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    reportItem.setFavourited(Boolean.TRUE.equals(result));
                    toggleActve(Boolean.TRUE.equals(result));
                    if (Boolean.TRUE.equals(result)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_REPORTING_FAVOURITY, reportItem, ReportingReportItem.this);
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REMOVE_REPORTING_FAVOURITY, reportItem, ReportingReportItem.this);
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                }
            });
        });
    }

    public SelectListRpc getReportItem() {
        return reportItem;
    }

    private void toggleActve(boolean value) {
        $(self.getElement()).toggleClass("active", value);
    }

}
