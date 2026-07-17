package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.*;

import java.util.List;

/**
 * The 'Multiple file properties' dialog box implementation.
 *
 * @author Sherali
 */
public class FilesPropertiesDialog extends AbstractPropertiesDialog {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final List<FileResource> files;

//    private Boolean initialVersioned;


    /**
     * The widget's constructor.
     *
     * @param _files
     */
    public FilesPropertiesDialog(final List<FileResource> _files) {
        super();

        files = _files;

        VerticalPanel outer = new VerticalPanel();
        FocusPanel focusPanel = new FocusPanel(outer);
        inner = new DecoratedTabPanel();
        inner.setAnimationEnabled(true);
        inner.addStyleName("decorated-tab--panel");
        VerticalPanel generalPanel = new VerticalPanel();

        inner.add(generalPanel, wfmStrings.properties());
        inner.selectTab(0);

        FlexTable generalTable = new FlexTable();
        generalTable.setText(0, 0, String.valueOf(files.size()) + " - " + wfmStrings.filesSelected());
        generalTable.setText(1, 0, wfmStrings.folder());
        FileResource firstFile = files.size() > 0 ? files.get(0) : null;
        if (firstFile != null && firstFile.getFolderName() != null) {
            generalTable.setText(1, 1, firstFile.getFolderName());
        } else {
            generalTable.setText(1, 1, "-");
        }

        generalTable.addStyleName("file--FilesPropertiesDialog");
        generalTable.getFlexCellFormatter().setStyleName(0, 0, "props-labels");
        generalTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        generalTable.getFlexCellFormatter().setStyleName(1, 0, "props-labels");
        generalTable.setCellSpacing(4);

        WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY, (ClickHandler) event -> {
            accept();
            closeDialog();
        });

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), (ClickHandler) event -> closeDialog());

        generalPanel.add(generalTable);

        // Asynchronously retrieve the tags defined by this user.
        DeferredCommand.addCommand(() -> updateTags());

        generalPanel.setSpacing(4);

        outer.add(inner);
        outer.addStyleName("doc-TabPanelBottom");
        focusPanel.setFocus(true);

        add(outer);
        addButton(cancel);
        addButton(ok);

        setWidth(300);
    }


    /**
     * Accepts any change and updates the file
     */
    @Override
    protected void accept() {
    }


}
