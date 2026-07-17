package com.edatasite.workforce.gwt.core.server.db.impl.documents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.documents.EdsCopiedFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BusinessEventManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.CopiedFileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.AttachmentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.FileHeadertEventListenerImpl;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 4, 2010
 * Time: 9:05:02 PM
 */

/**
 * Current class includes all server side document related conception.
 * Here you can see the centralized logic and main conception should
 * be managed only from this class.
 * <p/>
 * Main attention to developers who wants to make changes to current class
 * is that they either MUST learn the conception and act according to it.
 * With such action we can avoid from redundancy in class and method levels.
 */
@Transactional
@Service("attachmentUtilsManager")
public class AttachmentUtilsManagerImpl implements AttachmentUtilsManager {

    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("documentsService")
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private CopiedFileHeaderManager copiedFileHeaderManager;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private BusinessEventManager businessEventManager;
    @Autowired
    private ReferenceManager referenceManager;


    /**
     * Returns all attachments according to parameters
     *
     * @param folderType - type of the folder
     * @param folderID   - id of the folder(In case of task the id is projectID, in case of lead the id is leadID, etc...)
     * @param entityID   - id of the entity(Task, Project, Issue, Lead, etc...)
     * @return
     */
    public List<FileResource> getAttachments(int folderType, Integer folderID, Integer entityID, EdsUser user) {
        EdsFolder folder = folderManager.getFolder(folderType, folderID);
        if (folder == null) {
            folder = folderManager.getFolderByFolderType(folderType);
        }
//        EdsFolder folder = getFolder(folderType, folderID, null);

        if (folder != null) {//It means if company sections are not indexed, there is no need to return the list.
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setFolderId(folder.getObjectID());
            filter.setCrmEntityId(entityID);
            filter.setUserID(user != null ? user.getObjectID() : null);
            //filter.setFolderType(folderType);
            if (entityID != null) {
                if (folderType != Constants.F_TASK && folderType != Constants.F_PROJECT) {//Sherali M. va Sherali P. projectda to'g'ri ishlayapti tegilmang degandan keyin bularni ignore qildim
                    filter.setEntityBasedAttachmentList(true);
                }
            }
            ListResult<FileResource> files = new ListResult<>();
            try {
                files = documentsServiceLocal.listFile(filter);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            }
            return files.getList();
        }

        return new ArrayList<>();
    }

    /**
     * Returns all attachments according to parameters
     *
     * @param folderType - type of the folder
     * @param folderID   - id of the folder(In case of task the id is projectID, in case of lead the id is leadID, etc...)
     * @param entityID   - id of the entity(Task, Project, Issue, Lead, etc...)
     * @return
     */
    public List<FileResource> getAttachments(int folderType, Integer folderID, Integer entityID) {
        EdsUser loggedUser = folderManager.getUser();
        return getAttachments(folderType, folderID, entityID, loggedUser);
    }

    /**
     * Saves all section related attachments. Overall logic
     * has been sroring here in order to manage it easily.
     *
     * @param folderType - type of the folder that differentiates several sections.
     * @param folderID   - id of the folder(For example in case of project the id is projectID)
     * @param entityID   - id of the entity. Entity may be task, issue, project, etc...
     * @param items      - items that have to be saved as files in EdsFileHeader table.
     */
    public void saveAttachments(int folderType, Integer folderID, Integer entityID, FileItem[] items) {
        EdsUser loggedUser = fileHeaderManager.getUser();
        saveAttachments(folderType, folderID, entityID, items, loggedUser);
    }

