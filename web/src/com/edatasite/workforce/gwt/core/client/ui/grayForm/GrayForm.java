package com.edatasite.workforce.gwt.core.client.ui.grayForm;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Draws special gray panel used in add forms.
 * You can add three different kinds of panels:
 * 1. Bookmark panel - Small panel with text in the top of the form. You can't edit it.
 * 2. Inner panel - working panel. You can add widgets and change it.
 * 3. Tail panel - all things like in Inner panel, but a bit different color and size.
 * <p/>
 * GrayForm using styles in Core.css
 * .bookmark-font
 * .bookmark
 * .edge
 * .inner
 */
public class GrayForm extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private VerticalPanel verticalPanel; //all panels adding to this VerticalPanel.

    private VerticalPanel recordPanel; //panel for showing and adding history record.
    private HistoryListItem historyListItem; //used for keeping history record.
    private FlexTable record; //table, where current record shown.
    private StatusBar statusBar; //special status bar for showing information and error messages.
    private boolean enableNoteDelete;

    public GrayForm() {
        init();
        initWidget(verticalPanel);
    }

    /**
     * Bookmark panel. Small panel in the top of GrayForm.
     * Yuo may declare text that will be shown in it.
     *
     * @param text
     */
    public void addBookmark(String text) {
        HorizontalPanel topPanel = new HorizontalPanel();
        topPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        topPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

        HorizontalPanel bookmarkPanel = new HorizontalPanel();
        bookmarkPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        bookmarkPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        bookmarkPanel.setStyleName("bookmark");
        bookmarkPanel.setSize("300px", "43px");
        Label header = new Label(text);
        header.setStyleName("bookmark-font");
        bookmarkPanel.add(header);
        topPanel.add(bookmarkPanel);

        statusBar = new StatusBar();
        statusBar.setSize("472px", "39px");

        topPanel.add(statusBar);

        verticalPanel.add(topPanel);
    }

    /**
     * Inner gray panel. You can add there widgets and edit it's properties.
     *
     * @return Verticalpanel you can edit.
     */
    public VerticalPanel addInnerPanel() {
        VerticalPanel innerPanel = new VerticalPanel();
        innerPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        innerPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        innerPanel.addStyleName("inner");
        innerPanel.setSpacing(15);
        innerPanel.setSize("100%", "80px");
        verticalPanel.add(innerPanel);

        return innerPanel;
    }

    public VerticalPanel addVerticalPanel() {
        VerticalPanel innerPanel = new VerticalPanel();
        innerPanel.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        innerPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        innerPanel.setSpacing(0);
        innerPanel.setSize("100%", "10px");
        verticalPanel.add(innerPanel);

        return innerPanel;
    }

    /**
     * Tail gray panel. You can add there widgets and edit it's properties.
     *
     * @return Verticalpanel you can edit.
     */
    public VerticalPanel addTailPanel() {
        VerticalPanel tailPanel = new VerticalPanel();
        tailPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        tailPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        tailPanel.setStyleName("edge");
        tailPanel.setSpacing(15);
        tailPanel.setSize("100%", "74px");
        verticalPanel.add(tailPanel);

        return tailPanel;
    }

    /**
     * Add history panel. You can add there widgets and edit it's properties.
     * Although it has its own default functionality.
     *
     * @param add
     * @return
     */
    public VerticalPanel addHistoryPanel(boolean add) {
        HorizontalPanel historyButtons = new HorizontalPanel();
        Anchor addNote = new Anchor();
        addNote.setText(wfmStrings.addNote());
        addNote.addStyleName("customTitle");
        addNote.addStyleName("totalBold");
        addNote.addClickHandler(event -> noteShell());

        VerticalPanel historyPanel = new VerticalPanel();
        historyPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        historyPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        historyPanel.addStyleName("edge");
        historyPanel.setSpacing(15);
        historyPanel.setSize("100%", "50px");

        verticalPanel.add(historyPanel);

        recordPanel = new VerticalPanel();
        recordPanel.setSpacing(5);
        recordPanel.setWidth("100%");

        historyListItem = new HistoryListItem();

        if (add) {
            historyButtons.add(addNote);
        }

        historyPanel.add(historyButtons);
        historyPanel.setCellHorizontalAlignment(historyButtons, VerticalPanel.ALIGN_LEFT);
        historyPanel.setCellVerticalAlignment(historyButtons, VerticalPanel.ALIGN_TOP);
        historyPanel.add(recordPanel);

        return historyPanel;
    }

    /**
     * Get records added to history by user.
     *
     * @return
     */
    public HistoryListItem getHistory() {
        return historyListItem;
    }

    /**
     * Width of GrayForm panel.
     *
     * @param width width the object's new width, in CSS units (e.g. "10px", "1em")
     */
    public void setWidth(String width) {
        verticalPanel.setWidth(width);
    }

    /**
     * Add several records to History Panel.
     *
     * @param historyListItem
     */
    public void setHistory(HistoryListItem[] historyListItem) {
        recordPanel.clear();

        if (historyListItem != null) {
            for (HistoryListItem aHistoryListItem : historyListItem) {
                if (aHistoryListItem != null) {
                    if (aHistoryListItem.getComment() != null && !"".equals(aHistoryListItem.getComment())) {
                        addToPanel(addComment(aHistoryListItem, new FlexTable()));
                    }
                }
            }
        }
    }

    public void showStatusMessage(String message, int status) {
        statusBar.showStatusPanel(message, status);
    }

    public void resetStatus() {
        statusBar.resetStatus();
    }

    /**
     * Initialization logic.
     */
    private void init() {
        verticalPanel = new VerticalPanel();
    }

    /**
     * Adds comment to history panel.
     * Simply prepares data and calls insertRecord() function.
     *
     * @param historyListItem
     * @param record
     * @return
     */
    private FlexTable addComment(HistoryListItem historyListItem, FlexTable record) {
        final Image icon = new Image();
        String title = historyListItem.getEmployee() == null ? "" : (historyListItem.getEmployee() + " on ");
        title = title + DateUtils.preiewFormat(historyListItem.getEventDate());
        final String comment = historyListItem.getComment() == null ? wfmStrings.notAvailable() : historyListItem.getComment();

        return insertRecord(icon, title, comment, record, historyListItem);
    }

    /**
     * Used by addEvent() and addComment() functions.
     * Inserts data to FlexTable then it can be inserted to panel.
     * Can be used to edit record.
     *
     * @param icon
     * @param historyTitle
     * @param eventDescription
     * @param record
     * @return
     */
    private FlexTable insertRecord(Image icon, String historyTitle, String eventDescription, final FlexTable record, final HistoryListItem item) {
        record.clear();
        record.setCellSpacing(5);
        record.setStyleName("history-record");

        final ExtendedHorizontalPanel description = new ExtendedHorizontalPanel(item);

        Label title = new Label(historyTitle);
        title.addStyleName("add-font");
        title.addStyleName("totalBold");
        description.add(title);
        description.setCellVerticalAlignment(title, HorizontalPanel.ALIGN_MIDDLE);

        final HorizontalPanel spacing = new HorizontalPanel();
        spacing.setWidth("5px");
        description.add(spacing);

        final Label event = new Label(eventDescription);
        event.addStyleName("big-gray-font");
        description.add(event);
        description.setCellVerticalAlignment(event, HorizontalPanel.ALIGN_MIDDLE);

        record.setWidget(0, 0, icon);
        record.setWidget(0, 1, description);
        if (enableNoteDelete) {
            SimpleLink deleteLink = new SimpleLink(SimpleLink.REMOVE_ICON);
            deleteLink.addClickHandler(event1 -> record.removeFromParent());
            record.setWidget(0, 2, deleteLink);
        }

        return record;
    }

    private Widget[] widgets;

    /**
     * Call if you want to add record to panel.
     * Don't call if you want edit record.
     *
     * @param record
     */
    private void addToPanel(FlexTable record) {
        final int size = recordPanel.getWidgetCount();
        widgets = new Widget[size];
        for (int i = 0; i < size; i++) {
            widgets[i] = recordPanel.getWidget(i);
        }
        recordPanel.clear();
        recordPanel.add(record);
        recordPanel.setCellHorizontalAlignment(record, VerticalPanel.ALIGN_LEFT);
        for (int i = 0; i < size; i++) {
            recordPanel.add(widgets[i]);
            recordPanel.setCellHorizontalAlignment(record, VerticalPanel.ALIGN_LEFT);
        }

    }

    /**
     * Shell where we can add comment to history record.
     */
    private Command noteListener;
    private Command closeListener;

    private Command addToDatabaseListener;

    public void setNoteListener(Command noteListener) {
        this.noteListener = noteListener;
    }

    public void setCloseListener(Command closeListener) {
        this.closeListener = closeListener;
    }

    public void setEnableNoteDelete(boolean enableNoteDelete) {
        this.enableNoteDelete = enableNoteDelete;
    }

    public void noteShell() {
        this.noteShell(3000);
    }

    public void noteShell(String titleMessage) {
        this.noteShell(titleMessage, 3000, false, false, "");
    }

    public void noteShell(int noteLength) {
        this.noteShell(wfmStrings.addNote(), noteLength, false, false, "");
    }

    public void noteShell(String titleMessage, final Boolean isEmpy) {
        this.noteShell(titleMessage, 3000, isEmpy, false, "");
    }

    public void noteShell(String titleMessage, final Boolean isEmpy, Boolean checked, String checkboxMessage) {
        this.noteShell(titleMessage, 3000, isEmpy, checked, checkboxMessage);
    }

    public void noteShell(String titleMessage, int noteLength) {
        this.noteShell(titleMessage, noteLength, false, false, "");
    }

    public void noteShell(String titleMessage, int noteLength, final Boolean isEmpty, Boolean checked, String checkboxMessage) {
        final KpiModal messageModal = new KpiModal();

        final VerticalPanel notePanel = new VerticalPanel();
        notePanel.setWidth("200px");
        notePanel.setSpacing(10);
        notePanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        notePanel.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);

        final TextArea2 note = new TextArea2(noteLength);

        note.setWidth("330px");

        notePanel.add(note);

        final KpiCheckBox checkBox = new KpiCheckBox(" " + checkboxMessage);
        if (checked) {
            notePanel.add(checkBox);
        }


        final WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {

            if (note.getText().length() > 0 || (isEmpty != null && isEmpty)) {
                HistoryListItem historyItem = new HistoryListItem();
                historyItem.setEmployee(Utils.getFullName());
                historyItem.setEventDate(new Date());
                historyItem.setComment(note.getText());
                historyItem.setChecked(checkBox.getValue());

                //if user hasn't add any comments then add one
                record = addComment(historyItem, new FlexTable());
                addToPanel(record);
                historyListItem = historyItem;
                if (noteListener != null) {
                    noteListener.execute();
                }
                if (addToDatabaseListener != null) {
                    addToDatabaseListener.execute();
                }
            }
            messageModal.close();
        });
        final WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> messageModal.close());
        messageModal.addCloseHandler(popupPanelCloseEvent -> {
            if (closeListener != null) {
                closeListener.execute();
            }
        });

        messageModal.add(notePanel);
        messageModal.addButton(cancel);
        messageModal.addButton(save);

        messageModal.setWidth(380);
        messageModal.setTitle(titleMessage);
        messageModal.open();
    }

    public HistoryListItem[] getNotes() {
        HistoryListItem[] items = new HistoryListItem[recordPanel.getWidgetCount()];
        for (int i = 0; i < recordPanel.getWidgetCount(); i++) {
            FlexTable widget = (FlexTable) recordPanel.getWidget(i);
            ExtendedHorizontalPanel panel = (ExtendedHorizontalPanel) widget.getWidget(0, 1);
            items[i] = panel.getItem();
        }
        return items;
    }

    public class ExtendedHorizontalPanel extends HorizontalPanel {
        private HistoryListItem item;

        public ExtendedHorizontalPanel(HistoryListItem item) {
            this.item = item;
        }

        public HistoryListItem getItem() {
            return item;
        }
    }
}
