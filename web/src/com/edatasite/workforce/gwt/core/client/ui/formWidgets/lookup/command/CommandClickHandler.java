package com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.command;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 19-Oct-2010
 * Time: 20:55:29
 */
public class CommandClickHandler implements ClickHandler {
    private Command command;

    public CommandClickHandler(Command command) {
        this.command = command;
    }

    @Override
    public void onClick(ClickEvent event) {
        command.execute();

    }
}
