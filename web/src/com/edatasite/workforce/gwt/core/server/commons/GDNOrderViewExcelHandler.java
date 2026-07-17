package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilxom Lutfullaev on 2/20/2018.
 */
public class GDNOrderViewExcelHandler extends BaseExcelHandler {

    private static Logger logger = LoggerFactory.getLogger(GDNOrderViewExcelHandler.class);

    @Autowired
    protected CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    protected CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private QuoteServiceLocal quoteService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ItemManager itemManager;

    HSSFWorkbook workbook;
    HSSFSheet daSheet, invSheet, plSheet;
    HSSFCellStyle boldRight;

    @Override
    protected void setFileName() {
        filename = "GDN_Order_" + dateFormat(userManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        try {
            DecimalFormat decimalFormat = ServerUtils.getDecimalFormat(ServerUtils.getCalculationScale());
            ListingFilterParameter obj = (ListingFilterParameter) object;
            EdsCompany company = userManager.getUser().getCompany();
            ShippingData shippingData = quoteService.getShippingData(obj.getObjectId(), false, true);
            NewInvoice invoice = shippingData.getInvoice();
            workbook = new HSSFWorkbook();
            daSheet = workbook.createSheet("DA");

            if (shippingData != null) {
                CrmAccountItem customer = shippingData.getCustomer();
                Address address = customer.getMailAddresses()[0];
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(ViewName.ProductServiceView);

                HSSFFont font = workbook.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);

                boldRight = getCellStyle(font, CellStyle.ALIGN_RIGHT, false, false, true);
                HSSFCellStyle boldCenter = getCellStyle(font, CellStyle.ALIGN_CENTER, false, false, true);
                HSSFCellStyle reportTitleStyle = getCellStyle(font, CellStyle.ALIGN_CENTER, true, true, true);
                HSSFCellStyle rightAlignment = getCellStyle(font, CellStyle.ALIGN_RIGHT, false, false, false);
                HSSFCellStyle leftAlignment = getCellStyle(font, CellStyle.ALIGN_LEFT, false, false, false);
                HSSFCellStyle boldLeftAlignment = getCellStyle(font, CellStyle.ALIGN_LEFT, false, false, true);
                HSSFCellStyle centerAlignment = getCellStyle(font, CellStyle.ALIGN_CENTER, false, false, false);
                HSSFCellStyle headerStyle = getCellStyle(font, CellStyle.ALIGN_CENTER, true, false, true);
                HSSFCellStyle bordered = getCellStyle(font, CellStyle.ALIGN_CENTER, false, true, false);
                HSSFCellStyle borderedBold = getCellStyle(font, CellStyle.ALIGN_CENTER, false, true, true);

                HSSFRow row;
                HSSFCell cell;

//---------------------------------------PL--------------------------------------------------------------------------------
                plSheet = workbook.createSheet("PL");
                int plRowNum = drawHeader(company, plSheet, 4, 7);
                plSheet.setColumnWidth(1, 5 * 256);
                plSheet.setColumnWidth(2, 25 * 256);
                for (int i = 3; i < 8; i++) {
                    plSheet.setColumnWidth(i, 10 * 256);
                }
                plSheet.setColumnWidth(8, 3 * 256);
                row = plSheet.createRow(plRowNum);
                createAndWriteToCell(plSheet, row, 1, "PACKING LIST-" + (invoice != null ? invoice.getInvoiceNumber() : ""), reportTitleStyle, true, true, row.getRowNum(), row.getRowNum(), 1, 7);

                plRowNum++;
                row = plSheet.createRow(plRowNum);
                createAndWriteToCell(plSheet, row, 1, "Shipper: ", boldLeftAlignment, true, true, row.getRowNum(), row.getRowNum(), 1, 3);

                createAddressCells(plSheet, leftAlignment, plRowNum + 1, 1, 3, company.getName(), "Office # I-02,", "Dubai Airport Free Zone", "Tel: 04 2525 325", "Fax:04 2525 645");
                drawBorderToRegion(plSheet, new CellRangeAddress(plRowNum, plRowNum + 6, 1, 3));

                createAndWriteToCell(plSheet, row, 4, "Our Ref No.: " + (invoice != null ? invoice.getInvoiceNumber() : ""), leftAlignment, true, true, row.getRowNum(), row.getRowNum() + 4, 4, 7);

                row = plSheet.getRow(plRowNum + 5);
                createAndWriteToCell(plSheet, row, 4, "Customer Purchase Order No.", leftAlignment, true, true, row.getRowNum(), row.getRowNum(), 4, 7);
                plRowNum = row.getRowNum() + 1;

                row = plSheet.createRow(plRowNum);
                createAndWriteToCell(plSheet, row, 4, "Email confirmation", centerAlignment, true, true, row.getRowNum(), row.getRowNum(), 4, 7);

                plRowNum = row.getRowNum() + 1;
                row = plSheet.createRow(plRowNum);

                createAndWriteToCell(plSheet, row, 1, "Consignee:", boldLeftAlignment, true, true, row.getRowNum(), row.getRowNum(), 1, 3);
                createAndWriteToCell(plSheet, row, 4, "Terms of Delivery:", boldCenter, true, true, row.getRowNum(), row.getRowNum(), 4, 7);

                createAddressCells(plSheet, leftAlignment, plRowNum + 1, 1, 3, shippingData.getClientName() != null && !shippingData.getClientName().isEmpty() ? shippingData.getClientName() : "",
                        (address.getAddress() != null && !address.getAddress().isEmpty() ? address.getAddress() + "," : "") + (address.getAddressb() != null && !address.getAddressb().isEmpty() ? address.getAddressb() : ""),
                        (address.getCity() != null && !address.getCity().isEmpty() ? address.getCity() : "") + " " + (address.getCountry() != null && !address.getCountry().isEmpty() ? address.getCountry() : ""),
                        customer.getPhone() != null && !customer.getPhone().isEmpty() ? address.getCountry() : "", customer.getFax() != null && !customer.getFax().isEmpty() ? address.getCountry() : "");
                drawBorderToRegion(plSheet, new CellRangeAddress(plRowNum, plRowNum + 5, 1, 3));

                row = plSheet.getRow(plRowNum + 1);
                createAndWriteToCell(plSheet, row, 4, "Ex-Works", centerAlignment, true, true, row.getRowNum(), row.getRowNum() + 4, 4, 7);

                plRowNum += 6;
                row = plSheet.createRow(plRowNum);
                createAndWriteToCell(plSheet, row, 1, "Sr#", boldCenter, false, true, plRowNum, plRowNum, 1, 1);
                createAndWriteToCell(plSheet, row, 2, "Description of Goods", boldCenter, false, true, plRowNum, plRowNum, 2, 2);
                createAndWriteToCell(plSheet, row, 3, "HS Code", boldCenter, false, true, plRowNum, plRowNum, 3, 3);
                createAndWriteToCell(plSheet, row, 4, "County of Origin", boldCenter, false, true, plRowNum, plRowNum, 4, 4);
                createAndWriteToCell(plSheet, row, 5, "Qty (Pcs)", boldCenter, false, true, plRowNum, plRowNum, 5, 5);
                createAndWriteToCell(plSheet, row, 6, "Part No", boldCenter, false, true, plRowNum, plRowNum, 6, 6);
                createAndWriteToCell(plSheet, row, 7, "Weight Kgs", boldCenter, false, true, plRowNum, plRowNum, 7, 7);

                int order = 1;
                BigDecimal totalQty = BigDecimal.ZERO, totalAmount = BigDecimal.ZERO, totalNoOfPcs = BigDecimal.ZERO;
                for (ShippingDataItem di : shippingData.getItems()) {
//                    String partNo = "";
                    totalNoOfPcs = totalNoOfPcs.add(di.getNumberOfPacks() != null ? di.getNumberOfPacks() : BigDecimal.ZERO);
                    /*if (invoice != null) {
                        for (NewInvoiceItem sdi : invoice.getItems()) {
                            if (di.getItem().getId().equals(sdi.getItemID())) {
                                partNo = di.getItem().getDescription();
                            }
                        }
                    }*/
                    row = plSheet.createRow(row.getRowNum() + 1);
                    String cOrigin = "", hsCode = "";
                    BigDecimal weight = BigDecimal.ZERO;
                    EdsItem edsItem = itemManager.get(di.getItem().getId());
                    if (edsItem != null) {
                        List<CompanyCustomFieldItem> companyCustomFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getCustomFields(), customFieldsItems);
                        if (companyCustomFieldItems != null && !companyCustomFieldItems.isEmpty()) {
                            for (CompanyCustomFieldItem cfItem : companyCustomFieldItems) {
                                if ("County Origin".equals(cfItem.getFieldName()) || "COO".equals(cfItem.getFieldName())) {
                                    cOrigin = cfItem.getFieldStringValue();
                                } else if ("HS Code".equals(cfItem.getFieldName())) {
                                    hsCode = cfItem.getFieldStringValue();
                                } else if ("Product Weight".equals(cfItem.getFieldName())) {
                                    if (StringUtils.isNotEmpty(cfItem.getFieldStringValue()) && !"N/A".equals(cfItem.getFieldStringValue())) {
                                        weight = new BigDecimal(cfItem.getFieldStringValue().replace(",", "."));
                                        weight = weight.multiply(di.getAmount() != null ? di.getAmount() : BigDecimal.ONE);
                                    }
                                }
                            }
                        }
                    }

                    createAndWriteToCell(plSheet, row, 1, (order++) + "", centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 1, 1);
                    createAndWriteToCell(plSheet, row, 2, edsItem.getDescription(), leftAlignment, false, true, row.getRowNum(), row.getRowNum(), 2, 2);
                    createAndWriteToCell(plSheet, row, 3, hsCode, centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 3, 3);
                    createAndWriteToCell(plSheet, row, 4, cOrigin, centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 4, 4);
                    createAndWriteToCell(plSheet, row, 5, decimalFormat.format(di.getAmount()), centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 5, 5);
                    createAndWriteToCell(plSheet, row, 6, edsItem.getName(), centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 6, 6);
                    createAndWriteToCell(plSheet, row, 7, decimalFormat.format(weight), centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 7, 7);
                    totalQty = totalQty.add(di.getAmount());
                    totalAmount = totalAmount.add(weight);
                }
                row = plSheet.createRow(row.getRowNum() + 1);
                createAndWriteToCell(plSheet, row, 1, "", rightAlignment, true, true, row.getRowNum(), row.getRowNum(), 1, 4);
                createAndWriteToCell(plSheet, row, 5, decimalFormat.format(totalQty), boldCenter, false, true, row.getRowNum(), row.getRowNum(), 5, 5);
                createAndWriteToCell(plSheet, row, 6, "", centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 6, 6);
                createAndWriteToCell(plSheet, row, 7, decimalFormat.format(totalAmount), boldCenter, false, true, row.getRowNum(), row.getRowNum(), 7, 7);

                row = plSheet.createRow(row.getRowNum() + 1);
                createAndWriteToCell(plSheet, row, 1, "Total No of Pcs           :" + totalQty.intValue() + " Pcs", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 7, 512, CellStyle.VERTICAL_CENTER);
                row = plSheet.createRow(row.getRowNum() + 1);
                createAndWriteToCell(plSheet, row, 1, "No of Packages          :" + totalNoOfPcs.intValue() + " Cartons", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 7, 512, CellStyle.VERTICAL_CENTER);
                row = plSheet.createRow(row.getRowNum() + 1);
                createAndWriteToCell(plSheet, row, 1, "Total Gross Weight     :" + decimalFormat.format(totalAmount) + " Kgs.", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 7, 512, CellStyle.VERTICAL_CENTER);
                drawBorderToRegion(plSheet, new CellRangeAddress(row.getRowNum() - 2, row.getRowNum(), 1, 7));
//---------------------------------------Invoice---------------------------------------------------------------------------
                totalNoOfPcs = BigDecimal.ZERO;
                CellRangeAddress cellRangeAddress = null;
                if (invoice != null) {
                    NumberToWord numberToWordConverter = new NumberToWord_en();
                    invSheet = workbook.createSheet("Invoice");
                    int invRowNum = drawHeader(company, invSheet, 4, 9);
                    invSheet.setColumnWidth(1, 4 * 256);
                    invSheet.setColumnWidth(2, 20 * 256);
                    invSheet.setColumnWidth(3, 10 * 256);
                    for (int i = 4; i < 10; i++) {
                        invSheet.setColumnWidth(i, 9 * 256);
                    }
                    row = invSheet.createRow(invRowNum);
                    createAndWriteToCell(invSheet, row, 1, "INVOICE NO.: " + invoice.getInvoiceNumber(), reportTitleStyle, true, true, row.getRowNum(), row.getRowNum(), 1, 9);

                    invRowNum++;
                    row = invSheet.createRow(invRowNum);
                    createAndWriteToCell(invSheet, row, 1, "Shipper: ", boldLeftAlignment, true, true, row.getRowNum(), row.getRowNum(), 1, 3);

                    createAddressCells(invSheet, leftAlignment, invRowNum + 1, 1, 3, company.getName(), "Office # I-02,", "Dubai Airport Free Zone", "Tel: 04 2525 325", "Fax:04 2525 645");
                    drawBorderToRegion(invSheet, new CellRangeAddress(invRowNum, invRowNum + 6, 1, 3));

                    createAndWriteToCell(invSheet, row, 4, "Our Ref No.: " + invoice.getInvoiceNumber(), leftAlignment, true, true, row.getRowNum(), row.getRowNum() + 4, 4, 7);
                    createAndWriteToCell(invSheet, row, 8, "Date:" + dateFormat(invoice.getInvoiceDate().getDate()), leftAlignment, true, true, row.getRowNum(), row.getRowNum() + 6, 8, 9);

                    row = invSheet.getRow(invRowNum + 5);
                    createAndWriteToCell(invSheet, row, 4, "Customer Purchase Order No.", leftAlignment, true, true, row.getRowNum(), row.getRowNum(), 4, 7);
                    invRowNum = row.getRowNum() + 1;

                    row = invSheet.createRow(invRowNum);
                    createAndWriteToCell(invSheet, row, 4, "Email confirmation", centerAlignment, true, true, row.getRowNum(), row.getRowNum(), 4, 7);

                    invRowNum = row.getRowNum() + 1;
                    row = invSheet.createRow(invRowNum);

                    createAndWriteToCell(invSheet, row, 1, "Consignee:", boldLeftAlignment, true, true, row.getRowNum(), row.getRowNum(), 1, 3);
                    createAndWriteToCell(invSheet, row, 4, "Terms of Delivery:", boldCenter, true, true, row.getRowNum(), row.getRowNum(), 4, 7);
                    createAndWriteToCell(invSheet, row, 8, "Payment Terms", boldCenter, true, true, row.getRowNum(), row.getRowNum(), 8, 9);

                    CrmAccountItem client = shippingData.getCustomer();
                    createAddressCells(invSheet, leftAlignment, invRowNum + 1, 1, 3, shippingData.getClientName() != null ? shippingData.getClientName() : "",
                            (address.getAddress() != null ? address.getAddress() + "," : "") + (address.getAddressb() != null ? address.getAddressb() : ""),
                            (address.getCity() != null ? address.getCity() : "") + (address.getCountry() != null ? address.getCountry() : ""),
                            customer.getPhone() != null ? customer.getPhone() : "", customer.getFax() != null ? customer.getFax() : "");
                    drawBorderToRegion(invSheet, new CellRangeAddress(invRowNum, invRowNum + 5, 1, 3));

                    row = invSheet.getRow(invRowNum + 1);
                    createAndWriteToCell(invSheet, row, 4, "Ex-Works", centerAlignment, true, true, row.getRowNum(), row.getRowNum() + 4, 4, 7);
                    createAndWriteToCell(invSheet, row, 8, "Bank Transfer", centerAlignment, true, true, row.getRowNum(), row.getRowNum() + 4, 8, 9);

                    invRowNum += 6;
                    row = invSheet.createRow(invRowNum);
                    createAndWriteToCell(invSheet, row, 1, "Sr#", boldCenter, false, true, invRowNum, invRowNum, 1, 1);
                    createAndWriteToCell(invSheet, row, 2, "Description of Goods", boldCenter, false, true, invRowNum, invRowNum, 2, 2);
                    createAndWriteToCell(invSheet, row, 3, "HS Code", boldCenter, false, true, invRowNum, invRowNum, 3, 3);
                    createAndWriteToCell(invSheet, row, 4, "County of Origin", boldCenter, false, true, invRowNum, invRowNum, 4, 4);
                    createAndWriteToCell(invSheet, row, 5, "Qty (Pcs)", boldCenter, false, true, invRowNum, invRowNum, 5, 5);
                    createAndWriteToCell(invSheet, row, 6, "Part No", boldCenter, false, true, invRowNum, invRowNum, 6, 6);
                    createAndWriteToCell(invSheet, row, 7, "Weight Kgs", boldCenter, false, true, invRowNum, invRowNum, 7, 7);
                    createAndWriteToCell(invSheet, row, 8, "UnitPrice (" + invoice.getCurrencyName() + ")", boldCenter, false, true, invRowNum, invRowNum, 8, 8);
                    row = createAndWriteToCell(invSheet, row, 9, "Amount (" + invoice.getCurrencyName() + ")", boldCenter, false, true, invRowNum, invRowNum, 9, 9);

                    totalQty = BigDecimal.ZERO;
                    totalNoOfPcs = BigDecimal.ZERO;
                    BigDecimal totalNoOfPack = BigDecimal.ZERO;
                    BigDecimal totalWeight = BigDecimal.ZERO;
                    order = 1;
                    for (NewInvoiceItem invItem : invoice.getItems()) {
                        row = invSheet.createRow(row.getRowNum() + 1);
                        invRowNum = row.getRowNum();
                        String cOrigin = "", hsCode = "";
                        BigDecimal weight = BigDecimal.ZERO;
                        BigDecimal noOfPack = BigDecimal.ZERO;
//                        String partNo = "";
                        for (ShippingDataItem sdi : shippingData.getItems()) {
                            if (sdi.getItem().getId().equals(invItem.getItemID())) {
                                totalNoOfPcs = totalNoOfPcs.add(sdi.getNumberOfPacks());
//                                partNo = sdi.getItem().getDescription();
                                noOfPack = sdi.getNumberOfPacks();
                                totalNoOfPack = totalNoOfPack.add(noOfPack);
                            }
                        }
                        EdsItem edsItem = itemManager.get(invItem.getItemID());
                        if (edsItem != null) {
                            List<CompanyCustomFieldItem> companyCustomFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getCustomFields(), customFieldsItems);
                            if (companyCustomFieldItems != null && !companyCustomFieldItems.isEmpty()) {
                                for (CompanyCustomFieldItem cfItem : companyCustomFieldItems) {
                                    if ("HS Code".equals(cfItem.getFieldName())) {
                                        if (StringUtils.isNotEmpty(cfItem.getFieldStringValue())) {
                                            hsCode = cfItem.getFieldStringValue();
                                        }
                                    } else if ("County Origin".equals(cfItem.getFieldName()) || "COO".equals(cfItem.getFieldName())) {
                                        if (StringUtils.isNotEmpty(cfItem.getFieldStringValue())) {
                                            cOrigin = cfItem.getFieldStringValue();
                                        }
                                    } else if ("Product Weight".equals(cfItem.getFieldName())) {
                                        if (StringUtils.isNotEmpty(cfItem.getFieldStringValue()) && !"N/A".equals(cfItem.getFieldStringValue())) {
                                            weight = new BigDecimal(cfItem.getFieldStringValue().replace(",", "."));
                                            weight = weight.multiply(invItem.getQuantity() != null ? invItem.getQuantity() : BigDecimal.ONE);
                                            totalWeight = totalWeight.add(weight);
                                        }
                                    }
                                }
                            }
                        }

                        createAndWriteToCell(invSheet, row, 1, (order++) + "", centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 1, 1);
                        createAndWriteToCell(invSheet, row, 2, invItem.getItemNumber() + " " + invItem.getItemName(), leftAlignment, false, true, row.getRowNum(), row.getRowNum(), 2, 2);
                        createAndWriteToCell(invSheet, row, 3, hsCode, centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 3, 3);
                        createAndWriteToCell(invSheet, row, 4, cOrigin, centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 4, 4);
                        createAndWriteToCell(invSheet, row, 5, invItem.getQuantity() != null ? decimalFormat.format(invItem.getQuantity()) : "", centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 5, 5);
                        createAndWriteToCell(invSheet, row, 6, edsItem.getName(), centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 6, 6);
                        createAndWriteToCell(invSheet, row, 7, decimalFormat.format(weight), centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 7, 7);
                        createAndWriteToCell(invSheet, row, 8, invItem.getUnitPrice() != null ? decimalFormat.format(invItem.getUnitPrice()) : "", rightAlignment, false, true, row.getRowNum(), row.getRowNum(), 8, 8);
                        createAndWriteToCell(invSheet, row, 9, decimalFormat.format(invItem.getQuantity().multiply(invItem.getUnitPrice())), rightAlignment, false, true, row.getRowNum(), row.getRowNum(), 9, 9);
                        if (invItem.getQuantity() != null) {
                            totalQty = totalQty.add(invItem.getQuantity());
                        }
                    }
                    //Total row
                    row = invSheet.createRow(row.getRowNum() + 1);
                    createAndWriteToCell(invSheet, row, 1, "", rightAlignment, true, true, row.getRowNum(), row.getRowNum(), 1, 4);
                    createAndWriteToCell(invSheet, row, 5, decimalFormat.format(totalQty), boldCenter, false, true, row.getRowNum(), row.getRowNum(), 5, 5);
                    createAndWriteToCell(invSheet, row, 6, "", centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 6, 6);
                    createAndWriteToCell(invSheet, row, 7, decimalFormat.format(totalWeight), boldCenter, false, true, row.getRowNum(), row.getRowNum(), 7, 7);
                    createAndWriteToCell(invSheet, row, 8, "", centerAlignment, false, true, row.getRowNum(), row.getRowNum(), 8, 8);
                    createAndWriteToCell(invSheet, row, 9, decimalFormat.format(invoice.getTotal()), boldCenter, false, true, row.getRowNum(), row.getRowNum(), 9, 9);

                    row = invSheet.createRow(row.getRowNum() + 1);
                    createAndWriteToCell(invSheet, row, 1, "Total No of Pcs           :" + totalQty.intValue() + " Pcs", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 9, 512, CellStyle.VERTICAL_CENTER);
                    row = invSheet.createRow(row.getRowNum() + 1);
                    createAndWriteToCell(invSheet, row, 1, "No of Packages          :" + totalNoOfPcs.intValue() + " Cartons", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 9, 512, CellStyle.VERTICAL_CENTER);
                    row = invSheet.createRow(row.getRowNum() + 1);
                    createAndWriteToCell(invSheet, row, 1, "Total Gross Weight     :" + decimalFormat.format(totalWeight) + " Kgs.", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 9, 512, CellStyle.VERTICAL_CENTER);
                    drawBorderToRegion(invSheet, new CellRangeAddress(row.getRowNum() - 2, row.getRowNum(), 1, 9));

                    row = invSheet.createRow(row.getRowNum() + 1);
                    createAndWriteToCell(invSheet, row, 1, shippingData.getShippingLabel(), leftAlignment, true, true, row.getRowNum(), row.getRowNum(), 1, 9, 512, CellStyle.VERTICAL_CENTER);
                    row = invSheet.createRow(row.getRowNum() + 1);
                    createAndWriteToCell(invSheet, row, 1, "Total Amount", boldLeftAlignment, true, true, row.getRowNum(), row.getRowNum(), 1, 2);
                    createAndWriteToCell(invSheet, row, 3, invoice.getCurrencyName() + ": " + numberToWordConverter.toWord(invoice.getTotal()), boldLeftAlignment, true, true, row.getRowNum(), row.getRowNum(), 3, 8);
                    createAndWriteToCell(invSheet, row, 9, decimalFormat.format(invoice.getTotal()), boldRight, false, true, row.getRowNum(), row.getRowNum(), 9, 9);
                }
//---------------------------------------Delivery Advice-------------------------------------------------------------------
                int rowNum = drawHeader(company, daSheet, 4, 7);
                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "DELIVERY ADVICE", reportTitleStyle, true, true, rowNum, rowNum, 1, 7);
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "Our Ref: " + (invoice != null && invoice.getInvoiceNumber() != null && !invoice.getInvoiceNumber().isEmpty() ? invoice.getInvoiceNumber() : ""), rightAlignment, true, false, rowNum, rowNum, 1, 7);
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "F/Z Lic No. : 2427", rightAlignment, true, false, rowNum, rowNum, 1, 7);
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "D.A No.: " + (invoice != null && invoice.getInvoiceNumber() != null && !invoice.getInvoiceNumber().isEmpty() ? invoice.getInvoiceNumber() : ""), rightAlignment, true, false, rowNum, rowNum, 1, 7);
                rowNum += 2;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 5, "Import Code", reportTitleStyle, false, true, row.getRowNum(), row.getRowNum(), 5, 5);
                createAndWriteToCell(daSheet, row, 6, "Agent Code", reportTitleStyle, false, true, row.getRowNum(), row.getRowNum(), 6, 6);
                createAndWriteToCell(daSheet, row, 7, "Rep. Card No.", reportTitleStyle, false, true, row.getRowNum(), row.getRowNum(), 7, 7);
                rowNum++;
                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 5, "", leftAlignment, true, true, row.getRowNum(), row.getRowNum(), 5, 7);
                drawBorderToRegion(daSheet, rowNum - 1, rowNum, 5, 7);
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "Date: " + (invoice != null ? dateFormat(invoice.getInvoiceDate().getDate()) : ""), leftAlignment, false, false, row.getRowNum(), row.getRowNum(), 1, 1);
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "The Director,", leftAlignment, false, false, row.getRowNum(), row.getRowNum(), 1, 1);
                rowNum++;

                row = daSheet.createRow(rowNum);
                cell = row.createCell(1);
                cell.setCellValue("Dept of Ports & Customs");
                rowNum++;

                row = daSheet.createRow(rowNum);
                cell = row.createCell(1);
                cell.setCellValue("Please authorize release of the below mentioned goods from our warehouse to M/S:");
                rowNum++;
                row = daSheet.createRow(rowNum);
                cell = row.createCell(1);
