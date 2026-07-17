package com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.command.CommandIcon;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.command.ShowSuggestionCmd;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 19-Oct-2010
 * Time: 20:28:27
 */
public abstract class DropDownLookup extends Composite implements HasValue<String> {

    private HorizontalPanelDiv layout;

    public void setEnsureDebugId(String s) {
        openIcon.ensureDebugId(s);
    }

    public void setEnsureSuggestBox(String viewName) {
        if (viewName != null && suggestBox != null) {
            suggestBox.ensureDebugId(viewName + "_suggestBox");
        }
    }

    private Span dropdownButton;

    private Span clearButton;

    private final SimpleGwtSuggestBox suggestBox;

    private CommandIcon openIcon;

    public final DropDownOracle oracle;

    public final HashMap<String, SelectItem> oracleMap = new HashMap<>();

    private int selectedIndex = -1;

    private Command beforeSearch;

    private Command afterSearch;

    public Command clearCommand;

    private ShowSuggestionCmd command;

    private ListingFilterParameter filterParametrs;

    private boolean omitDiv;

    public DropDownLookup() {
        this(new DropDownOracle());
    }

    public DropDownLookup(boolean withOutSortable) {
        this(new DropDownOracle(withOutSortable));
    }
    public DropDownLookup(boolean omitDiv, boolean omitDiv2) {
        this(new DropDownOracle(), null, omitDiv);
    }

    public Span getDropdownButton() {
        return dropdownButton;
    }

    public Span getClearButton() {
        return clearButton;
    }

    public void setClearCommand(Command clearCommand) {
        this.clearCommand = clearCommand;
    }

    /**
     * Creates a new <code>ComboBox</code>.
     *
     * @param oracle
     */
    public DropDownLookup(DropDownOracle oracle) {
        this(oracle, null);

    }


    @Override
    public void setWidth(String width) {

        if (width.contains("px")) {
            Integer w = Integer.parseInt(width.split("px")[0]);
            layout.setWidth(w + 5 + "px");
            suggestBox.setWidth(w + "px");
        } else {
            super.setWidth(width);
        }
    }

    public DropDownLookup(TextBoxBase textBox) {
        this(new DropDownOracle(), textBox);
    }

    @Override
    public void addStyleName(String style) {
        getSuggestBox().getTextBox().addStyleName(style);
    }

    public DropDownLookup(DropDownOracle oracle, TextBoxBase itemBox) {
        init();
        this.oracle = oracle;
        oracle = oracle == null ? new DropDownOracle() : oracle;
        suggestBox = new SimpleGwtSuggestBox(oracle, itemBox);
        suggestBox.addStyleName("form-control");

        command = new ShowSuggestionCmd(suggestBox);

        openIcon = new CommandIcon(dropdownButton, command);

        layout = new HorizontalPanelDiv();
        //openIcon.setStylePrimaryName("openIconWrapper"); TODO

        openIcon.addClickHandler(event -> {
            getSuggestBox().getSuggestionPopup().setWidth(getSuggestBox().getTextBox().getOffsetWidth() + "px");
            getSuggestBox().getSuggestionPopup().removeStyleName("gwt-dropdown--hide");
            selectedIndex = -1;
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            runBeforeSearch();
            fetchDefaultItems(getFilterParametrs(), command);
            runAfterSearch();
            event.stopPropagation();
        });

        KeyDownHandler keyDownHandler = event -> {
            boolean navKey = false;

            if (!suggestBox.isSuggestionListShowing()) {
                selectedIndex = -1;
            }

            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_UP:
                    selectedIndex--;
                    navKey = true;
                    break;
                case KeyCodes.KEY_DOWN:
                    selectedIndex++;
                    navKey = true;
                    break;
                case KeyCodes.KEY_ESCAPE:
                    suggestBox.hideSuggestionList();
                    break;
                case KeyCodes.KEY_ENTER:
                case KeyCodes.KEY_TAB:
                    if (suggestBox.getSuggestionMenuSelectedItemIndex() < 0) {
                        suggestBox.hideSuggestionList();
                    } else if (suggestBox.isSuggestionListShowing()) {
                        suggestBox.doSelectedItemAction();
                    }
                    break;
            }

            if (navKey) {
                if (selectedIndex > -1) {
                    if (!suggestBox.isSuggestionListShowing()) {
                        command.execute();
                    }

                    suggestBox.selectItem(selectedIndex);
                } else if (selectedIndex < -1) {
                    selectedIndex = -1;
                }

                event.stopPropagation();
                event.preventDefault();
            }
        };

