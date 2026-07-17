package com.edatasite.workforce.gwt.core.client.ui.notesPanel;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;

import java.util.List;

public interface LoadNoteHistory {
    void loadData(AbstractAsyncCallback<List<HistoryNote>> callback);
}
