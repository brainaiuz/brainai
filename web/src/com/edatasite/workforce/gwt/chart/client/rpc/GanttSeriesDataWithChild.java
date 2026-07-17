package com.edatasite.workforce.gwt.chart.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GanttSeriesDataWithChild extends SerieData implements IsSerializable {
    private Integer childCount;
    private Integer member;

    public Integer getMember() {
        return member;
    }

    public void setMember(Integer member) {
        this.member = member;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }
}
