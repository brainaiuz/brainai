package com.edatasite.workforce.gwt.core.client.form.formbuild;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

public class CustomFormQuickAddView extends KpiSideNavBox {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer objectId;
    private String moduleCode;
    private boolean copy;

    public CustomFormQuickAddView(Integer objectId, String module) {
        super();
        this.objectId = objectId;
        this.moduleCode = module;
        initialize();
    }

    public CustomFormQuickAddView(Integer objectId, String module, boolean copy) {
        super();
        this.objectId = objectId;
        this.moduleCode = module;
        this.copy = copy;
        initialize();
    }

    private void initialize() {
        setStyleName(getElement(), "quick-add", true);

        CustomFormQuickAddForm quickAddForm = new CustomFormQuickAddForm(objectId, moduleCode, copy);

        Heading header = new Heading(HeadingSize.H1);

        if (this.objectId != null) {
            header.setText(wfmStrings.editForm());
        } else {
            header.setText(wfmStrings.addForm());
        }

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
//        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);

        saveButton.addClickHandler(event -> {
//            saveButton.setEnabled(false);
//            cancelButton.setEnabled(false);
            if (quickAddForm.validate()) {
                quickAddForm.save();
            } else {
                saveButton.setEnabled(true);
//                cancelButton.setEnabled(true);
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOM_FORM_SAVE_BUTTON_ENABLE, CustomFormQuickAddView.this, (sender, args) -> {
            saveButton.setEnabled(true);
//            cancelButton.setEnabled(true);
        });

//        cancelButton.addClickHandler(event -> {
//            quickAddForm.clearForm();
//            close();
//        });
        quickAddForm.setCommand(object -> {
//            cancelButton.setEnabled(true);
            saveButton.setEnabled(true);
            String formID = null;
            if (object != null) {
                formID = (String) object;
            }
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FORM_BUILD, null, CustomFormQuickAddView.this);
            if (formID != null && formID.length() > 0) {
                quickAddForm.clearForm();
                hide();
                if (objectId == null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm2|add/add" + "/" + formID + "/");
                }
            } else {
                hide();
            }
        });

        addOpeningHandler(event -> quickAddForm.getData());

        addHeader(header);
        addBody(quickAddForm);
        addFooter(saveButton);
//        addFooter(cancelButton);
        show();
    }

    public void setValues(Integer id, String s, boolean copy) {
        this.objectId = id;
        this.moduleCode = s;
        this.copy = copy;
        clear();
        initialize();
    }
}
