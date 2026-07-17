package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.documents.client.rest.resource.AllFilesResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OtherUserResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.impl.ClippedImagePrototype;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jamshid.asatillayev
 * Date: Jan 18, 2011
 * Time: 3:20:17 PM
 */
public class DocUtils {
    private Folders folders;// = DocumentsView.get().getFolders();
    private TreeItem selection;// = DocumentsView.get().getFolders().getCurrent();
    private Object curSelection;// = DocumentsView.get().getCurrentSelection();
    private DocumentsView documentsView;

    public DocUtils() {
        this.documentsView = DocumentsView.get();
        this.folders = DocumentsView.get().getFolders();
        this.selection = (this.folders != null) ? this.folders.getCurrent() : null;
        this.curSelection = DocumentsView.get().getCurrentSelection();
    }

    public DocUtils(DocumentsView documentsView) {
        this.documentsView = documentsView;
        this.folders = documentsView.getFolders();
        this.selection = (this.folders != null) ? this.folders.getCurrent() : null;
        this.curSelection = documentsView.getCurrentSelection();
    }

    public boolean canUpload() {
        if (!isRootItems() && selection != null && (folders.isFileItem(selection) || folders.isOthersSharedItem(selection))) {
            final FolderResource folderResource = ((FolderResource) selection.getUserObject());
            return folderResource.getPermission().isWrite();
        }
        return isSystemItem() || isMySharedItem();
    }

    public boolean canDelete() {
        if (!isRootItems() && !isMyFolderRoot() && !isPublicFolderRoot() && selection != null && !folders.isSystemItem(selection)) {
            final FolderResource folderResource = ((FolderResource) selection.getUserObject());
            return folderResource.getPermission().isDelete();
        }
        return false;
    }

    public boolean canDeleteFile() {
        return  curSelection instanceof FileResource && ((FileResource) curSelection).getPermission()!=null && ((FileResource) curSelection).getPermission().isDelete();
    }

    public boolean canRestore() {
        return selection != null && !folders.isTrash(selection) && folders.isTrashItem(selection);
    }

    public boolean canEmptyTrash() {
        return selection != null && folders.isTrash(selection);
    }

    public boolean canRename() {
        if (!isFileResource()) {
            if (!(isMyFolderRoot() || isPublicFolderRoot() || isRootItems() || (selection != null && folders.isSystemItem(selection)))) {
                final FolderResource folderResource = ((FolderResource) selection.getUserObject());
                return folderResource.getPermission().isWrite();
            }
            return false;
        }

        return ((FileResource) curSelection).getPermission().isWrite();
    }

    public boolean canCreateFolder() {
        if (selection != null && (folders.isFileItem(selection) && !folders.isDefaultFolders()) || isOthersSharedItem()/* || isPublicItem()*/) {
            final FolderResource folderResource = ((FolderResource) selection.getUserObject());
            return folderResource.getPermission().isWrite();
        }
        return isMySharedItem();
    }

    public boolean canCopy() {
        return selection != null && selection.getUserObject() instanceof FolderResource && !isMyFolderRoot() && !isPublicFolderRoot() && !folders.isSystemItem(selection);
    }

    public boolean canPaste() {
        return DocumentsView.get().getClipboard().hasFolderOrFileItem() && (canCreateFolder() || isFileResource() || isFileResources());
    }

    public boolean canShare() {
        if (!isFileResource()) {
            if (!isOthersShare() && !isRootItems() && !isMyFolderRoot() && !isPublicFolderRoot()) {
                if (folders.isOthersSharedItem(selection) || folders.isSystemItem(selection)) {
                    final FolderResource folderResource = ((FolderResource) selection.getUserObject());
                    return folderResource.getPermission().isModifyACL();
                } else if (folders.isFileItem(selection)) {
                    return true;
                }
            }
            return false;
        }
        return ((FileResource) curSelection).getPermission().isModifyACL();
    }

    private boolean isFileResource() {
        return curSelection instanceof FileResource;
    }

    private boolean isFileResources() {
        return curSelection instanceof List;
    }

    private boolean isSystemItem() {
        return selection != null && folders.isSystemItem(selection) && !folders.isSystem(selection);
    }

