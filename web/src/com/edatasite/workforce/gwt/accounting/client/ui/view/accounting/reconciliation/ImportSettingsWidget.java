package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ImportSettingsWidget extends Composite {
    interface ImportSettingsViewUiBinder extends UiBinder<Widget, ImportSettingsWidget> {
    }

    private static ImportSettingsViewUiBinder uiBinder = GWT.create(ImportSettingsViewUiBinder.class);

    @UiField
    FlexTable settingsTable;
    @UiField
    HTMLPanel mainTableContainer;

    protected ColumnsTabelWidget columnsTabelWidget;

    public ImportSettingsWidget(ImportStatementWidget importStatementWidget) {
        initWidget(uiBinder.createAndBindUi(this));
        mainTableContainer.clear();
        columnsTabelWidget = new ColumnsTabelWidget(importStatementWidget);
        mainTableContainer.add(columnsTabelWidget);
    }
}