package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleDocumentsManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.ProjectIndexRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.google.api.services.drive.Drive;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 6, 2010
 * Time: 2:04:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ProjectEventListenerImpl implements BusinessEventListener {

    public static String BILL_OF_MATERIALS = "BILL_OF_MATERIALS";

    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.projectEventListener);
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectIndexRbacManager projectIndexRbacManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private GoogleDocumentsManager googleDocumentsManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    public void onAddEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        projectIndexRbacManager.indexProject(project);
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isSolrIndexed()) {
            try {
//                solrManager.indexAddProject(project, creator.getCompany().getObjectID());
                projectSolrComponent.index(project);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                event.setSolrIndexed(false);
            }
        }

        // Register to myUpdates
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerProjectAddUpdate(project, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception e) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed() && event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

        //If enabled create folder to the google drive then a new folder created by named project name into the Google Drive
        boolean createFolderToGoogleDrive = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREATE_OBJECT_FOLDER_TO_GOOGLE_DRIVE);
        boolean createSubfolderStructure = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREATE_OBJECT_SUBFOLDER_STRUCTURE);

        if (createFolderToGoogleDrive) {
            TreeSelectItem projectFolderInGoogleDrive = new TreeSelectItem(project.getObjectID(), project.getName());

            if (createSubfolderStructure) {
                EdsReference subfolderStructure = referenceManager.findReferenceByCode("PROJECT_SUBFOLDER_STRUCTURE");
                if (subfolderStructure != null && !subfolderStructure.getChildlist().isEmpty()) {
                    buildSubfolderStructure(projectFolderInGoogleDrive, subfolderStructure.getChildlist());
                }
            }

            TreeSelectItem projectClientFolder = null;
            if (project.getClient() != null) {
                projectClientFolder = new TreeSelectItem(project.getClient().getObjectID(), project.getClient().getName());
                projectClientFolder.addChild(projectFolderInGoogleDrive);
            }

            createProjectFolderStructerToGDrive(creator, Collections.singletonList(projectClientFolder != null ? projectClientFolder : projectFolderInGoogleDrive), 1);
        }

    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (BILL_OF_MATERIALS.equals(event.getEventType())) {
            EdsProject project = projectManager.get(event.getEntityID());
            EdsUser creator = userManager.get(event.getSourceID());
            String status = event.getCustomStringField();
            event.setStatus(EventStatus.COMPLETED.name());
            messageManager.sendBillOfMaterialsNotification(creator, project, status);
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        projectIndexRbacManager.indexProject(project);
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isSolrIndexed()) {
            try {
//                solrManager.indexAddProject(project, creator.getCompany().getObjectID());
                projectSolrComponent.index(project);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        for (EdsProjectEmployee projectEmployee : projectEmployeeManager.getDeleteProjectEmployees(project)) {
            EdsUser employee = projectEmployee.getEmployeeDepartment().getEmployee();
            if (!employee.getObjectID().equals(creator.getObjectID())) {
                messageManager.sendProjectUpdateNotification(project, employee, creator);
            }
        }
        if (!event.isSendMail2()) {
            EdsCrmAccount client = project.getClient();
            if (client != null && client.isClient()) {
                List<EdsClientContact> clientContactList = clientContactManager.getAccessEnabledContacts(client);
                for (EdsClientContact clientContact : clientContactList) {
                    try {
                        messageManager.sendProjectUpdateNotification(project, clientContact, creator);
                        event.setSendMail2(true);
                    } catch (Exception e) {
                        event.setSendMail2(false);
                        event.setStatus(EventStatus.FAILED.name());
                    }
                }
            } else {
                event.setSendMail2(true);
            }
        }

        // Register to myUpdates
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerProjectEditUpdate(project, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                if (project.getManager() != null && !creator.getObjectID().equals(project.getManager().getObjectID())) {
                    EdsMyUpdate myUpdForManager = myUpdateManager.registerProjectManagerEditUpdate(project, project.getManager(), creator, event.getTime());
                    myUpdForManager.setPrivateUpdate(true);
                    myUpdForManager.setSuperUser(event.isSuperUser());
                }
                for (EdsEmployee backupManager : project.getBackupManagers()) {
                    if (!creator.getObjectID().equals(backupManager.getObjectID())) {
                        EdsMyUpdate myUpdForBackupManager = myUpdateManager.registerProjectBackupManagerEditUpdate(project, backupManager, creator, event.getTime());
                        myUpdForBackupManager.setPrivateUpdate(true);
                        myUpdForBackupManager.setSuperUser(event.isSuperUser());
                    }
                }
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed() && event.isMyUpdatesItemEdit() && event.isSendMail2()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

        //If enabled create folder to the google drive then a new folder created by named project name into the Google Drive
        boolean createFolderToGoogleDrive = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREATE_OBJECT_FOLDER_TO_GOOGLE_DRIVE);
        boolean createSubfolderStructure = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CREATE_OBJECT_SUBFOLDER_STRUCTURE);

        if (EdsGenericSettings.YES.equals(createFolderToGoogleDrive)) {
            TreeSelectItem projectFolderInGoogleDrive = new TreeSelectItem(project.getObjectID(), project.getName());

            if (EdsGenericSettings.YES.equals(createSubfolderStructure)) {
                EdsReference subfolderStructure = referenceManager.findReferenceByCode("PROJECT_SUBFOLDER_STRUCTURE");
                if (subfolderStructure != null && !subfolderStructure.getChildlist().isEmpty()) {
                    buildSubfolderStructure(projectFolderInGoogleDrive, subfolderStructure.getChildlist());
                }
            }

            TreeSelectItem projectClientFolder = null;
            if (project.getClient() != null) {
                projectClientFolder = new TreeSelectItem(project.getClient().getObjectID(), project.getClient().getName());
                projectClientFolder.addChild(projectFolderInGoogleDrive);
            }

            createProjectFolderStructerToGDrive(creator, Collections.singletonList(projectClientFolder != null ? projectClientFolder : projectFolderInGoogleDrive), 1);
        }

    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        //removed from solr
        EdsUser deleter = userManager.get(event.getSourceID());
        projectIndexRbacManager.removeProjectIndex(project);
        if (!event.isSolrIndexed()) {
            try {
                solrManager.removeCompanyProject(project.getObjectID(), deleter.getCompany().getObjectID());
                event.setSolrIndexed(true);
            } catch (Exception ex) {
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        // Register to myUpdates
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerProjectDeleteUpdate(project, deleter, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemDelete(true);
            } catch (Exception e) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete() && event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
        for (EdsProjectEmployee projectEmployee : projectEmployeeManager.getDeleteProjectEmployees(project)) {
            EdsUser employee = projectEmployee.getEmployeeDepartment().getEmployee();
            if (!employee.getObjectID().equals(deleter.getObjectID())) {
                messageManager.sendProjectDeleteNotification(project, employee, deleter);
            }
        }
    }

    private void buildSubfolderStructure(TreeSelectItem parent, List<EdsReference> childlist) {
        for (EdsReference child : childlist) {
            TreeSelectItem item = new TreeSelectItem();
            item.setName(child.getName());
            item.setParent(parent);
            parent.addChild(item);

            if (!child.getChildlist().isEmpty()) {
                buildSubfolderStructure(item, child.getChildlist());
            }
        }
    }

    private void createProjectFolderStructerToGDrive(EdsUser user, List<TreeSelectItem> folders, int attempt) {
        try {
            System.out.println("CompanyID: " + ServerSecurityContext.getInstance().getCompanyId());
            System.out.println("Google Drive ATTEMPT: " + attempt + " STARTED. Project/Client name: " + folders.get(0).getName());
            Drive service = googleDocumentsManager.getService(user);
            Boolean checkExistingFolderStructure = googleDocumentsManager.checkExistingFoldersIntoGoogleDrive(service, folders, null);

            if (!checkExistingFolderStructure) {
                googleDocumentsManager.createFoldersIntoGoogleDrive(service, folders, null);
            }
            System.out.println("CompanyID: " + ServerSecurityContext.getInstance().getCompanyId());
            System.out.println("Google Drive ATTEMPT: " + attempt + " FINISHED. Project/Client name: " + folders.get(0).getName());
        } catch (IOException e) {
            if (attempt <= 3) {
                try {
                    Thread.sleep(attempt* 1000L);
                    createProjectFolderStructerToGDrive(user, folders, ++attempt);
                } catch (InterruptedException e1) {
                    System.out.println("CompanyID: " + ServerSecurityContext.getInstance().getCompanyId());
                    System.out.println("Google Drive ATTEMPT: " + attempt + " FAILED. Project/Client name: " + folders.get(0).getName());
                    e1.printStackTrace();
                }
            }
        }
    }
}
