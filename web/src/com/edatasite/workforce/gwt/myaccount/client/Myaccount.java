package com.edatasite.workforce.gwt.myaccount.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.myaccount.client.ui.factory.MyAccountSinksContainerFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Nov 25, 2008
 * Time: 4:48:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Myaccount extends WorkforceEntryPoint {

	public interface MyAccountStyle extends ClientBundle {
	}

    public void initSinksContainerFactory() {

        if (Utils.hasRole(Constants.ADMIN) && !Utils.hasRole(CLIENT)) {
            containerFactory = new MyAccountSinksContainerFactory(this);
        } else {
            Utils.redirect(GWT.getHostPageBaseURL() + "index.html");
        }
    }
}
