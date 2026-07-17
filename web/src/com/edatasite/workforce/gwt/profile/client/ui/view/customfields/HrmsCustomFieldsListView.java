package com.edatasite.workforce.gwt.profile.client.ui.view.customfields;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldArea;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * User: Normurod Buriev
 * Date: 5/28/12
 * Time: 5:24 PM
 */
public class HrmsCustomFieldsListView extends CustomFieldsListView {

    public HrmsCustomFieldsListView() {
        super("hrmscustomfields", settingsStrings.hrmsCustomFields());
    }

    @Override
    public String getIconStyle() {
        return "icon-tasks";
    }

    protected ListingRequestProvider<CompanyCustomFieldItem> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setEntityName(getHrmsCFEntityNames());
            profileService.getCustomFields(filterParameter, new AbstractAsyncCallback<ListResult<CompanyCustomFieldItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<CompanyCustomFieldItem> customfields) {
                    callback.onSuccess(customfields);
                }
            });
        };
    }

    protected ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newLocation = getAddNewButton();

                newLocation.addClickHandler(baseEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|add/add/" + CustomFieldArea.HRMS));

                return newLocation;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };
    }

    private String getHrmsCFEntityNames() {
        String eNames = "";
        eNames += "'" + ViewName.Employee.name() + "'";
        eNames += ",'" + ViewName.Dependent.name() + "'";
        eNames += ",'" + ViewName.PersonalGoal.name() + "'";    //Personal Goal
        eNames += ",'" + ViewName.DepartmentGoal.name() + "'";  //Department Goal
        eNames += ",'" + ViewName.ProjectGoal.name() + "'";     //Project Goal
        eNames += ",'" + ViewName.BusinessGoal.name() + "'";    //Business Goal
        eNames += ",'" + ViewName.CompanyGoal.name() + "'";     //Company Goal
        eNames += ",'" + ViewName.OnboardingStep.name() + "'";     //Onboarding Step
        eNames += ",'" + ViewName.MeetingMInutesView.name() + "'"; // Meeting Minutes
        eNames += ",'" + ViewName.LeaveRequest.name() + "'"; // Leave Request
        if (Utils.hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
            eNames += ",'" + ViewName.Candidate.name() + "'";     //Recruitment: Candidate
            eNames += ",'" + ViewName.Vacancy.name() + "'";     //Vacancy
            eNames += ",'" + ViewName.Placement.name() + "'";     //Placement
        }
        eNames += ",'" + ViewName.Certificates.name() + "'";      //Certificates
        eNames += ",'" + ViewName.TalentProfileView.name() + "'";      //Talent Profile
        return eNames;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}