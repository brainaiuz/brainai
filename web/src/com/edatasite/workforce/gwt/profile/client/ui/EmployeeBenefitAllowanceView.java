package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * Created by Djuraev on 8/5/15.
 */
public class EmployeeBenefitAllowanceView extends CustomForm implements Constants, Colapse {

    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private HTML employeeName;
    private Integer objectID;
    private final Integer year;
    private ProfileItem item;
    private DynamicTable tblBenefitAllowance;
    private final String errormessage = "";

    public EmployeeBenefitAllowanceView(Integer objectID, Integer year) {
        super("employeeBenefitView", hrmsStrings.employeeBenefitAllowance());
        this.objectID = objectID;
        this.year = year;
    }

    @Override
    public String getIconStyle() {
        return "icon-edit";
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), null, ("employee_benefit_save"), event -> save(true));
    }

    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            HrmsService.App.get().getEmployeeBenefitAllowance(objectID, year, new AbstractAsyncCallback<ProfileItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ProfileItem result) {
                    item = result;
                    LoadingPanel.loading(false);
                    employeeName.setText(item.getName());
                    if (result.getEmployeeBenefits() != null && result.getEmployeeBenefits().size() > 0) {
                        createBenefitAllowanceTable(result.getEmployeeBenefits());
                    }
                }
            });

        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMPLOYEE_BENEFIT_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void initialize() {
        employeeName = new HTML();
        tblBenefitAllowance = new DynamicTable(getColumns(), "productstable-header", "", false);
        tblBenefitAllowance.setBorderWidth(0);
        tblBenefitAllowance.setStyleName("bulletin-GWTCode addRemoveData");
        tblBenefitAllowance.setWidth("65%");

        addTitleField(POSITIONS.BENEFIT_ALLOWANCE_INFORMATION, hrmsStrings.benefitAllowanceInfo());
        addField(EMPLOYEE_CODE, employeeName, getTitle(wfmStrings.employee(), false));
        addField(POSITIONS.LEAVE_ALLOUNCE_PANEL, tblBenefitAllowance, getTitle(hrmsStrings.manageBenefitAllowances(), false));
        show();
    }

    private DynamicTableColumn[] getColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[4];
        columns[0] = new DynamicTableColumn(wfmStrings.benefits(), "benefit", 120);
        columns[1] = new DynamicTableColumn("", "reasonsid", 1);
        columns[2] = new DynamicTableColumn(wfmStrings.allowance(), "allowance", 25);
        columns[3] = new DynamicTableColumn(wfmStrings.quantityType(), "qtyType", 25);
        return columns;
    }

    private void createBenefitAllowanceTable(HashMap<Integer, AnnualLeaveItem> allowance) {
        for (Integer key : allowance.keySet()) {
            tblBenefitAllowance.addRow(getWidgets(key, allowance));
        }
    }

    private Widget[] getWidgets(final Integer key, final HashMap<Integer, AnnualLeaveItem> allowances) {
        int index = 0;
        final Widget[] widgets = new Widget[tblBenefitAllowance.getCellCount(0)];

        Label bName = new Label(allowances.get(key).getReasonName());
        bName.getElement().getStyle().setFloat(Style.Float.LEFT);
        widgets[index++] = bName;

        Hidden benefitID = new Hidden();
        benefitID.setValue(String.valueOf(key));
        widgets[index++] = benefitID;

        TextBox allowanceDays = new TextBox();
        allowanceDays.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        allowanceDays.setMaxLength(8);
        Validation.addNumericKeyboardListener(allowanceDays, 0);
        allowanceDays.setValue(String.valueOf(allowances.get(key).getAnnualallowancedays()));
        widgets[index++] = allowanceDays;

        Label qtyType = new Label(allowances.get(key).getBenefitType());
        widgets[index++] = qtyType;
        return widgets;
    }

    private void reInit() {
        objectID = null;
        initForm();
        initialize();
    }

    private void onShellOk(boolean saveAndClose) {
        if (saveAndClose) {
            closeTab();
        } else {
            reInit();
        }
    }

    private void save(final boolean saveAndClose) {
        enableButton(false);
        setAnualDatas();
        if (!"".equals(errormessage)) {
            Info.show(errormessage, Info.Type.WARNING);
            enableButton(true);
            return;
        }
        LoadingPanel.loading(true);
        HrmsService.App.get().saveEmployeeBenefitAllowance(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer o) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show("Employee benefit allowance saved succesfully", Info.Type.INFO);
                onShellOk(saveAndClose);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_BENEFIT_ALLOWANCE, o, EmployeeBenefitAllowanceView.this);
            }
        });
    }

    private void setAnualDatas() {
        HashMap<Integer, AnnualLeaveItem> allounces = new HashMap<>();
        for (int i = 0; i < tblBenefitAllowance.getRowNumber(); i++) {
            DynamicTableItem item = tblBenefitAllowance.getItem(i);
            AnnualLeaveItem leaveItem = new AnnualLeaveItem();

            Hidden benefitID = (Hidden) item.getColumnById("reasonsid");
            leaveItem.setObjectID(Integer.valueOf(benefitID.getValue()));

            TextBox allowanceDays = (TextBox) item.getColumnById("allowance");
            leaveItem.setAnnualallowancedays(Double.valueOf(allowanceDays.getValue()));

            allounces.put(Integer.valueOf(benefitID.getValue()), leaveItem);
        }
        item.setEmployeeBenefits(allounces);
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
