package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

/**
 * Author: Azazello
 * Date: 4/10/2018
 * Time: 5:41 PM
 */
public class CustomFieldQuickAdd extends KpiSideNavBox {
    private WfmButton2 save;
    private Integer objectID;
    private CustomFieldSection section;
    private ItemTableEnum itemTableEnum;
    private Command command;
    private String entityCategoryName;
    private String formId, fieldSection, itemTableName;

    public CustomFieldQuickAdd(CustomFieldSection section, ItemTableEnum itemTableEnum, String uuid, Integer objectID, Command command) {
        super(KpiSideNavBox.DEFAULT_WIDTH);
        this.objectID = objectID;
        this.section = section;
        this.itemTableEnum = itemTableEnum;
        this.command = command;
        this.entityCategoryName = uuid;
        initialize();
    }

    public CustomFieldQuickAdd(String formId, String fieldSection, String itemTableName, CustomFieldSection section, ItemTableEnum itemTableEnum, String uuid, Integer objectID, Command command) {
        super(KpiSideNavBox.DEFAULT_WIDTH);
        this.objectID = objectID;
        this.section = section;
        this.itemTableEnum = itemTableEnum;
        this.command = command;
        this.entityCategoryName = uuid;
        this.formId = formId;
        this.fieldSection = fieldSection;
        this.itemTableName = itemTableName;
        initialize();
    }

    private void initialize() {
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.properties());
        addHeader(header);

        CustomFieldForm quickAddForm = new CustomFieldForm(section, itemTableEnum, entityCategoryName, objectID, formId, fieldSection, itemTableName);
        addBody(quickAddForm);

        addOpeningHandler(event -> quickAddForm.getQuickData());

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(event -> {
            enableButtons(false);
            if (quickAddForm.validate()) {
                quickAddForm.save();
            } else {
                enableButtons(true);
            }
        });

        addFooter(save);

        quickAddForm.setCommand(() -> {
            if (command != null) {
                command.execute();
            }
            remove();
        });
        quickAddForm.setButtonCommand(() -> enableButtons(true));
        show();
    }

    private void enableButtons(boolean enable) {
        save.setEnabled(enable);
    }
}
