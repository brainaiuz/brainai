package com.edatasite.workforce.core.solr.component;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderRbac;
import com.edatasite.workforce.core.solr.document.FolderSolrDoc;
import com.edatasite.workforce.core.solr.repository.FolderSolrDocRepository;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_FOLDER_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LIMIT;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:28.
 */
@Component
public class FolderSolrComponent {

    private static final Logger log = LoggerFactory.getLogger(FolderSolrComponent.class);

    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    private FolderSolrDocRepository folderSolrDocRepository;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private UploadAmazonSettingsManager uploadAmazonSettingsManager;
    @Autowired
    private AmazonManager amazonManager;
    @Autowired
    private SinxDocumentsSettingsManager sinxDocumentsSettingsManager;
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private ExecutorService executor;

    @Transactional
    public void index(EdsFileHeader edsFileHeader) throws InterruptedException {
        this.indexes(Arrays.asList(edsFileHeader));
    }

    @Transactional
    public void indexes(List<EdsFileHeader> edsFileHeaders) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        Boolean isIntegerNumberEnabled = employeeManager.isIntegerEmployeeCodeEnabled();
        if (!CollectionUtils.isEmpty(edsFileHeaders)) {
            List<FolderSolrDoc> folderSolrDocs = new ArrayList<>();

            for (EdsFileHeader edsFileHeader : edsFileHeaders) {
                if (Objects.nonNull(edsFileHeader)) {
                    try {
                        String downloadUrl = "";
                        if (edsFileHeader.getFolder() != null && Constants.F_EMPLOYEE_PROFILE == edsFileHeader.getFolder().getFolderType()) {
                            downloadUrl = getDownloadUrl(edsFileHeader);
                        }
                        List<EdsFolderRbac> folderRbacEntries = folderRbacManager.getFileRbacEntries(edsFileHeader.getObjectID());
                        if (!CollectionUtils.isEmpty(folderRbacEntries)) {
                            folderSolrDocs.add(createFolderDocument(folderRbacEntries, edsFileHeader, companyId, downloadUrl, isIntegerNumberEnabled));
                            log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                            log.info("INDEXED FOLDER CORE CID - {}, OBJID - {}", companyId, edsFileHeader.getObjectID());
                            log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                        }
                    } catch (Exception e) {
                        log.error("********************* ERROR occurred while creating solr doc EdsFileHeader = {} **********************", edsFileHeader.getName());
                        throw e;
                    }
                }
            }
            if (!folderSolrDocs.isEmpty()) {
                log.info("========= Create folder solr docs for company {} with size {} =========", companyId, folderSolrDocs.size());
                folderSolrDocRepository.saveAll(folderSolrDocs);
            }
        }
    }

    @Transactional
    public void indexConcurrently(List<EdsFileHeader> edsFileHeaders) throws InterruptedException {
        Integer companyId = SecurityContext.getCompanyID();
        Boolean isIntegerNumberEnabled = employeeManager.isIntegerEmployeeCodeEnabled();
        if (!CollectionUtils.isEmpty(edsFileHeaders)) {
            ConcurrentLinkedQueue<FolderSolrDoc> folderSolrDocs = new ConcurrentLinkedQueue<>();

            String dataBase = ServerSecurityContext.getInstance().getDatabase();
            String companId = ServerSecurityContext.getInstance().getCompanyId();

            List<Callable<Void>> tasks = new ArrayList<>();

            for (EdsFileHeader edsFileHeader : edsFileHeaders) {
                if (Objects.nonNull(edsFileHeader)) {
                    Callable<Void> task = () -> {
                        try {
                            ServerSecurityContext.getInstance().setDatabase(dataBase);
                            ServerSecurityContext.getInstance().setCompanyId(companId);

                            String downloadUrl = "";
                            if (edsFileHeader.getFolder() != null && Constants.F_EMPLOYEE_PROFILE == edsFileHeader.getFolder().getFolderType()) {
                                downloadUrl = getDownloadUrl(edsFileHeader);
                            }
                            List<EdsFolderRbac> folderRbacEntries = folderRbacManager.getFileRbacEntries(edsFileHeader.getObjectID());
                            if (!CollectionUtils.isEmpty(folderRbacEntries)) {
                                synchronized (this) {
                                    folderSolrDocs.add(createFolderDocument(folderRbacEntries, edsFileHeader, companyId, downloadUrl, isIntegerNumberEnabled));
                                    log.info("Indexed Folder Core CID - {}, objId - {}", companId, edsFileHeader.getObjectID());
                                }
                            }
                        } catch (Exception e) {
                            log.error("********************* ERROR occurred while creating solr doc EdsFileHeader = {} **********************", edsFileHeader.getName());
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
                log.error("Error on loading Additional Payment list", e);
            }

            if (!folderSolrDocs.isEmpty()) {
                try {
                    log.info("========= Create Folder solr docs for company {} with size {} =========", companyId, folderSolrDocs.size());
                    folderSolrDocRepository.saveAll(folderSolrDocs);
                } catch (Exception e) {
                    log.error("Error on saving Folder list", e);
                }
            }
        }
    }

    private String getDownloadUrl(EdsFileHeader edsFileHeader) {
        String downloadUrl = "";
        EdsFileBody fileBody = edsFileHeader.getCurrentBody();
        if (fileBody != null) {
            String uploadType = fileBody.getType() != null ? fileBody.getType().getCode() : "";
            if (Constants.AMAZON.equals(uploadType)) {
                downloadUrl = getFileUrl(edsFileHeader.getCurrentBody().getObjectID());
            } else if (Constants.GOOGLE.equals(uploadType) || Constants.OFFICE_365.equals(uploadType) || Constants.OFFICE_365_SHARE_POINT.equals(uploadType)) {
                EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(fileBody);
                if (googleDocumentsSettings != null) {
                    if (Constants.GOOGLE.equals(uploadType)) {
                        downloadUrl = googleDocumentsSettings.getDownloadLink();
                    } else {
                        downloadUrl = googleDocumentsSettings.getDocumentLink();
                    }
                }
            } else {
                downloadUrl = CommandConstants.COMMON_URL + "/downloadFile?id=" + fileBody.getObjectID();
            }
        }
        return downloadUrl;
    }

    private boolean isUploadTypeAmazon(EdsUpload upload) {
        return upload.getType() != null && Constants.AMAZON.equals(upload.getType().getCode());
    }

    public String getFileUrl(Integer fileId) {
        String url = "";
        EdsUpload upload = (EdsUpload) uploadManager.get(fileId);
        if (isUploadTypeAmazon(upload)) {
            EdsUploadAmazonSettings uploadAmazonSettings = uploadAmazonSettingsManager.getUploadAmazonSettings(upload);
            if (uploadAmazonSettings != null) {
                try {
                    url = amazonManager.getLink(uploadAmazonSettings);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            return EdsContextParams.getFullHost() + "common/downloadFile?id=" + upload.getObjectID();
        }
        return url;
    }

    private FolderSolrDoc createFolderDocument(List<EdsFolderRbac> edsFolderRbac, EdsFileHeader edsFileHeader, Integer companyId, String url, Boolean isIntegerNumberEnabled) {
        List<Integer> users = new ArrayList<>();
        List<Integer> groups = new ArrayList<>();
        FolderSolrDoc folderSolrDoc = new FolderSolrDoc();

        if (edsFolderRbac != null && edsFolderRbac.size() > 0) {
            for (EdsFolderRbac folderRbac : edsFolderRbac) {
                if (EdsTrusteeType.USER.equals(folderRbac.getTrusteeType())) {
                    users.add(folderRbac.getUser().getObjectID());
                } else if (EdsTrusteeType.GROUP.equals(folderRbac.getTrusteeType())) {
                    if (folderRbac.getGroup() != null) {
                        groups.add(folderRbac.getGroup().getObjectID());
                    }
                }
            }
        }

        folderSolrDoc.setOid(SolrUtils.generatedOId(companyId, edsFileHeader.getObjectID()));
        folderSolrDoc.setCompanyId(companyId);
        folderSolrDoc.setFolderId(edsFileHeader.getObjectID());
        folderSolrDoc.setFolderName(edsFileHeader.getFolder() != null ? edsFileHeader.getFolder().getName() : "");
        folderSolrDoc.setOwnerId(edsFileHeader.getOwner().getObjectID());
        folderSolrDoc.setOwnerName(edsFileHeader.getOwner().getName());
        folderSolrDoc.setFolderDescription(edsFileHeader.getCurrentBody().getDescription());
        folderSolrDoc.setDateCreation(edsFileHeader.getAuditInfo().getCreationDate());
        folderSolrDoc.setDateModification(edsFileHeader.getAuditInfo().getModificationDate());
        folderSolrDoc.setFolderConstantName(edsFileHeader.getName());
        folderSolrDoc.setEntityId(edsFileHeader.getEntityId());
        folderSolrDoc.setFolderTypeId(edsFileHeader.getFileType());
        folderSolrDoc.setHasParent(false);
        folderSolrDoc.setParentId(edsFileHeader.getFolder().getObjectID());
        folderSolrDoc.setDeleted(edsFileHeader.isDeleted());
        if (edsFileHeader.getAuditInfo().getCreatedBy() != null) {
            folderSolrDoc.setCreatedId(edsFileHeader.getAuditInfo().getCreatedBy().getObjectID());
            folderSolrDoc.setCreatedName(edsFileHeader.getAuditInfo().getCreatedBy().getName());
            folderSolrDoc.setCreatedIdName(SolrUtils.getIdName(edsFileHeader.getAuditInfo().getCreatedBy().getObjectID(), edsFileHeader.getAuditInfo().getCreatedBy().getName()));
        }
        final EdsUser modifiedBy = edsFileHeader.getAuditInfo().getModifiedBy();
        if (modifiedBy != null) {
            folderSolrDoc.setModifiedId(modifiedBy.getObjectID());
        }
        folderSolrDoc.setFile(true);
        folderSolrDoc.setSize(edsFileHeader.getCurrentBody().getFileSize());
        folderSolrDoc.setContentType(edsFileHeader.getCurrentBody().getContentType());
        folderSolrDoc.setUploadType(edsFileHeader.getCurrentBody().getType().getCode());
        folderSolrDoc.setDownloadUrl(url);
        folderSolrDoc.setBodyId(edsFileHeader.getCurrentBody().getObjectID());
        folderSolrDoc.setDocumentId(edsFileHeader.getDocumentID());
        folderSolrDoc.setIssuedDate(edsFileHeader.getIssuedDate());
        folderSolrDoc.setExpireDate(edsFileHeader.getExpireDate());
        folderSolrDoc.setReminderId(edsFileHeader.getReminderId());
        folderSolrDoc.setReminderName(edsFileHeader.getReminderName());
        if (edsFileHeader.getDocumentType() != null) {
            folderSolrDoc.setDocumentTypeId(edsFileHeader.getDocumentType().getObjectID());
            folderSolrDoc.setDocumentType(edsFileHeader.getDocumentType().getCode());
            folderSolrDoc.setDocumentTypeIdName(SolrUtils.getIdName(edsFileHeader.getDocumentType().getObjectID(), edsFileHeader.getDocumentType().getName()));
        }
        if (edsFileHeader.getInsuranceDocument() != null) {
            folderSolrDoc.setInsureeName(edsFileHeader.getInsuranceDocument().getInsureeName());
            folderSolrDoc.setInsureeLastName(edsFileHeader.getInsuranceDocument().getInsureeLastName());
            folderSolrDoc.setInsuranceStatusId(edsFileHeader.getInsuranceDocument().getStatusId());
            if (edsFileHeader.getInsuranceDocument().getStatusId() != null) {
                String statusname = null;
                if (edsFileHeader.getInsuranceDocument().getStatusId().equals(1)) {
                    folderSolrDoc.setInsuranceStatusName("Employee");
                    statusname = "Employee";
                } else if (edsFileHeader.getInsuranceDocument().getStatusId().equals(2)) {
                    folderSolrDoc.setInsuranceStatusName("Emp Dependent");
                    statusname = "Emp Dependent";
                }
                folderSolrDoc.setInsuranceStatusIdName(SolrUtils.getIdName(edsFileHeader.getInsuranceDocument().getStatusId(), statusname));
            }
            folderSolrDoc.setInsuranceCost(edsFileHeader.getInsuranceDocument().getInsuranceCost());
            folderSolrDoc.setInsurancePlan(edsFileHeader.getInsuranceDocument().getInsurancePlan());
            folderSolrDoc.setInsuranceCovarage(edsFileHeader.getInsuranceDocument().getInsuranceCoverage());
        }
        EdsUser enetityUser = edsFileHeader.getEnetityUser();

        if (enetityUser != null) {
            folderSolrDoc.setEntityUserId(enetityUser.getObjectID());
            folderSolrDoc.setEntityUserName(enetityUser.getName());
            folderSolrDoc.setEntityUserIdName(SolrUtils.getIdName(enetityUser.getObjectID(), enetityUser.getName()));
            try {
                if ((enetityUser instanceof EdsEmployee) && enetityUser.getEmployee() != null && enetityUser.getEmployee().getProfile() != null) {
                    EdsEmployeeProfile employeeProfile = enetityUser.getEmployee().getProfile();
                    folderSolrDoc.setEntityUserNumber(employeeProfile.getEmployeeCode());
                    String code = employeeProfile.getEmployeeCode();
                    if (code != null && !"".equals(code) && isIntegerNumberEnabled) {
                        folderSolrDoc.setEntityUserIntegerNumber(Long.parseLong(code.replaceAll("[\\D]", "")));
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        folderSolrDoc.setDocumentName(edsFileHeader.getDocumentName());
        log.info("||||||||||||||||||||||| USER VIEWERS ARE " + users + " |||||||||||||||||||||||");
        users.forEach(userId -> {
            folderSolrDoc.getUserViewers().add(userId);
        });
        groups.forEach(groupId -> {
            folderSolrDoc.getGroupViewers().add(groupId);
        });
        return folderSolrDoc;
    }

    public Page<FolderSolrDoc> getList(ListingFilterParameter filterParameter, String solrQuery) {
        SimpleQuery fileSolrQuery = new SimpleQuery(new SimpleStringCriteria(solrQuery));

        Sort solrSort = Sort.by(Sort.Direction.DESC, SolrFolderRepresenter.FIELD_DATE_MODIFICATION);
        if (!filterParameter.isSearchButton()) {
            if (StringUtils.isNotBlank(filterParameter.getSortField())) {
                Sort.Direction sortDirection = filterParameter.isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
                switch (filterParameter.getSortField()) {
                    case FileResource.NAME ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_FILE_NAME);
                    case FileResource.OWNER ->
                            solrSort = Sort.by(new Sort.Order(sortDirection, SolrFolderRepresenter.FIELD_OWNER_ID));
                    case FileResource.SIZE ->
                            solrSort = Sort.by(new Sort.Order(sortDirection, SolrFolderRepresenter.FIELD_SIZE));
                    case FileResource.DATE ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.FIELD_DATE_MODIFICATION);
                    case FileResource.TYPE ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_CONTENT_TYPE);
                    case FileResource.EMPLOYEE_NAME ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_EMPLOYEE_NAME);
                    case FileResource.DOCUMENT_NAME ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_DOCUMENT_NAME);
                    case FileResource.DOCUMENT_DESCRIPTION ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_DOCUMENT_DESCRIPTION);
                    case FileResource.DOCUMENT_ID ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_DOCUMENT_ID);
                    case FileResource.DOCUMENT_TYPE ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_DOCUMENT_TYPE);
                    case FileResource.REMINDER_TYPE ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_REMINDER_NAME);
                    case FileResource.ISSUED_DATE ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_ISSUED_DATE);
                    case FileResource.EXPIRE_DATE ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.EXPIRE_DATE);
                    case FileResource.CREATEBY ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_CREATED_NAME);
                    case FileResource.EMPLOYEE_CODE ->
                            solrSort = Sort.by(sortDirection, SolrFolderRepresenter.SORTABLE_EMPLOYEE_CODE);
                }
            }
        }
        int limit = filterParameter.getLimit() > 0 ? filterParameter.getLimit() : SOLR_LIMIT;
        fileSolrQuery.setPageRequest(PageRequest.of(filterParameter.getCurrentPage(), limit, solrSort));

        return solrTemplate.query(SOLR_FOLDER_CORE, fileSolrQuery, FolderSolrDoc.class);
    }
}
