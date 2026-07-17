package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PDFTransferObject;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Администратор on 08.09.14.
 */
public class StockAdjustmentPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

	@Autowired
	private ProductService productService;
    @Autowired
    public GenericSettingsManager genericSettingsManager;

	@Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData genericPdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();

        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        AdjustmentItem item = productService.getStockAdjustmentData(fp.getObjectId());

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean enabledWarehouse = financialSettings.getEnableMultiWarehouse();

        CustomisedITextTable numberAndDatesTable = new CustomisedITextTable();
        numberAndDatesTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);

        if (fp.getObjectId() != null) {
            numberAndDatesTable.addRowWithCode(INV_DATE,
                                               commonLocalizer.localizeAccounting(PdfLocalizationName.date),
                                               dateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : new Date()),
                                               INV_DATE);

            numberAndDatesTable.addRowWithCode(INV_NUMBER,
                                               commonLocalizer.localizeAccounting(PdfLocalizationName.no),
                                               escapeHtml(item.getNumber()),
                                               INV_NUMBER);

            numberAndDatesTable.addRowWithCode(ACCOUNT,
                                               commonLocalizer.localizeAccounting(PdfLocalizationName.adjustmentAccount),
                                               item.getAccount() != null ? escapeHtml(item.getAccount().getName()) : "",
                                               ACCOUNT);

            numberAndDatesTable.addRowWithCode(MEMO,
                                               commonLocalizer.localizeAccounting(PdfLocalizationName.description),
                                               escapeHtml(item.getMemo()),
                                               MEMO);
        } else {
            numberAndDatesTable.addRowWithCode(INV_DATE,
                                               commonLocalizer.localizeAccounting(PdfLocalizationName.date),
                                               escapeHtml(request.getParameter(PDFTransferObject.DATE)),
                                               INV_DATE);

            numberAndDatesTable.addRowWithCode(INV_NUMBER,
                                               commonLocalizer.localizeAccounting(PdfLocalizationName.no),
                                               escapeHtml(request.getParameter(PDFTransferObject.NUMBER)),
                                               INV_NUMBER);

            numberAndDatesTable.addRowWithCode(ACCOUNT,
                                               commonLocalizer.localizeAccounting(PdfLocalizationName.adjustmentAccount),
                                               escapeHtml(request.getParameter(PDFTransferObject.ADJUSTMENT_ACCOUNT_NAME)),
                                               ACCOUNT);

            numberAndDatesTable.addRowWithCode(MEMO,
                                               commonLocalizer.localizeAccounting(PdfLocalizationName.description),
                                               escapeHtml(request.getParameter(PDFTransferObject.MEMO)),
                                               MEMO);
        }
        numberAndDatesTable.addRowWithCode(ENABLED_MULTI_WAREHOUSE, "", enabledWarehouse ? "YES" : "NO", ENABLED_MULTI_WAREHOUSE);
        baseInvoice.setCustomNumberAndDatesTable(numberAndDatesTable);

        CustomisedITextTable productTable = new CustomisedITextTable();
        productTable.addColumn(ITEM_NAME, accountingLocalizer.localizeAccounting(PdfLocalizationName.name));
        productTable.addColumn(ITEM_DESCRIPTION, accountingLocalizer.localizeAccounting(PdfLocalizationName.description));
        productTable.addColumn(ITEM_WAREHOUSE, accountingLocalizer.localizeAccounting(PdfLocalizationName.warehouse));
        productTable.addColumn(ITEM_CURRENT_QTY, accountingLocalizer.localizeAccounting(PdfLocalizationName.currentQty));
        productTable.addColumn(ITEM_USED_QTY, accountingLocalizer.localizeAccounting(PdfLocalizationName.usedQty));
        productTable.addColumn(ITEM_NEW_QTY, accountingLocalizer.localizeAccounting(PdfLocalizationName.newQty));
        productTable.addColumn(ITEM_QTY, accountingLocalizer.localizeAccounting(PdfLocalizationName.total));
        productTable.addColumn(ITEM_COST_PER_ITEM, accountingLocalizer.localizeAccounting(PdfLocalizationName.costPerItem));
        productTable.addColumn(ITEM_PROJECT, accountingLocalizer.localizeAccounting(PdfLocalizationName.project));

        List<String> columnsValue = Lists.newArrayList();
        if (fp.getObjectId() != null) {
            if (item.getProductItems() != null) {
                for (ProductItem productItem : item.getProductItems()) {
                    columnsValue.clear();
                    columnsValue.add(escapeHtml(productItem.getProductNumber() + "->" + productItem.getName()));
                    columnsValue.add(escapeHtml(productItem.getDescription()));
                    columnsValue.add(escapeHtml(productItem.getWarehouseName()));
                    columnsValue.add(getDecimalFormat(productItem.getCurrentQty()));
                    columnsValue.add(getDecimalFormat(productItem.getUsedQty()));
                    columnsValue.add(getDecimalFormat(productItem.getNewQty()));
                    columnsValue.add(getDecimalFormat(productItem.getTotalQty()));
                    columnsValue.add(getDecimalFormat(productItem.getUnitpPrice()));
                    columnsValue.add(escapeHtml(productItem.getProjectName()));
                    productTable.addRow(columnsValue.toArray(new String[]{}));
                }
            }
        } else {
            Integer length = Integer.parseInt(request.getParameter(PDFTransferObject.LENGTH));
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    String name = request.getParameter(PDFTransferObject.ITEM_NAME + i);
                    String description = request.getParameter(PDFTransferObject.DESCRIPTION + i);
                    String warehouse = request.getParameter(PDFTransferObject.ITEM_WAREHOUSE + i);

                    BigDecimal currentQty = BigDecimal.ZERO;
                    String currentQtyString = request.getParameter(PDFTransferObject.QTY + i);
                    if (!"null".equals(currentQtyString)) {
                        currentQty = BigDecimal.valueOf(Double.valueOf(currentQtyString));
                    }
                    BigDecimal usedQty = BigDecimal.ZERO;
                    String usedQtyString = request.getParameter(PDFTransferObject.ITEM_USED_QTY + i);
                    if (!"null".equals(usedQtyString)) {
                        usedQty = BigDecimal.valueOf(Double.valueOf(usedQtyString));
                    }
                    BigDecimal newQty = BigDecimal.ZERO;
                    String newQtyString = request.getParameter(PDFTransferObject.ITEM_NEW_QTY + i);
                    if (!"null".equals(newQtyString)) {
                        newQty = BigDecimal.valueOf(Double.valueOf(newQtyString));
                    }
                    BigDecimal totQty = BigDecimal.ZERO;
                    String totQtyString = request.getParameter(PDFTransferObject.ITEM_TOTAL_QTY + i);
                    if (!"null".equals(totQtyString)) {
                        totQty = BigDecimal.valueOf(Double.valueOf(totQtyString));
                    }
                    BigDecimal unitPrice = BigDecimal.ZERO;
                    String unitPriceString = request.getParameter(PDFTransferObject.UNIT_PRICE + i);
                    if (!"null".equals(unitPriceString)) {
                        unitPrice = BigDecimal.valueOf(Double.valueOf(unitPriceString));
                    }
                    String projectName = request.getParameter(PDFTransferObject.PROJECT_ID + i);

                    columnsValue.clear();
                    columnsValue.add(escapeHtml(name));
                    columnsValue.add(escapeHtml(description));
                    columnsValue.add(escapeHtml(warehouse));
                    columnsValue.add(getDecimalFormat(currentQty));
                    columnsValue.add(getDecimalFormat(usedQty));
                    columnsValue.add(getDecimalFormat(newQty));
                    columnsValue.add(getDecimalFormat(totQty));
                    columnsValue.add(getDecimalFormat(unitPrice));
                    columnsValue.add(escapeHtml(projectName));
                    productTable.addRow(columnsValue.toArray(new String[]{}));
                }
            }
        }
        baseInvoice.setCustomProductTable(productTable);
        genericPdfData.setBaseInvoice(baseInvoice);
        return genericPdfData;
    }

    private String getDecimalFormat(BigDecimal value) {
        return value != null ? Utils.formatDecimal(value) : "";
    }

	@Override
	protected void setFileName(EdsUser user, Object dataClass) {
		setFileName("Stock_Adjustment_" + dateFormat(user.getUserDate()));
	}

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localizeAccounting(PdfLocalizationName.stockAdjustments);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.STOCK_ADJUSTMENT;
    }
}