    public void saveAttachments(int folderType, Integer folderID, Integer entityID, FileItem[] items, EdsUser loggedUser) {
        if (items == null || items.length == 0) {
            return;
        }
        EdsCompany company = loggedUser.getCompany();

        FolderResource tempFolder = commonService.getTempFolderByCompanyID(null, null);
        EdsFolder destination = getFolder(folderType, folderID, company);

        for (FileItem item : items) {
            EdsFileHeader file = fileHeaderManager.getFile(tempFolder.getObjectId(), item.getFileName());
            if(file == null){
                file = fileHeaderManager.getFile(destination.getObjectID(), item.getFileName());
            }
            EdsCopiedFileHeader copiedFile = copiedFileHeaderManager.getFile(tempFolder.getObjectId(), item.getFileName());
            if (file != null) {
                if (folderManager.existsFileOtherThisFileID(destination.getObjectID(), file.getName(), file.getObjectID())) {
                    file.setName(getFileName(file.getName()));
                }
                file.setEntityId(entityID);
                file.setOwner(loggedUser);
                file.setFolder(destination);
                file.setFileType(folderType);
                file.setDocumentID(item.getDocumentID());
                if (item.getExpireDate() != null && item.getExpireDate().getDate() != null) {
                    file.setExpireDate(item.getExpireDate().getNonConvertedDate());
                }
                if (item.getIssuedDate() != null && item.getIssuedDate().getDate() != null) {
                    file.setIssuedDate(item.getIssuedDate().getNonConvertedDate());
                }
                if (StringUtils.isNotBlank(item.getDescription())) {
                    file.getCurrentBody().setDescription(item.getDescription());
                }
                if (item.getTypeId() != null) {
                    file.setDocumentType(referenceManager.get(item.getTypeId()));
                }

                fileHeaderManager.update(file);
                if (folderType == Constants.F_TASK) {
                    baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.TASK_FILE_ADD, file, loggedUser);
                } else if (folderType == Constants.F_PROJECT) {
                    baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.PROJECT_FILE_ADD, file, loggedUser);
                } else if (folderType == Constants.F_PR_ISSUE) {
                    baseEventPostProcessor.registerEvent(FileHeadertEventListenerImpl.TYPE, FileHeadertEventListenerImpl.ISSUE_FILE_ADD, file, loggedUser);
                }

                EdsBusinessEvent edsBusinessEvent = baseEventPostProcessor.registerEvent(AttachmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, file, loggedUser);
                edsBusinessEvent.setRelationID(file.getEntityId());
                edsBusinessEvent.setRelationType(ServerUtils.getFolderRelationType(file.getFileType()));
                businessEventManager.update(edsBusinessEvent);

                /**
                 * We are removing it, because we are going to set the instance again to the solr,
                 * so, if we don't delete it, there will be two relations to one file. Thus, we are
                 * deleting the file id and adding the whole file to the solr.
                 */
                folderRbacManager.indexFile(file);
            }
            if (copiedFile != null) {
                copiedFile.setName(copiedFile.getName());
                if (copiedFileHeaderManager.existsFile(destination.getObjectID(), copiedFile.getName())) {
                    copiedFile.setName(getFileName(copiedFile.getName()));
                }
                copiedFile.setEntityId(entityID);
                copiedFile.setOwner(loggedUser);
                copiedFile.setFolder(destination);
                copiedFile.setFileType(folderType);
                copiedFileHeaderManager.update(copiedFile);
            }
        }
    }

    public void copyFileWhenConvert(int folderType, int folderID, int fileID, int entityID, FileResource fileResource) {

        EdsUser loggedUser = fileHeaderManager.getUser();
        EdsCompany company = loggedUser.getCompany();
        EdsFolder destination = getFolder(folderType, folderID, company);

        try {
            documentsService.copyFile(fileID, destination.getObjectID(), entityID);
        } catch (QuotaExceededException | DuplicateNameException | InsufficientPermissionsException | ObjectNotFoundException e) {
            e.printStackTrace();
        }

    }

    private String getFileName(String fullPath) {
        String s = String.valueOf(new Random().nextLong());
        int dot = fullPath.lastIndexOf(".");
        if (dot > 0) {
            String extension = fullPath.substring(dot + 1);
            String name = fullPath.substring(0, dot);
            return name + "(" + s.substring(s.length() - 5) + ")." + extension;
        } else {
            return fullPath + "(" + s.substring(s.length() - 5) + ")";
        }
    }

    /**
     * We are sending folderID instead of entityID, because the folders which have parent
     * folders, like task or issue, parent folder entity has to be sent as entity id, thus
     * we are sending folderID. When other types of folders, which have no parent folders
     * also have to send folderID. For them in service side we are setting their id for both
     * entityID and folderID, therefore it does not matter.
     */
    public EdsFolder getFolder(int folderType, Integer entityID, EdsCompany company) {
        EdsFolder folder = folderManager.getFolder(folderType, entityID);
        /**
         * This is in case of CRM and other folders, because CRM and other folders
         * have no id. Only project folder has id value.
         */
        if (folder == null) {
            folder = folderManager.getFolderByFolderType(folderType);
        }

        if(folder==null) {
            documentsServiceLocal.createSystemFolders(company.getObjectID());
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
            folder = folderManager.getFolderByFolderType(folderType);
        }
        return folder;
    }
}
