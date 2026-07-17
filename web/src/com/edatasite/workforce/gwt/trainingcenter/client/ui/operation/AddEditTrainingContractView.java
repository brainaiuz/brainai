package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomList;
import com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem;
import com.edatasite.workforce.gwt.core.client.ui.customlist.Design;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCServiceAsync;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TrainingContractItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddEditTrainingContractView extends CustomForm2 implements Colapse, Constants, TCConstants {
    private static final TCServiceAsync tcService = TCService.App.get();
    private static final TCStrings tcStrings = TCStrings.App.get();

    private Integer objectID;

    private TextBox name;
    private TextArea2 description;
    private DatePicker startDate;
    private DatePicker endDate;
    private KpiCheckBox prepaid;


    private CustomList courseList;
    private CrmAccountLookUp accountLookUp;


    public AddEditTrainingContractView() {
        super(TCConstants.TC_EDIT_TRAINING_CONTRACT, tcStrings.customerContractsEdit());
    }

    public AddEditTrainingContractView(Integer objectID) {
        super(TCConstants.TC_ADD_TRAINING_CONTRACT, tcStrings.customerContractsAdd());
        this.objectID = objectID;
    }
    FormHasCustomField customFieldUtil;

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.TrainingContract, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result);
                AddEditTrainingContractView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        initialize();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    protected void initialize() {
        String training_contract_add_edit_view = "training_contract_add_edit_view_";

        //Name
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);
        name.ensureDebugId(training_contract_add_edit_view + "name");
        name.addClickHandler(event -> name.removeStyleName(ERROR_FORM_STYLE));

        //Description
        description = new TextArea2(TextArea2.AREA_LENGTH_2);
        description.setWidth("100%");
        description.setHeight("150px");
        description.hideCharacterLimitPanel();
        description.ensureDebugId(training_contract_add_edit_view + "description");

        //start date
        startDate = new DatePicker();
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.ensureDebugId(training_contract_add_edit_view + "startDate");

        //end date
        endDate = new DatePicker();
        endDate.addStyleName(DEFAULT_WIDTH);
        endDate.ensureDebugId(training_contract_add_edit_view + "endDate");

        // Prepaid
        prepaid = new KpiCheckBox();

        //accaunt
        accountLookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        //Course Requirement list widget
        courseList = new CustomList(Design.CHECK, true);
        courseList.ensureDebugId(training_contract_add_edit_view + "crList");
        courseList.setSearchText(wfmStrings.searchCourses());
        courseList.setHeight("180px");

        //Add title fields
        addTitleField(CONTRACT.GENERAL_DETAILS, wfmStrings.contractDetails());

        addField(CONTRACT.ACCOUNT, accountLookUp, getTitle(wfmStrings.accountName(), true));
        addField(CONTRACT.NAME, name, getTitle(tcStrings.contractName(), true));
        addField(CONTRACT.DESCRIPTION, description, getTitle(wfmStrings.description(), true));
        addField(CONTRACT.START_DATE, startDate, getTitle(wfmStrings.startDate(), true));
        addField(CONTRACT.END_DATE, endDate, getTitle(wfmStrings.endDate(), true));
        addField(CONTRACT.PREPAID, prepaid, getTitle(wfmStrings.prePaid()));
        addField(CONTRACT.COURSES_LIST, courseList, getTitle(wfmStrings.courses(), true));
        getCustomFieldUtil().drawCustomFields(this, objectID, false);


        show();
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        tcService.getContractItem(objectID, new AbstractAsyncCallback<TrainingContractItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final TrainingContractItem result) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    fillFormWithData(result);
                });
            }
        });
    }

    private void fillFormWithData(TrainingContractItem contractItem) {
        name.setText(contractItem.getName());
        description.setText(contractItem.getDescription());
        startDate.setDate(contractItem.getStartDate());
        endDate.setDate(contractItem.getEndDate());
        prepaid.setValue(contractItem.getPrepaid());
        if (contractItem.getAccountItem() != null) {
            SelectItem account = new SelectItem(contractItem.getAccountItem().getObjectId(), contractItem.getAccountItem().getName());
            accountLookUp.addItem(account);
        }
        getCustomFieldUtil().fillCustomFieldsWithData(contractItem.getCustomFields());

        initCRWidget(contractItem.getCoursesList(), contractItem.getCourseIDs());
    }

    @Override
    protected void addButtons() {
        if (objectID == null) {
            addButton(wfmStrings.saveNext(), BTN_DEFAULT_OUTLINE, event -> save(false));
            addButton(wfmStrings.saveAndNew(), BTN_PRIMARY, event -> save(true));
        } else {
            addButton(wfmStrings.save(), BTN_PRIMARY, event -> save(false));
        }
    }
    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void save(final boolean saveAndNew) {
        if (validate()) {
            return;
        }
        enableButton(false);
        TrainingContractItem trainingContractItem = new TrainingContractItem();
        trainingContractItem.setObjectID(objectID);
        trainingContractItem.setName(name.getValue());
        trainingContractItem.setDescription(description.getText());
        trainingContractItem.setStartDate(startDate.getDate());
        trainingContractItem.setEndDate(endDate.getDate());
        trainingContractItem.setPrepaid(prepaid.getValue());

        if (courseList.getItems() != null && courseList.getItems().size() > 0) {
            ArrayList<Integer> appliedClients = new ArrayList<>();
            for (CustomListItem client : courseList.getItems()) {
                if (client.getValue()) {
                    appliedClients.add(client.getItem().getId());
                }
            }
            trainingContractItem.setCourseIDs(appliedClients);
        }

        trainingContractItem.setAccountID(accountLookUp.getSelectedItemID());

        tcService.saveTrainingContract(trainingContractItem, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                enableButton(true);
                Info.show(wfmStrings.messSuccessfullySaved(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TRAINING_CONTRACT_ADD_EDIT, result, AddEditTrainingContractView.this);
                if (!saveAndNew) {
                    closeTab();
                    if (objectID != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(TC_CHANGE_CONTRACT_PRICE + "|summary/" + objectID + "/true");
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged(TC_CHANGE_CONTRACT_PRICE + "|summary/" + result + "/false");
                    }
                } else {
                    resetValues();
                }
            }
        });

    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();
        errors += markAsError(CONTRACT.NAME, name, name.getValue() == null || name.getValue().equals(""));
        errors += markAsError(CONTRACT.DESCRIPTION, description, description.getText() == null || description.getText().equals(""));
        errors += markAsError(CONTRACT.START_DATE, startDate, startDate.getDate() == null);
        errors += markAsError(CONTRACT.END_DATE, endDate, endDate.getDate() == null);
        errors += markAsError(CONTRACT.ACCOUNT, accountLookUp, accountLookUp.getSelectedItem() == null);
        errors += markAsError(CONTRACT.COURSES_LIST, courseList, courseList.getCheckedItemCount() <= 0);


        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return true;
        }
        return false;
    }

    private void resetValues() {
        tcService.getContractItem(null, new AbstractAsyncCallback<TrainingContractItem>() {
            @Override
            public void success(TrainingContractItem courseItem) {
                initCRWidget(courseItem.getCoursesList(), courseItem.getCourseIDs());
            }
        });

        name.setText(null);
        description.setText(null);
        startDate.setDate(null);
        endDate.setDate(null);
        accountLookUp = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        accountLookUp.addStyleName(DEFAULT_WIDTH);
        prepaid.setValue(false);
    }


    private void initCRWidget(SelectItem[] crs, ArrayList<Integer> selectedCRs) {
        if (courseList.getItems() != null) {
            courseList.removeItems();
        }

        if (crs != null) {
            for (SelectItem cr : crs) {
                CustomListItem item = new CustomListItem(cr);
                courseList.add(item);
                if (selectedCRs.contains(cr.getId())) {
                    item.setCheck(true);
                }
            }
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TRAINING_CONTACT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
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
