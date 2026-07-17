package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemSerial;
import com.edatasite.workforce.core.domain.accounting.EdsItemSerialDetail;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialItem;

import java.util.ArrayList;
import java.util.List;

public interface ItemSerialManager extends Manager<EdsItemSerial> {

    EdsItemSerial getSerial(Integer itemID, String serial);

    ArrayList<String> getExistingSerials(Integer itemID, List<String> serials);

    List<SerialItem> getList(ListingFilterParameter fp);

    Integer getTotalCount(ListingFilterParameter fp);

    List<SelectItem> getAvailableSerials(ListingFilterParameter fp);

    List<EdsItemSerial> getSerials(Integer entityId, String entityType);

    List<EdsItemSerialDetail> getSerialDetails(Integer entitiyId, String entityType);

    EdsItemSerialDetail getSerialDetail(String serial, Integer entityId, String entityType);

    void deleteSerialDetail(Integer serialDetailId);

    void deleteSerials(Integer itemID);
}
