package com.edatasite.workforce.gwt.chart.client.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 11.12.2009
 * Time: 19:45:53
 * To change this template use File | Settings | File Templates.
 */
public enum ChartTypeEnum implements Serializable, IsSerializable {
    NONE(1, "None", "nochart"),
    VERTICAL_BAR_CHART(2, "Vertical Bar", "verticalchart"),
    HORIZONTAL_BAR_CHART(3, "Horizontal Bar", "horizontalchart"),
    LINE_CHART(4, "Line", "linechart"),
    AREA_CHART(5, "Area", "areachart"),
    PIE_CHART(6, "Pie", "piechart"),
    DONUT_CHART(7, "Donut", "donutchart"),
    SEMI_CIRCLE_DONUT_CHART(8, "Semi Circle", "semichart"),
    GAUGE_CHART(9, "Gauge", "gaugechart"),
    FUNNEL_CHART(10, "Funnel", "funnelchart"),
    BASIC_KPI(11, "BasicKpi", "basicchart"),
    STANDARD_KPI(12, "StandardKpi", "standartchart"),
    GROWTH_KPI(13, "GrowthKpi", "growthchart"),
    RANKING_KPI(14, "RankingKpi", "rankingchart"),
    PYRAMID_CHART(15, "Pyramid", "pyramidchart");

    private int Id;
    private String title;
    private String styleName;
//    private String svgUrl;

    ChartTypeEnum(int Id, String title, String iconClass) {
        this.Id = Id;
        this.title = title;
        this.styleName = iconClass;
//        this.svgUrl = "mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#" + iconClassType;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return title;
    }

    public String getStyleName() {
        return styleName;
    }

    public static ChartTypeEnum getById(int id) {
        for (ChartTypeEnum e : values()) {
            if (e.getId() == id) return e;
        }
        return null;
    }

//    public String getSvgUrl() {
//        return svgUrl;
//    }
}
