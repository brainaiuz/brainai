package com.edatasite.workforce.gwt.core.client.ui.editableTable.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 2/13/12
 * Time: 2:27 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EditableTableResources extends ClientBundle {

    @CssResource.NotStrict
    @Source("com/edatasite/workforce/gwt/core/client/ui/editableTable/resources/themes/default/theme.css")
    CssResource getDefaultTheme();
}
