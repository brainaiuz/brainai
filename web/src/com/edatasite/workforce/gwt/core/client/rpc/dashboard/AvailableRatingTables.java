package com.edatasite.workforce.gwt.core.client.rpc.dashboard;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 02.07.2009
 * Time: 14:30:23
 * To change this template use File | Settings | File Templates.
 */
public class AvailableRatingTables implements IsSerializable {

    private Integer departmentId;
    private AvailableFiveEmployee[] manyWorks;
    private AvailableFiveEmployee[] fewWorks;


    public AvailableFiveEmployee[] getManyWorks() {
        return manyWorks;
    }

    public void setManyWorks(AvailableFiveEmployee[] manyWorks) {
        this.manyWorks = manyWorks;
    }

    public AvailableFiveEmployee[] getFewWorks() {
        return fewWorks;
    }

    public void setFewWorks(AvailableFiveEmployee[] fewWorks) {
        this.fewWorks = fewWorks;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
