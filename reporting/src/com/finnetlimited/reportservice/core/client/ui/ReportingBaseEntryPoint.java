package com.finnetlimited.reportservice.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionSettings;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 19.03.2011
 * Time: 21:51:16
 * To change this template use File | Settings | File Templates.
 */

/**
 * Current entry point class has to store general ui related parts
 * of the page that helps avoid from code redundancy.
 */
public abstract class ReportingBaseEntryPoint extends WorkforceEntryPoint implements EntryPoint {

    /**
     * All necessary css sources has to be called here in
     * order to work with them later in each section.
     */
    interface GlobalResources extends ClientBundle {
        /* @CssResource.NotStrict
                @Source("com/finnetlimited/reportservice/core/client/css/reset.css")
                CssResource css();
        */
        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/Core.css")
        CssResource core();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/DatePicker.css")
        CssResource datePicker();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/Blue.css")
        CssResource blue();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/Maroon.css")
        CssResource maroon();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/Grey.css")
        CssResource grey();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/Green.css")
        CssResource green();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/Violet.css")
        CssResource violet();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/IPE.css")
        CssResource ipe();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/Mediacom.css")
        CssResource mediacom();

        @CssResource.NotStrict
        @Source("com/finnetlimited/reportservice/core/client/css/ie8-reporting.css")
        CssResource ie8();
    }

    public static GlobalResources globalResources = GWT.create(GlobalResources.class);

//    public static WfmModuleSetting moduleSetting = new WfmModuleSetting();

    @Override
    public void onModuleLoad() {

        initWfmCustomParams();

        final boolean hasSession = UserClientUtils.setSessionID();
        if (!hasSession) {
            return;
        }
        /*    GWT.<GlobalResources>create(GlobalResources.class).css().ensureInjected();
           globalResources.css().ensureInjected();*/
        globalResources.core().ensureInjected();
        globalResources.datePicker().ensureInjected();
        globalResources.blue().ensureInjected();
        globalResources.maroon().ensureInjected();
        globalResources.grey().ensureInjected();
        globalResources.green().ensureInjected();
        globalResources.violet().ensureInjected();
        globalResources.ipe().ensureInjected();
        globalResources.mediacom().ensureInjected();
        if (Utils.isIE()) {
            globalResources.ie8().ensureInjected();
        }
        loadUserPermissions();
    }

    @Override
    protected void loadUserPermissions() {
        //Load current user's permissions, all permissions of his roles are accumulated
        loadPermission();
    }

    public static void loadPermission() {
        RolePermissionService.App.get().getPermissionSettings(PermissionConstants.REPORTING, new AbstractAsyncCallback<PermissionSettings>() {
            @Override
            public void failure(Throwable t) {

            }

            @Override
            public void success(PermissionSettings settings) {
                Utils.setSettings(settings);
            }
        });
    }

    /**
     * Loads proper ui and other data related components
     * according necessary module.
     *
     * @param root - RootPanel that adds to itself all other widgets.
     */
    protected abstract void onLoad(RootPanel root);

    protected abstract void setContent(String value);


    /**
     * <i>... This is method read wfm custom template settings ...</i>
     * <br/>
     * <i>... Write developer {N.Buriev} ...</i>
     * <br/>
     * <i>... Created Date {14:28 11/05/2011} ...</i>
     * <br/>
     * <b>... if are you not understanding whay uses that mudule setting say with N.Buriev ...</b>
     */
}
