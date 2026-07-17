package com.edatasite.workforce.gwt.core.client.ui.treeselect;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Mar 18, 2010
 * Time: 3:40:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TreeSelectIcons extends ClientBundle {

    @Source("com/edatasite/workforce/gwt/core/resource/icons/filterTree/minus.png")
    ImageResource getTreeOpen();

    @Source("com/edatasite/workforce/gwt/core/resource/icons/filterTree/plus.png")
    ImageResource getTreeClosed();

    //plus / blank / minus icons
    @Source("com/edatasite/workforce/gwt/core/resource/treeLeaf.gif")
    ImageResource getTreeLeafBlank();

    @Source("com/edatasite/workforce/gwt/core/resource/treeClosed.gif")
    ImageResource getTreeClosedPlus();

    @Source("com/edatasite/workforce/gwt/core/resource/treeOpen.gif")
    ImageResource getTreeOpenMinus();


    @Source("com/edatasite/workforce/gwt/core/resource/icons/filterTree/checked.png")
    ImageResource getChecked();

    @Source("com/edatasite/workforce/gwt/core/resource/icons/filterTree/un-checked.png")
    ImageResource getUnChecked();

    @Source("com/edatasite/workforce/gwt/core/resource/icons/attachment/remove-icon.gif")
    ImageResource removeIcon();

    @Source("com/edatasite/workforce/gwt/documents/resources/edit_user.png")
    ImageResource user();

    class App {
        public static TreeSelectIcons get() {
            return GWT.create(TreeSelectIcons.class);
        }
    }
}
