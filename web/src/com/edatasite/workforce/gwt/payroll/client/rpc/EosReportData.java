package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/19/15
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class EosReportData implements IsSerializable {

    private ArrayList<EoSCalculationData> eoSCalculationData;

    private Integer totalCount;

    public EosReportData() {

    }

    public EosReportData(ArrayList<EoSCalculationData> eoSCalculationData, Integer totalCount) {
        this.eoSCalculationData = eoSCalculationData;
        this.totalCount = totalCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<EoSCalculationData> getEoSCalculationData() {
        return eoSCalculationData;
    }

    public void setEoSCalculationData(ArrayList<EoSCalculationData> eoSCalculationData) {
        this.eoSCalculationData = eoSCalculationData;
    }
}
