package com.edatasite.workforce.gwt.core.client.form;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Hurshid on 2/15/2018.
 */
public class DynamicPanelDropController extends VerticalPanelDropController {

    private Map<String, Map<ColumnType, LinkedList<CustomizeFormItem>>> modelFields;

    public DynamicPanelDropController(VerticalPanel dropTarget, Map map) {
        super(dropTarget);
        modelFields = map;
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);

        VerticalPanel verticalPanel = (VerticalPanel) dropTarget;
        if (verticalPanel.getStyleName().contains("drop-target--empty")) {
            verticalPanel.removeStyleName("drop-target--empty");
        }
        DynamicField dynamicField = (DynamicField) context.draggable;

        if (!dynamicField.isActive()) {
            dynamicField.setActive(true);
        }

        String data = (String) verticalPanel.getLayoutData();
        String newSection = data.split(Constants.DELIMITR)[0];
        ColumnType newColumn = (ColumnType.valueOf(data.split(Constants.DELIMITR)[1]));
        dynamicField.setActiveCommand(null);
        dynamicField.setInactiveCommand(() -> DynamicFormView.inactivateField(newSection, DynamicFormView.inactivePanelMap.get(newSection), verticalPanel, dynamicField));
        //dynamicField.getColumn() - field's last column, used for removing it from last position

        int index = verticalPanel.getWidgetIndex(dynamicField);

        if (!newSection.equals(dynamicField.getSection())) {

            modelFields.get(dynamicField.getSection()).get(dynamicField.getColumn()).remove(dynamicField.getField());

            modelFields.get(newSection).get(newColumn).add(index, dynamicField.getField());

        } else {

            Map<ColumnType, LinkedList<CustomizeFormItem>> columMap = modelFields.get(dynamicField.getSection());

            columMap.computeIfAbsent(newColumn, v -> new LinkedList<>());
            columMap.get(dynamicField.getColumn()).remove(dynamicField.getField());
            columMap.get(newColumn).add(index, dynamicField.getField());
        }

        dynamicField.setSection(newSection);
        dynamicField.setColumn(newColumn);
    }

    @Override
    public void onLeave(DragContext context) {
        super.onLeave(context);

        VerticalPanel verticalPanel = (VerticalPanel) dropTarget;
        if (verticalPanel.getWidgetCount() < 2) {
            if (!verticalPanel.getStyleName().contains("drop-target--empty")) {
                verticalPanel.addStyleName("drop-target--empty");
            }
        }
    }
}
