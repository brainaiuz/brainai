package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.ListingObjectItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface CompanyManager extends Manager<EdsCompany> {

    EdsCompany getCompany(Integer objectID);

    List<EdsCompany> getCompanies();

    List<EdsCompany> getOccupiedCompanies();

    List<EdsCompany> getCompaniesByRegDate(Date startTime, Date endTime);

    List<EdsCompany> getCompaniesUsedSystemByDate(Date startTime, Date endTime);

    List<Date> getSignupCompaniesByDate(Date startDate, Date endDate);

//    List<EdsCompany> searchCompaniesByName(String name) throws EdsDbException;

    ListingObjectItem<EdsCompany> getNonTestCompanies(ListingFilterParameter fp, List<String> existingCompanyList);

    List<Object[]> getCompanySchemas(ListingFilterParameter fp);

    List<String> getExistingSchemas();

    List<String> getExistingSchemasWithTemplate();

    boolean schemaExists(String schemaName);

    Integer getMaxSchemaId();

    List<Object[]> getSchemaList(ListingFilterParameter fp);

    List<EdsCompany> getCompaniesByIDs(String objectIDs);

    List<Integer> getCompaniesId();

    Integer getCurrentFreeSchemasCount();

    List<Integer> getReallyExistingCompanyIds();

    ArrayList<String> getXmlBackupEnableCompanies();

    List<Integer> getCompaniesIdsList(Boolean isPaid);

    void updateCompanyActive(Boolean active, Integer companyId);

    void updateTestCompany(Integer id);

}
