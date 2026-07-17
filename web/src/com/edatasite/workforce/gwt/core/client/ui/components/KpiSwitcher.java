package com.edatasite.workforce.gwt.core.client.ui.components;

import gwt.material.design.client.ui.MaterialSwitch;

/**
 * Created by: Azazello
 * Date: 1/26/2018
 * Time: 1:37 PM
 */
public class KpiSwitcher extends MaterialSwitch {
    public KpiSwitcher() {
        super();

        getOffLabel().setClass("switch__label--left");
        getOnLabel().setClass("switch__label--right");
    }

    public KpiSwitcher(String onLabel, String offLabel, Boolean value) {
        super(onLabel, offLabel, value);
    }
}
