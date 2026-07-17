package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 7/11/12
 * Time: 1:35 PM
 */
public class EditVacancyForm extends AddVacancyView implements Colapse {

    public EditVacancyForm(Integer objectID) {
        super("editVacancy", wfmStrings.edit() + " " + Property.get(VACANCY, wfmStrings.vacancy()), objectID);
    }

    public EditVacancyForm(String formType, Integer convertedId) {
        super(formType, convertedId);
    }

    @Override
    protected void addButtons() {
        super.addButtons();
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return super.getFormType();
    }

    @Override
    public String getIconStyle() {
        return super.getIconStyle();
    }

    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}