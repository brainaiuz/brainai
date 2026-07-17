package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBackupsEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface BackupsEmployeeManager extends Manager<EdsBackupsEmployee> {

    Integer getBackupsEmployeeLastIntNumber();

    List<EdsBackupsEmployee> getAllItems(ListingFilterParameter fp, List<CompanyCustomFieldItem> customFieldItems);

    Integer countBackupEmployee();
}
