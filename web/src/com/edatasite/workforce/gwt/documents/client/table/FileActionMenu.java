package com.edatasite.workforce.gwt.documents.client.table;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.documents.client.DocumentImages;
import com.edatasite.workforce.gwt.documents.client.commands.CopyCommand;
import com.edatasite.workforce.gwt.documents.client.commands.CutCommand;
import com.edatasite.workforce.gwt.documents.client.commands.PasteCommand;
import com.edatasite.workforce.gwt.documents.client.commands.PropertiesCommand;
import com.edatasite.workforce.gwt.documents.client.commands.RefreshCommand;
import com.edatasite.workforce.gwt.documents.client.commands.RestoreTrashCommand;
import com.edatasite.workforce.gwt.documents.client.commands.ToTrashCommand;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 29.08.2010
 * Time: 20:04:40
 * To change this template use File | Settings | File Templates.
 */
public class FileActionMenu extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    

    private FileResource fileResource;

    private boolean ctrlKeyPressed = false;

    private boolean leftClicked = false;

    private boolean rightClicked = false;

    private final DocumentImages.Images images = DocumentImages.get();

    private ActionMenu menu;

    public FileActionMenu(FileResource fileResource) {
        super();
        this.fileResource = fileResource;
        sinkEvents(Event.ONCLICK);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCONTEXTMENU) {
            return;
        }
        switch (DOM.eventGetType(event)) {
            case Event.ONKEYDOWN:
                int key = DOM.eventGetKeyCode(event);
                if (key == KeyCodes.KEY_CTRL) {
                    ctrlKeyPressed = true;
                }
                break;
            case Event.ONKEYUP:
                key = DOM.eventGetKeyCode(event);
                if (key == KeyCodes.KEY_CTRL) {
                    ctrlKeyPressed = false;
                }
                break;

            case Event.ONMOUSEDOWN:
                if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT) {
                    rightClicked = true;
                } else if (DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
                    leftClicked = true;
                }
                break;

            case Event.ONMOUSEUP:
                if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT) {
                    rightClicked = false;
                } else if (DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
                    leftClicked = false;
                }
                break;
        }

        super.onBrowserEvent(event);
    }

    public void showPopup(final int x, final int y) {
        if (menu != null) {
            menu.hide();
        }
        menu = new ActionMenu();
        int left = x;
        int top = y;

        if (left < 0) {
            left = 0;
        }
        if (top < 0) {
            top = 0;
        }
        if (Window.getClientHeight() - top < menu.getSize() * 25) {
            top = Window.getClientHeight() - menu.getSize() * 25 - 15;
        }
        menu.setPopupPosition(left, top);
        menu.setStyleName("action-listing-popup");
        menu.show();
    }

    private class ActionMenu extends PopupPanel implements Constants {

        private final DocumentImages.Images images = DocumentImages.get();

        private MenuItem cutItem;

        private MenuItem copyItem;

        private MenuItem pasteItem;

        private MenuItem sharingItem;

        private MenuItem propItem;

        private MenuItem trashItem;

        private MenuItem deleteItem;

        private MenuItem downloadItem;

        private MenuItem downloadUrlItem;

        private int size = 1;

        private ActionMenu() {
            super(true);
            setAnimationEnabled(true);
            DocumentsView.get().setCurrentSelection(fileResource);

            Command downloadCmd = () -> Window.open(getDownloadAction(fileResource), "_blank", "");

            Command downloadUrlCmd = () -> {
                final TextArea textArea = new TextArea();
                textArea.setSize("400px", "80px");
                textArea.setReadOnly(true);
                textArea.setText(getDownloadAction(fileResource));



                Label copyToClipboard = new Label("Copy to clipboard: Ctrl+C");
                KpiModal dialogBox = new KpiModal();
                dialogBox.setCloseButton(true);
                dialogBox.setTitle("File public URL");
                dialogBox.add(textArea);
                dialogBox.add(copyToClipboard);

                dialogBox.open();
                textArea.selectAll();
            };

            final MenuBar contextMenu = new MenuBar(true);
            contextMenu.setAnimationEnabled(true);

            if (fileResource.isDeleted()) {
                contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.versions()).getHTML() + "&nbsp;" + wfmStrings.restore() + "</span>", true, new RestoreTrashCommand(this));
                contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.delete()).getHTML() + "&nbsp;" + wfmStrings.delete() + "</span>", true, new ToTrashCommand(this)); //new DeleteCommand(this));
            } else {

                pasteItem = new MenuItem("<span>" + AbstractImagePrototype.create(images.paste()).getHTML() + "&nbsp;" + wfmStrings.paste() + "</span>", true, new PasteCommand(this));

                cutItem = new MenuItem("<span>" + AbstractImagePrototype.create(images.cut()).getHTML() + "&nbsp;" + wfmStrings.cut() + "</span>", true, new CutCommand(this));
                copyItem = new MenuItem("<span>" + AbstractImagePrototype.create(images.copy()).getHTML() + "&nbsp;" + wfmStrings.copy() + "</span>", true, new CopyCommand(this));

                deleteItem = new MenuItem("<span>" + AbstractImagePrototype.create(images.delete()).getHTML() + "&nbsp;" + wfmStrings.delete() + "</span>", true, new ToTrashCommand(this)); //new DeleteCommand(this));

                sharingItem = new MenuItem("<span>" + AbstractImagePrototype.create(images.sharing()).getHTML() + "&nbsp;" + wfmStrings.share() + "</span>", true, new PropertiesCommand(this, 1));
                propItem = new MenuItem("<span>" + AbstractImagePrototype.create(images.viewText()).getHTML() + "&nbsp;" + wfmStrings.properties() + "</span>", true, new PropertiesCommand(this, 0));
                TreeItem currentFolder = null;
                if (DocumentsView.get().getFolders() != null) {
                    currentFolder = DocumentsView.get().getFolders().getCurrent();
                }
                String[] link = {"", ""};
                downloadItem = new MenuItem("<span>" + link[0] + AbstractImagePrototype.create(images.download()).getHTML() + " Download" + link[1] + "</span>", true, downloadCmd);
                downloadUrlItem = new MenuItem("<span>" + AbstractImagePrototype.create(images.download()).getHTML() + " Get file URL" + "</span>", true, downloadUrlCmd);
                if (fileResource.getPermission().isDelete()) {
                    contextMenu.addItem(cutItem);
                }
                contextMenu.addItem(copyItem);
                if (currentFolder != null && currentFolder.getUserObject() instanceof FolderResource) {
                    contextMenu.addItem(pasteItem);
                }
                if (fileResource.getPermission().isDelete()) {
                    contextMenu.addItem(deleteItem);
                }
                contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.refresh()).getHTML() + "&nbsp;" + wfmStrings.refresh() + "</span>", true, new RefreshCommand(this));
                if (fileResource.getPermission().isModifyACL()) {
                    contextMenu.addItem(sharingItem);
                }
                contextMenu.addItem(propItem);

                if (DocumentsView.get().getClipboard().hasFileItem()) {
                    pasteItem.setVisible(true);
                } else {
                    pasteItem.setVisible(false);
                }
                if (currentFolder != null && currentFolder.getUserObject() instanceof FolderResource && (((FolderResource) currentFolder.getUserObject()).getFileType() == F_COMPANY_PUBLIC_ROOT || rootIsPublic(((FolderResource) currentFolder.getUserObject())))) {
                    contextMenu.addItem(downloadUrlItem);
                }
            }
            this.setWidget(contextMenu);
            this.setModal(true);

            this.setPopupPositionAndShow((offsetWidth, offsetHeight) -> setPopupPosition(offsetWidth, offsetHeight));
        }

        private String getDownloadAction(FileResource file) {
            if (file != null) {
                return file.getDownloadUrl();
            }
            return "";
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    private boolean rootIsPublic(FolderResource userObject) {
        boolean isPublic = false;
        if (userObject.getFileType() == Constants.F_COMPANY_PUBLIC_ROOT) {
            return true;
        } else if (userObject.getParent() != null) {
            isPublic = rootIsPublic(userObject.getParent());
        }
        return isPublic;
    }
}