//                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 4, 1, 4));
                cell = row.createCell(5);
                cell.setCellValue(shippingData.getClientName() != null ? shippingData.getClientName() : "");
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 5, 7));
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 5, (address.getAddress() != null && !address.getAddress().isEmpty() ? address.getAddress() + "," : "") + (address.getAddressb() != null && !address.getAddressb().isEmpty() ? address.getAddressb() : ""), leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 5, 7);
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 5, (address.getCity() != null && !address.getCity().isEmpty() ? address.getCity() + ", " : "") + (address.getCountry() != null && !address.getCountry().isEmpty() ? address.getCountry() : ""), leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 5, 7);
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 5, "Tel# " + (customer.getPhone() != null ? customer.getPhone() : ""), leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 5, 7);
                drawBorderToRegion(daSheet, rowNum - 3, rowNum, 5, 7);
                rowNum++;

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "Marks& Numbers", reportTitleStyle, false, true, row.getRowNum(), row.getRowNum(), 1, 1);
                createAndWriteToCell(daSheet, row, 2, "Type", reportTitleStyle, false, true, row.getRowNum(), row.getRowNum(), 2, 2);
                createAndWriteToCell(daSheet, row, 3, "Quantity\n(CTN)", reportTitleStyle, false, true, row.getRowNum(), row.getRowNum(), 3, 3);
                createAndWriteToCell(daSheet, row, 4, "Weight Kgs.", reportTitleStyle, false, true, row.getRowNum(), row.getRowNum(), 4, 4);
                createAndWriteToCell(daSheet, row, 5, "Description of Goods", centerAlignment, true, true, row.getRowNum(), row.getRowNum(), 5, 7);
                drawBorderToRegion(daSheet, rowNum, rowNum, 5, 7);
                rowNum++;
                row = daSheet.createRow(rowNum);

                totalQty = BigDecimal.ZERO;
                BigDecimal totalPcs = BigDecimal.ZERO;
                BigDecimal totalreceived = BigDecimal.ZERO;
                StringBuffer prods = new StringBuffer("");
                StringBuffer cfCodes = new StringBuffer("");
                StringBuffer countyOrigin = new StringBuffer("");
                if (shippingData.getItems() != null) {
                    int firstLineRow = rowNum;
                    for (ShippingDataItem di : shippingData.getItems()) {
                        row = daSheet.createRow(rowNum);
                        cell = row.createCell(1);
                        cell.setCellStyle(borderedBold);
                        createAndWriteToCell(daSheet, row, 2, "CTN", bordered, false, true, row.getRowNum(), row.getRowNum(), 2, 2);
                        createAndWriteToCell(daSheet, row, 3, String.valueOf(di.getNumberOfPacks() != null ? di.getNumberOfPacks().floatValue() : 0), bordered, false, true, row.getRowNum(), row.getRowNum(), 3, 3);

                        totalQty = totalQty.add(di.getNumberOfPacks() != null ? di.getNumberOfPacks() : BigDecimal.ZERO);
                        totalPcs = totalPcs.add(di.getAmount() != null ? di.getAmount() : BigDecimal.ZERO);
                        prods.append(di.getItem().getName()).append(", ");
                        BigDecimal weight = BigDecimal.ZERO;
                        EdsItem edsItem = itemManager.get(di.getItem().getId());
                        if (edsItem != null) {
                            List<CompanyCustomFieldItem> companyCustomFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getCustomFields(), customFieldsItems);
                            if (companyCustomFieldItems != null && !companyCustomFieldItems.isEmpty()) {
                                for (CompanyCustomFieldItem cfItem : companyCustomFieldItems) {
                                    if ("HS Code".equals(cfItem.getFieldName())) {
                                        cfCodes.append(cfItem.getFieldStringValue()).append(", ");
                                    } else if ("County Origin".equals(cfItem.getFieldName()) || "COO".equals(cfItem.getFieldName())) {
                                        if (StringUtils.isNotEmpty(cfItem.getFieldStringValue())) {
                                            countyOrigin.append(cfItem.getFieldStringValue()).append(", ");
                                        }
                                    } else if ("Product Weight".equals(cfItem.getFieldName())) {
                                        if (StringUtils.isNotEmpty(cfItem.getFieldStringValue()) && !"N/A".equals(cfItem.getFieldStringValue())) {
                                            weight = new BigDecimal(cfItem.getFieldStringValue().replace(",", "."));
                                            weight = weight.multiply(di.getAmount() != null ? di.getAmount() : BigDecimal.ONE);
                                        }
                                    }
                                }
                            }
                        }
                        createAndWriteToCell(daSheet, row, 4, String.valueOf(weight.floatValue()), bordered, false, true, rowNum, rowNum, 1, 1);
                        totalreceived = totalreceived.add(weight);
                        rowNum++;
                    }
                    daSheet.addMergedRegion(new CellRangeAddress(firstLineRow, rowNum, 1, 1));
                }

                for (int i = 0; i < 50; i++) {
                    HSSFRow row1 = daSheet.createRow(rowNum + i);
                    for (int j = 0; j < 8; j++) {
                        row1.createCell(j);
                    }
                }

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 2, "Total", borderedBold, false, true, row.getRowNum(), row.getRowNum(), 2, 2);
                createAndWriteToCell(daSheet, row, 3, String.valueOf(totalQty.floatValue()), borderedBold, false, true, row.getRowNum(), row.getRowNum(), 3, 3);
                createAndWriteToCell(daSheet, row, 4, String.valueOf(totalreceived.floatValue()), borderedBold, false, true, row.getRowNum(), row.getRowNum(), 4, 4);

                int rowCount = shippingData.getItems() != null ? shippingData.getItems().size() : 0;
                row = daSheet.getRow(rowNum - rowCount);
                createAndWriteToCell(daSheet, row, 5, totalQty.intValue() + " Cartons (" + totalPcs.floatValue() + " Pcs)", bordered, true, true, rowNum - rowCount, rowNum - rowCount, 5, 7);

                row = daSheet.getRow(rowNum - rowCount + 1);
                createAndWriteToCell(daSheet, row, 5, prods.deleteCharAt(prods.lastIndexOf(",")).toString(), bordered, true, true, rowNum - rowCount + 1, rowNum + 3, 5, 7);
                rowNum++;

                drawBorderToRegion(daSheet, rowNum - (shippingData.getItems().size()), rowNum, 1, 1);
                drawBorderToRegion(daSheet, rowNum - (shippingData.getItems().size()), rowNum, 2, 4);

                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "Payment Method Mark Where appropriate", leftAlignment, true, true, rowNum, rowNum, 1, 4);
                rowNum++;
                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 1, "CDR Cash       CDR Bank                    Deposit", leftAlignment, true, false, rowNum, rowNum, 1, 4);
                rowNum++;
                row = daSheet.createRow(rowNum);
                row.setHeightInPoints(20);
                createAndWriteToCell(daSheet, row, 1, "Credit A/c*                 Stan G*                   Bank G*", leftAlignment, true, false, rowNum, rowNum, 1, 4);
                drawBorderToRegion(daSheet, rowNum - 4, rowNum, 5, 7);
                rowNum++;
                row = daSheet.createRow(rowNum);
                row.setHeightInPoints(20);
                createAndWriteToCell(daSheet, row, 1, "FTT                        Alcohol                         Other", leftAlignment, true, false, rowNum, rowNum, 1, 4);
                createAndWriteToCell(daSheet, row, 5, "HS Code : " + cfCodes.toString(), borderedBold, true, true, rowNum, rowNum, 5, 7);
                rowNum++;
                row = daSheet.createRow(rowNum);
                createAndWriteToCell(daSheet, row, 5, "Exit", reportTitleStyle, false, false, rowNum, rowNum, 5, 5);
                createAndWriteToCell(daSheet, row, 6, "Destination", reportTitleStyle, true, true, rowNum, rowNum, 6, 7);
                drawBorderToRegion(daSheet, rowNum - 4, rowNum, 1, 4);
                rowNum++;
                row = daSheet.createRow(rowNum);
                cell = row.createCell(1);
                cell.setCellValue("Ref: A/C Nos.:");
                cell.setCellStyle(reportTitleStyle);
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 4));
                drawBorderToRegion(daSheet, rowNum, rowNum, 1, 4);
                cell = row.createCell(5);
                cell.setCellValue("Dubai Airport Free Zone");
                cell.setCellStyle(bordered);
                cell = row.createCell(6);
                cell.setCellValue("Dubai");
                drawBorderToRegion(daSheet, rowNum, rowNum, 6, 7);
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 6, 7));
                rowNum++;
                row = daSheet.createRow(rowNum);
                cell = row.createCell(1);
                cell.setCellValue("B/E Ref Nos : " + shippingData.getShippingLabel());
                cell.setCellStyle(bordered);
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 2, 1, 4));

                cell = row.createCell(5);
                cell.setCellValue("Carrier’s Agent");
                cell.setCellStyle(bordered);
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 5, 7));
                drawBorderToRegion(daSheet, rowNum, rowNum, 5, 7);
                rowNum++;
                row = daSheet.createRow(rowNum);
                cell = row.createCell(5);
                cell.setCellValue("County Origin");
                cell.setCellStyle(reportTitleStyle);
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 5, 6));
                cell = row.createCell(7);
                cell.setCellValue("Value");
                cell.setCellStyle(reportTitleStyle);
                rowNum++;

                row = daSheet.createRow(rowNum);
                cell = row.createCell(5);
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 5, 6));
                cell.setCellValue(countyOrigin.toString());
                cell.setCellStyle(bordered);
                cell = row.createCell(7);
                cell.setCellValue(invoice != null && invoice.getTotal() != null ? invoice.getCurrencyName() + decimalFormat.format(invoice.getTotal()) : "");
                cell.setCellStyle(bordered);
                drawBorderToRegion(daSheet, rowNum - 2, rowNum, 1, 4);
                rowNum++;


                row = daSheet.createRow(rowNum++);
                row.setHeightInPoints(20);
                createAndWriteToCell(daSheet, row, 1, "Customer Bill Type            Mark Where appropriate", boldLeftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 4);
                row = daSheet.createRow(rowNum++);
                createRectangle(row.getRowNum(), 1, 0);
                createRectangle(row.getRowNum(), 4, 725);
                row.setHeightInPoints(20);
                createAndWriteToCell(daSheet, row, 1, "      Import                                Import for Re-Export", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 4);
                row = daSheet.createRow(rowNum++);
                createRectangle(row.getRowNum(), 1, 0);
                createRectangle(row.getRowNum(), 4, 725);
                row.setHeightInPoints(20);
                createAndWriteToCell(daSheet, row, 1, "      Temporary Exit         Free Zone Internal Transfer", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 4);
                row = daSheet.createRow(rowNum++);
                createRectangle(row.getRowNum(), 1, 0);
                createRectangle(row.getRowNum(), 4, 725);
                row.setHeightInPoints(20);
                createAndWriteToCell(daSheet, row, 1, "      Export                                     FTZ Bill of Entry", leftAlignment, true, false, row.getRowNum(), row.getRowNum(), 1, 4);

                row.setHeightInPoints(20);
                createAndWriteToCell(daSheet, row, 5, "Licensee/Agent Stamp & Signature.", leftAlignment, true, true, row.getRowNum(), row.getRowNum(), 5, 7);

