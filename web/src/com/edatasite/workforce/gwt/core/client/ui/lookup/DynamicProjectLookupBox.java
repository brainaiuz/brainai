package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.html.Div;


public class DynamicProjectLookupBox extends FormGroup {
    private AccountingStrings accountingStrings = AccountingStrings.App.get();
    private WfmStrings wfmStrings = WfmStrings.App.get();
    private ProjectLookUp projectLookUp;
    private HTML namePanel;
    private InputGroup inputGroup;
    private Div appendableDiv;
    private WfmButton2 editButton;
    private WfmButton2 saveButton;
    private WfmButton2 cancelButton;
    public DynamicProjectLookupBox(SelectItem projectItem, String type, Integer clientID, ISaveProject saveCommand) {
        setLabel(Property.get(Constants.PROJECT, wfmStrings.project()));
        projectLookUp = new ProjectLookUp(Constants.RECEIVABLE);
        projectLookUp.setClientSupplierID(clientID);
        projectLookUp.addStyleName("form-control");
        namePanel = new HTML();
        namePanel.addStyleName("form-control");
        inputGroup = new InputGroup(namePanel);
        appendableDiv = new Div("input-group-append");
        editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_WHITE, e -> {
            activateEditMode();
        });
        cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_GREY, e -> {
            activateViewMode();
        });
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, e -> {
            saveCommand.save(projectLookUp.getSelectedItem());
            setSelected(projectLookUp.getSelectedItem());
            activateViewMode();
        });
        setSelected(projectItem);
        activateViewMode();
        setContent(inputGroup);
    }

    private void activateViewMode() {
        inputGroup.clear();
        appendableDiv.clear();
        inputGroup.add(namePanel);
        appendableDiv.add(editButton);
        inputGroup.add(appendableDiv);
    }

    private void activateEditMode() {
        inputGroup.clear();
        appendableDiv.clear();
        inputGroup.add(projectLookUp);

        appendableDiv.add(saveButton);
        appendableDiv.add(cancelButton);

        inputGroup.add(appendableDiv);
    }

    private void setSelected(SelectItem projectItem) {
        if (projectItem == null) {
            namePanel.setText("");
            projectLookUp.clear();
            return;
        }
        namePanel.setText(projectItem.getName());
        projectLookUp.clear();
        projectLookUp.addItem(projectItem);
        projectLookUp.setSelected(projectItem);
    }

    public interface ISaveProject {
        void save(SelectItem projectItem);
    };

}