        layout.add(suggestBox);
        layout.add(openIcon);
        layout.add(clearButton);
//        layout.setStylePrimaryName("simpleGwt-ComboBox form-control");
        layout.setStylePrimaryName("simpleGwt-ComboBox");
        initWidget(layout);

    }

    public DropDownLookup(DropDownOracle oracle, TextBoxBase itemBox, boolean omitDiv) {
        init();
        this.oracle = oracle;
        oracle = oracle == null ? new DropDownOracle() : oracle;
        suggestBox = new SimpleGwtSuggestBox(oracle, itemBox);

        command = new ShowSuggestionCmd(suggestBox);

        openIcon = new CommandIcon(dropdownButton, command);

        layout = new HorizontalPanelDiv();
        //openIcon.setStylePrimaryName("openIconWrapper"); TODO need to cliarfy with Stas

        suggestBox.addFocusListener(new FocusListener() {
            @Override
            public void onFocus(Widget widget) {
                selectedIndex = -1;
                if (filterParametrs == null) {
                    filterParametrs = new ListingFilterParameter();
                }
                runBeforeSearch();
                fetchDefaultItems(getFilterParametrs(), command);
                runAfterSearch();
            }

            @Override
            public void onLostFocus(Widget widget) {

            }
        });
        suggestBox.addKeyDownHandler(event -> {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_DOWN:
                    suggestBox.selectItem(suggestBox.getSuggestionMenuSelectedItemIndex() + 1);
                    suggestBox.getSuggestionMenu().getSelectedItem().getElement().scrollIntoView();
                    break;
                case KeyCodes.KEY_UP:
                    suggestBox.selectItem(suggestBox.getSuggestionMenuSelectedItemIndex() - 1);
                    suggestBox.getSuggestionMenu().getSelectedItem().getElement().scrollIntoView();
                    break;
            }
        });

        KeyDownHandler keyDownHandler = event -> {
            boolean navKey = false;

            if (!suggestBox.isSuggestionListShowing()) {
                selectedIndex = -1;
            }

            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_UP:
                    selectedIndex--;
                    navKey = true;
                    break;
                case KeyCodes.KEY_DOWN:
                    selectedIndex++;
                    navKey = true;
                    break;
                case KeyCodes.KEY_ESCAPE:
                    suggestBox.hideSuggestionList();
                    break;
                case KeyCodes.KEY_ENTER:
                case KeyCodes.KEY_TAB:
                    if (suggestBox.getSuggestionMenuSelectedItemIndex() < 0) {
                        suggestBox.hideSuggestionList();
                    } else if (suggestBox.isSuggestionListShowing()) {
                        suggestBox.doSelectedItemAction();
                    }
                    break;
            }

            if (navKey) {
                if (selectedIndex > -1) {
                    if (!suggestBox.isSuggestionListShowing()) {
                        command.execute();
                    }

                    suggestBox.selectItem(selectedIndex);
                } else if (selectedIndex < -1) {
                    selectedIndex = -1;
                }

                event.stopPropagation();
                event.preventDefault();
            }
        };

        suggestBox.addStyleName("simpleGwt-ComboBox");
        suggestBox.addStyleName("form-control");
        initWidget(suggestBox);
    }


    private void init() {
        dropdownButton = new Span();
        dropdownButton.setClass("caret");

        clearButton = new Span();
        clearButton.setClass("simpleGwt-ComboBox__reset close");

        initClearHandler(clearButton);
    }

    public void showClearButton() {
        if (layout.getStyleName() != null) {
            if (!layout.getStyleName().contains("simpleGwt-ComboBox--w-reset")) {
                layout.addStyleName("simpleGwt-ComboBox--w-reset");
            }
        } else {
            layout.addStyleName("simpleGwt-ComboBox--w-reset");
        }
    }

//    public void hideClearButton() {
//        if (layout.getStyleName() != null) {
//            layout.removeStyleName("simpleGwt-ComboBox--w-reset");
//        }
//    }

    protected abstract void initClearHandler(Span clearButton);

    protected abstract void fetchDefaultItems(ListingFilterParameter filterParametrs, Command cmd);

    /**
     * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
     */
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void clear() {
        oracle.clear();
    }


    /**
     * @see com.google.gwt.user.client.ui.HasValue#getValue()
     */
    public String getValue() {
        boolean exist = oracle.exists(suggestBox.getText());
        if (exist) {
            return suggestBox.getText();
        }
        return null;
    }

    /**
     * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object)
     */
    public void setValue(final String value) {
        setValue(value, false);
    }

    /**
     * @see com.google.gwt.user.client.ui.HasValue#setValue(java.lang.Object, boolean)
     */
    public void setValue(final String value, final boolean fireEvents) {
        final String oldValue = getValue();

        suggestBox.setText(oracle.exists(value) ? value : null);

        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
        }
    }

    public void runAfterSearch() {
        if (getAfterSearch() != null) {
            getAfterSearch().execute();
        }
    }

    public void runBeforeSearch() {
        if (getBeforeSearch() != null) {
            getBeforeSearch().execute();
        }
    }

    public SimpleGwtSuggestBox getSuggestBox() {
        return this.suggestBox;
    }

    public Command getBeforeSearch() {
        return beforeSearch;
    }

    public void setBeforeSearch(Command beforeSearch) {
        this.beforeSearch = beforeSearch;
    }

    public Command getAfterSearch() {
        return afterSearch;
    }

    public Command getCommand() {
        return command;
    }

    public void setAfterSearch(Command afterSearch) {
        this.afterSearch = afterSearch;
    }

    public ListingFilterParameter getFilterParametrs() {
        if (filterParametrs == null) {
            setFilterParametrs(new ListingFilterParameter());
        }
        filterParametrs.setLookUp(true);
        return filterParametrs;
    }

    public void setFilterParametrs(ListingFilterParameter filterParametrs) {
        this.filterParametrs = filterParametrs;
    }

    public CommandIcon getOpenIcon() {
        return openIcon;
    }

    public HorizontalPanelDiv getLayout() {
        return layout;
    }

    public void setAutocompleteOff(){
        getSuggestBox().getElement().setAttribute("autocomplete", "off");
    }
}
