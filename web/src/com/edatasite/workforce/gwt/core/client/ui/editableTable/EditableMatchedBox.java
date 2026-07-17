package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class EditableMatchedBox extends Composite implements CustomCellInterface {
    private WfmButton2 saveButton;




    public  EditableMatchedBox() {
        createButton();
    }

    private void createButton() {
        final HTMLPanel panel = new HTMLPanel("");
        this.saveButton = new WfmButton2("Match", WfmButton2.BTN_PRIMARY);
        panel.add(saveButton);
        initWidget(panel);
    }


    @Override
    public String getDisplayValue() {
        return "   ";
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {

    }

    public WfmButton2 getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(WfmButton2 saveButton) {
        this.saveButton = saveButton;
    }


}
