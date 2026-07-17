package com.edatasite.workforce.gwt.assessment.client.ui.view.customTabs;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.InProgressAssessmentListElem;
import com.edatasite.workforce.gwt.assessment.client.ui.AssessmentHelper;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 2/10/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class WaitingForSomeoneSimpleReviewTab extends HTMLPanel {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final KpiDataGrid<InProgressAssessmentListElem> dataGrid;
    private final ListDataProvider<InProgressAssessmentListElem> dataProvider;
    private final String emptyMessage;
    private boolean isManagerReview = false;
    private final boolean isPending;
    private final ColumnSortEvent.ListHandler<InProgressAssessmentListElem> listHandler;
    private final HorizontalPanel pdfPanel;


    public WaitingForSomeoneSimpleReviewTab(HorizontalPanel pdfPanel, String emptyMessage, boolean isManagerReview, boolean isPending) {
        super("");
        this.pdfPanel = pdfPanel;
        this.emptyMessage = emptyMessage;
        this.isManagerReview = isManagerReview;
        this.isPending = isPending;

        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setSize("100%", "100%");
        listHandler = new ColumnSortEvent.ListHandler<>(dataProvider.getList());
        dataGrid.addColumnSortHandler(listHandler);
        addDataDisplay(dataGrid);
        add(dataGrid);

        initTableColumns();
    }

    public static final ProvidesKey<InProgressAssessmentListElem> KEY_PROVIDER = item -> item == null ? null : item.getObjectID();

    public void addDataDisplay(HasData<InProgressAssessmentListElem> display) {
        dataProvider.addDataDisplay(display);
    }

    public void draw(InProgressAssessmentListElem[] assessmentElements) {
        if (assessmentElements == null || assessmentElements.length == 0) {
            dataProvider.getList().clear();
            dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(emptyMessage, null, null));
            dataGrid.setHeight("70px");
        } else {
            drawInitialize(assessmentElements);
        }
        dataProvider.refresh();
    }

    private void drawInitialize(InProgressAssessmentListElem[] assessmentElements) {
        //Bu employee listni dinamik ko'rinishi uchun qilindi
        dataGrid.setHeight((assessmentElements.length+1)*33+"px");

        List<InProgressAssessmentListElem> dataProviderList = dataProvider.getList();
        dataProviderList.clear();
        Collections.addAll(dataProviderList, assessmentElements);
    }

    private void initTableColumns() {
        //Employee name
        if (isPending) {
            final Column<InProgressAssessmentListElem, String> employee = new Column<InProgressAssessmentListElem, String>(new TextCell()) {
                @Override
                public String getValue(InProgressAssessmentListElem object) {
                    return object.getEmployeeName();
                }
            };
            dataGrid.addColumn(employee, wfmStrings.employee());
            dataGrid.setColumnWidth(employee, 11, Style.Unit.PCT);
            employee.setSortable(true);
            listHandler.setComparator(employee, (o1, o2) -> o1.getEmployeeName().compareToIgnoreCase(o2.getEmployeeName()));
        } else {
            final Column<InProgressAssessmentListElem, String> employee = new Column<InProgressAssessmentListElem, String>(new SimpleLinkCell()) {
                @Override
                public String getValue(InProgressAssessmentListElem object) {
                    return object.getEmployeeName();
                }
            };
            dataGrid.addColumn(employee, wfmStrings.employee());
            dataGrid.setColumnWidth(employee, 11, Style.Unit.PCT);
            employee.setFieldUpdater((index, object, value) -> {
                if (isManagerReview) {
                    if (Constants.SAVED_AS_DRAFT.equals(object.getStatus()) || (object.getEmployeeUsername().equals(Utils.getUserName()) && Utils.hasRole(Constants.TL))) {
                        if (object.getAssessmentType() != null && Constants.ASSESSMENT_360.equals(object.getAssessmentType())) {
                            if (object.getCollaborator() == null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged(AssessmentHelper.getReviewLinkForUI(object));
                            } else {
                                employee.setCellStyleNames("textDecoration");
                            }
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged(AssessmentHelper.getReviewLinkForUI(object));
                        }
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged(AssessmentHelper.getReviewLinkForUI(object) + "/true");
                    }
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged(AssessmentHelper.getReviewLinkForUI(object));
                }
            });
            employee.setSortable(true);
            listHandler.setComparator(employee, (o1, o2) -> o1.getEmployeeName().compareToIgnoreCase(o2.getEmployeeName()));
        }
        //Reviewer name
        Column<InProgressAssessmentListElem, String> status = new Column<InProgressAssessmentListElem, String>(new TextCell()) {
            @Override
            public String getValue(InProgressAssessmentListElem object) {
                return object.getStatusName();
            }
        };
        dataGrid.addColumn(status, wfmStrings.status());
        dataGrid.setColumnWidth(status, 11, Style.Unit.PCT);
        status.setSortable(true);
        listHandler.setComparator(status, (o1, o2) -> (o1.getStatusName() != null
                ? o1.getStatusName()
                : wfmStrings.notAvailable()).compareToIgnoreCase(o2.getStatusName() != null
                ? o2.getStatusName()
                : wfmStrings.notAvailable()));
        //Reviewer name
        Column<InProgressAssessmentListElem, String> reviewer = new Column<InProgressAssessmentListElem, String>(new TextCell()) {
            @Override
            public String getValue(InProgressAssessmentListElem object) {
                return object.getReviewerName();
            }
        };
        dataGrid.addColumn(reviewer, wfmStrings.reviewer());
        dataGrid.setColumnWidth(reviewer, 11, Style.Unit.PCT);
        reviewer.setSortable(true);
        listHandler.setComparator(reviewer, (o1, o2) -> o1.getReviewerName().compareToIgnoreCase(o2.getReviewerName()));
        //Initiate date
        Column<InProgressAssessmentListElem, String> initiateDate = new Column<InProgressAssessmentListElem, String>(new TextCell()) {
            @Override
            public String getValue(InProgressAssessmentListElem object) {
                return object.getInitiationDate() != null ? DateUtils.format(object.getInitiationDate()) + Utils.getHijriDate(object.getInitiationDate()) : wfmStrings.notAvailable();
            }
        };
        dataGrid.addColumn(initiateDate, hrmsStrings.initiatedDate());
        dataGrid.setColumnWidth(initiateDate, 10, Style.Unit.PCT);
        initiateDate.setSortable(true);
        listHandler.setComparator(initiateDate, (o1, o2) -> (o1.getInitiationDate() != null ? DateUtils.format(o1.getInitiationDate()) :
                wfmStrings.notAvailable()).compareToIgnoreCase(o2.getInitiationDate() != null ?
                DateUtils.format(o2.getInitiationDate()) : wfmStrings.notAvailable()));
        //Export print to PDF
        IconCell iconCell = new IconCell("ficon--file-pdf pointer");
        Column<InProgressAssessmentListElem, String> pdf = new Column<InProgressAssessmentListElem, String>(iconCell) {
            @Override
            public String getValue(InProgressAssessmentListElem object) {
                iconCell.setClickHandler(event -> {
                    String pdfURL = CommandConstants.PDF_URL + "/assessmentViewPDFHandler";
                    RequestObject requestObject = new RequestObject(object.getObjectID());
                    Utils.sendPDFOrExcelRequest(pdfPanel, pdfURL, requestObject.getRequestParams(), "_blank");
                });
                return null;
            }
        };
        pdf.setSortable(false);
        dataGrid.addColumn(pdf, wfmStrings.pdf());
        dataGrid.setColumnWidth(pdf, 3, Style.Unit.PCT);
        dataGrid.setStylePrimaryName("pdfHeader");

        //Delete option
        IconCell deleteIcon = new IconCell("ficon--trash pointer");
        Column<InProgressAssessmentListElem, String> delete = new Column<InProgressAssessmentListElem, String>(deleteIcon) {
            @Override
            public String getValue(InProgressAssessmentListElem object) {
                deleteIcon.setClickHandler(event -> {
                    deleteAction(object.getEmployeeAssessmentId());
                });
                return null;
            }
        };
        pdf.setSortable(false);
        dataGrid.addColumn(delete, wfmStrings.delete());
        dataGrid.setColumnWidth(delete, 3, Style.Unit.PCT);
    }

    private void deleteAction(Integer assessmentID) {
        if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_REQUEST)) {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            message.setTitle(wfmStrings.warning());
            message.setMessage(wfmStrings.messAreDelete());
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    AssessmentService.App.get().deleteAssessment(assessmentID, new AbstractAsyncCallback<Void>() {
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ASSASSMET_DELETED, null, WaitingForSomeoneSimpleReviewTab.this);
                            Info.show((Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.assessment())), Info.Type.INFO);
                        }
                    });
                }
            });
            message.open();
        } else {
            Info.show(wfmStrings.youDontHavePermission());
        }
    }
}
