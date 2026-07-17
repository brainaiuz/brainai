package com.edatasite.workforce.gwt.core.client.rpc.department;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DepartmentItem implements IsSerializable {
    private Integer defaultDepartmentId;
    private String departmentName;
    private Integer depatmentID;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepatmentID() {
        return depatmentID;
    }

    public void setDepatmentID(Integer depatmentID) {
        this.depatmentID = depatmentID;
    }

    public Integer getDefaultDepartmentId() {
        return defaultDepartmentId;
    }

    public void setDefaultDepartmentId(Integer defaultDepartmentId) {
        this.defaultDepartmentId = defaultDepartmentId;
    }

    public static SelectItem[] asSelectItem(DepartmentItem[] departmentsSelectItem) {
        if(departmentsSelectItem == null){
            return null;
        }
        SelectItem[] selectItems = new SelectItem[departmentsSelectItem.length];
        if(departmentsSelectItem.length > 0){
            int i = 0;
            for(DepartmentItem item : departmentsSelectItem){
                selectItems[i++] = new SelectItem(item.getDepatmentID(), item.getDepartmentName());
            }
        }
        return selectItems;
    }
}
