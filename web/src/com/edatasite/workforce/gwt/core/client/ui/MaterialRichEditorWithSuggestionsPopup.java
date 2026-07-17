package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.CANDIDATE;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.CASE;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.CONTACT;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.CUSTOMER;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.DEPARTMENT;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.EMPLOYEE;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.LEAD;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.LOCATION;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.OPPORTUNITY;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.PRODUCT;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.PROJECT;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.PURCHASE_INVOICE;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.PURCHASE_ORDER;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.SALES_INVOICE;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.SALES_ORDER;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.SALES_QUOTE;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.SUPPLIER;
import static com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum.TASK;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

public class MaterialRichEditorWithSuggestionsPopup extends VerticalPanel {
    private KpiEditor materialRichEditor;
    private PopupPanel suggestionsPopup;
    private ListBox suggestionsListBox;
    private List<String> entities;
    private String entityType;
    private boolean isDefault;

    public MaterialRichEditorWithSuggestionsPopup() {

        materialRichEditor = new KpiEditor(true);
        materialRichEditor.setWidth("600px");
        suggestionsPopup = createPopup();
        suggestionsListBox = new ListBox();
        suggestionsListBox.setVisibleItemCount(20);
        suggestionsListBox.addStyleName(DEFAULT_WIDTH);

        entities = Arrays.stream(CustomFieldLookUpTypeEnum.values())
                .map(CustomFieldLookUpTypeEnum::name).collect(Collectors.toList());

        VerticalPanel suggestionsPanel = new VerticalPanel();
        suggestionsPanel.add(suggestionsListBox);
        suggestionsPopup.setWidget(suggestionsPanel);
        add(materialRichEditor);


        materialRichEditor.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                entityType = null;
                isDefault = false;
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
                if (materialRichEditor.getData().contains("#")) {
                    String text = materialRichEditor.getData();
                    int atIndex = text.lastIndexOf("#");
                    int start = text.lastIndexOf("@");
                    int end = text.indexOf("#", start);
                    String entity = text.substring(start + 1, end);
                    if (entities.contains(entity.trim().toUpperCase())) {
                        entityType = entities.get(entities.indexOf(entity.trim().toUpperCase()));
                    }
                    if (atIndex != -1 && entityType != null) {
                        getSuggestionsByEntity(entityType, text.substring(atIndex + 1));
                    } else if (atIndex != -1 && !text.substring(0, atIndex).endsWith("html")) {
                        isDefault = true;
                        int[] cursorPosition = findCursorPosition();
                        suggestionsPopup.setPopupPosition(cursorPosition[0], cursorPosition[1]);
                        getSuggestionsByEntity(EMPLOYEE.name(), text.substring(atIndex + 1));
                    }
                }
            }
        });


        suggestionsListBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String pageLinkForEntity = null;
                int selectedIndex = suggestionsListBox.getSelectedIndex();
                if (selectedIndex != -1) {
                    String data = materialRichEditor.getData();
                    int atIndex = isDefault ? data.lastIndexOf("#") : data.lastIndexOf("@");
                    if (entityType != null || isDefault) {
                        pageLinkForEntity = getPageLinkForEntity(entityType == null ? EMPLOYEE.name() : entityType, suggestionsListBox.getValue(selectedIndex) != null ? Integer.valueOf(suggestionsListBox.getValue(selectedIndex)) : null);
                    }
                    materialRichEditor.setData(data.substring(0, atIndex) + (entityType == null && !isDefault ? "@" + suggestionsListBox.getValue(selectedIndex) : "<a href = \"" + pageLinkForEntity + "\">" + suggestionsListBox.getItemText(selectedIndex)) + "</a>");
                }
                suggestionsPopup.hide();
            }
        });
    }

    public String getData() {
        return materialRichEditor.getData();
    }

    public void setData(String data) {
        materialRichEditor.setData(data);
    }

    private void getSuggestions(String text) {
        text = text.replaceAll("</p>|<p>|<br>|&nbsp;|</b>|<b>|</span>", "");
        GWT.log(text);
        List<String> suggestions = new ArrayList<>();
        for (String entity : entities) {
            if (entity.startsWith(text.toUpperCase())) {
                suggestions.add(entity);
            }
        }
        if (!suggestions.isEmpty()) {
            suggestionsListBox.clear();
            for (String suggestion : suggestions) {
                suggestionsListBox.addItem(suggestion);
            }
            suggestionsPopup.show();
        } else {
            suggestionsPopup.hide();
        }
    }

    private void getSuggestionsByEntity(String type, String text) {
        text = text.replaceAll("</p>|<p>|<br>|&nbsp;|</b>|<b>|</span>", "");
        CustomFieldLookUpTypeEnum customFieldLookUpTypeEnum = CustomFieldLookUpTypeEnum.get(type);
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setSearchKey(text);
        filterParametrs.setLookUp(true);
        filterParametrs.setLimit(20);
        AllInOneService.App.get().getCustomFieldLookUpData(filterParametrs, customFieldLookUpTypeEnum, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    suggestionsListBox.clear();
                    for (SelectItem selectItem : result) {
                        suggestionsListBox.addItem(selectItem.getName(), selectItem.getId() + "");
                    }
                    suggestionsPopup.show();
                }
            }
        });
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

    private String getPageLinkForEntity(String entityType, Integer id) {
        String url = null;
        if (entityType.equals(CANDIDATE.name())) {
            url = "Hrms.html#candidate|summary/" + id;
        } else if (entityType.equals(CASE.name())) {
            url = "Crm.html#case|summary/" + id;
        } else if (entityType.equals(CONTACT.name())) {
            url = "Crm.html#contact|summary/" + id;
        } else if (entityType.equals(CUSTOMER.name())) {
            url = "Crm.html#account|summary/" + id + "/false/Customer";
        } else if (entityType.equals(DEPARTMENT.name())) {
            url = "ProjectManagement.html#department|summary/" + id;
        } else if (entityType.equals(EMPLOYEE.name())) {
            url = "Hrms.html#employeeProfile|employeeProfileView/" + id;
        } else if (entityType.equals(LEAD.name())) {
            url = "Crm.html#lead|summary/" + id;
        } else if (entityType.equals(LOCATION.name())) {
            url = "Crm.html#lead|summary/" + id;
        } else if (entityType.equals(OPPORTUNITY.name())) {
            url = "Crm.html#opportunity|summary/" + id;
        } else if (entityType.equals(PRODUCT.name())) {
            url = "Accounting.html#product|summary/" + id;
        } else if (entityType.equals(PROJECT.name())) {
            url = "ProjectManagement.html#project|summary/" + id;
        } else if (entityType.equals(PURCHASE_INVOICE.name())) {
            url = "Accounting.html#purchaseinvoice|summary/" + id;
        } else if (entityType.equals(PURCHASE_ORDER.name())) {
            url = "Accounting.html#purchaseorder|summary/" + id;
        } else if (entityType.equals(SALES_INVOICE.name())) {
            url = "Accounting.html#saleinvoice|summary/" + id;
        } else if (entityType.equals(SALES_QUOTE.name())) {
            url = "Accounting.html#salequote|summary/" + id;
        } else if (entityType.equals(SUPPLIER.name())) {
            url = "Crm.html#account|summary/" + id + "/false/Supplier";
        } else if (entityType.equals(TASK.name())) {
            url = "ProjectManagement.html#task|summary/" + id;
        } else if (entityType.equals(SALES_ORDER.name())) {
            url = "Accounting.html#saleorder|summary/" + id;
        }
        return url;
    }

    public KpiEditor getMaterialRichEditor() {
        return materialRichEditor;
    }

    public void setMaterialRichEditor(KpiEditor materialRichEditor) {
        this.materialRichEditor = materialRichEditor;
    }
}
