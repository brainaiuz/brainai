package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoogleAuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.Office365AuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 17.03.14
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class GoogleContactsQuestionPopup extends KpiModal implements Constants {
    private WfmButton2 saveButton;
    private KpiRadioButton both;
    private KpiRadioButton googleMaster;
    private KpiRadioButton kpiMaster;
    private final String storageType;

    public GoogleContactsQuestionPopup(String storageType) {
        super();
        this.storageType = storageType;
        setWidth("750px");
        setTitle(wfmStrings.pleadeChooseOneTheOption());
        build();
        open();
    }

    private void build() {
        String serverName = storageType.equals(GOOGLE) ? wfmStrings.google() : wfmStrings.office365();
        String hostName = Utils.getHelpHost();

        both = new KpiRadioButton("rb", wfmMessages.wayViceversaUpdatesInBothDirectionsKeepsHostNameContactsAndServerNameContactsInSynchWithEachOther(hostName, serverName), true);
        googleMaster = new KpiRadioButton("rb", wfmMessages.wayServerNameMasterServerNameContactUpdatesAreAppliedToHostNameContactsOnly(serverName, serverName, Utils.getHelpHost()), true);
        kpiMaster = new KpiRadioButton("rb", wfmMessages.wayHostNameMasterHostNameUpdatesAreAppliedToServerNameContactsOnly(Utils.getHelpHost(), Utils.getHelpHost(), serverName), true);
        both.setFormValue("BOTH");
        both.setValue(true);
        googleMaster.setFormValue(Constants.SERVERMASTER);
        kpiMaster.setFormValue(Constants.KPIMASTER);

        add(both);
        add(googleMaster);
        add(kpiMaster);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveButton);
    }

    private void save() {
        if (!validate()) {
            Info.show(wfmStrings.pleaseSelectRequiredFields(), Info.Type.WARNING);
            return;
        }
        String type = "BOTH";
        if (googleMaster.getValue()) {
            type = googleMaster.getFormValue();
        } else if (kpiMaster.getValue()) {
            type = kpiMaster.getFormValue();
        }
        saveButton.setEnabled(false);
        LoadingPanel.loading(true);
        ContactService.App.get().saveContactSyncSettings(type, storageType, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                if (storageType.equals(GOOGLE)) {
                    new GoogleAuthorizationPanel(GOOGLE_CONTACTS, true, GWT.getModuleName(), 3);
                } else {
                    new Office365AuthorizationPanel(OFFICE_365_CONTACTS, true);
                }
                close();
            }
        });
    }

    public boolean validate() {
        return both.getValue() || googleMaster.getValue() || kpiMaster.getValue();
    }
}
