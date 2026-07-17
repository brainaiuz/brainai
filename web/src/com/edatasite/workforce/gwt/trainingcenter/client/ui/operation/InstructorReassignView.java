package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SelectItemCell;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.InstructorScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 8/2/12
 * Time: 12:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstructorReassignView extends CustomForm2 implements Constants, Colapse {

    private static final TCStrings tcStrings = TCStrings.App.get();

    private KpiDataGrid<InstructorScheduledCourseItem> instructorReassign;
    private ListDataProvider<InstructorScheduledCourseItem> instructorReassignDataProvider;

    private List<SelectItem> instructors;

    private final Integer objectID;

    public static final ProvidesKey<InstructorScheduledCourseItem> KEY_PROVIDER_INSTRUCTOR_REASSIGN = item -> item == null ? null : item.getInstructorID();

    public InstructorReassignView(Integer objectID) {
        super("instructorReassign", tcStrings.instructorReassign());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        LoadingPanel.loading(true);
        TCService.App.get().getAvailabilityData(objectID, new AsyncCallback<ScheduledCourseItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(ScheduledCourseItem result) {
                LoadingPanel.loading(false);
                instructors = new ArrayList<>();
                Collections.addAll(instructors, result.getInstructors());
                initInternal();
            }
        });

        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initInternal() {
        instructorReassign = new KpiDataGrid<>(KEY_PROVIDER_INSTRUCTOR_REASSIGN);
        instructorReassign.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));
        instructorReassign.setSize("500px", "200px");
        initInstructorColumns();

        instructorReassignDataProvider = new ListDataProvider<>();
        instructorReassignDataProvider.addDataDisplay(instructorReassign);

        addTitleField(CustomFormConstants.TRAINING_CENTER.INSTRUCTOR_REASSIGN.DETAIL, tcStrings.scheduledCourseDetails());
        addField(CustomFormConstants.TRAINING_CENTER.INSTRUCTOR_REASSIGN.INSTRUCTOR_REASSIGN, instructorReassign, null);
        show();
    }

    private void initInstructorColumns() {
        //Number of Instructor
        instructorReassign.addColumn(new Column<InstructorScheduledCourseItem, String>(new TextCell()) {
            @Override
            public String getValue(InstructorScheduledCourseItem item) {
                return String.valueOf(item.getOrder());
            }
        }, wfmStrings.number());
        instructorReassign.setColumnWidth(instructorReassign.getColumn(0), 30, Style.Unit.PCT);

        //Scheduled date of course
        instructorReassign.addColumn(new Column<InstructorScheduledCourseItem, String>(new TextCell()) {
            @Override
            public String getValue(InstructorScheduledCourseItem item) {
                return item.getDateAsString();
            }
        }, tcStrings.courseDate());
        instructorReassign.setColumnWidth(instructorReassign.getColumn(1), 100, Style.Unit.PCT);

        //List of Instructor
        final SelectItemCell selectionCell = new SelectItemCell(instructors);
        selectionCell.setWidth("200px");
        Column<InstructorScheduledCourseItem, SelectItem> instructorColumn = new Column<InstructorScheduledCourseItem, SelectItem>(selectionCell) {
            @Override
            public SelectItem getValue(InstructorScheduledCourseItem object) {
                if (object.hasLeave()) {
                    selectionCell.setStyleName("instructor-reassign");
                } else {
                    selectionCell.setStyleName(null);
                }
                return new SelectItem(object.getInstructorID());
            }
        };
        instructorReassign.addColumn(instructorColumn, wfmStrings.instructor());
        instructorColumn.setFieldUpdater((index, object, value) -> object.setInstructorID(value.getId()));
        instructorReassign.setColumnWidth(instructorColumn, 150, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    private void fillData(ArrayList<InstructorScheduledCourseItem> instructors) {
        instructorReassignDataProvider.getList().clear();
        instructorReassignDataProvider.getList().addAll(instructors);
        instructorReassignDataProvider.refresh();
    }



    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getScheduledCourseInstructors(objectID, new AsyncCallback<ArrayList<InstructorScheduledCourseItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<InstructorScheduledCourseItem> result) {
                LoadingPanel.loading(false);
                fillData(result);
            }
        });
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> save());
    }

    private void save() {
        InstructorScheduledCourseItem[] items = new InstructorScheduledCourseItem[instructorReassignDataProvider.getList().size()];

        int i = 0;
        for (InstructorScheduledCourseItem item : instructorReassignDataProvider.getList()) {
            items[i++] = item;
        }

        LoadingPanel.loading(true);
        TCService.App.get().updateScheduledCourseInstructors(items, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()), Info.Type.INFO);
                closeTab();
            }
        });
    }


    @Override
    public String getIconStyle() {
        return "bgMark instructor-reassign-icon";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.INSTRUCTOR_REASSIGN_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
