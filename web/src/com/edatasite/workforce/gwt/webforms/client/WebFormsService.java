package com.edatasite.workforce.gwt.webforms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 7:39:23 PM
 */
public interface WebFormsService extends RemoteService {

    WebForm getWebForm(Integer formID, Integer companyID);

    WebForm decryptLink(String url);

    HashMap<String, SelectItem[]> fillDropDowns(Integer companyID, String form);

    HashMap<String, String> saveForm(WebFormItem webFormItem);

    ArrayList<CompanyCustomFieldItem> getCustomFields(ViewName viewName);

    void sendEmailNotifications(Integer webFormID, Integer entityID);

    class App {
        public static WebFormsServiceAsync get() {
            ServiceDefTarget target = GWT.create(WebFormsService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/webForms");
            return (WebFormsServiceAsync) target;
        }
    }
}
