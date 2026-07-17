package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department;

import com.edatasite.workforce.gwt.core.client.enums.DepartmentActionType;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;

public interface DepartmentActionsHandler {
    void onDepartmentAction(DepartmentNode node, DepartmentActionType action);
}
