package com.edatasite.workforce.gwt.invoice.client.ui.view.itemtablesettings;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.profile.client.ui.view.AbstractAddCustomFieldsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by Normurod on 3/22/2017.
 */
public class AddItemTableCustomFieldView extends AbstractAddCustomFieldsView {

    private CustomFieldSection section;
    private KpiModal dialogBox;

    public AddItemTableCustomFieldView(CustomFieldSection section, Integer objectID, Command command) {
        super("addcustomfield", "Item table custom field");
        this.section = section;
        this.objectID = objectID;
        this.commandProvider = command;
        this.isItemTableField = true;

        dialogBox = new KpiModal();
        pnlDialogContainer = new VerticalPanelDiv();

        dialogBox.add(pnlDialogContainer);
        dialogBox.setWidth("750px");
        dialogBox.setTitle(section != null ? section.getTitle() + " custom field" : "Item table custom field");

        asyncOnInitialize(new AsyncCallback<Widget>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Widget widget) {
                for (Widget button : buttons) {
                    dialogBox.addButton(button);
                }
                dialogBox.open();
            }
        });
    }

    @Override
    protected String getFormName() {
        return section != null ? section.getTitle() + " custom field" : "Item table custom field";
    }

    @Override
    protected SelectItem[] getUiTypes() {
        SelectItem[] uiTypes = new SelectItem[2];
        uiTypes[0] = new SelectItem(0, UI_TYPE_TEXTBOX);
        uiTypes[1] = new SelectItem(1, UI_TYPE_DROPDOWN);
        uiTypes[2] = new SelectItem(2, UI_TYPE_DATEPICKER);

        return uiTypes;
    }

    @Override
    protected SelectItem[] getRelatesToNames() {
        if (section != null) {
            int index = 0;

            ArrayList<SelectItem> items = new ArrayList<>();
            items.add(new SelectItem(index++, section.getTitle(), section.name()));

            return items.toArray(new SelectItem[]{});
        }
        return new SelectItem[0];
    }

    @Override
    protected void closeDialog() {
        dialogBox.close();
    }

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
