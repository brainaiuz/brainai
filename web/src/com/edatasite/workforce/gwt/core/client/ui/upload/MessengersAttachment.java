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
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_EMPLOYEE_PROFILE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_PRODUCTS_SERVICES;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PRODUCT_UPLOAD_FILES;

public class MessengersAttachment extends Composite {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();

    private Span uploadButton;

    private FileUploadDialog dlg;

    private FolderResource folderResource;

    private final int folderType;

    private Integer folderId;

    private GeneralAttachmentLinksComponent component;

    private Integer entityId;

    private boolean isDownloadable = true;

    private final HashMap<Integer, FileResource> savedFiles = new HashMap<>();

    private Command deleteCommand, finishCommand, command, buttonClick;

    private FileResource[] additionalAttachments;
    private boolean isEditable = false;
    private String typeCode;
    private final ArrayList<SelectItem> typeNames = new ArrayList<>();
    private final ArrayList<Integer> deletedIDs = new ArrayList<>();

    ClickHandler uploadHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            dlg = new FileUploadDialog(folderResource, Utils.getFileUploadType(), Utils.getMaxFileUploadSize(), true, isDownloadable,true);
            dlg.addUploadListener(command, FileUploadDialog.FileUploadEvents.FILE_UPLOADED, FileUploadDialog.FileUploadEvents.FILE_REMOVED);
        }
    };

    /**
     * @param folderType (F_PROJECT, F_TASK ...)
     * @param folderId   Folder Id
     * @param entityId   EdsObject Id
     */
    public MessengersAttachment(int folderType, Integer folderId, final Integer entityId) {
        this(folderType, folderId, entityId, true);
        uploadButton = createUploadButton();
        start();
    }

    public MessengersAttachment(int folderType, Integer folderId, final Integer entityId, boolean isDownloadable) {
        this.folderType = folderType;
        this.entityId = entityId;
        this.folderId = folderId;
        this.isDownloadable = isDownloadable;
        uploadButton = createUploadButton();
        start();
    }

    private Span createUploadButton() {
        Span button = new Span();
        button.add(new Image("../mainStyles/new-ui/icons/upload.svg"));
        button.setStyleName("btn btn--icon");

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dlg = new FileUploadDialog(folderResource, Utils.getFileUploadType(), Utils.getMaxFileUploadSize(), true, isDownloadable,true);
                dlg.addUploadListener(command, FileUploadDialog.FileUploadEvents.FILE_UPLOADED, FileUploadDialog.FileUploadEvents.FILE_REMOVED);
            }
        });

        button.getElement().setId("Upload_button");

        return button;
    }

    private void start() {
        if (folderId == null) {
            CommonService.App.get().getTempFolderByCompanyID(Utils.isWebForm() ? Utils.getEncryptedCompanyID() : null, null, new AbstractAsyncCallback<FolderResource>() {
                public void success(FolderResource result) {
                    MessengersAttachment.this.folderResource = result;
                    getFileMaxSize();
                }
            });
        } else {
            createFolderResource(false);
        }
        initWidget(uploadButton);
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
        createFolderResource(false);
        initWidget(uploadButton);
    }

    private void createFolderResource(final boolean isCustomField) {
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
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, false, MessengersAttachment.this);
                    }
                }
            });
        }
    };

    private void getFileResources() {
        if (entityId != null) {
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

        addSavedFiles(fileResources);

    }

    private void addSavedFiles(ArrayList<FileResource> fileResources) {
        for (FileResource file : fileResources) {
            savedFiles.put(file.getObjectId(), file);
        }
    }

    private void init() {
        uploadButton = new Span();
        uploadButton.add(new Image("../mainStyles/new-ui/icons/upload.svg"));
        uploadButton.setStyleName("btn btn--icon");

        uploadButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dlg = new FileUploadDialog(folderResource, Utils.getFileUploadType(), Utils.getMaxFileUploadSize(), true, isDownloadable,true);
                dlg.addUploadListener(command, FileUploadDialog.FileUploadEvents.FILE_UPLOADED, FileUploadDialog.FileUploadEvents.FILE_REMOVED);
                if (buttonClick != null) {
                    buttonClick.execute();
                }
            }
        });

        uploadButton.getElement().setId("Upload_button");

        folderResource.setFileType(folderType);
        dlg = new FileUploadDialog(folderResource, Utils.getFileUploadType(), Utils.getMaxFileUploadSize(), false, isDownloadable,true);

        command = () -> getFileResources();
        dlg.addUploadListener(command, FileUploadDialog.FileUploadEvents.FILE_UPLOADED, FileUploadDialog.FileUploadEvents.FILE_REMOVED);

        getFileResources();

        if ((F_PRODUCTS_SERVICES == folderType && Utils.hasPermission(ACCOUNTING_PRODUCT_UPLOAD_FILES)) || F_EMPLOYEE_PROFILE == folderType || Constants.F_OPPORTUNITY == folderType || folderResource.getPermission().isWrite() || folderId == null) {
            uploadButton.addStyleName("summaryFileUpload__action");
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DOCUMENTS_UPLOAD_FILES, MessengersAttachment.this, (sender, args) -> getFileResources());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILE_UPLOAD_FINISHED, MessengersAttachment.this, (sender, args) -> getFileResources());
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
        // Implement your logic to get attached files here
        return null;
    }

    public ArrayList<FileResource> getNewlyAttachedFiles() {
        // Implement your logic to get newly attached files here
        return null;
    }

    public void onDeleteCommand(Command deleteCommand) {
        this.deleteCommand = deleteCommand;
    }

    public void onFinishCommand(Command finishCommand) {
        this.finishCommand = finishCommand;
    }

    public void setButtonClick(Command buttonClick) {
        this.buttonClick = buttonClick;
    }

    public boolean validated() {
        // Implement your validation logic here
        return true;
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
}
