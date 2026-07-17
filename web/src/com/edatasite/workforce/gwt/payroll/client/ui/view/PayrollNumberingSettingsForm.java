package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omonullo on 5/13/2017.
 */
public class PayrollNumberingSettingsForm extends FooteredView {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);

    public PayrollNumberingSettingsForm() {
        super("payrollnumberingform", wfmStrings.numberingSettings());
    }

    private PayrollNumberingSettingsUIForm numberingView;
    private WfmButton2 saveButton;

    @Override
    public String getIconStyle() {
        return "accountMark ac-type-num-settings";
    }

    protected Widget onInitialize() {

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId("Payroll_numbering_save_button");


        LoadingPanel.loading(true);
        ProfileService.App.get().getPayrollNumberingSettings(new AbstractAsyncCallback<PMNumberingSettings>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void success(PMNumberingSettings result) {
                LoadingPanel.loading(false);
                numberingView = new PayrollNumberingSettingsUIForm(result);
                PayrollNumberingSettingsForm.this.add(numberingView.getRootElement());
                saveButton.addClickHandler(clicl -> {
                    numberingView.save();
                });

                add(createFooter());
            }
        });
        return null;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return PayrollNumberingSettingsForm.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return PayrollNumberingSettingsForm.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = new ArrayList<>();

        Div saveWrapper = new Div();
        saveWrapper.add(saveButton);
        rightSideWidgets.add(saveWrapper);

        return rightSideWidgets;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
