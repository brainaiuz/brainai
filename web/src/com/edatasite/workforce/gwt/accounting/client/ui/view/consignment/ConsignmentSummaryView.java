package com.edatasite.workforce.gwt.accounting.client.ui.view.consignment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Normurod on 6/18/15.
 */
public class ConsignmentSummaryView extends View implements AccountingConstants, AccountingCustomFormConstants, Colapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer objectID;
    private WfmButton2 edit, closeButton;
    private Consignment consignment;
    private DynamicTable itemsTable;
    private HTMLPanel htmlPanel;
    private final HashMap<String, Widget> widgetsMap = new HashMap<>();
    BigDecimal subTotalAmount = ZERO;
    private String viewName;
    private Integer transferType;
    private boolean debit;


    public ConsignmentSummaryView(Integer objectID) {
        super("summary", wfmStrings.consignments());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        loadData();
        return null;
    }

    private void loadData() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            ConsignmentService.App.get().getConsignmentData(objectID, new AsyncCallback<Consignment>() {
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void onSuccess(Consignment result) {
                    LoadingPanel.loading(false);
                    consignment = result;
                    initForm();
                    htmlPanel = new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer();
                    add(htmlPanel);

                }
            });
        }
    }

    private void initForm() {
        itemsTable = new DynamicTable(getColumns(), false);
        itemsTable.setBorderWidth(0);
        initWidgetsMap();
        initDynamicTable();
        initButtonPanel();
    }

    private DynamicTableColumn[] getColumns() {
        LinkedList<DynamicTableColumn> columnsList = new LinkedList<>();
        columnsList.add(new DynamicTableColumn(wfmStrings.from() + " Company", wfmStrings.from(), 150));
        columnsList.add(new DynamicTableColumn(wfmStrings.to() + " Company", wfmStrings.to(), 150));
        columnsList.add(new DynamicTableColumn(wfmStrings.product(), wfmStrings.product(), 250));
        columnsList.add(new DynamicTableColumn(wfmStrings.qty(), QTY_COLUMN, 120));

        return columnsList.toArray(new DynamicTableColumn[]{});
    }

    private void initWidgetsMap() {

        HTML headerTitleLabel = new HTML(wfmStrings.consignments());
        headerTitleLabel.setStyleName(STYLE_TITLE_LABEL);
        widgetsMap.put(LABEL_TITLE, headerTitleLabel);

        HTML nameLabel = new HTML(wfmStrings.name());
        nameLabel.setStyleName(STYLE_LABEL);
        widgetsMap.put(LABEL_NAME, nameLabel);
        widgetsMap.put(INPUT_NAME, new HTML(consignment.getName()));

        HTML referenceLabel = new HTML(wfmStrings.reference());
        referenceLabel.setStyleName(STYLE_LABEL);
        widgetsMap.put(LABEL_REFERENCE, referenceLabel);
        widgetsMap.put(INPUT_REFERENCE, new HTML(consignment.getReference() != null ? consignment.getReference() : "N/A"));

        HTML dateLabel = new HTML(wfmStrings.date());
        dateLabel.setStyleName(STYLE_LABEL);
        widgetsMap.put(LABEL_DATE, dateLabel);
        widgetsMap.put(INPUT_DATE, new HTML(DateUtils.formatInternal(consignment.getDate().getNonConvertedDate())));

        HTML numberLabel = new HTML(wfmStrings.number());
        numberLabel.setStyleName(STYLE_LABEL);
        widgetsMap.put(LABEL_NUMBER, numberLabel);
        widgetsMap.put(INPUT_NUMBER, new HTML(consignment.getNumber() != null ? consignment.getNumber() : ""));

//        itemsTable.setStyleName(STYLE_PRODUCT_TABLE);
        widgetsMap.put(INPUT_ITEM_TABLE, itemsTable);
    }

    private void initDynamicTable() {
        itemsTable.clear();
        LinkedHashMap<String, Widget> itemWidgetsMap;
        for (ConsignmentItem item : consignment.getItems()) {
            itemWidgetsMap = new LinkedHashMap<>();
            itemWidgetsMap.put(wfmStrings.from(), new Label(item.getFromCompany().getName()));
            itemWidgetsMap.put(wfmStrings.to(), new Label(item.getToCompany().getName()));
            itemWidgetsMap.put(wfmStrings.product(), new Label(item.getProduct() != null ? item.getProduct().getName() : ""));
            itemWidgetsMap.put(wfmStrings.qty(), new Label(AccountingUtils.get().format(item.getQuantity())));

            itemsTable.addRow(item.getObjectID(), itemWidgetsMap.values().toArray(new Widget[]{}));
        }
    }



    private void initButtonPanel() {
        MaterialPanel buttonsPanel = new MaterialPanel("btns-group");

        MaterialLink edit = new MaterialLink();
        edit.ensureDebugId("edit");
        edit.addStyleName("btn btn--icon btn--white");
        MaterialIcon editIcon = new MaterialIcon();
        editIcon.setStyleName("ficon--edit");
        edit.add(editIcon);
        edit.setTooltip(wfmStrings.edit());
        edit.addClickHandler(event -> goTo("consignment|edit/" + objectID));
        buttonsPanel.add(edit);

        closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        closeButton.addClickHandler(event -> closeTab());
        buttonsPanel.add(closeButton);

        MainLayout.get().addToActionsContainer(buttonsPanel);
        MainLayout.get().makeFrameContainerHaveTabsStyle(true);
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
