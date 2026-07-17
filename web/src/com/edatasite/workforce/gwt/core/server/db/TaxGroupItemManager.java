package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTaxGroupItem;

import java.util.List;

/**
 * Created by Sherzod on 10/28/2015.
 */
public interface TaxGroupItemManager extends Manager<EdsTaxGroupItem>{
    void deleteGroupItems(Integer taxID);
    List<EdsTaxGroupItem> getGroupItems(Integer taxID);
}
