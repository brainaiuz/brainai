/*
 * Copyright (c) 2022.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.server.app.hmrc.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.IdName;

public class CheckVatNumberDTO {
    private IdName target;
    private String processingDate;

    public IdName getTarget() {
        return target;
    }

    public void setTarget(IdName target) {
        this.target = target;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }
}
