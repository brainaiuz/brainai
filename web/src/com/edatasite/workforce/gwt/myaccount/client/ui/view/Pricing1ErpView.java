package com.edatasite.workforce.gwt.myaccount.client.ui.view;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.myaccount.client.bundles.MyaccountImageBundles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Fatxulla
 * Date: 17/06/15
 * Time: 1:51 PM
 */
public class Pricing1ErpView extends View implements Constants {
    interface PricingView1ErpUiBinder extends UiBinder<HTMLPanel, Pricing1ErpView> {
    }

    @UiField
    ListBox selectModule;

    @UiField
    TableElement tableModules;

    @UiField
    KpiCheckBox chbPM;

    @UiField
    KpiCheckBox chbHRMS;

    @UiField
    KpiCheckBox chbCRM;

    @UiField
    KpiCheckBox chbAccounting;

    @UiField
    KpiCheckBox chbPayroll;

    @UiField
    SpanElement numberOfUsersContainer;

    @UiField
    SpanElement packages;

    @UiField
    InputElement numberOfUsers;

    int moduleCount = 0;

    public Pricing1ErpView() {
        super("pricing1ErpViewNEW", "Subscription");
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public FlowPanel getHelpContainer() {
        return null;
    }

    @Override
    public ImageResource getIconImage() {
        return MyaccountImageBundles.App.get().currentSubscription();
    }

    @Override
    protected Widget onInitialize() {
        PricingView1ErpUiBinder ourUiBinder = GWT.create(PricingView1ErpUiBinder.class);
        add(ourUiBinder.createAndBindUi(this));

        selectModule.addChangeHandler(changeEvent -> {
            if (selectModule.getSelectedIndex() > 0) {
                tableModules.getStyle().setDisplay(Style.Display.BLOCK);
            } else {
                tableModules.getStyle().setDisplay(Style.Display.NONE);
            }
            checkModule(true);
        });

        Event.sinkEvents(numberOfUsers, Event.ONCHANGE);
        Event.setEventListener(numberOfUsers, event -> numberOfUsersContainer.setInnerText(numberOfUsers.getValue()));

        chbPM.addClickHandler(clickEvent -> {
            if (chbPM.getValue()) {
                moduleCount++;
                packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "Project Management"));
            } else {
                moduleCount--;
                clearModule();
            }
            checkModuleCount();
        });
        chbHRMS.addClickHandler(clickEvent -> {
            if (chbHRMS.getValue()) {
                moduleCount++;
                packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "HRMS"));
            } else {
                moduleCount--;
                clearModule();
            }
            checkModuleCount();
        });
        chbCRM.addClickHandler(clickEvent -> {
            if (chbCRM.getValue()) {
                moduleCount++;
                packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "CRM"));
            } else {
                moduleCount--;
                clearModule();
            }
            checkModuleCount();
        });
        chbAccounting.addClickHandler(clickEvent -> {
            if (chbAccounting.getValue()) {
                moduleCount++;
                packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "Accounting and Finance"));
            } else {
                moduleCount--;
                clearModule();
            }
            checkModuleCount();
        });
        chbPayroll.addClickHandler(clickEvent -> {
            if (chbPayroll.getValue()) {
                moduleCount++;
                packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "Payroll"));
            } else {
                moduleCount--;
                clearModule();
            }
            checkModuleCount();
        });

        return null;
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

    private void checkModuleCount() {
        checkModule(false);
    }

    private void checkModule(boolean setDefaultValue) {
        int index = selectModule.getSelectedIndex();
        index = index + (index == 3 ? 1 : 0);
        if (index > 0 && index + 1 == moduleCount) {
            if (!chbPM.getValue()) {
                chbPM.setEnabled(false);
            }
            if (!chbHRMS.getValue()) {
                chbHRMS.setEnabled(false);
            }
            if (!chbCRM.getValue()) {
                chbCRM.setEnabled(false);
            }
            if (!chbAccounting.getValue()) {
                chbAccounting.setEnabled(false);
            }
            if (!chbPayroll.getValue()) {
                chbPayroll.setEnabled(false);
            }
        } else {
            if (!chbPM.isEnabled()) {
                chbPM.setEnabled(true);
            }
            if (!chbHRMS.isEnabled()) {
                chbHRMS.setEnabled(true);
            }
            if (!chbCRM.isEnabled()) {
                chbCRM.setEnabled(true);
            }
            if (!chbAccounting.isEnabled()) {
                chbAccounting.setEnabled(true);
            }
            if (!chbPayroll.isEnabled()) {
                chbPayroll.setEnabled(true);
            }
        }
        if (setDefaultValue) {
            packages.setInnerText("");
            moduleCount = 0;
            chbPM.setValue(false);
            chbHRMS.setValue(false);
            chbCRM.setValue(false);
            chbAccounting.setValue(false);
            chbPayroll.setValue(false);
        }
    }

    private void clearModule() {
        packages.setInnerText("");
        if (chbPM.getValue()) {
            packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "Project Management"));
        }
        if (chbHRMS.getValue()) {
            packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "HRMS"));
        }
        if (chbCRM.getValue()) {
            packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "CRM"));
        }
        if (chbAccounting.getValue()) {
            packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "Accounting and Finance"));
        }
        if (chbPayroll.getValue()) {
            packages.setInnerText(packages.getInnerText() + ((!packages.getInnerText().equals("") ? ", " : "") + "Payroll"));
        }
    }
}