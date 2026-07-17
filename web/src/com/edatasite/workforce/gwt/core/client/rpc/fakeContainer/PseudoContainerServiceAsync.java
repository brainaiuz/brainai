package com.edatasite.workforce.gwt.core.client.rpc.fakeContainer;

import com.edatasite.workforce.gwt.core.client.rpc.PseudoMenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

public interface PseudoContainerServiceAsync {
    void getSettingsMenuItems(AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getAccountingMenuItems(String moduleName, AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getCrmMenuItems(String moduleName, AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getHRMSMenuItems(String moduleName, AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getPMMenuItems(String moduleName, AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getPayrollMenuItems(String moduleName, AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getReportingMenuItems(AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getDocsMenuItems(AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getLogisticsMenuItems(AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    void getTrainingCentesMenuItems(AsyncCallback<ArrayList<PseudoMenuItem>> callback);

    class App {
        public static PseudoContainerServiceAsync get() {
            ServiceDefTarget target = GWT.create(PseudoContainerService.class);
            target.setServiceEntryPoint("/rpc/pseudoService");
            return (PseudoContainerServiceAsync) target;
        }
    }
}
