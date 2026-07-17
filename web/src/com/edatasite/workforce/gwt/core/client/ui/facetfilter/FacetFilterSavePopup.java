package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Jun-2010
 * Time: 18:01:16
 */
public class FacetFilterSavePopup extends KpiModal implements ClickHandler {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer defaultFilterId;
    private WfmButton2 save;
    private WfmButton2 close;
    private TextBox name;
    private KpiSwitcher defaultFilter;
    private KpiSwitcher publicFilter;

    private WfmForm form;
    private WfmForm.Field nameField;
    private FacetFilterRpc facetFilterRpc;
    private FacetSaveCallback saveCallback;

    public FacetFilterSavePopup(Integer defaultFilterId, FacetFilterRpc facetFilterRpc, FacetSaveCallback saveCallback) {
        setWidth(400);

        form = new WfmForm("45%,50%,5%".split(","));
        //form.setWidth("400px");
        this.add(form);
//        this.setModal(false);
        this.setTitle(wfmStrings.saveFilter());
        this.facetFilterRpc = facetFilterRpc;
        this.saveCallback = saveCallback;
        this.defaultFilterId = defaultFilterId;
        init();
        action();
        drawPopup();
        if (facetFilterRpc.getName() != null && !"".equals(facetFilterRpc.getName())) {
            name.setText(facetFilterRpc.getName());
        }
        if (facetFilterRpc.getObjectID() != null) {
            if (facetFilterRpc.isDefaultFilter() || (facetFilterRpc.getObjectID().equals(defaultFilterId))) {
                defaultFilter.setValue(true);
            }
            if (facetFilterRpc.isPublicFilter()) {
                publicFilter.setValue(true);
            }
        }
    }

    /**
     * Draw save Task Facet Filter Popup
     */
    private void drawPopup() {
        nameField = form.addField(wfmStrings.name(), name, true);
        if (Utils.hasPermission(PermissionConstants.ADD_SYSTEM_FILTER) || Utils.hasRole(Constants.ADMIN)) {
            form.addField(wfmStrings.publicFilter(),publicFilter);
        }
        form.addField(wfmStrings.defaultFilter(), defaultFilter);
        addButton(close);
        addButton(save);
        
//        HorizontalPanel hPanel = new HorizontalPanel();
//        hPanel.setSpacing(5);
//        hPanel.add(save);
//        hPanel.add(close);
//        form.addButton(hPanel);
    }

    /**
     * All events
     */
    private void action() {
        save.addClickHandler(event -> {
            if (validation()) {
                if (!Utils.hasPermission(PermissionConstants.ADD_SYSTEM_FILTER) && facetFilterRpc.isPublicFilter()) {
                    Info.show(wfmStrings.youDontHaveToEditPublicFilter(), Info.Type.INFO);
                }
                save.setEnabled(false);
                close.setEnabled(false);
                facetFilterRpc.setName(name.getText());
                facetFilterRpc.setDefaultFilter(defaultFilter.getValue());
                facetFilterRpc.setPublicFilter(publicFilter.getValue());
                facetApplySave.saveFilter(facetFilterRpc, saveCallback);
            }
        });
        close.addClickHandler(event -> close());
    }

    /**
     * Validaton required fileds
     *
     * @return
     */
    private boolean validation() {
        int error = 0;
        form.cleanupErrors();
        if (!Validation.validateTextBoxRequired(name, nameField)) {
            error++;
        }
        return error <= 0;
    }

    /**
     * Initilalization all Ui objects
     */
    private void init() {
        name = new TextBox();
        defaultFilter = new KpiSwitcher();
        publicFilter = new KpiSwitcher();
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId("FacetFilterSavePopup_save_button");
        close = new WfmButton2(wfmStrings.close());
        close.ensureDebugId("FacetFilterSavePopup_close_button");
    }

    private FacetApplySave facetApplySave;

    public void setSaveFacetFilter(FacetApplySave facetApplySave) {
        this.facetApplySave = facetApplySave;
    }

    @Override
    public void onClick(ClickEvent event) {
        this.close();
    }
}
