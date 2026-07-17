package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.commands.*;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.ui.*;


/**
 * @author Sherali
 */
public class GroupContextMenu extends PopupPanel {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private MenuItem copy;
    private MenuItem paste;
    private int size = 0;


    public GroupContextMenu() {
        // The popup's constructor's argument is a boolean specifying that it
        // auto-close itself when the user clicks outside of it.
        super(true);
        final DocumentImages.Images images = DocumentImages.get();
        setAnimationEnabled(true);
        String type = Constants.IS_EMPLOYEE;
        if (DocumentsView.get().getCurrentSelection() instanceof GroupMembersViewItem) {
            type = ((GroupMembersViewItem) DocumentsView.get().getCurrentSelection()).getType();
        } else if (DocumentsView.get().getCurrentSelection() instanceof GroupMemberItem) {
            type = ((GroupMemberItem) DocumentsView.get().getCurrentSelection()).getType();
        }
        final MenuBar contextMenu = new MenuBar(true);
        contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.groups()).getHTML() + "&nbsp;" + wfmStrings.New() + " " + wfmStrings.group() + "</span>", true, new NewGroupCommand(this));
        size++;
        if (DocumentsView.get().getCurrentSelection() instanceof GroupMembersViewItem) {
            contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.groups()).getHTML() + "&nbsp;" + wfmStrings.edit() + " " + wfmStrings.group() + "</span>", true, new EditGroupCommand(this));
            size++;
            if (Constants.IS_EMPLOYEE.equals(type)) {
                contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.myShared()).getHTML() + "&nbsp;" + wfmStrings.addUser() + "</span>", true, new NewUserCommand(this));
                size++;
            }
        }
        if (Constants.IS_EMPLOYEE.equals(type)) {
            copy = new MenuItem("<span>" + AbstractImagePrototype.create(images.copy()).getHTML() + "&nbsp;" + wfmStrings.copy() + " " + wfmStrings.user() + "</span>", true, new CopyCommand(this));
            contextMenu.addItem(copy);
            size++;
            paste = new MenuItem("<span>" + AbstractImagePrototype.create(images.paste()).getHTML() + "&nbsp;" + wfmStrings.paste() + " " + wfmStrings.user() + "</span>", true, new PasteCommand(this));
            contextMenu.addItem(paste);
            size++;
        }
        contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.delete()).getHTML() + "&nbsp;" + wfmStrings.delete() + "</span>", true, new DeleteUserOrGroupCommand(this));
        size++;

        add(contextMenu);

    }

    /* (non-Javadoc)
      * @see com.google.gwt.user.client.ui.PopupPanel#show()
      */

    @Override
    public void show() {
        final TreeItem current = DocumentsView.get().getGroups().getCurrent();
        if (copy != null) {
            if (current != null && current.getUserObject() instanceof GroupMemberItem && DocumentsView.get().getCurrentSelection() instanceof GroupMemberItem) {
                copy.setVisible(true);
            } else {
                copy.setVisible(false);
                size--;
            }
            if (current != null && current.getUserObject() instanceof GroupMembersViewItem && DocumentsView.get().getCurrentSelection() instanceof GroupMembersViewItem && DocumentsView.get().getClipboard().hasMemberItem()) {
                paste.setVisible(true);
            } else {
                paste.setVisible(false);
                size--;
            }
        }
        super.show();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
