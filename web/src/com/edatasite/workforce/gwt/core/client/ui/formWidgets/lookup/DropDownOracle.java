package com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 19-Oct-2010
 * Time: 20:09:06
 */
public class DropDownOracle extends MultiWordSuggestOracle {

    private boolean fullSearch;
    private boolean isvisiblelink;
    private Anchor link;
    private Command linkCommand;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    /**
     * A {@link com.google.gwt.user.client.ui.SuggestOracle.Suggestion} that is simply a {@link String}.
     */
    public class StringSuggestion implements SuggestOracle.Suggestion {
        private final String string;

        /**
         * Creates a new <code>StringSuggestion</code>.
         *
         * @param string
         */
        public StringSuggestion(final String string) {
            super();
            this.string = string;
        }

        /**
         * @see com.google.gwt.user.client.ui.SuggestOracle.Suggestion#getDisplayString()
         */
        public String getDisplayString() {
            return string;
        }

        /**
         * @see com.google.gwt.user.client.ui.SuggestOracle.Suggestion#getReplacementString()
         */
        public String getReplacementString() {
            return string;
        }
    }

    private Collection<String> items;


    /**
     * Creates a new <code>StringComboBoxOracle</code>.
     */
    public DropDownOracle() {
        super();

        items = new TreeSet<>();
        link = new Anchor(wfmStrings.addNewWithLine());
        link.getElement().setAttribute("style", "text-align:center; font-weight:bold;text-decoration:none;");
    }

    /**
     * Creates a new <code>StringComboBoxOracle</code> without sortable option.
     */
    public DropDownOracle(boolean withOutSortable) {
        super();
        items = new LinkedHashSet<>();
        link = new Anchor(wfmStrings.addNewWithLine());
        link.getElement().setAttribute("style", "text-align:center; font-weight:bold;text-decoration:none;");
    }

    /**
     * @see com.google.gwt.user.client.ui.SuggestOracle#requestSuggestions(com.google.gwt.user.client.ui.SuggestOracle.Request,
     *      com.google.gwt.user.client.ui.SuggestOracle.Callback)
     */
    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        final List<Suggestion> suggestions = getSuggestions(request);

        final Response response = new Response(suggestions);
        callback.onSuggestionsReady(request, response);
    }

    private List<Suggestion> getSuggestions(final Request request) {
        final List<Suggestion> suggestions = new ArrayList<>();
        final Iterator<String> i = items.iterator();
        final String lowerCaseQuery = request.getQuery().toLowerCase();

        final List<Suggestion> tempSuggestions = new ArrayList<>();
        if (isvisiblelink) {
//            if (suggestions.size() > 0)
//                suggestions.set(0, new StringSuggestion(link.toString()));
//            else
            suggestions.add(new StringSuggestion(link.toString()));
        }
        while (suggestions.size() < request.getLimit() && i.hasNext()) {
            String next = i.next();
            if (next.toLowerCase().startsWith(lowerCaseQuery)) {
                suggestions.add(new StringSuggestion(next));
            } else if (fullSearch && next.toLowerCase().contains(lowerCaseQuery)) {
                tempSuggestions.add(new StringSuggestion(next));
            } else if (tempSuggestions.size() < 10 && next.toLowerCase().contains(lowerCaseQuery)) {
                tempSuggestions.add(new StringSuggestion(next));
            }
        }

        suggestions.addAll(tempSuggestions);
        return suggestions;
    }

    /**
     * @see com.google.gwt.user.client.ui.SuggestOracle#requestDefaultSuggestions(com.google.gwt.user.client.ui.SuggestOracle.Request,
     *      com.google.gwt.user.client.ui.SuggestOracle.Callback)
     */
    @Override
    public void requestDefaultSuggestions(final Request request, final Callback callback) {
        request.setQuery("");
        final List<Suggestion> suggestions = getSuggestions(request);

        final Response response = new Response(suggestions);
        callback.onSuggestionsReady(request, response);
    }

    /**
     * Adds the specified {@link String} to the oracle.
     *
     * @param item
     */
    public void add(final String item) {
        assert item != null : "Item cannot be null";
        items.add(item);
        super.add(item);
    }

    public void clearItems() {
        items.clear();
    }

    public void setFullSearch(boolean fullSearch) {
        this.fullSearch = fullSearch;
    }

    public void setIsvisiblelink(boolean isvisiblelink) {
        this.isvisiblelink = isvisiblelink;
    }

    public void setLinkCommand(Command linkcommand) {
        this.linkCommand = linkcommand;
    }

    public Command getLinkCommand() {
        return this.linkCommand;
    }

}
