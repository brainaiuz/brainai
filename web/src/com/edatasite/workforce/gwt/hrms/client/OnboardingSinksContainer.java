package com.edatasite.workforce.gwt.hrms.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.edatasite.workforce.gwt.hrms.client.ui.EmployeeStepListView;
import com.edatasite.workforce.gwt.hrms.client.ui.OnboardingPeriodListView;
import com.edatasite.workforce.gwt.hrms.client.ui.OnboardingStepListView;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ONBOARDING_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ONBOARDING_STEP_LIST;

/**
 * Created by Azazello on 7/7/15.
 */
public class OnboardingSinksContainer extends SinksContainer {
    private static final String ICON_STYLE = "icon-oStep";

    public OnboardingSinksContainer(String name, String description, String[] params) {
        super(name, description, params, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        refreshMap();
        //onboardings stage
        if (Utils.hasPermission(HRMS_ONBOARDING_STEP_LIST)) {
            addView(new OnboardingStepListView());
        }
        //onboardings period
        if (Utils.hasPermission(HRMS_ONBOARDING_LIST)) {
            addView(new OnboardingPeriodListView());
        }
        //custom steps
        initializeEventHandlers();
        if (Hrms.customSteps != null && Hrms.customSteps.size() > 0) {
            int i = 1;
            for (OnboardingItem item : Hrms.customSteps.values()) {
                String permissionCode = PermissionConstants.EMPLOYEE_STEP_ + item.getFormID().replaceAll(Constants.ONBOARDING_STEP_FORM, "") + "_LIST";
                if (Utils.hasPermission(permissionCode)) {
                    addView(new EmployeeStepListView(item.getStepId(), item.getStepName(), item.getFormID(), ICON_STYLE + i));
                }
                if (i == 10) {
                    i = 1;
                } else {
                    i++;
                }
            }
        }
    }

    private void initializeEventHandlers() {
        MaterialPanel eventContainer = new MaterialPanel();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ONBOARDING_STEP_ADD_EDIT, eventContainer, (sender, args) -> {
            OnboardingItem item = (OnboardingItem) args;
            if (item.isCreateForm()) {
                Hrms.customSteps.put(item.getStepId(), item);
                getViewByName().clear();
                getItemsByView().clear();
                renderSinksContainer();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ONBOARDING_STEP_DELETE, eventContainer, (sender, args) -> {
            OnboardingItem item = (OnboardingItem) args;
            boolean found = false;
            for (OnboardingItem s : Hrms.customSteps.values()) {
                if (item.getStepId().equals(s.getParentID())) {
                    s.setParentID(null);
                    found = true;
                }
            }
            if (found || Hrms.customSteps.containsKey(item.getStepId())) {
                Hrms.customSteps.remove(item.getStepId());
                getViewByName().clear();
                getItemsByView().clear();
                renderSinksContainer();
            }
        });
    }

    private void refreshMap() {
        HashMap<Integer, OnboardingItem> result = new LinkedHashMap<>();
        if (Hrms.customSteps != null && Hrms.customSteps.size() > 0) {
            getRec(null, result);
            Hrms.customSteps = result;
        }
    }

    private void getRec(Integer parentID, HashMap<Integer, OnboardingItem> result) {
        for (OnboardingItem item : Hrms.customSteps.values()) {
            if ((parentID == null && item.getParentID() == null) || (parentID != null && parentID.equals(item.getParentID()))) {
                result.put(item.getStepId(), item);
                getRec(item.getStepId(), result);
            }
        }
    }
}
