package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.customfields.EdsRentalOrderCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;

import java.util.List;

public interface RentalOrderServiceLocal {

    NumberData generateRentalOrderNumber();

    RentalOrderData getRentalOrderData(Integer objectId, boolean isCopy);

    Boolean deleteRentalOrder(Integer rentalOrderId);

    EdsRentalOrderCustomFields createRentalOrderCustomFields(List<CompanyCustomFieldItem> customFieldItems);

    Integer saveRentalOrderHistory(Integer rentalOrderId, HistoryListItem hisItem);

    }
