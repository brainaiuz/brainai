package com.edatasite.workforce.gwt.accounting.server.app.itemserials;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsItemSerial;
import com.edatasite.workforce.core.domain.accounting.EdsItemSerialDetail;
import com.edatasite.workforce.core.domain.accounting.EdsProductWarehouseLocation;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.accounting.client.rpc.itemserials.ItemSerialService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteItemManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemBatchManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemSerialManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.invoice.client.rpc.ImportSerialsBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialDetailItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("itemSerialService")
public class ItemSerialServiceImpl implements ItemSerialService, ItemSerialServiceLocal {

    @Autowired
    public UploadManager uploadManager;
    @Autowired
    protected ItemBatchManager itemBatchManager;
    @Autowired
    private ItemSerialManager itemSerialManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private QuoteItemManager quoteItemManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private AttachmentManager attachmentsManager;
    @Autowired
    private DocumentsService documentsService;

    public ListResult<SerialItem> getAllSerials(ListingFilterParameter fp) {
        List<SerialItem> result = itemSerialManager.getList(fp);
        return new ListResult<>((ArrayList<SerialItem>) result, itemSerialManager.getTotalCount(fp));
    }

    public SelectItem[] getAvailableSerials(ListingFilterParameter fp) {
        if (fp.getWarehouseID() == null) {
            EdsWarehouse warehouse = warehouseManager.getDefaultWarehouse();
            fp.setWarehouseID(warehouse.getObjectID());
        }
        return itemSerialManager.getAvailableSerials(fp).toArray(new SelectItem[]{});
    }

    public ArrayList<String> serialNumberExists(Integer productId, ArrayList<String> serials) {
        return itemSerialManager.getExistingSerials(productId, serials);
    }

    public ArrayList<String> getSerials(Integer entityId, ItemSerialEntityType entityType) {
        return itemSerialManager.getSerials(entityId, entityType.name()).stream().map(EdsItemSerial::getSerial).collect(Collectors.toCollection(ArrayList::new));
    }

    public SerialItem getSerial(Integer id) {
        SerialItem result = new SerialItem();
        EdsItemSerial itemSerial = itemSerialManager.get(id);

        if (itemSerial == null) {
            return result;
        }

        result.setId(itemSerial.getObjectID());
        result.setNumber(itemSerial.getSerial());
        result.setUsed(itemSerial.getUsed());
        ArrayList<SerialDetailItem> items = new ArrayList<>();
        for (EdsItemSerialDetail detail : itemSerial.getSerialDetails()) {
            SerialDetailItem detailItem = new SerialDetailItem();
            detailItem.setId(detail.getObjectID());
            EdsTransaction transaction = transactionManager.get(detail.getTransactionId());

            String transactionLink = "";
            String transactionNumber = "";
            String transactionType = "";
            if (ItemSerialEntityType.SALES_INVOICE.name().equals(detail.getEntityType())) {
                transactionLink = "saleinvoice/" + transaction.getKeyId();
                transactionNumber = invoiceManager.get(transaction.getKeyId()).getNumber();
                transactionType = detail.getReversed() ? "IN" : "OUT";
            } else if (ItemSerialEntityType.PURCHASE_INVOICE.name().equals(detail.getEntityType())) {
                transactionLink = "purchaseinvoice/" + transaction.getKeyId();
                transactionNumber = invoiceManager.get(transaction.getKeyId()).getNumber();
                transactionType = detail.getReversed() ? "OUT" : "IN";
            } else if (ItemSerialEntityType.GOODS_DELIVERED.name().equals(detail.getEntityType())) {
                transactionLink = "gdn|summary/" + transaction.getKeyId();
                transactionNumber = shippingDataManager.get(transaction.getKeyId()).getNumber();
                transactionType = "OUT";
            } else if (ItemSerialEntityType.GOODS_RECEIVED.name().equals(detail.getEntityType())) {
                transactionLink = "grn|summary/" + transaction.getKeyId();
                transactionNumber = shippingDataManager.get(transaction.getKeyId()).getNumber();
                transactionType = "IN";
            } else if (ItemSerialEntityType.CREDIT_NOTE.name().equals(detail.getEntityType())) {
                transactionLink = "receivablecreditnote/" + transaction.getKeyId();
                transactionNumber = invoiceManager.get(transaction.getKeyId()).getNumber();
                transactionType = detail.getReversed() ? "OUT" : "IN";
            } else if (ItemSerialEntityType.DEBIT_NOTE.name().equals(detail.getEntityType())) {
                transactionLink = "payablecreditnote/" + transaction.getKeyId();
                transactionNumber = invoiceManager.get(transaction.getKeyId()).getNumber();
                transactionType = detail.getReversed() ? "IN" : "OUT";
            } else if (ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name().equals(detail.getEntityType())) {
                transactionLink = "stockadjustment|summary/" + transaction.getKeyId();
                transactionNumber = stockAdjustmentManager.get(transaction.getKeyId()).getNumber();
                transactionType = "IN";
            } else if (ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name().equals(detail.getEntityType())) {
                transactionLink = "stockadjustment|summary/" + transaction.getKeyId();
                transactionNumber = stockAdjustmentManager.get(transaction.getKeyId()).getNumber();
                transactionType = "OUT";
            } else if (ItemSerialEntityType.OPENING_BALANCE.name().equals(detail.getEntityType())) {
                transactionNumber = "Opening Balance";
                transactionType = "IN";
            }
            detailItem.setEntityType(detail.getEntityType());
            detailItem.setTransactionLink(transactionLink);
            detailItem.setTransactionNumber(transactionNumber);
            detailItem.setTransactionDate(new DateNonConvertable(transaction.getJournalDate()));
            detailItem.setTransactionType(transactionType);
            detailItem.setReversed(detail.getReversed());

            items.add(detailItem);
        }
        items.sort(Comparator.comparing(SerialDetailItem::getId));
        result.setItems(items);
        return result;
    }


