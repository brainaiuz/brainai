package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department;

import com.edatasite.workforce.gwt.core.client.enums.DepartmentActionType;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class DepartmentActionsPopup extends PopupPanel {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final DepartmentNode node;
    private final DepartmentActionsHandler handler;

    public DepartmentActionsPopup(DepartmentNode node, DepartmentActionsHandler handler) {
        super(true, true);
        this.node = node;
        this.handler = handler;

        setStyleName("depActionsPopup");

        FlowPanel root = new FlowPanel();
        root.setStyleName("depActionsPopup-list");
        setWidget(root);


        if (!node.isRoot()) {
            root.add(createRow(new SvgIcon(SvgEnum.moveY), hrmsStrings.moveFunctionHierarchy(),
                    DepartmentActionType.MOVE_IN_HIERARCHY));
            if (node.isParentHasMoreThanOneChild()) {
                root.add(createRow(new SvgIcon(SvgEnum.moveX), hrmsStrings.reorderFunction(),
                        DepartmentActionType.MOVE_HORIZONTAL));
            }
        }

        root.add(createRow(new SvgIcon(SvgEnum.plusCircle), hrmsStrings.addSubfunction(),
                DepartmentActionType.ADD_SUB_DEPARTMENT));

        if (node.hasChildrenMoreThanOne()) {
            root.add(createRow(new SvgIcon(SvgEnum.sort), hrmsStrings.sortSubfunctions(),
                    DepartmentActionType.SORT_SUB_DEPARTMENTS));
        }

        root.add(createRow(new SvgIcon(SvgEnum.view), hrmsStrings.openFunction(),
                DepartmentActionType.OPEN_DEPARTMENT));

        if (!node.hasChildren() && !node.isRoot()) {
            root.add(createRow(new SvgIcon(SvgEnum.trash2), wfmStrings.delete(),
                    DepartmentActionType.DELETE_DEPARTMENT));
        }
    }

    private Widget createRow(SvgIcon icon,
                             String labelText,
                             DepartmentActionType actionType) {

        FlowPanel row = new FlowPanel();
        Label label = new Label(labelText);

        row.add(icon);
        row.add(label);

        row.addDomHandler(event -> {
            hide();
            if (handler != null) {
                handler.onDepartmentAction(node, actionType);
            }
        }, ClickEvent.getType());

        return row;
    }
}

