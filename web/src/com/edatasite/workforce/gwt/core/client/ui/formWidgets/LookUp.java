package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.DropDownLookup;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.DropDownOracle;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 11.09.2010
 * Time: 11:10:39
 */
public abstract class LookUp extends DropDownLookup {
    public HashSet<String> letters = new HashSet<>();
    private final static Integer DEFAULT_LIMIT = 20;
    private int startFromTHLetter = 0;
    private static int zIndex = 1000;
    private boolean isFirst = true;
    public static WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final String BY_NAME = Constants.BY_NAME;
    protected static final String BY_EMAIL = Constants.BY_EMAIL;
    protected static final String BY_BOTH = Constants.BY_BOTH;
    public static final String SEARCH_TEXT = wfmStrings.searchTypeMessage();
    private boolean valueNotEmptyMeansSelected = false;
    private String lastValueBeforeClick;

    private String defaultText = wfmStrings.searchTypeMessage();

    public LookUp(TextBoxBase itemBox) {
        super(itemBox);
        init(0);
    }

    public LookUp(int type, TextBoxBase itemBox) {
        super(itemBox);
        init(type);
    }

    public void setStartFromTHLetter(int startFromTHLetter) {
        this.startFromTHLetter = startFromTHLetter;
    }

    public boolean isValueNotEmptyMeansSelected() {
        return valueNotEmptyMeansSelected;
    }

    public void setValueNotEmptyMeansSelected(boolean valueNotEmptyMeansSelected) {
        this.valueNotEmptyMeansSelected = valueNotEmptyMeansSelected;
    }

    public String getLastValueBeforeClick() {
        return lastValueBeforeClick;
    }

    public boolean isSelected() {
        String value = this.getSuggestBox().getText();
        if (value == null) {
            return false;
        }
        Integer id = oracle.getItemID(value);
        if (id != null) {
            return true;
        }
        return isValueNotEmptyMeansSelected() && !getDefaultText().equals(value) && !"".equals(value.trim());
    }

    public void clear() {
        letters.clear();
        oracle.clear();
        getTextBox().setText("");
//        getTextBox().getElement().setAttribute("style", "color:#536677 !important");
        if ("".equals(getSuggestBox().getValue())) {
            getSuggestBox().setValue(getDefaultText());
//            getTextBox().getElement().setAttribute("style", "color:rgba(0, 0, 0, 0.26) !important");
        }
        getFilterParametrs().setSearchKey(null);
    }

    public void clearSelectedItem(){
        letters.clear();
        oracle.clear();
        oracleMap.clear();
        getTextBox().setText(null);
        getTextBox().setValue(null);
    }

    public void clearFilter() {
        getFilterParametrs().setSearchKey(null);
    }

    public void clearAndClearItems() {
        clear();
        oracle.clearItems();
    }

    public void clearOracleItems() {
        oracle.clearItems();
        isFirst = true;
    }

    protected String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        getSuggestBox().setValue(getDefaultText());
    }

    public LookUp() {
        super();
        init(0);
    }

    public LookUp(boolean omitDiv, boolean omitDiv2) {
        super(omitDiv, omitDiv2);
        init(0);
    }

    public LookUp(boolean withOutSortable) {
        super(withOutSortable);
        init(0);
    }

    public LookUp(int type) {
        super();
        init(type);
    }


    private void init(int type) {
        setFilterParametrs(new ListingFilterParameter());
        getSuggestBox().setOracle(oracle);
//        getSuggestBox().addStyleName("width234");
        getSuggestBox().setZIndex(++zIndex);
        getSuggestBox().setLimit(DEFAULT_LIMIT);
        TextBoxBase textBox = getTextBox();
        textBox.setText(getDefaultText());
//        textBox.getElement().setAttribute("style", "color:rgba(0, 0, 0, 0.26) !important");
        textBox.addFocusHandler(event -> {
            getSuggestBox().getSuggestionPopup().setWidth(textBox.getOffsetWidth() + "px");
            getSuggestBox().getSuggestionPopup().removeStyleName("gwt-dropdown--hide");
            if (getDefaultText().equals(getSuggestBox().getValue())) {
                getSuggestBox().setValue("");
//                textBox.getElement().setAttribute("style", "color:#536677 !important");
            }
            if (textBox instanceof TextBox) {
                ((TextBox) textBox).setSelectionRange(0, textBox.getValue().length());
            }
        });
        textBox.addBlurHandler(event -> {
//            textBox.getElement().setAttribute("style", "color:#536677 !important");
            getSuggestBox().getSuggestionPopup().setWidth(textBox.getOffsetWidth() + "px");
            getSuggestBox().getSuggestionPopup().removeStyleName("gwt-dropdown--hide");
            if ("".equals(getSuggestBox().getValue())) {
                getSuggestBox().setValue(getDefaultText());
//                textBox.getElement().setAttribute("style", "color:rgba(0, 0, 0, 0.26) !important");
            }
        });
        getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> textBox.getElement().setAttribute("style", "color:#536677 !important"));
