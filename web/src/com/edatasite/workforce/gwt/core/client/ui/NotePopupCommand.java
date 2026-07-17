package com.edatasite.workforce.gwt.core.client.ui;

public interface NotePopupCommand {

    public void onSaved(String note);

    public void onSave(String note);

    default void onCancel(String text) {

    }
}
