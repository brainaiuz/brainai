package com.edatasite.workforce.gwt.task.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.task.client.ui.view.TaskEditView;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Azazello
 * Date: 12/22/14
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskEditSinksContainer extends SinksContainer {

    public TaskEditSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    @Override
    protected void initViews() {
        String forWhat = params.length > 1 ? params[1] : null;
        addView(new TaskEditView(id, forWhat));
    }
}
