package com.edatasite.workforce.gwt.messagecenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailSummaryComposeView;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 11.07.11
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public class EmailViewSinksContainer extends SinksContainer {

    public EmailViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String id = null;
        if (params != null && !"".equals(params[0]) && !"add".equals(params[0])) {
            id = params[0];
        }
        addView(new EmailSummaryComposeView(id));
        //addView(new EmailSummaryView(id));
//        addView(new EmailComposeView());
    }
}
