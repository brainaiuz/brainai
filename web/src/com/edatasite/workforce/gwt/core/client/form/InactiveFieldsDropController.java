package com.edatasite.workforce.gwt.core.client.form;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.FlowPanelDropController;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created by Hurshid on 2/16/2018.
 */
public class InactiveFieldsDropController extends FlowPanelDropController {

    InactiveFieldsDropController(FlowPanel dropTarget) {
        super(dropTarget);
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);
        FlowPanel flowPanelDropTarget = (FlowPanel) dropTarget;
        String newSection = (String) flowPanelDropTarget.getLayoutData();
        DynamicField dynamicField = (DynamicField) context.draggable;

        if (dynamicField.isActive()) {
            dynamicField.setActive(false);
        }
        dynamicField.setInactiveCommand(null);
        dynamicField.setActiveCommand(() -> DynamicFormView.activateField(newSection, flowPanelDropTarget, dynamicField));
        dynamicField.setSection(newSection);
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {
        super.onPreviewDrop(context);

        DynamicField field = (DynamicField) context.draggable;

        if (field.isActive() && field.getField().isSystemMandatory()) {
            Info.warn("Mandatory field cannot be inactive");

            throw new VetoDragException();

        }
    }
}
