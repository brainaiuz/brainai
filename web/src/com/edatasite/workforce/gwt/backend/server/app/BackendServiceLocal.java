package com.edatasite.workforce.gwt.backend.server.app;

import com.edatasite.workforce.core.domain.EdsCompanyStatistic;
import com.edatasite.workforce.gwt.backend.client.rpc.PDFSettingsTransObject;
import com.edatasite.workforce.gwt.backend.server.actions.CreateSubscriptionCommand;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;

import java.util.HashSet;

public interface BackendServiceLocal {

    void createSubscription(CreateSubscriptionCommand c);

    void indexCompanyFolders(SolrReindexRpc solrReindexRpc);

    void indexCompanyFiles(SolrReindexRpc solrReindexRpc);

    void indexCompanyAccountToSolr(SolrReindexRpc solrReindex);

    void indexCompanyContactToSolr(SolrReindexRpc solrReindex);

    void indexLeads(SolrReindexRpc solrReindex);

    void indexCandidates(SolrReindexRpc solrReindex);

    void indexCompanyCrmCase(SolrReindexRpc solrReindex);

    void indexCompanyOpportunity(SolrReindexRpc solrReindex);

    void reindexCompanyTasks(SolrReindexRpc solrReindexRpc);

    void indexCompanyProjects(SolrReindexRpc solrRendex);

    Integer fixTaskInconsistenciesInDb(Integer companyID, Integer startAt);

    Integer fixTaskInconsistenciesInSolr(Integer companyID, Integer startAt);

    void wrapStatisticData(Object[] statistic, EdsCompanyStatistic companystat);

    Integer createSchemas(Integer count);

    void enableDisableGenericSettings(Integer companyID, GenericSettingsEnum key, boolean enable);

    PDFSettingsTransObject getCompanyPDFSettings(Integer companyID, Integer companyPDFTemplateID);

    Integer saveCompanyPdfTemplate(PDFSettingsTransObject transObject);

    boolean removeSchema(Integer companyID);

    String createSchemaByID(Integer schema, HashSet<String> activeModules);

    String getTemplateSchemaForID(Integer schema, TemplateSchema templateSchema);

    void indexCustomFormItems(SolrReindexRpc reindexRpc);

    String getInsertPublicData(Integer objectID);
}
