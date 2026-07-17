package com.edatasite.workforce.gwt.core.client.ui.notesPanel;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.FlexAlignContent;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.Date;

public class NotesWidget extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final NotesUiBinder ourUiBinder = GWT.create(NotesUiBinder.class);
    @UiField
    Div addCmd;
    @UiField
    Span writeTitle;
    @UiField
    Div listDiv;
    private KpiModal box;
    private KpiTextArea textArea;
    private TextArea txtReason;
    private boolean editable = true;
    private SaveIntoDatabase saveIntoDatabase;
    private RemoveFromDatabase removeFromDatabase;
    private Command noteListener;
    private Command closeListener;

    public NotesWidget(boolean editable) {
        this.editable = editable;
        initWidget(ourUiBinder.createAndBindUi(this));
        init();
    }

    public NotesWidget() {
        this(true);
    }

    private void init() {
        writeTitle.getElement().setInnerHTML(SafeHtmlUtils.htmlEscape(wfmStrings.addNote()));
        addCmd.addClickHandler((e) -> {
            if (box == null) {
                initDialogBox();
            }
            box.open();
        });
    }

    public void noteShell() {
        if (box == null) {
            initDialogBox();
        }
        box.open();
    }

    private void initDialogBox() {

        box = new KpiModal();
        box.setTitle(wfmStrings.note());
        box.setFlexAlignContent(FlexAlignContent.CENTER);
        txtReason = new TextArea();
//        txtReason.setWidth("342px");
//        txtReason.setHeight("120px");
        box.add(txtReason);
        box.addButton(new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, clickEvent ->
        {
            closeListener.execute();
            box.close();
        }));
        txtReason.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && event.isControlKeyDown()) {
                save();
            }
        });
        box.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            txtReason.removeStyleName(ERROR_FORM_STYLE);
            String comment = txtReason.getText();
            if (comment == null || "".equals(comment.trim())) {
                txtReason.addStyleName(ERROR_FORM_STYLE);
                Info.warn(wfmStrings.plsSpecifyNote());
                return;
            }
            box.close();
            save();
        }));
        box.setWidth("400px");
        box.addStyleName("modal-note");
        box.addOpenHandler(openEvent -> textArea.setFocus(true));
    }

    private void save() {
        if (txtReason.getText() != null && txtReason.getText().trim().length() > 0) {
            HistoryListItem historyItem = new HistoryListItem();
            historyItem.setEmployee(Utils.getFullName());
            historyItem.setEventDate(new Date());
            historyItem.setComment(txtReason.getText());
            NoteItem item = new NoteItem(historyItem);
            listDiv.add(item);
            if (noteListener != null) {
                noteListener.execute();
            }
            if (saveIntoDatabase != null) {
                saveIntoDatabase.save(historyItem);
            }
            txtReason.setText("");
            box.close();
        }
    }

    public HistoryListItem[] getItems() {
        HistoryListItem[] result = new HistoryListItem[listDiv.getWidgetCount()];
        for (int i = 0; i < listDiv.getWidgetCount(); i++) {
            NoteItem itemWidget = (NoteItem) listDiv.getWidget(i);
            result[i] = itemWidget.getItem();
        }
        return result;
    }

    public void setItems(HistoryListItem[] items) {
        if (items != null) {
            for (HistoryListItem item : items) {
                NoteItem itemWidget = new NoteItem(item);
                listDiv.add(itemWidget);
            }
        }
    }

    public void setSaveIntoDatabase(SaveIntoDatabase saveIntoDatabase) {
        this.saveIntoDatabase = saveIntoDatabase;
    }

    public void setRemoveFromDatabase(RemoveFromDatabase removeFromDatabase) {
        this.removeFromDatabase = removeFromDatabase;
    }

    public void setNoteListener(Command noteListener) {
        this.noteListener = noteListener;
    }

    public void setCloseListener(Command closeListener) {
        this.closeListener = closeListener;
    }

    public HistoryListItem getLastHistoryItem() {
        if (listDiv.getWidgetCount() > 0) {
            return ((NoteItem) listDiv.getWidget(listDiv.getWidgetCount() - 1)).getItem();
        }
        return null;
    }

    interface NotesUiBinder extends UiBinder<Widget, NotesWidget> {
    }

    private class NoteItem extends Div {
        private final HistoryListItem item;

        public NoteItem(HistoryListItem item) {
            this.addStyleName("wg_notes__history-item");
            this.item = item;
            init();
        }

        private void init() {
            Div remove = new Div("wg_notes__item-remove");
            MaterialIcon removeIcon = new MaterialIcon();
            Anchor removeAnchor = new Anchor();
            removeAnchor.addStyleName("wg_notes__item-remove-link");
            removeIcon.addStyleName("ficon--cancel");
            removeIcon.addClickHandler(clickEvent -> {
                if (removeFromDatabase != null) {
                    removeFromDatabase.remove(item);
                }
                this.removeFromParent();
            });
            removeAnchor.add(removeIcon);
            remove.add(removeIcon);
            this.add(remove);

            Div infoPanel = new Div("wg_notes__history-info");
            Div textDiv = new Div("wg_notes__title");
            textDiv.getElement().setInnerHTML(SafeHtmlUtils.htmlEscape(item.getComment()));
            infoPanel.add(textDiv);

            Div userInfoPanel = new Div("wg_notes__user-info");
            Div dateDiv = new Div("wg_notes__user-info-date");
            dateDiv.getElement().setInnerHTML(DateUtils.format(item.getEventDate()));
            Div nameDiv = new Div("wg_notes__user-info-name");
            nameDiv.getElement().setInnerHTML(SafeHtmlUtils.htmlEscape(item.getEmployee()));
            userInfoPanel.add(dateDiv);
            userInfoPanel.add(nameDiv);

            infoPanel.add(userInfoPanel);

            this.add(infoPanel);
        }

        public HistoryListItem getItem() {
            return item;
        }

    }

}
