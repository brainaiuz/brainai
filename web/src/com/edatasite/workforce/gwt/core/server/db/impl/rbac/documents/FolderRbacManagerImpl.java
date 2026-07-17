package com.edatasite.workforce.gwt.core.server.db.impl.rbac.documents;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.documents.EdsDocumentPermission;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderPolicy;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderRbac;
import com.edatasite.workforce.core.solr.component.FolderSolrComponent;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.RelationshipManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderPolicyManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.FileCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * User: Sherali
 * Date: 29.05.2010
 * Time: 12:57:55
 */
@Repository("folderRbacManager")
public class FolderRbacManagerImpl extends BaseManager<EdsFolderRbac> implements FolderRbacManager {

    @Autowired
    private FolderPolicyManager folderPolicyManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    @Qualifier("companyCFSettingsManager")
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    @Qualifier("documentsService")
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private RelationshipManager relationshipManager;
    @Autowired
    private FolderSolrComponent folderSolrComponent;

    public FolderRbacManagerImpl() {
        super(EdsFolderRbac.class);
    }

    /**
     * Remove folder rbac entries..
     *
     * @param folderId folder id
     * @return
     */
    @Override
    public void removeFolderEntries(Integer folderId) {
        update("DELETE FROM EdsFolderRbac ti WHERE ti.folder.objectID = ? and ti.file<>true", folderId);
    }

    /**
     * Remove file rbac entries..
     *
     * @param fileId file id
     * @return
     */
    @Override
    public void removeFileEntries(Integer fileId) {
        update("DELETE FROM EdsFolderRbac ti WHERE ti.fileHeader.objectID = ? and ti.file=true", fileId);
    }

    /**
     * removes filerbac entries in given id range
     *
     * @param ids
     * @param companyID
     */
    public void removeFileEntriesIdsIn(String ids, Integer companyID) {
        update("DELETE FROM EdsFolderRbac ti WHERE ti.fileHeader.objectID in (" + ids + ") AND ti.file = true");
    }

