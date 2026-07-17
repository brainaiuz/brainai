package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoogleAuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.Office365AuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 17.03.14
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class GoogleCalendarQuestionPopup extends KpiModal implements Constants {
    private WfmButton2 saveButton;
    private KpiRadioButton radioButton;
    private KpiRadioButton radioButton2;
    private boolean isGoogle;

    public GoogleCalendarQuestionPopup(boolean isGoogle) {
        super();
        this.isGoogle = isGoogle;
        setSize("600px", "auto");
        setTitle(wfmStrings.pleadeChooseOneTheOption());
        setStyleName("modal-chooseOptions");
        build();
        open();
    }

    private void build() {
        radioButton = new KpiRadioButton("Default");
        radioButton.addClickHandler(clickEvent -> radioButton2.setValue(false));
        radioButton.setText("- " + wfmStrings.synchronize() + " " + Utils.getHelpHost() + " " + (isGoogle ? Property.get(Constants.EVENT_LIST, wfmStrings.eventsWithMyDefaultGoogleCalendar(), wfmStrings.events().toLowerCase()) : "events with my default Office365 calendar"));
        radioButton.setValue(true);

        radioButton2 = new KpiRadioButton("Custom");
        radioButton2.addClickHandler(clickEvent -> radioButton.setValue(false));
        radioButton2.setText("-" + wfmStrings.createSeparate() + " " + Utils.getHelpHost() + " " + (isGoogle ? Property.get(Constants.EVENT_LIST, wfmStrings.eventsCalendarInMyGoogleAccount(), wfmStrings.events()) : "Events calendar in my Office365 account"));

        add(radioButton);
        add(radioButton2);
        add(getMessageHTML(wfmStrings.pleaseNoteThat() + " " + Utils.getHelpHost() + " " + (isGoogle ? wfmStrings.willCreateCalendar() : "will create a separate calendar for Tasks in your Office365 account")));

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveButton);
    }

    private void save(){
        boolean isSyncFromDefaultCalendar = radioButton.getValue();
        if (isGoogle) {
            GoogleAuthorizationPanel.redirectToGoogleCalendarAuthPage(GWT.getModuleName());
        } else {
            new Office365AuthorizationPanel(OFFICE_365_EVENTS, true);
        }
        saveButton.setEnabled(false);
        LoadingPanel.loading(true);
        GoogleCalendarService.App.get().saveCalendarSyncSettings(isSyncFromDefaultCalendar, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                close();
            }
        });
    }

    private Widget getMessageHTML(String text) {
        return new HTML("<span style='color:black; display: block; text-align: center;'>" + text + "</span>");
    }
}
