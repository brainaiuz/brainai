package com.edatasite.workforce.gwt.documents.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsPerformanceNote;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsSinxDocumentsSettings;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsMailMessage;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.documents.EdsCopiedFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsDocumentPermission;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.documents.EdsInsuranceDocument;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderRbac;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.solr.component.FolderSolrComponent;
import com.edatasite.workforce.core.solr.document.FolderSolrDoc;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.GroupTranslation;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.DocumentsSearchItem;
import com.edatasite.workforce.gwt.core.client.rpc.SearchResultItem;
import com.edatasite.workforce.gwt.core.client.rpc.SearchResultItemList;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmailTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleDocumentsManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.MailMessageManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PerformanceNoteManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.SinxDocumentsSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UploadAmazonSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.CopiedFileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.DocumentPermissionManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileBodyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.RelationshipManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.AttachmentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.FileHeadertEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.FileCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365DriveItem;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ResourceCollection;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365DriveService;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OtherUserResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OthersResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.RestResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.SharedResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.SystemResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.TrashResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.RecruitmentServiceLocal;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.base.Stopwatch;
import com.google.gdata.util.ServiceException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.CommonParams;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.ejb.EJBTransactionRolledbackException;
import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_EMPLOYEE_DOCUMENTS;

/**
 * User: Sherali
 * Date: 14.05.2010
 * Time: 15:34:29
 */
@Transactional
@Service("documentsService")
public class DocumentsServiceImpl implements DocumentsService, DocumentsServiceLocal, CommandConstants, Constants {

    private static final Logger log = LoggerFactory.getLogger(DocumentsServiceImpl.class);
    private static final Map<String, ComparatorFactory<FolderResource>> comparatorFactoriesI = new HashMap<>();
    static ComparatorFactory fileResourcesComparatorFactory = (ComparatorFactory<FileResource>) sortOrder -> new AbstractComparator<FileResource>() {
        public int compare(FileResource o1, FileResource o2) {
            return internalCompare(o1.getBodyId(), o2.getBodyId(), sortOrder);
        }
    };

    static {
        comparatorFactoriesI.put("name",
                sortOrder -> new AbstractComparator<FolderResource>() {
                    public int compare(FolderResource o1, FolderResource o2) {
                        return internalCompare(o1.getName(), o2.getName(), sortOrder);
                    }
                });
    }

    static {
        comparatorFactoriesI.put("rank",
                sortOrder -> new AbstractComparator<FolderResource>() {
                    public int compare(FolderResource o1, FolderResource o2) {
                        return internalCompare(o1.getRank(), o2.getRank(), sortOrder);
                    }
                });
    }

    @Autowired
    private FolderManager folderManager;
    @Autowired
    private DocumentPermissionManager documentPermissionManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private FileBodyManager fileBodyManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    @Qualifier("uploadManager")
    private UploadManager uploadManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private TrusteeManager trusteeManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private RelationshipManager relationshipManager;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private CopiedFileHeaderManager copiedFileHeaderManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ExpensePaymentManager expensePaymentManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SinxDocumentsSettingsManager sinxDocumentsSettingsManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private GoogleDocumentsManager googleDocumentsManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private ExpenseReportManager reportManager;
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private BackendServiceLocal backendServiceLocal;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private RecruitmentServiceLocal recruitmentServiceLocal;
    @Autowired
    private PlacementManager placementManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private PerformanceNoteManager performanceNoteManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    @Qualifier("companyCFSettingsManager")
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private Office365DriveService office365DriveService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private MailMessageManager mailMessageManager;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private UploadAmazonSettingsManager uploadAmazonSettingsManager;
    @Autowired
    private FolderSolrComponent folderSolrComponent;

