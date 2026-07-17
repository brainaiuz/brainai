package com.edatasite.workforce.gwt.profile.client.ui.quickadd;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

/**
 * User: Abror Abdukadirov
 * Date: 13.04.2018 20:30
 */
public class ModuleDashboardQuickAddView extends KpiSideNavBox {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer objectId;

    public ModuleDashboardQuickAddView(Integer objectId) {
        super();
        this.objectId = objectId;
        initialize();
    }

    private void initialize() {
        setStyleName(getElement(), "quick-add", true);

        ModuleDashboardQuickAddForm quickAddForm = new ModuleDashboardQuickAddForm(this.objectId);

        Heading header = new Heading(HeadingSize.H1);
        if (this.objectId != null) {
            header.setText(wfmStrings.editProperties());
        } else {
            header.setText(wfmStrings.addDashboard());
        }

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
//        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);

        saveButton.addClickHandler(event -> {
            saveButton.setEnabled(false);
//            cancelButton.setEnabled(false);
            if (quickAddForm.validate()) {
                quickAddForm.save();
            } else {
                saveButton.setEnabled(true);
//                cancelButton.setEnabled(true);
            }
        });
//        cancelButton.addClickHandler(event -> {
//            quickAddForm.clearForm();
//            remove();
//        });
        quickAddForm.setCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
//                cancelButton.setEnabled(true);
                saveButton.setEnabled(true);
                if (id > 0) {
                    quickAddForm.clearForm();
                    remove();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MODULE_DASHBOARD_ADD, id, ModuleDashboardQuickAddView.this);

                    if (objectId == null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("moduleDashboard|summary/" + id + "/" + true);
                    }
                }
            }
        });

        addOpeningHandler(event -> quickAddForm.getData());

        addHeader(header);
        addBody(quickAddForm);
        addFooter(saveButton);
//        addFooter(cancelButton);
        show();
    }
}
