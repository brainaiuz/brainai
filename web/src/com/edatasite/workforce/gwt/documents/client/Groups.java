package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.commands.NewGroupCommand;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;

/**
 * A component that displays a list of the user's groups.
 */
public class Groups extends Composite implements SelectionHandler, OpenHandler {

    /**
     * An image bundle for this widget.
     */
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public interface Images extends Tree.Resources {

    }

    private boolean ctrlKeyPressed = false;

    private boolean leftClicked = false;

    private boolean rightClicked = false;

    /**
     * cached latest group selection (for selecting and expanding on refresh)
     */
    private String selectedGroup = null;

    /**
     * The tree widget that displays the groups.
     */
    private Tree tree;

    /**
     * A cached copy of the currently selected group widget.
     */
    private TreeItem current;

    /**
     * A cached copy of the previously selected group widget.
     */
    private TreeItem previous;

    /**
     * A cached copy of the currently changed group widget.
     */
    private TreeItem changed;
    private GroupContextMenu menu;

    private boolean showMenu = false;

    public Groups() {
        menu = new GroupContextMenu();
        tree = new Tree(getTreeImageResources(), false);
        tree.setStyleName("scroll-box");
        this.addHandler(event -> {
            if (current == null) {
                return;
            }
            int left = current.getAbsoluteLeft() + 40;
            int top = current.getAbsoluteTop() + 20;
            if (left < 0) {
                left = 0;
            }
            if (top < 0) {
                top = 0;
            }
            if (Window.getClientHeight() - top < menu.getSize() * 24) {
                top = Window.getClientHeight() - menu.getSize() * 24;
            }
            showPopup(left, top);

        }, ContextMenuEvent.getType());
        tree.addSelectionHandler(this);
        tree.addOpenHandler(this);
        tree.setAnimationEnabled(true);

        MaterialPanel content = new MaterialPanel();
        content.add(tree);

        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com")) {//6279

            final TextBox fcid = new TextBox();
            fcid.setWidth("100px");
            final TextBox indcid = new TextBox();
            indcid.setWidth("100px");

            WfmButton2 ind = new WfmButton2(wfmStrings.indexCompanysFolders());
            ind.addClickHandler(event -> {
                Integer company = null;
                if (!"".equals(fcid.getText())) {
                    company = Integer.valueOf(fcid.getText());
                }
                LoadingPanel.loading(true);
                DocumentsService.App.get().indexFolders(company, new AsyncCallback<Void>() {

                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.foldersDone(), Info.Type.WARNING);
                    }
                });
            });
            final TextBox folderid = new TextBox();
            folderid.setWidth("100px");
            final TextBox companyid = new TextBox();
            companyid.setWidth("100px");

            WfmButton2 inf = new WfmButton2(wfmStrings.indexFolder());
            inf.addClickHandler(event -> {
                Integer company = null;
                if ("".equals(folderid.getText())) {
                    Info.show(wfmStrings.enterFolderId(), Info.Type.WARNING);
                    return;
                }
                LoadingPanel.loading(true);
                DocumentsService.App.get().indexFolder(Integer.valueOf(folderid.getText()), true, new AbstractAsyncCallback() {
                    @Override
                    public void success(Object result) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }
                });
            });

            WfmButton2 infiles = new WfmButton2(wfmStrings.indexCompanyFileOrCompany());
            infiles.addClickHandler(event -> {
                Integer company = null;
                if (!"".equals(companyid.getText())) {
                    company = Integer.valueOf(companyid.getText());
                }
                LoadingPanel.loading(true);
                DocumentsService.App.get().indexFiles(company, new AsyncCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }
                });
            });

            final SchemaLookUp schemaLookUp = new SchemaLookUp();
            schemaLookUp.setWidth("100px");
            final WfmButton2 copyCaseAttachments = new WfmButton2("Copy Case Attachments(It Works Only Once)");
            copyCaseAttachments.addClickHandler(event -> {
                schemaLookUp.setEnabled(false);
                copyCaseAttachments.setEnabled(false);
                LoadingPanel.loading(true);
                AllInOneService.App.get().copyCaseAttachments(schemaLookUp.getSelectedItemID(), new AbstractAsyncCallback() {
                    @Override
                    public void success(Object result) {
                        LoadingPanel.loading(false);
                        schemaLookUp.setEnabled(true);
                        copyCaseAttachments.setEnabled(true);
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        schemaLookUp.setEnabled(true);
                        copyCaseAttachments.setEnabled(true);
                    }
                });
            });

            final WfmButton2 reIndexButton = new WfmButton2("Re-Index and Calculate documents size");

            reIndexButton.addClickHandler(sender -> {
            schemaLookUp.setEnabled(false);
            reIndexButton.setEnabled(false);
                LoadingPanel.loading(true);
                DocumentsService.App.get().copyUploadDocumentSize(schemaLookUp.getSelectedItemID(), new AbstractAsyncCallback<String>() {
                    @Override
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        schemaLookUp.setEnabled(true);
                        reIndexButton.setEnabled(true);
                        caught.toString();
                    }

                    @Override
                    public void success(String result) {

                        LoadingPanel.loading(false);
                        schemaLookUp.setEnabled(true);
                        reIndexButton.setEnabled(true);
                        result.toLowerCase();
                    }
                });
            });

            final TextBox sattid = new TextBox();
            sattid.setWidth("100px");
            WfmButton2 sattbt = new WfmButton2(wfmStrings.createSystemFolder());
            sattbt.addClickHandler(event -> {
                Integer company = null;
                if (!"".equals(sattid.getText())) {
                    company = Integer.valueOf(sattid.getText());
                }
                LoadingPanel.loading(true);
                CommonService.App.get().createSystemFolders(company, new AbstractAsyncCallback() {
                    @Override
                    public void success(Object result) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.systemFoldersDone(), Info.Type.WARNING);
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.systemFolderError(), Info.Type.WARNING);
                    }
                });
            });

            final TextArea textArea = new TextArea();
            textArea.setWidth("100px");
            textArea.setText(wfmStrings.enterSolrRemoveQuery());
            final TextBox core = new TextBox();
            core.setWidth("100px");
            WfmButton2 solrBt = new WfmButton2(wfmStrings.runAndDeleteEnterData());
            solrBt.addClickHandler(event -> {
                LoadingPanel.loading(true);
                DocumentsService.App.get().executeSolrQuery(textArea.getText(), core.getText(), new AbstractAsyncCallback() {
                    @Override
                    public void success(Object result) {
                        LoadingPanel.loading(false);
                        Info.show("done", Info.Type.WARNING);
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        Info.show("error", Info.Type.WARNING);
                    }
                });
            });

            MaterialPanel buttonsPanel = new MaterialPanel("scroll-box");
            MaterialPanel indexPanel = new MaterialPanel("panel-box");
            MaterialPanel indexItemPanel = new MaterialPanel("panel-box__item");
            MaterialPanel indexItemPanel2 = new MaterialPanel("panel-box__item");
            MaterialPanel indexItemPanel3 = new MaterialPanel("panel-box__item");

            indexItemPanel.add(indcid);
            indexItemPanel2.add(fcid);
            indexItemPanel3.add(ind);

            indexPanel.add(indexItemPanel);
            indexPanel.add(indexItemPanel2);
            indexPanel.add(indexItemPanel3);
            buttonsPanel.add(indexPanel);

            MaterialPanel folderPanel = new MaterialPanel("panel-box");
            MaterialPanel folderItemPanel = new MaterialPanel("panel-box__item");
            MaterialPanel folderItemPanel2 = new MaterialPanel("panel-box__item");

            folderItemPanel.add(folderid);
            folderItemPanel2.add(inf);

            folderPanel.add(folderItemPanel);
            folderPanel.add(folderItemPanel2);
            buttonsPanel.add(folderPanel);

            MaterialPanel filePanel = new MaterialPanel("panel-box");
            MaterialPanel fileItemPanel = new MaterialPanel("panel-box__item");
            MaterialPanel fileItemPanel2 = new MaterialPanel("panel-box__item");

            fileItemPanel.add(companyid);
            fileItemPanel2.add(infiles);

            filePanel.add(fileItemPanel);
            filePanel.add(fileItemPanel2);
            buttonsPanel.add(filePanel);

            MaterialPanel systemPanel = new MaterialPanel("panel-box");
            MaterialPanel systemItemPanel = new MaterialPanel("panel-box__item");
            MaterialPanel systemItemPanel2 = new MaterialPanel("panel-box__item");

            systemItemPanel.add(sattid);
            systemItemPanel2.add(sattbt);

            systemPanel.add(systemItemPanel);
            systemPanel.add(systemItemPanel2);
            buttonsPanel.add(systemPanel);

            MaterialPanel solrPanel = new MaterialPanel("panel-box");
            MaterialPanel solrItemPanel = new MaterialPanel("panel-box__item");
            MaterialPanel solrItemPanel2 = new MaterialPanel("panel-box__item");
            MaterialPanel solrItemPanel3 = new MaterialPanel("panel-box__item");

            solrItemPanel.add(textArea);
            solrItemPanel2.add(core);
            solrItemPanel3.add(solrBt);

            solrPanel.add(solrItemPanel);
            solrPanel.add(solrItemPanel2);
            solrPanel.add(solrItemPanel3);
            buttonsPanel.add(solrPanel);

            MaterialPanel toolPanel = new MaterialPanel("panel-box");
            MaterialPanel toolItemPanel = new MaterialPanel("panel-box__item");
            MaterialPanel toolItemPanel2 = new MaterialPanel("panel-box__item");

            toolItemPanel.add(schemaLookUp);
            toolItemPanel2.add(copyCaseAttachments);


            toolPanel.add(toolItemPanel);
            toolPanel.add(toolItemPanel2);
            buttonsPanel.add(toolPanel);

            MaterialPanel reindexPanel = new MaterialPanel("panel-box");
            MaterialPanel reindexItemPanel = new MaterialPanel("panel-box__item");

            reindexItemPanel.add(reIndexButton);
            reindexPanel.add(reindexItemPanel);
            buttonsPanel.add(reindexPanel);
            content.add(buttonsPanel);
        }

        initWidget(content);

        setStylePrimaryName("doc-Groups");
        sinkEvents(Event.ONCONTEXTMENU);
        sinkEvents(Event.ONMOUSEUP);
        sinkEvents(Event.ONDBLCLICK);
    }

    public WfmButton2 drawAndGetAddGroupButton() {
        WfmButton2 button = new WfmButton2(wfmStrings.addGroup(), WfmButton2.BTN_PRIMARY);
        button.addClickHandler(event -> {
            PopupPanel p = new PopupPanel();
            NewGroupCommand f = new NewGroupCommand(p);
            f.execute();
        });
        return button;
    }

    public void updateGroups() {
        LoadingPanel.loading(true);
        RbacService.App.get().getCompanyGroupsWithMembers(new AbstractAsyncCallback<ArrayList<GroupMembersViewItem>>() {

            @Override
            public void success(ArrayList<GroupMembersViewItem> result) {
                LoadingPanel.loading(false);
                tree.clear();
                for (GroupMembersViewItem selectItem : result) {
                    final TreeItem item = new TreeItem();
                    item.setWidget(imageItemHTML(DocumentImages.get().groups(), selectItem.getGroupName(), item));
                    item.setUserObject(selectItem);
                    item.setStyleName("wg_tree-list");
                    tree.addItem(item);
                    updateUsers(item);
                }
            }
        });
    }

    /**
     * update status panel with currently showing file stats
     */
    public void updateCurrentlyShowingStats() {
//        DocumentsView.get().getStatusPanel().updateCurrentlyShowing(null); //clear stats - nothing to show for the groups tab
    }

    /**
     * A helper method to simplify adding tree items that have attached images.
     *
     * @param parent     the tree item to which the new item will be added.
     * @param title      the text associated with this item.
     * @param imageProto
     * @return the new tree item
     */
    private TreeItem addImageItem(final TreeItem parent, final String title, final ImageResource imageProto) {
        final TreeItem item = new TreeItem();
        item.setWidget(imageItemHTML(imageProto, title, item));
        parent.addItem(item);
        return item;
    }

    /**
     * Generates HTML for a tree item with an attached icon.
     *
     * @param imageProto the icon image
     * @param title      the title of the item
     * @return the resultant HTML
     */
    private HTML imageItemHTML(final ImageResource imageProto, final String title, final TreeItem item) {
        final HTML link = new HTML("<a class='hidden-link' href='javascript:;'>" + "<span>" + AbstractImagePrototype.create(imageProto).getHTML() + "&nbsp;" + title + "</span>" + "</a>") {
            @Override
            public void onBrowserEvent(Event event) {
                switch (DOM.eventGetType(event)) {
                    case Event.ONMOUSEDOWN:
                        if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT || DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
                            onSelection(item);
                        }
                        break;
                }
                super.onBrowserEvent(event);

            }
        };
        link.sinkEvents(Event.ONMOUSEDOWN);
        return link;
    }


    protected void showPopup(final int x, final int y) {
        if (getCurrent() == null) {
            GWT.log("[POPUP IS NULL]", null);
            return;
        }
        menu.hide();
        menu = new GroupContextMenu();
        menu.setPopupPosition(x, y);
        menu.getElement().getStyle().setZIndex(1010);
        showMenu = false;
        menu.show();
    }


    /**
     * Generate an RPC request to retrieve the users of the specified group for
     * display.
     *
     * @param groupItem the TreeItem widget that corresponds to the requested
     *                  group
     */
    void updateUsers(final TreeItem groupItem) {
        if (groupItem.getUserObject() instanceof GroupMembersViewItem) {
            GroupMembersViewItem res = (GroupMembersViewItem) groupItem.getUserObject();
            groupItem.removeItems();
            for (GroupMemberItem user : res.getMembers()) {
                final TreeItem userItem = addImageItem(groupItem, user.getTrusteeName(), DocumentImages.get().myShared());
                userItem.setUserObject(user);
            }
            if (selectedGroup != null && groupItem.getText().equals(selectedGroup)) {
                //SelectionEvent.fire(tree, groupItem);;
                onSelection(groupItem);
                groupItem.setState(true);
            }
        }

    }

    /**
     * Retrieve the current.
     *
     * @return the current
     */
    TreeItem getCurrent() {
        return current;
    }

    /**
     * Modify the current.
     *
     * @param newCurrent the current to set
     */
    void setCurrent(final TreeItem newCurrent) {
        current = newCurrent;
    }

    /**
     * Modify the changed.
     *
     * @param newChanged the changed to set
     */
    private void setChanged(final TreeItem newChanged) {
        changed = newChanged;
    }

    /**
     * Retrieve the previous.
     *
     * @return the previous
     */
    private TreeItem getPrevious() {
        return previous;
    }

    /**
     * Modify the previous.
     *
     * @param newPrevious the previous to set
     */
    private void setPrevious(final TreeItem newPrevious) {
        previous = newPrevious;
    }

    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);
        if (visible && tree.getItemCount() == 0) {
            updateGroups();
        }
    }

    @Override
    public void onSelection(SelectionEvent event) {
        final TreeItem item = (TreeItem) event.getSelectedItem();
        onSelection(item);

    }

    private void onSelection(TreeItem item) {
        final Object selected = item.getUserObject();
        // Preserve the previously selected item, so that the current's
        // onClick() method gets a chance to find it.
        if (getPrevious() != null) {
            getPrevious().getWidget().removeStyleName("doc-SelectedRow");
        }
        setCurrent(item);
        getCurrent().getWidget().addStyleName("doc-SelectedRow");
        setPrevious(getCurrent());
        DocumentsView.get().setCurrentSelection(selected);
        //cache the latest top level node (group) for selecting and expanding on refresh
        if (item.getParentItem() == null) {
            selectedGroup = item.getText();
        } else {
            selectedGroup = item.getParentItem().getText();
        }
    }

    @Override
    public void onOpen(OpenEvent event) {
        final TreeItem item = (TreeItem) event.getTarget();
        setChanged(item);
        updateUsers(item);

    }

    private Tree.Resources getTreeImageResources() {
        return new Tree.Resources() {
            public ImageResource treeClosed() {
                return DocumentImages.get().getTreeClosed();
            }

            public ImageResource treeLeaf() {
                return DocumentImages.get().groups();
            }

            public ImageResource treeOpen() {
                return DocumentImages.get().getTreeOpen();
            }
        };
    }
}