//        textBox.addKeyPressHandler(KeyPressEvent::getUnicodeCharCode);
//        textBox.addKeyUpHandler(event -> lastValueBeforeClick = ((TextBoxBase) event.getSource()).getText());
        // data shows when user enters space
//        textBox.addKeyPressHandler(event -> {
//            String chrctr = String.valueOf(event.getCharCode());
//            String txt = ((TextBoxBase) event.getSource()).getText();
//            txt = txt == null || "".equals(txt) ? chrctr.toLowerCase() : txt.toLowerCase() + chrctr;
//
//            if (txt.length() > startFromTHLetter) {
//
//                runBeforeSearch();
//
//                if (!getFilterParametrs().isDoNotSearch()) {
//                    setItems(txt);
//                    letters.add(txt);
//                    runAfterSearch();
//                }
//             }
//            }
//        });
        // Copy-paste eventini qo'shish
        textBox.addKeyUpHandler(event -> {
            String currentText = textBox.getText();
            if (currentText.length() > startFromTHLetter) {
                runBeforeSearch();
                setItems(currentText);
                letters.add(currentText);
                runAfterSearch();
            }
            // Oxirgi qiymatni yangilaymiz
            lastValueBeforeClick = currentText;
        });
        onItemDeleteInsertUpdate(type);
    }

    /**
     * This method refreshes lookUp items.
     * In the body of method should be defined all
     * listeners i.e.(delete/edit/create item)
     *
     * @param type
     */
    protected abstract void onItemDeleteInsertUpdate(int type);

    /**
     * This method used when editing page
     *
     * @param item-value for edit page
     */
    public void addItem(SelectItem item) {
        addOracle(item);
        setSelected(item);
    }

    public void setItems(String txt, SelectItem[] items) {
        getLayout().removeStyleName("is-loading");

        if (items != null) {
            for (SelectItem item : items) {
                addOracle(item);
                if (item.isSelected()) {
                    setSelected(item);
                }
            }
        }
        if (txt != null) {
            getSuggestBox().showSuggestions(txt);
        } else {
            getSuggestBox().showSuggestionList();
        }
    }

    public void setItemsandSelect(SelectItem[] items) {
        getLayout().removeStyleName("is-loading");

        if (items != null) {
            for (SelectItem item : items) {
                addOracle(item);
                if (item.isSelected()) {
                    setSelected(item);
                }
            }
        }
    }

    public void setItems(SelectItem[] items,String searchText) {
        getLayout().removeStyleName("is-loading");

        if (items != null) {
            for (SelectItem item : items) {
                addOracle(item);
            }
        }

        if (searchText != null) {
            getSuggestBox().showSuggestions(searchText);
        } else {
            getSuggestBox().showSuggestionList();
        }
    }



    public void setItems(String txt) {

        getFilterParametrs().setSearchKey(txt);
        getFilterParametrs().setLimit(getSuggestBox().getLimit());
        getLayout().addStyleName("is-loading");
        lookUpService(getFilterParametrs());
    }

    public void setItems(SelectItem[] items) {
        getLayout().removeStyleName("is-loading");

        if (items != null) {
            for (SelectItem item : items) {
                addOracle(item);
            }
        }
    }

    public void setSelected(String txt) {
        if (oracle.exists(txt) && oracle.getItemID(txt) != null) {
            this.getSuggestBox().setText(txt);
        }
    }

    public void setSelected(SelectItem selectedItem) {

        if (selectedItem != null) {
            if (oracle.exists(selectedItem.getName()) && oracle.getItemID(selectedItem.getName()) != null
                    && oracle.getItemID(selectedItem.getName()).equals(selectedItem.getId())) {
                this.getSuggestBox().setText(selectedItem.getName());
            } else if (selectedItem.getId() != null && selectedItem.getName() != null) {
                oracle.addItem(selectedItem.getName(), selectedItem.getId());
                this.addLetters(selectedItem.getName());
                this.getSuggestBox().setText(selectedItem.getName());
                oracleMap.put(selectedItem.asString(), selectedItem);
            }
        }
//        getTextBox().getElement().setAttribute("style", "color:#536677 !important");
    }

    public void setSelected(Integer ID, String value) {
        setSelected(new SelectItem(ID, value));
    }

    public SelectItem getSelectedItem() {
        if (islink()) {
            return null;
        }

        if (oracle.getItemID(this.getSuggestBox().getText()) != null) {
            SelectItem selectedItem = new SelectItem(oracle.getItemID(this.getSuggestBox().getValue()), this.getSuggestBox().getText());
            return oracleMap.getOrDefault(selectedItem.asString(), selectedItem);
        }
        return null;
    }

    public SelectItem getSelectedItem(Integer itemId) {
        if (itemId != null) {
            return new SelectItem(itemId, oracle.getSuggestion(itemId) != null ? oracle.getSuggestion(itemId).getDisplayString() : "");
        }
        return null;
    }

    public void setSelected(Integer itemId) {
        this.setSelected(this.getSelectedItem(itemId));
    }

    public Integer getSelectedItemID() {
        if (!islink()) {
            if (oracle.getItemID(this.getSuggestBox().getText()) != null) {
                return oracle.getItemID(this.getSuggestBox().getText());
            }
        }
        return null;
    }

    public Integer getSelectedItemIDByValue(String value) {
        if (oracle.getItemID(value) != null) {
            return oracle.getItemID(value);
        }
        return null;
    }

    protected abstract void lookUpService(ListingFilterParameter filterParametrs);

    public void addListener(Widget widget, int... event) {
        for (int anEvent : event) {
            WfmUiEventsBus.addWfmUiListener(anEvent, widget, (sender, args) -> isFirst = true);
        }
    }

    @Override
    protected void fetchDefaultItems(final ListingFilterParameter filterParametrs, final Command cmd) {
        if (isFirst) {
            filterParametrs.setLimit(17);
            filterParametrs.setLookUp(true);
            getLayout().addStyleName("is-loading");
            lookUpService(filterParametrs);
            isFirst = false;
        } else {
            cmd.execute();
        }
    }

    @Override
    protected void initClearHandler(Span clearButton) {
        if (clearButton != null) {
            clearButton.addClickHandler(event -> {
                clear();
                if (clearCommand != null) {
                    clearCommand.execute();
                }
            });
        }
    }

    public void addOracle(SelectItem oracle) {
        this.oracle.addItem(Utils.normalize(oracle.getName()), oracle.getId());
        oracleMap.put(oracle.asString(), oracle);
    }

    public void addLetters(String letter) {
        letters.add(letter);
    }

    public void clearLaters() {
        letters.clear();
    }

    public boolean existsOracle(String text) {
        return this.oracle.exists(text) && this.oracle.getItemID(text) != null;
    }

    public TextBoxBase getTextBox() {
        return getSuggestBox().getTextBox();
    }

    public String getText() {
        return getTextBox().getText();
    }

    public DropDownOracle getOracle() {
        return super.oracle;
    }

    public void setEnabled(boolean b) {
        if (b) {
            this.getDropdownButton().setVisible(b);
        } else {
            this.getDropdownButton().getElement().setAttribute("style", "display:none !important");
        }
        this.getSuggestBox().getTextBox().setEnabled(b);
        this.getClearButton().setEnabled(b);
    }

    public void refreshOracle(boolean forceToInit) {
        isFirst = forceToInit;
    }

    public Command getOnSelectListener() {
        return null;
    }

    public boolean islink() {
        if (this.getSuggestBox().getText().startsWith("<a")) {
            this.getSuggestBox().setText(getDefaultText());
            oracle.getLinkCommand().execute();
            return true;
        }
        return false;
    }


}