    public boolean isRootItems() {
        return selection != null && folders.isMyShares(selection) || folders.isOthersShared(selection) || folders.isTrash(selection) || folders.isSystem(selection);
    }

    public boolean isMyFolderRoot() {
        return selection != null && folders.getRootItem() != null && folders.getRootItem().equals(selection);
    }

    public boolean isPublicFolderRoot() {
        return selection != null && folders.getPublicItem() != null && folders.getPublicItem().equals(selection);
    }

    private boolean isMySharedItem() {
        return selection != null && folders.isMySharedItem(selection) && !folders.isMyShares(selection);
    }

    private boolean isPublicItem() {
        return selection != null && folders.isPublicItem(selection) && !folders.isPublic(selection);
    }

    private boolean isOthersSharedItem() {
        return selection != null && folders.isOthersSharedItem(selection) && !(isOthersShare() || folders.isOthersShared(selection));
    }

    public boolean isOthersShare() {
        return selection != null && selection.getUserObject() instanceof OtherUserResource;
    }

    public boolean isAllFiles() {
        return selection != null && selection.getUserObject() instanceof AllFilesResource;
    }

    public String createHtmlImage(String text, ImageResource img) {
        final String imageHtml = ClippedImagePrototype.create(img).getHTML();
        if (!imageHtml.contains("style='")) {
            return imageHtml;
        }
        final int indent = ClippedImagePrototype.create(img).createImage().getWidth() + 3;
        final String html = imageHtml.split("style='")[0] + "style='position:absolute; " + imageHtml.split("style='")[1];
        return html + "<span style='margin-right:1px;margin-left:" + indent + "px'>&nbsp;" + text + "</span>";
    }

    public TreeItem getSelectedItem() {
        return selection;
    }

    public Object getCurSelectedItem() {
        return curSelection;
    }

    public boolean hasCurrenSelection() {
        if (documentsView == null) {
            documentsView = DocumentsView.get();
        }
        return documentsView.getCurrentSelection() != null && documentsView.getFolders() != null && documentsView.getFolders().getCurrent() != null;
    }

    /**
     * Generates HTML for a tree item with an attached icon.
     *
     * @param imageProto the image icon
     * @param title      the title of the item
     * @return the resultant HTML
     */
    public static HTML imageItemHTML(final ImageResource imageProto, final String title) {
        return new HTML("<a class='hidden-link' href='javascript:;'><span >" + AbstractImagePrototype.create(imageProto).getHTML() + "&nbsp;" + title + "</span></a>");
    }

    public static String getFileType(String contentType) {
        String mimetype = contentType;
        if (mimetype == null) {
            return "Document";
        }
        mimetype = mimetype.toLowerCase();
        if (mimetype.startsWith("application/pdf")) {
            return "PDF Document";
        } else if (mimetype.endsWith("excel") || mimetype.endsWith("spreadsheetml.sheet")) {
            return "Microsoft Office Excel Worksheet";
        } else if (mimetype.endsWith("msword") || mimetype.endsWith("wordprocessingml.document")) {
            return "Microsoft Office Word Document";
        } else if (mimetype.endsWith("powerpoint") || mimetype.endsWith("presentationml.presentation")) {
            return "Microsoft Office PowerPoint Presentation";
        } else if (mimetype.startsWith("application/zip") ||
                mimetype.startsWith("application/gzip") ||
                mimetype.startsWith("application/x-gzip") ||
                mimetype.startsWith("application/x-tar") ||
                mimetype.startsWith("application/x-gtar")) {
            return "Archive";
        } else if (mimetype.startsWith("text/html")) {
            return "Html";
        } else if (mimetype.startsWith("text/plain")) {
            return "txt";
        } else if (mimetype.startsWith("image/png")) {
            return "PNG File";
        } else if (mimetype.startsWith("image/jpeg")) {
            return "JPG File";
        } else if (mimetype.startsWith("image/")) {
            return "Image File";
        } else if (mimetype.startsWith("video/")) {
            return "Video Clip";
        } else if (mimetype.startsWith("audio/")) {
            return "Audio";
        }
        return "Document";
    }
}
