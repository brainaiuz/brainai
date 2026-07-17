package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by Dilshod Madrahimov on 10/4/15 10:24 PM
 */
public class KpiSearchInput extends Composite {

    private TextBox textBox;
    private HorizontalPanelDiv content;
    private Anchor searchButton;
    private SearchHandler searchHandler;
    private String searchKey = "";
    private SpanElement resetButton;


    public KpiSearchInput() {
        onInitialize();
    }

    private void onInitialize() {
        textBox = new TextBox();
        textBox.getElement().setAttribute("placeholder", "Search");
        textBox.setStyleName("kpi-input-search");
        textBox.setName("search");
        searchButton = new Anchor("");
        searchButton.setStyleName("kpi-input-search-btn");
        resetButton = Document.get().createSpanElement();
        resetButton.setInnerText("x");
        resetButton.addClassName("kpi-input-search-reset-btn");
        content = new HorizontalPanelDiv();
        content.add(searchButton);
        initHandler();

        initWidget(content);
    }

    private void initHandler() {
        if (searchHandler == null) {
            searchHandler = new SearchHandler() {
                @Override
                public void onSearch() {

                }
            };
        }

        searchButton.addClickHandler(clickEvent -> {
            if (content.getWidgetCount() == 1) {
                content.clear();
                content.add(textBox);
                content.getElement().appendChild(resetButton);
                content.add(searchButton);
                textBox.setText("");
            } else if (content.getWidgetCount() == 2 && (textBox.getText() != null && !"".equals(textBox.getValue()))) {
                searchHandler.onSearch();
            } else {
                content.clear();
                content.add(searchButton);
            }

        });

        textBox.addKeyPressHandler(event -> {
            if (event.getUnicodeCharCode() > 0) {
                String charStr = String.valueOf(event.getCharCode());
                String txt = ((TextBox) event.getSource()).getText();
                txt = (txt == null || "".equals(txt) ? charStr.toLowerCase() : txt.toLowerCase() + charStr).trim();
                if (txt.length() > 2) {
                    searchKey = txt;
                    searchHandler.onSearch();
                }
            } else if (event.getUnicodeCharCode() == 0 && ("".equals(textBox.getText()) || textBox.getText().length() < 2)) {
                reset();
            }
        });

        DOM.sinkEvents(resetButton.cast(), Event.ONCLICK);
        DOM.setEventListener(resetButton.cast(), event -> reset());

    }


    public KpiSearchInput addSearchHandler(SearchHandler searchHandler) {
        this.searchHandler = searchHandler;
        return this;
    }

    public String getSearchKey() {
        return searchKey;
    }

    private void reset() {
        searchKey = "";
        textBox.setText("");
        searchHandler.onSearch();
    }

}
