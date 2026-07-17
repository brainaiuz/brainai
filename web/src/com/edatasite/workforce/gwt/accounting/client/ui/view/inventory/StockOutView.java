package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION;

/**
 * Stock Adjustment asosida yaratilgan,
 * lekin faqat Stock Out (chiqim) uchun moslashtirilgan View.
 */
public class StockOutView extends InventoryStockAdjustmentView {

    public StockOutView(String[] params) { super(params); }
    public StockOutView(Integer id) { super(id); }
    public StockOutView(Integer id, boolean isView) { super(id, isView); }

    @Override
    protected boolean showNewQty() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    @Override
    protected boolean showDescription() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    @Override
    protected boolean showCostPerItem() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    @Override
    protected boolean showProject() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    @Override
    protected boolean showUnitOfMeasure() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    @Override
    protected boolean showUsedQty() {
        return Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    @Override
    protected boolean showAccountLookUp() {
        return !Utils.hasGenericAccess(ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION);
    }

    @Override
    protected Widget onInitialize() {
        return super.onInitialize();
    }

    @Override
    protected String getAdjustmentType() {
        return AccountingConstants.STOCK_OUT_TYPE;
    }
}
