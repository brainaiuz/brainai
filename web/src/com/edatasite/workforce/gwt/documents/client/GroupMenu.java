package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.documents.client.commands.*;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * The 'Group' menu implementation.
 */
public class GroupMenu extends PopupPanel implements ClickHandler {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    /**
     * The widget's images.
     */
    private final MenuBar contextMenu;
    private int size = 0;

    /**
     * The widget's constructor.
     */
    public GroupMenu() {
        // The popup's constructor's argument is a boolean specifying that it
        // auto-close itself when the user clicks outside of it.
        super(true);
        setAnimationEnabled(true);
        final DocumentImages.Images images = DocumentImages.get();
        size = 0;
        contextMenu = new MenuBar(true);
        contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.sharing()).getHTML() + "&nbsp;</span>", true, new NewGroupCommand(this));
        size++;
        if (DocumentsView.get().getCurrentSelection() instanceof GroupMembersViewItem) {
            contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.sharing()).getHTML() + "&nbsp;" + wfmStrings.edit() + " " + wfmStrings.group() + "</span>", true, new EditGroupCommand(this));
            size++;
            contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.sharing()).getHTML() + "&nbsp;" + wfmStrings.addUser() + "</span>", true, new NewUserCommand(this));
            size++;
            contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.delete()).getHTML() + "&nbsp;" + wfmStrings.delete() + "</span>", true, new DeleteUserOrGroupCommand(this));
            size++;
        }
        if (DocumentsView.get().getCurrentSelection() instanceof GroupMemberItem) {
            contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.copy()).getHTML() + "&nbsp;" + wfmStrings.copy() + " " + wfmStrings.user() + "</span>", true, new CopyCommand(this));
            size++;
            contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.paste()).getHTML() + "&nbsp;" + wfmStrings.paste() + " " + wfmStrings.user() + "</span>", true, new PasteCommand(this));
            size++;
            contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.delete()).getHTML() + "&nbsp;" + wfmStrings.delete() + "</span>", true, new DeleteUserOrGroupCommand(this));
            size++;
        }
        add(contextMenu);
    }

    public void onClick(ClickEvent event) {
        GroupMenu menu = new GroupMenu();
        int left = event.getRelativeElement().getAbsoluteLeft();
        int top = event.getRelativeElement().getAbsoluteTop() + event.getRelativeElement().getOffsetHeight();
        menu.setPopupPosition(left, top);

        menu.show();
    }

    /**
     * Retrieve the contextMenu.
     *
     * @return the contextMenu
     */
    public MenuBar getContextMenu() {
        contextMenu.setAutoOpen(false);
        return contextMenu;
    }

}