    @Override
    public void addRbacEntries(EdsFolder folder) {
        //Old indexes should be removed in order to keep table clean without dublication or old entries
        removeFolderEntries(folder.getObjectID());
        EdsFolderPolicy ownerPolicy = folderPolicyManager.getCompanyRelationPolicy(EdsRelationship.DOC_OWNER);
        EdsFolderPolicy readerPolicy = folderPolicyManager.getCompanyRelationPolicy(EdsRelationship.DOC_READER);
        EdsFolderPolicy adminPolicy = folderPolicyManager.getCompanyRelationPolicy(EdsRelationship.DOC_ADMINISTRATOR);

        EdsGroup directorsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.DIRECTORS);
        EdsGroup projectManagersGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.PROJECT_MANAGERS);
        EdsGroup departmentLeadersGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.DEPARTMENT_LEADERS);
        EdsGroup accountantsMenGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.ACCOUNTANTS);

        EdsGroup adminLocationsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.ADMIN_LOCATIONS);
        EdsGroup hrsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.HRS);
        EdsGroup salesMenGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.SALESMEN);
        EdsGroup salesPersonsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.SALESPERSONS);
        EdsGroup customerServiceRepGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.CUSTOMER_SERVICE_REPRESENTATIVES);
        EdsGroup customerServiceManagerGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.CUSTOMER_SERVICE_MANAGER);

        EdsGroup membersGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.MEMBERS);
        EdsGroup clientsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.CLIENTS);

        if (folder.getFolderType() == EdsFolder.F_PROJECT) {
            EdsProject project = projectManager.get(folder.getEntityId());
            List<EdsProjectEmployee> projectEmployees = projectManager.getEmployeesByProject(project.getObjectID());
            List<EdsUser> employees = new ArrayList<>();

            for (EdsProjectEmployee pe : projectEmployees) {
                if (pe.getEmployeeDepartment() != null && pe.getEmployeeDepartment().getEmployee() != null) {
                    employees.add(pe.getEmployeeDepartment().getEmployee());
                    createDirectRelationEntry(folder, pe.getEmployeeDepartment().getEmployee(), EdsRelationship.DOC_READER, readerPolicy.getRelation().getRank(), readerPolicy.getPermission(), null);
                }
            }
            if (project.getManager() != null) {
                employees.add(project.getManager());
                createDirectRelationEntry(folder, project.getManager(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            }
            List<EdsEmployee> backupManagers = project.getBackupManagers();
            for (EdsEmployee backupManager : backupManagers) {
                employees.add(backupManager);
                createDirectRelationEntry(folder, backupManager, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            }
            if (project.getClient() != null) { //for project clients
                List<EdsClientContact> clientContactList = clientContactManager.getAccessEnabledContacts(project.getClient());
                for (EdsClientContact clientContact : clientContactList) {
                    employees.add(clientContact);
                    createDirectRelationEntry(folder, clientContact, EdsRelationship.DOC_READER, readerPolicy.getRelation().getRank(), readerPolicy.getPermission(), null);
                }
            }
            List<EdsFolderPolicy> indirectPolicies = folderPolicyManager.getCompanyIndirectRelationPolicies();
            for (EdsFolderPolicy fPolicy : indirectPolicies) {
                createIndirectRelationEntry(folder, fPolicy.getTrustee(), fPolicy.getRelation().getCode(), fPolicy.getRelation().getRank(), fPolicy.getPermission());
            }
            if (userManager.getUser() != null) {
                EdsFolder rootFolder = folderManager.getProjectRootFolder(userManager.getUser().getCompany());
                addRootFolderPermissionToFile(employees, rootFolder, folder);
            }

        } else if (folder.getFolderType() == EdsFolder.F_TASK || folder.getFolderType() == EdsFolder.F_PR_ISSUE) {
            EdsProject project = projectManager.get(folder.getParent().getEntityId());
            List<EdsUser> employees = new ArrayList<>();
            if (project == null) {
                System.out.println("CompanyId=" + ServerSecurityContext.getInstance().getCompanyId() + " ; Project not found id:" + folder.getParent().getEntityId());
                return;
            }
            if (project.getManager() != null) {
                employees.add(project.getManager());
                createDirectRelationEntry(folder, project.getManager(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            }
            List<EdsEmployee> backupManagers = project.getBackupManagers();
            for (EdsEmployee backupManager : backupManagers) {
                employees.add(backupManager);
                createDirectRelationEntry(folder, backupManager, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            }

            List<EdsProjectEmployee> empTasks = projectManager.getEmployeesByProject(project.getObjectID());
            for (EdsProjectEmployee pe : empTasks) {
                if (pe.getEmployeeDepartment() != null && pe.getEmployeeDepartment().getEmployee() != null) {
                    employees.add(pe.getEmployeeDepartment().getEmployee());
                    createDirectRelationEntry(folder, pe.getEmployeeDepartment().getEmployee(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
                }
            }
            if (project.getClient() != null) { //for project clients
                List<EdsClientContact> clientContactList = clientContactManager.getAccessEnabledContacts(project.getClient());
                for (EdsClientContact clientContact : clientContactList) {
                    employees.add(clientContact);
                    if (!folder.getOwner().getObjectID().equals(clientContact.getObjectID()) && !folder.getAuditInfo().getCreatedBy().getObjectID().equals(clientContact.getObjectID())) {
                        createDirectRelationEntry(folder, clientContact, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
                    }
                }
            }
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());

            if (userManager.getUser() != null) {
                EdsFolder rootFolder = folderManager.getProjectRootFolder(userManager.getUser().getCompany());
                addRootFolderPermissionToFile(employees, rootFolder, folder);
            }
        } else if (folder.getFolderType() == EdsFolder.F_PA || folder.getFolderType() == EdsFolder.F_360 || folder.getFolderType() == EdsFolder.F_PA_ISSUE) {
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, hrsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_DEP_GOAL ||
                folder.getFolderType() == EdsFolder.F_PROJ_GOAL || folder.getFolderType() == EdsFolder.F_BUSS_GOAL ||
                folder.getFolderType() == EdsFolder.F_LEAVE_REQUEST || folder.getFolderType() == EdsFolder.F_INCIDENT ||
                folder.getFolderType() == EdsFolder.F_PAST_EMPLOYMENT || folder.getFolderType() == EdsFolder.F_INTERNAL_EMPLOYMENT ||
                folder.getFolderType() == EdsFolder.F_DEPENDENTS ||
                folder.getFolderType() == EdsFolder.F_CONTRACT) {
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, hrsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, membersGroup, readerPolicy.getRelation().getCode(), readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_COMP_GOAL) {
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_CUSTOM_FIELD_ITEM) {
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);

            if (userManager.getUser() != null) {
                List<EdsUser> employees = new ArrayList<>();
                employees.add(folder.getOwner());
                EdsFolder rootFolder = folderManager.getFolderByFolderType(EdsFolder.F_CUSTOM_FIELD_ROOT);
                addRootFolderPermissionToFile(employees, rootFolder, folder);
            }

            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
//            EdsCompanyCustomFieldsSettings ccfsettings = companyCFManager.get(folder.getEntityId());
        } else if (folder.getFolderType() == EdsFolder.F_LEAD || folder.getFolderType() == EdsFolder.F_CRM_ACCOUNT ||
                folder.getFolderType() == EdsFolder.F_SOLUTION || folder.getFolderType() == EdsFolder.F_CASE ||
                folder.getFolderType() == EdsFolder.F_MASS_MAILING || folder.getFolderType() == EdsFolder.F_OPPORTUNITY) {
            if (folder.getFolderType() == EdsFolder.F_CASE && folder.getEntityId() != null) {
                EdsCase crmCase = caseManager.get(folder.getEntityId());
                if (crmCase != null) {
                    if (crmCase.getAssignee() != null) {
                        createDirectRelationEntry(folder, crmCase.getAssignee(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
                    }
                    if (crmCase.getResolver() != null) {
                        createDirectRelationEntry(folder, crmCase.getResolver(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
                    }
                }
            }
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, salesMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, salesPersonsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, customerServiceRepGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            if (customerServiceManagerGroup != null) {
                createIndirectRelationEntry(folder, customerServiceManagerGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            }
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_VACANCY || folder.getFolderType() == EdsFolder.F_CANDIDATE ||
                folder.getFolderType() == EdsFolder.F_PLACEMENT || folder.getFolderType() == EdsFolder.F_EMAIL_TEMPLATE) {
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, hrsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_CLIENT) {
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, salesMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, salesPersonsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, customerServiceRepGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, accountantsMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_CRM_CONTACT) {
            createIndirectRelationEntry(folder, membersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_SALE_INV || folder.getFolderType() == EdsFolder.F_SALE_QUOTE ||
                folder.getFolderType() == EdsFolder.F_PUR_INV || folder.getFolderType() == EdsFolder.F_PUR_ORDER
                || folder.getFolderType() == EdsFolder.F_BANK_TRANSFER || folder.getFolderType() == EdsFolder.F_BATCH_PAYMENT
                || folder.getFolderType() == EdsFolder.F_PREPAYMENT) {
            createIndirectRelationEntry(folder, accountantsMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            createIndirectRelationEntry(folder, clientsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_AF_ISSUE || folder.getFolderType() == EdsFolder.F_MANUAL_TRANSACTION) {
            createIndirectRelationEntry(folder, accountantsMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_EXP || folder.getFolderType() == EdsFolder.F_EXP_PAYMENT || folder.getFolderType() == EdsFolder.F_EXP_DOC || folder.getFolderType() == EdsFolder.F_PRODUCTS_SERVICES ||
                folder.getFolderType() == EdsFolder.F_STOCK_TRANSFER || EdsFolder.F_STOCK_ADJUSTMENT == folder.getFolderType() || EdsFolder.F_BANK_ACCOUNT == folder.getFolderType() ||
                folder.getFolderType() == EdsFolder.F_EMPLOYEE_PROFILE || folder.getFolderType() == EdsFolder.F_COMPANY_DOCUMENTS || folder.getFolderType() == EdsFolder.F_PERS_GOAL ||
                folder.getFolderType() == EdsFolder.F_EVENT || folder.getFolderType() == EdsFolder.F_MEETING_MINUTES || folder.getFolderType() == EdsFolder.F_RFQ || folder.getFolderType() == EdsFolder.F_TELEGRAM || folder.getFolderType() == EdsFolder.F_WHITE_LABEL_LOGO || folder.getFolderType() == EdsFolder.F_WHITE_LABEL_FAVICON || folder.getFolderType() == EdsFolder.F_WHATSAPP_MEDIA ||  folder.getFolderType() == EdsFolder.F_RFQ_1 || folder.getFolderType() == EdsFolder.F_RFP) {
            createIndirectRelationEntry(folder, membersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_WEBSITE_ROOT || folder.getFolderType() == EdsFolder.F_WEBSITE_BLOCK) {
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_DEFAULT || folder.getFolderType() == EdsFolder.F_PA_ROOT || folder.getFolderType() == EdsFolder.F_AF_ROOT || folder.getFolderType() == EdsFolder.F_WORKSPACE_ROOT || folder.getFolderType() == EdsFolder.F_PROJECT_ROOT || folder.getFolderType() == EdsFolder.F_CUSTOM_FIELD_ROOT || folder.getFolderType() == EdsFolder.F_HRMS_ROOT || folder.getFolderType() == EdsFolder.F_SETTINGS_ROOT) {
            if (folder.getType() != EdsFolder.CUSTOM && folder.getType() != EdsFolder.TEMP) {
                createIndirectRelationEntry(folder, membersGroup, readerPolicy.getRelation().getCode(), readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
            } else if (folder.getType() == EdsFolder.TEMP) {
                createIndirectRelationEntry(folder, membersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                //for all company client contacts group
                createIndirectRelationEntry(folder, clientsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            }
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_CRM_ROOT) {

            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, salesMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, salesPersonsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, customerServiceRepGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());

            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_BACKUPS_ROOT || folder.getFolderType() == EdsFolder.F_XML_BACKUPS_ROOT) {
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_PAYROLL_ROOT || folder.getFolderType() == EdsFolder.F_CASH_ADVANCE || folder.getFolderType() == EdsFolder.F_ADDITIONAL_PAYMENT) {
            createIndirectRelationEntry(folder, accountantsMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_NOTE_ROOT || folder.getFolderType() == EdsFolder.F_NOTE) {
            createIndirectRelationEntry(folder, accountantsMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        } else if (folder.getFolderType() == EdsFolder.F_COMPANY_PUBLIC_ROOT) {
            createIndirectRelationEntry(folder, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, salesMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, salesPersonsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, customerServiceRepGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, accountantsMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, membersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, hrsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, customerServiceManagerGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createIndirectRelationEntry(folder, clientsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            createDirectRelationEntry(folder, folder.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission(), null);
            createIndirectRelationEntry(folder, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
        }
    }

    //This methood for copying root folder user permissions to a new created folder
    private void addRootFolderPermissionToFile(List<EdsUser> users, EdsFolder rootFolder, EdsFolder folder) {
        if (rootFolder != null) {
            List<Integer> rootFolderPermissionUserIds = getRootFolderPermissionUserIds(rootFolder.getObjectID());
            for (Integer userId : rootFolderPermissionUserIds) {
                EdsUser userWithPermission = userManager.get(userId);
                EdsDocumentPermission documentPermission = getFolderPermissionForUser(rootFolder, userWithPermission);

                EdsFolderRbac userRbac = getFolderRbacEntryForUser(rootFolder.getObjectID(), userId);
                if (!users.contains(userWithPermission)) {
                    createDirectRelationEntry(folder, userWithPermission, userRbac.getRelationship(), userRbac.getRelationRank(), documentPermission, null);
                } else {
                    createDirectRelationEntry(folder, userWithPermission, userRbac.getRelationship(), userRbac.getRelationRank(), documentPermission, userRbac);
                }
            }
        }
    }

    private List<Integer> getRootFolderPermissionUserIds(Integer folderID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT fr.userid ");
        sql.append("from " + getCompanyId() + ".folder f ");
        sql.append("left join " + getCompanyId() + ".folderrbac fr on fr.folder_id=f.id ");
        sql.append("left join " + getCompanyId() + ".folderpermission fp on fr.documentpermissionid=fp.id ");
        sql.append("WHERE fr.folder_id=" + folderID + " and  f.deleted <> TRUE and fr.userid IS NOT null");
        return findNative(sql.toString());
    }

    private EdsFolderRbac createDirectRelationEntry(EdsFolder folder, EdsUser employee, String relationship, Integer relationRank, EdsDocumentPermission permission, EdsFolderRbac folderRbac) {
        if (folderRbac == null) {
            folderRbac = new EdsFolderRbac();
        }
        folderRbac.setUser(employee);
        folderRbac.setTrusteeType(EdsTrusteeType.USER);
        folderRbac.setEntryType(EdsFolderRbac.INHERITED);
        folderRbac.setFolder(folder);
        folderRbac.setEntityId(folder.getEntityId());
        folderRbac.setFolderType(folder.getFolderType());
        folderRbac.setRelationship(relationship);
        folderRbac.setRelationRank(relationRank);
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(false);
        createOrUpdate(folderRbac);
        return folderRbac;
    }

    private EdsFolderRbac createIndirectRelationEntry(EdsFolder folder, EdsGroup group, String relationship, Integer relationRank, EdsDocumentPermission permission) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setFolder(folder);
        folderRbac.setEntityId(folder.getEntityId());
        folderRbac.setFolderType(folder.getFolderType());
        folderRbac.setGroup(group);
        folderRbac.setTrusteeType(EdsTrusteeType.GROUP);
        folderRbac.setEntryType(EdsFolderRbac.INHERITED);
        folderRbac.setRelationship(relationship);
        folderRbac.setRelationRank(relationRank);
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(false);
        create(folderRbac);
        return folderRbac;
    }

    private EdsFolderRbac createIndirectRelationEntry(EdsFolder folder, EdsTrustee trustee, String relationship, Integer relationRank, EdsDocumentPermission permission) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setFolder(folder);
        folderRbac.setEntityId(folder.getEntityId());
        folderRbac.setFolderType(folder.getFolderType());
        if (EdsTrusteeType.USER.equals(trustee.getType().getObjectID())) {
            EdsUser user = userManager.get(trustee.getTrusteeID());
            folderRbac.setUser(user);
            folderRbac.setTrusteeType(EdsTrusteeType.USER);
        } else if (EdsTrusteeType.GROUP.equals(trustee.getType().getObjectID())) {
            EdsGroup group = groupManager.get(trustee.getTrusteeID());
            folderRbac.setGroup(group);
            folderRbac.setTrusteeType(EdsTrusteeType.GROUP);
        }
        folderRbac.setEntryType(EdsFolderRbac.INHERITED);
        folderRbac.setRelationship(relationship);
        folderRbac.setRelationRank(relationRank);
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(false);
        create(folderRbac);
        return folderRbac;
    }

    @Override
    public EdsFolderRbac createCustomFolderGroupRelationEntry(EdsFolder folder, EdsGroup group, EdsDocumentPermission permission, EdsRelationship relationship, int entryType) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setFolder(folder);
        folderRbac.setEntityId(folder.getEntityId());
        folderRbac.setFolderType(folder.getFolderType());
        folderRbac.setGroup(group);
        folderRbac.setTrusteeType(EdsTrusteeType.GROUP);
        folderRbac.setEntryType(entryType);
        folderRbac.setRelationship(relationship.getCode());
        folderRbac.setRelationRank(relationship.getRank());
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(false);
        create(folderRbac);
        return folderRbac;
    }

    @Override
    public EdsFolderRbac createCustomFileGroupRelationEntry(EdsFileHeader fileHeader, EdsGroup group, EdsDocumentPermission permission, EdsRelationship relationship, int entryType) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setEntityId(fileHeader.getEntityId());
        folderRbac.setFolderType(fileHeader.getFileType());
        folderRbac.setFolder(fileHeader.getFolder());
        folderRbac.setFileHeader(fileHeader);
        folderRbac.setGroup(group);
        folderRbac.setTrusteeType(EdsTrusteeType.GROUP);
        folderRbac.setEntryType(entryType);
        folderRbac.setRelationship(relationship.getCode());
        folderRbac.setRelationRank(relationship.getRank());
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(true);
        create(folderRbac);
        return folderRbac;
    }

    @Override
    public EdsFolderRbac createCustomFolderUserRelationEntry(EdsFolder folder, EdsUser user, EdsDocumentPermission permission, EdsRelationship relationship, int entryType) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setFolder(folder);
        folderRbac.setEntityId(folder.getEntityId());
        folderRbac.setFolderType(folder.getFolderType());
        folderRbac.setUser(user);
        folderRbac.setTrusteeType(EdsTrusteeType.USER);
        folderRbac.setEntryType(entryType);
        folderRbac.setRelationship(relationship.getCode());
        folderRbac.setRelationRank(relationship.getRank());
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(false);
        create(folderRbac);
        return folderRbac;
    }

    @Override
    public EdsFolderRbac createCustomFileUserRelationEntry(EdsFileHeader fileHeader, EdsUser user, EdsDocumentPermission permission, EdsRelationship relationship, int entryType) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setEntityId(fileHeader.getEntityId());
        folderRbac.setFolderType(fileHeader.getFileType());
        folderRbac.setFolder(fileHeader.getFolder());
        folderRbac.setFileHeader(fileHeader);
        folderRbac.setUser(user);
        folderRbac.setTrusteeType(EdsTrusteeType.USER);
        folderRbac.setEntryType(entryType);
        folderRbac.setRelationship(relationship.getCode());
        folderRbac.setRelationRank(relationship.getRank());
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(true);
        create(folderRbac);

        return folderRbac;
    }

    @Override
    public void removeFolderCustomEntries(EdsFolder folder) {
        List<EdsFolderRbac> tRbacEntries = getFolderRbacEntries(folder.getObjectID(), EdsFolderRbac.CUSTOM);
        for (EdsFolderRbac tEntry : tRbacEntries) {
            delete(tEntry);
        }
    }

    @Override
    public void removeFileCustomEntries(EdsFileHeader fileHeader) {
        List<EdsFolderRbac> tRbacEntries = getFileRbacEntries(fileHeader.getObjectID(), EdsFolderRbac.CUSTOM);
        for (EdsFolderRbac tEntry : tRbacEntries) {
            delete(tEntry);
        }
    }

    @Override
    public void removeFolderRbacList(List<Integer> folderRbacList) {
        if (folderRbacList.size() > 0) {
            update("delete FROM EdsFolderRbac r WHERE r.objectID in (" + ServerUtils.getAsCommoDelimited(folderRbacList, "0", ",") + ")");
        }
    }

    @Override
    public void bulkDeleteFileRbacEntriesForFolder(Integer folderId, boolean isSubFolder) {
        String schema = getCompanyId();
        StringBuilder sql = new StringBuilder()
                .append("DELETE FROM ").append(schema).append(".folderrbac r ")
                .append("WHERE r.file = true ")
                .append("AND r.fileheader_id IN (")
                .append("SELECT fh.id FROM ").append(schema).append(".fileheader fh ")
                .append("WHERE fh.folder_id = ").append(folderId).append(" AND fh.deleted IS NOT TRUE")
                .append(") ")
                .append("AND r.relationship IN ('DOC_CREATOR','DOC_OWNER','DOC_READER','DOC_VIEWER')");
        if (isSubFolder) {
            sql.append(" AND r.entrytype != 4");
        }
        updateNative(sql.toString());
    }

    @Override
    public void bulkInsertFileRbacEntriesForFolder(Integer folderId, Integer userId, Integer groupId,
                                                   Integer permissionId, String relationship, int relationRank, int entryType,
                                                   int trusteeType, boolean skipOwner) {
        String schema = getCompanyId();
        StringBuilder sql = new StringBuilder()
                .append("INSERT INTO ").append(schema).append(".folderrbac ")
                .append("(fileheader_id, folder_id, userid, groupid, trusteetype, entrytype, ")
                .append("relationship, relationrank, documentpermissionid, file, entityid, foldertype) ")
                .append("SELECT fh.id, fh.folder_id, ")
                .append(userId != null ? userId : "NULL").append(", ")
                .append(groupId != null ? groupId : "NULL").append(", ")
                .append(trusteeType).append(", ").append(entryType).append(", ")
                .append("'").append(relationship).append("', ").append(relationRank).append(", ")
                .append(permissionId).append(", true, fh.entityid, fh.filetype ")
                .append("FROM ").append(schema).append(".fileheader fh ")
                .append("WHERE fh.folder_id = ").append(folderId).append(" AND fh.deleted IS NOT TRUE");
        if (skipOwner && userId != null) {
            sql.append(" AND fh.owner_id != ").append(userId);
        }
        updateNative(sql.toString());
    }

    @Override
    public List<EdsFolderRbac> getFolderRbacEntries(Integer folderId) {
        return (List<EdsFolderRbac>) find("SELECT ti FROM EdsFolderRbac ti WHERE ti.folder.objectID = ? and ti.file<>true", folderId);
    }

    @Override
    public List<EdsFolderRbac> getFileRbacEntries(Integer fileId) {
        return (List<EdsFolderRbac>) find("SELECT ti FROM EdsFolderRbac ti WHERE ti.fileHeader.objectID = ? and ti.file=true", fileId);
    }

    @Override
    public List<EdsFolderRbac> getFolderRbacEntries(Integer folderId, int entryType) {
        return (List<EdsFolderRbac>) find("SELECT ti FROM EdsFolderRbac ti WHERE ti.folder.objectID = ? and ti.entryType = ? and ti.file<>true", folderId, entryType);
    }

    @Override
    public List<EdsFolderRbac> getFolderRbacEntries(Integer folderId, String relationship) {
        return (List<EdsFolderRbac>) find("SELECT ti FROM EdsFolderRbac ti WHERE ti.folder.objectID = ? and ti.relationship = ? and ti.file<>true", folderId, relationship);
    }

    @Override
    public List<EdsFolderRbac> getFileRbacEntries(Integer fileId, int entryType) {
        return (List<EdsFolderRbac>) find("SELECT ti FROM EdsFolderRbac ti WHERE ti.fileHeader.objectID = ? and ti.entryType = ? and ti.file=true", fileId, entryType);
    }

    @Override
    @Transactional
    public void indexFile(EdsFileHeader file) {
        addFileRbacEntries(file);
        try {
            folderSolrComponent.index(file);
        } catch (InterruptedException e) {
            baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_ADD, file, getUser());
        }
    }

    @Override
    public void indexFiles(List<EdsFileHeader> files) {
        for (EdsFileHeader edsFileHeader : files) {
            if (Objects.nonNull(edsFileHeader)) {
                addFileRbacEntries(edsFileHeader);
            }
        }
        try {
            folderSolrComponent.indexConcurrently(files);
        } catch (InterruptedException e) {
            log.error("Error File Index. Company ID : {} , Message : {} ", ServerSecurityContext.getInstance().getCompanyId(), e.getMessage());
        }
    }

    private FileHeaderManager fileHeaderManager;

    public void setFileHeaderManager(FileHeaderManager fileHeaderManager) {
        this.fileHeaderManager = fileHeaderManager;
    }

    public void setBaseEventPostProcessor(BaseEventsPostProcessor baseEventPostProcessor) {
        this.baseEventPostProcessor = baseEventPostProcessor;
    }

    @Override
    public void removeIndexFileEntries(Integer fileId) {
        removeFileEntries(fileId);
        removeFileFromSolr(fileId);
    }

    @Override
    public void removeFileFromSolr(Integer fileId) {
        try {
            solrManager.removeFile(fileId);
        } catch (SolrServerException | IOException e) {
            baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_DELETE, fileHeaderManager.get(fileId), getUser());
        }
    }

    @Override
    public List<EdsFolderRbac> getFileEntryForUser(EdsFileHeader file, EdsUser user) {
        Map params = new HashMap();
        params.put("userId", user.getObjectID());
        params.put("fileId", file.getObjectID());
        params.put("trusteeType", EdsTrusteeType.USER);
        return (List<EdsFolderRbac>) findByNamedParams("select distinct f from EdsFolderRbac f where (f.user.objectID=:userId or f.group.objectID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userId and t.type.objectID=:trusteeType))) and f.fileHeader.objectID=:fileId", params);
    }

    @Override
    public List<EdsFolderRbac> getFolderEntryForUser(EdsFolder folder, EdsUser user) {
        if (user == null) {
            user = (EdsUser) SecurityContext.getInstance().getUser();
        }
        Map params = new HashMap();
        params.put("userId", user != null ? user.getObjectID() : 0);
        params.put("folderId", folder.getObjectID());
        params.put("trusteeType", EdsTrusteeType.USER);
        return (List<EdsFolderRbac>) findByNamedParams("select distinct f from EdsFolderRbac f where (f.user.objectID=:userId or f.group.objectID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userId and t.type.objectID=:trusteeType))) and f.folder.objectID=:folderId", params);
    }

    @Override
    public EdsDocumentPermission getFolderEntryForUser2(EdsFolder folder, EdsUser user) {
        if (user == null) {
            user = (EdsUser) SecurityContext.getInstance().getUser();
        }
        Map params = new HashMap();
        params.put("userId", user != null ? user.getObjectID() : 0);
        params.put("folderId", folder.getObjectID());
        params.put("trusteeType", EdsTrusteeType.USER);
//        return (List<EdsFolderRbac>) findByNamedParams("select distinct f from EdsFolderRbac f where (f.user.objectID=:userId or f.group.objectID in " +
//                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userId and t.type.objectID=:trusteeType))) and f.folder.objectID=:folderId", params);


        StringBuilder sql = new StringBuilder("SELECT f.id, bool_or(perm.read) p_read, bool_or(perm.write) p_write, bool_or(perm.delete) p_delete, bool_or(perm.modifyACL) p_modifyACL ")
                .append(" FROM ").append(getCompanyId()).append(".folderrbac rbac ");
        sql
                .append("JOIN ").append(getCompanyId()).append(".folder f on f.id = rbac.folder_id \n")
                .append("JOIN ").append(getCompanyId()).append(".folderpermission perm on perm.id = rbac.documentPermissionId \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trusteegroup tgroup on tgroup.id = rbac.groupid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trusteegroup_trustee tgm on tgm.trusteegroup_id = tgroup.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trustee on trustee.id = tgm.members_id \n");
        sql
                .append("WHERE f.deleted is not true \n")
                .append(" AND f.id = " + folder.getObjectID() + " \n")
                .append("AND rbac.file is false AND perm.read is true \n")
                .append("AND (")
                .append("rbac.userid = " + user.getObjectID() + " OR (trustee.trusteetype = " + EdsTrusteeType.USER + " AND trustee.trusteeID = " + user.getObjectID() + ")")
                .append(" ) \n");
        sql.append("GROUP BY f.id \n");

        Object object = findNativeSingle(sql.toString());
        EdsDocumentPermission edsDocumentPermission = new EdsDocumentPermission();
        if (object != null) {
            Object[] permissionHolder = (Object[]) object;
            edsDocumentPermission.setRead((boolean) permissionHolder[1]);
            edsDocumentPermission.setWrite((boolean) permissionHolder[2]);
            edsDocumentPermission.setDelete((boolean) permissionHolder[3]);
            edsDocumentPermission.setModifyACL((boolean) permissionHolder[4]);
        }

        return edsDocumentPermission;

    }

    @Override
    public Map<EdsFolder, EdsDocumentPermission> getFoldersPermissionEntriesForUser(ListingFilterParameter fp, List<EdsFolder> folders, EdsUser user, boolean deleted) {
        Map<EdsFolder, EdsDocumentPermission> folderEdsDocumentPermissionMap = new LinkedHashMap<>();
        if (folders.isEmpty()) {
            return folderEdsDocumentPermissionMap;
        }
        Map params = new HashMap();
        params.put("userId", user.getObjectID());
        params.put("folders", folders);
        params.put("trusteeType", EdsTrusteeType.USER);
        params.put("deleted", deleted);
        String sortBy = "";
        if (fp != null && FileResource.NAME.equalsIgnoreCase(fp.getSortField())) {
            sortBy = " order by folder.name " + (fp.getSortDir() != null && Constants.DESC == fp.getSortDir().intValue() ? "desc" : "asc");
        }
        List<Object[]> folderRbacs = findByNamedParams("select distinct f, folder.name from EdsFolderRbac f left join f.folder folder where (f.user.objectID=:userId or f.group.objectID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userId and t.type.objectID=:trusteeType))) and f.folder.deleted=:deleted and f.folder in (:folders)" + sortBy, params);

        Map<EdsFolder, List<EdsDocumentPermission>> folderListMap = new LinkedHashMap<>();
        for (Object[] objs : folderRbacs) {
            EdsFolderRbac rbac = (EdsFolderRbac) objs[0];
            if (folderListMap.containsKey(rbac.getFolder())) {
                folderListMap.get(rbac.getFolder()).add(rbac.getDocumentPermission());
            } else {
                ArrayList<EdsDocumentPermission> permissions = new ArrayList<>();
                permissions.add(rbac.getDocumentPermission());
                folderListMap.put(rbac.getFolder(), permissions);
            }
        }

        for (EdsFolder folder : folderListMap.keySet()) {
            EdsDocumentPermission permission = new EdsDocumentPermission();
            permission.mergePermissions(permission, folderListMap.get(folder));
            if (permission.hasRead()) {
                folderEdsDocumentPermissionMap.put(folder, permission);
            }
        }

        return folderEdsDocumentPermissionMap;
    }

    @Override
    public EdsFolderRbac getFolderRbacEntryForGroup(Integer folderId, Integer groupId) {
        Map params = new HashMap();
        params.put("groupId", groupId);
        params.put("folderId", folderId);
        return (EdsFolderRbac) findSingleByNamedParams("select distinct f from EdsFolderRbac f" +
                " where f.group.objectID=:groupId and f.folder.objectID=:folderId", params);
    }

    @Override
    public EdsFolderRbac getFolderRbacEntryForUser(Integer folderId, Integer userId) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("folderId", folderId);
        return (EdsFolderRbac) findSingleByNamedParams("select distinct f from EdsFolderRbac f" +
                " where f.user.objectID=:userId and f.folder.objectID=:folderId", params);
    }

    @Override
    public EdsFolderRbac getFileRbacEntryForGroup(Integer fileId, Integer groupId) {
        Map params = new HashMap();
        params.put("groupId", groupId);
        params.put("fileId", fileId);
        return (EdsFolderRbac) findSingleByNamedParams("select distinct f from EdsFolderRbac f " +
                "where f.group.objectID=:groupId and f.fileHeader.objectID=:fileId", params);
    }

    @Override
    public EdsFolderRbac getFileRbacEntryForUser(Integer fileId, Integer userId) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("fileId", fileId);
        return (EdsFolderRbac) findSingleByNamedParams("select distinct f from EdsFolderRbac f" +
                " where f.user.objectID=:userId and f.fileHeader.objectID=:fileId", params);
    }

    @Override
    public EdsDocumentPermission getFilePermissionForUser(EdsFileHeader file, EdsUser user) {
        List<EdsDocumentPermission> permissions = new ArrayList<>();
        for (EdsFolderRbac rbac : getFileEntryForUser(file, user)) {
            permissions.add(rbac.getDocumentPermission());
        }
        EdsDocumentPermission permission = new EdsDocumentPermission();
        permission.mergePermissions(permission, permissions);
        return permission;
    }

    @Override
    public EdsDocumentPermission getFolderPermissionForUser(EdsFolder folder, EdsUser user) {
        List<EdsFolderRbac> folderRbacList = getFolderEntryForUser(folder, user);
        EdsDocumentPermission permission = new EdsDocumentPermission();
        for (EdsFolderRbac edsFolderRbac : folderRbacList) {
            if (!permission.hasRead()) {
                permission.setRead(edsFolderRbac.getDocumentPermission().hasRead());
            }
            if (!permission.hasDelete()) {
                permission.setDelete(edsFolderRbac.getDocumentPermission().hasDelete());
            }
            if (!permission.hasWrite()) {
                permission.setWrite(edsFolderRbac.getDocumentPermission().hasWrite());
            }
            if (!permission.hasModifyACL()) {
                permission.setModifyACL(edsFolderRbac.getDocumentPermission().hasModifyACL());
            }
            if (permission.hasRead() && permission.hasDelete() && permission.hasWrite() && permission.hasModifyACL()) {
                break;
            }
        }

        return permission;
    }

    @Override
    @Transactional
    public void addFileRbacEntries(EdsFileHeader fileHeader) {
        //Old indexes should be removed in order to keep table clean without dublication or old entries
        removeFileEntries(fileHeader.getObjectID());
        EdsFolderPolicy ownerPolicy = folderPolicyManager.getCompanyRelationPolicy(EdsRelationship.DOC_OWNER);
        EdsFolderPolicy readerPolicy = folderPolicyManager.getCompanyRelationPolicy(EdsRelationship.DOC_READER);
        EdsFolderPolicy adminPolicy = folderPolicyManager.getCompanyRelationPolicy(EdsRelationship.DOC_ADMINISTRATOR);

        EdsGroup accountantsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.ACCOUNTANTS);
        EdsGroup adminLocationsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.ADMIN_LOCATIONS);
        EdsGroup directorsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.DIRECTORS);
        EdsGroup projectManagersGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.PROJECT_MANAGERS);
        EdsGroup departmentLeadersGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.DEPARTMENT_LEADERS);
        EdsGroup membersGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.MEMBERS);
        EdsGroup hrsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.HRS);
        EdsGroup salesMenGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.SALESMEN);
        EdsGroup salesPersonsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.SALESPERSONS);
        EdsGroup customerServiceManagerGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.CUSTOMER_SERVICE_MANAGER);

        switch (fileHeader.getFolder().getFolderType()) {
            case EdsFolder.F_PROJECT -> {
                EdsProject project = projectManager.get(fileHeader.getFolder().getEntityId());
                List<EdsProjectEmployee> empTasks = projectManager.getEmployeesByProject(project.getObjectID());
                for (EdsProjectEmployee pe : empTasks) {
                    if (pe.getEmployeeDepartment() != null && pe.getEmployeeDepartment().getEmployee() != null) {
                        createFileDirectRelationEntry(fileHeader, pe.getEmployeeDepartment().getEmployee(), EdsRelationship.DOC_READER, readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                    }
                }
                if (project.getManager() != null) {
                    createFileDirectRelationEntry(fileHeader, project.getManager(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                }
                List<EdsEmployee> backupManagers = project.getBackupManagers();
                for (EdsEmployee backupManager : backupManagers) {
                    createFileDirectRelationEntry(fileHeader, backupManager, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                }
                if (project.getClient() != null) {//for project clients
                    List<EdsClientContact> clientContacts = clientContactManager.getAccessEnabledContacts(project.getClient());
                    for (EdsClientContact clientContact : clientContacts) {
                        createFileDirectRelationEntry(fileHeader, clientContact, EdsRelationship.DOC_READER, readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                    }
                }
                List<EdsFolderPolicy> indirectPolicies = folderPolicyManager.getCompanyIndirectRelationPolicies();
                for (EdsFolderPolicy fPolicy : indirectPolicies) {
                    createFileIndirectRelationEntry(fileHeader, fPolicy.getTrustee(), fPolicy.getRelation().getCode(), fPolicy.getRelation().getRank(), fPolicy.getPermission());
                }
                List<EdsFolderRbac> folderRbacList = getFolderRbacEntries(fileHeader.getFolder().getObjectID(), EdsRelationship.DOC_VIEWER);
                for (EdsFolderRbac folderRbac : folderRbacList) {
                    createFileDirectRelationEntry(fileHeader, folderRbac.getUser(), EdsRelationship.DOC_VIEWER, folderRbac.getRelationRank(), folderRbac.getDocumentPermission());
                }
            }
            case EdsFolder.F_DEFAULT -> {
                if (fileHeader.getFolder().getType() != EdsFolder.CUSTOM && fileHeader.getFolder().getType() != EdsFolder.TEMP) {

                    createFileIndirectRelationEntry(fileHeader, membersGroup, readerPolicy.getRelation().getCode(), readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                    //for all company client contacts group
                    EdsGroup clientsGroup = groupManager.getCompanyBuiltInGroup(EdsGroup.CLIENTS);
                    createFileIndirectRelationEntry(fileHeader, clientsGroup, readerPolicy.getRelation().getCode(), readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                }
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            case EdsFileHeader.F_TASK -> {
                EdsTask task = taskManager.get(fileHeader.getEntityId());
                if (task != null && task.getProject() != null) {
                    EdsProject project1 = task.getProject();
                    Set<EdsEmployeeTask> taskEmpls = task.getUnDeletedAssignments();
                    for (EdsEmployeeTask empTask : taskEmpls) {
                        if (empTask.getProjectEmployee() != null && empTask.getProjectEmployee().getEmployeeDepartment() != null) {
                            createFileDirectRelationEntry(fileHeader, empTask.getProjectEmployee().getEmployeeDepartment().getEmployee(), EdsRelationship.DOC_READER, readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                        }
                    }
                    if (task.getCreator() != null) {
                        createFileDirectRelationEntry(fileHeader, task.getCreator(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                    }
                    if (project1.getManager() != null) {
                        createFileDirectRelationEntry(fileHeader, project1.getManager(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                    }
                    List<EdsEmployee> backUpManagers = project1.getBackupManagers();
                    for (EdsEmployee backupManager : backUpManagers) {
                        createFileDirectRelationEntry(fileHeader, backupManager, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                    }
                    if (project1.getClient() != null) {//for project1 clients
                        List<EdsClientContact> clientContacts = clientContactManager.getAccessEnabledContacts(project1.getClient());
                        for (EdsClientContact clientContact : clientContacts) {
                            createFileDirectRelationEntry(fileHeader, clientContact, EdsRelationship.DOC_READER, readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                        }
                    }
                    List<EdsFolderPolicy> indirectPolicies1 = folderPolicyManager.getCompanyIndirectRelationPolicies();
                    for (EdsFolderPolicy fPolicy : indirectPolicies1) {
                        createFileIndirectRelationEntry(fileHeader, fPolicy.getTrustee(), fPolicy.getRelation().getCode(), fPolicy.getRelation().getRank(), fPolicy.getPermission());
                    }
                }
            }
            case EdsFileHeader.F_PUR_INV, EdsFileHeader.F_SALE_INV -> {
                EdsInvoice invoice = invoiceManager.get(fileHeader.getEntityId());
                if (invoice != null && invoice.getClientOrSupplier() != null) {
                    List<EdsClientContact> clientContacts = clientContactManager.getAccessEnabledContacts(invoice.getClientOrSupplier());
                    for (EdsClientContact clientContact : clientContacts) {
                        createFileDirectRelationEntry(fileHeader, clientContact, EdsRelationship.DOC_READER, readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                    }
                }
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, accountantsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            }
            case EdsFileHeader.F_SALE_QUOTE, EdsFileHeader.F_PUR_ORDER -> {
                EdsQuote quote = quoteManager.get(fileHeader.getEntityId());
                if (quote != null && quote.getClientOrSupplier() != null) {
                    List<EdsClientContact> clientContacts = clientContactManager.getAccessEnabledContacts(quote.getClientOrSupplier());
                    for (EdsClientContact clientContact : clientContacts) {
                        createFileDirectRelationEntry(fileHeader, clientContact, EdsRelationship.DOC_READER, readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                    }
                }
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, accountantsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            }
            case EdsFileHeader.F_BANK_TRANSFER, EdsFileHeader.F_BATCH_PAYMENT, EdsFileHeader.F_PREPAYMENT -> {
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, accountantsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            }
            case EdsFileHeader.F_EXP, EdsFileHeader.F_EXP_PAYMENT, EdsFileHeader.F_EXP_DOC -> {
                createFileIndirectRelationEntry(fileHeader, membersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, accountantsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, hrsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, salesMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, salesPersonsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
            }
            case EdsFileHeader.F_PRODUCTS_SERVICES, EdsFileHeader.F_RFQ, EdsFileHeader.F_TELEGRAM, EdsFileHeader.F_WHITE_LABEL_LOGO, EdsFileHeader.F_WHITE_LABEL_FAVICON, EdsFileHeader.F_WHATSAPP_MEDIA, EdsFileHeader.F_RFP, EdsFileHeader.F_PERS_GOAL, EdsFileHeader.F_EMPLOYEE_ATTENDANCE -> {
                createFileIndirectRelationEntry(fileHeader, membersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            case EdsFileHeader.F_DEP_GOAL, EdsFileHeader.F_PROJ_GOAL, EdsFileHeader.F_BUSS_GOAL, EdsFileHeader.F_LEAVE_REQUEST, EdsFileHeader.F_INCIDENT, EdsFileHeader.F_PAST_EMPLOYMENT, EdsFileHeader.F_INTERNAL_EMPLOYMENT, EdsFileHeader.F_DEPENDENTS, EdsFileHeader.F_CONTRACT, EdsFileHeader.F_CUSTOM_FIELD_ITEM -> {
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, hrsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, membersGroup, readerPolicy.getRelation().getCode(), readerPolicy.getRelation().getRank(), readerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            case EdsFileHeader.F_COMP_GOAL -> {
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            case EdsFileHeader.F_EMPLOYEE_PROFILE -> {
                if (fileHeader.getEntityId() != null) {
                    EdsEmployee employee = employeeManager.get(fileHeader.getEntityId());
                    if (employee != null) {
                        createFileDirectRelationEntry(fileHeader, employee, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                    }
                }
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, hrsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            case EdsFileHeader.F_MEETING_MINUTES, EdsFileHeader.F_VACANCY, EdsFileHeader.F_CANDIDATE, EdsFileHeader.F_PLACEMENT, EdsFileHeader.F_EMAIL_TEMPLATE -> {
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            case EdsFileHeader.F_CASE -> {
                if (fileHeader.getEntityId() != null) {
                    EdsCase crmCase = caseManager.get(fileHeader.getEntityId());
                    if (crmCase != null) {
                        if (crmCase.getAssignee() != null) {
                            createFileDirectRelationEntry(fileHeader, crmCase.getAssignee(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                        }
                        if (crmCase.getResolver() != null) {
                            createFileDirectRelationEntry(fileHeader, crmCase.getResolver(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                        }
                    }
                }
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, salesMenGroup, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, salesPersonsGroup, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                if (customerServiceManagerGroup != null) {
                    createFileIndirectRelationEntry(fileHeader, customerServiceManagerGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                }
            }
            case EdsFileHeader.F_LEAD, EdsFileHeader.F_CRM_ACCOUNT, EdsFileHeader.F_SOLUTION, EdsFileHeader.F_MASS_MAILING, EdsFileHeader.F_OPPORTUNITY, EdsFileHeader.F_CRM_CONTACT -> {
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, salesMenGroup, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, salesPersonsGroup, EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                if (customerServiceManagerGroup != null) {
                    createFileIndirectRelationEntry(fileHeader, customerServiceManagerGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                }
            }
            case EdsFolder.F_CASH_ADVANCE -> {
                createFileIndirectRelationEntry(fileHeader, accountantsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                //Cash advance can be created from mobile ignoring all user permission. So the once created, he should get access to the cash advance attachments
                createFileIndirectRelationEntry(fileHeader, adminLocationsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, departmentLeadersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, membersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, hrsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, salesMenGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, salesPersonsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            case EdsFolder.F_ADDITIONAL_PAYMENT -> {
                createFileIndirectRelationEntry(fileHeader, accountantsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            case EdsFolder.F_NOTE -> {
                createFileIndirectRelationEntry(fileHeader, accountantsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, directorsGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, projectManagersGroup, ownerPolicy.getRelation().getCode(), ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileDirectRelationEntry(fileHeader, fileHeader.getOwner(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
            }
            default -> {
                createFileIndirectRelationEntry(fileHeader, adminPolicy.getTrustee(), EdsRelationship.DOC_ADMINISTRATOR, adminPolicy.getRelation().getRank(), adminPolicy.getPermission());
                setParentPermissionToFile(fileHeader, false, userManager.getUser());
            }
        }

        if (fileHeader.getAuditInfo().getCreatedBy() != null) {
            createFileDirectRelationEntry(fileHeader, fileHeader.getAuditInfo().getCreatedBy(), EdsRelationship.DOC_OWNER, ownerPolicy.getRelation().getRank(), ownerPolicy.getPermission());
        }
    }

    private void setParentPermissionToFile(EdsFileHeader file, boolean isSubFolder, EdsUser user) {
        setFilePermissions(file, isSubFolder);
        Date now = new Date();
        file.getAuditInfo().setModificationDate(now);
        file.getAuditInfo().setModifiedBy(user);
        try {
            folderSolrComponent.index(file);
        } catch (InterruptedException e) {
            baseEventPostProcessor.registerEvent(FileCustomEventListenerImpl.TYPE, FileCustomEventListenerImpl.EVENT_ADD, file, user);
        }
    }

    private void setFilePermissions(EdsFileHeader file, boolean isSubFolder) {
        EdsFolder parentFolder = folderManager.get(file.getFolder().getObjectID());

        try {
            List<PermissionHolder> permissions = new ArrayList<>(documentsServiceLocal.getFolderPermissions(parentFolder.getObjectID()));
            List<EdsFolderRbac> rbacList2 = new ArrayList<>();
            if (permissions != null && !permissions.isEmpty()) {
                // Delete previous entries.
                for (EdsFolderRbac rbac : getFileRbacEntries(file.getObjectID())) {
                    boolean deleted = false;
                    if (rbac.getEntryType() != EdsFolderRbac.CUSTOM || (!isSubFolder && rbac.getEntryType() == EdsFolderRbac.CUSTOM)) {
                        if (EdsRelationship.DOC_ADMINISTRATOR.equals(rbac.getRelationship())) {

                        } else if (EdsRelationship.DOC_DIRECTOR.equals(rbac.getRelationship())) {

                        } else if (EdsRelationship.DOC_CREATOR.equals(rbac.getRelationship())) {
                            delete(rbac);
                            deleted = true;
                        } else if (EdsRelationship.DOC_OWNER.equals(rbac.getRelationship())) {
                            delete(rbac);
                            deleted = true;
                        } else if (EdsRelationship.DOC_READER.equals(rbac.getRelationship())) {
                            delete(rbac);
                            deleted = true;
                        } else if (EdsRelationship.DOC_VIEWER.equals(rbac.getRelationship())) {
                            delete(rbac);
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
                            update(fRbac);
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
                            createCustomFileGroupRelationEntry(file, groupManager.get(dto.getGroup().getGroupID()), permission, relationship, isSubFolder ? EdsFolderRbac.INHERITED : EdsFolderRbac.CUSTOM);
                        } else if (dto.getUser() != null) {
                            createCustomFileUserRelationEntry(file, userManager.get(dto.getUser().getObjectId()), permission, relationship, (isSubFolder || EdsRelationship.DOC_OWNER.equals(dto.getRelationship())) ? EdsFolderRbac.INHERITED : EdsFolderRbac.CUSTOM);
                        }

                    }
                }
            }
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        } catch (InsufficientPermissionsException e) {
            e.printStackTrace();
        }
    }

    private EdsFolderRbac getFolderOrFileRbacForUser(List<EdsFolderRbac> rbacList, Integer userOrGroupId, boolean isUser) {
        for (EdsFolderRbac rbac : rbacList) {
            if (isUser && EdsTrusteeType.USER.equals(rbac.getTrusteeType()) && userOrGroupId.equals(rbac.getUser().getObjectID())) {
                return rbac;
            }
            if (!isUser && EdsTrusteeType.GROUP.equals(rbac.getTrusteeType()) && userOrGroupId.equals(rbac.getGroup().getObjectID())) {
                return rbac;
            }
        }
        return null;
    }

    private EdsDocumentPermission getPermission(PermissionHolder dto) {
        EdsDocumentPermission res = new EdsDocumentPermission();
        res.setRead(dto.isRead());
        res.setWrite(dto.isWrite());
        res.setDelete(dto.isDelete());
        res.setModifyACL(dto.isModifyACL());
        return res;
    }

    private EdsFolderRbac createFileIndirectRelationEntry(EdsFileHeader fileHeader, EdsTrustee trustee, String relationship, Integer relationRank, EdsDocumentPermission permission) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setFolder(fileHeader.getFolder());
        folderRbac.setFileHeader(fileHeader);
        folderRbac.setEntityId(fileHeader.getEntityId());
        folderRbac.setFolderType(fileHeader.getFileType());
        if (EdsTrusteeType.USER.equals(trustee.getType().getObjectID())) {
            EdsUser user = userManager.get(trustee.getTrusteeID());
            folderRbac.setUser(user);
            folderRbac.setTrusteeType(EdsTrusteeType.USER);
        } else if (EdsTrusteeType.GROUP.equals(trustee.getType().getObjectID())) {
            EdsGroup group = groupManager.get(trustee.getTrusteeID());
            folderRbac.setGroup(group);
            folderRbac.setTrusteeType(EdsTrusteeType.GROUP);
        }
        folderRbac.setEntryType(EdsTaskRbac.INHERITED);
        folderRbac.setRelationship(relationship);
        folderRbac.setRelationRank(relationRank);
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(true);
        create(folderRbac);
        return folderRbac;
    }

    private EdsFolderRbac createFileDirectRelationEntry(EdsFileHeader fileHeader, EdsUser employee, String relationship, Integer relationRank, EdsDocumentPermission permission) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setUser(employee);
        folderRbac.setTrusteeType(EdsTrusteeType.USER);
        folderRbac.setEntryType(EdsFileHeader.INHERITED);
        folderRbac.setFolder(fileHeader.getFolder());
        folderRbac.setFileHeader(fileHeader);
        folderRbac.setEntityId(fileHeader.getEntityId());
        folderRbac.setFolderType(fileHeader.getFileType());
        folderRbac.setRelationship(relationship);
        folderRbac.setRelationRank(relationRank);
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(true);
        create(folderRbac);
        return folderRbac;
    }

    private EdsFolderRbac createFileIndirectRelationEntry(EdsFileHeader fileHeader, EdsGroup group, String relationship, Integer relationRank, EdsDocumentPermission permission) {
        EdsFolderRbac folderRbac = new EdsFolderRbac();
        folderRbac.setFolder(fileHeader.getFolder());
        folderRbac.setFileHeader(fileHeader);
        folderRbac.setEntityId(fileHeader.getEntityId());
        folderRbac.setFolderType(fileHeader.getFileType());
        folderRbac.setGroup(group);
        folderRbac.setTrusteeType(EdsTrusteeType.GROUP);
        folderRbac.setEntryType(EdsTaskRbac.INHERITED);
        folderRbac.setRelationship(relationship);
        folderRbac.setRelationRank(relationRank);
        folderRbac.setDocumentPermission(permission);
        folderRbac.setFile(true);
        create(folderRbac);
        return folderRbac;
    }

    @Override
    public void removeGroupEntries(Integer groupId) {
        Map params = new HashMap();
        params.put("groupId", groupId);
        updateByNamedParams("DELETE FROM EdsFolderRbac r where r.group.objectID=:groupId", params);
    }

    @Override
    public void removeUserEntries(Integer userId) {
        Map params = new HashMap();
        params.put("userId", userId);
        updateByNamedParams("DELETE FROM EdsFolderRbac r where r.user.objectID=:userId", params);
    }
}
