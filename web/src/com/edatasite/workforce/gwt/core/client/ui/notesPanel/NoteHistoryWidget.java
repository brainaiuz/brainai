package com.edatasite.workforce.gwt.core.client.ui.notesPanel;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.UpdateTypeStyle;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteHistoryWidget extends Div {
    private static final DateTimeFormat hourFormat = DateTimeFormat.getFormat("HH:mm");
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat(Utils.getShortDateFormat());
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private TextBox noteBox;
    private Div updateList;
    private SaveIntoDatabase saveIntoDatabase;
    private RemoveFromDatabase removeFromDatabase;


    public NoteHistoryWidget(LoadNoteHistory loadData) {
        super("history-notes");
        add(createHeading(true));
        add(createBody());
        if (loadData != null) {
            loadData.loadData(new AbstractAsyncCallback<List<HistoryNote>>() {
                public void onSuccess(List<HistoryNote> result) {
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    sortItems(result);
                    setItems(result);
                }
            });
        }
    }

    public NoteHistoryWidget(LoadNoteHistory loadData, boolean editable) {
        super("history-notes");
        add(createHeading(editable));
        add(createBody());
        if (loadData != null) {
            loadData.loadData(new AbstractAsyncCallback<List<HistoryNote>>() {
                public void onSuccess(List<HistoryNote> result) {
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    sortItems(result);
                    setItems(result);
                }
            });
        }
    }

    public void setLoadData(LoadNoteHistory loadData) {
        if (loadData != null) {
            loadData.loadData(new AbstractAsyncCallback<List<HistoryNote>>() {
                public void onSuccess(List<HistoryNote> result) {
                    if (result == null || result.size() == 0) {
                        return;
                    }
                    sortItems(result);
                    setItems(result);
                }
            });
        }
    }

    public void setFocusToNoteBox() {
        noteBox.setFocus(true);
    }

    private void sortItems(List<HistoryNote> result) {
        result.sort((o1, o2) -> o2.getEventDate().compareTo(o1.getEventDate()));
    }

    public void setSaveIntoDatabase(SaveIntoDatabase saveIntoDatabase) {
        this.saveIntoDatabase = saveIntoDatabase;
    }

    public void setRemoveFromDatabase(RemoveFromDatabase removeFromDatabase) {
        this.removeFromDatabase = removeFromDatabase;
    }

    private Div createHeading(boolean editable) {
        Div heading = new Div("history-notes__heading");
        noteBox = new TextBox();
        noteBox.setPlaceHolder(wfmStrings.typeYourMessageHere());
        noteBox.addStyleName("form-control");
        noteBox.setEnabled(editable);
        noteBox.addKeyPressHandler(e -> {
            if (e.getUnicodeCharCode() == KeyCodes.KEY_ENTER) {
                save();
                noteBox.setValue("");
            }
        });
        noteBox.addStyleName("keepDropDownOpen");
        FormGroup notingField = new FormGroup(wfmStrings.addNote(), noteBox);
        heading.add(notingField);
        return heading;
    }

    private Div createBody() {
        Div body = new Div("history-notes__body");
        updateList = new Div("updates-list");
        body.add(updateList);
        return body;
    }


    private void prependItem(HistoryListItem note) {
        if (note != null) {
            updateList.insert(new NoteItemWidget(note), 0);
        }
    }

    private void addItem(HistoryListItem item) {
        if (item != null) {
            updateList.add(new NoteItemWidget(item));
        }
    }

    private void addItem(MyUpdateItem item) {
        if (item != null) {
            updateList.add(new UpdateItemWidget(item));
        }
    }

    private void setItems(List<HistoryNote> items) {
        updateList.clear();
        if (items == null) {
            return;
        }
        for (HistoryNote historyNote : items) {
            if (historyNote instanceof MyUpdateItem) {
                updateList.add(new UpdateItemWidget((MyUpdateItem) historyNote));
            } else if (historyNote instanceof HistoryListItem) {
                if (((HistoryListItem) historyNote).isRejectionReason()) {
                    updateList.add(new RejectionReasonWidget((HistoryListItem) historyNote));
                }
                updateList.add(new NoteItemWidget((HistoryListItem) historyNote));
            }
        }
    }

    private void save() {
        if (Utils.isNullOrEmpty(noteBox.getValue())) {
            return;
        }
        HistoryListItem note = new HistoryListItem();
        note.setEmployee(Utils.isSuperUser() ? Constants.defaultSupportName : Utils.getFullName());
        note.setEventDate(new Date());
        note.setComment(noteBox.getText());
        prependItem(note);
        if (saveIntoDatabase != null) {
            saveIntoDatabase.save(note);
        }
        noteBox.setValue("");
    }

    private void remove(HistoryListItem note) {
        if (removeFromDatabase != null) {
            removeFromDatabase.remove(note);
        }
    }

    public List<HistoryListItem> getNotes() {
        List<HistoryListItem> result = new ArrayList<>();
        for (Widget widget : updateList) {
            if (widget instanceof NoteItemWidget) {
                HistoryListItem data = ((NoteItemWidget) widget).getData();
                if (data != null) {
                    result.add(data);
                }
            }
        }
        return result;
    }

    //////\\\\\\
    private class NoteItemWidget extends Div {
        private final HistoryListItem item;
        private Span infoSpan;

        public NoteItemWidget(HistoryListItem item) {
            super("updates-row updates-cat-edited");
            this.item = item;
            add(createTimeDiv(item));
            add(createInfoDiv(item));
        }

        private Div createTimeDiv(HistoryListItem item) {
            Div timeDiv = new Div("updates-row__time");
            String dateAsText = null;
            if (item.getEventDate() != null) {
                dateAsText = dateFormat.format(item.getEventDate());
                String hoursMinutes = hourFormat.format(item.getEventDate());
                timeDiv.getElement().setInnerHTML(dateAsText + "<br>" + hoursMinutes + "<br>" + item.getEmployee());
            }
            return timeDiv;
        }

        private Div createInfoDiv(HistoryListItem item) {
            Div infoDIv = new Div("updates-row__text");
            Div titleDiv = new Div("updates-row__title");
            MaterialIcon icon = new MaterialIcon();
            icon.addStyleName("ficon--edited");
            infoSpan = new Span(item.getComment());
            titleDiv.add(icon);
            titleDiv.add(infoSpan);
            infoDIv.add(titleDiv);
            return infoDIv;
        }

        public HistoryListItem getData() {
            if (Utils.isNullOrEmpty(infoSpan.getText())) {
                return null;
            }
            item.setComment(infoSpan.getText());
            return item;
        }
    }

    private class RejectionReasonWidget extends NoteItemWidget {

        public RejectionReasonWidget(HistoryListItem item) {
            super(item);
        }

        @Override
        public HistoryListItem getData() {
            return null;
        }
    }

    private class UpdateItemWidget extends Div {
        public UpdateItemWidget(MyUpdateItem item) {
            super(UpdateTypeStyle.getStyleByUpdateSubType(item.getSubType()));
            add(createTimeDiv(item));
            add(createInfoDiv(item));
        }

        private Div createTimeDiv(MyUpdateItem item) {
            Div timeDiv = new Div("updates-row__time");
            String dateAsText = dateFormat.format(item.getEventDate());
            String hoursMinutes = hourFormat.format(item.getEventDate());
            timeDiv.getElement().setInnerHTML(dateAsText + "<br>" + hoursMinutes + "<br>" + item.getUserName());
            return timeDiv;
        }

        private Div createInfoDiv(MyUpdateItem item) {
            Div infoDIv = new Div("updates-row__text");
            Div titleDiv = new Div("updates-row__title");
            Span infoSpan = new Span(item.getMessage());
            titleDiv.add(getIconStyle(item));
            titleDiv.add(infoSpan);
            infoDIv.add(titleDiv);
            return infoDIv;
        }
    }

    public static MaterialIcon getIconStyle(MyUpdateItem item) {
        MaterialIcon icon = new MaterialIcon();
        if (item.getSubType() == null) {
            icon.addStyleName("ficon--edited");
            return icon;
        }
        switch (item.getSubType()) {
            case MyUpdateItem.ADD:
                icon.addStyleName("ficon--plus");
                break;
            case MyUpdateItem.DELETE:
                icon.addStyleName("ficon--trash");
                break;
            case MyUpdateItem.FILE_UPLOAD:
                icon.addStyleName("ficon--uploaded");
                break;
            case MyUpdateItem.STATUS_PAID:
                icon.addStyleName("ficon--paid");
                break;
            case MyUpdateItem.STATUS_APPROVED:
            case MyUpdateItem.STATUS_COMPELETED:
            case MyUpdateItem.STATUS_RECEIVED:
                icon.addStyleName("ficon--completed");
                break;
            case MyUpdateItem.STATUS_REJECT:
            case MyUpdateItem.STATUS_TERMINATED:
                icon.addStyleName("ficon--rejected");
                break;
            case MyUpdateItem.ASSIGN:
                icon.addStyleName("ficon--assigned");
                break;
            case MyUpdateItem.IMPORTED:
                icon.addStyleName("ficon--import");
                break;
            case MyUpdateItem.CONVERTED:
                icon.addStyleName("ficon--converted");
                break;
            case MyUpdateItem.STATUS_REFUNDED:
                icon.addStyleName("ficon--arrow-left");
                break;
            case MyUpdateItem.STATUS_SUBMITED:
                icon.addStyleName("ficon--submited");
                break;
            case MyUpdateItem.STATUS_SENT:
                icon.addStyleName("ficon--sent");
                break;
            case MyUpdateItem.STATUS_CANCELLED:
            case MyUpdateItem.STATUS_CLOSED:
                icon.addStyleName("ficon--cancel");
                break;
            default:
                icon.addStyleName("ficon--edited");
        }
        return icon;
    }

}
