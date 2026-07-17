package com.edatasite.workforce.rest.v3.release10.core.to.pm;

import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

public class AssigneeDto extends ItemDto {
    private Integer projectEmployeeId;
    private Integer empoyeeId;

    public AssigneeDto(PositionsSelectItem selectItem) {
        super(selectItem.getId(), selectItem.getName(), selectItem.getCode());
        this.projectEmployeeId = selectItem.getId();
        this.empoyeeId = selectItem.getEmployeeId();
    }

    public AssigneeDto(Integer id, String name, String code, String color, Integer projectEmployeeId, Integer empoyeeId) {
        super(id, name, code, color);
        this.projectEmployeeId = projectEmployeeId;
        this.empoyeeId = empoyeeId;
    }

    public AssigneeDto() {
    }

    public Integer getProjectEmployeeId() {
        return projectEmployeeId;
    }

    public void setProjectEmployeeId(Integer projectEmployeeId) {
        this.projectEmployeeId = projectEmployeeId;
    }

    public Integer getEmpoyeeId() {
        return empoyeeId;
    }

    public void setEmpoyeeId(Integer empoyeeId) {
        this.empoyeeId = empoyeeId;
    }
}
