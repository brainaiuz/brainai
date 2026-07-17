package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dialogs;

import com.edatasite.workforce.gwt.core.client.enums.ChildOrientation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.OrgChartColorSchema;
import com.edatasite.workforce.gwt.team.client.rpc.request.CreateDepartmentReq;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.UnorderedList;

public class AddSubDepartment extends KpiModal {

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final DataListBox departments;
    private final KpiTextBox departmentName;
    private final KpiTextArea shortDescription;
    private final KpiTextArea fullDescription;
    private final OrgChartColorSchema colorPanel;

    private boolean isVertical = true;

    public AddSubDepartment(DepartmentNode currentNode, SelectItem[] depItems, SaveHandler saveHandler) {
        addStyleName("orgBoardPopup");

        setTitle(hrmsStrings.addFunction());
        // ПОЛУЧАЕМ ДОСТУП К SPAN ИЗ БАЗОВОГО КЛАССА И ДОБАВЛЯЕМ КЛАСС
        // modalTitle — это protected/private поле, но у нас есть доступ к header
        if (getModalHeader() != null && getModalHeader().getWidgetCount() > 0) {
            // Первый виджет в modalHeader — это и есть наш Span modalTitle
            Widget titleWidget = getModalHeader().getWidget(0);
            titleWidget.addStyleName("modal-title");
        }

        departmentName = new KpiTextBox();
        shortDescription = new KpiTextArea();
        fullDescription = new KpiTextArea();

        FlowPanel orientationPanel = createOrientationPanel();
        colorPanel = new OrgChartColorSchema(false);

        departments = new DataListBox(false, false, true);
        departments.setItems(depItems);
        departments.setSelected(currentNode.getId());

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            if (saveHandler != null) {
                CreateDepartmentReq req = new CreateDepartmentReq();
                req.setName(departmentName.getText());
                req.setShortDescription(shortDescription.getText());
                req.setDescription(fullDescription.getText());
                req.setOrientation(isVertical ? ChildOrientation.VERTICAL : ChildOrientation.HORIZONTAL);
                req.setColor(colorPanel.getColor());
                req.setParentId(departments.getSelectedId());
                saveHandler.onSave(req);
                close();
            }
        });
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> close());

        addWidget(departmentName, wfmStrings.name());
        addWidget(shortDescription, hrmsStrings.keyDeliverableWithoutDot());
        addWidget(fullDescription, hrmsStrings.workActiviteisWithoutDot());
        addWidget(orientationPanel, hrmsStrings.subfunctionDirection());
        addWidget(colorPanel, wfmStrings.color());
        addWidget(departments, hrmsStrings.parentFunction());

        addButton(save);
        addButton(cancel);
    }

    private FlowPanel createOrientationPanel() {
        FlowPanel orientationPanel = new FlowPanel();
        orientationPanel.setStyleName("dirModeTabs");

        UnorderedList orientationList = new UnorderedList();
        orientationList.setStyleName("tabs");

        ListItem vertical = new ListItem();
        vertical.setStyleName("tab dirModeTabs__tab");

        MaterialLink verticalLink = new MaterialLink();
        verticalLink.add(new SvgIcon(SvgEnum.stackY));
        verticalLink.setText(hrmsStrings.vertically());
        verticalLink.setStyleName("active");

        vertical.add(verticalLink);

        ListItem horizontal = new ListItem();
        horizontal.setStyleName("tab dirModeTabs__tab");

        MaterialLink horizontalLink = new MaterialLink();
        horizontalLink.add(new SvgIcon(SvgEnum.stackX));
        horizontalLink.setText(hrmsStrings.horizontally());
        horizontal.add(horizontalLink);

        verticalLink.addClickHandler(e -> {
            isVertical = true;
            verticalLink.setStyleName("active");
            horizontalLink.removeStyleName("active");
        });

        horizontalLink.addClickHandler(e -> {
            isVertical = false;
            verticalLink.removeStyleName("active");
            horizontalLink.setStyleName("active");
        });

        ListItem indicator = new ListItem();
        indicator.setStyleName("indicator");

        orientationList.add(vertical);
        orientationList.add(horizontal);
        orientationList.add(indicator);
        orientationPanel.add(orientationList);

        return orientationPanel;
    }

    public interface SaveHandler {
        void onSave(CreateDepartmentReq req);
    }
}
