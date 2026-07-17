package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.*;

import static com.edatasite.workforce.gwt.assessment.client.rpc.SkillGroupItem.COMPETENCY_GROUP_NAME;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

public class TextAreaWithSuggestionPopup extends VerticalPanel {
    private final KpiEditor materialRichEditor;
    private final PopupPanel suggestionsPopup;
    private final ListBox suggestionsListBox;
    private final List<String> entities = new ArrayList<>();
    private String entityType;
    private final VerticalPanel textAreaPanel;
    public HorizontalPanel textPanel;
    public HorizontalPanel textPanel2;
    public HorizontalPanel textPanelMain;
    private final HashMap<String, ArrayList<SkillItem>> skillItemMap = new HashMap<>();
    public String text;

    public TextAreaWithSuggestionPopup(String title) {
        this.text = title;
        getSkillItemsMap();
        textAreaPanel = new VerticalPanel();
        textAreaPanel.addStyleName("textAreaPanel"); //committed
// Todo Stanislav aka should check
//        textAreaPanel.getElement().addClassName("textAreaPanel");

        textPanel = new HorizontalPanel();
        textPanel2 = new HorizontalPanel();
        textPanelMain = new HorizontalPanel();
        textPanelMain.setWidth("100%");
// Todo Stanislav aka should check
//        textPanelMain.getElement().addClassName("txtRichEditor__labelWrapper");
//        textPanel.getElement().addClassName("txtRichEditor__labelLeft");
//        textPanel2.getElement().addClassName("txtRichEditor__labelRight");

        textPanelMain.add(textPanel);
        textPanelMain.add(textPanel2);
        materialRichEditor = new KpiEditor(true);
        materialRichEditor.setHeight("415px");
        materialRichEditor.getRichEditor().setHeight("300px");
// Todo Stanislav aka should check
//        materialRichEditor.addStyleName("txtRichEditor");


        materialRichEditor.setWidth("100%");
        materialRichEditor.getRichEditor().setWidth("100%");

        materialRichEditor.getElement().getStyle()
                .setOverflowX(Style.Overflow.HIDDEN);

        materialRichEditor.getElement().getStyle()
                .setProperty("wordBreak", "break-word");

        materialRichEditor.getElement().getStyle()
                .setProperty("overflowWrap", "anywhere");

        suggestionsPopup = createPopup();
        suggestionsListBox = new ListBox();
        suggestionsListBox.setVisibleItemCount(20);
        suggestionsListBox.addStyleName(DEFAULT_WIDTH);

        textPanelMain.setCellHorizontalAlignment(textPanel, HasHorizontalAlignment.ALIGN_LEFT);
        textPanelMain.setCellHorizontalAlignment(textPanel2, HasHorizontalAlignment.ALIGN_RIGHT);
        textAreaPanel.add(textPanelMain);
        add(textPanelMain);

        VerticalPanel suggestionsPanel = new VerticalPanel();
        suggestionsPanel.add(suggestionsListBox);
        suggestionsPopup.setWidget(suggestionsPanel);

        add(materialRichEditor);


        materialRichEditor.addKeyUpHandler(event -> {
            entityType = null;
            if (materialRichEditor.getData().contains("@")) {
                int[] cursorPosition = findCursorPosition();
                suggestionsPopup.setPopupPosition(cursorPosition[0], cursorPosition[1]);
                String text = materialRichEditor.getData();
                int atIndex = text.lastIndexOf("@");
                if (atIndex != -1) {
                    getSuggestions(text.substring(atIndex + 1).trim());
                }
            } else {
                suggestionsPopup.hide();
            }
        });


        suggestionsListBox.addClickHandler(event -> {
            int selectedIndex = suggestionsListBox.getSelectedIndex();
            if (selectedIndex != -1) {
                String data = materialRichEditor.getData();
                int atIndex = data.lastIndexOf("@");
                materialRichEditor.setData(data.substring(0, atIndex) + suggestionsListBox.getValue(selectedIndex));
            }
            suggestionsPopup.hide();
        });
    }


    private void getSuggestions(String text) {
        HashMap<String, String> suggestion = new HashMap<>();
        text = text.replaceAll("</p>|<p>|<br>|&nbsp;|</b>|<b>|</span>|&gt;", "");

        // Store the uppercase entities in a HashSet.
        Set<String> entitySet = new HashSet<>();
        for (String entity : entities) {
            entitySet.add(entity.toUpperCase());
        }

        // Iterate over the entities and skill items.
        for (String entity : entitySet) {
            if (entity.contains(text.toUpperCase())) {
                entityType = entity.substring(0, entity.indexOf("-"));
                List<SkillItem> skillItems = skillItemMap.get(entityType);
                if (skillItems != null) {
                    for (SkillItem skillItem : skillItems) {
                        String suggest = skillItem.getGroupName().toUpperCase() + "-" + skillItem.getName().toUpperCase();
                        if (suggest.contains(text.toUpperCase())) {
                            suggestion.put(suggest, "!@" + skillItem.getName() + ":" + skillItem.getDescription());
                        }
                    }
                }
            }
        }

        // Update the suggestions list box and popup.
        if (!suggestion.isEmpty()) {
            suggestionsListBox.clear();
            suggestion.forEach(suggestionsListBox::addItem);
            suggestionsPopup.show();
        } else {
            suggestionsPopup.hide();
        }
    }


    protected PopupPanel createPopup() {
        PopupPanel p = new DecoratedPopupPanel(false);
        p.setStyleName("gwt-SuggestBoxPopup");
        p.setPreviewingAllNativeEvents(true);
        p.setAnimationType(PopupPanel.AnimationType.ROLL_DOWN);
        return p;
    }

    private native int[] findCursorPosition() /*-{
        var λ = []
        var richEditor = $doc.getElementsByClassName("note-editable");
        var selection = $wnd.getSelection();
        var range = selection.getRangeAt(0);
        var clientRects = range.getClientRects();
        var firstRect = clientRects[0];
        if (firstRect != null) {
            λ [0] = firstRect.left;
            λ [1] = firstRect.top;
        }
        return λ;

    }-*/;


    private void getSkillItemsMap() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSortField(COMPETENCY_GROUP_NAME);
        AssessmentService.App.get().getCompetencies(filterParameter, new AsyncCallback<ListResult<SkillItem>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ListResult<SkillItem> result) {
                for (SkillItem item : result.getList()) {
                    entities.add(item.getGroupName().toUpperCase() + "-" + item.getName().toUpperCase());
                    if (!skillItemMap.containsKey(item.getGroupName().toUpperCase())) {
                        skillItemMap.put(item.getGroupName().toUpperCase(), new ArrayList<>());
                    }
                    skillItemMap.get(item.getGroupName().toUpperCase()).add(item);
                }
            }
        });
    }

    public KpiEditor getMaterialRichEditor() {
        return materialRichEditor;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.text = title;
        if (materialRichEditor != null) {
            materialRichEditor.setTitle(title);
        }
    }
}
