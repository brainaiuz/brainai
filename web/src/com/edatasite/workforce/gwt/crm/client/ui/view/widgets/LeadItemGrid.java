package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeadItemGrid extends AbstractDataGrid<OpportunityItem> {
    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private final Integer leadID;
    private ColumnConfigs[] columnConfigs;

    public LeadItemGrid(Integer leadID) {
        super();
        this.leadID = leadID;
        ItemTableSettingService.App.get().getColumnConfigs(ItemTableEnum.LEAD_ITEM, new AbstractAsyncCallback<ColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ColumnConfigs[] result) {
                LeadItemGrid.this.columnConfigs = result;
                initialize();
            }
        });
    }

    public Map<String, List<ColumnConfigs>> getColumnConfigs() {
        return Arrays.stream(columnConfigs).collect(Collectors.groupingBy(ColumnConfigs::getCode));
    }

    @Override
    protected void addColums() {
        if (columnConfigs != null) {
            for (ColumnConfigs cc : columnConfigs) {
                if (ItemTableConstants.PRODUCT.equals(cc.getCode())) {
                    Column<OpportunityItem, String> product = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            return item.getItemName() != null ? item.getItemName() : "";
                        }
                    };
                    addColumn(product, cc.isChanged() ? cc.getTitle() : wfmStrings.product());
                    setColumnWidth(product, cc.getWidth() + "%");
                } else if (ItemTableConstants.DESCRIPTION.equals(cc.getCode())) {
                    Column<OpportunityItem, String> description = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            return item.getDescription() != null ? item.getDescription() : "";
                        }
                    };
                    addColumn(description, cc.isChanged() ? cc.getTitle() : wfmStrings.description());
                    setColumnWidth(description, cc.getWidth() + "%");
                } else if (ItemTableConstants.QTY.equals(cc.getCode())) {
                    Column<OpportunityItem, String> qty = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            return item.getQty() != null ? AccountingUtils.get().formatQty(item.getQty()) : "";
                        }
                    };
                    addColumn(qty, cc.isChanged() ? cc.getTitle() : wfmStrings.qty());
                    setColumnWidth(qty, cc.getWidth() + "%");

                } else if (ItemTableConstants.MEASUREMENT.equals(cc.getCode())) {
                    Column<OpportunityItem, String> measuremnent = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            return item.getUnitMeasurement() != null ? item.getUnitMeasurement().getName() : "";
                        }
                    };
                    addColumn(measuremnent, cc.isChanged() ? cc.getTitle() : wfmStrings.measurement());
                    setColumnWidth(measuremnent, cc.getWidth() + "%");
                } else if (ItemTableConstants.UNITPRICE.equals(cc.getCode())) {
                    Column<OpportunityItem, String> description = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            return item.getPrice() != null ? AccountingUtils.get().formatPrice(item.getPrice()) : "";
                        }
                    };
                    addColumn(description, cc.isChanged() ? cc.getTitle() : wfmStrings.price());
                    setColumnWidth(description, cc.getWidth() + "%");
                } else if (ItemTableConstants.TAX_LIST.equals(cc.getCode())) {
                    Column<OpportunityItem, String> tax = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            if (item.getTaxItem() != null) {
                                return AccountingUtils.get().formatDiscount(item.getTaxItem().getEffectiveTaxPercent()) + " %";
                            } else if (item.getTaxAmount() != null) {
                                return AccountingUtils.get().formatDiscount(item.getTaxAmount());
                            } else {
                                return AccountingUtils.get().formatDiscount(BigDecimal.ZERO);
                            }
                        }
                    };
                    addColumn(tax, cc.isChanged() ? cc.getTitle() : wfmStrings.taxRate());
                    setColumnWidth(tax, cc.getWidth() + "%");
                } else if (ItemTableConstants.SUPPLIER.equals(cc.getCode())) {
                    Column<OpportunityItem, String> supplier = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            return item.getSupplierName() != null ? item.getSupplierName() : "";
                        }
                    };
                    addColumn(supplier, cc.isChanged() ? cc.getTitle() : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()));
                    setColumnWidth(supplier, cc.getWidth() + "%");
                } else if (ItemTableConstants.NET_AMT.equals(cc.getCode())) {
                    Column<OpportunityItem, String> net = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            if (item.getNet() != null) {
                                return AccountingUtils.get().formatPrice(item.getNet());
                            }
                            return AccountingUtils.get().formatPrice(BigDecimal.ZERO);
                        }
                    };
                    addColumn(net, cc.isChanged() ? cc.getTitle() : wfmStrings.netAmount());
                    setColumnWidth(net, cc.getWidth() + "%");

                } else if (ItemTableConstants.TOTAL_AMT.equals(cc.getCode())) {
                    Column<OpportunityItem, String> total = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            if (item.getSubTotal() != null) {
                                return AccountingUtils.get().formatPrice(item.getSubTotal());
                            }
                            return AccountingUtils.get().formatPrice(BigDecimal.ZERO);
                        }
                    };
                    addColumn(total, cc.isChanged() ? cc.getTitle() : wfmStrings.total());
                    setColumnWidth(total, cc.getWidth() + "%");

                } else if (ItemTableConstants.CATEGORY.equals(cc.getCode())) {
                    Column<OpportunityItem, String> category = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            return item.getProductCategory() != null ? item.getProductCategory().getName() : "";
                        }
                    };
                    addColumn(category, cc.isChanged() ? cc.getTitle() : wfmStrings.category());
                    setColumnWidth(category, cc.getWidth() + "%");
                } else if (ItemTableConstants.BRAND.equals(cc.getCode())) {
                    Column<OpportunityItem, String> brand = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            return item.getProductBrand() != null ? item.getProductBrand().getName() : "";
                        }
                    };
                    addColumn(brand, cc.isChanged() ? cc.getTitle() : wfmStrings.brand());
                    setColumnWidth(brand, cc.getWidth() + "%");
                } else {
                    Column<OpportunityItem, String> cf = new Column<OpportunityItem, String>(new TextCell()) {
                        @Override
                        public String getValue(OpportunityItem item) {
                            if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(cc.getCode())) {
                                CompanyCustomFieldItem cfItem = item.getCustomFieldValuesAsMap().get(cc.getCode());
                                if (Constants.DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                                    return cfItem.getFieldDateNonConvertedValue() != null ? DateUtils.format(cfItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                                } else if (Constants.UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                                    return cfItem.getFieldStringValue() != null ? cfItem.getFieldStringValue() + " % " : "";
                                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                                    String finalValue = "";
                                    if (cfItem.getSelectItems() != null && cfItem.getSelectItems().size() > 0) {
                                        for (SelectItem selectItem : cfItem.getSelectItems()) {
                                            finalValue += selectItem.getName() + "; ";
                                        }
                                    }
                                    return finalValue;

                                } else {
                                    return cfItem.getFieldStringValue();
                                }
                            } else {
                                return "";
                            }
                        }
                    };
                    addColumn(cf, cc.getTitle());
                    setColumnWidth(cf, cc.getWidth() + "%");
                }
            }
        }
    }

    @Override
    public void refresher() {
        CRMService.App.get().getLeadItems(leadID, new AbstractAsyncCallback<OpportunityItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(OpportunityItem[] result) {
                if (result.length > 0) {
                    supplyProvider(result);
                    reDrawItems();
                }
            }
        });
    }
}
