package com.finnetlimited.reportservice.core.client.ui.content.folder;

import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.FolderType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 3/12/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class FolderListingDialogBox extends KpiModal {

    private FlexTable flexTable;
    private AddFolderDialogBox addFolderDialogBox;

    public FolderListingDialogBox() {
        super();
        setSize(500, 250);
        setScrollable(true);
        getScrollPanel().setStyleName("folder-content");
        renderContent();

    }

    private void renderContent() {
        getFolderList();
        this.addCloseHandler(popupPanelCloseEvent -> WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SAVED_REPORT_LIST_REFRESH, null, FolderListingDialogBox.this));
    }

    private void getFolderList() {
        DRSLoadingPanel.show();
        CoreService.App.get().getFolderList(new AsyncCallback<ArrayList<SelectListRpc>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(ArrayList<SelectListRpc> selectListRpcs) {
                renderFolderList(selectListRpcs);
                DRSLoadingPanel.hide();
            }
        });
    }

    private void renderFolderList(ArrayList<SelectListRpc> folders) {

        clear();
        flexTable = new FlexTable();
        flexTable.getElement().setClassName("f-table");
        flexTable.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        flexTable.setHTML(0, 0, "<strong>" + wfmStrings.folderName() + " </strong>");
        flexTable.setHTML(0, 1, "<strong>" + wfmStrings.actions() + "</strong>");
        flexTable.getFlexCellFormatter().getElement(0, 0).setClassName("f-header");
        flexTable.getFlexCellFormatter().getElement(0, 1).setClassName("f-header");

        Command command = () -> getFolderList();

        int i = 0;
        for (i = 0; i < folders.size(); i++) {
            final SelectListRpc item = folders.get(i);
            int index = i + 1;

            Anchor linkEdit = new Anchor(wfmStrings.edit(), "#editfolder");
            linkEdit.addClickListener(sender -> {
                Command command12 = () -> getFolderList();
                addFolderDialogBox = new AddFolderDialogBox();
                addFolderDialogBox.setFolderName(item.getName());
                addFolderDialogBox.setFolderId(item.getId());
                addFolderDialogBox.setFolderType(item.getType());
                addFolderDialogBox.setCommand(command12);
                addFolderDialogBox.show(true);
            });

            Anchor linkDelete = new Anchor(wfmStrings.delete(), "#deltefolder");
            linkDelete.addClickListener(sender -> {
                final WfmMessageBox msg = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouReallyWantTodeleteThisFolder());
                msg.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        DRSLoadingPanel.show(wfmStrings.deletingFolder());
                        CoreService.App.get().deleteFolder(item.getId(), new AsyncCallback<Boolean>() {

                            public void onFailure(Throwable throwable) {
                                DRSLoadingPanel.hide();
                            }

                            public void onSuccess(Boolean result) {
                                DRSLoadingPanel.hide();
                                if (result) {
                                    getFolderList();
                                }
                            }
                        });
                    }
                });
                msg.center();
            });

            HorizontalPanel hp = new HorizontalPanel();
			if (!FolderType.System.name().equals(item.getType())) {
				hp.add(linkEdit);
				hp.add(linkDelete);
			}

            if (i % 2 == 1) {
                flexTable.getRowFormatter().setStyleName(index, "odd");
            }

            flexTable.setHTML(index, 0, item.getName());
            flexTable.setWidget(index, 1, hp);
        }

        Button addLink = new Button(wfmStrings.addFolder());
        addLink.getElement().

                setAttribute("style", "float:right");

        addLink.addClickListener(sender -> {
            Command command1 = () -> getFolderList();
            addFolderDialogBox = new AddFolderDialogBox();
            addFolderDialogBox.setCommand(command1);
            addFolderDialogBox.show(false);
        });

        flexTable.setHTML(i + 1, 0, "&nbsp;");
        flexTable.setWidget(i + 1, 1, addLink);


        add(flexTable);

    }

    public void RefreshList() {
        getFolderList();
    }


}
