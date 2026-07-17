package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Abstract class, parent of all 'File properties' dialog boxes.
 *
 * @author Sherali
 */
public abstract class AbstractPropertiesDialog extends KpiModal {

    protected static final String MULTIPLE_VALUES_TEXT = "(Multiple values)";

    /**
     * Text box with the tags associated with the file
     */
    protected TextBox tags = new TextBox();

    protected String initialTagText;

    /**
     * A FlowPanel with all user tags
     */
    protected FlowPanel allTagsContent;


    protected TabPanel inner = null;

    /**
     * The widget's constructor.
     */
    public AbstractPropertiesDialog() {

        // Enable IE selection for the dialog (must disable it upon closing it)
        DocumentsView.enableIESelection();
    }

    /**
     * Retrieves all user tags from the server and updates the FlowPanel
     *
     * @param userId
     */
    protected void updateTags() {
    }

    /**
     * Accepts any change and updates the file
     */
    protected abstract void accept();

    public void selectTab(int _tab) {
        inner.selectTab(_tab);
    }


    /**
     * Enables IE selection prevention and hides the dialog
     * (we disable the prevention on creation of the dialog)
     */
    public void closeDialog() {
        DocumentsView.preventIESelection();
        close();
    }

}
