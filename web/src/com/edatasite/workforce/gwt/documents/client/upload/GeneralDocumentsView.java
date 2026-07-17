package com.edatasite.workforce.gwt.documents.client.upload;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_EMPLOYEE_PROFILE;

/**
 * User: Ruslan Muhammadov
 * Date: Jul 3, 2010
 * Time: 5:21:03 PM
 */

/**
 * Current class includes all document related conception. General
 * or similar methods will be run only here in order to get class more
 * understandable and controllable. Here you can see centralized logic
 * and main conception should be managed from this class.
 * <p/>
 * Main attention to developers who wants to make changes to current class
 * is that they either MUST learn the conception and act according to it.
 * With such action we can avoid from redundancy in class and method levels.
 */
public abstract class GeneralDocumentsView extends CustomForm {

    protected int folderType;
    protected Integer folderId;
    protected Integer entityId;
    protected EmployeeLookUpWithCode employeeLookUp;
    protected GeneralFileUpload generalFileUpload;
    protected String typeCode;

    public GeneralDocumentsView(String name, String description, int folderType, Integer folderId, Integer entityId, String typeCode) {
        super(name, description);
        this.folderType = folderType;
        this.folderId = folderId;
        this.entityId = entityId;
        this.typeCode = typeCode;
    }

    @Override
    protected void addButtons() {
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EMPLOYEE_CLIENT_DOCUMENTS_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        employeeLookUp = new EmployeeLookUpWithCode();
        employeeLookUp.ensureDebugId("employeeName");
        if (entityId != null && folderId.equals(F_EMPLOYEE_PROFILE)) {
            AllInOneService.App.get().getEmployeeAsSelectItem(entityId, new AsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(SelectItem selectItem) {
                    employeeLookUp.setSelected(selectItem);
                }
            });
        } else {
            employeeLookUp.selectCurrentUser();
        }
        employeeLookUp.addStyleName(Constants.DEFAULT_WIDTH);
        if (entityId != null || !Utils.hasPermission(PermissionConstants.VIEW_ALL_EMPLOYEE_DOCUMENTS)) {
            employeeLookUp.setEnabled(false);
        }
        employeeLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            entityId = employeeLookUp.getSelectedItemID();
            generalFileUpload.setIDData(employeeLookUp.getSelectedItemID(), employeeLookUp.getSelectedItemID());
            generalFileUpload.clear();
        });

        addField(CustomFormConstants.EMPLOYEE, employeeLookUp, getTitle(wfmStrings.employee(), false));

        //employee|or|client attachment
        generalFileUpload = new GeneralFileUpload(folderType, folderId, true, entityId, typeCode);
        generalFileUpload.ensureDebugId("attachment");

        addField(CustomFormConstants.ATTACHMENTS, generalFileUpload, wfmStrings.attachments());
        show();
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}