    @Override
    public Map<Integer, ImportSerialsBatchItem> importSerials(Integer productId, FileItem[] attachedFiles, ArrayList<FileResource> attachments) {
        Map<Integer, ImportSerialsBatchItem> importedSerialsMap = new HashMap<>();

        if (attachments.isEmpty() || attachments.get(0).getAmazonLink().isBlank() || attachments.size() > 1) {
            return importedSerialsMap;
        }
        FileResource fileResource = attachments.get(0);
        String fileURL = fileResource.getAmazonLink();

        try {
            URL url = new URL(fileURL);
            URLConnection conn = url.openConnection();
            InputStream inputStream = conn.getInputStream();

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                Cell productNumberCell = row.getCell(0);
                Cell serialNumberCell = row.getCell(1);
                Cell quantityCell = row.getCell(2);
                if (productNumberCell == null || serialNumberCell == null || quantityCell == null) {
                    continue;
                }
                productNumberCell.setCellType(Cell.CELL_TYPE_STRING);
                serialNumberCell.setCellType(Cell.CELL_TYPE_STRING);

                // Check if any cell is null or empty, skip the row if true
                if (productNumberCell.getStringCellValue().isBlank() ||
                        serialNumberCell.getStringCellValue().isBlank() ||
                        quantityCell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                    continue;
                }

                String numberOrName = productNumberCell.getStringCellValue().trim();

                String serialNumber = serialNumberCell.getStringCellValue();
                double quantity = quantityCell.getNumericCellValue();

                EdsItem item = null;
                item = itemManager.getItemByNumber(numberOrName);
                if (item == null) {
                    item = itemManager.getItemByName(numberOrName);
                }

                if (item != null) {
                    // Create a new ProductTrackBatchItem
                    ProductTrackBatchItem serialItem = new ProductTrackBatchItem();
                    serialItem.setSerial(serialNumber);
                    serialItem.setQty(BigDecimal.valueOf(quantity));

                    // Get or create the ImportSerialsBatchItem for the current productNumber
                    ImportSerialsBatchItem importSerialsBatchItem = importedSerialsMap.getOrDefault(item.getObjectID(), new ImportSerialsBatchItem());
                    importSerialsBatchItem.setProductId(item.getObjectID());
                    importSerialsBatchItem.setName(item.getName());

                    // Add the ProductTrackBatchItem to the list of serials in the ImportSerialsBatchItem
                    importSerialsBatchItem.getSerials().add(serialItem);

                    // Put the ImportSerialsBatchItem back into the map
                    importedSerialsMap.put(item.getObjectID(), importSerialsBatchItem);
                }
            }

            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            documentsService.deleteFile(fileResource.getObjectId(), fileResource.getFolderId(), Constants.F_EVENT);
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InsufficientPermissionsException e) {
            throw new RuntimeException(e);
        }
        return importedSerialsMap;
    }


    public void createForOpeningBalance(EdsProductWarehouseLocation location, Integer transactionId) {
        for (String serial : location.getItemSerials()) {
            EdsItemSerial itemSerial = itemSerialManager.getSerial(location.getProduct().getObjectID(), serial);
            if (itemSerial == null) {
                itemSerial = new EdsItemSerial();
            }

            itemSerial.setItem(location.getProduct());
            itemSerial.setSerial(serial);
            itemSerial.setWarehouse(location.getWarehouse());
            itemSerialManager.createOrUpdate(itemSerial);

            EdsItemSerialDetail detail = new EdsItemSerialDetail();
            detail.setSerial(itemSerial);
            detail.setEntityId(location.getObjectID());
            detail.setEntityType(ItemSerialEntityType.OPENING_BALANCE.name());
            detail.setTransactionId(transactionId);

            itemSerial.getSerialDetails().add(detail);
            itemSerialManager.update(itemSerial);
        }
    }

    public void createForStockAdjustment(EdsAdjustmentItem adjustmentItem, Integer transactionId) {
        if (adjustmentItem.getSerials() == null || adjustmentItem.getSerials().size() == 0) {
            return;
        }

        List<EdsItemSerialDetail> oldSerialDetails = itemSerialManager.getSerialDetails(adjustmentItem.getObjectID(), ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name());
        if (oldSerialDetails != null) {
            for (EdsItemSerialDetail oldSerialDetail : oldSerialDetails) {
                deleteSerialDetail(oldSerialDetail);
            }
        }

        for (String serial : adjustmentItem.getSerials()) {
            EdsItemSerial itemSerial = itemSerialManager.getSerial(adjustmentItem.getItem().getObjectID(), serial);
            if (itemSerial == null) {
                itemSerial = new EdsItemSerial();
            }

            itemSerial.setItem(adjustmentItem.getItem());
            itemSerial.setSerial(serial);
            itemSerial.setWarehouse(adjustmentItem.getWarehouse());
            itemSerialManager.createOrUpdate(itemSerial);

            EdsItemSerialDetail detail = new EdsItemSerialDetail();
            detail.setSerial(itemSerial);
            detail.setEntityId(adjustmentItem.getObjectID());
            detail.setEntityType(ItemSerialEntityType.STOCK_ADJUSTMENT_IN.name());
            detail.setTransactionId(transactionId);

            itemSerial.getSerialDetails().add(detail);
            itemSerialManager.update(itemSerial);
        }
    }

    public void createForGoodsReceived(EdsShippingDataItem shippingDataItem, Integer transactionId) {
        List<EdsItemSerialDetail> oldSerialDetails = itemSerialManager.getSerialDetails(shippingDataItem.getObjectID(), ItemSerialEntityType.GOODS_RECEIVED.name());
        if (oldSerialDetails != null) {
            for (EdsItemSerialDetail oldSerialDetail : oldSerialDetails) {
                deleteSerialDetail(oldSerialDetail);
            }
        }

        for (String serial : shippingDataItem.getSerials()) {
            EdsQuoteItem quoteItem = quoteItemManager.get(shippingDataItem.getQuoteItemId());
            EdsItemSerial itemSerial = itemSerialManager.getSerial(quoteItem.getItem().getObjectID(), serial);
            if (itemSerial == null) {
                itemSerial = new EdsItemSerial();
            }

            itemSerial.setItem(quoteItem.getItem());
            itemSerial.setSerial(serial);
            itemSerial.setWarehouse(quoteItem.getWarehouse());
            itemSerialManager.createOrUpdate(itemSerial);

            EdsItemSerialDetail detail = new EdsItemSerialDetail();
            detail.setSerial(itemSerial);
            detail.setEntityId(shippingDataItem.getObjectID());
            detail.setEntityType(ItemSerialEntityType.GOODS_RECEIVED.name());
            detail.setTransactionId(transactionId);

            itemSerial.getSerialDetails().add(detail);
            itemSerialManager.update(itemSerial);
        }
    }

    public void createForPurchaseInvoice(EdsBaseInvoiceItem invoiceItem, Integer transactionId) {
        EdsItem item = invoiceItem.getItem();

        List<EdsItemSerialDetail> oldSerialDetails = itemSerialManager.getSerialDetails(invoiceItem.getObjectID(), ItemSerialEntityType.PURCHASE_INVOICE.name());
        if (oldSerialDetails != null) {
            for (EdsItemSerialDetail oldSerialDetail : oldSerialDetails) {
                deleteSerialDetail(oldSerialDetail);
            }
        }
        EdsWarehouse warehouse = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse() : warehouseManager.getDefaultWarehouse();

        for (String serial : invoiceItem.getSerials()) {
            EdsItemSerial itemSerial = itemSerialManager.getSerial(item.getObjectID(), serial);
            if (itemSerial == null) {
                itemSerial = new EdsItemSerial();
            }

            itemSerial.setItem(item);
            itemSerial.setSerial(serial);
            itemSerial.setWarehouse(warehouse);
            itemSerialManager.createOrUpdate(itemSerial);

            EdsItemSerialDetail detail = itemSerialManager.getSerialDetail(serial, invoiceItem.getObjectID(), ItemSerialEntityType.PURCHASE_INVOICE.name());
            if (detail == null) {
                detail = new EdsItemSerialDetail();
            }

            detail.setSerial(itemSerial);
            detail.setEntityId(invoiceItem.getObjectID());
            detail.setEntityType(ItemSerialEntityType.PURCHASE_INVOICE.name());
            detail.setTransactionId(transactionId);

            itemSerial.getSerialDetails().add(detail);
            itemSerialManager.update(itemSerial);
        }
    }

    public void assignForStockAdjustment(EdsAdjustmentItem adjustmentItem, Integer transactionId) {
        if (adjustmentItem.getAssignedSerials() == null || adjustmentItem.getAssignedSerials().size() == 0) {
            return;
        }

        List<EdsItemSerialDetail> oldSerialDetails = itemSerialManager.getSerialDetails(adjustmentItem.getObjectID(), ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name());
        if (oldSerialDetails != null) {
            for (EdsItemSerialDetail oldSerialDetail : oldSerialDetails) {
                deleteSerialDetail(oldSerialDetail);
            }
        }

        for (String serial : adjustmentItem.getAssignedSerials()) {
            EdsItemSerial itemSerial = itemSerialManager.getSerial(adjustmentItem.getItem().getObjectID(), serial);
            if (itemSerial != null) {
                EdsItemSerialDetail detail = new EdsItemSerialDetail();
                detail.setSerial(itemSerial);
                detail.setEntityId(adjustmentItem.getObjectID());
                detail.setEntityType(ItemSerialEntityType.STOCK_ADJUSTMENT_OUT.name());
                detail.setTransactionId(transactionId);

                itemSerial.getSerialDetails().add(detail);
                itemSerial.setUsed(true);
                itemSerialManager.update(itemSerial);
            }
        }
    }

    public void assignForGoodsDelivered(EdsShippingDataItem shippingDataItem, Integer transactionId) {
        List<EdsItemSerialDetail> oldSerialDetails = itemSerialManager.getSerialDetails(shippingDataItem.getObjectID(), ItemSerialEntityType.GOODS_DELIVERED.name());
        if (oldSerialDetails != null) {
            for (EdsItemSerialDetail oldSerialDetail : oldSerialDetails) {
                deleteSerialDetail(oldSerialDetail);
            }
        }

        for (String serial : shippingDataItem.getSerials()) {
            EdsQuoteItem quoteItem = quoteItemManager.get(shippingDataItem.getQuoteItemId());
            EdsItemSerial itemSerial = itemSerialManager.getSerial(quoteItem.getItem().getObjectID(), serial);
            if (itemSerial != null) {
                EdsItemSerialDetail detail = new EdsItemSerialDetail();
                detail.setSerial(itemSerial);
                detail.setEntityId(shippingDataItem.getObjectID());
                detail.setEntityType(ItemSerialEntityType.GOODS_DELIVERED.name());
                detail.setTransactionId(transactionId);

                itemSerial.getSerialDetails().add(detail);
                itemSerial.setUsed(true);
                itemSerialManager.update(itemSerial);
            }
        }
    }

    public void assignForSalesInvoice(EdsBaseInvoiceItem invoiceItem, Integer transactionId) {
        EdsItem item = invoiceItem.getItem();

        List<EdsItemSerialDetail> oldSerialDetails = itemSerialManager.getSerialDetails(invoiceItem.getObjectID(), ItemSerialEntityType.SALES_INVOICE.name());
        if (oldSerialDetails != null) {
            for (EdsItemSerialDetail oldSerialDetail : oldSerialDetails) {
                deleteSerialDetail(oldSerialDetail);
            }
        }

        for (String serial : invoiceItem.getSerials()) {
            EdsItemSerial itemSerial = itemSerialManager.getSerial(item.getObjectID(), serial);
            if (itemSerial != null) {
                EdsItemSerialDetail detail = new EdsItemSerialDetail();
                detail.setSerial(itemSerial);
                detail.setEntityId(invoiceItem.getObjectID());
                detail.setEntityType(ItemSerialEntityType.SALES_INVOICE.name());
                detail.setTransactionId(transactionId);

                itemSerial.getSerialDetails().add(detail);
                itemSerial.setUsed(true);
                itemSerialManager.update(itemSerial);
            }
        }
    }

    public void assignForCreditNote(EdsBaseInvoiceItem invoiceItem, Integer transactionId) {
        EdsItem item = invoiceItem.getItem();

        List<EdsItemSerialDetail> oldSerialDetails = itemSerialManager.getSerialDetails(invoiceItem.getObjectID(), ItemSerialEntityType.CREDIT_NOTE.name());
        if (oldSerialDetails != null) {
            for (EdsItemSerialDetail oldSerialDetail : oldSerialDetails) {
                deleteSerialDetail(oldSerialDetail);
            }
        }

        for (String serial : invoiceItem.getSerials()) {
            EdsItemSerial itemSerial = itemSerialManager.getSerial(item.getObjectID(), serial);
            if (itemSerial != null) {
                EdsItemSerialDetail detail = new EdsItemSerialDetail();
                detail.setSerial(itemSerial);
                detail.setEntityId(invoiceItem.getObjectID());
                detail.setEntityType(ItemSerialEntityType.CREDIT_NOTE.name());
                detail.setTransactionId(transactionId);

                itemSerial.getSerialDetails().add(detail);
                itemSerial.setUsed(false);
                itemSerialManager.update(itemSerial);
            }
        }
    }

    public void assignForDebitNote(EdsBaseInvoiceItem invoiceItem, Integer transactionId) {
        EdsItem item = invoiceItem.getItem();

        List<EdsItemSerialDetail> oldSerialDetails = itemSerialManager.getSerialDetails(invoiceItem.getObjectID(), ItemSerialEntityType.DEBIT_NOTE.name());
        if (oldSerialDetails != null) {
            for (EdsItemSerialDetail oldSerialDetail : oldSerialDetails) {
                deleteSerialDetail(oldSerialDetail);
            }
        }

        for (String serial : invoiceItem.getSerials()) {
            EdsItemSerial itemSerial = itemSerialManager.getSerial(item.getObjectID(), serial);
            if (itemSerial != null) {
                EdsItemSerialDetail detail = new EdsItemSerialDetail();
                detail.setSerial(itemSerial);
                detail.setEntityId(invoiceItem.getObjectID());
                detail.setEntityType(ItemSerialEntityType.DEBIT_NOTE.name());
                detail.setTransactionId(transactionId);

                itemSerial.getSerialDetails().add(detail);
                itemSerial.setUsed(true);
                itemSerialManager.update(itemSerial);
            }
        }
    }

    public void voidForInvoice(EdsInvoice invoice, Integer transactionId) {
        String entityType = "";
        boolean used = false;
        if (invoice instanceof EdsSaleInvoice) {
            if (invoice.isCreditNote()) {
                entityType = ItemSerialEntityType.CREDIT_NOTE.name();
                used = true;
            } else {
                entityType = ItemSerialEntityType.SALES_INVOICE.name();
                used = false;
            }
        } else {
            if (invoice.isCreditNote()) {
                entityType = ItemSerialEntityType.DEBIT_NOTE.name();
                used = false;
            } else {
                entityType = ItemSerialEntityType.PURCHASE_INVOICE.name();
                used = true;
            }
        }
        for (EdsBaseInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            EdsItem item = invoiceItem.getItem();
            if (item != null && item.getInventoryTrackingEnabled()) {
                List<EdsItemSerial> itemSerials = itemSerialManager.getSerials(invoiceItem.getObjectID(), entityType);
                if (itemSerials != null && itemSerials.size() > 0) {
                    for (EdsItemSerial itemSerial : itemSerials) {
                        EdsItemSerialDetail detail = new EdsItemSerialDetail();
                        detail.setSerial(itemSerial);
                        detail.setEntityId(invoiceItem.getObjectID());
                        detail.setEntityType(entityType);
                        detail.setTransactionId(transactionId);
                        detail.setReversed(true);

                        itemSerial.getSerialDetails().add(detail);
                        itemSerial.setUsed(used);
                        itemSerialManager.update(itemSerial);
                    }
                }
            }
        }
    }

    public void deleteSerials(Integer itemID) {
        itemSerialManager.deleteSerials(itemID);
    }

    public void deleteForInvoice(EdsInvoice invoice) {
        String entityType = "";
        if (invoice instanceof EdsSaleInvoice) {
            if (invoice.isCreditNote()) {
                entityType = ItemSerialEntityType.CREDIT_NOTE.name();
            } else {
                entityType = ItemSerialEntityType.SALES_INVOICE.name();
            }
        } else {
            if (invoice.isCreditNote()) {
                entityType = ItemSerialEntityType.DEBIT_NOTE.name();
            } else {
                entityType = ItemSerialEntityType.PURCHASE_INVOICE.name();
            }
        }

        for (EdsBaseInvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            EdsItem item = invoiceItem.getItem();
            if (item != null && item.getInventoryTrackingEnabled()) {
                deleteSerialRelation(invoiceItem.getObjectID(), entityType);
            }
            if (item != null && item.getTrackBatchesEnabled()) {
                itemBatchManager.deleteBatchesByEntity(invoice.getObjectID(), item.getObjectID(), entityType);
            }
        }
    }

    public void deleteForInvoice(EdsInvoice invoice, List<Integer> entityIds) {
        if (entityIds == null && entityIds.size() == 0) {
            return;
        }

        String entityType = "";
        if (invoice instanceof EdsSaleInvoice) {
            if (invoice.isCreditNote()) {
                entityType = ItemSerialEntityType.CREDIT_NOTE.name();
            } else {
                entityType = ItemSerialEntityType.SALES_INVOICE.name();
            }
        } else {
            if (invoice.isCreditNote()) {
                entityType = ItemSerialEntityType.DEBIT_NOTE.name();
            } else {
                entityType = ItemSerialEntityType.PURCHASE_INVOICE.name();
            }
        }

        for (Integer entityId : entityIds) {
            deleteSerialRelation(entityId, entityType);
        }
    }

    public void deleteSerialRelation(Integer entityId, String entityType) {
        List<EdsItemSerialDetail> details = itemSerialManager.getSerialDetails(entityId, entityType);
        if (details != null) {
            for (EdsItemSerialDetail detail : details) {
                deleteSerialDetail(detail);
            }
        }
    }

    private void deleteSerialDetail(EdsItemSerialDetail detail) {
        EdsItemSerial itemSerial = detail.getSerial();
        itemSerialManager.deleteSerialDetail(detail.getObjectID());
        if (itemSerial.getSerialDetails().size() == 0) {
            itemSerialManager.delete(itemSerial);
        } else {
            boolean used = false;
            for (EdsItemSerialDetail existingDetails : itemSerial.getSerialDetails()) {
                if (ItemSerialEntityType.SALES_INVOICE.name().equals(existingDetails.getEntityType()) || ItemSerialEntityType.GOODS_DELIVERED.name().equals(existingDetails.getEntityType())) {
                    used = true;
                    break;
                }
            }
            itemSerial.setUsed(used);
            itemSerialManager.update(itemSerial);
        }
    }
}
