/*
 *	Copyright 2008 Isaac Truett.
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.google.gwt.user.client.ui;


public class SimpleGwtSuggestBox extends SuggestBox {

    public SimpleGwtSuggestBox() {
        super();
    }

    public SimpleGwtSuggestBox(final SuggestOracle suggestOracle, TextBoxBase textBox) {
        super(suggestOracle, textBox == null ? new TextBox() : textBox);
    }

    /**
     * Creates a new <code>SimpleGwtSuggestBox</code>.
     *
     * @param suggestOracle
     */
    public SimpleGwtSuggestBox(final SuggestOracle suggestOracle) {
        super(suggestOracle);
    }

    /**
     * Show suggestions for the specified query.
     *
     * @param query
     */
    @Override
    public void showSuggestions(final String query) {
        super.showSuggestions(query);
    }

    /**
     * Selects the suggestion at the specified index in the suggestion menu.
     *
     * @param index
     */
    public void selectItem(final int index) {
        getSuggestionMenu().selectItem(index);
    }

    /**
     * Returns the index of the currently highlighted menu item.
     *
     * @return the index of the currently highlighted menu item.
     */
    public int getSuggestionMenuSelectedItemIndex() {
        return getSuggestionMenu().getSelectedItemIndex();
    }

    /**
     * Causes the currently highlighted menu item to be selected.
     */
    public void doSelectedItemAction() {
        getSuggestionMenu().doSelectedItemAction();
    }
}
