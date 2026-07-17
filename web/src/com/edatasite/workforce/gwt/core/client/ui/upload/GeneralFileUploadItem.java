package com.edatasite.workforce.gwt.core.client.ui.upload;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUploadDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Faxriddin
 * Date: 28/07/15
 */
public class GeneralFileUploadItem extends Composite {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private int folderType;
    private HashMap<Integer, FileResource> savedFiles = new HashMap<>();
    private GWTFileUploadDialog uploadDialog;
    private VerticalPanel attachViewPanel;
    private VerticalPanel attachedFilesPanel;
    private WfmButton2 attachLink;

    public GeneralFileUploadItem(int folderType) {
        this.folderType = folderType;
        draw();
    }

    public FileResource[] getAttachedFiles() {
        return savedFiles.values().toArray(new FileResource[]{});
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

    public void setFiles(Integer folderId, Integer entityId, boolean isViewMode) {
        DocumentsService.App.get().getFileResources(folderType, folderId, entityId, new AbstractAsyncCallback<ArrayList<FileResource>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ArrayList<FileResource> fileResources) {
                if (fileResources != null && fileResources.size() > 0) {
                    for (FileResource file : fileResources) {
                        savedFiles.put(file.getObjectId(), file);
                    }
                    showAttachmentsPanel(isViewMode);
                }
            }
        });

    }

    private void draw() {
        attachViewPanel = new VerticalPanel();
        attachViewPanel.setSpacing(2);

        attachLink = new WfmButton2(wfmStrings.chooseFile());
        attachLink.getElement().setClassName("gwt-FileUpload");
        uploadDialog = new GWTFileUploadDialog(folderType, null, null);
        uploadDialog.onLoadCommand(() -> {
            HashMap<Integer, FileResource> files = uploadDialog.getUploadedFiles();
            if (files != null && files.size() > 0) {
                for (FileResource fr : files.values()) {
                    savedFiles.put(fr.getObjectId(), fr);
                }
            }
            showAttachmentsPanel(false);
        });
        uploadDialog.setActivator(attachLink);
        attachViewPanel.add(attachLink);

        attachedFilesPanel = new VerticalPanel();

        attachViewPanel.add(attachedFilesPanel);


        initWidget(attachViewPanel);
    }

    private void showAttachmentsPanel(boolean isViewMode) {
        attachedFilesPanel.clear();
        if (savedFiles.size() > 0) {
            FileResource[] files = savedFiles.values().toArray(new FileResource[]{});
            Arrays.sort(files, (o1, o2) -> o2.getObjectId().compareTo(o1.getObjectId()));
            for (final FileResource fileResource : files) {
                final String action = fileResource.getDownloadUrl(GWT.getHostPageBaseURL() + CommandConstants.COMMON_URL + "/");
                final HorizontalPanel hp = new HorizontalPanel();
                hp.setSpacing(5);
                SimpleLink link = new SimpleLink(fileResource.getFileName());
                link.addClickHandler(clickEvent -> Window.open(action, "_blank", ""));
                hp.add(link);

                Icon remove = new Icon();
                remove.addStyleName("ficon--trash pointer");
                remove.addClickHandler(clickEvent -> {
                    DocumentsService.App.get().deleteFile(fileResource.getObjectId(), new AbstractAsyncCallback() {
                        public void success(Object result) {
                            savedFiles.remove(fileResource.getObjectId());
                        }
                    });
                    hp.removeFromParent();
                });
                if (!isViewMode) {
                    hp.add(remove);
                }
                attachedFilesPanel.add(hp);
            }
        }
    }

    public void setViewMode() {
        attachLink.setEnabled(false);
    }
}
