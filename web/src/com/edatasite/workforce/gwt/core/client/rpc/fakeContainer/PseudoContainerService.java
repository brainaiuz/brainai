package com.edatasite.workforce.gwt.core.client.rpc.fakeContainer;

import com.edatasite.workforce.gwt.core.client.rpc.PseudoMenuItem;
import com.google.gwt.user.client.rpc.RemoteService;

import java.util.ArrayList;

public interface PseudoContainerService extends RemoteService {
    ArrayList<PseudoMenuItem> getSettingsMenuItems();

    ArrayList<PseudoMenuItem> getAccountingMenuItems(String moduleName);

    ArrayList<PseudoMenuItem> getCrmMenuItems(String moduleName);

    ArrayList<PseudoMenuItem> getHRMSMenuItems(String moduleName);

    ArrayList<PseudoMenuItem> getPMMenuItems(String moduleName);

    ArrayList<PseudoMenuItem> getPayrollMenuItems(String moduleName);

    ArrayList<PseudoMenuItem> getReportingMenuItems();

    ArrayList<PseudoMenuItem> getDocsMenuItems();

    ArrayList<PseudoMenuItem> getLogisticsMenuItems();

    ArrayList<PseudoMenuItem> getTrainingCentesMenuItems();
}
