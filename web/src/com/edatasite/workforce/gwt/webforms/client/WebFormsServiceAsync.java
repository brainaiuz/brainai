package com.edatasite.workforce.gwt.webforms.client;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 7:40:39 PM
 */
public interface WebFormsServiceAsync {

    void getWebForm(Integer formID, Integer companyID, AsyncCallback<WebForm> callback);

    void decryptLink(String url, AsyncCallback<WebForm> callback);

    void fillDropDowns(Integer companyID, String form, AsyncCallback<HashMap<String, SelectItem[]>> callback);

    void saveForm(WebFormItem webFormItem, AsyncCallback<HashMap<String, String>> asyncCallback);

    void getCustomFields(ViewName viewName, AsyncCallback<ArrayList<CompanyCustomFieldItem>> asyncCallback);

    void sendEmailNotifications(Integer webFormID, Integer entityID, AsyncCallback<Void> asyncCallback);
}
