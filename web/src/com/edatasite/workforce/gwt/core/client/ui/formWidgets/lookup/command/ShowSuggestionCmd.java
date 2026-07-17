package com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.command;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SimpleGwtSuggestBox;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 19-Oct-2010
 * Time: 20:49:15
 */
public class ShowSuggestionCmd implements Command {
    SimpleGwtSuggestBox suggestBox;

    public ShowSuggestionCmd(SimpleGwtSuggestBox simpleGwtSuggestBox) {
        this.suggestBox = simpleGwtSuggestBox;
    }

    @Override
    public void execute() {
        this.suggestBox.showSuggestions("");
    }
}
