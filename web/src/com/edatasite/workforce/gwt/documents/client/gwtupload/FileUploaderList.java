package com.edatasite.workforce.gwt.documents.client.gwtupload;

import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectIcons;
import com.edatasite.workforce.gwt.documents.client.gwtupload.events.UploadingCompletedEvent;
import com.edatasite.workforce.gwt.documents.client.gwtupload.events.UploadingCompletedEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;
import java.util.List;

public class FileUploaderList extends FileUploader implements UploadingCompletedEventHandler {

    private final TreeSelectIcons icons = GWT.create(TreeSelectIcons.class);
    private Div files;
    private FlowPanel dropZoneAndButtonContainer;
    private HashMap<String, FileWidget> filesNamesMap;

    public FileUploaderList(Options options, Div files) {
        super(options);
        dropZoneAndButtonContainer = new FlowPanel();
        dropZoneAndButtonContainer.addStyleName("dropzone");
        styleizeDropZone();
        this.files = files;
        filesNamesMap = new HashMap<>();
        initialize();
        addUploadingCompletedEventHandler(this);
    }

    private void styleizeDropZone() {
        Div wrapper = new Div();
        Div iconDiv = new Div("kpi-upload__upload-icon");
//        MaterialIcon downloadIcon = new MaterialIcon();
//        downloadIcon.addStyleName("ficon--download-cloud");
//        iconDiv.add(downloadIcon);
        Image uploadIcon = new Image();
        uploadIcon.setUrl("/mainStyles/new-ui/images/upload.svg");
        iconDiv.add(uploadIcon);
        Div dropzoneMessage = getDropZoneMessage();
        Div dropzoneSubMessage = new Div("dropzone-submessage kpi-upload__content-wrapper");
        dropzoneSubMessage.getElement().setInnerHTML(wfmStrings.youCanDragNDropFilesIntoThisArea());
        wrapper.add(iconDiv);
        wrapper.add(dropzoneMessage);
        wrapper.add(dropzoneSubMessage);
        dropZoneAndButtonContainer.add(wrapper);
    }

    private Div getDropZoneMessage() {
        Span span = new Span(wfmStrings.dragAndDropFilesHereOr() + " ");
        Anchor anchor = new Anchor(wfmStrings.browse());
        Div dropzoneMessage = new Div("dropzone-message kpi-upload__content-wrapper");
        dropzoneMessage.add(span);
        dropzoneMessage.add(anchor);
        return dropzoneMessage;
    }

    public void add(Widget widget) {
        mainContainer.add(widget);
    }

    @Override
    protected FlowPanel getDropZone() {
        return dropZoneAndButtonContainer;
    }

    @Override
    public void updateView(List<FileInfo> fileInfos) {
        clearFiles();
        initAddFileInfos(fileInfos);
    }

    @Override
    protected void onDropEvent() {
        getDropZone().getElement().removeClassName("dropzoneHover");
    }

    @Override
    protected void onDragLeaveEvent() {
        getDropZone().getElement().removeClassName("dropzoneHover");
    }

    @Override
    protected void onDragEnterEvent() {
        getDropZone().getElement().addClassName("dropzoneHover");
    }

    @Override
    protected void updateExactFileInfo(String justAddedId) {
        updateExactFileInfo(justAddedId, false);
    }

    @Override
    protected void updateExactFileInfo(String justAddedId, boolean failed) {
        FileInfo fileInfo = fileInfos.get(justAddedId);
        FileWidget fileWidget;
        if (filesNamesMap.containsKey(fileInfo.getId())) {
            fileWidget = filesNamesMap.get(fileInfo.getId());
        } else {
            fileWidget = new FileWidget(justAddedId, fileInfo.getName());

            int row = files.getWidgetCount();

            fileWidget.setRemoveCommand(() -> {
                files.remove(fileWidget);
                fileInfos.remove(fileWidget.getFileId());
            });

            if (!filesNamesMap.containsKey(fileInfo.getId())) {
                files.add(fileWidget);
                filesNamesMap.put(fileInfo.getId(), fileWidget);
            }
        }

        if (failed) {
            failedList.add(fileInfo.getName());
        }
        fileWidget.setCompleted(fileInfo.getPercentageReadyAsDouble());
//        html.setHTML("<span style=\"word-break: break-all;\">" + fileInfo.getName() + (failed ? " <em style=\"color:red\"><i><b>(Failed)</b></i></em>" : "") + ", " + fileInfo.getPercentageReady() + "% </span>"); //TODO Restore
    }

    public void addFile(FileWidget fileWidget) {
        files.add(fileWidget);
    }

    public void removeFile(FileWidget fileWidget) {
        files.remove(fileWidget);
    }

    @Override
    public void onUploadingCompleted(UploadingCompletedEvent event) {
        GWT.log("all files uploaded");
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FILE_UPLOAD_FINISHED, null, FileUploaderList.this);
    }

    public void startUpload() {
        onInputChange(getFileInput());
    }

    public void restart(Integer entityID) {
        if (entityID != null) {
            clearFiles();
            filesNamesMap.clear();
            fileInfos.clear();
        }
    }

    protected void clearFiles() {
        files.clear();
    }

}