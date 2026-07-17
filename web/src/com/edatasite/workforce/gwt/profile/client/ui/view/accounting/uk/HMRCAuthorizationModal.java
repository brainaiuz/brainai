package com.edatasite.workforce.gwt.profile.client.ui.view.accounting.uk;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class HMRCAuthorizationModal extends KpiModal {

    private AuthModalContent content;
    private AccountingServiceAsync accountingService = AccountingService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public HMRCAuthorizationModal() {
        super();
        setTitle(accountingStrings.authorizeAtHMRC());
        setWidth(900);
        initWidgets();
        open();
    }

    private void initWidgets() {

        content = new AuthModalContent();
        modalContent.add(content);

        WfmButton2 saveButton = new WfmButton2("Save & Authorise Later", WfmButton2.BTN_DEFAULT, clickEvent -> save(false));
        WfmButton2 authorizeButton = new WfmButton2("Authorize Now", WfmButton2.BTN_PRIMARY, clickEvent -> save(true));

        addButton(saveButton);
        addButton(authorizeButton);
    }

    private void save(boolean authorize) {
        if (!content.validate()) {
            Info.warn(wfmStrings.fillAllRequiredFields());
            return;
        }
        LoadingPanel.loading(true);
        accountingService.saveHMRCAuthSettings(content.getData(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()));
                if (authorize) {
                    String hmrcAuthUrl = Utils.getHostURL() + Constants.HMRC_AUTH_URL;
                    Window.open(hmrcAuthUrl, "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,width=900,height=650");
                }
                close();
            }
        });
    }

    @Override
    public void close() {
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VAT_RETURN_FILE_CHANGED, null, this);
        super.close();
    }
}
