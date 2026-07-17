package com.edatasite.workforce.gwt.accounting.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AssemblyBuildItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/5/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductUnBuildView {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final WfmStrings wfmString = WfmStrings.App.get();
    private final ProductServiceAsync productService = ProductService.App.get();
    private final BuildAssemblyServiceAsync buildAssemblyService = BuildAssemblyService.App.get();
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM dd, yyyy");
    private final Integer assemblyID;
    private FlexTable itemTable;
    private Boolean hasErrors;

    public ProductUnBuildView(Integer assemblyID) {
        this.assemblyID = assemblyID;
        init();
    }

    private void init() {
        buildAssemblyService.getAssemblyBuildItems(assemblyID, new AbstractAsyncCallback<ArrayList<AssemblyBuildItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<AssemblyBuildItem> result) {
                initPopup(result);
            }
        });

    }

    private void initPopup(List<AssemblyBuildItem> buildItems) {
        VerticalPanel verticalPanel = new VerticalPanel();
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, true);
        messageBox.setWidth("450px");
        messageBox.setTitle(wfmString.information());
        messageBox.setMessage("Assembly Item build history");
        ScrollPanel panel = new ScrollPanel();
        panel.setWidth("450px");
        panel.setHeight("180px");
        itemTable = new FlexTable();
        itemTable.setStyleName("flexTable");
        itemTable.setCellPadding(0);
        itemTable.setCellSpacing(0);
        itemTable.setWidget(0, 0, new HTML(""));
        itemTable.setWidget(0, 1, new HTML(accountingStrings.builtDate()));
        itemTable.setWidget(0, 2, new HTML(accountingStrings.builtQty()));
        if (Utils.isMultiWarehouseEnabled()) {
            itemTable.setWidget(0, 3, new HTML(accountingStrings.warehouse()));
            itemTable.getFlexCellFormatter().setStyleName(0, 3, "flexTable-Label");
        }
        itemTable.getFlexCellFormatter().setStyleName(0, 0, "flexTable-Label");
        itemTable.getFlexCellFormatter().setStyleName(0, 1, "flexTable-Label");
        itemTable.getFlexCellFormatter().setStyleName(0, 2, "flexTable-Label");


        int i = 1;
        for (AssemblyBuildItem buildItem : buildItems) {
            ExtendedCheckBox checkButton = new ExtendedCheckBox();
            checkButton.getMapItems().put(i, buildItem);

            Label qty = new Label();
            qty.setText(AccountingUtils.get().formatQty(buildItem.getQty()));
            itemTable.setWidget(i, 0, checkButton);
            itemTable.setWidget(i, 1, new HTML(dateFormat.format(buildItem.getDate().getNonConvertedDate())));
            itemTable.setWidget(i, 2, qty);
            if (Utils.isMultiWarehouseEnabled()) {
                itemTable.setWidget(i, 3, new HTML(buildItem.getWarehouse()));
                itemTable.getFlexCellFormatter().setStyleName(i, 3, "flexTable-td");
            }
            itemTable.getFlexCellFormatter().setStyleName(i, 0, "flexTable-td");
            itemTable.getFlexCellFormatter().setStyleName(i, 1, "flexTable-td");
            itemTable.getFlexCellFormatter().setStyleName(i, 2, "flexTable-td");
            i++;
        }
        panel.add(itemTable);

        WfmButton2 okButton = new WfmButton2(accountingStrings.unBuild());
        okButton.addClickHandler(event -> {
            hasErrors = true;
            ArrayList<AssemblyBuildItem> sendItems = new ArrayList<>();
            for (int i1 = 1; i1 < itemTable.getRowCount(); i1++) {
                ExtendedCheckBox checkBox = (ExtendedCheckBox) itemTable.getWidget(i1, 0);
                if (checkBox.getValue()) {
                    if (Utils.isInventoryLocked() && DateUtils.getTransactionLockDate().after(checkBox.getMapItems().get(i1).getDate().getNonConvertedDate())) {
                        Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Built", Utils.getTransactionLockDate()), Info.Type.WARNING);
                        return;
                    } else {
                        if (checkBox.getMapItems().get(i1) != null && !checkBox.getMapItems().get(i1).isHasOutTransactions()) {
                            sendItems.add(checkBox.getMapItems().get(i1));
                        }
                        hasErrors = false;
                    }
                }
            }
            if (hasErrors) {
                Info.show(accountingStrings.unBuildError(), Info.Type.INFO);
            } else {
                LoadingPanel.loading(true);
                Integer[] transactionIds = sendItems.stream().map(a -> a.getTransactionID()).collect(Collectors.toList()).toArray(new Integer[]{});
                InvoiceService.App.get().validateStockInconsistencyInUnbuildAssembly(transactionIds, new AbstractAsyncCallback<SelectItem>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(true);
                    }

                    @Override
                    public void success(SelectItem result) {
                        if (result == null) {
                            unbuild(sendItems, messageBox);
                        } else {
                            Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                            LoadingPanel.loading(false);
                        }
                    }
                });

            }
        });

        WfmButton2 cancelButton = new WfmButton2(wfmString.cancel(), WfmButton2.BTN_DEFAULT);
        cancelButton.addClickHandler(event -> messageBox.close());

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(18);
        hp.add(okButton);
        hp.add(cancelButton);
        verticalPanel.add(panel);
        verticalPanel.add(hp);
        verticalPanel.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_RIGHT);
        messageBox.setContent(verticalPanel);
        messageBox.open();

    }

    private void unbuild(ArrayList<AssemblyBuildItem> sendItems, WfmMessageBox messageBox) {
        buildAssemblyService.unBuildAsseblyItems(sendItems, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(true);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(true);
                messageBox.close();
                Info.show(accountingStrings.succesfullyUnBuildAssembly(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTSERVICE_SAVED, null, null);
            }
        });
    }


    public class ExtendedCheckBox extends KpiCheckBox {
        public Map<Integer, AssemblyBuildItem> mapItems;

        public ExtendedCheckBox() {
            super();
        }

        public Map<Integer, AssemblyBuildItem> getMapItems() {
            if (mapItems == null) {
                mapItems = new HashMap<>();
            }
            return mapItems;
        }
    }
}
