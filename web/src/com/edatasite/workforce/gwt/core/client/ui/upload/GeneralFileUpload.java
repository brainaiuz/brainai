package com.edatasite.workforce.gwt.core.client.ui.upload;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.FileUploadDialog;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.upload.AttachmentDeleteHandler;
import com.edatasite.workforce.gwt.documents.client.upload.EditableAttachmentLinksComponents;
import com.edatasite.workforce.gwt.documents.client.upload.GeneralAttachmentLinksComponent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_EMPLOYEE_PROFILE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_PRODUCTS_SERVICES;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PRODUCT_UPLOAD_FILES;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 6/13/11
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralFileUpload extends Composite {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();

    protected VerticalPanel panel = new VerticalPanel();

    private GeneralAttachmentLinksComponent component;

    private final HashMap<Integer, FileResource> savedFiles = new HashMap<>();

    private FileUploadDialog dlg;

    private FolderResource folderResource;

    private final int folderType;

    private Integer folderId;

    private Integer entityId;

    private boolean isDownloadable = true;

    private Command deleteCommand, finishCommand, command, buttonClick;

    private FileResource[] additionalAttachments;
    private boolean isEditable = false;
    private String typeCode;
    private final ArrayList<SelectItem> typeNames = new ArrayList<>();
    private WfmButton2 uploadButton;
    private final ArrayList<Integer> deletedIDs = new ArrayList<>();

    ClickHandler uploadHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            dlg = new FileUploadDialog(folderResource, Utils.getFileUploadType(), Utils.getMaxFileUploadSize(), true, isDownloadable);
            dlg.addUploadListener(command, FileUploadDialog.FileUploadEvents.FILE_UPLOADED, FileUploadDialog.FileUploadEvents.FILE_REMOVED);
        }
    };

    /**
     * @param folderType (F_PROJECT, F_TASK ...)
     * @param folderId   Folder Id
     * @param entityId   EdsObject Id
     */
    public GeneralFileUpload(int folderType, Integer folderId, final Integer entityId) {
        this(folderType, folderId, entityId, true);
    }

    /**
     * @param folderType     (F_PROJECT, F_TASK ...)
     * @param folderId       Folder Id
     * @param entityId       EdsObject Id
     * @param isDownloadable This field is to determine if we should add Content-desposition meta data or not
     *                       if Content-desposition is set then browsers will not display but rather will download file
     */

    public GeneralFileUpload(int folderType, Integer folderId, final Integer entityId, boolean isDownloadable) {
        this.folderType = folderType;
        this.entityId = entityId;
        this.folderId = folderId;
        this.isDownloadable = isDownloadable;
        start();
    }

    public GeneralFileUpload(int folderType, Integer folderId, boolean isEditable, final Integer entityId, String typeCode) {
        this.folderType = folderType;
        this.entityId = entityId;
        this.folderId = folderId;
        this.isEditable = isEditable;
        this.typeCode = typeCode;
        start2();
    }

    private void start() {
        if (folderId == null) {
            CommonService.App.get().getTempFolderByCompanyID(Utils.isWebForm() ? Utils.getEncryptedCompanyID() : null, null, new AbstractAsyncCallback<FolderResource>() {
                public void success(FolderResource result) {
                    GeneralFileUpload.this.folderResource = result;
                    getFileMaxSize();
                }
            });
        } else {
            createFolderResours(false);
        }
        initWidget(panel);
    }

    private void start2() {
        typeNames.add(new SelectItem(0, wfmStrings.pleaseSelect()));
        DocumentsService.App.get().getDocumentTypes(typeCode, new AbstractAsyncCallback<HashMap<Integer, ArrayList<SelectItem>>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(HashMap<Integer, ArrayList<SelectItem>> documenTypes) {
                if (documenTypes.size() > 0) {
                    typeNames.addAll(documenTypes.get(folderType) != null ? documenTypes.get(folderType) : documenTypes.get(Constants.F_COMPANY_DOCUMENTS));
                }
            }
        });
        createFolderResours(false);
        initWidget(panel);
    }

    private void createFolderResours(final boolean isCustomField) {
        DocumentsService.App.get().getFolderResource(folderType, folderId, new AbstractAsyncCallback<FolderResource>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(FolderResource result) {
                folderResource = result;
                if (entityId != null) {
                    folderResource.setEntityId(entityId);
                }
                if (isCustomField) {
                    getFileResources();
                } else {
                    getFileMaxSize();
                }
            }
        });
    }

    private void getFileMaxSize() {
        if (Utils.getMaxFileUploadSize() == null) {
            DocumentsService.App.get().getCompanyFileUploadMaxSize(new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable caught) {
                    Utils.setMaxFileUploadSize(null);
                    init();
                }

                @Override
                public void success(Integer result) {
                    Utils.setMaxFileUploadSize(result);
                    init();
                }
            });
        } else {
            init();
        }
    }

    private final AttachmentDeleteHandler attachmentDeleteHandler = new AttachmentDeleteHandler() {
        public void onDelete(final Integer fileID) {
            DocumentsService.App.get().deleteFile(fileID, folderId, folderType, new AbstractAsyncCallback() {
                public void success(Object result) {
                    dlg.getUploadedFiles().remove(fileID);
                    savedFiles.remove(fileID);
                    deletedIDs.add(fileID);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.attachment()), Info.Type.INFO);
                    if (deleteCommand != null) {
                        deleteCommand.execute();
                    }
                    if (folderType == Constants.F_OPPORTUNITY) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, false, GeneralFileUpload.this);
                    }
                }
            });
        }
    };

    private void getFileResources() {
        if (entityId == null) {
            ArrayList<FileResource> files = getAttachments();
            if (dlg.getFileResources() != null && !dlg.getFileResources().isEmpty()) {
                for (FileResource file : dlg.getFileResources()) {
                    if (!files.contains(file)) {
                        files.add(file);
                    }
                }
            }
            loadFileResources(files, false);
        } else {
            DocumentsService.App.get().getFileResources(folderType, folderId, entityId, new AbstractAsyncCallback<ArrayList<FileResource>>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(ArrayList<FileResource> fileResources) {
                    loadFileResources(fileResources, true);
                    if (finishCommand != null) {
                        finishCommand.execute();
                    }
                }
            });
        }
    }

    protected void loadFileResources(ArrayList<FileResource> fileResources, boolean isDownload) {
        if (component != null) {
            panel.remove(component);
        }
        boolean isDeleted = false;
        if (!Utils.hasRole(Constants.CLIENT)) {
            isDeleted = true;
        }
        if (!folderResource.getPermission().isDelete()) {
            isDeleted = false;
        }
        if (additionalAttachments != null && additionalAttachments.length > 0) {
            fileResources.addAll(Arrays.asList(additionalAttachments));
        }
        FileResource[] resources = fileResources.toArray(new FileResource[]{});
        if (isEditable) {
            component = new EditableAttachmentLinksComponents(resources, isDeleted, typeNames, folderType, typeCode);
        } else {
            component = new GeneralAttachmentLinksComponent(resources, folderType, isDownload, isDeleted);
        }
        component.onRemoveAttachment(attachmentDeleteHandler);
        if (entityId != null) {
            panel.insert(component, 0);
        } else {
            panel.insert(component, 0);
        }
        addSevedFiles(fileResources);
    }

    private void addSevedFiles(ArrayList<FileResource> fileResources) {
        for (FileResource file : fileResources) {
            savedFiles.put(file.getObjectId(), file);
        }
    }

    private void init() {
        panel.setSpacing(1);
        panel.addStyleName("summaryFileUpload");
        uploadButton = new WfmButton2(wfmStrings.upload(), Constants.BTN_PRIMARY, "ficon--upload", uploadHandler);
        uploadButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                buttonClick.execute();
            }
        });
        uploadButton.getElement().setId("Upload_buttom");

        folderResource.setFileType(folderType);
        dlg = new FileUploadDialog(folderResource, Utils.getFileUploadType(), Utils.getMaxFileUploadSize(), false, isDownloadable);

        command = () -> getFileResources();
        dlg.addUploadListener(command, FileUploadDialog.FileUploadEvents.FILE_UPLOADED, FileUploadDialog.FileUploadEvents.FILE_REMOVED);

        getFileResources();

        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        if ((F_PRODUCTS_SERVICES == folderType && Utils.hasPermission(ACCOUNTING_PRODUCT_UPLOAD_FILES)) || F_EMPLOYEE_PROFILE == folderType || Constants.F_OPPORTUNITY == folderType || folderResource.getPermission().isWrite() || folderId == null) {
            FlowPanel fp = new FlowPanel();
            fp.add(uploadButton);
            fp.addStyleName("summaryFileUpload__action");
            panel.add(fp);
        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DOCUMENTS_UPLOAD_FILES, GeneralFileUpload.this, (sender, args) -> getFileResources());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILE_UPLOAD_FINISHED, GeneralFileUpload.this, (sender, args) -> getFileResources());
    }

    public void clearAndAdd() {
        dlg.getUploadedFiles().clear();
        getFileResources();
    }

    public void clear() {
        dlg.getUploadedFiles().clear();
    }

    public FileItem[] getAttachedFiles() {
        ArrayList<FileResource> files = getAttachments();

        /*if (component != null && entityId == null && getAttachments() != null) {
            Collections.addAll(files, getAttachments());
        }*/
        if (dlg != null && dlg.getFileResources() != null && !dlg.getFileResources().isEmpty()) {
            for (FileResource file : dlg.getFileResources()) {
                if (!files.contains(file)) {
                    files.add(file);
                }
            }
        }
        FileItem[] attachments = new FileItem[files.size()];
        int i = 0;
        for (FileResource file : files) {
            attachments[i] = new FileItem();
            attachments[i].setId(file.getObjectId());
            attachments[i].setFileName(file.getName());
            attachments[i].setDescription(file.getDescription());
            attachments[i].setGoogleDocumentLink(file.getGoogleDownloadLink());
            attachments[i].setOfficeDocumentLink(file.getOfficeDownloadLink());
            attachments[i].setUploadType(file.getUploadType());
            attachments[i].setTypeId(file.getTypeId());
            attachments[i].setDocumentID(file.getDocumentID());
            attachments[i].setExpireDate(file.getExpireDate());
            attachments[i].setIssuedDate(file.getIssuedDate());
            i++;
        }
        return attachments;
    }

    public FileItem[] getAttachedFiles1() {
        FileItem[] attachments = new FileItem[savedFiles.size()];

        int i = 0;
        for (FileResource file : savedFiles.values()) {
            attachments[i] = new FileItem();
            attachments[i].setId(file.getObjectId());
            attachments[i].setFileName(file.getName());
            i++;
        }
        return attachments;
    }

    public ArrayList<FileResource> getNewlyAttachedFiles() {
        /*ArrayList<FileResource> files = new ArrayList<>();
        if (component != null && getAttachments() != null) {
            Collections.addAll(files, getAttachments());
        }*/
        return getAttachments();
    }

    public void onDeleteCommand(Command deleteCommand) {
        this.deleteCommand = deleteCommand;
    }

    public void onFinishCommand(Command finishCommand) {
        this.finishCommand = finishCommand;
    }

    public VerticalPanel getPanel() {
        return panel;
    }

    public void addAdditionalAttachments(FileResource[] result, boolean isEditableForm) {
//        this.additionalAttachments = result;
        if (component != null) {
            component.supplyProvider(result, isEditableForm);
        }
    }

    public void setCustomFieldData(int folderID, Integer entityID) {
        this.folderId = folderID;
        this.entityId = entityID;
        createFolderResours(true);

    }

    public void setIDData(int folderID, Integer entityID) {
        folderId = folderID;
        entityId = entityID;
        if (folderResource != null) {
            folderResource.setEntityId(entityId);
        }
    }

    public boolean validated() {
        return component.validate();
    }

    public ArrayList<FileResource> getAttachments() {
        ArrayList<FileResource> attachments = new ArrayList<>();
        if (component == null) {
            return attachments;
        }
        FileResource[] resources = component.getAttachments();
        if (resources.length > 0) {
            for (FileResource resource : resources) {
                if (!deletedIDs.contains(resource.getObjectId())) {
                    attachments.add(resource);
                }
            }
        }
        return attachments;
    }

    public void setButtonClick(Command buttonClick) {
        this.buttonClick = buttonClick;
    }

    public void addCovertedAttachments(ArrayList<FileResource> fileResources) {
        loadFileResources(fileResources, false);
    }
}
