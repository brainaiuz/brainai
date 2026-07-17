package com.edatasite.workforce.gwt.documents.client.container;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.form.AddCustomFormItemView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemView;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;

import java.util.LinkedList;

public class DocumentsSinksContainer extends SinksContainer {

    public DocumentsSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    protected void initViews() {
        DocumentsView view = DocumentsView.get();
        view.setSelectfolderbyvew(true);
        addView(view);

        if (Utils.properties.size() > 0) {
            for (PropertyItem item : Utils.properties.values()) {
                if (item.getfID() != null && item.isCustom() && Utils.hasPermission(item.getFormID() + "_" + Utils.getCompanyID())) {
                    if (Constants.PAGE.equals(item.getType())) {
                        if (item.getSelectedItemID() != null && Utils.hasPermission(item.getFormID() + "_SUMMARY_" + Utils.getCompanyID())) {
                            addView(new CustomFormItemView(item.getSelectedItemID(), item.getfID(), item.getFormID(), item.getPlural(), true));
                        } else if (item.getSelectedItemID() != null && Utils.hasPermission(item.getFormID() + "_EDIT_" + Utils.getCompanyID()) || Utils.hasPermission(item.getFormID() + "_ADD_" + Utils.getCompanyID())) {
                            addView(new AddCustomFormItemView(item.getSelectedItemID(), item.getfID(), item.getFormID(), item.getPlural(), true));
                        }
                    } else {
                        addView(new CustomFormItemListView(item.getfID(), item.getPlural(), item.getFormID()));
                    }
                }
            }
        }
    }

    @Override
    public void activate(View view) {
        super.activate(view);
        if (view instanceof DocumentsView) {
            DocumentsView.setSingleton((DocumentsView) view);
        }
    }

    @Override
    public void reInit() {
        super.reInit();
        if (getWorkarea() != null && getWorkarea().getCurrentView() != null && getWorkarea().getCurrentView() instanceof DocumentsView) {
            DocumentsView.setSingleton((DocumentsView) getWorkarea().getCurrentView());
            DocumentsView dv = DocumentsView.get();
            if (dv.getFolders() != null && dv.isSelectfolderbyvew()) {
                switch (dv.getName()) {
                    case Constants.DOCUMENTS_FOLDER_SHARED:
                        dv.getFolders().selectSharedByMe();
                        break;
                    case Constants.DOCUMENTS_FOLDER_OTHERS:
                        dv.getFolders().selectSharedWithOthers();
                        break;
                    case Constants.DOCUMENTS_FOLDER_TRASH:
                        dv.getFolders().selectTrash();
                        break;
                    case Constants.DOCUMENTS_FOLDER_ALL:
                        dv.getFolders().selectAll();
                        break;
                    case Constants.DOCUMENTS_FOLDER_MYFOLDERS:
                        dv.getFolders().selectMyFolders();
                        break;
                    case Constants.DOCUMENTS_FOLDER_PUBLIC:
                        dv.getFolders().selectPublic();
                        break;
                    case Constants.DOCUMENTS_FOLDER_SYSTEM:
                        dv.getFolders().selectSystemFolders();
                        break;
                }
            }
        }
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}