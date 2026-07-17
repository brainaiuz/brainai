package com.edatasite.workforce.gwt.reportingsystem.client.enumerable.chart;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 15.12.2009
 * Time: 16:13:51
 * To change this template use File | Settings | File Templates.
 */
public enum LabelRotationTypeEnum implements IsSerializable {
    VERTICAL(-90),
    HALF_DIAGONAL(-24),
    DIAGONAL(-45),
    HORIZONTAL(0),
    AGAINST_DIAGONAL(45);

    private int degrees;

    LabelRotationTypeEnum(int degrees) {
        this.degrees = degrees;
    }

    LabelRotationTypeEnum() {
    }

    @Override
    public String toString() {
        return String.valueOf(degrees);
    }

}
