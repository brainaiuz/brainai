/*
 * Copyright (c) 2022.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.server.app.hmrc.service;

import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.FraudPreventionData;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.UKVatReturn;
import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.VatObligationsDTO;
import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.VatReturnResponseDTO;

import java.util.List;

public interface HmrcMtdServiceLocal {
    List<VatObligationsDTO> retrieveVatObligations(FraudPreventionData fraudPreventionData);

    void loadVatReturnsFromHMRC(FraudPreventionData fraudPreventionData);

    VatReturnResponseDTO submitVatReturnForPeriod(UKVatReturn ukVatReturn, EdsVatReturn edsVatReturn, FraudPreventionData fraudPreventionData);
}