//                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 1, 1, 4));
                HSSFRow row1 = daSheet.getRow(rowNum - 4);
                createAndWriteToCell(daSheet, row1, 5, "I/We Declare the details given herein to be true and complete.", leftAlignment, true, true, row1.getRowNum(), row1.getRowNum() + 2, 5, 7);

//                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 5, 7));
                drawBorderToRegion(daSheet, rowNum - 4, rowNum, 1, 4);
                drawBorderToRegion(daSheet, rowNum, rowNum, 5, 7);

                row = daSheet.createRow(rowNum);
                row.setHeightInPoints(36);
                cell = row.createCell(1);
                cell.setCellValue("For Customs Use");
                cell.setCellStyle(boldCenter);
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum + 1, 1, 4));
                cell = row.createCell(5);
                cell.setCellValue("I/We Declare the details given herein to be true and complete.");
                cell.setCellStyle(bordered);
                daSheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 5, 7));
                drawBorderToRegion(daSheet, rowNum, rowNum, 5, 7);
                rowNum++;
                row = daSheet.createRow(rowNum);
                row.setHeightInPoints(72);
                createAndWriteToCell(daSheet, row, 5, "Importer’s Stamp & Signature", bordered, true, true, rowNum, rowNum, 5, 7);
                drawBorderToRegion(daSheet, rowNum - 1, rowNum, 1, 4);

                daSheet.setColumnWidth(1, 12 * 256);
                daSheet.setColumnWidth(2, 12 * 256);
                daSheet.setColumnWidth(3, 12 * 256);
                daSheet.setColumnWidth(4, 12 * 256);
                daSheet.setColumnWidth(5, 12 * 256);
                daSheet.setColumnWidth(6, 12 * 256);
                daSheet.setColumnWidth(7, 12 * 256);
                daSheet.setColumnWidth(8, 3 * 256);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            logger.error("Cannot generate " + filename + " excel report, exception: " + exp);
        }
        return workbook;
    }

    private void createRectangle(int rowNum, int colNum, int start) {
        HSSFPatriarch patriarch = (HSSFPatriarch) daSheet.createDrawingPatriarch();
        HSSFClientAnchor a = new HSSFClientAnchor(start, 5, start + 270, 200, (short) colNum, rowNum, (short) colNum, rowNum);
        HSSFSimpleShape shape1 = patriarch.createSimpleShape(a);
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_RECTANGLE);
        shape1.setLineStyleColor(0, 0, 0);
        shape1.setFillColor(255, 255, 255);
        shape1.setLineWidth(HSSFShape.LINEWIDTH_ONE_PT * 2);
        shape1.setLineStyle(HSSFShape.LINESTYLE_SOLID);
    }

    private HSSFRow createAndWriteToCell(HSSFSheet sheet, HSSFRow row, int colNum, String data, HSSFCellStyle style, boolean merged, boolean bordered, int firstRow, int lastRow, int firstCol, int lastCol) {
        return createAndWriteToCell(sheet, row, colNum, data, style, merged, bordered, firstRow, lastRow, firstCol, lastCol, null, CellStyle.VERTICAL_TOP);
    }

    private HSSFRow createAndWriteToCell(HSSFSheet sheet, HSSFRow row, int colNum, String data, HSSFCellStyle style, boolean merged, boolean bordered, int firstRow, int lastRow, int firstCol, int lastCol, Integer rowHeight, short verticalAlignment) {
        HSSFCell cell = row.createCell(colNum);
        cell.setCellValue(data);
        cell.setCellStyle(style);
        CellRangeAddress cellRangeAddress = null;
        if (merged || bordered) {
            cellRangeAddress = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        }
        if (merged) {
            sheet.addMergedRegion(cellRangeAddress);
        }
        if (bordered) {
            drawBorderToRegion(sheet, cellRangeAddress);
        }
        if (rowHeight != null) {
            row.setHeight(rowHeight.shortValue());
        }
        cell.getCellStyle().setVerticalAlignment(verticalAlignment);
        return row;
    }

    private int drawHeader(EdsCompany company, HSSFSheet sheet, int firstCol, int lastCol) {
        sheet.setDefaultColumnWidth(15);
        sheet.setColumnWidth(0, 3 * 256);
        sheet.addMergedRegion(new CellRangeAddress(0, 4, 1, 3));
        getCompanyLogo(sheet, company);
        int rowNum = 0;
        rowNum = createAddressCells(sheet, boldRight, rowNum, firstCol, lastCol, company.getName(), "Office # I-02,", "Dubai Airport Free Zone", "Tel: 04 2525 325", "Fax:04 2525 645");
        return rowNum;
    }

    private int createAddressCells(HSSFSheet sheet, HSSFCellStyle style, int rowNum, int firstCol, int lastCol,
                                   String companyName, String address1, String address2, String phone, String fax) {
        HSSFRow row = sheet.createRow(rowNum);
        createAndWriteToCell(sheet, row, firstCol, companyName != null ? companyName : "", style, true, false, rowNum, rowNum, firstCol, lastCol);
        rowNum++;

        row = sheet.createRow(rowNum);
        createAndWriteToCell(sheet, row, firstCol, address1 != null && !address1.isEmpty() ? address1 : "", style, true, false, rowNum, rowNum, firstCol, lastCol);
        rowNum++;

        row = sheet.createRow(rowNum);
        createAndWriteToCell(sheet, row, firstCol, address2 != null && !address2.isEmpty() ? address2 : "", style, true, false, rowNum, rowNum, firstCol, lastCol);
        rowNum++;

        row = sheet.createRow(rowNum);
        createAndWriteToCell(sheet, row, firstCol, "Tel: " + phone != null && !phone.isEmpty() ? phone : "", style, true, false, rowNum, rowNum, firstCol, lastCol);
        rowNum++;

        row = sheet.createRow(rowNum);
        createAndWriteToCell(sheet, row, firstCol, "Fax: " + fax != null && !fax.isEmpty() ? fax : "", style, true, false, rowNum, rowNum, firstCol, lastCol);
        rowNum++;
        return rowNum;
    }

    private HSSFCellStyle getCellStyle(HSSFFont font, short alignment, boolean hasBackgroundColor, boolean hasBorder, boolean isBold) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        if (isBold) {
            cellStyle.setFont(font);
        }
        cellStyle.setAlignment(alignment);
        if (hasBackgroundColor) {
            cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        if (hasBorder) {
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        }
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    private void drawBorderToRegion(HSSFSheet sheet, int fromRow, int toRow, int fromCol, int toCol) {
        drawBorderToRegion(sheet, new CellRangeAddress(fromRow, toRow, fromCol, toCol));
    }

    private void drawBorderToRegion(HSSFSheet sheet, CellRangeAddress cellRangeAddress) {
        HSSFRegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
        HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
        HSSFRegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
        HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, cellRangeAddress, sheet, workbook);
    }

    private void getCompanyLogo(HSSFSheet sheet, EdsCompany company) {
        try {
            String companyLogoURL = companyAttachmentManager.getCompanyLogoUrl(company, CommandConstants.FOR_INVOICEPDF, Constants.IMAGE_SIZE_SMALL);
            if (StringUtils.isNotBlank(companyLogoURL)) {
                InputStream is = new URL(companyLogoURL).openStream();
                byte[] bytes = IOUtils.toByteArray(is);
                int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                is.close();

                CreationHelper helper = workbook.getCreationHelper();
                Drawing drawingPatriarch = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();

                anchor.setRow1(1);
                anchor.setCol1(1);
                anchor.setRow2(5);
                anchor.setCol2(3);
                Picture pict = drawingPatriarch.createPicture(anchor, pictureIndex);
                pict.resize(1.5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
