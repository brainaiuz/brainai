package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.WFTPlaginListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.QuickViewPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 11.08.2010
 * Time: 16:39:23
 * To change this template use File | Settings | File Templates.
 */
public class WFTPlaginViewPanel extends QuickViewPanel {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private WFTPlaginView plaginView;
    private TextBox versionBox;
    private final BackendStrings backendStrings = BackendStrings.App.get();

    public WFTPlaginViewPanel() {
        super();
    }

    public WFTPlaginViewPanel(WFTPlaginView plaginView) {
        this.plaginView = plaginView;
    }

    @Override
    protected boolean doPreview(Object o) {
        if (!(o instanceof WFTPlaginListItem)) {
            return false;
        }
        clear();
        final WFTPlaginListItem item = (WFTPlaginListItem) o;

        versionBox = new TextBox();
        versionBox.setWidth("100px");
        Validation.addNumericKeyboardListener(versionBox);
        versionBox.setText(item.getPlaginVersion() != null ? item.getPlaginVersion() : "");
        Button saveButton = new Button(wfmStrings.save());
        saveButton.addClickHandler(event -> save(item));

        FlexTable flexTable = new FlexTable();
        flexTable.setCellPadding(10);
        flexTable.setCellSpacing(10);
        flexTable.setWidget(0, 0, new HTML("<font size='14px'><b class=customTitle>" + item.getPlaginName() + "</b></font>"));
        flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        flexTable.setHTML(1, 0, wfmStrings.enterCurrentVersion() + ": ");
        flexTable.setWidget(1, 1, versionBox);
        flexTable.setWidget(2, 1, saveButton);
        add(flexTable);
//        setScrollEnabled(true);
//        layout(true);
        return true;
    }

    private void save(final WFTPlaginListItem plaginListItem) {
        plaginListItem.setPlaginVersion(versionBox.getText());
        BackendService.App.get().updatePlaginItem(plaginListItem, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            public void success(Void result) {
                Info.show(plaginListItem.getPlaginName() + Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.plaginName()), Info.Type.WARNING);
                plaginView.refresh();
            }
        });
    }
}
