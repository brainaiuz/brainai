package com.edatasite.workforce.gwt.core.client.ui.upload;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.UploadFile;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 30-May-2009
 * Time: 14:44:24
 * To change this template use File | Settings | File Templates.
 */
public class UploadForm extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final SimpleLink addLink;
    private final VerticalPanel panel;
    private final String fromSection;
    private List uploadFiles;
    private Command uploadFinished;

    public Command getUploadFinished() {
        return uploadFinished;
    }

    public void setUploadFinished(Command uploadFinished) {
        this.uploadFinished = uploadFinished;
    }

    public List getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(List uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public UploadForm(boolean viewOnly) {
        this(viewOnly, null);
    }

    public UploadForm(final boolean viewOnly, String fromSection) {
        this.fromSection = fromSection;
        uploadFiles = new ArrayList();
        panel = new VerticalPanel();
        initWidget(panel);
        addLink = new SimpleLink(wfmStrings.addAnotherAttachment(), SimpleLink.ADD_ICON);
        addLink.addClickHandler(sender -> addFileField(viewOnly));

        addFileField(viewOnly);
    }

    private void addFileField(final boolean viewOnly) {
        final UploadFile uploadFile = new UploadFile(viewOnly, null, fromSection);
        uploadFile.setUploadFinnished(() -> isFinished());
        uploadFiles.add(uploadFile);
        final HorizontalPanel hp = new HorizontalPanel();
        SimpleLink removeLink = new SimpleLink(wfmStrings.delete(), SimpleLink.REMOVE_ICON);
        removeLink.setWidth("60px");
        removeLink.addClickHandler(sender -> {
            final int index = uploadFiles.indexOf(uploadFile);
            if (uploadFile.getId() == null) {
                uploadFiles.remove(index);
                panel.remove(hp);
            } else {
                WfmMessageBox confirmationDialog = new WfmMessageBox(IconEnum.WARN, Action.YesNo, wfmStrings.sureYouWantToDelete());
                confirmationDialog.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        uploadFiles.remove(index);
                        panel.remove(hp);
                    }
                });
                confirmationDialog.center();
            }
        });
        uploadFile.setRemoveIfPressedESC(() -> {
            uploadFiles.remove(uploadFile);
            panel.remove(hp);
            addFileField(viewOnly);
        });
        hp.add(uploadFile);
        hp.add(removeLink);
        hp.setCellVerticalAlignment(removeLink, HasAlignment.ALIGN_MIDDLE);

        panel.remove(addLink);
        panel.add(hp);
        panel.add(addLink);
    }

    public boolean isEmpty() {
        boolean t = true;
        for (Object uploadFile : uploadFiles) {
            if (!((UploadFile) uploadFile).isEmpty()) {
                t = false;
            }
        }
        return t;
    }

    public boolean isFinished() {
        boolean t = true;
        for (Object uploadFile : uploadFiles) {
            if (!((UploadFile) uploadFile).isDone && !((UploadFile) uploadFile).isEmpty()) {
                t = false;
            }
        }
        if (t) {
            if (uploadFinished != null) {
                uploadFinished.execute();
            }
        }
        return t;
    }

    public void clearAndAdd(boolean viewOnly) {
        panel.clear();
        uploadFiles.clear();
        addFileField(viewOnly);
    }

    public void setSpacing(int space) {
        panel.setSpacing(space);
    }
}