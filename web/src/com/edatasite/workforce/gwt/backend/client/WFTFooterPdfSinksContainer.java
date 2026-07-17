package com.edatasite.workforce.gwt.backend.client;

import com.edatasite.workforce.gwt.backend.client.ui.view.WFTFooterPdfVew;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 24.09.2010
 * Time: 16:56:59
 */
public class WFTFooterPdfSinksContainer extends SinksContainer {
	public WFTFooterPdfSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
		super.addView(new WFTFooterPdfVew(id));
	}
}