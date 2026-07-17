package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.MergeItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MergeAbstractView;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 12/4/12
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentMergeView extends MergeAbstractView {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final TCStrings tcStings = TCStrings.App.get();

    public StudentMergeView(String name, String description, Integer... studentIds) {
        super(name, description);

        initItems(studentIds);
    }

    private void initItems(Integer[] studentIds) {
        LoadingPanel.loading(true);
        TCService.App.get().getStudentListForMerge(studentIds, new AsyncCallback<ArrayList<StudentItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(ArrayList<StudentItem> studentList) {
                LoadingPanel.loading(false);
                setItems(studentList);
            }
        });
    }

    @Override
    protected Integer getItemObjectID(Object item) {
        return ((StudentItem) item).getObjectId();
    }

    @Override
    protected void merge() {
        ArrayList<Integer> objectIDs = new ArrayList<>();
        for (Integer item : getItems().keySet()) {
            if (item != null && !item.equals(getMainItem().getObjectId())) {
                objectIDs.add(item);
            }
        }

        LoadingPanel.loading(true);
        TCService.App.get().mergeStudents(getMainItem(), true, objectIDs, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                if (result) {
                    Info.get().show("Students have been successfully merged!", Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STUDENT_DELETE, null, StudentMergeView.this);
                    merged();
                } else {
                    Info.get().show("Students have not been successfully merged!", Info.Type.WARNING);
                }
            }
        });

    }

    @Override
    protected void drawAllFields() {
        addFieldInOneRow(MAINITEM, null, "Master Record", StudentItem.getAsMergeItems(StudentItem.STUDENT_NAME, getItems()));
        addFieldInOneRow(StudentItem.STUDENT_CUSTOMER, MAINITEM, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));
        addFieldInOneRow(StudentItem.STUDENT_FIRST_NAME, MAINITEM, wfmStrings.firstName());
        addFieldInOneRow(StudentItem.STUDENT_LAST_NAME, MAINITEM, wfmStrings.lastName());
        addFieldInOneRow(StudentItem.STUDENT_E_MAIL, MAINITEM, wfmStrings.email());
        addFieldInOneRow(StudentItem.STUDENT_PHONE_NUMBER, MAINITEM, wfmStrings.phone());
        addFieldInOneRow(StudentItem.STUDENT_COMPANY_EMPLOYEE_NUMBER, MAINITEM, wfmStrings.companyEmployeeNumber());
        addFieldInOneRow(StudentItem.STUDENT_RESIDENCE_NUMBER, MAINITEM, tcStings.residenceNumber());
        addFieldInOneRow(StudentItem.STUDENT_REFERENCE_IND_NUMBER, MAINITEM, tcStings.refIndNumber());
        addFieldInOneRow(StudentItem.STUDENT_GENDER, MAINITEM, wfmStrings.gender());
        addFieldInOneRow(StudentItem.STUDENT_NATIONALITY, MAINITEM, wfmStrings.nationality());
        addFieldInOneRow(StudentItem.STUDENT_ADDRESS, MAINITEM, wfmStrings.address());
        addFieldInOneRow(StudentItem.DATE_OF_BIRTH, MAINITEM, wfmStrings.dateOfBirth());
    }

    @Override
    protected ArrayList<MergeItem> getMergeItems(String fieldName) {
        return StudentItem.getAsMergeItems(fieldName, getItems());
    }

    @Override
    protected void changeByMergeItem(String fieldName, MergeItem item, Boolean value) {
        getMainItem().changeByMergeItem(fieldName, item, value);
    }

    public StudentItem getMainItem() {
        return (StudentItem) super.getMainItem();
    }

    private HashMap<Integer, StudentItem> getItems() {
        return (HashMap<Integer, StudentItem>) mapOfRPCs;
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
