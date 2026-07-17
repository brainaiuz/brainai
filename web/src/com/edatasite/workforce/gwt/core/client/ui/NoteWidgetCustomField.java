package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import java.util.ArrayList;
import java.util.Date;

public class NoteWidgetCustomField extends NoteWidget {
    private CompanyCustomFieldItem customFieldItem;
    private final String entityType;
    private final ArrayList<Integer> newNoteIds = new ArrayList<>();
    private Integer formItemId;

    public NoteWidgetCustomField(Integer entityID, String entityType, CompanyCustomFieldItem customFieldItem, HistoryListItem... notes) {
        super(entityID, entityType, notes);
        this.entityType = entityType;
        this.customFieldItem = customFieldItem;
//        GWT.log("object id " + entityID);
    }

    @Override
    public void createNote(HistoryListItem note, boolean saving) {
//        Window.alert("object id " + entityID + " enitty typa " + entityType);
        Integer key = note.getObjectID();
        if (note.getObjectID() == null) {//this is new note...
            note.setObjectID(getNewKey());
            note.setEventDate(new Date());
            note.setEmployee(Utils.getUserFullName());
            key = null;
        }

        FlexTable table = null;
        if (editingKey != null) {
            table = (FlexTable) map.get(editingKey).getWidget();
            if (table != null) {
                table.setWidget(0, 0, drawContent(note));
                table.removeStyleName("noteTableHover");
            }
            editingKey = null;
        }
        if (table == null) {
            table = new FlexTable();
            table.getElement().addClassName("noteTable");
            table.setWidget(0, 0, drawContent(note));
            table.setWidget(1, 0, drawFooter(note));
            table.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LOCALE_END);
            panel.insert(table, 0);
        }
        map.put(key, new NoteEntry(note, table));
        final Integer finalKey = key;
        textBox.setText("");
        if (saving) {
            LoadingPanel.loading(true);
            AllInOneService.App.get().createCFCommitBoxNote(note, customFieldItem, entityID, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer result) {
                    NoteEntry noteEntry = map.get(finalKey);
                    note.setObjectID(result);
                    if (entityID == null) {
                        newNoteIds.add(result);
                    }
                    noteEntry.setNote(note);
                    map.remove(finalKey);
                    map.put(result, noteEntry);
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NEWS_COMMENTS_ADD, result, NoteWidgetCustomField.this);
                }
            });
        }
    }

    @Override
    protected void removeNote(HistoryListItem note) {
        if (note != null && map.get(note.getObjectID())!=null) {
            map.get(note.getObjectID()).remove();
            map.remove(note.getObjectID());
            if (!note.isNew()) {
                AllInOneService.App.get().removeCommitFromCFCommitBox(note.getObjectID(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(Void result) {
                    }
                });
            }
        }
    }

    public void setCustomFieldItem(CompanyCustomFieldItem customFieldItem){
        this.customFieldItem = customFieldItem;
        drawOldNotes();
    }

    @Override
    public void drawOldNotes() {
        if (customFieldItem != null && entityID != null) {
            AllInOneService.App.get().getCFCommitBoxNotes(customFieldItem, entityID, new AsyncCallback<ArrayList<HistoryListItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ArrayList<HistoryListItem> historyListItems) {
                    if (historyListItems != null && historyListItems.size() > 0) {
                        for (HistoryListItem note : historyListItems) {
                            createNote(note, false);
                        }
                    }
                }
            });
        }
    }

    public void setFormItemIdToAllCommitOfThisCF(Integer formItemId) {
        if (formItemId != null) {
            LoadingPanel.loading(true);
            AllInOneService.App.get().setFormItemIdToAllCommitsOfThisCFWidget(formItemId, newNoteIds, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Void aVoid) {
                    LoadingPanel.loading(false);
                    newNoteIds.clear();
                }
            });
        }
    }

    public Integer getFormItemId() {
        return formItemId;
    }

    public void setFormItemId(Integer formItemId) {
        this.formItemId = formItemId;
    }
}
