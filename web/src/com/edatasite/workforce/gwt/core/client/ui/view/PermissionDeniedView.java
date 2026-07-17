package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * User: Fathulla
 * Date: 15.04.13
 * Time: 14:29
 */
public class PermissionDeniedView extends View implements Colapse {
    private int count = 10;
    private String deniedMessage = null;
    private boolean redirect;
    Anchor anchor; /*= new Anchor("TutorialsPoint",
            false,
            "http://www.tutorialspoint.com",
            "_blank");*/
    public PermissionDeniedView() {
        super("denied", wfmStrings.permissionDenied());
        this.deniedMessage = null;
    }

    public PermissionDeniedView(String message) {
        super("denied", wfmStrings.permissionDenied());
        this.deniedMessage = message;
    }

    public PermissionDeniedView(boolean redirect) {
        this();
        this.redirect = redirect;
    }

    public PermissionDeniedView(String message, boolean redirect) {
        this();
        this.redirect = redirect;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        FlowPanel panel = new FlowPanel();
        panel.add(new HTML(this.deniedMessage == null ? wfmStrings.youDontHavePermission() : deniedMessage));
        panel.addStyleName("serviceNote-noPermissionToView"); //https://prnt.sc/rxrksy
//        panel.getElement().getStyle().setFontSize(25, Style.Unit.PX);
        add(panel);
        if (redirect) {
            String section = getFirstAvailableSectionName();

            VerticalPanel vp = getRedirectingPanel(section);
//            vp.getElement().getStyle().setFontSize(25, Style.Unit.PX);
            vp.addStyleName("serviceNote-noPermissionToView--getRedirectingPanel");
            add(vp);
        }
        this.ensureDebugId("PermissionDeinedSinksContainer_PermissionDeniedView");
//        this.getElement().getStyle().setMarginTop(200, Style.Unit.PX);
//        this.getElement().getStyle().setMarginLeft(400, Style.Unit.PX);
        this.addStyleName("serviceNote-noPermissionToView__wrapper");
        return null;
    }

    private VerticalPanel getRedirectingPanel(final String section) {
        VerticalPanel vp = new VerticalPanel();
        anchor = new Anchor(wfmStrings.youAreGoingToBeRedirectedToAnotherAvailableSection() + "</br>" + wfmMessages.nSecondsLeft(String.valueOf(count)), true, GWT.getHostPageBaseURL() + section);
//        anchor.getElement().getStyle().setFontSize(25, Style.Unit.PX);
        vp.add(anchor);
        Timer t = new Timer() {

            public void run() {
                count--;
                anchor.setHTML(wfmStrings.youAreGoingToBeRedirectedToAnotherAvailableSection() + "</br>" + wfmMessages.nSecondsLeft(String.valueOf(count)));
                if (count <= 0) {
                    Utils.redirect(GWT.getHostPageBaseURL() + section);
                    cancel();
                }
            }
        };

        // Schedule the timer to run once every second, 1000 ms.
        t.scheduleRepeating(1000);
        return vp;
    }

    private String getFirstAvailableSectionName() {
        String result = null;
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            result = "Accounting.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.HRMS_MAIN_MENU)) {
            result = "Hrms.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
            result = "Crm.html";
            return result;
        } /*else if (Utils.hasPermission(PermissionConstants.DASHBOARD_MAIN_MENU)) {
            result = "Dashboard.html";
            return result;
        }*/ else if (Utils.hasPermission(PermissionConstants.DOCUMENTS_MAIN_MENU)) {
            result = "Documents.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            result = "ProjectManagement.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.SETTINGS_MAIN_MENU)) {
            result = "Settings.html";
            return result;
        } else if (Utils.hasPermission(PermissionConstants.TC_MAIN_MENU)) {
            result = "TrainingCenter.html";
            return result;
        }
        /**
         * PermissionConstants.ACCOUNTING_MAIN_MENU
         * PermissionConstants.CRM_MAIN_MENU
         * PermissionConstants.DASHBOARD_MAIN_MENU
         * PermissionConstants.DOCUMENTS_MAIN_MENU
         * PermissionConstants.HRMS_MAIN_MENU
         * PermissionConstants.PAYROLL_MAIN_MENU
         * PermissionConstants.PM_MAIN_MENU
         * PermissionConstants.SETTINGS_MAIN_MENU
         * PermissionConstants.TC_MAIN_MENU
         * PermissionConstants.WORKSPACE_MAIN_MENU
         */
        return result;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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