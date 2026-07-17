package com.edatasite.workforce.gwt.documents.client.gwtupload;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.gwtupload.events.FileUploadingCompletedEvent;
import com.edatasite.workforce.gwt.documents.client.gwtupload.events.UploadingCompletedEvent;
import com.edatasite.workforce.gwt.documents.client.gwtupload.events.UploadingCompletedEventHandler;
import com.edatasite.workforce.gwt.documents.client.gwtupload.file.File;
import com.edatasite.workforce.gwt.documents.client.gwtupload.file.FileList;
import com.edatasite.workforce.gwt.documents.client.gwtupload.xhr.DataTransferAdvanced;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsServiceAsync;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import gwt.material.design.client.ui.html.Div;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public abstract class FileUploader extends Composite implements UploadProgressHandlers, HasHandlers {
    private HandlerManager handlerManager;
    private UploadButton uploadButton;
    private FileInputElement fileInput;
    private Options options;
    private int filesInProgress = 0;
    private UploadHandlerAbstract uploadHandler;
    HashMap<String, FileInfo> fileInfos;
    HashSet<String> failedList;
    protected static final WfmMessages messages = WfmMessages.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected Div mainContainer;
    private Label label;
    private DocumentsServiceAsync documentsService = DocumentsService.App.get();

    protected abstract FlowPanel getDropZone();

    protected abstract void updateExactFileInfo(String justAddedId);

    protected abstract void updateExactFileInfo(String justAddedId, boolean failed);

    protected abstract void onDropEvent();

    protected abstract void onDragLeaveEvent();

    protected abstract void onDragEnterEvent();

    public abstract void updateView(List<FileInfo> files);

    FileUploader(Options options) {
        handlerManager = new HandlerManager(this);
        this.options = options;
        fileInfos = new HashMap<>();
        failedList = new HashSet<>();

        uploadHandler = createUploadHandler();
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
        super.fireEvent(event);
    }

    void addUploadingCompletedEventHandler(UploadingCompletedEventHandler handler) {
        handlerManager.addHandler(UploadingCompletedEvent.TYPE, handler);
    }

    protected final void initialize() {
        mainContainer = new Div("kpi-upload__dropzone");
        createUploadButton(getDropZone());
        mainContainer.add(getDropZone());
        initWidget(mainContainer);
        addDragAndDropHandlers();
    }

    public void setStorage(String storage) {
        options.setStorage(storage);
    }

    public HashMap<String, FileInfo> getFileInfos() {
        return fileInfos;
    }

    private UploadHandlerAbstract createUploadHandler() {
        final UploadHandlerAbstract uploadHandler;
        if (options.useAdvancedUploader()) {
            uploadHandler = new UploadHandlerXhr(this, options);
        } else {
            uploadHandler = new UploadHandlerForm(this, options);
        }
        return uploadHandler;
    }

    private void createUploadButton(FlowPanel container) {
        if (uploadButton != null) {
            uploadButton.reset();
        }
        uploadButton = new UploadButton(container, options.isMultiple() && options.useAdvancedUploader());
        fileInput = uploadButton.getInput();
        Event.sinkEvents(fileInput, Event.ONCLICK | Event.ONMOUSEUP);
        Event.setEventListener(fileInput, event -> {
            checkForStorage();
        });
    }

    private void initLabel(boolean visible) {
        if (label == null) {
            label = new Label("Sorry, you have reached the storage limit for uploading documents, please request for additional storage from support@kpi.com.");
            label.getElement().getStyle().setColor("red");
            mainContainer.add(label);
            LoadingPanel.loading(false);
        }
        label.setVisible(visible);
    }

    private void checkForStorage() {
        if (Utils.getMaxStorage() != null && Utils.getUsedStorage() != null && Utils.getMaxStorage() < Utils.getUsedStorage()) {
//            initLabel(true);

            fileInput.setDisabled(true);

        } else {
            fileInput.setDisabled(false);
            addInputChangeHandlers(fileInput);
        }
    }

    private native void addInputChangeHandlers(FileInputElement element) /*-{
        var that = this;
        element.onchange = function () {
            that.@com.edatasite.workforce.gwt.documents.client.gwtupload.FileUploader::onInputChange(Lcom/edatasite/workforce/gwt/documents/client/gwtupload/FileInputElement;)(element);
        };
    }-*/;

    public void onInputChange(FileInputElement input) {
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FILE_UPLOAD_STARTED, null, FileUploader.this);
        if (uploadHandler instanceof UploadHandlerXhr) {
            uploadFileList(input.getFiles());
        } else {
            if (validateFile(input)) {
                uploadFile(input);
            }
        }
        createUploadButton(getDropZone());
    }

    private void uploadFileList(FileList files) {
        for (File file : files) {
            if (validateFile(file) && !fileInput.isDisabled()) {
                uploadFile(file);
            }
        }
    }

    private void uploadFile(Object file) {
        String id = uploadHandler.add(file);
        String fileName = uploadHandler.getName(id);
        if (fileName != null) {
            fileName = fileName.replace("\\+", "");
        }
        onSubmit(id, fileName);
        uploadHandler.upload(id);
    }

    private boolean validateFile(FileInputElement input) {
        String name = input.getValue().replace("\\+", "").replaceAll(".*(\\/|\\\\)", "");
        int size = -1;
        return validate(name, size);
    }

    private boolean validateFile(File file) {
        String name = file.getName();
        int size = file.getSize();
        return validate(name, size);
    }

    private boolean validate(String name, int size) {
        final String error = validateFile(name, size);
        if (error != null) {
            Info.show(error, Info.Type.WARNING);
//            addFileToList(UUID.uuid(), name, size, error);
        }
        return error == null;
    }

    /**
     * Checks file for being valid for upload
     *
     * @param name file name
     * @param size file size
     * @return Error cause or null if OK
     */
    private String validateFile(String name, int size) {
        if (size == 0) {
            return messages.fileSizeIsZero();
        }
        if (size > 0 && options.getMinSize() > 0 && size < options.getMinSize()) {
            return messages.fileIsTooSmall(formatSize(options.getMinSize()));
        }
        if (size > 0 && options.getMaxSize() > 0 && size > options.getMaxSize()) {
//            return messages.fileIsTooLarge("100 MB");
            return messages.fileIsTooLarge(formatSize(options.getMaxSize()));
        }
        checkForStorage();
        return null;
    }

    private void addDragAndDropHandlers() {
        if (options.useAdvancedUploader()) {
            addDomHandler(event -> {
                final EventTarget eventTarget = event.getNativeEvent().getEventTarget();
                Element el = Element.as(eventTarget);
                if (el == fileInput) {
                    DataTransferAdvanced dta = (DataTransferAdvanced) event.getDataTransfer();
                    uploadFileList(dta.getFiles());
                    onDropEvent();
                }
                event.preventDefault();
            }, DropEvent.getType());
            addDomHandler(event -> {
                DataTransferAdvanced dta = (DataTransferAdvanced) event.getDataTransfer();
                String effect = dta.getEffectAllowed();
                if (effect.equals("move") || effect.equals("linkmove")) {
                    // for FF (only move allowed)
                    dta.setDropEffect("move");
                } else {
                    // for Chrome
                    dta.setDropEffect("copy");
                }
                event.stopPropagation();
                event.preventDefault();
            }, DragOverEvent.getType());
            addDomHandler(event -> {
                final EventTarget eventTarget = event.getNativeEvent().getEventTarget();
                Element el = Element.as(eventTarget);
                if (el == fileInput) {
                    onDragEnterEvent();
                }
            }, DragEnterEvent.getType());
            addDomHandler(event -> {
                final EventTarget eventTarget = event.getNativeEvent().getEventTarget();
                Element el = Element.as(eventTarget);
                if (el == fileInput) {
                    onDragLeaveEvent();
                }
            }, DragLeaveEvent.getType());
        }
    }

    private String formatSize(int bytes) {
        if (bytes == -1) {
            return "";
        }
        final int kbSize = 1024;
        final int mbSize = kbSize * kbSize;

        boolean mb = false;
        int divideBy = kbSize;
        if (bytes >= mbSize) {
            mb = true;
            divideBy = mbSize;
        }
        final String s = String.valueOf((double) bytes / divideBy);
        int endIndex = s.indexOf('.') + 2;
        if (endIndex > s.length()) {
            endIndex = s.length() - 1;
        }
        return s.substring(0, endIndex) + " " + (mb ? messages.sizeMB() : messages.sizeKB());
    }

    @Override
    public void onCancel(String id, String filename) {
        fireUploadCompletedIfRequired();
    }

    @Override
    public void onComplete(String id, String filename, JSONValue response) {
        if (response.isArray() != null) {
            JSONArray array = (JSONArray) response;
            for (int i = 0; i < array.size(); i++) {
                filesInProgress--;
                final JSONObject value = (JSONObject) array.get(i);
                final JSONValue size = value.get("size");
                final JSONValue url = value.get("url");
                final JSONValue type = value.get("type");
                final String url2 = String.valueOf(url);
                final FileInfo fi = new FileInfo(id, url2, filename, Integer.valueOf(String.valueOf(size)), Integer.valueOf(String.valueOf(size)), String.valueOf(type), null, false, value);
                fileInfos.put(id, fi);
                updateExactFileInfo(id);
                uploadHandler.deQueueFiles(id);
                fireEvent(new FileUploadingCompletedEvent(fi));
                fireUploadCompletedIfRequired();
            }
        }
    }

    @Override
    public void onFailure(String id, String filename) {
        filesInProgress--;
        uploadHandler.deQueueFiles(id);
        updateExactFileInfo(id, true);
        fireUploadCompletedIfRequired();
    }

    private void fireUploadCompletedIfRequired() {
        if (filesInProgress == 0 || uploadHandler.getQueue().isEmpty()) {
            fireEvent(new UploadingCompletedEvent());

            if (!failedList.isEmpty()) {
                StringBuilder alert = new StringBuilder("<b>" + (failedList.size() == 1 ? "This file is failed to upload, Please re-upload it:" : "These files're failed to upload, Please re-upload them:") + "</b>");

                for (String fname : failedList) {
                    alert.append("<p>").append(fname).append("</p>");
                }
                failedList.clear();

                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                messageBox.setTitle("Failed List");
                messageBox.setMessage(alert.toString());
                messageBox.open();
            }
        }
    }

    private native static boolean preventLeaveInProgress() /*-{
        var self = this;

        qq.attach(window, 'beforeunload', function (e) {
            if (!self._filesInProgress) {
                return;
            }

            var e = e || window.event;
            // for ie, ff
            e.returnValue = self._options.messages.onLeave;
            // for webkit
            return self._options.messages.onLeave;
        });
    }-*/;

    // original comment said 'return false to cancel submit'?! wtf
    @Override
    public int onSubmit(String id, String filename) {
        filesInProgress++;
        addFileToList(id, filename, -1, null);
        return filesInProgress;
    }

    private void addFileToList(String id, String filename, int size, String error) {
        fileInfos.put(id, new FileInfo(id, id + "_upld_" + filename, filename, size, -1, null, error, false, null));
        updateExactFileInfo(id);
    }

    @Override
    public void onProgress(String id, String filename, int loaded, int total) {
        fileInfos.put(id, new FileInfo(id, null, filename, total, loaded, null, null, false, null));
        updateExactFileInfo(id);
    }

    private void initAddFileInfo(FileInfo file) {
        final String uuid = UUID.uuid();
        fileInfos.put(uuid, file);
        updateExactFileInfo(uuid);
    }

    void initAddFileInfos(List<FileInfo> files) {
        if (files != null) {
            for (FileInfo file : files) {
                initAddFileInfo(file);
            }
        }
    }

    private int filesDueToUpload() {
        int result = 0;
        for (FileInfo fileInfo : fileInfos.values()) {
            if (fileInfo.dueToUpload()) {
                result++;
            }
        }
        return result;
    }

    protected int filesUploaded() {
        int result = 0;
        for (FileInfo fileInfo : fileInfos.values()) {
            if (fileInfo.dueToUpload() && fileInfo.uploadingHasFinished()) {
                result++;
            }
        }
        return result;
    }

    protected int totalProgress() {
        int result = 0;
        for (FileInfo fileInfo : fileInfos.values()) {
            if (fileInfo.dueToUpload()) {
                result += fileInfo.getPercentageReady();
            }
        }
        return result / filesDueToUpload();
    }

    FileInputElement getFileInput() {
        return fileInput;
    }
}
