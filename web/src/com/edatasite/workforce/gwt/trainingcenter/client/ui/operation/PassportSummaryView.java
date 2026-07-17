package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTextBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.passport.PassportData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

/**
 * Created with IntelliJ IDEA.
 * User: Azazello
 * Date: 6/27/14
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class PassportSummaryView extends CustomForm2 implements Colapse, TCConstants {
    private static final String COURSE_NAME = "COURSE_NAME";
    private static final String COURSE_CODE = "COURSE_CODE";
    private static final String SCHEDULE_DATE = "SCHEDULE_DATE";
    private static final String EXPIRE_DATE = "EXPIRE_DATE";
    private static final TCStrings tcStrings = TCStrings.App.get();
    private final Integer objectID;
    private PassportData item;
    private EditableTable courses;
    private HTML level, status, type, number, student;

    public PassportSummaryView(Integer objectID) {
        super("summary", tcStrings.passportView());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        courses = new EditableTable(getColumnConfig(), false);
        courses.setWidth("700px");
        level = new HTML();
        status = new HTML();
        type = new HTML();
        number = new HTML();
        student = new HTML();

        addTitleField(TITLE, tcStrings.passportView());
        addField(NUMBER, number, getTitle(wfmStrings.number()));
        addField(CERTIFICATE.STUDENT, student, getTitle(wfmStrings.student()));
        addField(TYPE, type, getTitle(wfmStrings.type()));
        addField(STATUS, status, getTitle(wfmStrings.status()));
        addField(LEVEL, level, getTitle(wfmStrings.level()));
        addField(COURSES, courses, null);
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    public ColumnConfig[] getColumnConfig() {
        ColumnConfig[] columnConfig = new ColumnConfig[4];
        columnConfig[0] = new ColumnConfig(CustomCell.class, COURSE_CODE, tcStrings.courseCode(), 200, false);
        columnConfig[1] = new ColumnConfig(CustomCell.class, COURSE_NAME, tcStrings.courseName(), 300, false);
        columnConfig[2] = new ColumnConfig(CustomCell.class, SCHEDULE_DATE, tcStrings.courseDate(), 100, false);
        columnConfig[3] = new ColumnConfig(CustomCell.class, EXPIRE_DATE, wfmStrings.expiryDate(), 100, false);
        return columnConfig;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getPassportData(objectID, new AbstractAsyncCallback<PassportData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(PassportData result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    fillFields(result);
                }
                item = result;
            }
        });
    }

    private void fillFields(PassportData result) {
        number.setText((result.getNumberString() != null ? result.getNumberString() : "") + result.getNumber());
        level.setText(result.getLevel() != null ? result.getLevel() : wfmStrings.notAvailable());
        type.setText(result.getType());
        status.setText(result.getStatus());
        student.setText(result.getStudent());
        addCourses(result.getCourses());

    }

    private void addCourses(CourseItem[] result) {
        EditableTextBox courseCode = new EditableTextBox();
        EditableTextBox courseName = new EditableTextBox();
        EditableTextBox startDate = new EditableTextBox();
        EditableTextBox expireDate = new EditableTextBox();
        courseCode.setEnabled(false);
        courseName.setEnabled(false);
        startDate.setEnabled(false);
        expireDate.setEnabled(false);
        for (CourseItem item : result) {
            courseCode.setText(item.getNumber());
            courseName.setText(item.getCourseName());
            startDate.setText(DateUtils.getDateFormatShort(item.getCourseDate()));
            expireDate.setText(DateUtils.getDateFormatShort(item.getExpireDate()));
            Object[] objects = new Object[4];
            objects[0] = courseCode;
            objects[1] = courseName;
            objects[2] = startDate;
            objects[3] = expireDate;
            courses.addRow(objects);
        }
    }

    @Override
    protected void addButtons() {

        customizeButton.setVisible(false);

        MaterialLink options = new MaterialLink(wfmStrings.options());
        MaterialSplitButton optionsButton = new MaterialSplitButton(options, Constants.BTN_DEFAULT_OUTLINE);
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + URL.encodeQueryString(url));
            });
            optionsButton.addItem(customize);
        }
        if (Utils.hasPermission(PermissionConstants.TC_PASSPORT_DELETE)) {
            MaterialLink remove = new MaterialLink(wfmStrings.delete());
            remove.addClickHandler(event -> {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete() );
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        if (item != null) {
                            LoadingPanel.loading(true);
                            TCService.App.get().deletePassport(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Boolean result) {
                                    if (result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), tcStrings.passport()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PASSPORT_DELETE, result, PassportSummaryView.this);
                                        closeTab();
                                    }
                                }
                            });
                        }
                    }
                });
                messageBox.open();
            });
            optionsButton.addItem(remove);
        }
        addButton(optionsButton);
        if (Utils.hasPermission(PermissionConstants.TC_PASSPORT_EDIT)) {
            WfmButton2 edit = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
            edit.addClickHandler(event -> {
                closeTab();
                SinksContainerFactory.entryPoint.onHistoryChanged(TC_PASSPORT + "|edit/" + item.getObjectID());
            });
            addButton(edit);
        }
    }



    @Override
    protected String getFormID() {
        return LayoutRPC.HSE_PASSPORT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });

    }
}
