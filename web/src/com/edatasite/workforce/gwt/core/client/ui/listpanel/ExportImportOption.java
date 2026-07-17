package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 30-Aug-2010
 * Time: 15:23:53
 */
public interface ExportImportOption {

    void initExport(FlowPanel additionalContent);

    void initExport(FlowPanel additionalContent, Boolean showExport);
}
