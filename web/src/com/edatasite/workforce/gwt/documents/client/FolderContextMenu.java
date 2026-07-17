package com.edatasite.workforce.gwt.documents.client;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.enums.FileUploadType;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.commands.CopyCommand;
import com.edatasite.workforce.gwt.documents.client.commands.CutCommand;
import com.edatasite.workforce.gwt.documents.client.commands.EmptyTrashCommand;
import com.edatasite.workforce.gwt.documents.client.commands.NewFolderCommand;
import com.edatasite.workforce.gwt.documents.client.commands.PasteCommand;
import com.edatasite.workforce.gwt.documents.client.commands.PropertiesCommand;
import com.edatasite.workforce.gwt.documents.client.commands.RefreshCommand;
import com.edatasite.workforce.gwt.documents.client.commands.RenameFolderCommand;
import com.edatasite.workforce.gwt.documents.client.commands.RestoreTrashCommand;
import com.edatasite.workforce.gwt.documents.client.commands.ToTrashCommand;
import com.edatasite.workforce.gwt.documents.client.commands.UploadFileCommand;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jamshid.asatillayev
 * Date: Jan 18, 2011
 * Time: 4:16:21 PM
 */
public class FolderContextMenu extends PopupPanel implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    

    private MenuBar contextMenu;

    private int size = 0;

    private DocUtils docUtils;
    private DocumentImages.Images images;

    public FolderContextMenu() {
        super(true);
        this.images = DocumentImages.get();
        setAnimationEnabled(true);
        createMenu();
    }

    private void createMenu() {
        docUtils = new DocUtils();
        contextMenu = new MenuBar(true);
        if (docUtils.getSelectedItem() != null) {
            if (!docUtils.isOthersShare() && !docUtils.isAllFiles()) {
                addNewFolderItem();
                addUploadItem();
                addCopyItem();
                addCutItem();
                addPasteItem();
                addRenameItem();
                addRestoreItem();
            }
            if (!docUtils.isOthersShare() && !docUtils.isAllFiles()) {
                addEmptyTrashItem();
                addDeleteAndToTrash();
                if (docUtils.getCurSelectedItem() instanceof FolderResource && ((FolderResource) docUtils.getCurSelectedItem()).getFileType() != F_COMPANY_PUBLIC_ROOT) {
                    addSharingItem();
                }
                addPropertiesItem();
            }
            addRefreshItem();
            add(contextMenu);
        }
    }

    public void showPopup(final int x, final int y) {

        int left = x;
        int top = y;

        if (left < 0) {
            left = 0;
        }
        if (top < 0) {
            top = 0;
        }
        if (Window.getClientHeight() - top < size * 25) {
            top = Window.getClientHeight() - size * 25 - 15;
        }
        this.setPopupPosition(left, top);
        this.setStyleName("action-listing-popup");
        this.show();
    }

    private void addPasteItem() {

        if (docUtils.canPaste()) {
            Object selection = DocumentsView.get().getCurrentSelection();
            String pasteLabel = "";
            if (selection instanceof FolderResource || (selection instanceof FileResource && ((FileResource)selection).isFolder()) ) {
                pasteLabel = wfmStrings.paste();

            } else if (selection instanceof FileResource) {
                pasteLabel = wfmStrings.pasteFile();

            } else if (selection instanceof List) {
                pasteLabel = wfmStrings.pasteFile();

            }

            contextMenu.addItem(docUtils.createHtmlImage(pasteLabel, images.paste()), true, new PasteCommand(this));
        }
    }

    private void addUploadItem() {
        DocumentsService.App.get().getEnableUploadTypes(new AbstractAsyncCallback<HashMap<String, Boolean>>() {
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(HashMap<String, Boolean> result) {
                createStorageMenues(result);
            }
        });
    }

    private void createStorageMenues(HashMap<String, Boolean> result) {
        if (docUtils.canUpload()) {
            if (result.get(AMAZON) != null && result.get(AMAZON)) {
                contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.uploadFile(), images.fileUploadAmazon()), true, new UploadFileCommand(this, FileUploadType.AMAZON));
                size++;
            }
            if (docUtils.getCurSelectedItem() instanceof FolderResource && ((FolderResource) docUtils.getCurSelectedItem()).getFileType() != F_COMPANY_PUBLIC_ROOT) {
                if (result.get(GOOGLE) != null && result.get(GOOGLE)) {
                    contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.uploadToMyGoogleDocs(), images.fileUploadGoogle()), true, new UploadFileCommand(this, FileUploadType.GOOGLE_DOCUMENTS));
                    size++;
                }
                if (result.get(OFFICE_365) != null && result.get(OFFICE_365)) {
                    contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.uploadToOfficeDocuments(), images.fileUploadGoogle()), true, new UploadFileCommand(this, FileUploadType.OFFICE_DOCUMENTS));
                    size++;
                }
                if (result.get(UPLOAD_SHARE_POINT) != null && result.get(UPLOAD_SHARE_POINT)) {
                    contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.uploadToOfficeSharePointDocuments(), images.fileUploadGoogle()), true, new UploadFileCommand(this, FileUploadType.OFFICE_SHARE_POINT_DOCUMENTS));
                    size++;
                }
            }
        }
    }

    private void addNewFolderItem() {
        if (docUtils.canCreateFolder()) {
            contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.create(), images.folderNew()), true, new NewFolderCommand(this));
            size++;
        }
    }

    private void addCopyItem() {
        if (docUtils.canCopy()) {
            contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.copy(), images.copy()), true, new CopyCommand(this));
            size++;
        }
    }

    private void addCutItem() {
        if (docUtils.canDelete()) {
            contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.cut(), images.cut()), true, new CutCommand(this));
            size++;
        }
    }

    private void addDeleteAndToTrash() {
        if (docUtils.canDelete()) {
            /*if (DocumentsView.get().getCurrentSelection() != null) {
                if (DocumentsView.get().getCurrentSelection() instanceof FolderResource) {
                    FolderResource fol = (FolderResource) DocumentsView.get().getCurrentSelection();
                    if (!fol.isDeleted()) {
                        contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.moveToTrash(), images.trash()), true, new ToTrashCommand(this));
                        size++;
                    }
                }
            }*/
            if (DocumentsView.get().getCurrentSelection() != null && ((FolderResource) DocumentsView.get().getCurrentSelection()).isDeleted()) {
                contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.delete(), images.delete()), true, new DeleteFolderCommand(this));
            } else {
                contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.delete(), images.delete()), true, new ToTrashCommand(this)); //new DeleteFolderCommand(this));
            }
            size++;
        }
    }

    private void addRefreshItem() {
        contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.refresh(), images.refresh()), true, new RefreshCommand(this));
        size++;
    }

    private void addRenameItem() {
        if (docUtils.canRename()) {
            contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.rename(), images.rename()), true, new RenameFolderCommand(this));
            size++;
        }
    }

    private void addSharingItem() {
        if (docUtils.canShare()) {
            contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.share(), images.sharing()), true, new PropertiesCommand(this, 1));
            size++;
        }
    }

    private void addPropertiesItem() {
        if (!docUtils.isRootItems()) {
            contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.properties(), images.viewText()), true, new PropertiesCommand(this, 0));
            size++;
        }
    }

    private void addRestoreItem() {
        if (docUtils.canRestore()) {
            contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.restoreFolderAndContevt(), images.viewText()), true, new RestoreTrashCommand(this));
            size++;
        }
    }

    private void addEmptyTrashItem() {
        if (docUtils.canEmptyTrash()) {
            contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.emptyTrash(), images.delete()), true, new EmptyTrashCommand(this));
            size++;
        }
    }

    public int getSize() {
        return size;
    }
}
