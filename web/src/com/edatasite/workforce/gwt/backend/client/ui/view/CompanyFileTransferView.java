package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.enums.FileTranserTypeEnum;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Murad Satimov
 * Date: 8/23/17 5:43 PM
 */
public class CompanyFileTransferView extends View {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private final Integer companyId;

    private DataListBox dataListBox;
    private WfmForm.Field dataListBoxField;

    public CompanyFileTransferView(Integer companyId) {
        super("fileTransferView", backendStrings.companyFileTransfer());
        this.companyId = companyId;
    }

    @Override
    protected Widget onInitialize() {
        final WfmForm wfmForm = new WfmForm(new String[]{"40%", "50%"});

        dataListBox = new DataListBox();
        dataListBox.addListItem(FileTranserTypeEnum.FROM_AMAZON_TO_LOCAL.toSelectItem());
//        dataListBox.addListItem(FileTranserTypeEnum.FROM_AMAZON_TO_AMAZON.toSelectItem());
        dataListBoxField = wfmForm.addField(backendStrings.fileTransferType(), dataListBox);
        wfmForm.addHorizontalLine();
        final WfmButton2 startButton = new WfmButton2(backendStrings.startTransfering());

        wfmForm.addField(null, startButton);
        startButton.addClickHandler(clickEvent -> startImport());
        add(wfmForm);
        return null;
    }

    private void startImport() {
        if (!Validation.validateListBoxRequired(dataListBox, dataListBoxField, "Please, fill this field")) {
            return;
        }
        final SelectItem importType = dataListBox.getSelectedItem();

        LoadingPanel.loading(true);
        BackendService.App.get().startTansferCompanyFile(this.companyId, importType, new AbstractAsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LoadingPanel.loading(false);
                Info.show(result);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
