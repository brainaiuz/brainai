package com.edatasite.workforce.gwt.core.client.enums;

import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;


public enum AttendanceHoursType {
    NIGHT_SHIFT(-1),
    DUTY(LookUpConstants.EMPLOYEE_ID),
    OVERTIME(LookUpConstants.OVERTIME),
    MANUAL_OR_SHIFT(LookUpConstants.BRIGADA_ID);

    private final Integer code;

    AttendanceHoursType(Integer code) {
        this.code = code;
    }

    public static AttendanceHoursType getByCode(Integer code) {
        if (code != null) {
            for (AttendanceHoursType type : AttendanceHoursType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
        }
        return MANUAL_OR_SHIFT;
    }
}
