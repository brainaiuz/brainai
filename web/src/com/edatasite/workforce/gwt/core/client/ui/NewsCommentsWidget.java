package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.news.client.rpc.NewsService;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import java.util.Date;

public class NewsCommentsWidget extends NoteWidget {

    public NewsCommentsWidget(Integer entityID, String entityType, HistoryListItem... notes) {
        super(entityID, entityType, notes);
    }

    @Override
    public void createNote(HistoryListItem note, boolean saving) {
        Integer key = note.getObjectID();
        if (note.getObjectID() == null) {//this is new note...
            note.setObjectID(getNewKey());
            note.setEventDate(new Date());
            note.setEmployee(Utils.getUserFullName());
            key = note.getObjectID();
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
        textBox.setText("");
        if (saving && entityID != null) {
            LoadingPanel.loading(true);
            note.setRelatedId(entityID);
            NewsService.App.get().saveNewsComment(note, new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(Void result) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NEWS_COMMENTS_ADD, result, NewsCommentsWidget.this);
                    LoadingPanel.loading(false);
                }
            });
        }
    }


    @Override
    protected void removeNote(HistoryListItem note) {
        if (note != null && map.get(note.getObjectID()) != null) {
            map.get(note.getObjectID()).remove();
            map.remove(note.getObjectID());
            NewsService.App.get().deleteNewsComment(note.getObjectID(), new AbstractAsyncCallback<Void>() {
                public void failure(Throwable caught) {
                }

                public void success(Void result) {
                }
            });
        }
    }
}
