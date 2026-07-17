package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ui.view.pdf.SettingsPdfTemplateView;

import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 07.12.2018 18:26
 */
public class SettingsPdfTemplateSinksContainer extends SinksContainer {

    public SettingsPdfTemplateSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String pdfType = null;
        if (this.params.length > 1) {
            pdfType = this.params[1];
        }
        addView(new SettingsPdfTemplateView(this.id, pdfType));
    }
}
