package com.edatasite.workforce.gwt.documents.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree;

/**
 * Created by IntelliJ IDEA.
 * User: jamshid.asatillayev
 * Date: Jan 20, 2011
 * Time: 3:54:54 PM
 */
public class DocumentImages {

    public interface Images extends Tree.Resources {

        @Source("com/edatasite/workforce/gwt/documents/resources/hrms.png")
        ImageResource sharing();

        @Source("com/edatasite/workforce/gwt/documents/resources/delete-1.png")
        ImageResource delete();

        @Source("com/edatasite/workforce/gwt/documents/resources/refresh.png")
        ImageResource refresh();

        @Source("com/edatasite/workforce/gwt/documents/resources/editcut.png")
        ImageResource cut();

        @Source("com/edatasite/workforce/gwt/documents/resources/editcopy.png")
        ImageResource copy();

        @Source("com/edatasite/workforce/gwt/documents/resources/folder_new.png")
        ImageResource folderNew();

        @Source("com/edatasite/workforce/gwt/documents/resources/upload.png")
        ImageResource upload();

        @Source("com/edatasite/workforce/gwt/documents/resources/create-new.png")
        ImageResource fileUploadAmazon();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/google.png")
        ImageResource fileUploadGoogle();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/download.gif")
        ImageResource download();

        @Source("com/edatasite/workforce/gwt/documents/resources/folder_home.png")
        ImageResource home();

        @Source("com/edatasite/workforce/gwt/documents/resources/folder_yellow.png")
        ImageResource folderYellow();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/document.png")
        ImageResource document();

        @Source("com/edatasite/workforce/gwt/documents/resources/groupevent.png")
        ImageResource othersShared();

        @Source("com/edatasite/workforce/gwt/documents/resources/edit_user.png")
        ImageResource myShared();

        @Source("com/edatasite/workforce/gwt/documents/resources/folder_user.png")
        ImageResource sharedFolder();

        @Source("com/edatasite/workforce/gwt/documents/resources/trashcan_empty.png")
        ImageResource trash();

        @Source("com/edatasite/workforce/gwt/documents/resources/advancedsettings.png")
        ImageResource systemFolder();

        @Source("com/edatasite/workforce/gwt/documents/resources/editpaste.png")
        ImageResource paste();

        @Source("com/edatasite/workforce/gwt/documents/resources/document.png")
        ImageResource folders();

        @Source("com/edatasite/workforce/gwt/documents/resources/edit_group_22.png")
        ImageResource groups();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/botton-arrow.gif")
        ImageResource desc();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/document_shared.png")
        ImageResource documentShared();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/word.png")
        ImageResource wordprocessor();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/excel.png")
        ImageResource spreadsheet();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/power.png")
        ImageResource presentation();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/iconpdf.jpg")
        ImageResource pdf();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/image.png")
        ImageResource image();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/video2.png")
        ImageResource video();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/knotify.png")
        ImageResource audio();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/html.png")
        ImageResource html();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/txt.png")
        ImageResource txt();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/ark2.png")
        ImageResource zip();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/word.png")
        ImageResource wordprocessorShared();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/excel.png")
        ImageResource spreadsheetShared();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/power.png")
        ImageResource presentationShared();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/iconpdf.jpg")
        ImageResource pdfShared();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/image_shared.png")
        ImageResource imageShared();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/video2_shared.png")
        ImageResource videoShared();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/knotify_shared.png")
        ImageResource audioShared();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/html_shared.png")
        ImageResource htmlShared();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/txt_shared.png")
        ImageResource txtShared();

        @Source("com/edatasite/workforce/gwt/documents/resources/mimetypes/ark2_shared.png")
        ImageResource zipShared();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/botton-arrow.gif")
        ImageResource down();

        @Source("com/edatasite/workforce/gwt/documents/resources/view_text.png")
        ImageResource viewText();

        @Source("com/edatasite/workforce/gwt/documents/resources/edit.png")
        ImageResource rename();

        @Source("com/edatasite/workforce/gwt/documents/resources/doc_versions.png")
        ImageResource versions();

        @Source("com/edatasite/workforce/gwt/documents/resources/border_remove.png")
        ImageResource unselectAll();

        @Source("com/edatasite/workforce/gwt/documents/resources/windowlist.png")
        ImageResource totalFiles();

        @Source("com/edatasite/workforce/gwt/documents/resources/edit_group.png")
        ImageResource group();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/action.png")
        ImageResource actions();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/images.png")
        ImageResource play();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/filterTree/minus.png")
        ImageResource getTreeOpen();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/filterTree/plus.png")
        ImageResource getTreeClosed();
    }

    private static Images img;

    public static Images get() {
        return img == null ? img = GWT.create(Images.class) : img;
    }
}