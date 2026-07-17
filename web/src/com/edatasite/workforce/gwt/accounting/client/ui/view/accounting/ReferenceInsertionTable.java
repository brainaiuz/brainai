package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.importfile.client.rpc.CustomisedImportData;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 3, 2011
 * Time: 5:52:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceInsertionTable extends Widget{

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    //private RadioButton fromFile;
    //private RadioButton fromSystem;
    private DataListBox csvValuesDataListBox;
    private Widget sysValuesWidget;
    private Widget sysExtraValuesWidget;
    private WfmForm.Field field;
    private boolean required;
    private String labelName;
    private String helpMessage;
    private WfmForm table;
    private FlexTable mainTable;

    public ReferenceInsertionTable(String name, Widget sysValuesWidget, WfmForm table) {
        this.labelName = name;
        this.sysValuesWidget = sysValuesWidget;
        this.table = table;
        initialize(name);
    }

    public ReferenceInsertionTable(String name, Widget sysValuesWidget, boolean required, WfmForm table) {
        this.labelName = name;
        this.sysValuesWidget = sysValuesWidget;
        this.required = required;
        this.table = table;
        initialize(name);
    }
    public ReferenceInsertionTable(String name, Widget sysValuesWidget, boolean required, WfmForm table, String helpMessage) {
        this.labelName = name;
        this.sysValuesWidget = sysValuesWidget;
        this.required = required;
        this.table = table;
        this.helpMessage = helpMessage;
        initialize(name);
    }

    public ReferenceInsertionTable(String name, Widget sysValuesWidget, Widget sysExtraValuesWidget, WfmForm table) {
        this.labelName = name;
        this.sysValuesWidget = sysValuesWidget;
        this.sysExtraValuesWidget = sysExtraValuesWidget;
        this.table = table;
        initialize(name);
    }

    private void initialize(String name) {
        mainTable = new FlexTable();
        csvValuesDataListBox = new DataListBox();
        csvValuesDataListBox.addStyleName(DEFAULT_WIDTH);
        sysValuesWidget.addStyleName(DEFAULT_WIDTH);
        if (sysExtraValuesWidget != null) {
            sysExtraValuesWidget.addStyleName(DEFAULT_WIDTH);
        }

        mainTable.setWidget(1, 0, csvValuesDataListBox);
        Label label = new Label(accountingMessages.ifNotExistsInSystemChooseDefault(name));
        label.getElement().setAttribute("style","background-color: #e2dcdc;padding-top:4px;padding-bottom:4px;padding-left:4px;");
        mainTable.setWidget(2, 0, label);
        mainTable.setWidget(3, 0, sysValuesWidget);

        if (sysExtraValuesWidget != null) {
            mainTable.setWidget(2, 1, sysExtraValuesWidget);
        }
        mainTable.setCellSpacing(2);

        if(helpMessage!=null){
            field = table.addField(name, mainTable, helpMessage, 4, required);
        }else{
            field = table.addField(name, mainTable, required);
        }
    }

    public FlexTable getTable() {
        return mainTable;
    }


    private void addTypeChangeListener(RadioButton rButton) {
        rButton.addClickHandler(clickEvent -> {
           // csvValuesDataListBox.setEnabled(fromFile.getValue());
            field.setErrorMessage(null, null);
        });
    }

    public DataListBox getCsvDataListBox() {
        return csvValuesDataListBox;
    }

    public Widget getSysValuesWidget() {
        return sysValuesWidget;
    }

    public Widget getSysExtraValuesWidget() {
        return sysExtraValuesWidget;
    }

    public CustomisedImportData getData() {
        CustomisedImportData data = new CustomisedImportData();

        data.setCsvColumnId(csvValuesDataListBox.getSelectedId());

        if (sysValuesWidget instanceof DataListBox) {
            data.setSystemSelectedId(((DataListBox) sysValuesWidget).getSelectedId());
        } else if (sysValuesWidget instanceof WfmDropdown) {
            data.setSystemSelectedId(((WfmDropdown) sysValuesWidget).getSelectedId());
        } else if (sysValuesWidget instanceof LookUp) {
            data.setSystemSelectedId(((LookUp) sysValuesWidget).getSelectedItemID());
        }
        return data;
    }

    public boolean validate() {

        if (sysValuesWidget instanceof DataListBox) {
            return Validation.validateListBoxRequired((DataListBox) sysValuesWidget, field, "Please select " + labelName);
        } else if (sysValuesWidget instanceof WfmDropdown) {
            return Validation.validateWfmDropdown((WfmDropdown) sysValuesWidget, field, true);
        } else if (sysValuesWidget instanceof LookUp) {
            return Validation.validateLookUpRequired((LookUp) sysValuesWidget, field, "Please select " + labelName);
        }
        return true;
    }

    public WfmForm.Field getField() {
        return field;
    }
}
