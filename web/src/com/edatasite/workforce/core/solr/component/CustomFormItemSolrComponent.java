package com.edatasite.workforce.core.solr.component;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFormCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.solr.document.CustomFormItemSolrDoc;
import com.edatasite.workforce.core.solr.repository.CustomFormItemSolrDocRepository;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactSolrItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormSolrRPC;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCustomFormConst;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CUSTOM_FORM_ITEM_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:26.
 */
@Component
public class CustomFormItemSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(CustomFormItemSolrComponent.class);

    @Autowired
    private CustomFormItemSolrDocRepository customFormItemSolrDocRepository;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    protected XSync<String> sync;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsCustomFormItems edsCustomForm) throws IOException, SolrServerException, InterruptedException {
        this.indexes(Arrays.asList(edsCustomForm));
    }

    @Transactional
    public void indexes(List<EdsCustomFormItems> edsCustomForms) throws IOException, SolrServerException, InterruptedException {

        if (!CollectionUtils.isEmpty(edsCustomForms)) {
            List<CustomFormItemSolrDoc> customFormItemSolrDocs = new ArrayList<>();

            Integer companyId = SecurityContext.getCompanyID();
            for (EdsCustomFormItems edsCustomForm : edsCustomForms) {
                if (edsCustomForm != null) {
                    try {
                        customFormItemSolrDocs.add(createCustomFormDocument(edsCustomForm.getSolrRPC(), Integer.valueOf(companyId), edsCustomForm.getFormCustomFields()));
                        log.info("Indexed CustomForm Core CID - {}, objId - {}", companyId, edsCustomForm.getObjectID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("********************* Error on CustomForm with id {}, and error message {} **********************", edsCustomForm.getObjectID(), e.getMessage());
                        throw e;
                    }
                }
            }

            if (!customFormItemSolrDocs.isEmpty()) {
                log.info("========= Create CustomForm solr docs for company {} with size {} =========", companyId, customFormItemSolrDocs.size());
                customFormItemSolrDocRepository.saveAll(customFormItemSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsCustomFormItems> edsCustomForms) throws IOException, SolrServerException, InterruptedException {
        if (!CollectionUtils.isEmpty(edsCustomForms)) {
            ConcurrentLinkedQueue<CustomFormItemSolrDoc> customFormItemSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companyId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();
            for (EdsCustomFormItems edsCustomForm : edsCustomForms) {
                if (edsCustomForm != null) {
                    CustomFormSolrRPC solrRPC = edsCustomForm.getSolrRPC();
                    EdsCustomFormCustomFields formCustomFields = edsCustomForm.getFormCustomFields();
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            sync.execute(getSynchronizedKey(solrRPC), () -> {
                                        customFormItemSolrDocs.add(createCustomFormDocument(solrRPC, Integer.valueOf(companyId), formCustomFields));
                                        log.info("Indexed CustomForm Core CID - {}, objId - {}", companyId, edsCustomForm.getObjectID());
                                    }
                            );
                        } catch (Exception e) {
                            log.error("********************* Error on CustomForm with id {}, and error message {} **********************", edsCustomForm.getObjectID(), e.getMessage());
                        }
                        return null;
                    };
                    tasks.add(task);
                }
            }

            try {
                List<Future<Void>> results = executor.invokeAll(tasks);
                for (Future<Void> f : results) {
                    try {
                        f.get();
                    } catch (ExecutionException e) {
                        log.error("❌ Task execution failed", e.getCause());
                    }
                }
            } catch (InterruptedException e) {
                log.error("Error on loading CustomForm list", e);
            }

            if (!customFormItemSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create CustomForm solr docs for company {} with size {} =========", companyId, customFormItemSolrDocs.size());
                    customFormItemSolrDocRepository.saveAll(customFormItemSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving CustomForm list", e);
                }
            }
        }
    }

    protected String getSynchronizedKey(CustomFormSolrRPC customForm) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + customForm.getObjectId();
    }

    private CustomFormItemSolrDoc createCustomFormDocument(CustomFormSolrRPC customForm, Integer companyId, EdsCustomFields customFields) {
        CustomFormItemSolrDoc customFormItemSolrDoc = new CustomFormItemSolrDoc();

        customFormItemSolrDoc.setOid(SolrUtils.generatedOId(companyId, customForm.getObjectId()));
        customFormItemSolrDoc.setCompanyId(companyId);
        customFormItemSolrDoc.setObjectId(customForm.getObjectId());
        customFormItemSolrDoc.setDocType(SolrCustomFormConst.CUSTOM_FORM_SOLR_DOC);
        customFormItemSolrDoc.setItemId(customForm.getItemId());
        customFormItemSolrDoc.setFormId(customForm.getFormId());
        customFormItemSolrDoc.setFormName(customForm.getFormName());

        if (customForm.getStatus() != null) {
            customFormItemSolrDoc.setStatusId(customForm.getStatus().getId());
            customFormItemSolrDoc.setStatusName(customForm.getStatus().getName());
            customFormItemSolrDoc.setStatusIdName(SolrUtils.getIdName(customForm.getStatus().getId(), customForm.getStatus().getName()));
        }

        if (customForm.getCurrentApprover() != null) {
            customFormItemSolrDoc.setCurrentApproverId(customForm.getCurrentApprover().getId());
            customFormItemSolrDoc.setCurrentApproverName(customForm.getCurrentApprover().getName());
            customFormItemSolrDoc.setCurrentApproverIdName(SolrUtils.getIdName(customForm.getCurrentApprover().getId(), customForm.getCurrentApprover().getName()));
        }

        if (customForm.getCreator() != null) {
            customFormItemSolrDoc.setCreatorId(customForm.getCreator().getId());
            customFormItemSolrDoc.setCreatorName(customForm.getCreator().getName());
            customFormItemSolrDoc.setCreatorIdName(SolrUtils.getIdName(customForm.getCreator().getId(), customForm.getCreator().getName()));
        }
        if (customForm.getUpdater() != null) {
            customFormItemSolrDoc.setUpdatedId(customForm.getUpdater().getId());
            customFormItemSolrDoc.setUpdaterName(customForm.getUpdater().getName());
            customFormItemSolrDoc.setUpdaterIdName(SolrUtils.getIdName(customForm.getUpdater().getId(), customForm.getUpdater().getName()));
        }
        customFormItemSolrDoc.setCreatedDate(customForm.getCreatedDate());
        customFormItemSolrDoc.setUpdatedDate(customForm.getUpdatedDate());

        CustomFieldsUtils.setSolrDocDynamicFields(customFormItemSolrDoc, customFields);
        return customFormItemSolrDoc;
    }


    public Page<CustomFormItemSolrDoc> getList(ListingFilterParameter fp, String solrQuery) {
        SimpleQuery query = new SimpleQuery(new SimpleStringCriteria(solrQuery));
        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrCustomFormConst.FIELD_CREATED_DATE);
        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                boolean desc = !fp.isAscending();
                Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
                switch (fp.getSortField()) {
                    case FormItems.UPDATED_DATE ->
                            solrSort = Sort.by(sortDirection, SolrCustomFormConst.FIELD_UPDATED_DATE);
                    case FormItems.CREATED_DATE ->
                            solrSort = Sort.by(sortDirection, SolrCustomFormConst.FIELD_CREATED_DATE);
                    case FormItems.UPDATER -> solrSort = Sort.by(sortDirection, SolrCustomFormConst.FIELD_UPDATER_NAME);
                    case FormItems.CREATER -> solrSort = Sort.by(sortDirection, SolrCustomFormConst.FIELD_CREATOR_NAME);
                    case FormItems.STATUS -> solrSort = Sort.by(sortDirection, SolrCustomFormConst.FIELD_STATUS_NAME);
                    case FormItems.APPROVER ->
                            solrSort = Sort.by(sortDirection, SolrCustomFormConst.FIELD_CURRENT_APPROVER_NAME);
                    default ->
                            solrSort = CustomFieldsUtils.getSortCustomFieldsSortableNameToSolr(fp.getSortField(), !fp.isAscending(), true);
                }
            }
        }
        int limit = fp.getLimit() > 0 ? fp.getLimit() : SOLR_LIMIT;
        query.setPageRequest(PageRequest.of(fp.getCurrentPage(), limit, solrSort));
        return solrTemplate.query(SOLR_CUSTOM_FORM_ITEM_CORE, query, CustomFormItemSolrDoc.class);
    }
}
