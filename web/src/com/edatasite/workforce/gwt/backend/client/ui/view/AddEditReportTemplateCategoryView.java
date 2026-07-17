package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 01.11.11
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */

public class AddEditReportTemplateCategoryView extends View {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final BackendStrings backendStrings = BackendStrings.App.get();

    private final CoreServiceAsync coreService = CoreService.App.get();
    private Integer objectID;

    private SelectItem categoryItem;
    private WfmForm form;
    private WfmForm.Field nameField;
    private TextBox name;
    private WfmButton2 saveButton;

    public AddEditReportTemplateCategoryView() {
        super("addreporttemplatecategory", "Add Report Template Category");
        super.setDescription(backendStrings.addReportTemplateCategory());
    }

    public AddEditReportTemplateCategoryView(Integer objectID) {
        super("addreporttemplatecategory", "Edit Report Template Category");
        super.setDescription(backendStrings.editReportTemplateCategory());
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        name = new TextBox();
        saveButton = new WfmButton2(wfmStrings.save(), event -> save());
        form = new WfmForm();
        nameField = form.addField(wfmStrings.name(), name, true);
        form.addButton(saveButton);
        add(form);
        if (objectID != null) {
            coreService.getReportTemplateCategory(objectID, new AbstractAsyncCallback<SelectItem>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(SelectItem result) {
                    categoryItem = result;
                    name.setText(result.getName());
                }
            });
        }

        return null;
    }

    private void save() {
        if(!Validation.validateTextBoxRequired(name, nameField)) {
            return;

        }
        if(categoryItem == null)
            categoryItem = new SelectItem();
        categoryItem.setName(name.getText());
        coreService.saveOrUpdateReportTemplateCategory(categoryItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.reportTemplate()), Info.Type.INFO);
                closeTab();
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
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
