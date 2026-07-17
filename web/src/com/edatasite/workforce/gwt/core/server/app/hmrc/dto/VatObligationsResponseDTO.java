/*
 * Copyright (c) 2022.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.server.app.hmrc.dto;

import java.util.List;

public class VatObligationsResponseDTO {
    private List<VatObligationsDTO> obligations;

    public VatObligationsResponseDTO() {
    }

    public List<VatObligationsDTO> getObligations() {
        return obligations;
    }

    public void setObligations(List<VatObligationsDTO> obligations) {
        this.obligations = obligations;
    }
}