    private static byte[] getBytesFromStream(InputStream is) throws IOException {
        long length = is.available();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file ");
        }
        is.close();
        return bytes;
    }

    /**
     * Helper method for identifying mime type by examining the filename extension
     *
     * @param filenamec
     * @return the mime type
     */
    public static String identifyMimeType(String filenamec) {
        if (filenamec.contains(".")) {
            String filename = filenamec.substring(filenamec.lastIndexOf('.')).toLowerCase(Locale.ENGLISH);
            if (filename.contains("jpg") || filename.contains("jpeg") || filename.contains("jpe")) {
                return "image/jpeg";
            } else if (filename.contains("png")) {
                return "image/png";
            } else if (filename.contains("bmp")) {
                return "image/bmp";
            } else if (filename.contains("gif")) {
                return "image/gif";
            } else if (filename.contains("tiff") || filename.contains("tif")) {
                return "image/tiff";
            } else if (filename.contains("txt")) {
                return "text/plain";
            } else if (filename.contains("html") || filename.contains("htm")) {
                return "text/html";
            } else if (filename.contains("odt")) {
                return "application/vnd.oasis.opendocument.text";
            } else if (filename.contains("sxw")) {
                return "application/vnd.sun.xml.writer";
            } else if (".doc".equals(filename)) {
                return "application/msword";
            } else if (filename.contains("docx")) {
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            } else if (filename.contains("rtf")) {
                return "application/rtf";
            } else if (filename.contains("pdf")) {
                return "application/pdf";
            } else if (filename.contains("pps") || ".ppt".contains(filename)) {
                return "application/vnd.ms-powerpoint";
            } else if (filename.contains("pptx")) {
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            } else if (".xls".contains(filename)) {
                return "application/vnd.ms-excel";
            } else if (filename.contains("xlsx")) {
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else if (filename.contains("ods")) {
                return "application/vnd.oasis.opendocument.spreadsheet";
            } else if (filename.contains("csv")) {
                return "text/csv";/*"application/csv";*/
            } else if (filename.contains("tsv") || filename.contains("tab")) {
                return "text/tab-separated-values";
            } else if (filename.contains("swf")) {
                return "application/x-shockwave-flash";
            } else if (filename.contains("zip")) {
                return "application/zip";
            } else if (filename.contains("json")) {
                return "application/json";
            } else if (filename.contains("mp3")) {
                return "audio/mpeg";
            }
        }
        return "application/octet-stream";
    }

    public void copyCaseAttachments(EdsCompany company) {

        SecurityContext.getInstance().setCompanyId(company.getObjectID());
        List<EdsAttachment> attachmentList = attachmentManager.getCompanyCaseAttachments();
        int k = 0;
        Map<Integer, Integer> ids = new HashMap<>();
        EdsFolder folder = folderManager.getFolderByFolderType(F_CASE);
        if (folder != null) {
            List<EdsEmployee> admins = userManager.getAdmins(company.getObjectID());
            for (EdsAttachment attachment : attachmentList) {
                if (attachment.getAttachmentId() != null && !attachment.isCaseAttachmentCopied() && attachment.getSize() < 10000) {
                    EdsCase crmCase = caseManager.getByTrackerID(attachment.getAttachmentId());
                    if (crmCase != null && crmCase.getTracker() != null) {
                        k++;
                        DocumentItem fileBody = new DocumentItem();
                        CopyInputStream cis = new CopyInputStream(uploadManager.getInputStream(attachment));
                        InputStream input1 = cis.getCopy();
                        fileBody.setInputStream(input1);
                        fileBody.setContentType(attachment.getContentType());
                        fileBody.setName(attachment.getOriginalName());
                        if (fileBody.getName() == null) {
                            fileBody.setName("noname.non");
                        }
                        fileBody.setFolderId(folder.getObjectID());
                        fileBody.setDescription(attachment.getDescription());
                        fileBody.setDoNotAddToIndex(true);
                        EdsUser owner = attachment.getCreator() == null ? admins.get(0) : attachment.getCreator();
                        try {
                            log.info(crmCase.getCaseNumberString() + " -- " + attachment.getObjectID() + " : " + fileBody.getName() + "(" + fileBody.getContentType() + ")");
                            FileResource file = createFile(owner, fileBody, EdsContextParams.getUploadType(), F_CASE, crmCase.getObjectID());
                            if (file != null) {
                                ids.put(file.getObjectId(), owner.getObjectID());
                            }
                            attachment.setCaseAttachmentCopied(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (ids.size() > 0) {
                for (Map.Entry<Integer, Integer> entry : ids.entrySet()) {
                    EdsFileHeader edsFile = fileHeaderManager.get(entry.getKey());
                    folderRbacManager.indexFile(edsFile);
                    try {
                        setFolderPermissionsToFile(edsFile, new ArrayList<>(getFolderPermissions(folder.getObjectID())), false, userManager.get(entry.getValue()));
                    } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }
        log.info("COUNT : {}", k);
    }

    @Transactional
    @Override
    public void indexFolder(Integer integer, boolean isUpdateExistingOne) {
        folderManager.indexFolder(folderManager.get(integer), isUpdateExistingOne);
    }

    /**
     * ATTENTION!!! DO NOT FORGET CLEAR HIBERNATE CACHE AT THE END OF THIS MEHTOD
     * TO PREVENT DIRTY ENTITY RETRIVAL FROM CACHE OF PREVIOUS COMPANY
     *
     * @param solrReindex
     * @param start
     * @param limit
     */
    @Transactional
    public Integer indexCompanyFolders(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        List<EdsFolder> folderList = folderManager.getCompanyFolderListForSolr(solrReindex, start, limit);
        if (folderList.isEmpty()) {
            return -1;
        }
        folderManager.indexFolders(folderList, true);
        return folderList.get(folderList.size() - 1).getObjectID();
    }

    @Override
    @Transactional
    public void indexCompanySystemFolders(SolrReindexRpc solrReindex) {
        log.trace(">>>Bigin Indexing company system folders CompanyId: = {}", solrReindex.getCompanyId());
        ServerSecurityContext.getInstance().setCompanyId(solrReindex.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));
        EdsFolder systemFolder = folderManager.getSystemFolder(solrReindex.getCompanyId());
        List<Integer> folderIds = new ArrayList<>();
        folderIds.add(systemFolder.getObjectID());
        collectSubFolderIds(folderIds, systemFolder.getSubfolders());

        int start = 0;
        while (start < folderIds.size()) {
            List<Integer> subIds = folderIds.subList(start, start + (start + 20 > folderIds.size() ? folderIds.size() - start : 20));
            start = start + (start + 20 > folderIds.size() ? folderIds.size() - start : 20);

            List<EdsFolder> folders = folderManager.getFoldersByIds(subIds);
            folderManager.indexFolders(folders, true);
            folderManager.flushAndClear();
        }
        log.trace(">>>DONE index company system folders CompanyId: = {}", solrReindex.getCompanyId());
    }

    private void collectSubFolderIds(List<Integer> folderIds, List<EdsFolder> folders) {
        for (EdsFolder folder : folders) {
            folderIds.add(folder.getObjectID());
            collectSubFolderIds(folderIds, folder.getSubfolders());
        }
    }

    @Transactional
    public void createSystemFolders(Integer companyid) {
        ServerSecurityContext.getInstance().setCompanyId(companyid);
        log.info("COMPANYID:>>>>>>>> : {}", companyid);

        EdsCompany company = companyManager.get(companyid);
        List<EdsEmployee> admins = employeeManager.getAdministrators();
        if (CollectionUtils.isEmpty(admins)) {
            admins = employeeManager.getEmployeeByRole(DR);
        }
        EdsUser companyCreator = admins.get(0);

        Locale locale = ServerUtils.getUserLocale();

        EdsFolder systemEdsFolder = folderManager.getSystemFolder(company.getObjectID());
        if (systemEdsFolder == null) {
            String systemFolderName = messageSource.getMessage("createSystemFolders.systemFolderName", null, "System Folder", locale);
            systemEdsFolder = createFolder(systemFolderName, null, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_DEFAULT, null);
        }
        //Project Management Folders
        createPMSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //Performance Appraisal Folders
        createPASystemFolder(company, systemEdsFolder, companyCreator, locale);
        //Accounting & Finance Folders
        createAccountingSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //CRM Folders
        createCRMSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //Custom Field Folders
        createCustomFieldSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //HRMS Folders
        createHRMSSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //Website Folders
        createWebsiteSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //Workspace Folders
        createWorkspaceSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //Settings Folders
        createSettingsSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //Payroll Folders
        createPayrollSystemFolder(company, systemEdsFolder, companyCreator, locale);
        //Note Folders
        createNoteSystemFolder(company, systemEdsFolder, companyCreator, locale);
        // Company Public folder
        createCompanyPublicSystemFolder(company, companyCreator, locale);
        // Company DB Backups folder
        createDBBackupSystemFolder(systemEdsFolder, companyCreator, locale);

        // Company DB  Xml Backups folder
        createDBXmlBackupSystemFolder(systemEdsFolder, companyCreator, locale);

        //TEMP Folder
        createTMPSystemFolder(company, companyCreator, locale);

        folderManager.flushAndClear();
        ServerSecurityContext.getInstance().removeCompanyId();
    }

    @Transactional
    public void createPMSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder pmFolder = folderManager.getProjectRootFolder(company);
        if (pmFolder == null) {
            String projectManagementRootFolderName = messageSource.getMessage("createSystemFolders.projectManagementRootFolderName", null, "Project Management", locale);
            pmFolder = createFolder(projectManagementRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PROJECT_ROOT, null);
        }
        EdsFolder contractFolder = folderManager.getFolderByFolderType(EdsFolder.F_CONTRACT);
        if (contractFolder == null) {
            String contractAttachmentsFolderName = messageSource.getMessage("createSystemFolders.contractAttachmentsFolderName", null, "Contract Attachments", locale);
            createFolder(contractAttachmentsFolderName, pmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CONTRACT, null);
        }
        int c = 0;
        int flushCommit = 0;
        List<EdsProject> cProjects = projectManager.getCompanyProjects();
        for (EdsProject p : cProjects) {
            EdsFolder projectFolder = folderManager.getProjectFolder(p.getObjectID());
            if (projectFolder == null) {
                log.info("COUNT:>>>>>>>> : {}", c++);
                createProjectFolder(p.getObjectID());
                flushCommit++;
                flushCommit++;
            }
            if (flushCommit > 5) {
                folderManager.flushAndClear();
                flushCommit = 0;
            }
        }
    }

    @Transactional
    public void createPASystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder paFolder = folderManager.getFolderByFolderType(EdsFolder.F_PA_ROOT);
        if (paFolder == null) {
            String performanceAppraisalRootFolderName = messageSource.getMessage("createSystemFolders.performanceAppraisalRootFolderName", null, "Performance Appraisal", locale);
            paFolder = createFolder(performanceAppraisalRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PA_ROOT, null);
            String paAttachmentsFolderName = messageSource.getMessage("createSystemFolders.paAttachmentsFolderName", null, "PA Attachments", locale);
            createFolder(paAttachmentsFolderName, folderManager.get(paFolder.getObjectID()), companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PA, null);
            String circleAttachmentsFolderName = messageSource.getMessage("createSystemFolders.circleAttachmentsFolderName", null, "360 Attachments", locale);
            createFolder(circleAttachmentsFolderName, folderManager.get(paFolder.getObjectID()), companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_360, null);
            String issuePaAttachmentsFolderName = messageSource.getMessage("createSystemFolders.issuePaAttachmentsFolderName", null, "Issue PA Attachments", locale);
            createFolder(issuePaAttachmentsFolderName, folderManager.get(paFolder.getObjectID()), companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PA_ISSUE, null);
        }
    }

    @Transactional
    public void createAccountingSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder afFolder = folderManager.getFolderByFolderType(EdsFolder.F_AF_ROOT);
        if (afFolder == null) {
            String accountingAndFinanceRootFolderName = messageSource.getMessage("createSystemFolders.accountingAndFinanceRootFolderName", null, "Accounting & Finance", locale);
            afFolder = createFolder(accountingAndFinanceRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_AF_ROOT, null);
            String saleInvoiceAttachmentsFolderName = messageSource.getMessage("createSystemFolders.saleInvoiceAttachmentsFolderName", null, "Sale Invoice Attachments", locale);
            createFolder(saleInvoiceAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_SALE_INV, null);
            String prepaymentFolderName = messageSource.getMessage("prepaymentSystemFolder", null, "Prepayment", locale);
            createFolder(prepaymentFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PREPAYMENT, null);
            String purchaseInvoicesAttachmentsFolderName = messageSource.getMessage("createSystemFolders.purchaseInvoicesAttachmentsFolderName", null, "Purchase Invoices Attachments", locale);
            createFolder(purchaseInvoicesAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PUR_INV, null);
            String expenseLineAttachmentsFolderName = messageSource.getMessage("createSystemFolders.expenseLineAttachmentsFolderName", null, "Expense-Line Attachments", locale);
            createFolder(expenseLineAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EXP, null);
            String transactionAttachmentsFolderName = messageSource.getMessage("createSystemFolders.transactionAttachmentsFolderName", null, "Transaction Attachments", locale);
            createFolder(transactionAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_MANUAL_TRANSACTION, null);
            String saleQuoteAttachmentsFolderName = messageSource.getMessage("createSystemFolders.saleQuoteAttachmentsFolderName", null, "Sale Quote Attachments", locale);
            createFolder(saleQuoteAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_SALE_QUOTE, null);
            String saleQuoteItemAttachmentsFolderName = messageSource.getMessage("createSystemFolders.saleQuoteItemAttachmentsFolderName", null, "Sale Quote Item Attachments", locale);
            createFolder(saleQuoteItemAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_SALE_QUOTE_ITEM, null);
            String purchaseOrderAttachmentsFolderName = messageSource.getMessage("createSystemFolders.purchaseOrderAttachmentsFolderName", null, "Purchase Order Attachments", locale);
            createFolder(purchaseOrderAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PUR_ORDER, null);
            String expenseUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.expenseUploadedDocumentsFolderName", null, "Expense-Uploaded Documents", locale);
            createFolder(expenseUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EXP_DOC, null);
            String productUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.productServicesDocumentsFolderName", null, "Products/Services Attachments", locale);
            createFolder(productUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PRODUCTS_SERVICES, null);
            String bankTransferFolderName = messageSource.getMessage("bankTransferAttachments", null, "Bank Transfer Attachments", locale);
            createFolder(bankTransferFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_BANK_TRANSFER, null);
            String batchPaymentFolderName = messageSource.getMessage("batchPaymentAttachments", null, "Batch Payment Attachments", locale);
            createFolder(batchPaymentFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_BATCH_PAYMENT, null);
            String stockTransferFolderName = messageSource.getMessage("stockTransferAttachments", null, "Stock Transfer Attachments", locale);
            createFolder(stockTransferFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_STOCK_TRANSFER, null);
            String stockAdjustmentFolderName = messageSource.getMessage("stockAdjustmentAttachments", null, "Stock Adjustment Attachments", locale);
            createFolder(stockAdjustmentFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_STOCK_ADJUSTMENT, null);
            String rfqUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.rfqAttachmentsFolderName", null, "Request for Quote Attachments", locale);
            createFolder(rfqUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_RFQ, null);
            String rfqMainUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.rfqMainAttachmentsFolderName", null, "Request for Quote Main Attachments", locale);
            createFolder(rfqMainUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_RFQ_1, null);
            String rfpUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.rfqAttachmentsFolderName", null, "Request for Purchase Attachments", locale);
            createFolder(rfpUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_RFP, null);
            String bankAccountFolderName = messageSource.getMessage("bankAccountAttachments", null, "Bank Account Attachments", locale);
            createFolder(bankAccountFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_BANK_ACCOUNT, null);
            String expensePaymentFolderName = messageSource.getMessage("expensePaymentAttachments", null, "Expense Payment Attachments", locale);
            createFolder(expensePaymentFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EXP_PAYMENT, null);
            String telegramFolderName = messageSource.getMessage("telegramAttachments", null, "Telegram Attachments", locale);
            createFolder(telegramFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_TELEGRAM, null);
            String whiteLabelFolderName = messageSource.getMessage("whiteLabelAttachments", null, "White Label Attachments", locale);
            createFolder(whiteLabelFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WHITE_LABEL_LOGO, null);
            String whiteLabelFavIconFolderName = messageSource.getMessage("whiteLabelAttachments", null, "White Label FavIcon Attachments", locale);
            createFolder(whiteLabelFavIconFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WHITE_LABEL_FAVICON, null);
            String whatsAppFolderName = messageSource.getMessage("whatsAppAttachments", null, "WhatsApp Attachments", locale);
            createFolder(whatsAppFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WHATSAPP_MEDIA, null);
        } else {
            EdsFolder saleQuoteFolder = folderManager.getFolderByFolderType(EdsFolder.F_SALE_QUOTE);
            if (saleQuoteFolder == null) {
                String saleQuoteAttachmentsFolderName = messageSource.getMessage("createSystemFolders.saleQuoteAttachmentsFolderName", null, "Sale Quote Attachments", locale);
                createFolder(saleQuoteAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_SALE_QUOTE, null);
            }
            EdsFolder saleQuoteItemFolder = folderManager.getFolderByFolderType(EdsFolder.F_SALE_QUOTE_ITEM);
            if (saleQuoteItemFolder == null) {
                String saleQuoteItemAttachmentsFolderName = messageSource.getMessage("createSystemFolders.saleQuoteItemAttachmentsFolderName", null, "Sale Quote Item Attachments", locale);
                createFolder(saleQuoteItemAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_SALE_QUOTE_ITEM, null);
            }
            EdsFolder purchaseOrderFolder = folderManager.getFolderByFolderType(EdsFolder.F_PUR_ORDER);
            if (purchaseOrderFolder == null) {
                String purchaseOrderAttachmentsFolderName = messageSource.getMessage("createSystemFolders.purchaseOrderAttachmentsFolderName", null, "Purchase Order Attachments", locale);
                createFolder(purchaseOrderAttachmentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PUR_ORDER, null);
            }
            EdsFolder expensesDocsFolder = folderManager.getFolderByFolderType(EdsFolder.F_EXP_DOC);
            if (expensesDocsFolder == null) {
                String expenseUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.expenseUploadedDocumentsFolderName", null, "Expense-Uploaded Documents", locale);
                createFolder(expenseUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EXP_DOC, null);
            }
            EdsFolder productsServicesDocsFolder = folderManager.getFolderByFolderType(EdsFolder.F_PRODUCTS_SERVICES);
            if (productsServicesDocsFolder == null) {
                String productsServicesDocumentsFolderName = messageSource.getMessage("createSystemFolders.productServicesDocumentsFolderName", null, "Products/Services Attachments", locale);
                createFolder(productsServicesDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PRODUCTS_SERVICES, null);
            }
            EdsFolder bankTransferFolder = folderManager.getFolderByFolderType(EdsFolder.F_BANK_TRANSFER);
            if (bankTransferFolder == null) {
                String bankTransferFolderName = messageSource.getMessage("bankTransferAttachments", null, "Bank Transfer Attachments", locale);
                createFolder(bankTransferFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_BANK_TRANSFER, null);
            }
            EdsFolder batchPaymentFolder = folderManager.getFolderByFolderType(EdsFolder.F_BATCH_PAYMENT);
            if (batchPaymentFolder == null) {
                String batchPaymentFolderName = messageSource.getMessage("batchPaymentAttachments", null, "Batch Payment Attachments", locale);
                createFolder(batchPaymentFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_BATCH_PAYMENT, null);
            }
            EdsFolder stockTransferFolder = folderManager.getFolderByFolderType(EdsFolder.F_STOCK_TRANSFER);
            if (stockTransferFolder == null) {
                String stockTransferFolderName = messageSource.getMessage("stockTransferAttachments", null, "Stock Transfer Attachments", locale);
                createFolder(stockTransferFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_STOCK_TRANSFER, null);
            }
            EdsFolder stockAdjustmentFolder = folderManager.getFolderByFolderType(EdsFolder.F_STOCK_ADJUSTMENT);
            if (stockAdjustmentFolder == null) {
                String stockAdjustmentFolderName = messageSource.getMessage("stockAdjustmentAttachments", null, "Stock Adjustment Attachments", locale);
                createFolder(stockAdjustmentFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_STOCK_ADJUSTMENT, null);
            }
            EdsFolder rfqDocsFolder = folderManager.getFolderByFolderType(EdsFolder.F_RFQ);
            if (rfqDocsFolder == null) {
                String rfqUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.rfqAttachmentsFolderName", null, "Request for Quote Attachments", locale);
                createFolder(rfqUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_RFQ, null);
            }
            EdsFolder rfqMainDocsFolder = folderManager.getFolderByFolderType(EdsFolder.F_RFQ_1);
            if (rfqMainDocsFolder == null) {
                String rfqMainUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.rfqMainAttachmentsFolderName", null, "Request for Quote Main Attachments", locale);
                createFolder(rfqMainUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_RFQ_1, null);
            }
            EdsFolder rfpDocsFolder = folderManager.getFolderByFolderType(EdsFolder.F_RFP);
            if (rfpDocsFolder == null) {
                String rfpUploadedDocumentsFolderName = messageSource.getMessage("createSystemFolders.rfpAttachmentsFolderName", null, "Request for Purchase Attachments", locale);
                createFolder(rfpUploadedDocumentsFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_RFP, null);
            }
            EdsFolder bankAccountFolder = folderManager.getFolderByFolderType(EdsFolder.F_BANK_ACCOUNT);
            if (bankAccountFolder == null) {
                String bankAccountFolderName = messageSource.getMessage("bankAccountAttachments", null, "Bank Account Attachments", locale);
                createFolder(bankAccountFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_BANK_ACCOUNT, null);
            }
            EdsFolder prepaymentFolder = folderManager.getFolderByFolderType(EdsFolder.F_PREPAYMENT);
            if (prepaymentFolder == null) {
                String prepaymentFolderName = messageSource.getMessage("prepaymentSystemFolder", null, "Prepayment", locale);
                createFolder(prepaymentFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PREPAYMENT, null);
            }

            EdsFolder expensePaymentFolder = folderManager.getFolderByFolderType(EdsFolder.F_EXP_PAYMENT);
            if (expensePaymentFolder == null) {
                String expensePaymentFolderName = messageSource.getMessage("expensePaymentAttachments", null, "Expense Payment Attachments", locale);
                createFolder(expensePaymentFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EXP_PAYMENT, null);
            }

            EdsFolder telegramFolder = folderManager.getFolderByFolderType(EdsFolder.F_TELEGRAM);
            if (telegramFolder == null) {
                String telegramFolderName = messageSource.getMessage("telegramAttachments", null, "Telegram Attachments", locale);
                createFolder(telegramFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_TELEGRAM, null);
            }

            EdsFolder whiteLabelFolder = folderManager.getFolderByFolderType(EdsFolder.F_WHITE_LABEL_LOGO);
            if (whiteLabelFolder == null) {
                String whiteLabelFolderName = messageSource.getMessage("whiteLabelAttachments", null, "White Label Attachments", locale);
                createFolder(whiteLabelFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WHITE_LABEL_LOGO, null);
            }

            EdsFolder whiteLabelFavIconFolder = folderManager.getFolderByFolderType(EdsFolder.F_WHITE_LABEL_FAVICON);
            if (whiteLabelFavIconFolder == null) {
                String whiteLabelFolderName = messageSource.getMessage("whiteLabelAttachments", null, "White Label FavIcon Attachments", locale);
                createFolder(whiteLabelFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WHITE_LABEL_FAVICON, null);
            }

            EdsFolder whatsAppFolder = folderManager.getFolderByFolderType(EdsFolder.F_WHATSAPP_MEDIA);
            if (whatsAppFolder == null) {
                String whatsAppFolderName = messageSource.getMessage("whatsAppAttachments", null, "WhatsApp Attachments", locale);
                createFolder(whatsAppFolderName, afFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WHATSAPP_MEDIA, null);
            }
        }
    }

    @Transactional
    public void createCRMSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder crmFolder = folderManager.getFolderByFolderType(EdsFolder.F_CRM_ROOT);
        if (crmFolder == null) {
            String crmRootFolderName = messageSource.getMessage("createSystemFolders.crmRootFolderName", null, "CRM", locale);
            crmFolder = createFolder(crmRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CRM_ROOT, null);
            String contactAttachmentsFolderName = messageSource.getMessage("createSystemFolders.contactAttachmentsFolderName", null, "Contacts Attachments", locale);
            createFolder(contactAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CRM_CONTACT, null);
            String leadsAttachmentsFolderName = messageSource.getMessage("createSystemFolders.leadsAttachmentsFolderName", null, "Leads Attachments", locale);
            createFolder(leadsAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_LEAD, null);
            String caseAttachmentsFolderName = messageSource.getMessage("createSystemFolders.caseAttachmentsFolderName", null, "Case Attachments", locale);
            createFolder(caseAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CASE, null);
            String clientCustomerAttachmentsFolderName = messageSource.getMessage("createSystemFolders.clientCustomerAttachmentsFolderName", null, "Client/Customer Attachments", locale);
            createFolder(clientCustomerAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CLIENT, null);
            String opportunityAttachmentsFolderName = messageSource.getMessage("createSystemFolders.opportunityAttachmentsFolderName", null, "Opportunity Attachments", locale);
            createFolder(opportunityAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_OPPORTUNITY, null);
            String solutionAttachmentsFolderName = messageSource.getMessage("createSystemFolders.solutionAttachmentsFolderName", null, "Solution Attachments", locale);
            createFolder(solutionAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_SOLUTION, null);
            String crmAccountAttachmentsFolderName = messageSource.getMessage("createSystemFolders.crmAccountAttachmentsFolderName", null, "CRM Account Attachments", locale);
            createFolder(crmAccountAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CRM_ACCOUNT, null);
            String massMailing = messageSource.getMessage("createSystemFolders.massMailingAttachmentsFolderName", null, "Mass Mailing Attachments", locale);
            createFolder(massMailing, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_MASS_MAILING, null);
        } else {
            EdsFolder opportunityFolder = folderManager.getFolderByFolderType(EdsFolder.F_OPPORTUNITY);
            if (opportunityFolder == null) {
                String opportunityAttachmentsFolderName = messageSource.getMessage("createSystemFolders.opportunityAttachmentsFolderName", null, "Opportunity Attachments", locale);
                createFolder(opportunityAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_OPPORTUNITY, null);
            }
            EdsFolder solutionFolder = folderManager.getFolderByFolderType(EdsFolder.F_SOLUTION);
            if (solutionFolder == null) {
                String solutionAttachmentsFolderName = messageSource.getMessage("createSystemFolders.solutionAttachmentsFolderName", null, "Solution Attachments", locale);
                createFolder(solutionAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_SOLUTION, null);
            }
            EdsFolder crmAccountFolder = folderManager.getFolderByFolderType(EdsFolder.F_CRM_ACCOUNT);
            if (crmAccountFolder == null) {
                String crmAccountAttachmentsFolderName = messageSource.getMessage("createSystemFolders.crmAccountAttachmentsFolderName", null, "CRM Account Attachments", locale);
                createFolder(crmAccountAttachmentsFolderName, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CRM_ACCOUNT, null);
            }
            EdsFolder massMailingFolder = folderManager.getFolderByFolderType(EdsFolder.F_MASS_MAILING);
            if (massMailingFolder == null) {
                String massMailing = messageSource.getMessage("createSystemFolders.massMailingAttachmentsFolderName", null, "Mass Mailing Attachments", locale);
                createFolder(massMailing, crmFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_MASS_MAILING, null);
            }
        }
    }

    @Transactional
    public void createCustomFieldSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        int c = 0;
        int flushCommit = 0;
        EdsFolder cfFolder = folderManager.getCustomFieldRootFolder(company);
        if (cfFolder == null) {
            String customFieldFolderName = messageSource.getMessage("createSystemFolders.customFieldFolderName", null, "Custom Fields", locale);
            createFolder(customFieldFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CUSTOM_FIELD_ROOT, null);
        }
        List<EdsCompanyCustomFieldsSettings> comCustomFields = companyCFManager.getCompanyFileUploadCustomFields();
        for (EdsCompanyCustomFieldsSettings ccfs : comCustomFields) {
            EdsFolder projectFolder = folderManager.getCustomFieldFolder(ccfs.getObjectID());
            if (projectFolder == null) {
                System.out.println("COUNT:>>>>>>>> : " + c++);
                createCustomFieldFolder(ccfs.getObjectID());
                flushCommit++;
                flushCommit++;
            }
            if (flushCommit > 5) {
                folderManager.flushAndClear();
                flushCommit = 0;
            }
        }
    }

    @Transactional
    public void createHRMSSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder hrmsFolder = folderManager.getFolderByFolderType(EdsFolder.F_HRMS_ROOT);
        if (hrmsFolder == null) {
            String hrmsRootFolderName = messageSource.getMessage("createSystemFolders.hrmsRootFolderName", null, "HRMS", locale);
            hrmsFolder = createFolder(hrmsRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_HRMS_ROOT, null);
            String personalGoalsFolderName = messageSource.getMessage("createSystemFolders.personalGoalsFolderName", null, "Personal Goals", locale);
            createFolder(personalGoalsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PERS_GOAL, null);
            String departmentGoalsFolderName = messageSource.getMessage("createSystemFolders.departmentGoalsFolderName", null, "Department Goals", locale);
            createFolder(departmentGoalsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_DEP_GOAL, null);
            String projectGoalsFolderName = messageSource.getMessage("createSystemFolders.projectGoalsFolderName", null, "Project Goals", locale);
            createFolder(projectGoalsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PROJ_GOAL, null);
            String businessGoalsFolderName = messageSource.getMessage("createSystemFolders.businessGoalsFolderName", null, "Business Goals", locale);
            createFolder(businessGoalsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_BUSS_GOAL, null);
            String companyGoalsFolderName = messageSource.getMessage("createSystemFolders.companyGoalsFolderName", null, "Company Goals", locale);
            createFolder(companyGoalsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_COMP_GOAL, null);
            String employeeProfileAttachmentsFolderName = messageSource.getMessage("createSystemFolders.employeeProfileAttachmentsFolderName", null, "Employee Profile Attachments", locale);
            createFolder(employeeProfileAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EMPLOYEE_PROFILE, null);
            String vacancyAttachmentsFolderName = messageSource.getMessage("createSystemFolders.vacancyAttachmentsFolderName", null, "Vacancy Attachments", locale);
            createFolder(vacancyAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_VACANCY, null);
            String candidateAttachmentsFolderName = messageSource.getMessage("createSystemFolders.candidateAttachmentsFolderName", null, "Candidate Attachments", locale);
            createFolder(candidateAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CANDIDATE, null);
            String placementAttachmentsFolderName = messageSource.getMessage("createSystemFolders.placementAttachmentsFolderName", null, "Placement Attachments", locale);
            createFolder(placementAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PLACEMENT, null);
            String leaveRequestAttachmentsFolderName = messageSource.getMessage("createSystemFolders.leaveRequestAttachmentsFolderName", null, "Leave Request Attachments", locale);
            createFolder(leaveRequestAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_LEAVE_REQUEST, null);
            String incidentAttachmentsFolderName = messageSource.getMessage("createSystemFolders.incidentAttachmentsFolderName", null, "Incident Attachments", locale);
            createFolder(incidentAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_INCIDENT, null);
            String pastEmploymentAttachmentsFolderName = messageSource.getMessage("createSystemFolders.pastEmploymentAttachmentsFolderName", null, "Past Employment Attachments", locale);
            createFolder(pastEmploymentAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PAST_EMPLOYMENT, null);
            String internalEmploymentAttachmentsFolderName = messageSource.getMessage("createSystemFolders.internalEmploymentAttachmentsFolderName", null, "Internal Employment Attachments", locale);
            createFolder(internalEmploymentAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_INTERNAL_EMPLOYMENT, null);
            String companyDocRootFolderName = messageSource.getMessage("createSystemFolders.companyDocRootFolderName", null, "Company Documents", locale);
            createFolder(companyDocRootFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_COMPANY_DOCUMENTS, null);
            String dependantAttachmentsFolderName = messageSource.getMessage("createSystemFolders.dependantAttachmentsFolderName", null, "Dependent Attachments", locale);
            createFolder(dependantAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_DEPENDENTS, null);
            String employeeAttendanceAttachmentsFolderName = messageSource.getMessage("createSystemFolders.employeeAttendanceAttachmentsFolderName", null, "Employee Attendance Attachments", locale);
            createFolder(employeeAttendanceAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EMPLOYEE_ATTENDANCE, null);
        } else {
            EdsFolder companyDocumentFolder = folderManager.getFolderByFolderType(EdsFolder.F_COMPANY_DOCUMENTS);
            if (companyDocumentFolder == null) {
                String companyDocRootFolderName = messageSource.getMessage("createSystemFolders.companyDocRootFolderName", null, "Company Documents", locale);
                createFolder(companyDocRootFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_COMPANY_DOCUMENTS, null);
            }

            EdsFolder emplProfFolder = folderManager.getFolderByFolderType(EdsFolder.F_EMPLOYEE_PROFILE);
            if (emplProfFolder == null) {
                String employeeProfileAttachmentsFolderName = messageSource.getMessage("createSystemFolders.employeeProfileAttachmentsFolderName", null, "Employee Profile Attachments", locale);
                createFolder(employeeProfileAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EMPLOYEE_PROFILE, null);
            }
            EdsFolder vacancyFolder = folderManager.getFolderByFolderType(EdsFolder.F_VACANCY);
            if (vacancyFolder == null) {
                String vacancyAttachmentsFolderName = messageSource.getMessage("createSystemFolders.vacancyAttachmentsFolderName", null, "Vacancy Attachments", locale);
                createFolder(vacancyAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_VACANCY, null);
            }
            EdsFolder candidateFolder = folderManager.getFolderByFolderType(EdsFolder.F_CANDIDATE);
            if (candidateFolder == null) {
                String candidateAttachmentsFolderName = messageSource.getMessage("createSystemFolders.candidateAttachmentsFolderName", null, "Candidate Attachments", locale);
                createFolder(candidateAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CANDIDATE, null);
            }
            EdsFolder placementFolder = folderManager.getFolderByFolderType(EdsFolder.F_PLACEMENT);
            if (placementFolder == null) {
                String placementAttachmentsFolderName = messageSource.getMessage("createSystemFolders.placementAttachmentsFolderName", null, "Placement Attachments", locale);
                createFolder(placementAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PLACEMENT, null);
            }
            EdsFolder leaveRequestFolder = folderManager.getFolderByFolderType(EdsFolder.F_LEAVE_REQUEST);
            if (leaveRequestFolder == null) {
                String leaveRequestAttachmentsFolderName = messageSource.getMessage("createSystemFolders.leaveRequestAttachmentsFolderName", null, "Leave Request Attachments", locale);
                createFolder(leaveRequestAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_LEAVE_REQUEST, null);
            }
            EdsFolder incidentFolder = folderManager.getFolderByFolderType(EdsFolder.F_INCIDENT);
            if (incidentFolder == null) {
                String incidentAttachmentsFolderName = messageSource.getMessage("createSystemFolders.incidentAttachmentsFolderName", null, "Incident Attachments", locale);
                createFolder(incidentAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_INCIDENT, null);
            }
            EdsFolder pastEmploymentFolder = folderManager.getFolderByFolderType(EdsFolder.F_PAST_EMPLOYMENT);
            if (pastEmploymentFolder == null) {
                String pastEmploymentAttachmentsFolderName = messageSource.getMessage("createSystemFolders.pastEmploymentAttachmentsFolderName", null, "Past Employment Attachments", locale);
                createFolder(pastEmploymentAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PAST_EMPLOYMENT, null);
            }
            EdsFolder internalEmploymentFolder = folderManager.getFolderByFolderType(EdsFolder.F_INTERNAL_EMPLOYMENT);
            if (internalEmploymentFolder == null) {
                String internalEmploymentAttachmentsFolderName = messageSource.getMessage("createSystemFolders.internalEmploymentAttachmentsFolderName", null, "Internal Employment Attachments", locale);
                createFolder(internalEmploymentAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_INTERNAL_EMPLOYMENT, null);
            }
            EdsFolder dependantFolder = folderManager.getFolderByFolderType(EdsFolder.F_DEPENDENTS);
            if (dependantFolder == null) {
                String dependantAttachmentsFolderName = messageSource.getMessage("createSystemFolders.dependantAttachmentsFolderName", null, "Dependent Attachments", locale);
                createFolder(dependantAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_DEPENDENTS, null);
            }
            EdsFolder employeeAttendanceFolder = folderManager.getFolderByFolderType(EdsFolder.F_EMPLOYEE_ATTENDANCE);
            if (employeeAttendanceFolder == null) {
                String employeeAttendanceAttachmentsFolderName = messageSource.getMessage("createSystemFolders.employeeAttendanceAttachmentsFolderName", null, "Employee Attendance Attachments", locale);
                createFolder(employeeAttendanceAttachmentsFolderName, hrmsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EMPLOYEE_ATTENDANCE, null);
            }
        }
    }

    @Transactional
    public void createWebsiteSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder websiteFolder = folderManager.getFolderByFolderType(EdsFolder.F_WEBSITE_ROOT);
        if (websiteFolder == null) {
            String websiteRootFolderName = messageSource.getMessage("createSystemFolders.websiteRootFolderName", null, "Website", locale);
            websiteFolder = createFolder(websiteRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WEBSITE_ROOT, null);
            String blockDocumentsFolderName = messageSource.getMessage("createSystemFolders.blockDocumentsFolderName", null, "Block Documents", locale);
            createFolder(blockDocumentsFolderName, websiteFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WEBSITE_BLOCK, null);
        }
    }

    @Transactional
    public void createWorkspaceSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder workspaceFolder = folderManager.getFolderByFolderType(EdsFolder.F_WORKSPACE_ROOT);
        if (workspaceFolder == null) {
            String workspaceRootFolderName = messageSource.getMessage("createSystemFolders.workspaceRootFolderName", null, "Workspace", locale);
            workspaceFolder = createFolder(workspaceRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_WORKSPACE_ROOT, null);
            String eventAttachmentsFolderName = messageSource.getMessage("createSystemFolders.eventAttachmentsFolderName", null, "Event Attachments", locale);
            createFolder(eventAttachmentsFolderName, workspaceFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EVENT, null);
            String meetingMinutesFolderName = messageSource.getMessage("createSystemFolders.meetingMinutesFolderName", null, "Meeting Minutes Attachments", locale);
            createFolder(meetingMinutesFolderName, workspaceFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_MEETING_MINUTES, null);
        } else {
            EdsFolder meetingMinutesFolder = folderManager.getFolderByFolderType(EdsFolder.F_MEETING_MINUTES);
            if (meetingMinutesFolder == null) {
                String meetingMinutesFolderName = messageSource.getMessage("createSystemFolders.meetingMinutesFolderName", null, "Meeting Minutes Attachments", locale);
                createFolder(meetingMinutesFolderName, workspaceFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_MEETING_MINUTES, null);
            }
        }
    }

    @Transactional
    public void createSettingsSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder settingsFolder = folderManager.getFolderByFolderType(EdsFolder.F_SETTINGS_ROOT);
        if (settingsFolder == null) {
            String settingsRootFolderName = messageSource.getMessage("createSystemFolders.settingsRootFolderName", null, "Settings", locale);
            settingsFolder = createFolder(settingsRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_SETTINGS_ROOT, null);
            String emailTemplateAttachmentsFolderName = messageSource.getMessage("createSystemFolders.emailTemplateAttachmentsFolderName", null, "Email Template Attachments", locale);
            createFolder(emailTemplateAttachmentsFolderName, settingsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EMAIL_TEMPLATE, null);
        }
    }

    @Transactional
    public void createPayrollSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder payrollFolder = folderManager.getFolderByFolderType(EdsFolder.F_PAYROLL_ROOT);
        if (payrollFolder == null) {
            String payrollRootFolderName = messageSource.getMessage("createSystemFolders.payrollRootFolderName", null, "Payroll", locale);
            payrollFolder = createFolder(payrollRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PAYROLL_ROOT, null);
            String cashAdvanceFolderName = messageSource.getMessage("createSystemFolders.cashAdvanceFolderName", null, "Cash Advance", locale);
            createFolder(cashAdvanceFolderName, payrollFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CASH_ADVANCE, null);
            String additionalPaymentFolderName = messageSource.getMessage("createSystemFolders.additionalPaymentFolderName", null, "Additional payment", locale);
            createFolder(additionalPaymentFolderName, payrollFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_ADDITIONAL_PAYMENT, null);
        } else {
            EdsFolder cashAdvanceFolder = folderManager.getFolderByFolderType(EdsFolder.F_CASH_ADVANCE);
            if (cashAdvanceFolder == null) {
                String cashAdvanceFolderName = messageSource.getMessage("createSystemFolders.cashAdvanceFolderName", null, "Cash Advance", locale);
                createFolder(cashAdvanceFolderName, payrollFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CASH_ADVANCE, null);
            }
            EdsFolder additionalPaymentFolder = folderManager.getFolderByFolderType(EdsFolder.F_ADDITIONAL_PAYMENT);
            if (additionalPaymentFolder == null) {
                String additionalPaymentFolderName = messageSource.getMessage("createSystemFolders.additionalPaymentFolderName", null, "Additional payment", locale);
                createFolder(additionalPaymentFolderName, payrollFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_ADDITIONAL_PAYMENT, null);
            }
        }
    }

    @Transactional
    public void createNoteSystemFolder(EdsCompany company, EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder noteRootFolder = folderManager.getFolderByFolderType(EdsFolder.F_NOTE_ROOT);
        if (noteRootFolder == null) {
            String noteRootFolderName = messageSource.getMessage("createSystemFolders.note", null, "Note", locale);
            noteRootFolder = createFolder(noteRootFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_NOTE_ROOT, null);

            String noteFolderName = messageSource.getMessage("noteAttachments", null, "Note Attachments", locale);
            createFolder(noteFolderName, noteRootFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_NOTE, null);
        } else {
            EdsFolder noteFolder = folderManager.getFolderByFolderType(EdsFolder.F_NOTE);
            if (noteFolder == null) {
                String noteFolderName = messageSource.getMessage("noteAttachments", null, "Note Attachments", locale);
                createFolder(noteFolderName, noteRootFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_NOTE, null);
            }
        }
    }

    @Transactional
    public void createCompanyPublicSystemFolder(EdsCompany company, EdsUser companyCreator, Locale locale) {
        EdsFolder companyPublicFolder = folderManager.getPublicFolder(company.getObjectID());
        if (companyPublicFolder == null) {
            String companyPublicFolderName = messageSource.getMessage("createSystemFolders.companyPublicFolderName", null, "Public", locale);
            companyPublicFolder = createFolder(companyPublicFolderName, null, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_COMPANY_PUBLIC_ROOT, null);
        }
    }

    @Transactional
    public void createDBBackupSystemFolder(EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder backupsFolder = folderManager.getFolderByFolderType(EdsFolder.F_BACKUPS_ROOT);
        if (backupsFolder == null) {
            String backupsFolderName = messageSource.getMessage("createSystemFolders.backupsFolderName", null, "Backups", locale);
            createFolder(backupsFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_BACKUPS_ROOT, null);
        }
    }

    public void createDBXmlBackupSystemFolder(EdsFolder systemEdsFolder, EdsUser companyCreator, Locale locale) {
        EdsFolder backupsFolder = folderManager.getFolderByFolderType(EdsFolder.F_XML_BACKUPS_ROOT);
        if (backupsFolder == null) {
            String backupsFolderName = messageSource.getMessage("createSystemFolders.backupsFolderName", null, "Xml Backups", locale);
            createFolder(backupsFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_XML_BACKUPS_ROOT, null);
        }
    }

    @Transactional
    public void createTMPSystemFolder(EdsCompany company, EdsUser companyCreator, Locale locale) {
        EdsFolder tmpFolder = folderManager.getTempFolder(company.getObjectID());
        if (tmpFolder == null) {
            String tempFolderName = messageSource.getMessage("createSystemFolders.tempFolderName", null, "TEMP", locale);
            createFolder(tempFolderName, null, companyCreator, EdsFolder.TEMP, EdsFolder.F_DEFAULT, null);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public void executeSolrQuery(String query, String core) {
        try {
            solrManager.removeEntity(query, core);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns current user's all folders which to be inside the folder "My Folders"
     * used for getting folders list from other sections (except Documents)
     *
     * @return List<FolderResource> object
     */
    @Override
    public List<FolderResource> getFolders() throws ObjectNotFoundException {
        return getFolders(null);
    }

    /**
     * This method returns current user's all folders which to be inside the folder "My Folders"
     * used for getting folders list from other sections (except Documents)
     *
     * @param folderId
     * @return List<FolderResource> object
     */
    @Transactional
    @Override
    public ArrayList<FolderResource> getFolders(Integer folderId) throws ObjectNotFoundException {
        final EdsUser user = userManager.getUser();
        if (folderId != null) {
            ArrayList<FolderResource> folders = new ArrayList<>();
            try {
                folders.add(getFolderResource(folderId, user));
            } catch (InsufficientPermissionsException e) {
                e.printStackTrace();
            }
            return folders;
        }
        EdsCompany company = user.getCompany();
        //Return shared folders for Robert companys
        if (Integer.valueOf(3465).equals(company.getObjectID()) && !Integer.valueOf(5958).equals(user.getObjectID())/*&& !user.hasRole(roleManager.get(EdsRole.ADMIN))*/) {
            return getSharedResource(5958);//Robert id
        }
        //Return shared folders for second Robert companys
        if (Integer.valueOf(8687).equals(company.getObjectID()) && !Integer.valueOf(29229).equals(user.getObjectID())/*&& !user.hasRole(roleManager.get(EdsRole.ADMIN))*/) {
            return getSharedResource(29229);//Second Robert id
        }
        //Return shared folders for third Robert companys
        if (Integer.valueOf(25608).equals(company.getObjectID()) && !Integer.valueOf(1).equals(user.getObjectID())/*&& !user.hasRole(roleManager.get(EdsRole.ADMIN))*/) {
            return getSharedResource(1);//Third Robert id
        }

        ArrayList<FolderResource> folderResourceList = new ArrayList<>();
        EdsFolder rootFolder = folderManager.getRootFolder(user.getObjectID());

        if (rootFolder == null) {//Create root folder
            Locale locale = ServerUtils.getUserLocale();
            String myFoldersName = messageSource.getMessage("createSystemFolders.myFoldersName", null, "My folders", locale);
            rootFolder = createFolder(myFoldersName, null, user, EdsFolder.CUSTOM, EdsFolder.F_DEFAULT, null);
        }

        try {
            folderResourceList.add(getFolderResource(rootFolder, user));
        } catch (InsufficientPermissionsException e) {
            e.printStackTrace();
        }
        return folderResourceList;
    }

    /**
     * This method returns current user's all LEFT folders
     * used for getting folders list from other sections (except Documents)
     *
     * @param isClient
     * @return List<SelectItem> object
     */
    @Transactional
    @Override
    public LinkedList<SelectItem> getLeftFolders(boolean isClient) throws ObjectNotFoundException {
        LinkedList<SelectItem> result = new LinkedList<>();
//        result.add(new SelectItem(null, "all"));
        if (!isClient) {
            try {
                SystemResource systemResource = getSystemFolder();
                if (systemResource != null) {
                    result.add(new SelectItem(systemResource.getObjectId(), "system"));
                }
            } catch (InsufficientPermissionsException ignored) {

            }
            try {
                FolderResource publicResource = getPublicFolder();
                if (publicResource != null) {
                    result.add(new SelectItem(publicResource.getObjectId(), "public"));
                }
            } catch (InsufficientPermissionsException ignored) {

            }
        }
        if (!isClient) {
            SharedResource sharedResource = getSharedFolder();
            if (sharedResource != null) {
                result.add(new SelectItem(sharedResource.getObjectId(), "shared"));
            }
        }
        OthersResource othersResource = getOthersShared();
        if (othersResource != null) {
            result.add(new SelectItem(othersResource.getObjectId(), "others"));
        }
        TrashResource trashResource = getTrashedFolder();
        if (trashResource != null) {
            result.add(new SelectItem(trashResource.getObjectId(), "trash"));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FolderResource getSubFolders(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException {
        EdsUser user = folderManager.getUser();
        FolderResource folderResource = getFolderResource(folderId, user);
        folderResource.setFolders(new ArrayList<>(getSubFolders(folderId, false)));
        return folderResource;
    }

    public HashMap<String, FolderResource> getAllMainFolders() {
        HashMap<String, FolderResource> allMainFolders = new HashMap<>();


        try {
            List<FolderResource> myFolders = getFolders();
            for (FolderResource mainFolderResource : myFolders) {
                mainFolderResource.setSubfolders(new ArrayList<>(getSubFolders(mainFolderResource.getObjectId(), false)));
                mainFolderResource.setHasChild(true);
                mainFolderResource.setName(commonLocalizer.localize("myFolders", mainFolderResource.getName()));
                allMainFolders.put("myFolder", mainFolderResource);
            }

            SystemResource systemFolders = getSystemFolder();

            FolderResource systemFoldersResource = new FolderResource();
            systemFoldersResource.setName(commonLocalizer.localize("sysTemFolder", systemFolders.getName()));
            systemFoldersResource.setFiles(systemFolders.getFiles());
            systemFoldersResource.setObjectId(systemFolders.getObjectId());
            systemFoldersResource.setSubfolders(getSystemSubFolders(systemFolders.getObjectId()));
            systemFoldersResource.setHasChild(true);

            allMainFolders.put("systemFolder", systemFoldersResource);

            SharedResource sharedFolders = getSharedFolder();

            FolderResource sharedFoldersResource = new FolderResource();
            sharedFoldersResource.setName(commonLocalizer.localize(PdfLocalizationName.sharedByMe, "Shared By Me"));
            sharedFoldersResource.setFiles(sharedFolders.getFiles());
            sharedFoldersResource.setObjectId(sharedFolders.getObjectId());
            sharedFoldersResource.setSubfolders(sharedFolders.getSubFolders());
            sharedFoldersResource.setHasChild(true);
            allMainFolders.put("sharedByMe", sharedFoldersResource);

            OthersResource othersResource = getOthersShared();

            ArrayList<FolderResource> otherFolderResourceList = new ArrayList<>();

            for (OtherUserResource otherUserResource : othersResource.getOtherUsers()) {
                FolderResource otherFolderResource = new FolderResource();
                otherFolderResource.setName(otherUserResource.getUsername());
                otherFolderResource.setFiles(otherUserResource.getFiles());
                otherFolderResource.setObjectId(otherUserResource.getObjectId());
                otherFolderResource.setHasChild(true);
                otherFolderResourceList.add(otherFolderResource);
            }

            FolderResource sharedWithMe = new FolderResource();
            sharedWithMe.setName(commonLocalizer.localize(PdfLocalizationName.sharedWithMe, "Shared With Me"));
            sharedWithMe.setObjectId(othersResource.getObjectId());
            sharedWithMe.setSubfolders(otherFolderResourceList);
            sharedWithMe.setHasChild(true);

            allMainFolders.put("sharedWithMe", sharedWithMe);
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            e.printStackTrace();
        }
        return allMainFolders;
    }

    @Transactional
    @Override
    public FolderResource createFolder(Integer parentId, String name)
            throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException {
        // Validate.
        if (StringUtils.isEmpty(name)) {
            throw new ObjectNotFoundException("New folder name is empty");
        }
        if (parentId == null) {
            throw new ObjectNotFoundException("No parent specified");
        }
        if (folderManager.existsFolder(parentId, name)) {
            throw new DuplicateNameException("A folder with the name '" +
                    name + "' already exists at this level");
        }

        EdsUser creator = folderManager.getUser();

        EdsFolder parent;
        parent = folderManager.get(parentId);
        EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(parent, creator);
        if (!permission.hasWrite()) {
            throw new InsufficientPermissionsException("You don't have the permissions" +
                    " to write to this folder");
        }

        // Do the actual work.
        return createFolder(name, parent, creator, EdsFolder.CUSTOM, EdsFolder.F_DEFAULT, null).getDTO();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public UserResource getCurrentUserResource() {
        return folderManager.getUser().getDTO();
    }

    /**
     * Create a new folder related to EdsProject.
     *
     * @param projectId
     */
    @Transactional
    public void createProjectFolder(Integer projectId) {
        EdsProject project = projectManager.get(projectId);
        if (project == null) {
            return;
        }
        if (folderManager.getProjectFolder(projectId) != null) {
            return;
        }
        EdsUser creator = project.getManager() != null ? project.getManager() : projectManager.getUser();
        EdsCompany company = creator.getCompany();
        EdsFolder pmFolder = folderManager.getProjectRootFolder(company);
        EdsFolder projectFolder = createFolder(project.getName(), pmFolder, creator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PROJECT, projectId);
        Locale locale = ServerUtils.getUserLocale();

        String taskAttachmentsFolderName = messageSource.getMessage("createSystemFolders.taskAttachmentsFolderName", null, "Task Attachments", locale);
        createFolder(taskAttachmentsFolderName, projectFolder, creator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_TASK, projectId);
        String issuePmAttachmentsFolderName = messageSource.getMessage("createSystemFolders.issuePmAttachmentsFolderName", null, "Issue PM Attachments", locale);
        createFolder(issuePmAttachmentsFolderName, projectFolder, creator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_PR_ISSUE, projectId);
    }

    @Transactional
    public void createExpensePaymentFolder(Integer paymentId) {
        EdsExpensePayment expensePayment = expensePaymentManager.get(paymentId);
        EdsUser creator = expensePaymentManager.getUser();
        EdsCompany company = creator.getCompany();
        EdsFolder expPaymentFolder = folderManager.getExpensePaymentRootFolder(company);
        if (expPaymentFolder == null) {
            EdsFolder afFolder = folderManager.getFolderByFolderType(EdsFolder.F_AF_ROOT);
            Locale locale = ServerUtils.getUserLocale();
            String expensePaymentFolderName = messageSource.getMessage("expensePaymentAttachments", null, "Expense Payment Attachments", locale);
            createFolder(expensePaymentFolderName, afFolder, creator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EXP_PAYMENT, null);
        }
        createFolder(expensePayment.getReference() != null ? expensePayment.getReference() : expensePayment.getObjectID().toString(), expPaymentFolder, creator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_EXP_PAYMENT, paymentId);
    }

    /**
     * Create a new folder related to EdsProject.
     *
     * @param mailMessageID
     */
    @Transactional
    public void createMailMessageFolder(Integer mailMessageID) {
        EdsMailMessage mailMessage = mailMessageManager.get(mailMessageID);
        EdsUser creator = userManager.getUser();
        EdsCompany company = creator.getCompany();
        Locale locale = ServerUtils.getUserLocale();
        EdsFolder systemEdsFolder = folderManager.getSystemFolder(company.getObjectID());
        if (systemEdsFolder == null) {
            String systemFolderName = messageSource.getMessage("createSystemFolders.systemFolderName", null, "System Folder", locale);
            systemEdsFolder = createFolder(systemFolderName, null, company.getCreator(), EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_DEFAULT, null);
        }
        EdsFolder massMailingRootFolder = folderManager.getMassMailingFolder(company);
        if (massMailingRootFolder == null) {
            String massMailing = messageSource.getMessage("createSystemFolders.massMailingAttachmentsFolderName", null, "Mass Mailing Attachments", locale);
            massMailingRootFolder = createFolder(massMailing, systemEdsFolder, company.getCreator(), EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_MASS_MAILING, null);
        }
        EdsFolder folder = folderManager.getFolder(F_MASS_MAILING, mailMessageID);
        if (folder == null) {
            createFolder(mailMessage.getSubject(), massMailingRootFolder, creator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_MASS_MAILING, mailMessageID);
        }
    }

    @Transactional
    public void createCustomFieldFolder(Integer customFieldId) {
        EdsCompanyCustomFieldsSettings ccf = companyCFManager.get(customFieldId);
        EdsCompany company = companyManager.get(SecurityContext.getCompanyID());
        Locale locale = ServerUtils.getUserLocale();
        EdsUser companyCreator = employeeManager.getAdministrators().get(0);
        EdsFolder systemEdsFolder = folderManager.getSystemFolder(company.getObjectID());
        if (systemEdsFolder == null) {
            String systemFolderName = messageSource.getMessage("createSystemFolders.systemFolderName", null, "System Folder", locale);
            systemEdsFolder = createFolder(systemFolderName, null, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_DEFAULT, null);
        }
        EdsFolder cfFolder = folderManager.getCustomFieldRootFolder(company);
        if (cfFolder == null) {
            String customFieldFolderName = messageSource.getMessage("createSystemFolders.customFieldFolderName", null, "Custom Fields", locale);
            createFolder(customFieldFolderName, systemEdsFolder, companyCreator, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CUSTOM_FIELD_ROOT, null);
        }
        createFolder(ccf.getFieldName(), cfFolder, userManager.getUser(), EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_CUSTOM_FIELD_ITEM, customFieldId);
    }

    /**
     * Create a new folder with the provided name, parent and owner.
     *
     * @param name       folder name
     * @param parent     parent folder
     * @param creator    folder creator
     * @param type
     * @param folderType (F_DEFAULT-0,F_PROJECT-1,F_TASK-2,...)
     * @param entityId   (null,projectId,taskId,...)
     * @return the new folder
     */
    @Transactional
    public EdsFolder createFolder(String name, EdsFolder parent, EdsUser creator, int type, int folderType, Integer entityId) {
        EdsFolder folder = new EdsFolder();
        folder.setName(name);
        folder.setFolderType(folderType);
        folder.setEntityId(entityId);
        folder.setType(type);
        if (parent != null) {
            parent.addSubfolder(folder);
            folder.setOwner(parent.getOwner());
        } else {
            folder.setOwner(creator);
        }

        Date now = new Date();
        EdsAuditInfo auditInfo = new EdsAuditInfo();
        auditInfo.setCreatedBy(creator);
        auditInfo.setCreationDate(now);
        auditInfo.setModifiedBy(creator);
        auditInfo.setModificationDate(now);
        folder.setAuditInfo(auditInfo);
        folderManager.create(folder);
        folderManager.indexFolder(folder, false);
        if (parent != null) {
            parent.setHasChild(true);
            folderManager.setFolderHasChild(true, parent.getObjectID());
            folderManager.createOrUpdate(parent);
            if (folder.getType() != EdsFolder.SYSTEM_BUILTIN) {
                try {
                    setFolderPermissions(creator, folder, new ArrayList<>(getFolderPermissions(parent.getObjectID())), false);
                } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                    e.printStackTrace();
                }
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFolder.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        if (folder != null && folder.getObjectID() != null) {
            kpiLog.setEntityId(folder.getObjectID());
        }
        ServerUtils.kpiLog(log, kpiLog, "Create new folder");
        return folder;
    }

    @Override
    @Transactional
    public void deleteFolder(final Integer folderId) throws InsufficientPermissionsException, ObjectNotFoundException {
        // Do the actual work.
        final EdsFolder folder = folderManager.get(folderId);
        final EdsFolder parent = folder.getParent();
        if (parent == null) {
            throw new ObjectNotFoundException("Deleting the root folder is not allowed");
        }
        EdsUser user = userManager.getUser();
        EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(folder, user);
        if (!permission.hasDelete()) {
            throw new InsufficientPermissionsException("User " + user.getFullName() + " cannot delete folder " + folder.getName());
        }
        List<Integer> folderRbacList = new ArrayList<>();
        removeSubfolderFiles(folder, folderRbacList);
        deleteSubFolders(folder, folderRbacList);
        folderManager.delete(folder);

        if (folderManager.getChildsByParentId(parent.getObjectID()).size() <= 0) {
            parent.setHasChild(false);
            folderManager.createOrUpdate(parent);
        }


        //delete folder rbac entries
        folderRbacManager.removeFolderRbacList(folderRbacList);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFolder.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(folderId);
        ServerUtils.kpiLog(log, kpiLog, "Delete folder");
    }

    @Transactional
    public void deleteSubFolders(EdsFolder folder, List<Integer> folderRbacList) {
        //remove folders for all folder
        for (EdsFolder subfolder : folder.getSubfolders()) {
            deleteSubFolders(subfolder, folderRbacList);
            folderManager.delete(subfolder);
        }
        //remove folderrbacs for all folder
        folderRbacList.addAll(jdbcSpringManager.getFolderRbacEntries(folder.getObjectID()));
    }

    /**
     * Traverses the folder and deletes all actual files (file system)
     * regardless of permissions
     *
     * @param folder
     */
    @Transactional
    public void removeSubfolderFiles(EdsFolder folder, List<Integer> folderRbacList) {
        //remove files for all subfolders
        for (EdsFolder subfolder : folder.getSubfolders()) {
            removeSubfolderFiles(subfolder, folderRbacList);
        }
        //remove this folder's file bodies (actual files)

        for (EdsFileHeader file : folder.getFiles()) {
            for (EdsFileBody body : file.getBodies()) {
                fileBodyManager.delete(body);
            }
            folderRbacList.addAll(jdbcSpringManager.getFileRbacEntries(file.getObjectID()));
            folderRbacManager.removeFileFromSolr(file.getObjectID());
            fileHeaderManager.delete(file);
        }
    }

    /**
     * Traverses the folder and deletes all actual files (file system)
     * regardless of permissions
     *
     * @param fileBody
     * @param upType
     * @param fileType
     * @param entityId
     */
    @Transactional
    @Override
    public FileResource createFile(DocumentItem fileBody, String upType, int fileType, Integer entityId) throws DuplicateNameException, ObjectNotFoundException,
            InsufficientPermissionsException, QuotaExceededException {
        // Validate.
        EdsUser owner = fileBodyManager.getUser();
        return createFile(owner, fileBody, upType, fileType, entityId);
    }

    /**
     * Traverses the folder and deletes all actual files (file system)
     * regardless of permissions
     *
     * @param fileBody
     * @param upType
     * @param fileType
     * @param entityId
     * @param userID
     */
    @Transactional
    @Override
    public FileResource createFile(DocumentItem fileBody, String upType, int fileType, Integer entityId, Integer userID) throws DuplicateNameException, ObjectNotFoundException,
            InsufficientPermissionsException, QuotaExceededException {
        // Validate.
        EdsUser owner = userManager.get(userID);
        return createFile(owner, fileBody, upType, fileType, entityId);
    }

    /**
     * Traverses the folder and deletes all actual files (file system)
     * regardless of permissions
     *
     * @param owner
     * @param fileBody
     * @param upType
     * @param fileType
     * @param entityId
     */
    @Transactional
    public FileResource createFile(EdsUser owner, DocumentItem fileBody, String upType, int fileType, Integer entityId) throws DuplicateNameException, ObjectNotFoundException,
            InsufficientPermissionsException, QuotaExceededException {
        return createFile(owner, fileBody, upType, fileType, entityId, null);
    }

    @Transactional
    public FileResource createFile(EdsUser owner, DocumentItem fileBody, String upType, int fileType, Integer entityId, PermissionHolder permission) throws DuplicateNameException, ObjectNotFoundException,
            InsufficientPermissionsException, QuotaExceededException {
        // Validate.
        EdsUser edsUser = userManager.getUser();
        if (owner == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileBody.getFolderId() == null) {
            throw new ObjectNotFoundException("No folder specified");
        }
        if (StringUtils.isEmpty(fileBody.getName())) {
            throw new ObjectNotFoundException("No file name specified");
        }

        boolean isExist = folderManager.existsFile(fileBody.getFolderId(), fileBody.getName());
        boolean isFileBodyNameChanged = false;
        if (fileBody.getSize() == 0 && isExist
                && !(CommandConstants.OFFICE_365_DOCS_PARAM_NAME.equals(upType.replace("\"", "")) || Constants.OFFICE_365.equals(upType.replace("\"", "")))) {
            fileBody.setName(getFileName(fileBody.getName()));
            isFileBodyNameChanged = true;
        }

        // Do the actual work.
        EdsFolder parent = folderManager.get(fileBody.getFolderId());
        if (permission == null) {
            permission = folderRbacManager.getFolderEntryForUser2(parent, owner).getDTO();
        }
        if (!permission.isWrite() && F_EMPLOYEE_PROFILE != parent.getFolderType() && F_EVENT != parent.getFolderType()) {
            throw new InsufficientPermissionsException("You don't have the permissions to write to this folder");
        }
        EdsFileHeader file = new EdsFileHeader();
        if (isExist && !isFileBodyNameChanged) {
            file.setName(getFileName(fileBody.getName()));
        } else {
            file.setName(fileBody.getName());
        }
        System.out.println(fileBody.getName());
        parent.addFile(file);
        // set file owner to folder owner
        file.setOwner(parent.getOwner());

        Date now = new Date();
        EdsAuditInfo auditInfo = new EdsAuditInfo();
        auditInfo.setCreatedBy(edsUser != null ? edsUser : owner);
        auditInfo.setCreationDate(now);
        auditInfo.setModifiedBy(edsUser != null ? edsUser : owner);
        auditInfo.setModificationDate(now);
        file.setAuditInfo(auditInfo);
        file.setVersioned(false);

        // Create the file body.
        EdsFileBody body = new EdsFileBody();
        body.setContentType(identifyMimeType(fileBody.getName()));
        body.setAuditInfo(auditInfo);
        body.setDescription(ADD_DESCRIPTION.equals(fileBody.getDescription()) ? "" : fileBody.getDescription());
        body.setDriveFolderId(fileBody.getDriveFolderId());
        body.setDriveFolderName(fileBody.getDriveFolderName());
        body.setDownloadable(fileBody.isDownloadable());
        if (fileBody.getSize() > 0) {
            body.setFileSize(fileBody.getSize());
        } else if (fileBody.getInputStream() != null) {
            try {
                body.setFileSize(fileBody.getInputStream().available());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            body.setFileSize(0);
        }

        body.setInputStream(fileBody.getInputStream());

        body.setOriginalName(fileBody.getName());
        body.setDuration(fileBody.getDuration());
        EdsReference uploadType;
        if (CommandConstants.GOOGLE_DOCS_PARAM_NAME.equals(upType.replace("\"", "")) || Constants.GOOGLE.equals(upType.replace("\"", ""))) {
            uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.GOOGLE);
        } else if (CommandConstants.OFFICE_365_DOCS_PARAM_NAME.equals(upType.replace("\"", "")) || Constants.OFFICE_365.equals(upType.replace("\"", ""))) {
            uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.OFFICE_365);
        } else if (CommandConstants.OFFICE_365_DOCS_SHARE_POINT_PARAM_NAME.equals(upType.replace("\"", "")) || Constants.OFFICE_365_SHARE_POINT.equals(upType.replace("\"", ""))) {
            uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.OFFICE_365_SHARE_POINT);
        } else if (CommandConstants.MINIO_PARAM_NAME.equals(upType.replace("\"", "")) || Constants.MINIO.equals(upType.replace("\"", ""))) {
            uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.MINIO);
        } else if (CommandConstants.LOCAL_PARAM_NAME.equals(upType.replace("\"", "")) || Constants.LOCAL.equals(upType.replace("\"", ""))) {
            uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.LOCAL);
        } else {
            uploadType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.AMAZON);
        }
        body.setType(uploadType);
        //CLEAR OLD VERSION IF FILE IS NOT VERSIONED AND GETS UPDATED
        if (!file.isVersioned() && file.getCurrentBody() != null) {
            file.setCurrentBody(null);
            if (file.getBodies() != null) {
                Iterator<EdsFileBody> it = file.getBodies().iterator();
                while (it.hasNext()) {
                    EdsFileBody bo = it.next();
                    it.remove();
                    fileBodyManager.delete(bo);
                }
            }
        }

        file.addBody(body);
        file.setAuditInfo(auditInfo);
        file.setFileType(fileType);
        file.setEntityId(entityId);

        if (fileBody.getObjectId() != null) {
            EdsFileHeader originalFile = fileHeaderManager.get(fileBody.getObjectId());
            file.setDocumentID(originalFile.getDocumentID());
            file.setDocumentType(originalFile.getDocumentType());
            file.setExpireDate(originalFile.getExpireDate());
        }

        if (entityId != null && fileBody.isEmployeeDoc()) {
            file.setEnetityUser(userManager.get(entityId));
        }

        long timeMillis = System.currentTimeMillis();
        fileBodyManager.create(body);
        System.out.println("body : " + (System.currentTimeMillis() - timeMillis) + "_ ObjectId " + body.getObjectID());
        if (!fileBody.isDoNotAddToIndex()) {
            long s = System.currentTimeMillis();
            folderRbacManager.indexFile(file);
            System.out.println("Upload file SOLR took: " + (System.currentTimeMillis() - s));
            if (file.getFolder().getType() != EdsFolder.SYSTEM_BUILTIN) {
                setFolderPermissionsToFile(file, new ArrayList<>(getFolderPermissions(parent.getObjectID())), false, owner);
            }
        }

        if (file.getEntityId() != null) {
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(AttachmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, file, owner);
            event.setCustomStringField(file.getName());
            event.setRelationID(file.getEntityId());
            event.setRelationType(ServerUtils.getFolderRelationType(file.getFileType()));
        }

        EdsFolder tempFolder = folderManager.getTempFolder(owner.getCompany().getObjectID());
        if (tempFolder != null && !tempFolder.getObjectID().equals(file.getFolder().getObjectID())) {

            if (fileType == Constants.F_TASK) {
                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.TASK_FILE_ADD, file, owner);
            } else if (fileType == Constants.F_PROJECT) {
                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.PROJECT_FILE_ADD, file, owner);
            } else if (fileType == Constants.F_PR_ISSUE) {
                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.ISSUE_FILE_ADD, file, owner);
            }

        }

        FileResource fileResource = file.getDTO();
        fileResource.setAmazonLink(commonServiceLocal.getFileUrl(body.getObjectID()));
        // IF UPLOADED TO GOOGLE DOCS
        if (Constants.GOOGLE.equals(fileResource.getUploadType()) && sinxDocumentsSettingsManager.getSinxDocsSettings(body) != null) {
            fileResource.setGoogleDownloadLink(sinxDocumentsSettingsManager.getSinxDocsSettings(body).getDownloadLink());
        } else if (OFFICE_365.equals(fileResource.getUploadType()) || OFFICE_365_SHARE_POINT.equals(fileResource.getUploadType())) {
            EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(body);
            if (googleDocumentsSettings != null) {
                fileResource.setDocumentID(googleDocumentsSettings.getDocumentID());
                fileResource.setDocumentOpenID(googleDocumentsSettings.getDocumentOpenID());
                fileResource.setOfficeDownloadLink(googleDocumentsSettings.getDocumentLink());
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(fileResource.getObjectId());
        ServerUtils.kpiLog(log, kpiLog, "Uploaded new file");
        return fileResource;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void createFile(List<DocumentItem> files, String type, int fileType, Integer entityId, String sourceBucket, String destinationBucket) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException, QuotaExceededException {
        if (files != null && !files.isEmpty()) {
            EdsUser owner = userManager.getUser();

            for (DocumentItem fileBody : files) {
                fileBody.setSourceBucketName(sourceBucket);
                fileBody.setDestinationBucketName(destinationBucket);
                createFile2(owner, fileBody, fileType, entityId);
            }
        }

    }

    @Transactional
    public FileResource createFile2(EdsUser owner, DocumentItem fileBody, int fileType, Integer entityId) throws DuplicateNameException, ObjectNotFoundException,
            InsufficientPermissionsException, QuotaExceededException {
        // Validate.
        if (owner == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileBody.getFolderId() == null) {
            throw new ObjectNotFoundException("No folder specified");
        }
        if (StringUtils.isEmpty(fileBody.getName())) {
            throw new ObjectNotFoundException("No file name specified");
        }
        String contentType = identifyMimeType(fileBody.getName());
        if (StringUtils.isEmpty(contentType)) {
        }
        if (fileBody.getSize() == 0 && folderManager.existsFile(fileBody.getFolderId(), fileBody.getName())) {
            fileBody.setName(getFileName(fileBody.getName()));
        }

        // Do the actual work.
        EdsFolder parent = folderManager.get(fileBody.getFolderId());

        EdsFileHeader file = new EdsFileHeader();
        if (folderManager.existsFile(fileBody.getFolderId(), fileBody.getName())) {
            file.setName(getFileName(fileBody.getName()));
        } else {
            file.setName(fileBody.getName());
        }
        file.setDocumentName(fileBody.getDescription());
        System.out.println(fileBody.getName());
        parent.addFile(file);
        // set file owner to folder owner
        file.setOwner(parent.getOwner());

        Date now = new Date();
        EdsAuditInfo auditInfo = new EdsAuditInfo();
        auditInfo.setCreatedBy(owner);
        auditInfo.setCreationDate(now);
        auditInfo.setModifiedBy(owner);
        auditInfo.setModificationDate(now);
        file.setAuditInfo(auditInfo);
        file.setVersioned(false);

        // Create the file body.
        EdsFileBody body = new EdsFileBody();
        body.setContentType(fileBody.getContentType());
        body.setAuditInfo(auditInfo);
        body.setDescription(ADD_DESCRIPTION.equals(fileBody.getDescription()) ? "" : fileBody.getDescription());
        body.setFileSize(fileBody.getSize());
        body.setOriginalName(fileBody.getName());
        body.setType(referenceManager.findReference(_UPLOAD_TYPE, EdsContextParams.getUploadType()));

        file.addBody(body);
        file.setAuditInfo(auditInfo);
        file.setFileType(fileType);
        file.setEntityId(entityId);

        fileBodyManager.createUpload(body);

        try {
            uploadManager.putFile2(body, fileBody);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (!fileBody.isDoNotAddToIndex()) {
            folderRbacManager.indexFile(file);
            if (file.getFolder().getType() != EdsFolder.SYSTEM_BUILTIN) {
                setFolderPermissionsToFile(file, new ArrayList<>(getFolderPermissions(parent.getObjectID())), false, owner);
            }
        }

        FileResource fileResource = file.getDTO();
        // IF UPLOADED TO GOOGLE DOCS
        if (Constants.GOOGLE.equals(fileResource.getUploadType()) && sinxDocumentsSettingsManager.getSinxDocsSettings(body) != null) {
            fileResource.setGoogleDownloadLink(sinxDocumentsSettingsManager.getSinxDocsSettings(body).getDownloadLink());
        } else if (OFFICE_365.equals(fileResource.getUploadType()) || OFFICE_365_SHARE_POINT.equals(fileResource.getUploadType())) {
            EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(body);
            if (googleDocumentsSettings != null) {
                fileResource.setDocumentID(googleDocumentsSettings.getDocumentID());
                fileResource.setDocumentOpenID(googleDocumentsSettings.getDocumentOpenID());
                fileResource.setOfficeDownloadLink(googleDocumentsSettings.getDocumentLink());
            }
        } else {
            fileResource.setAmazonLink(commonServiceLocal.getFileUrl(body.getObjectID()));
        }

        return fileResource;
    }

    private String getFileName(String fullPath) {
        int dot = fullPath.lastIndexOf(".");
        String extension = fullPath.substring(dot + 1);
        Random r = new Random();
        String name = fullPath.substring(0, dot);
        String s = String.valueOf(r.nextLong());
        return name + "(" + s.substring(s.length() - 5) + ")." + extension;
    }

    @Transactional
    @Override
    public FileResource createFile(DocumentItem file) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException, QuotaExceededException {
        return createFile(file, EdsContextParams.getUploadType(), EdsFileBody.DEFAULT, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FileResource getFileResource(Integer fileId) {
        EdsFileHeader file = fileHeaderManager.get(fileId);
        if (file == null) {
            return null;
        }
        FileResource fileResource = file.getDTO();
        EdsUser user = userManager.getUser();
        fileResource.setPermission(folderRbacManager.getFilePermissionForUser(file, user).getDTO());
        if (GOOGLE.equals(fileResource.getUploadType())) {
            EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(file.getCurrentBody());
            if (googleDocumentsSettings != null) {
                fileResource.setGoogleDownloadLink(googleDocumentsSettings.getDocumentLink());
            }
        } else if (OFFICE_365.equals(fileResource.getUploadType()) || OFFICE_365_SHARE_POINT.equals(fileResource.getUploadType())) {
            EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(file.getCurrentBody());
            if (googleDocumentsSettings != null) {
                fileResource.setDocumentID(googleDocumentsSettings.getDocumentID());
                fileResource.setDocumentOpenID(googleDocumentsSettings.getDocumentOpenID());
                fileResource.setOfficeDownloadLink(googleDocumentsSettings.getDocumentLink());
            }
        } else {
            fileResource.setAmazonLink(commonServiceLocal.getFileUrl(fileResource.getBodyId()));
        }
        return fileResource;
    }

    // Returns the contents of the file in a byte array.

    /**
     * @param fileType F_CASH_ADVANCE,F_PROJECT
     * @param fileName the name of the file
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FileResource getFileResourceByFileTypeAndName(Integer fileType, String fileName) {
        EdsFileHeader file = fileHeaderManager.getFileByFileTypeFileName(fileType, fileName);
        if (file == null) {
            return null;
        }
        return file.getDTO();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public DocumentItem getFile(Integer fileId) {
        DocumentItem documentItem = new DocumentItem();
        documentItem.setStatus(DocumentItem.OK);
        if (fileId == null) {
            documentItem.setStatus("File");
        }
        final EdsUser user = fileHeaderManager.getUser();
        if (user == null) {
            documentItem.setStatus("No user specified");
        }
        // Do the actual work.
        final EdsFileHeader file = fileHeaderManager.get(fileId);
        final EdsFolder parent = file.getFolder();
        if (parent == null) {
            documentItem.setStatus("The specified file has no parent folder");
        }
        try {
            documentItem.setContent(getBytesFromStream(uploadManager.getInputStream(file.getCurrentBody())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        documentItem.setDescription(file.getCurrentBody().getDescription());
        documentItem.setContentType(file.getCurrentBody().getContentType());
        documentItem.setFolderId(parent.getObjectID());
        documentItem.setName(file.getCurrentBody().getOriginalName());
        return documentItem;
    }

    @Override
    public void deleteFile(Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException {
        deleteFile(fileId, null, null);
    }

    private EdsFolder getFolder(Integer folderType, Integer folderId, EdsCompany company) {
        EdsFolder folder = folderManager.getFolder(folderType, folderId);
        /**
         * This is in case of CRM and other folders, because CRM and other folders
         * have no id. Only project folder has id value.
         */
        if (folder == null) {
            folder = folderManager.getFolderByFolderType(folderType);
        }
        return folder;
    }

    private boolean changeFileFolderToCopyFile(Integer fileId, EdsFileHeader file) {
        List<EdsCopiedFileHeader> edsCopiedFileHeaders = copiedFileHeaderManager.getCopiedFile(fileId);
        if (edsCopiedFileHeaders != null && edsCopiedFileHeaders.size() > 0) {
            EdsCopiedFileHeader copyfileHeader = edsCopiedFileHeaders.get(0);
            file.setName(copyfileHeader.getName());
            file.setEntityId(copyfileHeader.getEntityId());
            file.setOwner(copyfileHeader.getOwner());
            file.setFolder(copyfileHeader.getFolder());
            file.setFileType(copyfileHeader.getFileType());

            fileHeaderManager.update(file);
            if (copyfileHeader.getFileType() == Constants.F_TASK) {
                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.TASK_FILE_ADD, file, copyfileHeader.getOwner());
            } else if (copyfileHeader.getFileType() == Constants.F_PROJECT) {
                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.PROJECT_FILE_ADD, file, copyfileHeader.getOwner());
            } else if (copyfileHeader.getFileType() == Constants.F_PR_ISSUE) {
                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.ISSUE_FILE_ADD, file, copyfileHeader.getOwner());
            }
            folderRbacManager.indexFile(file);
            copyfileHeader.setDeleted(true);
            copiedFileHeaderManager.update(copyfileHeader);

            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsCopiedFileHeader.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(fileId);
            ServerUtils.kpiLog(log, kpiLog, "Change document rolder");
            return true;
        }
        return false;
    }

    private boolean deleteCopiedFile(Integer fileId, Integer folderId) {
        EdsCopiedFileHeader edsCopiedFileHeader = copiedFileHeaderManager.getCopiedFile(fileId, folderId);
        if (edsCopiedFileHeader != null) {
            edsCopiedFileHeader.setDeleted(true);
            copiedFileHeaderManager.update(edsCopiedFileHeader);

            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsCopiedFileHeader.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(fileId);
            ServerUtils.kpiLog(log, kpiLog, "Delete copy document");
            return true;
        }
        return false;
    }

    public void batchDeleteFiles(ArrayList<Integer> fileIds) throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = fileHeaderManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        boolean batchDelete = false;
        // Do the actual work.
        for (Integer fileId : fileIds) {
            final EdsFileHeader file = fileHeaderManager.get(fileId);
            if (file.isDeleted()) {
                batchDelete = true;
                final EdsFolder parent = file.getFolder();
                if (parent == null) {
                    throw new ObjectNotFoundException("The specified file has no parent folder");
                }
                EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
                if (!permission.hasDelete()) {
                    throw new InsufficientPermissionsException("User " + user.getObjectID() + " cannot delete file " + file.getName() + "(" + file.getObjectID() + ")");
                }

                parent.removeFile(file);
                file.setCurrentBody(null);
                fileHeaderManager.update(file);
                List<EdsFileBody> fileBodyList = file.getBodies();
                for (final EdsFileBody body : fileBodyList) {
                    body.setHeader(null);
                    fileBodyManager.update(body);
                }

                fileHeaderManager.delete(file);

                for (final EdsFileBody body : fileBodyList) {
                    fileBodyManager.delete(body);
                }

                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.DELETE);
                kpiLog.setEntityId(fileId);
                ServerUtils.kpiLog(log, kpiLog, "Delete document");
            } else {
                moveFileToTrash(fileId);
            }
        }
        if (batchDelete) {
            for (Integer fileId : fileIds) {
                folderRbacManager.removeIndexFileEntries(fileId);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ListResult<FileResource> listFile(ListingFilterParameter fp) throws ObjectNotFoundException {
        return getFileList(fp);
    }

    /**
     * Get Company & Employee Docs Expiry For Document Expiry Widget
     *
     * @param fp
     * @return List of FileResource
     * @sort sorted by docs expiry date
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<FileResource> getCompanyAndEmployeeDocumentExpiryList(ListingFilterParameter fp) {
        ArrayList<FileResource> resultList = new ArrayList<>();
        EdsUser user = fileHeaderManager.getUser();
        fp.setDueDate(ServerUtils.getDayStartTime(new Date()));

        //get company docs
        fp.setFolderType(F_COMPANY_DOCUMENTS);
        List<FileResource> companyDocs = getFileResource(fp, user).getList();

        //get employee docs
        fp.setDataType(DASHBOARD_WIDGET_CODE.DOCUMENT_EXPIRY);
        fp.setFolderType(F_EMPLOYEE_PROFILE);
        fp.setUserID(user.getObjectID());
        fp.setEntityID(user.getObjectID());
//        if (!user.getRoleIds().contains(EdsRole.HR)) {
//            fp.setCrmEntityId(user.getObjectID());
//        }
        List<FileResource> employeeDocs = getFileResource(fp, user).getList();

        resultList.addAll(companyDocs);
        resultList.addAll(employeeDocs);

        resultList.sort(Comparator.comparing(FileResource::getDaysLeft));

        return resultList;
    }

    @Transactional
    public void deleteFile(final Integer fileId, Integer folderId, Integer folderType) throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = fileHeaderManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileId == null) {
            throw new ObjectNotFoundException("No file specified");
        }
        final EdsFileHeader file = fileHeaderManager.get(fileId);
        if (file == null) {
            throw new ObjectNotFoundException("No file specified");
        }
        final EdsFolder parent = file.getFolder();

        if (folderId != null && folderType != null) {
            EdsFolder copyfolder = getFolder(folderType, folderId, null);
            if (deleteCopiedFile(fileId, copyfolder.getObjectID())) return;
        }
        if (changeFileFolderToCopyFile(fileId, file)) return;
        // Do the actual work.

        if (parent == null) {
            throw new ObjectNotFoundException("The specified file has no parent folder");
        }
        EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
        if (!permission.hasDelete()) {
            throw new InsufficientPermissionsException("User " + user.getObjectID() + " cannot delete file " + file.getName() + "(" + file.getObjectID() + ")");
        }
        folderRbacManager.removeIndexFileEntries(file.getObjectID());
        parent.removeFile(file);
        file.setCurrentBody(null);
        fileHeaderManager.update(file);
        List<EdsFileBody> fileBodyList = file.getBodies();
        for (final EdsFileBody body : fileBodyList) {
            body.setHeader(null);
            fileBodyManager.update(body);
        }

        fileHeaderManager.delete(file);

        for (final EdsFileBody body : fileBodyList) {
            EdsUploadAmazonSettings settings = uploadAmazonSettingsManager.getUploadAmazonSettings(body);
            if (settings != null) {
                uploadAmazonSettingsManager.delete(settings);
            }
            fileBodyManager.delete(body);
        }

        if (folderType != null && Constants.F_OPPORTUNITY == folderType && file.getEntityId() != null) {
            EdsOpportunity edsOpportunity = opportunityManager.get(file.getEntityId());
            if (edsOpportunity != null) {
                try {
                    solrManager.addOpportunityToIndex(edsOpportunity);
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();
                }
            }
        }


        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(fileId);
        ServerUtils.kpiLog(log, kpiLog, "Delete document");

        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(AttachmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, file, user);
        event.setCustomStringField(file.getName());
        event.setRelationID(file.getEntityId());
        event.setRelationType(ServerUtils.getFolderRelationType(file.getFileType()));
    }

    /**
     * Get Company & Employee Docs Expiry For Document Expiry Widget
     *
     * @param fp
     * @return List of FileResource
     * @sort sorted by docs expiry date
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ListResult<FileResource> getCompanyAndEmployeeDocumentExpiryListNew(ListingFilterParameter fp) {
        ListResult<FileResource> resultList = new ListResult<>(new ArrayList<>(), 0);
        EdsUser user = fileHeaderManager.getUser();
        fp.setDueDate(ServerUtils.getDayStartTime(new Date()));

        //get company docs
        fp.setFolderType(F_COMPANY_DOCUMENTS);
        ListResult<FileResource> companyDocs = getFileResource(fp, user);

        //get employee docs
        fp.setDataType(DASHBOARD_WIDGET_CODE.DOCUMENT_EXPIRY);
        fp.setFolderType(F_EMPLOYEE_PROFILE);
        fp.setUserID(user.getObjectID());
        fp.setEntityID(user.getObjectID());
//        if (!user.getRoleIds().contains(EdsRole.HR)) {
//            fp.setCrmEntityId(user.getObjectID());
//        }
        ListResult<FileResource> employeeDocs = getFileResource(fp, user);

        resultList.getList().addAll(companyDocs.getList());
        resultList.getList().addAll(employeeDocs.getList());
        resultList.setTotal(companyDocs.getTotal() + employeeDocs.getTotal());

        resultList.getList().sort(Comparator.comparing(FileResource::getDaysLeft));

        return resultList;
    }

    private ListResult<FileResource> getFileList(ListingFilterParameter filter) throws ObjectNotFoundException {
        //final EdsUser user = userManager.getUser() == null ? (filter != null && filter.getUserID() != null ? userManager.get(filter.getUserID()) : null) : userManager.getUser();
        EdsUser user = null;
        if (filter.getUserID() != null) {
            user = userManager.get(filter.getUserID());
        }
        user = user != null ? user : userManager.getUser();

        if (filter.isTrashResource()) {
            ArrayList<FileResource> fileRes = getDeletedFiles(filter, user);
            return new ListResult<>(fileRes, fileRes.size());
        } else if (filter.isOtherSharedResource()) {
            ArrayList<FileResource> fileRes = getSharedFiles(filter.getUserID(), user.getObjectID());
            return new ListResult<>(fileRes, fileRes.size());
        } else if (filter.isOtherResource()) {
            ArrayList<FileResource> fileRes = new ArrayList<>();
            return new ListResult<>(fileRes, fileRes.size());
        } else if (filter.isSharedResource()) {
            ArrayList<FileResource> fileRes = getSharedFilesNotInSharedFolders(user);
            return new ListResult<>(fileRes, fileRes.size());
        } else if (filter.isAllFilesResource()) {
            ArrayList<FileResource> sharedFiles = getSharedFilesNotInSharedFolders(user);
            ListResult<FileResource> fileResource = getFileResource(filter, user);
            int total = fileResource.getTotal() + sharedFiles.size();
            sharedFiles.addAll(fileResource.getList());
            return new ListResult<>(sharedFiles, total);
        } else {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.LIST);
            ServerUtils.kpiLog(log, kpiLog, "Get files and folder list");
            return getFileResource(filter, user);
        }
    }

    @Transactional
    @Override
    public void addUserToGroup(Integer groupId, Integer userToAddId) throws ObjectNotFoundException, DuplicateNameException, InsufficientPermissionsException {
        if (groupId == null) {
            throw new ObjectNotFoundException("No group specified");
        }
        if (userToAddId == null) {
            throw new ObjectNotFoundException("No user to add specified");
        }
        final EdsUser user = fileHeaderManager.getUser();
        EdsGroup group = groupManager.get(groupId);
        if (group.getEntryType() == BUILT_IN) {
            throw new InsufficientPermissionsException("You are not the owner of this group");
        }
        if (!(group.getOwner().getTrusteeID().equals(user.getObjectID()) || user.hasRole(roleManager.get(EdsRole.ADMIN)))) {
            throw new InsufficientPermissionsException();
        }
        EdsUser userToAdd = userManager.get(userToAddId);
        EdsTrustee trustee = trusteeManager.getTrustee(userToAdd);
        if (!group.contains(userToAdd)) {
            group.getMembers().add(trustee);
            user.getMembershipGroups().add(group);
            userToAdd.getMembershipGroups().add(group);
        } else {
            throw new DuplicateNameException("User already exists in group");
        }

        groupManager.update(group);
    }

    @Transactional
    @Override
    public void deleteGroup(Integer groupId) throws ObjectNotFoundException, InsufficientPermissionsException {
        // Validate.
        if (groupId == null) {
            throw new ObjectNotFoundException("No group specified");
        }
        // Do the actual work.
        final EdsUser owner = fileHeaderManager.getUser();
        EdsGroup group = groupManager.get(groupId);
        if (group.getEntryType() == BUILT_IN) {
            throw new InsufficientPermissionsException("You are not the owner of this group");
        }
        // Only delete the group if actually owned by the user.
        if (group.getOwner().getTrusteeID().equals(owner.getObjectID()) || owner.hasRole(roleManager.get(EdsRole.ADMIN))) {
            for (EdsTrustee member : group.getMembers()) {
                if (EdsTrusteeType.USER.equals(member.getType().getObjectID())) {
                    userManager.get(member.getTrusteeID()).getMembershipGroups().remove(group);
                }
            }
            group.getMembers().removeAll(group.getMembers());
            folderRbacManager.removeGroupEntries(groupId);
            groupManager.delete(group);
        } else {
            throw new InsufficientPermissionsException("You are not the owner of this group");
        }
    }

    @Transactional
    @Override
    public void removeMemberFromGroup(Integer groupId, Integer memberId) throws ObjectNotFoundException, InsufficientPermissionsException {
        if (groupId == null) {
            throw new ObjectNotFoundException("No group specified");
        }
        if (memberId == null) {
            throw new ObjectNotFoundException("No member specified");
        }
        final EdsUser owner = fileHeaderManager.getUser();
        EdsGroup group = groupManager.get(groupId);
        if (group.getEntryType() == BUILT_IN) {
            throw new InsufficientPermissionsException("You are not the owner of this group");
        }
        if (!(group.getOwner().getTrusteeID().equals(owner.getObjectID()) || owner.hasRole(roleManager.get(EdsRole.ADMIN)))) {
            throw new InsufficientPermissionsException("User is not the owner of the group");
        }
        for (EdsTrustee trustee : group.getMembers()) {
            if (trustee.getTrusteeID().equals(memberId)) {
                userManager.get(memberId).getMembershipGroups().remove(group);
                group.getMembers().remove(trustee);
                return;
            }
        }
        groupManager.update(group);
    }

    @Transactional
    public void deleteFiles(List<Integer> fileIds) throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = fileHeaderManager.getUser();
        //first delete database objects
        for (Integer fileId : fileIds) {
            if (fileId == null) {
                throw new ObjectNotFoundException("No file specified");
            }
            final EdsFileHeader file = fileHeaderManager.get(fileId);
            final EdsFolder parent = file.getFolder();
            if (parent == null) {
                throw new ObjectNotFoundException("The specified file has no parent folder");
            }
            EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
            if (!permission.hasDelete()) {
                throw new InsufficientPermissionsException("User " + user.getFullName() + " cannot delete file " + file.getName());
            }

            for (final EdsFileBody body : file.getBodies()) {
                fileBodyManager.delete(body);
            }
            //then unindex deleted files
            folderRbacManager.removeIndexFileEntries(file.getObjectID());
            parent.removeFile(file);
            fileHeaderManager.delete(file);
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        ServerUtils.kpiLog(log, kpiLog, "Delete documents");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<TeamEmployees> getCompanyEmployeesWithTeams(boolean includeActiveUser) {
        EdsUser user = userManager.getUser();
        ArrayList<TeamEmployees> result = new ArrayList<>();
        List<EdsDepartment> departments = departmentManager.getCompanyDepartments(user.getCompany());

        List<Integer> employeeIDs = new ArrayList<>();
        for (EdsDepartment department : departments) {
            WfmTreeItem team = new WfmTreeItem(department.getObjectID(), department.getName());
            LinkedList<WfmTreeItem> members = new LinkedList<>();

            for (EdsEmployeeDepartment employeeDepartment : department.getMembers()) {
                EdsEmployee employee = employeeDepartment.getEmployee();
                if (employee != null && (!includeActiveUser || EMPLOYEE_STATUS_ACTIVE.equals(employee.getAccountStatus().getCode())) &&
                        !employeeDepartment.getDeleted() && !employeeIDs.contains(employee.getObjectID())) {
                    members.add(new WfmTreeItem(employee.getObjectID(), employee.getName()));
                    employeeIDs.add(employee.getObjectID());
                }
            }

            if (members.size() != 0) {
                members.sort(Comparator.comparing(SelectItem::getName));
                result.add(new TeamEmployees(team, members));
            }
        }

        return result;
    }

    @Transactional
    @Override
    public void addUsersToGroup(Integer groupId, ArrayList<Integer> userIds) throws InsufficientPermissionsException {
        final EdsUser user = fileHeaderManager.getUser();
        EdsGroup group = groupManager.get(groupId);
        if (!(group.getOwner().getTrusteeID().equals(user.getObjectID()) || user.hasRole(roleManager.get(EdsRole.ADMIN)))) {
            throw new InsufficientPermissionsException();
        }
        for (Integer userToAddId : userIds) {
            EdsUser userToAdd = userManager.get(userToAddId);
            EdsTrustee trustee = trusteeManager.getTrustee(userToAdd);
            if (!group.contains(userToAdd)) {
                group.getMembers().add(trustee);
            }
            userToAdd.getMembershipGroups().add(group);
        }
        groupManager.update(group);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public SharedResource getSharedFolder() throws ObjectNotFoundException {
        final EdsUser user = fileHeaderManager.getUser();
        SharedResource sharedResource = new SharedResource();
        sharedResource.setSubFolders(getSharedRootFolders(user.getObjectID()));
        return sharedResource;
    }

    @Transactional
    @Override
    public void updateFolder(FolderResource oldFolder, ArrayList<PermissionHolder> perms) throws InsufficientPermissionsException, ObjectNotFoundException,
            DuplicateNameException {
        if (oldFolder.getObjectId() == null) {
            throw new ObjectNotFoundException("No folder specified");
        }
        EdsFolder folder = folderManager.get(oldFolder.getObjectId());
        final EdsUser user = fileHeaderManager.getUser();
        EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(folder, user);
        if (oldFolder.getName() != null && !permission.hasWrite()) {
            throw new InsufficientPermissionsException("You don't have the necessary permissions");
        }
        if (perms != null && !perms.isEmpty() && !permission.hasModifyACL()) {
            throw new InsufficientPermissionsException("You don't have the necessary permissions");
        }

        EdsFolder parent = folder.getParent();
        if (oldFolder.getName() != null) {
            if (parent != null && !folder.getName().equals(oldFolder.getName()) && folderManager.existsFolder(parent.getObjectID(), oldFolder.getName())) {
                throw new DuplicateNameException("A folder or file with the name '" + oldFolder.getName() + "' already exists at this level");
            }

            // Do the actual modification.
            folder.setName(oldFolder.getName());
            if (folder.getAuditInfo() != null) {
                folder.getAuditInfo().setModificationDate(new Date());
                folder.getAuditInfo().setModifiedBy(user);
            }
        }
        if (perms != null) {
            setFolderPermissions(user, folder, perms, false);
        }

        folderManager.update(folder);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFolder.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(oldFolder.getObjectId());
        ServerUtils.kpiLog(log, kpiLog, "================== Folder Updated ==================");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public TrashResource getTrashedFolder() throws ObjectNotFoundException {
        final EdsUser user = fileHeaderManager.getUser();
        TrashResource trashResource = new TrashResource();
        trashResource.setSubFolders(getDeletedRootFolders(user.getObjectID()));
        return trashResource;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public OthersResource getOthersShared() throws ObjectNotFoundException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        final EdsUser user = fileHeaderManager.getUser();
        OthersResource othersResource = new OthersResource();
//        List<EdsUser> users = getUsersSharingFoldersForUser(owner);
        ArrayList<OtherUserResource> otherUserResourcesList = new ArrayList<>();


        List<FolderResource> list = getSharedRootFolderList(user.getObjectID());
        list.stream().collect(Collectors.groupingBy(FolderResource::getOwner)).forEach((userResource, folders) -> {
            OtherUserResource otherUserResource = new OtherUserResource();
            otherUserResource.setFolders(new ArrayList<>(folders));
            otherUserResource.setUsername(userResource.getFullName());
            otherUserResource.setObjectId(userResource.getObjectId());
            otherUserResourcesList.add(otherUserResource);
        });
        othersResource.setOtherUsers(otherUserResourcesList);
        stopwatch.elapsed(TimeUnit.MILLISECONDS);
        log.info("Load shared with me files s = {}", stopwatch);

        return othersResource;
    }

    private ArrayList<FolderResource> getSharedResource(Integer userId) throws ObjectNotFoundException {
        return getSharedRootFolders(userId, userManager.getUser().getObjectID());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public OtherUserResource getOtherUserResource(Integer userId) throws ObjectNotFoundException {
        EdsUser user = userManager.get(userId);
        EdsUser currUser = userManager.getUser();
        ArrayList<FolderResource> folders = getSharedRootFolders(user.getObjectID(), currUser.getObjectID());
        OtherUserResource otherUserResource = new OtherUserResource();
        otherUserResource.setFolders(folders);
        otherUserResource.setUsername(user.getFullName());
        otherUserResource.setObjectId(user.getObjectID());
        ArrayList<FileResource> fileResourceList = getSharedFiles(user.getObjectID(), currUser.getObjectID());
        otherUserResource.setFiles(fileResourceList);
        return otherUserResource;
    }

    @Transactional
    @Override
    public void emptyTrash() throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = fileHeaderManager.getUser();
        List<EdsFolder> folders = folderManager.getDeletedRootFolders(user.getObjectID());
        final List<EdsFileHeader> files = fileHeaderManager.getDeletedFiles(null, user);
        for (EdsFileHeader filedto : files) {
            deleteFile(filedto.getObjectID());
        }
        List<Integer> folderRbacList = new ArrayList<>();
        for (EdsFolder folder : folders) {
            final EdsFolder parent = folder.getParent();
            if (parent == null) {
                throw new ObjectNotFoundException("Deleting the root folder is not allowed");
            }
            EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(folder, user);
            if (!permission.hasDelete()) {
                throw new InsufficientPermissionsException("User " + user.getObjectID() + " cannot delete folder " + folder.getName() + "(" + folder.getObjectID() + ")");
            }
            removeSubfolderFiles(folder, folderRbacList);
            deleteSubFolders(folder, folderRbacList);
            folderManager.delete(folder);
        }
        folderRbacManager.removeFolderRbacList(folderRbacList);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        ServerUtils.kpiLog(log, kpiLog, "Empty trash");
    }

    @Transactional
    @Override
    public void moveFolderToTrash(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = fileHeaderManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (folderId == null) {
            throw new ObjectNotFoundException("No folder specified");
        }
        EdsFolder folder = folderManager.get(folderId);
        EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(folder, user);
        if (!permission.hasDelete()) {
            throw new InsufficientPermissionsException("You don't have the necessary permissions");
        }
        folder.setDeleted(true);
        folder.getAuditInfo().setModificationDate(new Date());
        folder.getAuditInfo().setModifiedBy(user);
        folderManager.update(folder);
        folderManager.indexFolder(folder, true);
        List<EdsFileHeader> copyFiles = new ArrayList<>(folder.getFiles());
        for (EdsFileHeader file : copyFiles) {
            moveFileToTrash(file.getObjectID());
        }
        for (EdsFolder subFolder : folder.getSubfolders()) {
            moveFolderToTrash(subFolder.getObjectID());
        }
    }

    @Transactional
    @Override
    public void moveFileToTrash(Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = fileHeaderManager.getUser();
        moveFileToTrash(user, fileId);
    }

    private void copyFolderStructure(Integer folderId, Integer destId) throws
            ObjectNotFoundException, DuplicateNameException, InsufficientPermissionsException, QuotaExceededException {
        if (folderId == null) {
            throw new ObjectNotFoundException("No folder specified");
        }
        if (destId == null) {
            throw new ObjectNotFoundException("No destination specified");
        }

        EdsFolder folder = folderManager.get(folderId);
        EdsFolder destination = folderManager.get(destId);
        if (isParent(folder, destination)) {
            throw new DuplicateNameException("The destination folder is a subfolder of the source folder.");
        }
        final EdsUser user = fileHeaderManager.getUser();
        // XXX: quick fix need to copy only visible items to user (Source
        // for bugs)
        EdsDocumentPermission fpermission = folderRbacManager.getFolderPermissionForUser(folder, user);
        if (!(folder.getOwner().getObjectID().equals(user.getObjectID()) || fpermission.hasRead())) {
            return;
        }
        if (folder.isDeleted())//do not copy trashed folder and contents
        {
            return;
        }
        EdsDocumentPermission depermission = folderRbacManager.getFolderPermissionForUser(destination, user);

        if (!(depermission.hasWrite() || fpermission.hasRead())) {
            throw new InsufficientPermissionsException("You don't have the necessary permissions");
        }
        createFolder(user.getObjectID(), destination.getObjectID(), folder.getName());
        EdsFolder createdFolder = folderManager.getFolder(destination.getObjectID(), folder.getName());
        List<EdsFileHeader> files = folder.getFiles();
        if (files != null) {
            for (EdsFileHeader file : files) {
                if (!file.isDeleted()) {
                    copyFile(file.getObjectID(), createdFolder.getObjectID(), file.getName());
                }
            }
        }
        List<EdsFolder> subFolders = folder.getSubfolders();
        if (subFolders != null) {
            for (EdsFolder sub : subFolders) {
                if (!sub.getObjectID().equals(createdFolder.getObjectID())) {
                    copyFolderStructure(sub.getObjectID(), createdFolder.getObjectID());
                }
            }
        }

    }

    private void copyFile(Integer fileId, Integer destId, String destName) throws ObjectNotFoundException, DuplicateNameException, InsufficientPermissionsException, QuotaExceededException {
        final EdsUser user = fileHeaderManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileId == null) {
            throw new ObjectNotFoundException("No file specified");
        }
        if (destId == null) {
            throw new ObjectNotFoundException("No destination specified");
        }
        if (StringUtils.isEmpty(destName)) {
            throw new ObjectNotFoundException("No destination file name specified");
        }

        EdsFileHeader file = fileHeaderManager.get(fileId);
        EdsFolder destination = folderManager.get(destId);
        EdsDocumentPermission fipermission = folderRbacManager.getFilePermissionForUser(file, user);
        EdsDocumentPermission depermission = folderRbacManager.getFolderPermissionForUser(destination, user);
        if (!(fipermission.hasRead() || depermission.hasWrite())) {
            throw new InsufficientPermissionsException("You don't have the necessary permissions");
        }
        EdsFileBody oldestBody = file.getBodies().get(0);
        assert oldestBody != null;
        DocumentItem documentItem = new DocumentItem();
        documentItem.setContentType(oldestBody.getContentType());
        documentItem.setDescription(oldestBody.getDescription());
        documentItem.setFolderId(destination.getObjectID());
        documentItem.setName(oldestBody.getOriginalName());

        InputStream inputStream = uploadManager.getInputStream(oldestBody);
        if (inputStream != null) {
            CopyInputStream cis = new CopyInputStream(inputStream);
            InputStream input1 = cis.getCopy();
            documentItem.setInputStream(input1);
            createFile(documentItem, EdsContextParams.getUploadType(), file.getFileType(), file.getEntityId());
        }
    }

    @Transactional
    @Override
    public void copyFolder(Integer folderId, Integer destId) throws ObjectNotFoundException, DuplicateNameException, InsufficientPermissionsException, QuotaExceededException {
        copyFolderStructure(folderId, destId);

    }

    private boolean isParent(EdsFolder folder, EdsFolder destination) {
        return destination != null && (folder.equals(destination.getParent()) || isParent(folder, destination.getParent()));
    }

    @Transactional
    @Override
    public void moveFolder(Integer folderId, Integer destId) throws InsufficientPermissionsException, DuplicateNameException {
        EdsFolder source = folderManager.get(folderId);
        EdsFolder destination = folderManager.get(destId);
        EdsUser user = userManager.getUser();
        if (source.equals(destination) || isParent(source, destination)) {
            throw new DuplicateNameException("The destination folder is a subfolder of the source folder.");
        }
        if (folderManager.existsFolder(destId, source.getName())) {
            throw new DuplicateNameException("A folder with the name '" +
                    source.getName() + "' already exists at this level.");
        }
        EdsUser sourceOwner = source.getOwner();
        EdsUser destinationOwner = destination.getOwner();
        // Do not move trashed folders and contents.
        if (source.isDeleted()) {
            return;
        }
        // Check permissions.
        EdsDocumentPermission depermission = folderRbacManager.getFolderPermissionForUser(destination, user);
        EdsDocumentPermission sopermission = folderRbacManager.getFolderPermissionForUser(source, user);
        if (!(depermission.hasWrite() || sopermission.hasRead() || sopermission.hasWrite())) {
            throw new InsufficientPermissionsException("You don't have the necessary permissions");
        }
        // Use the same timestamp for all subsequent modifications to make
        // changes appear simultaneous.
        Date now = new Date();

        // Perform the move.
        EdsFolder oldParent = source.getParent();
//        folderRbacManager.removeFolderEntries(source.getObjectID());
        oldParent.removeSubfolder(source);
        destination.addSubfolder(source);

        // If source and destination are not in the same user's namespace,
        // change owners and check quota.
        if (!sourceOwner.equals(destinationOwner)) {
            changeOwner(source, destinationOwner, user, now);
        }
        // Mark the former parent and destination trees upwards as modified.
    }

    /**
     * Recursively change the owner of the specified folder and all of its
     * contents to the specified owner. Also mark them all as modified with the
     * specified modifier and modificationDate.
     */
    @Transactional
    public void changeOwner(EdsFolder folder, EdsUser owner, EdsUser modifier, Date modificationDate) {
        folderRbacManager.removeFolderEntries(folder.getObjectID());
        folder.setOwner(owner);
        folder.getAuditInfo().setModificationDate(modificationDate);
        folder.getAuditInfo().setModifiedBy(modifier);
        folderManager.indexFolder(folder, true);
        for (EdsFileHeader file : folder.getFiles()) {
            folderRbacManager.removeFileEntries(file.getObjectID());
            file.setOwner(owner);
            file.getAuditInfo().setModificationDate(modificationDate);
            file.getAuditInfo().setModifiedBy(modifier);
            folderRbacManager.indexFile(file);

        }
        for (EdsFolder sub : folder.getSubfolders()) {
            changeOwner(sub, owner, modifier, modificationDate);
        }
    }

    @Transactional
    @Override
    public void copyFile(Integer fileId, Integer folderId, Integer entityID) throws QuotaExceededException, ObjectNotFoundException, InsufficientPermissionsException, DuplicateNameException {
        EdsUser user = userManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileId == null) {
            throw new ObjectNotFoundException("No file specified");
        }
        if (folderId == null) {
            throw new ObjectNotFoundException("No destination specified");
        }

        EdsFileHeader file = fileHeaderManager.get(fileId);
        EdsFolder destination = folderManager.get(folderId);
        EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
        if (!(permission.hasRead() || permission.hasWrite())) {
            throw new InsufficientPermissionsException("You don't have the necessary permissions");
        }
        boolean versioned = file.isVersioned();
        int versionsNumber = file.getBodies().size();
        if (file.getAuditInfo() != null) {
            file.getAuditInfo().setModificationDate(new Date());
            file.getAuditInfo().setModifiedBy(user);
        }
        EdsFileBody oldestBody = file.getBodies().get(0);
        assert oldestBody != null;
        DocumentItem fileBody = new DocumentItem();
        InputStream inputStream = uploadManager.getInputStream(oldestBody);
        if (inputStream != null) {
            CopyInputStream cis = new CopyInputStream(inputStream);
            InputStream input1 = cis.getCopy();
            fileBody.setInputStream(input1);
            fileBody.setContentType(oldestBody.getContentType());
            fileBody.setName(oldestBody.getOriginalName());
            fileBody.setFolderId(destination.getObjectID());
            fileBody.setDescription(oldestBody.getDescription());
            fileBody.setObjectId(fileId);
            if (entityID != null) {
                createFile(fileBody, EdsContextParams.getUploadType(), file.getFileType(), entityID);
            } else {
                createFile(fileBody, EdsContextParams.getUploadType(), file.getFileType(), file.getEntityId());
            }
            EdsFileHeader copiedFile = fileHeaderManager.getFile(destination.getObjectID(), oldestBody.getOriginalName());

            if (copiedFile != null) {
                copiedFile.setVersioned(versioned);
            }
        }
    }

    @Transactional
    @Override
    public void moveFile(Integer fileId, Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException {
        EdsUser user = userManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileId == null) {
            throw new ObjectNotFoundException("No file specified");
        }
        if (folderId == null) {
            throw new ObjectNotFoundException("No destination specified");
        }

        EdsFileHeader file = fileHeaderManager.get(fileId);
        EdsFolder source = file.getFolder();
        EdsFolder destination = folderManager.get(folderId);
        EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
        if (!permission.hasDelete() && !permission.hasWrite()) {
            throw new InsufficientPermissionsException("User " + user.getFullName() + " cannot move file " + file.getName() /*+ "(" + file.getObjectID() + ")"*/);
        }

        // if the destination folder belongs to another user:
        if (!file.getOwner().equals(destination.getOwner())) {
            EdsUser newOwner = destination.getOwner();
            // (b) if quota OK, change the owner of the file
            file.setOwner(newOwner);
        }
        // move the file to the destination folder
        file.setFolder(destination);
        if (file.getAuditInfo() != null) {
            file.getAuditInfo().setModificationDate(new Date());
            file.getAuditInfo().setModifiedBy(user);
        }
        //add source folder to solr
        try {
//            solrManager.addFileToIndex(file);
            folderSolrComponent.index(file);
        } catch (InterruptedException e) {
            baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_ADD, file, user);
        }

    }

    private FolderResource createFolder(Integer userId, Integer parentId, String name) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException {
        // Validate.
        if (userId == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (StringUtils.isEmpty(name)) {
            throw new ObjectNotFoundException("New folder name is empty");
        }
        if (parentId == null) {
            throw new ObjectNotFoundException("No parent specified");
        }
        if (folderManager.existsFolder(parentId, name)) {
            throw new DuplicateNameException("A folder with the name '" +
                    name + "' already exists at this level");
        }

        EdsUser creator = userManager.get(userId);
        EdsFolder parent = folderManager.get(parentId);
        EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(parent, creator);
        if (!permission.hasWrite()) {
            throw new InsufficientPermissionsException("You don't have the permissions" +
                    " to write to this folder");
        }

        // Do the actual work.
        return createFolder(name, parent, creator, EdsFolder.CUSTOM, EdsFolder.F_DEFAULT, null).getDTO();
    }

    private void moveFileToTrash(EdsUser user, Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException {
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileId == null) {
            throw new ObjectNotFoundException("No file specified");
        }

        // Do the actual work.
        EdsFileHeader file = fileHeaderManager.get(fileId);
        if (file != null) {
            if (!file.isDeleted()) {
                EdsFolder parent = file.getFolder();
                if (parent == null) {
                    throw new ObjectNotFoundException("The specified file has no parent folder");
                }
                EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
                if (!permission.hasDelete()) {
                    throw new InsufficientPermissionsException("User " + user.getFullName() + " cannot delete file " + file.getName() /*+ "(" + file.getObjectID() + ")"*/);
                }

                file.setDeleted(true);
                file.getAuditInfo().setModificationDate(new Date());
                file.getAuditInfo().setModifiedBy(user);
                fileHeaderManager.update(file);
                folderRbacManager.indexFile(file);
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.DELETE);
                kpiLog.setEntityId(fileId);
                ServerUtils.kpiLog(log, kpiLog, "Move file to trash");
            } else {
                deleteFile(fileId);
            }
        }
    }

    public void indexFiles(ArrayList<Integer> fileIds) {
        StringBuilder ids = new StringBuilder();
        if (fileIds != null) {
            for (Integer id : fileIds) {
                ids.append(id).append(",");
            }
        }
        if (!ids.isEmpty()) {
            List<EdsFileHeader> fileIdsIn = fileHeaderManager.getFileIdsIn(ids.deleteCharAt(ids.length() - 1).toString());
            folderRbacManager.indexFiles(fileIdsIn);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<EdsUser> getUsersSharingFoldersForUser(EdsUser user) throws ObjectNotFoundException {
        List<EdsUser> users = folderManager.getUsersSharingFoldersForUser(user);
        List<EdsUser> usersFiles = fileHeaderManager.getUsersSharingFilesForUser(user);
        List<EdsUser> res = new ArrayList<>();
        List<Integer> resId = new ArrayList<>();
        for (EdsUser u : users) {
            res.add(u);
            resId.add(u.getObjectID());
        }
        for (EdsUser fu : usersFiles) {
            if (!resId.contains(fu.getObjectID())) {
                res.add(fu);
                resId.add(fu.getObjectID());
            }
        }
        return res;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FileResource> getSharedFiles(Integer ownerId, Integer callingUserId) throws ObjectNotFoundException {
        if (ownerId == null) {
            throw new ObjectNotFoundException("No owner specified");
        }
        if (callingUserId == null) {
            throw new ObjectNotFoundException("No calling user specified");
        }
        List<EdsFileHeader> files = fileHeaderManager.getSharedFiles(userManager.get(ownerId), callingUserId);
        List<EdsFileHeader> retv = new ArrayList<>();
        for (EdsFileHeader f : files) {
            EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(f, userManager.get(ownerId));
            if (permission.hasRead()) {
                retv.add(f);
            }
        }

        ArrayList<FileResource> result = new ArrayList<>();
        for (EdsFileHeader f : retv) {
            FileResource fDTO = f.getDTO();
            fDTO.setPermissions(getFilePermissions(fDTO.getObjectId()));
            fDTO.setPermission(folderRbacManager.getFilePermissionForUser(f, fileHeaderManager.getUser()).getDTO());
            if (GOOGLE.equals(fDTO.getUploadType())) {
                EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(f.getCurrentBody());
                if (googleDocumentsSettings != null) {
                    fDTO.setGoogleDownloadLink(googleDocumentsSettings.getDocumentLink());
                }
            } else if (OFFICE_365.equals(fDTO.getUploadType()) || OFFICE_365_SHARE_POINT.equals(fDTO.getUploadType())) {
                EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(f.getCurrentBody());
                if (googleDocumentsSettings != null) {
                    fDTO.setDocumentID(googleDocumentsSettings.getDocumentID());
                    fDTO.setDocumentOpenID(googleDocumentsSettings.getDocumentOpenID());
                    fDTO.setOfficeDownloadLink(googleDocumentsSettings.getDocumentLink());
                }
            } else {
                fDTO.setAmazonLink(commonServiceLocal.getFileUrl(fDTO.getBodyId()));
            }
            result.add(fDTO);
        }
        return result;
    }

    private ArrayList<FileResource> getDeletedFiles(ListingFilterParameter filterParameter, EdsUser user) {
        // Do the actual work.
        ArrayList<FileResource> result = new ArrayList<>();
        List<EdsFileHeader> files = fileHeaderManager.getDeletedFiles(filterParameter, user);
        for (EdsFileHeader f : files) {
            FileResource fDTO = f.getDTO();
            fDTO.setPermissions(getFilePermissions(fDTO.getObjectId()));
            fDTO.setPermission(folderRbacManager.getFilePermissionForUser(f, fileHeaderManager.getUser()).getDTO());
            if (GOOGLE.equals(fDTO.getUploadType())) {
                EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(f.getCurrentBody());
                if (googleDocumentsSettings != null) {
                    fDTO.setGoogleDownloadLink(googleDocumentsSettings.getDocumentLink());
                }
            } else if (OFFICE_365.equals(fDTO.getUploadType()) || OFFICE_365_SHARE_POINT.equals(fDTO.getUploadType())) {
                EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(f.getCurrentBody());
                if (googleDocumentsSettings != null) {
                    fDTO.setDocumentID(googleDocumentsSettings.getDocumentID());
                    fDTO.setDocumentOpenID(googleDocumentsSettings.getDocumentOpenID());
                    fDTO.setOfficeDownloadLink(googleDocumentsSettings.getDocumentLink());
                }
            } else {
                fDTO.setAmazonLink(commonServiceLocal.getFileUrl(fDTO.getBodyId()));
            }
            result.add(fDTO);
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FolderResource> getDeletedRootFolders(Integer userId) throws ObjectNotFoundException {
        List<EdsFolder> folders = folderManager.getDeletedRootFolders(userId);
        ArrayList<FolderResource> result = new ArrayList<>();
        for (EdsFolder folder : folders) {
            FolderResource dto = folder.getDTO();
            //dto.setPermissions(getFolderPermissions(folder.getObjectID()));
            dto.setPermission(folderRbacManager.getFolderEntryForUser2(folder, userManager.getUser()).getDTO());
            dto.setSubfolders(getSharedSubfolders(userId, folder.getObjectID()));
            result.add(dto);
        }
        return result;
    }

    // ─── Constants for deletable relationships ────────────────────────────────────
    private static final Set<String> DELETABLE_FOLDER_RELATIONSHIPS = Set.of(
            EdsRelationship.DOC_CREATOR,
            EdsRelationship.DOC_READER,
            EdsRelationship.DOC_VIEWER
    );

    private static final Set<String> DELETABLE_FILE_RELATIONSHIPS = Set.of(
            EdsRelationship.DOC_CREATOR,
            EdsRelationship.DOC_OWNER,
            EdsRelationship.DOC_READER,
            EdsRelationship.DOC_VIEWER
    );

    /**
     * Set the provided permissions as the new permissions of the specified
     * folder.
     *
     * @param user
     * @param folder
     * @param permissions
     * @throws com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException
     * @throws com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException
     */
    @Transactional
    public void setFolderPermissions(
            EdsUser user,
            EdsFolder folder,
            List<PermissionHolder> permissions,
            boolean isSubFolder
    ) throws ObjectNotFoundException, InsufficientPermissionsException {

        log.info("Set Folder permissions for folderId={}", folder.getObjectID());

        if (folder.getParent() != null) {
            createRbacForPrentFolder(folder.getParent(), permissions, isSubFolder);
        }

        Map<String, EdsRelationship> relationshipCache = buildRelationshipCache(permissions);

        applyFolderPermissionsRecursive(user, folder, permissions, isSubFolder, relationshipCache);
    }

    private void applyFolderPermissionsRecursive(
            EdsUser user,
            EdsFolder folder,
            List<PermissionHolder> permissions,
            boolean isSubFolder,
            Map<String, EdsRelationship> relationshipCache
    ) throws ObjectNotFoundException {

        List<EdsFolderRbac> oldRbacList = folderRbacManager.getFolderRbacEntries(folder.getObjectID());

        Map<Boolean, List<EdsFolderRbac>> partitioned = partitionRbacForFolder(oldRbacList, isSubFolder);
        List<EdsFolderRbac> toDelete = partitioned.get(Boolean.TRUE);
        List<EdsFolderRbac> newRbacList = partitioned.get(Boolean.FALSE);

        if (!toDelete.isEmpty()) {
            List<Integer> list = toDelete.stream().map(EdsFolderRbac::getObjectID).toList();
            folderRbacManager.deleteAllByIds(list);
        }

        Map<String, EdsFolderRbac> rbacLookup = buildRbacLookup(newRbacList);

        applyPermissions(folder, permissions, isSubFolder, newRbacList, rbacLookup, relationshipCache);

        if (isSubFolder) {
            folderManager.update(folder);
        }
        List<EdsFileHeader> files = folder.getFiles();
        List<EdsFolder> subfolders = folder.getSubfolders();
        int chunkSize = 50;

        if (!files.isEmpty()) {
            folderRbacManager.bulkDeleteFileRbacEntriesForFolder(folder.getObjectID(), isSubFolder);

            for (PermissionHolder dto : permissions) {
                if (!dto.isCanChange()) continue;
                if (!dto.isRead() && !dto.isWrite() && !dto.isDelete() && !dto.isModifyACL()) continue;

                EdsDocumentPermission permission = new EdsDocumentPermission();
                permission.setRead(dto.isRead());
                permission.setWrite(dto.isWrite());
                permission.setDelete(dto.isDelete());
                permission.setModifyACL(dto.isModifyACL());
                documentPermissionManager.create(permission);

                EdsRelationship rel = resolveRelationship(dto, relationshipCache);
                int entryType = resolveEntryType(dto, isSubFolder);
                boolean ownerNeedsFullPerms = !dto.isRead() || !dto.isWrite() || !dto.isDelete() || !dto.isModifyACL();

                if (dto.getGroup() != null) {
                    folderRbacManager.bulkInsertFileRbacEntriesForFolder(
                            folder.getObjectID(), null, dto.getGroup().getGroupID(),
                            permission.getObjectID(), rel.getCode(), rel.getRank(), entryType,
                            EdsTrusteeType.GROUP, false);
                } else if (dto.getUser() != null) {
                    folderRbacManager.bulkInsertFileRbacEntriesForFolder(
                            folder.getObjectID(), dto.getUser().getObjectId(), null,
                            permission.getObjectID(), rel.getCode(), rel.getRank(), entryType,
                            EdsTrusteeType.USER, ownerNeedsFullPerms);
                }
            }

            Date now = new Date();
            List<List<EdsFileHeader>> chunks = new ArrayList<>();
            for (int i = 0; i < files.size(); i += chunkSize) {
                chunks.add(new ArrayList<>(files.subList(i, Math.min(i + chunkSize, files.size()))));
            }
            for (List<EdsFileHeader> chunk : chunks) {
                for (EdsFileHeader file : chunk) {
                    file.getAuditInfo().setModificationDate(now);
                    file.getAuditInfo().setModifiedBy(user);
                }
                try {
                    folderSolrComponent.indexConcurrently(chunk);
                } catch (InterruptedException e) {
                    log.error("Solr indexing interrupted for files with size={}: {}", chunk.size(), e.getMessage());
                }
            }
        }

        for (EdsFolder sub : folder.getSubfolders()) {
            applyFolderPermissionsRecursive(user, sub, permissions, true, relationshipCache);
        }
    }

    private Map<Boolean, List<EdsFolderRbac>> partitionRbacForFolder(
            List<EdsFolderRbac> rbacList,
            boolean isSubFolder
    ) {
        List<EdsFolderRbac> toDelete = new ArrayList<>();
        List<EdsFolderRbac> toKeep = new ArrayList<>();

        for (EdsFolderRbac rbac : rbacList) {
            boolean isEligibleForDeletion =
                    rbac.getEntryType() != EdsFolderRbac.CUSTOM
                            || (!isSubFolder && rbac.getEntryType() == EdsFolderRbac.CUSTOM);

            if (isEligibleForDeletion
                    && DELETABLE_FOLDER_RELATIONSHIPS.contains(rbac.getRelationship())) {
                toDelete.add(rbac);
            } else {
                toKeep.add(rbac);
            }
        }

        Map<Boolean, List<EdsFolderRbac>> result = new HashMap<>();
        result.put(Boolean.TRUE, toDelete);
        result.put(Boolean.FALSE, toKeep);
        return result;
    }

    private Map<String, EdsFolderRbac> buildRbacLookup(List<EdsFolderRbac> rbacList) {
        Map<String, EdsFolderRbac> map = new HashMap<>(rbacList.size() * 2);
        for (EdsFolderRbac rbac : rbacList) {
            if (EdsTrusteeType.USER.equals(rbac.getTrusteeType()) && rbac.getUser() != null) {
                map.put("USER:" + rbac.getUser().getObjectID(), rbac);
            } else if (EdsTrusteeType.GROUP.equals(rbac.getTrusteeType()) && rbac.getGroup() != null) {
                map.put("GROUP:" + rbac.getGroup().getObjectID(), rbac);
            }
        }
        return map;
    }

    private EdsFolderRbac lookupRbac(Map<String, EdsFolderRbac> lookup, PermissionHolder dto) {
        if (dto.getGroup() != null) {
            return lookup.get("GROUP:" + dto.getGroup().getGroupID());
        } else if (dto.getUser() != null) {
            return lookup.get("USER:" + dto.getUser().getObjectId());
        }
        return null;
    }

    private Map<String, EdsRelationship> buildRelationshipCache(List<PermissionHolder> permissions) {
        Set<String> codes = new HashSet<>();
        codes.add(EdsRelationship.DOC_VIEWER);
        for (PermissionHolder dto : permissions) {
            if (dto.getRelationship() != null && !dto.getRelationship().isEmpty()) {
                codes.add(dto.getRelationship());
            }
        }
        Map<String, EdsRelationship> cache = new HashMap<>();
        for (String code : codes) {
            cache.put(code, relationshipManager.getRelationship(code));
        }
        return cache;
    }

    private EdsRelationship resolveRelationship(
            PermissionHolder dto,
            Map<String, EdsRelationship> cache
    ) {
        String code = (dto.getRelationship() != null && !dto.getRelationship().isEmpty())
                ? dto.getRelationship()
                : EdsRelationship.DOC_VIEWER;
        return cache.get(code);
    }

    private void applyPermissions(
            EdsFolder folder,
            List<PermissionHolder> permissions,
            boolean isSubFolder,
            List<EdsFolderRbac> newRbacList,
            Map<String, EdsFolderRbac> rbacLookup,
            Map<String, EdsRelationship> relationshipCache
    ) throws ObjectNotFoundException {

        for (PermissionHolder dto : permissions) {
            if (!dto.isCanChange()) continue;

            boolean isOwner = dto.getUser() != null
                    && dto.getUser().getObjectId().equals(folder.getOwner().getObjectID());

            if (isOwner && (!dto.isRead() || !dto.isWrite() || !dto.isModifyACL())) continue;
            if (!dto.isRead() && !dto.isWrite() && !dto.isDelete() && !dto.isModifyACL()) continue;

            EdsFolderRbac existing = lookupRbac(rbacLookup, dto);

            if (existing != null) {
                EdsDocumentPermission dp = existing.getDocumentPermission();
                if (dp != null && permissionChanged(dp, dto)) {
                    existing.setDocumentPermission(getPermissionExistFolderRbac(dto));
                    folderRbacManager.update(existing);
                }
                continue;
            }

            EdsDocumentPermission permission = getPermission(dto);
            EdsRelationship relationship = resolveRelationship(dto, relationshipCache);
            int entryType = resolveEntryType(dto, isSubFolder);

            if (dto.getGroup() != null) {
                folderRbacManager.createCustomFolderGroupRelationEntry(
                        folder, groupManager.get(dto.getGroup().getGroupID()),
                        permission, relationship, entryType
                );
            } else if (dto.getUser() != null) {
                folderRbacManager.createCustomFolderUserRelationEntry(
                        folder, userManager.get(dto.getUser().getObjectId()),
                        permission, relationship, entryType
                );
            }
        }
    }

    private boolean permissionChanged(EdsDocumentPermission dp, PermissionHolder dto) {
        return dp.hasRead() != dto.isRead()
                || dp.hasDelete() != dto.isDelete()
                || dp.hasWrite() != dto.isWrite()
                || dp.hasModifyACL() != dto.isModifyACL();
    }

    private int resolveEntryType(PermissionHolder dto, boolean isSubFolder) {
        if (isSubFolder || EdsRelationship.DOC_OWNER.equals(dto.getRelationship())) {
            return EdsFolderRbac.INHERITED;
        }
        return EdsFolderRbac.CUSTOM;
    }

    @Transactional
    public void setFolderPermissionsToFile(
            EdsFileHeader file,
            List<PermissionHolder> permissions,
            boolean isSubFolder,
            EdsUser user,
            Map<String, EdsRelationship> relationshipCache
    ) throws ObjectNotFoundException {

        setFilePermissions(file, permissions, isSubFolder, relationshipCache);

        Date now = new Date();
        file.getAuditInfo().setModificationDate(now);
        file.getAuditInfo().setModifiedBy(user);

        try {
            folderSolrComponent.index(file);
        } catch (InterruptedException e) {
            log.error("Solr indexing interrupted for fileId={}: {}", file.getObjectID(), e.getMessage());
            baseEventPostProcessor.registerEvent(
                    FileCustomEventListenerImpl.TYPE,
                    FileCustomEventListenerImpl.EVENT_ADD,
                    file, user
            );
        }
    }

    @Transactional
    public void setFilePermissions(
            EdsFileHeader file,
            List<PermissionHolder> permissions,
            boolean isSubFolder,
            Map<String, EdsRelationship> relationshipCache
    ) throws ObjectNotFoundException {

        if (permissions == null || permissions.isEmpty()) return;

        List<EdsFolderRbac> existing = folderRbacManager.getFileRbacEntries(file.getObjectID());

        Map<Boolean, List<EdsFolderRbac>> partitioned = partitionRbacForFile(existing, isSubFolder);
        List<EdsFolderRbac> toDelete = partitioned.get(Boolean.TRUE);
        List<EdsFolderRbac> remainList = partitioned.get(Boolean.FALSE);

        if (!toDelete.isEmpty()) {
            List<Integer> list = toDelete.stream().map(EdsFolderRbac::getObjectID).toList();
            folderRbacManager.deleteAllByIds(list);
        }

        Map<String, EdsFolderRbac> rbacLookup = buildRbacLookup(remainList);

        for (PermissionHolder dto : permissions) {
            if (!dto.isCanChange()) continue;

            boolean isOwner = dto.getUser() != null
                    && dto.getUser().getObjectId().equals(file.getOwner().getObjectID());
            if (isOwner && (!dto.isRead() || !dto.isWrite() || !dto.isDelete() || !dto.isModifyACL())) continue;
            if (!dto.isRead() && !dto.isWrite() && !dto.isDelete() && !dto.isModifyACL()) continue;

            EdsFolderRbac fRbac = lookupRbac(rbacLookup, dto);
            if (fRbac != null) {
                fRbac.setDocumentPermission(
                        fRbac.getDocumentPermission().mergePermission(getPermission(dto))
                );
                folderRbacManager.update(fRbac);
                continue;
            }

            EdsDocumentPermission permission = getPermission(dto);
            EdsRelationship relationship = resolveRelationship(dto, relationshipCache);
            int entryType = resolveEntryType(dto, isSubFolder);

            if (dto.getGroup() != null) {
                folderRbacManager.createCustomFileGroupRelationEntry(
                        file, groupManager.get(dto.getGroup().getGroupID()),
                        permission, relationship, entryType
                );
            } else if (dto.getUser() != null) {
                folderRbacManager.createCustomFileUserRelationEntry(
                        file, userManager.get(dto.getUser().getObjectId()),
                        permission, relationship, entryType
                );
            }
        }
    }

    private Map<Boolean, List<EdsFolderRbac>> partitionRbacForFile(
            List<EdsFolderRbac> rbacList,
            boolean isSubFolder
    ) {
        List<EdsFolderRbac> toDelete = new ArrayList<>();
        List<EdsFolderRbac> toKeep = new ArrayList<>();

        for (EdsFolderRbac rbac : rbacList) {
            boolean eligible = rbac.getEntryType() != EdsFolderRbac.CUSTOM || (!isSubFolder && rbac.getEntryType() == EdsFolderRbac.CUSTOM);

            if (eligible && DELETABLE_FILE_RELATIONSHIPS.contains(rbac.getRelationship())) {
                toDelete.add(rbac);
            } else {
                toKeep.add(rbac);
            }
        }

        Map<Boolean, List<EdsFolderRbac>> result = new HashMap<>();
        result.put(Boolean.TRUE, toDelete);
        result.put(Boolean.FALSE, toKeep);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ListResult<FileResource> listFilesAndFolders(ListingFilterParameter fp) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        final EdsUser user = fileHeaderManager.getUser();
        KpiLog kpiLog = new KpiLog();
        kpiLog.setUsername(user.getFullName());

        Integer totalCount = 0;
        ArrayList<FileResource> filesFolderList = new ArrayList<>();
        ArrayList<FileResource> subList = new ArrayList<>();
        EdsFolder selectedFolder = null;
        if (fp.getFolderId() != null) {
            selectedFolder = folderManager.get(fp.getFolderId());
        }
        if (selectedFolder != null && (selectedFolder.getFolderType() == EdsFolder.F_BACKUPS_ROOT || selectedFolder.getFolderType() == EdsFolder.F_XML_BACKUPS_ROOT)) {

            subList = uploadManager.getCompanyBackupFiles(selectedFolder, fp);
            totalCount = fp.getStatusID();
        } else {
            if (!fp.isSystemSubFolder() && fp.getFolderId() != null && fp.getFolderId() > 0) {
                filesFolderList = getSubFoldersAsFiles(fp);
                totalCount = filesFolderList.size();
            } else if (!fp.isSystemSubFolder() && fp.getFolderId() != null && fp.getFolderId() == 0) {
                filesFolderList = getSubFoldersAsFiles(fp);
                totalCount = filesFolderList.size();
            } else if (fp.isTrashResource()) {
                try {
                    filesFolderList = getFilesFromFolders(fp, folderManager.getDeletedRootFolders(user.getObjectID()), user, null, true);
                    totalCount = filesFolderList.size();
                } catch (ObjectNotFoundException e) {
                    log.error("", e);
                }
            } else if (fp.isSharedResource()) {
                filesFolderList = getFilesFromFolders(fp, folderManager.getSharedRootFolders(user.getObjectID()), user, null, false);
                totalCount = filesFolderList.size();
            } else if (fp.isOtherResource()) {
                try {
                    final EdsUser edsUser = fileHeaderManager.getUser();
                    List<FolderResource> sharedFolders = getSharedRootFolderList(edsUser.getObjectID());
                    filesFolderList.addAll(sharedFolders.stream().map(folderResource -> {
                        FileResource f = new FileResource();
                        f.setFolderResource(folderResource);
                        f.setObjectId(folderResource.getObjectId());
                        f.setName(folderResource.getName());
                        f.setOwner(folderResource.getOwner());
                        f.setModificationDate(folderResource.getModificationDate());
                        f.setCreatedBy(folderResource.getCreatedBy());
                        f.setCreationDate(folderResource.getCreationDate());
                        f.setFolder(true);
                        f.setPath(folderResource.getPath());
                        f.setPermission(folderResource.getPermission());
                        Optional.ofNullable(folderResource.getOwner()).ifPresent(owner -> f.setName(owner.getName()));
                        f.setName(folderResource.getOwner() != null ? folderResource.getOwner().getName() : null);
                        return f;
                    }).toList());
                    filesFolderList.sort((f1, f2) -> fp.getSortDir() != null && Constants.DESC == fp.getSortDir().intValue() ? f2.getFileName().compareTo(f1.getFileName()) : f1.getFileName().compareTo(f2.getFileName()));
                    totalCount = filesFolderList.size();
                } catch (ObjectNotFoundException e) {
                    log.error("", e);
                }
            } else if (fp.isOtherSharedResource()) {
                EdsUser cUser = userManager.get(fp.getUserID());
                filesFolderList = getFilesFromFolders(fp, folderManager.getSharedRootFolders(cUser.getObjectID(), user.getObjectID()), user, null, false);
                totalCount = filesFolderList.size();
            }
            try {
                if (StringUtils.isNotBlank(fp.getSearchKey()) && fp.isFromMobile()) {
                    filesFolderList = (ArrayList<FileResource>) filesFolderList.stream()
                            .filter(item -> item.getFileName().toLowerCase().contains(fp.getSearchKey().toLowerCase()))
                            .toList();
                    totalCount = filesFolderList.size();
                }

                if (totalCount > fp.getStart()) {
                    int limit = totalCount - fp.getStart() > fp.getLimit() ? fp.getLimit() : totalCount - fp.getStart();
                    subList = new ArrayList<>(filesFolderList.subList(fp.getStart(), fp.getStart() + limit));
                } else {
                    fp.setStart(fp.getStart() - totalCount);
                }
                int limit = fp.getLimit();
                fp.setLimit(subList.size() < limit ? (limit - subList.size()) : limit);

                //Get Files List
                ListResult<FileResource> filesList = listFile(fp);
                totalCount += filesList.getTotal();
                if (subList.size() < limit) {
                    subList.addAll(filesList.getList());
                }
            } catch (ObjectNotFoundException ex) {
                log.error("", ex);
            }
        }
        stopwatch.elapsed(TimeUnit.MILLISECONDS);
        log.info("Load files {}", stopwatch);
        return new ListResult<>(subList, totalCount);
    }

    private void collectRback(boolean isSubFolder, List<EdsFolderRbac> oldRbacList, List<EdsFolderRbac> newRbacList) {
        for (EdsFolderRbac rbac : oldRbacList) {
            boolean deleted = false;
            if (rbac.getEntryType() != EdsFolderRbac.CUSTOM || (!isSubFolder && rbac.getEntryType() == EdsFolderRbac.CUSTOM)) {
                if (EdsRelationship.DOC_ADMINISTRATOR.equals(rbac.getRelationship())) {

                } else if (EdsRelationship.DOC_DIRECTOR.equals(rbac.getRelationship())) {

                } else if (EdsRelationship.DOC_CREATOR.equals(rbac.getRelationship())) {
                    folderRbacManager.delete(rbac);
                    deleted = true;
                }/* else if (EdsRelationship.DOC_OWNER.equals(rbac.getRelationship())) {
                }*/ else if (EdsRelationship.DOC_READER.equals(rbac.getRelationship())) {
                    folderRbacManager.delete(rbac);
                    deleted = true;
                } else if (EdsRelationship.DOC_VIEWER.equals(rbac.getRelationship())) {
                    folderRbacManager.delete(rbac);
                    deleted = true;
                }
            }
            if (!deleted) {
                newRbacList.add(rbac);
            }
        }
    }

    private void createRback(EdsFolder folder, List<PermissionHolder> permissions, boolean isSubFolder, List<EdsFolderRbac> newRbacList) {
        for (PermissionHolder dto : permissions) {
            if (dto.isCanChange()) {
                if ((dto.getUser() != null && dto.getUser().getObjectId().equals(folder.getOwner().getObjectID()) && (!dto.isRead() || !dto.isWrite() || !dto.isModifyACL())) ||
                        (!dto.isRead() && !dto.isWrite() && !dto.isDelete() && !dto.isModifyACL())) {
                    continue;
                }
                EdsFolderRbac fRbac = null;
                if (dto.getGroup() != null) {
                    fRbac = getFolderOrFileRbacForUser(newRbacList, dto.getGroup().getGroupID(), false);
                } else if (dto.getUser() != null) {
                    fRbac = getFolderOrFileRbacForUser(newRbacList, dto.getUser().getObjectId(), true);
                }
                if (fRbac != null) {
                    EdsDocumentPermission documentPermission = fRbac.getDocumentPermission();
                    if (documentPermission != null &&
                            (documentPermission.hasRead() != dto.isRead() ||
                                    documentPermission.hasDelete() != dto.isDelete() ||
                                    documentPermission.hasWrite() != dto.isWrite() ||
                                    documentPermission.hasModifyACL() != dto.isModifyACL())
                    ) {
                        fRbac.setDocumentPermission(getPermissionExistFolderRbac(dto));
                        folderRbacManager.update(fRbac);
                    }
                    continue;
                }
                EdsDocumentPermission permission = getPermission(dto);
                EdsRelationship relationship;
                if (null != dto.getRelationship() && !"".equals(dto.getRelationship())) {
                    relationship = relationshipManager.getRelationship(dto.getRelationship());
                } else {
                    relationship = relationshipManager.getRelationship(EdsRelationship.DOC_VIEWER);
                }
                if (dto.getGroup() != null) {
                    folderRbacManager.createCustomFolderGroupRelationEntry(folder, groupManager.get(dto.getGroup().getGroupID()), permission, relationship, isSubFolder ? EdsFolderRbac.INHERITED : EdsFolderRbac.CUSTOM);
                } else if (dto.getUser() != null) {
                    folderRbacManager.createCustomFolderUserRelationEntry(folder, userManager.get(dto.getUser().getObjectId()), permission, relationship, (isSubFolder || EdsRelationship.DOC_OWNER.equals(dto.getRelationship())) ? EdsFolderRbac.INHERITED : EdsFolderRbac.CUSTOM);
                }
            }
        }
    }

    @Transactional
    public void setFolderPermissionsToFile(EdsFileHeader file, List<PermissionHolder> permissions, boolean isSubFolder, EdsUser user)
            throws ObjectNotFoundException, InsufficientPermissionsException {
        log.info("================== Set Folder permissions To file ==================");
        setFilePermissions(file, permissions, isSubFolder);
        Date now = new Date();
        file.getAuditInfo().setModificationDate(now);
        file.getAuditInfo().setModifiedBy(user);
        try {
//            solrManager.addFileToIndex(file);
            folderSolrComponent.index(file);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            log.trace("==================" + e.getMessage() + "==================");
            baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_ADD, file, user);
        }
    }

    private EdsFolderRbac getFolderOrFileRbacForUser(List<EdsFolderRbac> rbacList, Integer userOrGroupId, boolean isUser) {
        Optional<EdsFolderRbac> optFolderRbac = isUser ? rbacList.stream()
                .filter(rb -> EdsTrusteeType.USER.equals(rb.getTrusteeType()) && rb.getUser() != null)
                .filter(rb -> userOrGroupId.equals(rb.getUser().getObjectID())).findFirst() :
                rbacList.stream()
                        .filter(rb -> EdsTrusteeType.GROUP.equals(rb.getTrusteeType()) && rb.getGroup() != null)
                        .filter(rb -> userOrGroupId.equals(rb.getGroup().getObjectID())).findFirst();
        return optFolderRbac.orElse(null);
    }

    private EdsDocumentPermission getPermission(PermissionHolder dto) {
        EdsDocumentPermission res = null;
        if (dto.getObjectId() != null) {
            res = documentPermissionManager.get(dto.getObjectId());
        }
        if (res == null) {
            res = new EdsDocumentPermission();
        }
        res.setRead(dto.isRead());
        res.setWrite(dto.isWrite());
        res.setDelete(dto.isDelete());
        res.setModifyACL(dto.isModifyACL());
        return res;
    }

    private EdsDocumentPermission getPermissionExistFolderRbac(PermissionHolder dto) {
        EdsDocumentPermission edsDocumentPermission = new EdsDocumentPermission();
        edsDocumentPermission.setRead(dto.isRead());
        edsDocumentPermission.setWrite(dto.isWrite());
        edsDocumentPermission.setDelete(dto.isDelete());
        edsDocumentPermission.setModifyACL(dto.isModifyACL());
        documentPermissionManager.create(edsDocumentPermission);
        return edsDocumentPermission;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FolderResource> getSharedRootFolders(Integer userId) throws ObjectNotFoundException {
        if (userId == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        List<EdsFolder> folders = folderManager.getSharedRootFolders(userId);
        ArrayList<FolderResource> result = new ArrayList<>();
        for (EdsFolder f : folders) {
            FolderResource dto = f.getDTO();
            //dto.setPermissions(getFolderPermissions(f.getObjectID()));
            dto.setPermission(folderRbacManager.getFolderEntryForUser2(f, userManager.getUser()).getDTO());
            dto.setSubfolders(getSharedSubfolders(userId, f.getObjectID()));
            result.add(dto);
        }
        return result;
    }

    @Deprecated
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FolderResource> getSharedRootFolders(Integer ownerId, Integer callingUserId) throws ObjectNotFoundException {
        if (ownerId == null) {
            throw new ObjectNotFoundException("No owner specified");
        }
        if (callingUserId == null) {
            throw new ObjectNotFoundException("No calling user specified");
        }
        List<EdsFolder> folders = folderManager.getSharedRootFolders(ownerId, callingUserId);
        ArrayList<FolderResource> result = new ArrayList<>();
        for (EdsFolder f : folders) {
            FolderResource dto = f.getDTO();
            dto.setPermission(folderRbacManager.getFolderEntryForUser2(f, userManager.getUser()).getDTO());
            //dto.setPermissions(getFolderPermissions(f.getObjectID()));
            dto.setSubfolders(getSharedSubfolders(ownerId, callingUserId, f.getObjectID()));
            result.add(dto);
        }
        return result;

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<FolderResource> getSharedRootFolderList(Integer userId) throws ObjectNotFoundException {
        return getSharedRootFolderList(null, userId);
    }

    @Transactional(readOnly = true)
    public List<FolderResource> getSharedRootFolderList(Integer ownerId, Integer userId) throws ObjectNotFoundException {
        Optional.ofNullable(userId).orElseThrow(() -> new ObjectNotFoundException("No calling user specified"));
        List<FolderResource> allSharedFolders = folderManager.getSharedFolders(ownerId, userId);
        Map<Integer, FolderResource> resourceMap = allSharedFolders.stream().collect(Collectors.toMap(RestResource::getObjectId, r -> r));

        List<FolderResource> rootFolders = allSharedFolders.stream()
                .filter(r -> r.getParent() == null || resourceMap.get(r.getParent().getObjectId()) == null)
                .collect(Collectors.toList());
        allSharedFolders.removeAll(rootFolders); //clear all all filtered data
        rootFolders.forEach(root -> root.setSubfolders(retrieveSharedSubFolders(root.getObjectId(), allSharedFolders)));
        return rootFolders;
    }

    private ArrayList<FolderResource> retrieveSharedSubFolders(Integer parentId, List<FolderResource> resources) {
        ArrayList<FolderResource> subFolders = resources.stream()
                .filter(r -> r.getParent() != null && r.getParent().getObjectId().equals(parentId))
                .collect(Collectors.toCollection(ArrayList::new));
        if (CollectionUtils.isNotEmpty(subFolders)) {
            resources.removeAll(subFolders);//clear all all filtered data
            subFolders.forEach(sf -> sf.setSubfolders(retrieveSharedSubFolders(sf.getObjectId(), resources)));
        }
        return subFolders;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FolderResource> getSharedSubfolders(Integer userId, Integer callingUserId, Integer folderId) throws ObjectNotFoundException {
        if (userId == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (callingUserId == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (folderId == null) {
            throw new ObjectNotFoundException("No folder specified");
        }
        EdsUser user = userManager.get(callingUserId);
        EdsFolder folder = folderManager.get(folderId);
        ArrayList<FolderResource> result = new ArrayList<>();
        if (folderManager.isSharedFolderForOtherUser(user.getObjectID(), folder.getObjectID())) {
            for (EdsFolder f : folder.getSubfolders()) {
                if (folderManager.isSharedFolderForOtherUser(user.getObjectID(), f.getObjectID()) && !f.isDeleted()) {
                    FolderResource dto = f.getDTO();
                    dto.setPermission(folderRbacManager.getFolderEntryForUser2(folder, userManager.getUser()).getDTO());
                    //dto.setPermissions(getFolderPermissions(f.getObjectID()));
                    dto.setSubfolders(getSharedSubfolders(userId, callingUserId, dto.getObjectId()));
                    result.add(dto);
                }
            }
        }
        return result;
    }

    /**
     * Set the provided permissions as the new permissions of the specified
     * file. This method sets the modification date/user attributes to the
     * current values as a side effect.
     *
     * @param file
     * @param permissions
     * @param isSubFolder
     * @throws com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException
     * @throws com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException
     */
    @Transactional
    public void setFilePermissions(EdsFileHeader file, List<PermissionHolder> permissions, boolean isSubFolder) throws ObjectNotFoundException, InsufficientPermissionsException {
        List<EdsFolderRbac> rbacList2 = new ArrayList<>();
        if (permissions != null && !permissions.isEmpty()) {
            // Delete previous entries.
            for (EdsFolderRbac rbac : folderRbacManager.getFileRbacEntries(file.getObjectID())) {
                boolean deleted = false;
                if (rbac.getEntryType() != EdsFolderRbac.CUSTOM || (!isSubFolder && rbac.getEntryType() == EdsFolderRbac.CUSTOM)) {
                    if (EdsRelationship.DOC_ADMINISTRATOR.equals(rbac.getRelationship())) {

                    } else if (EdsRelationship.DOC_DIRECTOR.equals(rbac.getRelationship())) {

                    } else if (EdsRelationship.DOC_CREATOR.equals(rbac.getRelationship())) {
                        folderRbacManager.delete(rbac);
                        deleted = true;
                    } else if (EdsRelationship.DOC_OWNER.equals(rbac.getRelationship())) {
                        folderRbacManager.delete(rbac);
                        deleted = true;
                    } else if (EdsRelationship.DOC_READER.equals(rbac.getRelationship())) {
                        folderRbacManager.delete(rbac);
                        deleted = true;
                    } else if (EdsRelationship.DOC_VIEWER.equals(rbac.getRelationship())) {
                        folderRbacManager.delete(rbac);
                        deleted = true;
                    }
                }
                if (!deleted) {
                    rbacList2.add(rbac);
                }
            }

            for (PermissionHolder dto : permissions) {
                if (dto.isCanChange()) {
                    if (dto.getUser() != null && dto.getUser().getObjectId().equals(file.getOwner().getObjectID()) && (!dto.isRead() || !dto.isWrite() || !dto.isDelete() || !dto.isModifyACL())) {
                        continue;
                    }
                    // Don't include 'empty' permission.
                    if (!dto.isRead() && !dto.isWrite() && !dto.isDelete() && !dto.isModifyACL()) {
                        continue;
                    }

                    EdsFolderRbac fRbac = null;
                    if (dto.getGroup() != null) {
                        fRbac = getFolderOrFileRbacForUser(rbacList2, dto.getGroup().getGroupID(), false);
                    } else if (dto.getUser() != null) {
                        fRbac = getFolderOrFileRbacForUser(rbacList2, dto.getUser().getObjectId(), true);
                    }
                    if (fRbac != null) {
                        fRbac.setDocumentPermission(fRbac.getDocumentPermission().mergePermission(getPermission(dto)));
                        folderRbacManager.update(fRbac);
                        continue;
                    }

                    EdsDocumentPermission permission = getPermission(dto);
                    EdsRelationship relationship;
                    if (null != dto.getRelationship() && !"".equals(dto.getRelationship())) {
                        relationship = relationshipManager.getRelationship(dto.getRelationship());
                    } else {
                        relationship = relationshipManager.getRelationship(EdsRelationship.DOC_VIEWER);
                    }
                    if (dto.getGroup() != null) {
                        folderRbacManager.createCustomFileGroupRelationEntry(file, groupManager.get(dto.getGroup().getGroupID()), permission, relationship, isSubFolder ? EdsFolderRbac.INHERITED : EdsFolderRbac.CUSTOM);
                    } else if (dto.getUser() != null) {
                        folderRbacManager.createCustomFileUserRelationEntry(file, userManager.get(dto.getUser().getObjectId()), permission, relationship, (isSubFolder || EdsRelationship.DOC_OWNER.equals(dto.getRelationship())) ? EdsFolderRbac.INHERITED : EdsFolderRbac.CUSTOM);
                    }

                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FolderResource> getSharedSubfolders(Integer userId, Integer folderId) throws ObjectNotFoundException {
        if (userId == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (folderId == null) {
            throw new ObjectNotFoundException("No folder specified");
        }
        final EdsUser user = userManager.getUser();
        EdsFolder folder = folderManager.get(folderId);
        ArrayList<FolderResource> result = new ArrayList<>();
        if (folderManager.isSharedFolder(user.getObjectID(), folder.getObjectID())) {
            for (EdsFolder f : folder.getSubfolders()) {
                if (folderManager.isSharedFolder(user.getObjectID(), f.getObjectID()) && !f.isDeleted()) {
                    FolderResource fDTO = f.getDTO();
                    //fDTO.setPermissions(getFolderPermissions(fDTO.getObjectId()));
                    fDTO.setPermission(folderRbacManager.getFolderEntryForUser2(folder, userManager.getUser()).getDTO());
                    result.add(fDTO);
                }
            }
        }
        return result;
    }

    @Transactional
    public ArrayList<FileResource> getSharedFilesNotInSharedFolders(EdsUser user) throws ObjectNotFoundException {
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        List<EdsFileHeader> files = fileHeaderManager.getSharedFilesNotInSharedFolders(user);
        ArrayList<FileResource> result = new ArrayList<>();
        for (EdsFileHeader f : files) {
            EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(f, user);
            if (!permission.hasRead()) {
                FileResource fDTO = f.getDTO();
                fDTO.setPermissions(getFilePermissions(fDTO.getObjectId()));
                fDTO.setPermission(folderRbacManager.getFilePermissionForUser(f, userManager.getUser()).getDTO());
                if (GOOGLE.equals(fDTO.getUploadType())) {
                    EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(f.getCurrentBody());
                    if (googleDocumentsSettings != null) {
                        fDTO.setGoogleDownloadLink(googleDocumentsSettings.getDocumentLink());
                    }
                } else if (OFFICE_365.equals(fDTO.getUploadType()) || OFFICE_365_SHARE_POINT.equals(fDTO.getUploadType())) {
                    EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(f.getCurrentBody());
                    if (googleDocumentsSettings != null) {
                        fDTO.setDocumentID(googleDocumentsSettings.getDocumentID());
                        fDTO.setDocumentOpenID(googleDocumentsSettings.getDocumentOpenID());
                        fDTO.setOfficeDownloadLink(googleDocumentsSettings.getDocumentLink());
                    }
                } else {
                    fDTO.setAmazonLink(commonServiceLocal.getFileUrl(fDTO.getBodyId()));
                }
                result.add(fDTO);
            }
        }
        return result;
    }

    @Transactional
    public void updateFile(Integer fileId, String name, Boolean readForAll, ArrayList<PermissionHolder> permissions)
            throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = userManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileId == null) {
            throw new ObjectNotFoundException("No file specified");
        }
        EdsFileHeader file = fileHeaderManager.get(fileId);
        final EdsFolder parent = file.getFolder();
        if (parent == null) {
            throw new ObjectNotFoundException("The specified file has no parent folder");
        }

        // Check permissions for modifying the file metadata.
        EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
        if (name != null && !permission.hasWrite()) {
            throw new InsufficientPermissionsException("User " + user.getFullName() + " cannot update file " + file.getName());
        }
        // Check permissions for making file public.
        // Check permissions for modifying the ACL.
        if (permissions != null && !permissions.isEmpty() && !permission.getModifyACL()) {
            throw new InsufficientPermissionsException("User " + user.getFullName() + " cannot update the permissions on file " + file.getName());
        }

        if (name != null && !name.equals(file.getName())) {
            // Do plain check for file already exists.
            // Extreme concurrency case should be caught by constraint violation later.
            if (folderManager.existsFile(parent.getObjectID(), name)) {
                throw new DuplicateNameException("A file with the name '" + name + "' already exists");
            }
            file.setName(name);
            file.getCurrentBody().setOriginalName(name);
            EdsReference googleRefType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.GOOGLE);
            EdsReference officeRefType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.OFFICE_365);
            EdsReference officesharePointRefType = referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.OFFICE_365_SHARE_POINT);
            if (file.getCurrentBody().getType().equals(googleRefType)) {
                try {
                    googleDocumentsManager.updateFile(file.getCurrentBody());
                } catch (GeneralSecurityException | ServiceException | IOException e) {
                    e.printStackTrace();
                }
            } else if (file.getCurrentBody().getType().equals(officeRefType) || file.getCurrentBody().getType().equals(officesharePointRefType)) {
                updateOffice365File(file.getCurrentBody(), file.getCurrentBody().getType().getCode());
            }
        }

        file.getAuditInfo().setModificationDate(new Date());
        file.getAuditInfo().setModifiedBy(user);

        if (readForAll != null && user.equals(file.getOwner())) {
            file.setReadForAll(readForAll);
        }
        if (permissions != null && !permissions.isEmpty()) {
            setFilePermissions(file, permissions, false);
        }

        /*
         * Force constraint violation to manifest itself here.
         * This should cover extreme concurrency cases that the simple check
         * above hasn't caught.
         */
        try {
            fileHeaderManager.update(file);
        } catch (EJBTransactionRolledbackException e) {
            Throwable cause = e.getCause();
            if (cause instanceof PersistenceException && cause.getCause() instanceof ConstraintViolationException) {
                throw new DuplicateNameException("A file or folder with the name '" + name + "' already exists");
            }
            throw e;
        }

        try {
//            solrManager.addFileToIndex(file);
            folderSolrComponent.index(file);
        } catch (InterruptedException e) {
            baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_ADD, file, user);
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(fileId);
        ServerUtils.kpiLog(log, kpiLog, "Update document");
    }

    private void updateOffice365File(EdsFileBody upload, String storegeType) {
        String driveType = OFFICE_365.equals(storegeType) ? Office365Constants.OFFICE_ONE_DRIVE : Office365Constants.OFFICE_SHARE_POINT;
        EdsSinxDocumentsSettings docSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(upload);
        if (docSettings != null) {
            Office365AccessTokenDTO dto = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), storegeType);
            if (dto != null) {
                Office365DriveItem item = office365DriveService.getItem(googleDocumentsManager.getDocumentID(docSettings), dto, driveType);
                item.setName(upload.getOriginalName());
                office365DriveService.updateItem(item, dto, driveType);
            }
        }
    }

    @Transactional
    @Override
    public void removeFileFromTrash(Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = userManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (fileId == null) {
            throw new ObjectNotFoundException("No file specified");
        }

        // Do the actual work.
        EdsFileHeader file = fileHeaderManager.get(fileId);
        EdsFolder parent = file.getFolder();
        if (parent == null) {
            throw new ObjectNotFoundException("The specified file has no parent folder");
        }
        EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
        if (!permission.hasDelete()) {
            throw new InsufficientPermissionsException("User " + user.getFullName() +
                    " cannot restore file " + file.getName());
        }

        file.setDeleted(false);
        file.getAuditInfo().setModificationDate(new Date());
        file.getAuditInfo().setModifiedBy(user);
        fileHeaderManager.update(file);
        folderRbacManager.indexFile(file);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        ServerUtils.kpiLog(log, kpiLog, "Files and folders restored");
    }

    @Transactional
    @Override
    public void removeFolderFromTrash(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = userManager.getUser();
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        if (folderId == null) {
            throw new ObjectNotFoundException("No folder specified");
        }
        EdsFolder folder = folderManager.get(folderId);
        EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(folder, user);
        if (!permission.hasDelete()) {
            throw new InsufficientPermissionsException("User " + user.getFullName() +
                    " cannot restore folder " + folder.getName());
        }
        folder.setDeleted(false);
        if (folder.getAuditInfo() != null) {
            folder.getAuditInfo().setModificationDate(new Date());
            folder.getAuditInfo().setModifiedBy(user);
        }
        for (EdsFileHeader file : folder.getFiles()) {
            removeFileFromTrash(file.getObjectID());
        }
        for (EdsFolder subFolder : folder.getSubfolders()) {
            removeFolderFromTrash(subFolder.getObjectID());
        }
        folderManager.update(folder);
        folderManager.indexFolder(folder, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public SystemResource getSystemFolder() throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        EdsFolder systemFolder = folderManager.getSystemFolder(company.getObjectID());
        if (systemFolder == null) {
            createSystemFolders(company.getObjectID());
            systemFolder = folderManager.getSystemFolder(company.getObjectID());
        }
        if (systemFolder == null) {
            throw new ObjectNotFoundException("Can not found System Folder!");
        }
        FolderResource folderResource = getFolderResource(systemFolder.getObjectID(), user);
        SystemResource systemResource = new SystemResource();
        systemResource.setName(folderResource.getEncodeName());
        systemResource.setObjectId(folderResource.getObjectId());
        systemResource.setSubFolders(folderResource.getFolders());
        return systemResource;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FolderResource getFolderResource(Integer folderId, EdsUser user) throws InsufficientPermissionsException {
        EdsFolder folder = folderManager.get(folderId);
        return getFolderResource(folder, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FolderResource getFolderResource(EdsFolder folder, EdsUser user) throws InsufficientPermissionsException {
        FolderResource f;
        boolean showBackupFolder = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHOW_BACKUP_FOLDER);
        EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(folder, user);
        if (!permission.hasRead()) {
            throw new InsufficientPermissionsException("You don't have the permissions to read this folder");
        }
        f = folder.getDTO();
        f.setPermission(permission.getDTO());
        if (folder.getParent() != null) {
            f.setParentName(folder.getParent().getName());
            f.setParentId(folder.getParent().getObjectID());
            if (f.getFileType() == EdsFolder.F_WORKSPACE_ROOT) {
                f.setRank(0);
            } else if (f.getFileType() == EdsFolder.F_PROJECT_ROOT) {
                f.setRank(1);
            } else if (f.getFileType() == EdsFolder.F_CRM_ROOT) {
                f.setRank(2);
            } else if (f.getFileType() == EdsFolder.F_PA_ROOT) {
                f.setRank(3);
            } else if (f.getFileType() == EdsFolder.F_AF_ROOT) {
                f.setRank(4);
            } else if (f.getFileType() == EdsFolder.F_HRMS_ROOT) {
                f.setRank(5);
            } else if (f.getFileType() == EdsFolder.F_WEBSITE_ROOT) {
                f.setRank(6);
            } else if ((f.getFileType() == EdsFolder.F_BACKUPS_ROOT || f.getFileType() == EdsFolder.F_XML_BACKUPS_ROOT) && user.hasRoles(EdsRole.ADMIN) && showBackupFolder) {
                f.setRank(7);
            } else if (f.getFileType() == EdsFolder.F_SETTINGS_ROOT) {
                f.setRank(8);
            } else if (f.getFileType() == EdsFolder.F_COMPANY_PUBLIC_ROOT) {
                f.setRank(9);
            } else if (f.getFileType() == EdsFolder.F_CUSTOM_FIELD_ROOT) {
                f.setRank(10);
            } else if (f.getFileType() == EdsFolder.F_PAYROLL_ROOT) {
                f.setRank(11);
            } else if (f.getFileType() == EdsFolder.F_NOTE_ROOT) {
                f.setRank(12);
            }
        }
        return f;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<FolderResource> getFoldersResource(ListingFilterParameter fp, List<EdsFolder> folders, EdsUser user, boolean deleted) {
        List<FolderResource> folderResources = new LinkedList<>();
        boolean showBackupFolder = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHOW_BACKUP_FOLDER);
        Map<EdsFolder, EdsDocumentPermission> folderListMap = folderRbacManager.getFoldersPermissionEntriesForUser(fp, folders, user, deleted);
        for (EdsFolder folder : folderListMap.keySet()) {
            FolderResource f = folder.getDTO();
            f.setPermission(folderListMap.get(folder).getDTO());
            if (folder.getParent() != null) {
                f.setParentName(folder.getParent().getName());
                f.setParentId(folder.getParent().getObjectID());
                if (f.getFileType() == EdsFolder.F_WORKSPACE_ROOT) {
                    f.setRank(0);
                } else if (f.getFileType() == EdsFolder.F_PROJECT_ROOT) {
                    f.setRank(1);
                } else if (f.getFileType() == EdsFolder.F_CRM_ROOT) {
                    f.setRank(2);
                } else if (f.getFileType() == EdsFolder.F_PA_ROOT) {
                    f.setRank(3);
                } else if (f.getFileType() == EdsFolder.F_AF_ROOT) {
                    f.setRank(4);
                } else if (f.getFileType() == EdsFolder.F_HRMS_ROOT) {
                    f.setRank(5);
                } else if (f.getFileType() == EdsFolder.F_WEBSITE_ROOT) {
                    f.setRank(6);
                } else if ((f.getFileType() == EdsFolder.F_BACKUPS_ROOT || f.getFileType() == EdsFolder.F_XML_BACKUPS_ROOT) && user.hasRoles(EdsRole.ADMIN) && showBackupFolder) {
                    f.setRank(7);
                } else if (f.getFileType() == EdsFolder.F_SETTINGS_ROOT) {
                    f.setRank(8);
                } else if (f.getFileType() == EdsFolder.F_COMPANY_PUBLIC_ROOT) {
                    f.setRank(9);
                } else if (f.getFileType() == EdsFolder.F_CUSTOM_FIELD_ROOT) {
                    f.setRank(10);
                } else if (f.getFileType() == EdsFolder.F_PAYROLL_ROOT) {
                    f.setRank(11);
                } else if (f.getFileType() == EdsFolder.F_NOTE_ROOT) {
                    f.setRank(12);
                }
            }
            folderResources.add(f);
        }

        return folderResources;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FileResource> getFilesFromFolders(ListingFilterParameter fp, List<EdsFolder> folders, EdsUser user, String fileName, boolean deleted) {
        ArrayList<FileResource> resultList = new ArrayList<>();
        List<FolderResource> folderResources = getFoldersResource(fp, folders, user, deleted);
        for (FolderResource folderResource : folderResources) {
            FileResource f = new FileResource();
            f.setFolderResource(folderResource);
            f.setObjectId(folderResource.getObjectId());
            f.setName(folderResource.getName());
            f.setOwner(folderResource.getOwner());
            f.setModificationDate(folderResource.getModificationDate());
            f.setCreatedBy(folderResource.getCreatedBy());
            f.setCreationDate(folderResource.getCreationDate());
            f.setDeleted(deleted);
            f.setFolder(true);
            f.setPath(folderResource.getPath());
            f.setPermission(folderResource.getPermission());

            if (fileName != null && !"".equals(fileName)) {
                f.setName(fileName);
            }

            resultList.add(f);
        }

        return resultList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public HashSet<PermissionHolder> getFolderPermissions(Integer folderId) {
        Locale userLocale = ServerUtils.getUserLocale();
        String language = userLocale.getLanguage();
        List<EdsFolderRbac> res = folderRbacManager.getFolderRbacEntries(folderId);
        HashSet<PermissionHolder> permissionHolderSet = new HashSet<>();
        for (EdsFolderRbac rbac : res) {
            PermissionHolder p = new PermissionHolder();
            p.setObjectId(rbac.getDocumentPermission().getObjectID());
            p.setRelationship(rbac.getRelationship());
            if (EdsTrusteeType.GROUP.equals(rbac.getTrusteeType()) && rbac.getGroup() != null) {
                GroupMembersViewItem groupM = rbac.getGroup().getDTO();
                String translated = GroupTranslation.getTranslatedName(groupM.getGroupConstantName(), language);
                groupM.setGroupName(translated);
                p.setGroup(groupM);
            }
            if (EdsTrusteeType.USER.equals(rbac.getTrusteeType())) {
                p.setUser(rbac.getUser().getDTO());
                if (EdsRelationship.DOC_CREATOR.equals(rbac.getRelationship())) {
                    continue;
                } else if (EdsRelationship.DOC_OWNER.equals(rbac.getRelationship())) {
                    p.setRole("Owner");
                }
            }
            p.setCanChange(true);
            if (EdsRelationship.DOC_ADMINISTRATOR.equals(rbac.getRelationship())) {
                p.setCanChange(false);
            } else if (EdsRelationship.DOC_DIRECTOR.equals(rbac.getRelationship())) {
                p.setCanChange(false);
            } else if (EdsRelationship.DOC_OWNER.equals(rbac.getRelationship())) {

            } else if (EdsRelationship.DOC_READER.equals(rbac.getRelationship())) {

            } else if (EdsRelationship.DOC_VIEWER.equals(rbac.getRelationship())) {

            }
            p.setDelete(rbac.getDocumentPermission().hasDelete());
            p.setRead(rbac.getDocumentPermission().hasRead());
            p.setWrite(rbac.getDocumentPermission().hasWrite());
            p.setModifyACL(rbac.getDocumentPermission().hasModifyACL());
            permissionHolderSet.add(p);
        }
        return permissionHolderSet;
    }



    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashSet<PermissionHolder> getFilePermissions(Integer fileId) {
        List<EdsFolderRbac> res = folderRbacManager.getFileRbacEntries(fileId);
        HashSet<PermissionHolder> permissionHolderSet = new HashSet<>();
        for (EdsFolderRbac rbac : res) {
            PermissionHolder p = new PermissionHolder();
            p.setObjectId(rbac.getDocumentPermission().getObjectID());
            p.setRelationship(rbac.getRelationship());
            if (EdsTrusteeType.GROUP.equals(rbac.getTrusteeType())) {
                GroupMembersViewItem groupM = null;
                if (rbac.getGroup() != null) {
                    groupM = rbac.getGroup().getDTO();
                }
                if (groupM != null) {
                    groupM.setGroupName(commonLocalizer.localize(groupM.getGroupConstantName(), groupM.getGroupName()));
                }
                p.setGroup(groupM);
            }
            if (EdsTrusteeType.USER.equals(rbac.getTrusteeType())) {
                p.setUser(rbac.getUser().getDTO());
                if (EdsRelationship.DOC_CREATOR.equals(rbac.getRelationship())) {
                    continue;
                } else if (EdsRelationship.DOC_OWNER.equals(rbac.getRelationship())) {
                    p.setRole("Owner");
                }
            }
            p.setCanChange(true);
            if (EdsRelationship.DOC_ADMINISTRATOR.equals(rbac.getRelationship())) {
                p.setCanChange(false);
            } else if (EdsRelationship.DOC_DIRECTOR.equals(rbac.getRelationship())) {
                p.setCanChange(false);
            } else if (EdsRelationship.DOC_OWNER.equals(rbac.getRelationship())) {

            } else if (EdsRelationship.DOC_READER.equals(rbac.getRelationship())) {

            } else if (EdsRelationship.DOC_VIEWER.equals(rbac.getRelationship())) {

            }
            if (rbac.getDocumentPermission() != null) {
                p.setDelete(rbac.getDocumentPermission().hasDelete());
                p.setRead(rbac.getDocumentPermission().hasRead());
                p.setWrite(rbac.getDocumentPermission().hasWrite());
                p.setModifyACL(rbac.getDocumentPermission().hasModifyACL());
            }
            permissionHolderSet.add(p);
        }
        return permissionHolderSet;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<FileResource> getFileResource(ListingFilterParameter filter, EdsUser user) {
        filter.setCheckNumber(employeeManager.isIntegerEmployeeCodeEnabled());
        return getFileListSolrResponse(filter, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileResource getFileResource(Integer fileHeaderId, EdsUser user, ListingFilterParameter fp) throws ObjectNotFoundException {
        EdsFileHeader fileHeader = fileHeaderManager.get(fileHeaderId);
        if (fileHeader == null) {
            return null;
        }
        FileResource f = fileHeader.getDTO();
        f.setType(commonLocalizer.localize(f.getType(), f.getType()));
        f.setPermission(folderRbacManager.getFilePermissionForUser(fileHeader, user).getDTO());
        f.setPermissions(getFilePermissions(f.getObjectId()));
        if (GOOGLE.equals(f.getUploadType())) {
            EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(fileHeader.getCurrentBody());
            if (googleDocumentsSettings != null) {
                f.setGoogleDownloadLink(googleDocumentsSettings.getDocumentLink());
            } else {
                return null;
            }
        } else if (OFFICE_365.equals(f.getUploadType()) || OFFICE_365_SHARE_POINT.equals(f.getUploadType())) {
            EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(fileHeader.getCurrentBody());
            if (googleDocumentsSettings != null) {
                f.setDocumentID(googleDocumentsSettings.getDocumentID());
                f.setDocumentOpenID(googleDocumentsSettings.getDocumentOpenID());
                f.setOfficeDownloadLink(googleDocumentsSettings.getDocumentLink());
            } else {
                return null;
            }
        } else {
            f.setAmazonLink(commonServiceLocal.getFileUrl(f.getBodyId()));
        }
        if (fileHeader.getFileType() == EdsFileHeader.F_TASK || fileHeader.getFileType() == EdsFileHeader.F_PR_ISSUE) {
            EdsTask task = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    task = taskManager.get(fileHeader.getEntityId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (task != null) {
                f.setEntityName(task.getName());
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_EMPLOYEE_PROFILE || EdsFileHeader.F_COMPANY_DOCUMENTS == fileHeader.getFileType()) {
            EdsEmployee employee;
            long daysLeft = 0;
            if (fileHeader.getExpireDate() != null && fileHeader.getExpireDate().after(new Date())) {
                Long dateDiff = fileHeader.getExpireDate().getTime() - new Date().getTime();
                daysLeft = TimeUnit.DAYS.convert(dateDiff, TimeUnit.MILLISECONDS);
            }
            f.setDaysLeft(daysLeft);
            if (fileHeader.getEnetityUser() != null && fileHeader.getEnetityUser().getEmployee() != null) {
                employee = fileHeader.getEnetityUser().getEmployee();
                f.setEntityName(employee.getName());
                if (employee.getProfile() != null) {
                    String code = employee.getProfile().getEmployeeCode();
                    if (code != null) {
                        f.setEmployeeCode(fp.isCheckNumber() ? code.replaceAll("[\\D]", "") : code);
                    }
                }
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_COMPANY_DOCUMENTS) {
            EdsCompany company = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    company = companyManager.get(fileHeader.getEntityId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (company != null) {
                f.setEntityName(company.getName());
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_EXP_DOC) {
            EdsExpenseReport expenseReport = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    expenseReport = reportManager.get(fileHeader.getEntityId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (expenseReport != null) {
                f.setEntityName(expenseReport.getTitle());
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_VACANCY) {
            EdsVacancy vacancy = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    vacancy = vacancyManager.get(fileHeader.getEntityId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (vacancy != null) {
                f.setEntityName(vacancy.getJobTitle());
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_CANDIDATE) {
            EdsCrmContact candidate = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    candidate = crmContactManager.get(fileHeader.getEntityId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (candidate != null) {
                f.setEntityName(candidate.getName());
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_PLACEMENT) {
            EdsPlacement placement = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    placement = placementManager.get(fileHeader.getEntityId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (placement != null && placement.getCandidate() != null) {
                f.setEntityName(placement.getCandidate().getName());
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_LEAVE_REQUEST) {
            EdsSickRequest sickRequest = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    sickRequest = sickRequestManager.get(fileHeader.getEntityId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (sickRequest != null && sickRequest.getEmployee() != null) {
                String name = "";
                if (sickRequest.getLeaveReason() != null) {
                    name = referenceWfmMessageSource.localizeRef(sickRequest.getLeaveReason());
                }
                final String requestType = name.contains("request") || name.contains("Request") ? name : name + " request";
                final String requestDuration = "(" + ServerUtils.longDateFormat(sickRequest.getStartDate(), user) + "-" + ServerUtils.longDateFormat(sickRequest.getEndDate(), user) + ")";
                f.setEntityName(sickRequest.getEmployee().getName() + "/" + requestType + requestDuration);
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_INCIDENT) {
            EdsPerformanceNote incident = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    incident = performanceNoteManager.get(fileHeader.getEntityId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (incident != null) {
                String entityName = (incident.getRelatedTo() != null ? (incident.getRelatedTo().getName() + "/") : "") + incident.getName();
                f.setEntityName(entityName);
            }
        } else if (fileHeader.getFileType() == EdsFileHeader.F_EMAIL_TEMPLATE) {
            EdsEmailTemplate emailTemplate = null;
            try {
                if (fileHeader.getEntityId() != null) {
                    emailTemplate = emailTemplateManager.get(fileHeader.getEntityId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (emailTemplate != null) {
                f.setEntityName(emailTemplate.getName());
            }
        }

        return f;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<FolderResource> getSystemSubFolders(Integer parentId) {
        return new ArrayList<>(getSubFolders(parentId, false));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<FolderResource> getSubFolders(Integer parentId, boolean deleted) {
        final EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        boolean showBackupFolder = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHOW_BACKUP_FOLDER);
        List<EdsFolder> subFolders = folderManager.get(parentId).getSubfolders();
        List<FolderResource> folderResources = getFoldersResource(null, subFolders, user, deleted);

        EdsFolder systemEdsFolder = folderManager.getSystemFolder(company.getObjectID());
        EdsFolder backupsEdsFolder = folderManager.getFolderByFolderType(EdsFolder.F_BACKUPS_ROOT);
        EdsFolder xmlBackupsEdsFolder = folderManager.getFolderByFolderType(EdsFolder.F_XML_BACKUPS_ROOT);
        ComparatorFactory factory;
        if (systemEdsFolder != null && parentId.equals(systemEdsFolder.getObjectID())) {
            factory = comparatorFactoriesI.get("rank");
            if (backupsEdsFolder != null && !(user.hasRoles(EdsRole.ADMIN) || showBackupFolder)) {
                for (FolderResource resource : folderResources) {
                    if (resource.getObjectId().equals(backupsEdsFolder.getObjectID())) {
                        folderResources.remove(resource);
                        break;
                    }
                }
            }

            if (xmlBackupsEdsFolder != null && !(user.hasRoles(EdsRole.ADMIN) || showBackupFolder)) {
                for (FolderResource resource : folderResources) {
                    if (resource.getObjectId().equals(xmlBackupsEdsFolder.getObjectID())) {
                        folderResources.remove(resource);
                        break;
                    }
                }
            }
        } else {
            factory = comparatorFactoriesI.get("name");
        }
        if (!folderResources.isEmpty()) {
            folderResources.sort(factory.createComparator(Constants.ASC));
        }

        return folderResources;
    }

    private ArrayList<FileResource> getSubFoldersAsFiles(ListingFilterParameter fp) {
        Integer parentId = fp.getFolderId();
        Integer rootFolderId = fp.getRootID();

        final EdsUser user = userManager.getUser();
        boolean showBackupFolder = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHOW_BACKUP_FOLDER);
        EdsFolder backupsEdsFolder = folderManager.getFolderByFolderType(EdsFolder.F_BACKUPS_ROOT);
        EdsFolder xmlBackupsEdsFolder = folderManager.getFolderByFolderType(EdsFolder.F_XML_BACKUPS_ROOT);
        ArrayList<FileResource> folderResourceList = new ArrayList<>();
        EdsFolder currentFolder = folderManager.get(parentId);
        if (currentFolder == null) {
            List<EdsFolder> allFolders = folderManager.list(user.getCompany().getObjectID());
            if (allFolders != null && allFolders.size() > 0) {
                for (EdsFolder allFolder : allFolders) {
                    FileResource folder = new FileResource();
                    folder.setObjectId(allFolder.getObjectID());
                    folder.setName(allFolder.getName());
                    if (allFolder.getAuditInfo() != null) {
                        folder.setModificationDate(allFolder.getAuditInfo().getModificationDate());
                        if (allFolder.getAuditInfo().getModifiedBy() != null) {
                            folder.setOwnerName(allFolder.getAuditInfo().getCreatedBy().getName());
                        }
                        if (allFolder.getAuditInfo().getCreatedBy() != null) {
                            folder.setCreatedBy(allFolder.getAuditInfo().getCreatedBy().getName());
                        }
                        folder.setCreationDate(allFolder.getAuditInfo().getCreationDate());
                    }
                    folder.setDeleted(allFolder.isDeleted());
                    folder.setFolder(true);
                    folder.setPath(allFolder.getPath());

                    folderResourceList.add(folder);
                }
            }
        }
        List<EdsFolder> subFolders;
        List<FolderResource> folderResources = null;
        if (currentFolder != null) {
            subFolders = currentFolder.getSubfolders();
            folderResources = getFoldersResource(fp, subFolders, user, false);
        }

        if (backupsEdsFolder != null && folderResources != null && !folderResources.isEmpty() && !(user.hasRoles(EdsRole.ADMIN) || showBackupFolder)) {
            for (FolderResource resource : folderResources) {
                if (resource.getObjectId().equals(backupsEdsFolder.getObjectID())) {
                    folderResources.remove(resource);
                    break;
                }
            }
        }

        if (xmlBackupsEdsFolder != null && folderResources != null && !folderResources.isEmpty() && !(user.hasRoles(EdsRole.ADMIN) || showBackupFolder)) {
            for (FolderResource resource : folderResources) {
                if (resource.getObjectId().equals(xmlBackupsEdsFolder.getObjectID())) {
                    folderResources.remove(resource);
                    break;
                }
            }
        }
        if (folderResources != null) {
            for (FolderResource folderResource : folderResources) {
                FileResource f = new FileResource();
                f.setFolderResource(folderResource);
                f.setObjectId(folderResource.getObjectId());
                f.setName(folderResource.getName());
                f.setOwner(folderResource.getOwner());
                f.setModificationDate(folderResource.getModificationDate());
                f.setCreatedBy(folderResource.getCreatedBy());
                f.setCreationDate(folderResource.getCreationDate());
                f.setFolder(true);
                f.setPath(folderResource.getPath());
                f.setPermission(folderResource.getPermission());

                folderResourceList.add(f);
            }
        }

        if (!folderResourceList.isEmpty()) {
            folderResourceList.sort(fileResourcesComparatorFactory.createComparator(Constants.ASC));
        }

        if (!fp.isFromMobile()) {
            EdsFolder parentFolder = null;
            if (currentFolder != null) {
                parentFolder = currentFolder.getParent();
            }
            if (parentFolder != null && rootFolderId != null && parentId != null && !parentId.equals(rootFolderId) || parentFolder != null && parentId != null && rootFolderId == null) {
                FileResource parentFileFolder = currentFolder.getFileDTO();
                parentFileFolder.setObjectId(parentFolder.getObjectID());
                parentFileFolder.setName("...");
                parentFileFolder.setBackFolder(true);
                try {
                    parentFileFolder.setFolderResource(getFolderResource(currentFolder, user));
                } catch (InsufficientPermissionsException e) {
                    e.printStackTrace();
                }
                folderResourceList.add(0, parentFileFolder);
            }
        }

        return folderResourceList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTasks(Integer projectId) {
        List<EdsTask> tasks = taskManager.listByProjectAndEmployee(projectId);
        SelectItem[] items = new SelectItem[tasks.size()];
        int i = 0;
        for (EdsTask task : tasks) {
            items[i] = new SelectItem(task.getObjectID(), task.getName());
            i++;
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public SelectItem[] getIssues(Integer projectId) {
        List<EdsIssue> issueList = issueManager.getProjectIssues(projectId);
        if (issueList != null && issueList.size() > 0) {
            SelectItem[] items = new SelectItem[issueList.size()];
            int i = 0;
            for (EdsIssue issue : issueList) {
                items[i] = new SelectItem(issue.getObjectID(), issue.getName());
                i++;
            }
            return items;
        }
        return new SelectItem[0];
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployees() {
        ListingFilterParameter fpE = new ListingFilterParameter();
        fpE.setViewAsId(EdsRole.DR);
        fpE.setResignedEmployeesIncluded(false);
        List<EdsEmployee> employees = employeeManager.list(fpE);
        SelectItem[] items = new SelectItem[employees.size()];
        int i = 0;
        for (EdsEmployee employee : employees) {
            items[i] = new SelectItem(employee.getObjectID(), employee.getName());
            i++;
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getVacancyOrCandidateOrPlacement(String vacancyOrCandidateOrPlacement) {
        ArrayList<SelectItem> vacancyOrCandidateOrPlacementItems = new ArrayList<>();
        if (VACANCY.equals(vacancyOrCandidateOrPlacement)) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setBriefly(false);
            List<EdsVacancy> vacancies = vacancyManager.list(filterParameter);
            for (EdsVacancy vacancy : vacancies) {
                if (vacancy != null) {
                    vacancyOrCandidateOrPlacementItems.add(new SelectItem(vacancy.getObjectID(), vacancy.getJobTitle()));
                }
            }
        } else if (CANDIDATE.equals(vacancyOrCandidateOrPlacement)) {
            ListResult<ContactListItem> candidates = recruitmentServiceLocal.listCandidates(new ListingFilterParameter());
            for (ContactListItem candidate : candidates.getList()) {
                if (candidate != null) {
                    vacancyOrCandidateOrPlacementItems.add(new SelectItem(candidate.getObjectId(), candidate.getName()));
                }
            }

        } else if (PLACEMENT.equals(vacancyOrCandidateOrPlacement)) {
            List<EdsPlacement> placements = placementManager.getPlacementList(new ListingFilterParameter(), userManager.getUser());
            for (EdsPlacement placement : placements) {
                if (placement != null && placement.getCandidate() != null) {
                    vacancyOrCandidateOrPlacementItems.add(new SelectItem(placement.getObjectID(), placement.getCandidate().getName()));
                }
            }
        } else if (ST_EMAIL_TEMPLATE.equals(vacancyOrCandidateOrPlacement)) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setParams("EmailTemplateListView");
            List<EdsEmailTemplate> emailTemplates = emailTemplateManager.getCompanyEmailTemplates(fp);
            for (EdsEmailTemplate emailTemplate : emailTemplates) {
                if (emailTemplate != null) {
                    vacancyOrCandidateOrPlacementItems.add(new SelectItem(emailTemplate.getObjectID(), emailTemplate.getName()));
                }
            }
        }

        return vacancyOrCandidateOrPlacementItems.toArray(new SelectItem[]{});
    }

    @Transactional
    @Override
    public void removeDocumentEntries(Integer userId) {
        //Delete document rbac Entries
        folderRbacManager.removeUserEntries(userId);
    }

    @Transactional
    @Override
    public void reIndexProjectDocument(Integer projectId) {
        EdsFolder projectFolder = folderManager.getProjectFolder(projectId);
        folderManager.indexFolder(projectFolder, true);
        for (EdsFileHeader file : projectFolder.getFiles()) {
            folderRbacManager.indexFile(file);
        }
    }

    @Transactional
    @Override
    public void reIndexTaskDocument(Integer taskId) {
        EdsFolder taskFolder = folderManager.getTaskFolder(taskManager.get(taskId).getProject().getObjectID());
        folderManager.indexFolder(taskFolder, true);
        if (56895 == SecurityContext.getCompanyID()) {
            String files = taskFolder.getFiles().stream().map(i -> i.getName()).map(String::valueOf).collect(Collectors.joining(", "));
            log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            log.info("INDEXING FILES CID - {}, TASK_ID: - {}, FILES - {}", SecurityContext.getCompanyID(), taskId, files);
            log.info("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        }
        for (EdsFileHeader file : taskFolder.getFiles()) {
            if (taskId.equals(file.getEntityId())) {
                folderRbacManager.indexFile(file);
            }
        }
    }

    private void createRbacForPrentFolder(EdsFolder parentFolder, List<PermissionHolder> permissions, boolean isSubFolder) {
        List<EdsFolderRbac> oldRbacList = folderRbacManager.getFolderRbacEntries(parentFolder.getObjectID());
        List<EdsFolderRbac> newRbacList = new ArrayList<>();
        collectRback(isSubFolder, oldRbacList, newRbacList);
        createRback(parentFolder, permissions, isSubFolder, newRbacList);

        if (parentFolder.getParent() != null) {
            createRbacForPrentFolder(parentFolder.getParent(), permissions, isSubFolder);
        }
    }

    /**
     * Related Company File Upload max size;
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Integer getCompanyFileUploadMaxSize() {
        return getCompanyFileUploadMaxSize((Integer[]) null);
    }

    /**
     * Company Used Storageni olib beradi;
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Double getCompanyFileUploadUsedStorage() {

        return uploadManager.getUploadStorage();
    }

    /**
     * Related Company File Upload max size;
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getCompanyFileUploadMaxSize(Integer... companyIDs) {
        EdsCompany company = null;
        if (companyIDs != null && companyIDs.length > 0 && companyIDs[0] != null) {
            company = companyManager.get(companyIDs[0]);
        }
        EdsUser user = userManager.getUser();//current user
        if (user != null && company == null) {
            company = user.getCompany();
        }
        if (company != null) {
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(company.getObjectID());
            if (companySystemSettings != null && companySystemSettings.getMaxSizeFileUpload() != null) {
                return companySystemSettings.getMaxSizeFileUpload();//max size file upload
            } else {
                return EdsCompanySystemSettings.DEFAULT_FILE_LIMIT;
            }
        } else {
            return null;
        }
    }

    private ListResult<FileResource> getFileListSolrResponse(ListingFilterParameter filter, EdsUser user) {
        EdsCompany company = user.getCompany();
        Set<EdsGroup> membershipsGroups = user.getMembershipGroups();
        if (filter.getFolderType() != null) {
            EdsFolder folder = getFolder(filter.getFolderType(), filter.getCrmEntityId(), company);
            if (folder != null) {
                filter.setFolderId(folder.getObjectID());
            }
        }
        StringBuilder solrQuery = new StringBuilder();
        FacetFilterRpc documentFacetFilter = filter.getFacetFilter();
        if (documentFacetFilter != null && !documentFacetFilter.isFilterChanges()) {
            documentFacetFilter = commonServiceLocal.getUserFacetFilter(documentFacetFilter);
        }
        ArrayList<Integer> employeeIDs = new ArrayList<>();
        if (DASHBOARD_WIDGET_CODE.EXPIRED_DOCUMENTS.equals(filter.getDataType()) && filter.getLocationId() != null || filter.getDepartmentId() != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setLocationId(filter.getLocationId());
            fp.setDepartmentId(filter.getDepartmentId());
            fp.setLimit(10000);
            ListResult<EmployeeListItem> employeeList = employeeService.getEmployeeList(fp);
            if (employeeList != null && !employeeList.getList().isEmpty()) {
                for (EmployeeListItem employee : employeeList.getList()) {
                    employeeIDs.add(employee.getObjectID());
                }
                filter.setObjectIDs(employeeIDs);
            }
        }
        solrQuery.append(QueryBuilderForSolr.getDocumentsSolrCore(filter, user, user.getCompany(), membershipsGroups));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(documentFacetFilter, user.getCompany(), SolrFolderRepresenter.FIELD_DATE_CREATION, SolrFolderRepresenter.FIELD_DATE_CREATION));
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println(solrQuery);
        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        return getFileListResponse(filter, user, solrQuery.toString());
    }

    private ListResult<FileResource> getFileListResponse(ListingFilterParameter filterParameter, EdsUser edsUser, String solrQuery) {
        Page<FolderSolrDoc> eventSolrDocPage = folderSolrComponent.getList(filterParameter, solrQuery);
        return getFileFromSolrResult(eventSolrDocPage, edsUser, filterParameter);
    }

    private ListResult<FileResource> getFileFromSolrResult(Page<FolderSolrDoc> folderSolrDocPage, EdsUser edsUser, ListingFilterParameter filterParameter) {
        int totalNumber = (int) folderSolrDocPage.getTotalElements();
        ArrayList<FileResource> fileResourceList = new ArrayList<>();
        for (FolderSolrDoc sDoc : folderSolrDocPage.getContent()) {
            Integer folderID = sDoc.getFolderId();
            try {
                FileResource resource = getFileResource(folderID, edsUser, filterParameter);
                if (resource != null) {
                    fileResourceList.add(resource);
                }
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ListResult<>(fileResourceList, totalNumber);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SearchResultItemList getSearchResult(DocumentsSearchItem searchItem) {
        ListResult<FileResource> listResult = null;
        try {
            ListingFilterParameter filterParameters = new ListingFilterParameter();
            filterParameters.setSearchKey(searchItem.getKeyword());
            filterParameters.setStart(searchItem.getStart());
            filterParameters.setLimit(searchItem.getLimit());
            listResult = getFileList(filterParameters);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
        if (listResult != null && listResult.getList() != null) {
            int count = listResult.getList().size();
            SearchResultItem[] resultItems = new SearchResultItem[count];
            for (int i = 0, listSize = listResult.getList().size(); i < listSize; i++) {
                final FileResource file = listResult.getList().get(i);
                resultItems[i] = new SearchResultItem();
                final boolean isAmazon = "AMAZON".equals(file.getUploadType()) || "LOCAL".equals(file.getUploadType());
                resultItems[i].setFromAmazon(isAmazon);
                if (isAmazon) {
                    resultItems[i].setPlainLink(getLink(file.getEntityName(), file.getEncodedPath()));
                } else {
                    resultItems[i].setPlainLink(file.getGoogleDownloadLink());
                }
                resultItems[i].setBodyId(file.getBodyId());
                resultItems[i].setName(file.getEncodedName());
                resultItems[i].setDescription(file.getDescription());
                resultItems[i].setSize(getSizeAsString(file.getContentLength()));
                final String companyId = SecurityContext.getInstance().getCompanyId();
                final EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.get(Integer.valueOf(companyId));
                EdsCompanySettings companySettings = null;
                if (companySystemSettings != null) {
                    companySettings = companySystemSettings.getCompany().getCompanySettings();
                }
                final boolean hasDateFormat = companySettings != null && companySettings.getLongDateFormat() != null;
                DateFormat formatter = new SimpleDateFormat(hasDateFormat ? companySettings.getLongDateFormat() : ServerUtils.SHORT_DATE_FORMAT_13);
                String date = formatter.format(file.getCreationDate());
                resultItems[i].setDateCreated(date);
                resultItems[i].setEntityType(file.getContentType());
                resultItems[i].setHighlits(new HashMap<>());
            }
            return new SearchResultItemList(listResult.getTotal(), resultItems, listResult.getQueryTime());
        }
        return new SearchResultItemList(0, new SearchResultItem[0], 0);
    }

    private String getLink(String entityName, String path) {
        String pathVal = URLDecoder.decode(path);
        if (entityName != null && !"".equals(entityName)) {
            pathVal = pathVal.substring(0, pathVal.lastIndexOf("/")) + "/" + entityName + pathVal.substring(pathVal.lastIndexOf("/"));
        }
        return pathVal;
    }

    private String getSizeAsString(Long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return getSize(size, 1024D) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return getSize(size, (1024D * 1024D)) + " MB";
        }
        return size + " B";
    }

    private String getSize(Long size, double v) {
        DecimalFormat format = new DecimalFormat("######.#");
        Double res = Double.valueOf(size.toString()) / v;
        return format.format(res);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FolderResource getFolderResource(int folderType, Integer entityID) {
        EdsUser user = userManager.getUser();
        EdsEmployee employee = null;
        if (Constants.F_EMPLOYEE_PROFILE == folderType && entityID != null) {
            employee = employeeManager.get(entityID);
        }
        if (employee != null && user != null && user.getObjectID() != null && employee.getObjectID() != null && user.getObjectID().equals(employee.getObjectID())) {
            user = employee;
        }
        EdsFolder folder = folderManager.getFolder(folderType, entityID);
        if (folder == null) {
            folder = folderManager.getFolderByFolderType(folderType);
        }
        if (folder != null) {
            FolderResource dto = folder.getDTO();
            dto.setPermission(folderRbacManager.getFolderEntryForUser2(folder, user).getDTO());
            return dto;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FolderResource getFolderResource(Integer folderID) {
        EdsFolder folder = folderManager.get(folderID);
        if (folder != null) {
            FolderResource dto = folder.getDTO();
            EdsUser user = userManager.getUser();
            dto.setPermission(folderRbacManager.getFolderEntryForUser2(folder, user).getDTO());
            return dto;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Integer getFolderID(int folderType, Integer entityId) {
        if (folderType == F_TASK || folderType == F_AF_ISSUE) {
            EdsTask task = taskManager.get(entityId);
            entityId = task.getProject().getObjectID();
        }

        FolderResource folderResource = getFolderResource(folderType, entityId);
        if (folderResource != null) {
            return folderResource.getObjectId();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<FileResource> getFileResources(int folderType, Integer folderId, Integer entityId) {
        ArrayList<FileResource> results = (ArrayList<FileResource>) attachmentUtilsManager.getAttachments(folderType, folderId, entityId);

        EdsFolder edsFolder;
        if (folderType == F_TASK || folderType == F_PR_ISSUE) {
            edsFolder = folderManager.getFolder(folderType, folderId);
        } else {
            edsFolder = folderManager.getFolder(folderType, entityId);
        }
        if (edsFolder == null) {
            edsFolder = folderManager.getFolderByFolderType(folderType);
        }
        if (edsFolder != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setFolderId(edsFolder.getObjectID());
            fp.setEntityID(entityId);

            List<EdsCopiedFileHeader> edsCopiedFiles = copiedFileHeaderManager.getListByFolderIdAndEntityId(fp);
            if (edsCopiedFiles != null && !edsCopiedFiles.isEmpty()) {
                for (EdsCopiedFileHeader edsCopiedFile : edsCopiedFiles) {
                    try {
                        FileResource copiedFile = getFileResource(edsCopiedFile.getFileHeaderId(), userManager.getUser(), fp);

                        if (copiedFile != null) {
                            copiedFile.setName(edsCopiedFile.getName());
                            results.add(copiedFile);
                        }
                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        results.sort((o1, o2) -> {
            if (o1.getCreationDate() != null && !"".equals(o1.getCreationDate()) && o2.getCreationDate() != null && !"".equals(o2.getCreationDate())) {
                if (o1.getCreationDate().before(o2.getCreationDate())) {
                    return 1;
                } else {
                    if (o1.getCreationDate().after(o2.getCreationDate())) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            } else {
                return 1;
            }
        });
        if (folderType == F_EMPLOYEE_PROFILE) {
            EdsUser user = userManager.get(entityId);
            if (user != null && user.getPhoto() != null) {
                String link = commonServiceLocal.getImageUrl(user.getPhoto().getObjectID());
                FileResource file = new FileResource();
                file.setName("Profile photo");
                file.setAmazonLink(link);
                file.setBodyId(user.getPhoto().getObjectID());
                file.setUploadType(EdsContextParams.getUploadType());
                results.add(file);
            }
        }
        return results;
    }

    @Override
    public Integer getRootFolderID() {
        return getRootFolder().getObjectID();
    }

    public EdsFolder getRootFolder() {
        EdsUser user = folderManager.getUser();
        EdsFolder rootFolder = folderManager.getRootFolder(user.getObjectID());
        if (rootFolder == null) {//Create root folder
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
            Locale locale = ServerUtils.getUserLocale();
            String myFoldersName = messageSource.getMessage("createSystemFolders.myFoldersName", null, "My folders", locale);
            rootFolder = createFolder(myFoldersName, null, user, EdsFolder.CUSTOM, EdsFolder.F_DEFAULT, null);
        }
        return rootFolder;
    }

    public FolderResource getRootFolderResource() {
        EdsFolder rootFolder = getRootFolder();
        return getFolderResource(rootFolder.getObjectID());
    }

    @Transactional
    public void indexFolders(Integer companyId) {
        SolrReindexRpc solrReindexRpc = new SolrReindexRpc();
        solrReindexRpc.setCompanyId(companyId);
        backendServiceLocal.indexCompanyFolders(solrReindexRpc);
    }

    @Override
    public void indexFiles(Integer companyId) {
        SolrReindexRpc solrReindexRpc = new SolrReindexRpc();
        solrReindexRpc.setCompanyId(companyId);
        backendServiceLocal.indexCompanyFiles(solrReindexRpc);
    }

    public FolderResource[] getUsersAllGoogleDocumentsAndFolders(String storageType) {
        try {
            if (GOOGLE.equals(storageType)) {
                return googleDocumentsManager.getAllGoogleFolders();
            } else if (OFFICE_365.equals(storageType) || OFFICE_365_SHARE_POINT.equals(storageType)) {
                return getOffice365RootFolders(null, storageType);
            }
        } catch (GeneralSecurityException | ServiceException | IOException e) {
            e.printStackTrace();
        }
        return new FolderResource[]{};
    }

    /*@Override
    public FolderResource[] getUsersAllGoogleDocumentsAndFolders() {
        return getUsersAllGoogleDocumentsAndFolders(GOOGLE);
    }*/

    private FolderResource[] getOffice365RootFolders(String itemId, String storageType) {
        ArrayList<FolderResource> result = new ArrayList<>();

        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), storageType);
        if (tokenDTO != null) {
            Office365ResourceCollection<Office365DriveItem> folders;
            if (itemId != null) {
                folders = office365DriveService.listFolderChildren(itemId, tokenDTO, storageType, false);
            } else {
                folders = office365DriveService.listRootChildren(tokenDTO, storageType);
                FolderResource allFilesFolder = new FolderResource();
                allFilesFolder.setName(commonLocalizer.localize("allFiles", "All Files"));
                allFilesFolder.setDriveFolderId("all_files");
                result.add(allFilesFolder);
            }

            if (folders != null && (folders.getValue() != null && folders.getValue().size() > 0)) {
                for (Office365DriveItem item : folders.getValue()) {
                    String savedTitle = "";
                    if (Constants.OFFICE_365_SHARE_POINT.equals(storageType)) {
                        savedTitle = hasThisUrlInSettings(item.getId());
                    }
                    if (item.getFolder() != null && savedTitle != null) {
                        FolderResource rootFolder = new FolderResource();
                        rootFolder.setName(savedTitle.isEmpty() ? item.getName() : savedTitle);
                        rootFolder.setCreatedBy(item.getId());
                        rootFolder.setDriveFolderId(item.getId());
                        initializeSubFoldersForPopUp(item.getId(), rootFolder, tokenDTO, storageType);
                        result.add(rootFolder);
                    }
                }
            }
        }
        return result.toArray(new FolderResource[]{});
    }

    private void initializeSubFoldersForPopUp(String id, FolderResource rootFolder, Office365AccessTokenDTO tokenDTO, String storageType) {
        ArrayList<FolderResource> newFolders = new ArrayList<>();
        Office365ResourceCollection<Office365DriveItem> folders = office365DriveService.listFolderChildren(id, tokenDTO, storageType, false);
        if (folders != null && (folders.getValue() != null && folders.getValue().size() > 0)) {
            for (Office365DriveItem folderitem : folders.getValue()) {
                if (folderitem.getFolder() != null) {
                    FolderResource item = new FolderResource();
                    item.setName(folderitem.getName());
                    item.setCreatedBy(folderitem.getId());
                    item.setDriveFolderId(folderitem.getId());
                    initializeSubFoldersForPopUp(folderitem.getId(), item, tokenDTO, storageType);
                    newFolders.add(item);
                }
            }
            rootFolder.setSubfolders(newFolders);
        }
    }

    @Override
    public FolderResource[] getGoogleSubFolders(String parentId, String storageType) {
        try {
            if (GOOGLE.equals(storageType)) {
                List<FolderResource> result = googleDocumentsManager.getGoogleFolders(googleDocumentsManager.getService(googleDocumentsManager.getUser()), parentId);
                return result.toArray(new FolderResource[]{});
            } else if (OFFICE_365.equals(storageType) || OFFICE_365_SHARE_POINT.equals(storageType)) {
                getOffice365RootFolders(parentId, storageType);
            }
        } catch (GeneralSecurityException | ServiceException | IOException e) {
            e.printStackTrace();
        }
        return new FolderResource[]{};
    }

    /*@Override
    public FolderResource[] getGoogleSubFolders(String parentId) {
        return getGoogleSubFolders(parentId, GOOGLE);
    }*/

    @Override
    public ArrayList<FileResource> getGoogleFiles(String folderId, String storageType) {
        try {
            if (GOOGLE.equals(storageType)) {
                return googleDocumentsManager.getGoogleFiles(googleDocumentsManager.getService(googleDocumentsManager.getUser()), folderId);
            } else if (OFFICE_365.equals(storageType) || OFFICE_365_SHARE_POINT.equals(storageType)) {
                return getOffice365Files(folderId, storageType);
            }
        } catch (GeneralSecurityException | ServiceException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private ArrayList<FileResource> getOffice365Files(String folderId, String storageType) {
        ArrayList<com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource> fileList = new ArrayList<>();

        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), storageType);
        if (tokenDTO != null) {
            getSubFolderFiles(folderId, fileList, tokenDTO, storageType);

        }
        fileList.sort(Comparator.comparing(o -> o.getEncodedName().toLowerCase()));
        return fileList;
    }

    private void getSubFolderFiles(String folderId, ArrayList<FileResource> fileList, Office365AccessTokenDTO tokenDTO, String storageType) {
        Office365ResourceCollection<Office365DriveItem> folders;
        if (folderId != null) {
            folders = office365DriveService.listFolderChildren(folderId, tokenDTO, storageType, true);
        } else {
            folders = office365DriveService.listRootChildren(tokenDTO, storageType);
        }

        if (folders != null && (folders.getValue() != null && folders.getValue().size() > 0)) {
            for (Office365DriveItem document : folders.getValue()) {
                if (document.getFile() != null) {
                    FileResource fileItem = new FileResource();
                    fileItem.setName(document.getName());
                    if (document.getDownloadUrl() != null && !"".equals(document.getDownloadUrl())) {
                        fileItem.setGoogleDownloadLink(document.getDownloadUrl().replace(" ", "%20"));
                    }
                    fileItem.setDocumentID(document.getId());
                    fileItem.setDocumentOpenID(document.geteTag());
                    fileItem.setDescription(document.getId());
                    if (OFFICE_365.equals(storageType)) {
                        fileItem.setUploadType(CommandConstants.LINK_TO_OFFICE_365_DOCS_PARAM_NAME);
                    } else if (OFFICE_365_SHARE_POINT.equals(storageType)) {
                        fileItem.setUploadType(CommandConstants.LINK_TO_OFFICE_365_SHARE_POINT_DOCS_PARAM_NAME);
                    }
                    fileItem.setGoogleOrOffice365Id(document.getId());
                    fileList.add(fileItem);
                } else if (document.getFolder() != null && OFFICE_365.equals(storageType) && document.getFolder().getChildCount() > 0) {
                    getSubFolderFiles(document.getId(), fileList, tokenDTO, storageType);
                } else if (document.getFolder() != null && OFFICE_365_SHARE_POINT.equals(storageType)) {
                    getSubFolderFiles(document.getId(), fileList, tokenDTO, storageType);
                }
            }
        }
    }

    public ArrayList<TreeSelectItem> getUsersAllSubFoldersInKpiRoot(String root, String driveType) {
        try {
            if (GOOGLE.equals(driveType)) {
                return googleDocumentsManager.getAllSubFoldersInKpiRoot(root);
            } else if (OFFICE_365.equals(driveType) || OFFICE_365_SHARE_POINT.equals(driveType)) {
                return getOfficeFolderTree(driveType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public FolderResource getTempFolderByCompany(Integer companyID) {
        companyID = companyID == null ? SecurityContext.getCompanyID() : companyID;
        log.info("Company ID: {}", companyID);
        log.info("DATABASE: {}", ServerSecurityContext.getInstance().getDatabase());
        EdsFolder tmpFolder = folderManager.getTempFolder(companyID);
        FolderResource folderResource = new FolderResource();
        folderResource.setName(tmpFolder.getName());
        folderResource.setObjectId(tmpFolder.getObjectID());
        folderResource.setPermission(folderRbacManager.getFolderEntryForUser2(tmpFolder, userManager.getUser()).getDTO());

        return folderResource;
    }

    private ArrayList<TreeSelectItem> getOfficeFolderTree(String storageType) {
        ArrayList<TreeSelectItem> result = new ArrayList<>();
        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), storageType);
        if (tokenDTO != null) {
            Office365ResourceCollection<Office365DriveItem> folders = office365DriveService.listRootChildren(tokenDTO, storageType);

            if (folders != null && (folders.getValue() != null && folders.getValue().size() > 0)) {
                for (Office365DriveItem item : folders.getValue()) {
                    String savedTitle = "";
                    if (Constants.OFFICE_365_SHARE_POINT.equals(storageType)) {
                        savedTitle = hasThisUrlInSettings(item.getId());
                    }
                    if (item.getFolder() != null && savedTitle != null) {
                        TreeSelectItem rootFolder = new TreeSelectItem();
                        rootFolder.setName(savedTitle.isEmpty() ? item.getName() : savedTitle);
                        rootFolder.setDescription(item.getId());
                        rootFolder.setShowInDropDown(true);
                        rootFolder.setSelected(false);

                        if (!Constants.OFFICE_365_SHARE_POINT.equals(storageType)) {
                            initializeSubFolders(item.getId(), rootFolder, tokenDTO, storageType);
                        }

                        result.add(rootFolder);
                    }
                }
            }
            return result;
        }
        return null;
    }

    private void initializeSubFolders(String root, TreeSelectItem parent, Office365AccessTokenDTO tokenDTO, String storageType) {
        Office365ResourceCollection<Office365DriveItem> folders = office365DriveService.listFolderChildren(root, tokenDTO, storageType, false);
        if (folders != null && (folders.getValue() != null && !folders.getValue().isEmpty())) {
            for (Office365DriveItem folderitem : folders.getValue()) {
                if (folderitem.getFolder() != null) {
                    TreeSelectItem item = new TreeSelectItem();
                    item.setName(folderitem.getName());
                    item.setDescription(folderitem.getId());
                    item.setShowInDropDown(true);
                    item.setParent(parent);
                    parent.getChildren().add(item);
                    initializeSubFolders(folderitem.getId(), item, tokenDTO, storageType);
                }
            }
        }
    }

    private String hasThisUrlInSettings(String itemId) {
        EdsCompanySettings comS = userManager.getUser().getCompany().getCompanySettings();
        if (comS != null && comS.getSharePointSiteUrls() != null && !"".equals(comS.getSharePointSiteUrls())) {
            String[] savedURLs = comS.getSharePointSiteUrls().split("_&_");
            for (String tileWithUrl : savedURLs) {
                String[] titleAndUrl = tileWithUrl.split("_@_");
                if (titleAndUrl[1].contains(itemId)) {
                    return titleAndUrl[0];
                }
            }
        }
        return null;
    }

    private ArrayList<FileResource> linkToSinxDocs(FileResource[] fileResources, FolderResource folder, String description, String storageType) {
        ArrayList<FileResource> result = new ArrayList<>();
        if (fileResources != null) {
            for (FileResource fileResource : fileResources) {
                String fileName = fileResource.getEncodedName();
                try {
                    int folderTypeID = folder.getFileType() > 0 ? folder.getFileType() : EdsFileHeader.F_DEFAULT;

                    EdsSinxDocuments googleDocuments = googleDocumentsManager.getGoogleDocuments(googleDocumentsManager.getUser(), true);
                    if (googleDocuments == null) {
                        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), storageType);
                        googleDocuments = office365DriveService.createSixDocumentData(googleDocumentsManager.getUser(), true, tokenDTO);
                    }
                    String[] fileContentTypeAndExtension = null;
                    if (GOOGLE.equals(storageType)) {
                        fileContentTypeAndExtension = googleDocumentsManager.getDocumentParameters(fileResource.getDescription());
                    } else if (OFFICE_365.equals(storageType) || OFFICE_365_SHARE_POINT.equals(storageType)) {
                        fileContentTypeAndExtension = getOffice365DocumentParameters(fileResource.getDescription(), storageType);
                    }
                    if (fileContentTypeAndExtension != null) {
                        int fileSize = fileContentTypeAndExtension[3] != null ? Integer.valueOf(fileContentTypeAndExtension[3]) : 0;
                        EdsUser owner = googleDocumentsManager.getUser();
                        EdsFolder parent = folderManager.get(folder.getObjectId());
                        EdsFileHeader file = new EdsFileHeader();
                        if (folderManager.existsFile(folder.getObjectId(), fileName)) {
                            fileName = fileName + "_" + new Date().getTime();
                        }
                        file.setName(fileName);
                        System.out.println(fileName);
                        parent.addFile(file);
                        file.setOwner(parent.getOwner());

                        Date now = new Date();
                        EdsAuditInfo auditInfo = new EdsAuditInfo();
                        auditInfo.setCreatedBy(owner);
                        auditInfo.setCreationDate(now);
                        auditInfo.setModifiedBy(owner);
                        auditInfo.setModificationDate(now);
                        file.setAuditInfo(auditInfo);
                        file.setVersioned(false);

                        EdsFileBody body = new EdsFileBody();
                        body.setContentType(fileContentTypeAndExtension[0]);
                        body.setAuditInfo(auditInfo);
                        body.setDescription(ADD_DESCRIPTION.equals(description) ? "" : description);

                        body.setFileSize(fileSize);
                        body.setOriginalName(fileName);
                        if (GOOGLE.equals(storageType)) {
                            body.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.GOOGLE));
                        } else if (OFFICE_365.equals(storageType)) {
                            body.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.OFFICE_365));
                        } else if (OFFICE_365_SHARE_POINT.equals(storageType)) {
                            body.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, Constants.OFFICE_365_SHARE_POINT));
                        }

                        if (!file.isVersioned() && file.getCurrentBody() != null) {
                            file.setCurrentBody(null);
                            if (file.getBodies() != null) {
                                Iterator<EdsFileBody> it = file.getBodies().iterator();
                                while (it.hasNext()) {
                                    EdsFileBody bo = it.next();
                                    it.remove();
                                    fileBodyManager.delete(bo);
                                }
                            }
                        }

                        file.addBody(body);
                        file.setAuditInfo(auditInfo);
                        file.setFileType(folderTypeID);
                        file.setEntityId(folder.getEntityId());

                        fileBodyManager.createUpload(body);
                        folderRbacManager.indexFile(file);
                        try {
                            setFolderPermissionsToFile(file, new ArrayList<>(getFolderPermissions(parent.getObjectID())), false, owner);
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            e.printStackTrace();
                        }

                        EdsFolder tempFolder = folderManager.getTempFolder(owner.getCompany().getObjectID());
                        if (tempFolder != null && !tempFolder.getObjectID().equals(file.getFolder().getObjectID())) {
                            if (folderTypeID == Constants.F_TASK) {
                                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.TASK_FILE_ADD, file, owner);
                            } else {
                                if (folderTypeID == Constants.F_PROJECT) {
                                    baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.PROJECT_FILE_ADD, file, owner);
                                } else {
                                    if (folderTypeID == Constants.F_PR_ISSUE) {
                                        baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.ISSUE_FILE_ADD, file, owner);
                                    }
                                }
                            }
                        }
                        EdsSinxDocumentsSettings googleDocumentsSettings = new EdsSinxDocumentsSettings();
                        googleDocumentsSettings.setDocumentID(fileResource.getDocumentID());
                        if (StringUtils.isNotBlank(fileResource.getDocumentOpenID())) {
                            if (fileResource.getDocumentOpenID().indexOf("{") > -1 && fileResource.getDocumentOpenID().indexOf("}") > -1) {
                                googleDocumentsSettings.setDocumentOpenID(fileResource.getDocumentOpenID().substring(fileResource.getDocumentOpenID().indexOf("{") + 1, fileResource.getDocumentOpenID().indexOf("}")));
                            } else {
                                googleDocumentsSettings.setDocumentOpenID(fileResource.getDocumentOpenID());
                            }
                        }
                        googleDocumentsSettings.setDocumentLink(fileContentTypeAndExtension[2] != null ? fileContentTypeAndExtension[2] : fileResource.getGoogleDownloadLink());
                        googleDocumentsSettings.setSinxDocuments(googleDocuments);
                        googleDocumentsSettings.setUpload(body);
                        sinxDocumentsSettingsManager.create(googleDocumentsSettings);
                        result.add(file.getDTO());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private String[] getOffice365DocumentParameters(String description, String storageType) {
        String contentType;
        String extension;
        String downloadUrl;
        long fileSize;


        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), storageType);
        Office365DriveItem document = null;
        if (tokenDTO != null) {
            document = office365DriveService.getItem(description, tokenDTO, storageType);
        }

        if (document != null) {
            downloadUrl = document.getWebUrl();
            fileSize = document.getSize();
            String docType = document.getName();
            extension = document.getName();
            String filename = document.getName();
            contentType = document.getName();
            if ("spreadsheet".equals(docType)) {//TODO Utilsga chiqarish kk buni.
                contentType = "application/vnd.ms-excel";
            } else if ("document".equals(docType) || "file".equals(docType)) {
                if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                    contentType = "image/jpeg";
                } else if (filename.endsWith(".png")) {
                    contentType = "image/png";
                } else if (filename.endsWith(".bmp")) {
                    contentType = "image/bmp";
                } else if (filename.endsWith(".gif")) {
                    contentType = "image/gif";
                } else if (filename.endsWith(".html") || filename.endsWith(".htm")) {
                    contentType = "text/html";
                } else if (filename.endsWith(".sxw")) {
                    contentType = "application/vnd.sun.xml.writer";
                } else if (filename.endsWith(".rtf")) {
                    contentType = "application/rtf";
                } else if (filename.endsWith(".pps") || filename.endsWith(".ppt")) {
                    contentType = "application/vnd.ms-powerpoint";
                } else if (filename.endsWith(".pptx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
                } else if (filename.endsWith(".csv")) {
                    contentType = "text/csv";
                } else if (filename.endsWith(".tsv") || filename.endsWith(".tab")) {
                    contentType = "text/tab-separated-values";
                } else if (filename.endsWith(".swf")) {
                    contentType = "application/x-shockwave-flash";
                } else if (filename.endsWith(".txt")) {
                    contentType = "text/plain";
                } else if (filename.endsWith(".doc")) {
                    contentType = "application/msword";
                } else if (filename.endsWith(".docx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                } else if (filename.endsWith(".xls")) {
                    contentType = "application/vnd.ms-excel";
                } else if (filename.endsWith(".xlsx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                } else if (filename.endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else if (filename.endsWith(".pages")) {
                    contentType = "application/vnd.apple.pages";
                } else if (filename.endsWith(".odt")) {
                    contentType = "application/vnd.oasis.opendocument.text";
                } else if (filename.endsWith(".ods")) {
                    contentType = "application/vnd.oasis.opendocument.spreadsheet";
                } else if (filename.endsWith(".ai")) {
                    contentType = "application/postscript";
                } else if (filename.endsWith(".psd")) {
                    contentType = "application/octet-stream";
                } else if (filename.endsWith(".tiff")) {
                    contentType = "image/tiff";
                } else if (filename.endsWith(".dxf")) {
                    contentType = "application/dxf";
                } else if (filename.endsWith(".svg")) {
                    contentType = "image/svg+xml";
                } else if (filename.endsWith(".eps") || filename.endsWith(".ps")) {
                    contentType = "application/postscript";
                } else if (filename.endsWith(".otf")) {
                    contentType = "application/vnd.oasis.opendocument.formula-template";
                } else if (filename.endsWith(".ttf")) {
                    contentType = "application/x-font-ttf";
                } else if (filename.endsWith(".xps")) {
                    contentType = "application/vnd.ms-xpsdocument";
                } else if (filename.endsWith(".zip")) {
                    contentType = "application/zip";
                } else if (filename.endsWith(".rar")) {
                    contentType = "application/rar";
                } else if (filename.contains("json")) {
                    contentType = "application/json";
                }
            } else if ("presentation".equals(docType)) {
                contentType = "application/vnd.ms-powerpoint";
            } else if ("drawing".equals(docType)) {
                contentType = "image/png";
            }
            String[] result = new String[4];
            result[0] = contentType;                // file content type
            result[1] = extension;                  // file extension: doc, xls, pdf,...
            result[2] = downloadUrl;                // file download URL address
            result[3] = String.valueOf(fileSize);   // file size
            return result;
        }
        return null;
    }

    public ArrayList<FileResource> linkToKpiDocs(FileResource[] fileResources, FolderResource folder) {
        ArrayList<FileResource> result = new ArrayList<>();
        EdsUser owner = folderManager.getUser();
        if (fileResources != null) {
            for (FileResource fileResource : fileResources) {
                try {
                    int folderTypeID = folder.getFileType() > 0 ? folder.getFileType() : EdsFileHeader.F_DEFAULT;

                    EdsFolder parent = folderManager.get(folder.getObjectId());

                    EdsFileHeader oldFile = fileHeaderManager.getFile(fileResource.getFolderId(), fileResource.getEncodedName());
                    FileResource fileResourceResult;
                    if (oldFile != null) {
                        EdsCopiedFileHeader file = new EdsCopiedFileHeader();
                        file.setName(fileResource.getEncodedName());
                        if (copiedFileHeaderManager.existsFile(folder.getObjectId(), fileResource.getEncodedName())) {
                            file.setName(getFileName(fileResource.getEncodedName()));
                        }
                        file.setOwner(parent.getOwner());

                        Date now = new Date();
                        EdsAuditInfo auditInfo = new EdsAuditInfo();
                        auditInfo.setCreatedBy(owner);
                        auditInfo.setCreationDate(now);
                        auditInfo.setModifiedBy(owner);
                        auditInfo.setModificationDate(now);
                        file.setAuditInfo(auditInfo);

                        file.setFileType(folderTypeID);
                        file.setFolder(parent);
                        file.setFileHeaderId(oldFile.getObjectID());
                        file.setEntityId(folder.getEntityId());

                        copiedFileHeaderManager.create(file);

                        EdsFolder tempFolder = folderManager.getTempFolder(owner.getCompany().getObjectID());
                        if (tempFolder != null && !tempFolder.getObjectID().equals(file.getFolder().getObjectID())) {
                            if (folderTypeID == Constants.F_TASK) {
                                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.TASK_FILE_ADD, oldFile, owner);
                            } else if (folderTypeID == Constants.F_PROJECT) {
                                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.PROJECT_FILE_ADD, oldFile, owner);
                            } else if (folderTypeID == Constants.F_PR_ISSUE) {
                                baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.ISSUE_FILE_ADD, oldFile, owner);
                            }

                        }
                        fileResourceResult = oldFile.getDTO();
                        if (oldFile != null && oldFile.getCurrentBody() != null) {
                            fileResourceResult.setAmazonLink(commonServiceLocal.getFileUrl(oldFile.getCurrentBody().getObjectID()));
                        }
                        result.add(fileResourceResult);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<FileResource> listFilesAndFoldersForPopup(ListingFilterParameter fp) {
        ListResult<FileResource> result = new ListResult<>(new ArrayList<>(), 0);
        List<EdsFileHeader> allFiles = new ArrayList<>();
        ArrayList<FileResource> filesFolderList = new ArrayList<>();
        final EdsUser user = userManager.getUser() == null ? (fp != null && fp.getUserID() != null ? userManager.get(fp.getUserID()) : null) : userManager.getUser();
        KpiLog kpiLog = new KpiLog();
        kpiLog.setUsername(user.getFullName());

        if (fp.isSystemSubFolder() && fp.getFolderId() != null && fp.getFolderId() > 0) {
            try {
                result = listFile(fp);
                EdsFolder currentFolder = folderManager.get(fp.getFolderId());
                List<EdsFolder> subFolders = currentFolder.getSubfolders();
                if (subFolders != null) {
                    for (EdsFolder folder : subFolders) {
                        fp.setFolderId(folder.getObjectID());
                        result.getList().addAll(listFile(fp).getList());
                    }
                }
                return result;

            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        } else if (fp.isTrashResource()) {
            try {
                List<EdsFolder> deletedRootFolders = folderManager.getDeletedRootFolders(user.getObjectID());
                if (deletedRootFolders != null && !deletedRootFolders.isEmpty()) {
                    for (EdsFolder folder : deletedRootFolders) {
                        allFiles.addAll(fileHeaderManager.getFiles(folder.getObjectID(), user, true));
                    }
                }
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        } else if (fp.isSharedResource()) {
            List<EdsFolder> sharedRootFolders = folderManager.getSharedRootFolders(user.getObjectID());
            if (sharedRootFolders != null && !sharedRootFolders.isEmpty()) {
                for (EdsFolder folder : sharedRootFolders) {
                    allFiles.addAll(fileHeaderManager.getFiles(folder.getObjectID(), user, true));
                }
            }
        } else if (fp.isOtherResource()) {
            try {
                List<EdsUser> users = getUsersSharingFoldersForUser(user);
                for (EdsUser sharedUser : users) {
                    List<EdsFolder> sharedRootFolders = folderManager.getSharedRootFolders(sharedUser.getObjectID(), user.getObjectID());
                    if (sharedRootFolders != null && !sharedRootFolders.isEmpty()) {
                        for (EdsFolder folder : sharedRootFolders) {
                            allFiles.addAll(fileHeaderManager.getFiles(folder.getObjectID(), user, true));
                        }
                    }
                }
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
        } else if (fp.isOtherSharedResource()) {
            EdsUser cUser = userManager.get(fp.getUserID());
            List<EdsFolder> folders = folderManager.getSharedRootFolders(cUser.getObjectID(), user.getObjectID());
            if (folders != null && !folders.isEmpty()) {
                for (EdsFolder folder : folders) {
                    allFiles.addAll(fileHeaderManager.getFiles(folder.getObjectID(), user, true));
                }
            }
        } else {
            if (fp.getFolderId() != null) {
                EdsFolder rootFolder = folderManager.get(fp.getFolderId());
                if (rootFolder != null) {
                    allFiles.addAll(fileHeaderManager.getFiles(rootFolder.getObjectID(), user, true));
                }
            } else {
                try {
                    fp.setAllFilesResource(true);
                    return listFile(fp);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!allFiles.isEmpty()) {
            int size = allFiles.size();
            if (allFiles.size() >= fp.getStart()) {
                if (fp.getStart() + fp.getLimit() < size) {
                    allFiles = allFiles.subList(fp.getStart(), fp.getStart() + fp.getLimit());
                } else {
                    allFiles = allFiles.subList(fp.getStart(), allFiles.size());
                }
            }
            for (EdsFileHeader file : allFiles) {
                FileResource fDTO = new FileResource();
                fDTO.setObjectId(file.getObjectID());
                fDTO.setFolderId(file.getFolder().getObjectID());
                fDTO.setName(file.getName());
                fDTO.setFolder(false);
                fDTO.setFolderResource(file.getFolder().getDTO());
                if (file.getCurrentBody() != null) {
                    fDTO.setBodyId(file.getCurrentBody().getObjectID());
                    fDTO.setContentType(file.getCurrentBody().getContentType());
                    fDTO.setContentLength(file.getCurrentBody().getFileSize());
                }
                filesFolderList.add(fDTO);
            }
            filesFolderList.sort(fileResourcesComparatorFactory.createComparator(ASC));
            result = new ListResult<>(filesFolderList, size);
        }
        return result;
    }

    public ArrayList<FileResource> searchDocument(Integer folderID, String documentName) {
        ArrayList<FileResource> result = new ArrayList<>();
        List<EdsFileHeader> files = fileHeaderManager.searchFiles(folderID, documentName);
        if (files != null && !files.isEmpty()) {
            for (EdsFileHeader file : files) {
                FileResource fDTO = new FileResource();
                fDTO.setObjectId(file.getObjectID());
                fDTO.setFolderId(file.getFolder().getObjectID());
                fDTO.setName(file.getName());
                fDTO.setFolder(false);
                fDTO.setBodyId(file.getCurrentBody().getObjectID());
                fDTO.setFolderResource(file.getFolder().getDTO());
                fDTO.setContentType(file.getCurrentBody().getContentType());
                result.add(fDTO);
            }
            result.sort(fileResourcesComparatorFactory.createComparator(ASC));
        }
        return result;
    }

    @Transactional
    public ArrayList<FileResource> saveXhrFile(ArrayList<FileResource> files, FolderResource folder, String description) {
        ArrayList<FileResource> result = new ArrayList<>();
        ArrayList<File> uploadedFiles = new ArrayList<>();
        if (files != null && !files.isEmpty()) {

            EdsFolder parent = folderManager.get(folder.getObjectId());
            PermissionHolder permission = folder.getPermission();

            EdsUser owner = null;
            if (Constants.F_EMPLOYEE_PROFILE == folder.getFileType()) {
                owner = userManager.get(folder.getEntityId());
            }
            if (owner == null) {
                owner = userManager.getUser();
            }

            if (permission == null) {
                EdsDocumentPermission documentPermission = folderRbacManager.getFolderPermissionForUser(parent, owner);
                if (documentPermission != null) {
                    permission = documentPermission.getDTO();
                }
            }
            //T4056
            if (Constants.F_EMPLOYEE_PROFILE == folder.getFileType() || Constants.F_OPPORTUNITY == folder.getFileType()) {
                permission.setWrite(true);
            }

            for (FileResource tmpFile : files) {
                if (tmpFile != null) {
                    InputStream inputStream = null;
                    File file = null;
                    //Done for converting existing images of company news
                    if (tmpFile.getEntityID() != null) {
                        EdsUpload upload = (EdsUpload) uploadManager.get(tmpFile.getEntityID());
                        inputStream = uploadManager.getInputStream(upload);
                    } else {
                        file = new File(GwtUploadServlet.realPath + tmpFile.getEncodedName().replace("\\+", "").replace("\"", ""));
                        try {
                            inputStream = new FileInputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    String[] _fileName = tmpFile.getEncodedName().split("_upld_");
                    String fileName = _fileName[_fileName.length - 1].replace("\\+", "").replace("\"", "");
                    String saveTo = tmpFile.getUploadType();
                    DocumentItem fileBody = new DocumentItem();
                    fileBody.setInputStream(inputStream);
                    fileBody.setContentType(DocumentsServiceImpl.identifyMimeType(fileName));
                    fileBody.setName(fileName);
                    fileBody.setFolderId(folder.getObjectId());
                    fileBody.setDescription(description);
                    fileBody.setDriveFolderId(folder.getDriveFolderId());
                    fileBody.setDriveFolderName(folder.getDriveFolderName());
                    fileBody.setDuration(folder.getDuration());
                    fileBody.setDownloadable(tmpFile.isDownloadable());
                    if (file != null) {
                        uploadedFiles.add(file);
                    }
                    FileResource fileResource;
                    try {
                        fileResource = createFile(owner, fileBody, saveTo, folder.getFileType(), folder.getEntityId(), permission);
                        result.add(fileResource);
                    } catch (DuplicateNameException | ObjectNotFoundException | QuotaExceededException |
                             InsufficientPermissionsException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!uploadedFiles.isEmpty()) {
            for (File file : uploadedFiles) {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public FileResource saveDocumentFile(MultipartFile multipartFile, String uploadType,
                                         Integer folderID, Integer folderType,
                                         Integer entityID, String description) {
        return saveDocumentFile(multipartFile, uploadType, folderID, folderType, entityID, description, true);
    }

    public FileResource saveDocumentFile(MultipartFile multipartFile,
                                         Integer folderID, Integer folderType,
                                         Integer entityID, String description) {
        return saveDocumentFile(multipartFile, EdsContextParams.getUploadType(), folderID, folderType, entityID, description, false);
    }

    /**
     * Saves uploaded file and associates it with entity
     * parameter value.
     *
     * @param multipartFile  uploaded multipart file
     * @param folderID
     * @param folderType     type of folder (e.g. Constants.F_LEAVE_REQUEST, Constants.F_LEAD etc)
     * @param uploadType     upload type, available values are (AMAZON, LOCAL, KPI_STORAGE, GOOGLE, OFFICE_365, OFFICE_365_SHARE_POINT)
     * @param entityID       id of entity e.g. it can be leadid, contactid etc
     * @param description    file description
     * @param isDownloadable This field is to determine if we should add Content-desposition metadata or not
     *                       if Content-desposition is set then browsers will not display but rather will download file
     */
    private FileResource saveDocumentFile(MultipartFile multipartFile, String uploadType,
                                          Integer folderID, Integer folderType,
                                          Integer entityID, String description, boolean isDownloadable) {
        //Get Folder where to upload
        FolderResource folder;
        if (folderID != null) {
            folder = getFolderResource(folderID);
        } else {
            folder = getFolderResource(folderType, entityID);
        }

        if (multipartFile != null && folder != null) {
            EdsUser owner = null;
            if (Constants.F_EMPLOYEE_PROFILE == folder.getFileType()) {
                owner = userManager.get(folder.getEntityId());
            }
            owner = owner != null ? owner : userManager.getUser();
            //Get DB object of parent
            EdsFolder parent = folderManager.get(folder.getObjectId());

            PermissionHolder permission = folder.getPermission();
            if (permission == null) {
                EdsDocumentPermission documentPermission = folderRbacManager.getFolderEntryForUser2(parent, owner);
                if (documentPermission != null) {
                    permission = documentPermission.getDTO();
                }
            }

            try {
                DocumentItem fileBody = new DocumentItem();
                fileBody.setInputStream(multipartFile.getInputStream());
                fileBody.setContentType(DocumentsServiceImpl.identifyMimeType(multipartFile.getOriginalFilename()));
                fileBody.setName(multipartFile.getOriginalFilename());
                fileBody.setFolderId(folder.getObjectId());
                fileBody.setDescription(description);
                fileBody.setDriveFolderId(folder.getDriveFolderId());
                fileBody.setDriveFolderName(folder.getDriveFolderName());
                fileBody.setDuration(folder.getDuration());
                fileBody.setDownloadable(isDownloadable);
                try {
                    return createFile(owner, fileBody, uploadType, folder.getFileType(), entityID, permission);
                } catch (DuplicateNameException | ObjectNotFoundException | QuotaExceededException |
                         InsufficientPermissionsException e) {
                    log.error("", e);
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }

        return null;
    }

    public ArrayList<FileResource> uploadAllFiles(ArrayList<FileResource> files, FolderResource folder, String description) {
        return uploadAllFiles(files, null, folder, description);
    }
    public ArrayList<FileResource> uploadAllFiles(ArrayList<FileResource> files, ArrayList<FileResource> kpiFiles, FolderResource folder, String description) {

        long start = System.currentTimeMillis();
        ArrayList<FileResource> result = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            result.addAll(saveXhrFile(files, folder, description));
        }
        if (kpiFiles != null && !kpiFiles.isEmpty()) {
            result.addAll(linkToKpiDocs(kpiFiles.toArray(new FileResource[]{}), folder));
        }
        if (folder != null && Constants.F_OPPORTUNITY == folder.getFileType() && folder.getEntityId() != null) {
            EdsOpportunity edsOpportunity = opportunityManager.get(folder.getEntityId());
            if (edsOpportunity != null) {
                try {
                    solrManager.addOpportunityToIndex(edsOpportunity);
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Upload file took: " + (System.currentTimeMillis() - start));
        return result;
    }

    @Transactional(propagation = Propagation.NEVER)
    public String copyUploadDocumentSize(Integer companyID) {
        SecurityContext.getInstance().setCompanyId(companyID);
        return String.valueOf(uploadManager.copyCompanyDocumentsSizeToUploadTable(companyID));
    }

    @Transactional
    public Boolean isIndexedUploadDocument() {
        Boolean isReindex = companyManager.getUser().getCompany().getCompanySettings().getIndexedDocumentUpload();
        return isReindex != null ? isReindex : Boolean.FALSE;
    }

    public void saveFileDescription(Integer fileBodyId, String description) {
        if (fileBodyId != null) {
            EdsFileBody item = fileBodyManager.get(fileBodyId);
            if (item != null) {
                item.setDescription(description);
                EdsUser user = userManager.getUser();
                fileBodyManager.createOrUpdate(item);
                try {
                    folderSolrComponent.index(item.getHeader());
                } catch (InterruptedException e) {
                    baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_ADD, item.getHeader(), user);
                }
            }
        }
    }

    @Override
    public HashMap<Integer, ArrayList<SelectItem>> getDocumentTypes(String typeCode) {
        HashMap<Integer, ArrayList<SelectItem>> types = new HashMap<>();
        types.put(Constants.F_EMPLOYEE_PROFILE, new ArrayList<>());
        types.put(Constants.F_COMPANY_DOCUMENTS, new ArrayList<>());
        List<EdsReference> employeeDocTypes = referenceManager.listReferences(EdsFileHeader._DOCUMENT_TYPES);
        if (employeeDocTypes != null) {
            for (EdsReference reference : employeeDocTypes) {
                reference.setName(commonLocalizer.localize(reference.getCode(), reference.getName()));
                if (typeCode != null && !"".equals(typeCode)) {
                    if (reference.getCode().equals(typeCode)) {
                        types.get(F_EMPLOYEE_PROFILE).add(new SelectItem(reference.getObjectID(), reference.getName(), reference.getCode()));
                    }
                } else {
                    types.get(F_EMPLOYEE_PROFILE).add(new SelectItem(reference.getObjectID(), reference.getName(), reference.getCode()));
                }
            }
        }
        List<EdsReference> companyDocTypes = referenceManager.listReferences(EdsFileHeader._COMPANY_DOCUMENT_TYPES);
        if (companyDocTypes != null) {
            for (EdsReference reference : companyDocTypes) {
                reference.setName(commonLocalizer.localize(reference.getCode(), reference.getName()));
                if (typeCode != null && !"".equals(typeCode)) {
                    if (reference.getCode().equals(typeCode)) {
                        types.get(F_COMPANY_DOCUMENTS).add(new SelectItem(reference.getObjectID(), reference.getName(), reference.getCode()));
                    }
                } else {
                    types.get(F_COMPANY_DOCUMENTS).add(new SelectItem(reference.getObjectID(), reference.getName(), reference.getCode()));
                }
            }
        }
        return types;
    }

    @Override
    public void updateFiles(ArrayList<FileResource> items, Integer entityID, String typeCode) {
        List<EdsFileHeader> files = new ArrayList<>();
        EdsUser user = userManager.getUser();
        if (items != null && !items.isEmpty()) {
            for (FileResource item : items) {
                EdsFileHeader file = fileHeaderManager.get(item.getObjectId());
                if (file == null) {
                    continue;
                }
                EdsReference fileType = null;
                file.setEntityId(entityID);
                if (entityID != null) {
                    file.setEnetityUser(userManager.get(entityID));
                }
                file.setDocumentName(item.getDocumentName() != null ? item.getDocumentName() : item.getFileName());
                file.getCurrentBody().setDescription(item.getDescription());
                file.setIssuedDate(item.getIssuedDate() != null && item.getIssuedDate().getDate() != null ? item.getIssuedDate().getNonConvertedDate() : null);
                file.setExpireDate(item.getExpireDate() != null && item.getExpireDate().getDate() != null ? item.getExpireDate().getNonConvertedDate() : null);
                file.setDocumentID(item.getDocumentID());
                file.setReminderId(item.getReminderId());
                file.setReminderName(item.getReminderName());
                EdsInsuranceDocument insDocument = file.getInsuranceDocument();
                if (insDocument == null) {
                    insDocument = new EdsInsuranceDocument();
                }
                insDocument.setInsureeName(item.getInsureeName());
                insDocument.setInsureeLastName(item.getInsureeLastName());
                insDocument.setStatusId(item.getStatusId());
                insDocument.setInsuranceCost(item.getInsuranceCost());
                insDocument.setInsurancePlan(item.getInsurancePlan());
                insDocument.setInsuranceCoverage(item.getInsuranceCoverage());
                fileHeaderManager.persist(insDocument);
                file.setInsuranceDocument(insDocument);
                createDocumentRecurring(item);
                if (item.getTypeId() != null) {
                    fileType = referenceManager.get(item.getTypeId());
                } else if (typeCode != null && !"".equals(typeCode)) {
                    fileType = referenceManager.findReference(EdsFileHeader._DOCUMENT_TYPES, typeCode);
                }
                file.setDocumentType(fileType);
                fileHeaderManager.update(file);
                files.add(file);
                EdsBusinessEvent employeeDocumentsWorkflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, file, user);
                employeeDocumentsWorkflow.setEntityType(TYPE_EMPLOYEE_DOCUMENTS);
                baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_ADD, file, user);
            }
            try {
//                solrManager.addFileToIndex(files);
                folderSolrComponent.indexes(files);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(user.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Update document list");
        }
    }

    @Override
    public FolderResource getPublicFolder() throws ObjectNotFoundException, InsufficientPermissionsException {
        final EdsUser user = userManager.getUser();
        Locale locale = ServerUtils.getUserLocale();
        EdsCompany company = user.getCompany();
        EdsFolder publicFolder = folderManager.getPublicFolder(company.getObjectID());
        if (publicFolder == null) {
            String companyPublicFolderName = messageSource.getMessage("createSystemFolders.companyPublicFolderName", null, "Public", locale);
            publicFolder = createFolder(companyPublicFolderName, null, user, EdsFolder.SYSTEM_BUILTIN, EdsFolder.F_COMPANY_PUBLIC_ROOT, null);
        }
        if (publicFolder == null) {
            throw new ObjectNotFoundException("Can not found System Folder!");
        }
        return getFolderResource(publicFolder.getObjectID(), user);
    }

    @Override
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeeDocumentsWithTreeInfo(ListingFilterParameter fp, ArrayList<Integer> employeeDocuments) throws ObjectNotFoundException {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> pageList = new LinkedHashMap<>();
        KpiTreeInfo pageInfo = new KpiTreeInfo(1, "Employee Documents");
        ListResult<FileResource> documentList = listFile(fp);
        FileResource[] items = documentList.getList().toArray(new FileResource[]{});
        ArrayList<KpiTreeInfo> listall = getEmployeeDocumentItems(items, employeeDocuments);
        pageList.put(pageInfo, listall);
        return pageList;
    }

    @Override
    public HashMap<String, Boolean> getEnableUploadTypes() {
        return profileService.getEnableUploadTypes();
    }

    @Override
    public String getFileLink(Integer fileBodyID) {
        return commonServiceLocal.getFileUrl(fileBodyID, null, false);
    }

    @Override
    public Double[] getStorageSize() {
        Double[] result = new Double[2];
        result[0] = 0d;
        result[1] = 10d;
        final EdsUser user = userManager.getUser();
        if (user == null) {
            return result;
        }
        EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(user.getCompany());
        if (usagePlan == null) {
            usagePlan = usagePlanManager.getLastUsagePlan(user.getCompany().getObjectID());
        }
        result[0] = usagePlan.getStorage() != null ? usagePlan.getStorage() : 10d;
        result[1] = uploadManager.getUploadStorage();
        return result;
    }

    private ArrayList<KpiTreeInfo> getEmployeeDocumentItems(FileResource[] items, ArrayList<Integer> employeeDocuments) {
        ArrayList<KpiTreeInfo> list = new ArrayList<>();
        KpiTreeInfo treeItem;
        for (FileResource fileResourse : items) {
            String fileName = fileResourse.getFileName().toLowerCase();
            if (fileResourse.getContentType() != null && fileResourse.getContentType().startsWith("image/") ||
                    fileName.endsWith(".jpe") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                    fileName.endsWith(".ico") || fileName.endsWith(".png") || fileName.endsWith(".bmp") || fileName.endsWith(".gif")) {
                treeItem = new KpiTreeInfo();
                treeItem.setDepartmentId(1);
                treeItem.setId(fileResourse.getBodyId());
                if (employeeDocuments.contains(fileResourse.getBodyId())) {
                    treeItem.setSelected(true);
                }
                treeItem.setName(getHtmlName(fileResourse));
                treeItem.setLabel(fileResourse.getFileName());
                treeItem.setSkills(fileResourse.getDescription());
                treeItem.setImageUrl(fileResourse.getAmazonLink());
                list.add(treeItem);
            }
        }
        return list;
    }

    private String getHtmlName(FileResource fileResourse) {
        StringBuilder str = new StringBuilder();
        str.append("<p title=\"" + fileResourse.getDescription() + "\">" + fileResourse.getFileName() + "<br> <span style=\"font-size:10px;\"> ");
        if (!fileResourse.getDescription().isEmpty() && fileResourse.getDescription().length() > 70) {
            str.append(fileResourse.getDescription(), 0, 70);
            str.append("...");
        } else {
            str.append(fileResourse.getDescription());
        }
        str.append("</span></p>");
        return str.toString();
    }

    private void createDocumentRecurring(FileResource item) {
        hrmsServiceLocal.saveEmployeeDocumentReminder(item);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<FileResource> getDocumentList(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFileHeader.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Document list (from solr)");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setCheckNumber(employeeManager.isIntegerEmployeeCodeEnabled());
        FacetFilterRpc docFacetFilter = fp.getFacetFilter();
        if (docFacetFilter != null && !docFacetFilter.isFilterChanges()) {
            docFacetFilter = commonServiceLocal.getUserFacetFilter(docFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        StringBuilder solrQuery = new StringBuilder();
        EdsCompany company = edsUser.getCompany();
        Set<EdsGroup> membershipsGroups = edsUser.getMembershipGroups();
        if (fp.getFolderType() != null) {
            EdsFolder folder = getFolder(fp.getFolderType(), fp.getCrmEntityId(), company);
            if (folder != null) {
                fp.setFolderId(folder.getObjectID());
            }
        }
        if (fp.getViewType() != null && !"".equals(fp.getViewType())) {
            EdsReference employeeDocType = referenceManager.findReference(EdsFileHeader._DOCUMENT_TYPES, fp.getViewType());
            if (employeeDocType != null) {
                fp.setType(employeeDocType.getObjectID());
            }
        }

        solrQuery.append(QueryBuilderForSolr.getDocumentsSolrCore(fp, edsUser, edsUser.getCompany(), membershipsGroups));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(docFacetFilter, edsUser.getCompany(), SolrFolderRepresenter.FIELD_DATE_CREATION, SolrFolderRepresenter.FIELD_DATE_CREATION));
        return getDocumentListResponse(fp, solrQuery.toString());
    }

    /*@Override
    public ArrayList<TreeSelectItem> getUsersAllSubFoldersInKpiRoot(String root) {
        return getUsersAllSubFoldersInKpiRoot(root, GOOGLE);
    }*/

    private ListResult<FileResource> getDocumentListResponse(ListingFilterParameter filterParameter, String solrQuery) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_FOLDER_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getDocumentsSolrQuery(filterParameter, solrQuery), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getDocFromSolrResult(resp, filterParameter);
    }

    private SolrQuery getDocumentsSolrQuery(ListingFilterParameter filter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(filter.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(filter.getLimit()));
        if (!filter.isSearchButton()) {
            if (filter.getSortField() != null) {
                query.setStart(filter.getStart());
                query.setParam(CommonParams.ROWS, String.valueOf(filter.getLimit()));

                String sortField = filter.getSortField();
                boolean isAscending = filter.isAscending();
                SolrQuery.ORDER sort = SolrQuery.ORDER.desc;
                if (isAscending) {
                    sort = SolrQuery.ORDER.asc;
                }
                String sortName;
                if (FileResource.OWNER.equals(sortField)) {
                    sortName = SolrFolderRepresenter.FIELD_OWNER_ID;
                } else if (FileResource.SIZE.equals(sortField)) {
                    sortName = SolrFolderRepresenter.FIELD_SIZE;
                } else if (FileResource.DATE.equals(sortField)) {
                    sortName = SolrFolderRepresenter.FIELD_DATE_MODIFICATION;
                } else if (FileResource.TYPE.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_CONTENT_TYPE;
                } else if (FileResource.EMPLOYEE_NAME.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_EMPLOYEE_NAME;
                } else if (FileResource.DOCUMENT_NAME.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_DOCUMENT_NAME;
                } else if (FileResource.DOCUMENT_DESCRIPTION.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_DOCUMENT_DESCRIPTION;
                } else if (FileResource.DOCUMENT_ID.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_DOCUMENT_ID;
                } else if (FileResource.DOCUMENT_TYPE.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_DOCUMENT_TYPE;
                } else if (FileResource.REMINDER_TYPE.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_REMINDER_NAME;
                } else if (FileResource.ISSUED_DATE.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_ISSUED_DATE;
                } else if (FileResource.EXPIRE_DATE.equals(sortField)) {
                    sortName = FileResource.EXPIRE_DATE;
                } else if (FileResource.CREATEBY.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_CREATED_NAME;
                } else if (FileResource.EMPLOYEE_CODE.equals(sortField)) {
                    sortName = SolrFolderRepresenter.SORTABLE_EMPLOYEE_CODE;
                } else {
                    sortName = SolrFolderRepresenter.SORTABLE_FILE_NAME;
                }
                query.setSort(sortName, sort);
            } else {
                if (filter.isCheckNumber()) {
                    query.setSort(SolrFolderRepresenter.FIELD_ENTITY_USER_INTEGER_NUMBER, SolrQuery.ORDER.asc);
                } else if (filter.getModule() != null && filter.getModule().equals(LayoutRPC.HRMS_SECTION)) {
                    query.setSort(SolrFolderRepresenter.FIELD_FOLDER_ID, SolrQuery.ORDER.desc);
                }
                query.setStart(filter.getStart());
                if (filter.getLimit() > 0) {
                    query.setParam(CommonParams.ROWS, String.valueOf(filter.getLimit()));
                } else {
                    query.setParam(CommonParams.ROWS, "1000");
                }
            }
        }
        return query;
    }

    private ListResult<FileResource> getDocFromSolrResult(QueryResponse resp, ListingFilterParameter filterParameter) {
        ArrayList<FileResource> itemList = new ArrayList<>();
        int totalNumber = 0;
        if (resp != null && resp.getResults() != null) {
            totalNumber = (int) resp.getResults().getNumFound();
            for (SolrDocument relevantDoc : resp.getResults()) {
                FileResource item = new FileResource();
                item.setObjectId(SolrUtils.asInteger(relevantDoc, SolrFolderRepresenter.FIELD_FOLDER_ID));
                item.setEntityID(SolrUtils.asInteger(relevantDoc, SolrFolderRepresenter.FIELD_ENTITY_ID));
                item.setBodyId(SolrUtils.asInteger(relevantDoc, SolrFolderRepresenter.FIELD_BODY_ID));
                item.setEmployeeCode(filterParameter.isCheckNumber() ?
                        String.valueOf(relevantDoc.getFieldValue(SolrFolderRepresenter.FIELD_ENTITY_USER_INTEGER_NUMBER)) :
                        SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_ENTITY_USER_NUMBER));
                item.setEntityName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_ENTITY_USER_NAME));
                item.setOwnerName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_OWNER_NAME));
                item.setContentType(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_CONTENT_TYPE));
                item.setUploadType(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_UPLOAD_TYPE));
                item.setUrlFromSolr(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_DOWNLOAD_URL));
                item.setName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_FOLDER_CONSTANT_NAME));
                item.setFolderName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_FOLDER_NAME));
                item.setDocumentName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_DOCUMENT_NAME));
                item.setDescription(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_FOLDER_DESCRIPTION));
                item.setDocID(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_DOCUMENT_ID));
                item.setDocumentID(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_DOCUMENT_ID));
                item.setType(commonLocalizer.localize(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_DOCUMENT_TYPE), SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_DOCUMENT_TYPE)));
                item.setReminderName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_REMINDER_NAME));
                Date issueDate = SolrUtils.asDate(relevantDoc, SolrFolderRepresenter.FIELD_ISSUED_DATE);
                if (issueDate != null) {
                    item.setIssuedDate(new DateNonConvertable(issueDate));
                }
                Date expireDate = SolrUtils.asDate(relevantDoc, SolrFolderRepresenter.FIELD_EXPIRE_DATE);
                if (expireDate != null) {
                    item.setExpireDate(new DateNonConvertable(expireDate));
                }
                item.setCreationDate(SolrUtils.asDate(relevantDoc, SolrFolderRepresenter.FIELD_DATE_CREATION));
                item.setModificationDate(SolrUtils.asDate(relevantDoc, SolrFolderRepresenter.FIELD_DATE_MODIFICATION));
                item.setCreatedBy(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_CREATED_NAME));
                item.setInsureeName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_INSUREE_NAME));
                item.setInsureeLastName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_INSUREE_LAST_NAME));
                item.setStatusId(SolrUtils.asInteger(relevantDoc, SolrFolderRepresenter.FIELD_INSURANCE_STATUS_ID));
                item.setStatusName(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_INSURANCE_STATUS_NAME));
                item.setInsuranceCost(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_INSURANCE_COST));
                item.setInsurancePlan(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_INSURANCE_PLAN));
                item.setInsuranceCoverage(SolrUtils.asString(relevantDoc, SolrFolderRepresenter.FIELD_INSURANCE_COVARAGE));
                if ("MY_DOCUMENT".equals(filterParameter.getViewType())) { //don't use this more than 10 file
                    item.setAmazonLink(getFileLink(item.getBodyId()));
                    long daysLeft = 0;
                    if (item.getExpireDate() != null && item.getExpireDate().getDate().after(new Date())) {
                        long dateDiff = item.getExpireDate().getDate().getTime() - new Date().getTime();
                        daysLeft = TimeUnit.DAYS.convert(dateDiff, TimeUnit.MILLISECONDS);
                    }
                    item.setDaysLeft(daysLeft);
                }
                itemList.add(item);
            }
        }
        return new ListResult<>(itemList, totalNumber);
    }

    @Override
    public void saveEmployeeProfilePicture(Integer companyID, Integer employeeID) {
        try {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
            log.info("saveEmployeeProfilePicture EmployeeID: {}", employeeID);

            EdsEmployee edsEmployee = employeeManager.get(employeeID);
            log.info("saveEmployeeProfilePicture image URL: {}", edsEmployee.getSocialImageUrl());

            try (InputStream inputStream = new URL(edsEmployee.getSocialImageUrl()).openStream()) {

                String[] fileNameArray = edsEmployee.getSocialImageUrl().split("\\?")[0].split("/");
                String fileName = fileNameArray[fileNameArray.length - 1];
                fileName = com.edatasite.workforce.gwt.documents.client.gwtupload.UUID.uuid() + "_upld_" + fileName;
                String fileNameDecode = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

                if (GwtUploadServlet.realPath == null) {
                    GwtUploadServlet.realPath = servletContext.getRealPath("uploads") + "/";
                }
                String url = GwtUploadServlet.realPath + fileNameDecode;

                File file = new File(url);
                file.getParentFile().mkdirs();

                try (FileOutputStream os = new FileOutputStream(file)) {
                    IOUtils.copy(inputStream, os);
                    os.flush();
                }

                ArrayList<FileResource> files = new ArrayList<>();
                FileResource fileResource = new FileResource();
                fileResource.setName(fileName);
                fileResource.setPath(GwtUploadServlet.realPath + fileName);
                fileResource.setUploadType(EdsContextParams.getUploadType());
                files.add(fileResource);

                FolderResource folderResource = getFolderResource(Constants.F_EMPLOYEE_PROFILE, employeeID);
                folderResource.setEntityId(employeeID);
                ArrayList<FileResource> fileResources = uploadAllFiles(files, null, folderResource, files.get(0).getDescription());
                if (fileResources.size() > 1) {
                    commonServiceLocal.saveImageUrl(fileResources.get(1).getBodyId(), employeeID);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("saveEmployeeProfilePicture: " + e);
        }
    }

    public static class CopyInputStream {
        private final InputStream _is;
        private final ByteArrayOutputStream _copy = new ByteArrayOutputStream();

        /**
         *
         */
        public CopyInputStream(InputStream is) {
            _is = is;

            try {
                copy();
            } catch (IOException ex) {
                // do nothing
            }
        }

        private int copy() throws IOException {
            int read = 0;
            int chunk;
            byte[] data = new byte[256];

            while (-1 != (chunk = _is.read(data))) {
                read += data.length;
                _copy.write(data, 0, chunk);
            }

            return read;
        }

        public InputStream getCopy() {
            return new ByteArrayInputStream(_copy.toByteArray());
        }

    }


}
