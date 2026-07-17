package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customfields.EdsBuildAssemblyCustomFields;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.gwt.accounting.client.rpc.AssemblyBuildItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.BuildAssemblyService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.MultiPriceItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.costofgoods.COGSServices;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.PathFinder;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.BuildAssemblyCFManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.BuildAssemblyEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Transactional
@Service("buildAssemblyService")
public class BuildAssemblyServiceImpl implements BuildAssemblyService, BuildAssemblyServiceLocal, Constants, AccountingConstants {
    private static final Logger log = LoggerFactory.getLogger(BuildAssemblyServiceImpl.class);

    private final WfmMessageSource commonLocalizer;
    private final CommonService commonService;
    private final CommonServiceLocal commonServiceLocal;
    private final AccountingManager accountingManager;
    private final UserManager userManager;
    private final EmployeeManager employeeManager;
    private final ApproverManager approverManager;
    private final ReferenceManager referenceManager;
    private final NumberingSettingsManager numberingSettingsManager;
    private final FinancialSettingsManager financialSettingsManager;
    private final ItemManager itemManager;
    private final ItemStockManager itemStockManager;
    private final WarehouseManager warehouseManager;
    private final TransactionManager transactionManager;
    private final ProductsServicesSolrComponent productsServicesSolrComponent;
    private final COGSServices cogsServices;
    private final BaseEventsPostProcessor baseEventPostProcessor;
    private final CompanyPdfTemplateManager companyPdfTemplateManager;
    private final AssemblyHistoryManager assemblyHistoryManager;
    private final BuildAssemblyNoteManager buildAssemblyNoteManager;
    private final SavedAssemblyItemManager savedAssemblyItemManager;
    private final AssemblyItemItemsManager assemblyItemItemsManager;
    private final AssemblyBuildHistoryItemManager assemblyBuildHistoryItemManager;
    private final BuildAssemblyCFManager buildAssemblyCFManager;
    private final InvoiceService invoiceService;

    @Autowired
    public BuildAssemblyServiceImpl(@Qualifier("commonLocalizer") WfmMessageSource commonLocalizer, @Qualifier("commonService") CommonServiceLocal commonServiceLocal, CommonService commonService, AccountingManager accountingManager, UserManager userManager, EmployeeManager employeeManager, ApproverManager approverManager, ReferenceManager referenceManager, NumberingSettingsManager numberingSettingsManager, FinancialSettingsManager financialSettingsManager, ItemManager itemManager, ItemStockManager itemStockManager, WarehouseManager warehouseManager, TransactionManager transactionManager, ProductsServicesSolrComponent productsServicesSolrComponent, COGSServices cogsServices, BaseEventsPostProcessor baseEventPostProcessor, CompanyPdfTemplateManager companyPdfTemplateManager, AssemblyHistoryManager assemblyHistoryManager, AssemblyBuildHistoryItemManager assemblyBuildHistoryItemManager, SavedAssemblyItemManager savedAssemblyItemManager, AssemblyItemItemsManager assemblyItemItemsManager, BuildAssemblyCFManager buildAssemblyCFManager, BuildAssemblyNoteManager buildAssemblyNoteManager, InvoiceService invoiceService) {
        this.commonLocalizer = commonLocalizer;
        this.commonServiceLocal = commonServiceLocal;
        this.commonService = commonService;
        this.accountingManager = accountingManager;
        this.userManager = userManager;
        this.employeeManager = employeeManager;
        this.approverManager = approverManager;
        this.referenceManager = referenceManager;
        this.numberingSettingsManager = numberingSettingsManager;
        this.financialSettingsManager = financialSettingsManager;
        this.itemManager = itemManager;
        this.itemStockManager = itemStockManager;
        this.warehouseManager = warehouseManager;
        this.transactionManager = transactionManager;
        this.productsServicesSolrComponent = productsServicesSolrComponent;
        this.cogsServices = cogsServices;
        this.baseEventPostProcessor = baseEventPostProcessor;
        this.companyPdfTemplateManager = companyPdfTemplateManager;
        this.assemblyHistoryManager = assemblyHistoryManager;
        this.buildAssemblyNoteManager = buildAssemblyNoteManager;
        this.savedAssemblyItemManager = savedAssemblyItemManager;
        this.assemblyItemItemsManager = assemblyItemItemsManager;
        this.assemblyBuildHistoryItemManager = assemblyBuildHistoryItemManager;
        this.buildAssemblyCFManager = buildAssemblyCFManager;
        this.invoiceService = invoiceService;
    }

    @Override
    public ListResult<AssemblyItem> getBuildAssemblyList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        ListPanelToolRpc panelSettings = fp.getListPanelTool();
        List<EdsSavedAssemblyItem> list = savedAssemblyItemManager.getList(fp);
        ArrayList<AssemblyItem> items = new ArrayList<>();
        for (EdsSavedAssemblyItem edsSavedAssemblyItem : list) {
            AssemblyItem rpc = edsSavedAssemblyItem.getRpc();
            if (panelSettings != null) {
                HashMap<String, Object> map = CustomFieldsUtils.getRPCCustomFields(edsSavedAssemblyItem.getCustomFields(), panelSettings.getColumnCodeName());
                rpc.setCustomFieldValuesItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
            }
            items.add(rpc);
        }

        Integer totalCount = savedAssemblyItemManager.getTotalCount(fp);
        return new ListResult<>(items, totalCount);
    }

    @Override
    public AssemblyItem getBuildAssemblyItem(Integer id) {
        EdsSavedAssemblyItem savedAssembly = savedAssemblyItemManager.get(id);
        AssemblyItem item = new AssemblyItem();
        if (savedAssembly == null) {
            item.setNumberData(generateAssemblyNumber());
        } else {
            item = savedAssembly.getRpc();
            item.setWareHouseItem(warehouseManager.get(savedAssembly.getWarehouseID()) != null ? warehouseManager.get(savedAssembly.getWarehouseID()).getAsSelectItem() : null);
            item.setAssemblyItem(itemManager.get(savedAssembly.getItemId()) != null ? itemManager.get(savedAssembly.getItemId()).getAsSelectItem() : null);
            item.setTemplates(getPdfTemplates(PdfReferenceCodeNameEnum.BUILD_ASSEMBLY.name()).getItems());

            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.BuildAssembly);
            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(savedAssembly.getCustomFields(), customFieldsItems));
            NewProduct newProduct = new NewProduct();
            ArrayList<AssemblyItem> assemblyItems = new ArrayList<>();
            for (EdsAssemblyItemItems assemblyItemItems : savedAssembly.getItemTables()) {
                AssemblyItem assemblyItem = new AssemblyItem();
                EdsItem edsItem = itemManager.get(assemblyItemItems.getItemId());
                assemblyItem.setAssemblyItemId(savedAssembly.getItemId());
                assemblyItem.setProduct(edsItem.getAsSelectItem());
                assemblyItem.setProductType(edsItem.getProductType());
                assemblyItem.setCategory(edsItem.getCategory() != null ? edsItem.getCategory().getName() : null);
                assemblyItem.setItemsInStock(edsItem.getQty());
                assemblyItem.setQuantity(assemblyItemItems.getQuantity());
                assemblyItem.setDescription(assemblyItemItems.getDescription());
                assemblyItem.setProductDefaultWarehouse(warehouseManager.get(assemblyItemItems.getWarehouseID()) != null ?
                        warehouseManager.get(assemblyItemItems.getWarehouseID()).getAsSelectItem() : null);
                assemblyItems.add(assemblyItem);
            }
            newProduct.setAssemblyItems(assemblyItems);
            item.setNewProduct(newProduct);
        }
        item.setApproveProcessEnabled(approverManager.isExistApproverByEntityType(RelationItem.TYPE_BUILD_ASSEMBLY));
        return item;
    }

    @Override
    @Transactional
    public Integer buildAssemblyItem(AssemblyItem assemblyItem) {
        EdsSavedAssemblyItem edsSavedAssemblyItem = savedAssemblyItemManager.get(
                assemblyItem.getId()) != null ? savedAssemblyItemManager.get(assemblyItem.getId()) : new EdsSavedAssemblyItem();
        boolean isEdit = edsSavedAssemblyItem.getObjectID() != null;

        fillBaseSavedAssemblyData(edsSavedAssemblyItem, assemblyItem);
        setAssemblyItems(edsSavedAssemblyItem, assemblyItem);
        setAuditFields(edsSavedAssemblyItem, assemblyItem);
        savedAssemblyItemManager.createOrUpdate(edsSavedAssemblyItem);

        createSavedAssemblyCustomFields(edsSavedAssemblyItem, assemblyItem.getCustomFieldItems());
        if (!isOk(assemblyItem.getApprovers())) {
            edsSavedAssemblyItem.setStatus(referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, BUILD_ASSEMBLY_STATUS_APPROVED));
            saveBuildAssemblyHistory(edsSavedAssemblyItem);
            saveBuildAssemblyNote(edsSavedAssemblyItem.getObjectID(), new HistoryListItem("approved"));
        } else {
            processApprovers(edsSavedAssemblyItem, assemblyItem);
        }
        registerEvents(edsSavedAssemblyItem, isEdit);
        return assemblyItem.getProductId();
    }

    @Override
    public void updateStatusBuildAssembly(Integer savedAssemblyId, String statusCode) {
        EdsSavedAssemblyItem savedAssemblyItem = savedAssemblyItemManager.get(savedAssemblyId);

        if (savedAssemblyItem != null) {
            final EdsUser user = this.employeeManager.getUser();
            final EdsReference edsReference = this.referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, statusCode);

            if (!Constants.BUILD_ASSEMBLY_STATUS_APPROVED.equals(edsReference.getCode())) {
                savedAssemblyItem.setOverallStatus(edsReference);
                savedAssemblyItem.setStatus(edsReference);
            }
            savedAssemblyItem.updateStatus(edsReference);
            savedAssemblyItemManager.update(savedAssemblyItem);
            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), savedAssemblyItem, user);
            workflowEvent.setEntityType(RelationItem.TYPE_BUILD_ASSEMBLY);

            if (Constants.BUILD_ASSEMBLY_STATUS_SUBMITTED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(BuildAssemblyEventListenerImpl.TYPE, BuildAssemblyEventListenerImpl.BUILD_ASSEMBLY_STATUS_SUBMITTED, savedAssemblyItem, user);
            } else if (Constants.BUILD_ASSEMBLY_STATUS_APPROVED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(BuildAssemblyEventListenerImpl.TYPE, BuildAssemblyEventListenerImpl.BUILD_ASSEMBLY_STATUS_APPROVED, savedAssemblyItem, user);
            } else if (Constants.BUILD_ASSEMBLY_STATUS_REJECTED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(BuildAssemblyEventListenerImpl.TYPE, BuildAssemblyEventListenerImpl.BUILD_ASSEMBLY_STATUS_REJECTED, savedAssemblyItem, user);
            }
        }
    }

    @Override
    public void unBuildAsseblyItems(ArrayList<AssemblyBuildItem> items) {
        if (items != null && !items.isEmpty()) {
            for (AssemblyBuildItem item : items) {
                EdsAssemblyItemBuildHistory assemblyItemBuildHistory = assemblyHistoryManager.get(item.getObjectID());
                assemblyItemBuildHistory.setDeleted(true);
                updateStatusBuildAssembly(assemblyItemBuildHistory.getSavedAssemblyItemID(), BUILD_ASSEMBLY_STATUS_UNBUILD);
                itemStockManager.deleteItemStocksByTransaction(item.getTransactionID());
                EdsTransaction transaction = transactionManager.get(item.getTransactionID());
                transaction.setDeleted(true);
                List<EdsItem> products = new ArrayList<>();
                products.add(itemManager.get(assemblyItemBuildHistory.getAssemblyItemID()));
                for (EdsAssemblyBuildHistoryItem historyItem : assemblyItemBuildHistory.getItems()) {
                    products.add(itemManager.get(historyItem.getItemId()));
                }
                try {
                    productsServicesSolrComponent.indexes(products);
                } catch (Exception e) {
                    log.error("Error while unBuildAssemblyItems", e);
                }

            }
        }
    }

    @Override
    public ArrayList<AssemblyBuildItem> getAssemblyBuildItems(Integer assemblyID) {
        ArrayList<AssemblyBuildItem> result = new ArrayList<>();
        List<EdsAssemblyItemBuildHistory> buildItems = assemblyHistoryManager.getAssemblyBuildHistory(assemblyID);
        if (buildItems != null && !buildItems.isEmpty()) {
            for (EdsAssemblyItemBuildHistory buildItem : buildItems) {
                AssemblyBuildItem assemblyBuildItem = new AssemblyBuildItem();
                assemblyBuildItem.setObjectID(buildItem.getObjectID());
                assemblyBuildItem.setAssemblyID(assemblyID);
                assemblyBuildItem.setDate(new DateNonConvertable(buildItem.getBuildDate()));
                assemblyBuildItem.setQty(buildItem.getQty());
                assemblyBuildItem.setTransactionID(buildItem.getTransactionID());
                if (buildItem.getWarehouseID() != null) {
                    EdsWarehouse warehouse = warehouseManager.get(buildItem.getWarehouseID());
                    if (warehouse != null) {
                        assemblyBuildItem.setWarehouse(warehouse.getName());
                    }
                }
                result.add(assemblyBuildItem);
            }
        }
        return result;
    }

    @Override
    public NewProduct getProductForBuildAssembly(Integer productId) {
        NewProduct product = new NewProduct();
        product.setLayoutHTML(PathFinder.getLayoutHTML(LayoutRPC.BUILD_ASSEMBLY_FORM));
        if (productId != null) {
            EdsItem item = itemManager.get(productId);
            if (item.getQty() != null) {
                product.setObjectId(productId);
                product.setItemName(item.getName());
                product.setQuantity(item.getQty());
                product.setAssemblyItems(wrapAssemblyItems(item.getAssemblyItems()));
                if (item.getDefaultWarehouse() != null) {
                    product.setDefaultItemWarehouse(item.getDefaultWarehouse().getAsSelectItem());
                }
            }

        }

        return product;
    }

    private ArrayList<AssemblyItem> wrapAssemblyItems(List<EdsAssemblyItem> assemblyItems) {
        ArrayList<AssemblyItem> result = new ArrayList<>();
        if (assemblyItems != null && !assemblyItems.isEmpty()) {
            AssemblyItem item = null;
            for (EdsAssemblyItem assemblyItem : assemblyItems) {
                item = new AssemblyItem();
                EdsItem productItem = assemblyItem.getProductItem();
                if (productItem != null) {
                    if (productItem.getProductNumber() != null && !"".equals(productItem.getProductNumber())) {
                        item.setProduct(new SelectItem(productItem.getObjectID(), productItem.getProductNumber() + " -> " + productItem.getName()));
                    } else {
                        item.setProduct(new SelectItem(productItem.getObjectID(), productItem.getName()));
                    }
                    ArrayList<MultiPriceItem> multiPriceItems = new ArrayList<>();
                    for (EdsItemMultiPrice mulPriceItem : productItem.getMultiPrices()) {
                        if (PAYABLE.equals(mulPriceItem.getType())) {
                            MultiPriceItem multiPriceItem = new MultiPriceItem();
                            multiPriceItem.setCurrency(mulPriceItem.getCurrency() != null ? mulPriceItem.getCurrency().getAsSelectItem() : null);
                            multiPriceItem.setPrice(mulPriceItem.getSellingPrice());
                            multiPriceItems.add(multiPriceItem);
                        }
                    }
                    item.setProductDefaultWarehouse(productItem.getDefaultWarehouse() != null ? productItem.getDefaultWarehouse().getAsSelectItem() : null);
                    item.setMultiPriceItems(multiPriceItems);
                    item.setItemsInStock(productItem.getQty());
                    item.setActive(productItem.isActive());
                    item.setProductPrice(productItem.getUnitPrice());
                    item.setProductSellingPrice(productItem.getSellingPrice());
                    Set<EdsLocation> locations = productItem.getLocations();
                    ArrayList<Integer> locationIds = new ArrayList<>();
                    if (!locations.isEmpty()) {
                        locationIds = locations.stream()
                                .map(EdsLocation::getObjectID)
                                .collect(Collectors.toCollection(ArrayList::new));
                        item.setLocationIds(locationIds);
                    }
                }
                if (assemblyItem.getObjectID() != null) {
                    item.setAssemblyItemId(assemblyItem.getObjectID());
                }

                item.setDescription(assemblyItem.getDescription());
                item.setQuantity(assemblyItem.getQty());
                item.setCostPrice(assemblyItem.getCostPrice());
                item.setSellingPrice(assemblyItem.getItem().getSellingPrice());
                item.setProductType(assemblyItem.getType());
                item.setCategory(productItem.getCategory() != null ? productItem.getCategory().getName() : null);
                result.add(item);
            }
        }

        result.sort(Comparator
                .comparing(AssemblyItem::getCategory, Comparator.nullsLast(String::compareToIgnoreCase))
                .thenComparing(item -> item.getProduct() != null ? item.getProduct().getName() : "", String.CASE_INSENSITIVE_ORDER));

        return result;
    }

    @Override
    @Transactional
    public SelectItem unBuildAssemblyItem(Integer savedAssemblyItemId) {
        EdsSavedAssemblyItem savedAssemblyItem = savedAssemblyItemManager.get(savedAssemblyItemId);
        EdsAssemblyItemBuildHistory buildAssemblyBuildHistory = savedAssemblyItem.getBuildAssemblyBuildHistory();
        SelectItem selectItem = invoiceService.validateStockInconsistencyInUnbuildAssembly(new Integer[]{buildAssemblyBuildHistory.getTransactionID()});
        if (selectItem != null) {
            return selectItem;
        }
        buildAssemblyBuildHistory.setDeleted(true);
        assemblyHistoryManager.update(buildAssemblyBuildHistory);
        updateStatusBuildAssembly(savedAssemblyItemId, BUILD_ASSEMBLY_STATUS_UNBUILD);
        saveBuildAssemblyNote(savedAssemblyItemId, new HistoryListItem("unBuilt"));
        itemStockManager.deleteItemStocksByTransaction(buildAssemblyBuildHistory.getTransactionID());
        EdsTransaction transaction = transactionManager.get(buildAssemblyBuildHistory.getTransactionID());
        transaction.setDeleted(true);
        List<EdsItem> products = new ArrayList<>();
        products.add(itemManager.get(savedAssemblyItem.getItemId()));
        for (EdsAssemblyItemItems assemblyItemItems : savedAssemblyItem.getItemTables()) {
            products.add(itemManager.get(assemblyItemItems.getItemId()));
        }
        try {
            productsServicesSolrComponent.indexes(products);
        } catch (Exception e) {
            log.error("Error while unBuildAssemblyItems", e);
        }
        return null;
    }

    @Override
    public void deleteSavedAssembly(Integer savedAssemblyIds) {
        EdsSavedAssemblyItem savedAssemblyItem = savedAssemblyItemManager.get(savedAssemblyIds);
        if (savedAssemblyItem != null) {
            savedAssemblyItem.setDeleted(true);
            savedAssemblyItemManager.update(savedAssemblyItem);
        }
    }

    @Override
    public void deleteSelectedSavedAssemblyList(ArrayList<Integer> savedAssemblyIds) {
        savedAssemblyIds.forEach(savedAssemblyId -> {
            EdsSavedAssemblyItem savedAssemblyItem = savedAssemblyItemManager.get(savedAssemblyId);
            if (savedAssemblyItem != null) {
                savedAssemblyItem.setDeleted(true);
                savedAssemblyItemManager.update(savedAssemblyItem);
            }
        });
    }

    @Override
    public void reBuildAssemblyItem(AssemblyItem assemblyItem, Integer oldTransactionId) {
        EdsTransaction transaction = transactionManager.get(oldTransactionId);
        transaction.setDeleted(true);
        itemStockManager.deleteItemStocksByTransaction(oldTransactionId);
        buildAssemblyItem(assemblyItem);
    }

    @Override
    public void saveBuildAssemblyHistory(EdsSavedAssemblyItem edsSavedAssemblyItem) {
        EdsAssemblyItemBuildHistory buildAssemblyHistory = new EdsAssemblyItemBuildHistory();
        setValues2History(buildAssemblyHistory, edsSavedAssemblyItem);
        buildAssemblyHistory.setTransactionID(createAssemblyTransaction(edsSavedAssemblyItem));
        edsSavedAssemblyItem.setBuildAssemblyBuildHistory(buildAssemblyHistory);
    }

    private CustomFormItemPdfTemplateList getPdfTemplates(String type) {
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getCompanyPDFTemplatesByType(type, false);
        SelectItem[] items = new SelectItem[templates.size()];
        int i = 0;
        Integer defaultTemplateID = null;
        for (EdsCompanyPdfTemplate t : templates) {
            items[i] = new SelectItem(t.getObjectID(), t.getName());
            if (t.isDefaultTemplate()) {
                defaultTemplateID = t.getObjectID();
            }
            i++;
        }
        return new CustomFormItemPdfTemplateList(items, defaultTemplateID);
    }

    private NumberData generateAssemblyNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = savedAssemblyItemManager.getAssemblyLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getAssemblyNumberingDate() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getAssemblyNumberingDate(), settings.getDelimetrAssemblyNumberingFormat(), null, null, null, "");
            numberData.setDelimiter(settings.getDelimetrAssemblyNumberingFormat());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_ASSEMBLY_PREFIX);
        }
    }

    private void fillBaseSavedAssemblyData(EdsSavedAssemblyItem edsSavedAssemblyItem, AssemblyItem assemblyItem) {
        edsSavedAssemblyItem.setDate(assemblyItem.getDate() != null ? assemblyItem.getDate().getDate() : null);
        edsSavedAssemblyItem.setItemId(assemblyItem.getProductId());
        edsSavedAssemblyItem.setItemName(assemblyItem.getProduct().getName());
        edsSavedAssemblyItem.setWarehouseID(assemblyItem.getWarehouseId());
        edsSavedAssemblyItem.setQuantity(assemblyItem.getQuantity());
        edsSavedAssemblyItem.setAccountId(assemblyItem.getAccount() != null ? assemblyItem.getAccount().getId() : null);

        NumberData numberData = assemblyItem.getNumberData();
        if (savedAssemblyItemManager.isSavedAssemblyItemExist(assemblyItem.getId() == null ? numberData.getNumberString() : edsSavedAssemblyItem.getAssemblyItemCode(), assemblyItem.getId())) {
            numberData = this.generateAssemblyNumber();
        }

        if (numberData != null && numberData.getNumberString() != null && !numberData.getNumberString().isEmpty()) {
            edsSavedAssemblyItem.setIntNumber(numberData.getIntNumber());
            edsSavedAssemblyItem.setAssemblyItemCode(numberData.getNumberString());
        }
    }

    private void setAssemblyItems(EdsSavedAssemblyItem edsSavedAssemblyItem, AssemblyItem assemblyItem) {
        if (assemblyItem.getId() != null) {
            for (EdsAssemblyItemItems item : edsSavedAssemblyItem.getItemTables()) {
                assemblyItemItemsManager.delete(item);
            }
        }

        Set<EdsAssemblyItemItems> itemSet = new HashSet<>();
        for (QuantityItem item : assemblyItem.getItems()) {
            EdsAssemblyItemItems eds = new EdsAssemblyItemItems();
            eds.setItemId(item.getId());
            eds.setWarehouseID(item.getWarehouseID());
            eds.setQuantity(item.getQuantity());
            eds.setDescription(item.getDescription());
            eds.setEdsSavedAssemblyItem(edsSavedAssemblyItem);
            itemSet.add(eds);
        }
        edsSavedAssemblyItem.setItemTables(itemSet);
    }

    private void setAuditFields(EdsSavedAssemblyItem edsSavedAssemblyItem, AssemblyItem assemblyItem) {
        EdsUser user = userManager.getUser();
        if (assemblyItem.getId() == null) {
            edsSavedAssemblyItem.setCreatedDate(new Date());
            edsSavedAssemblyItem.setUpdatedDate(new Date());
            edsSavedAssemblyItem.setCreatorId(user.getObjectID());
            edsSavedAssemblyItem.setUpdaterId(user.getObjectID());
        } else {
            edsSavedAssemblyItem.setUpdatedDate(new Date());
            edsSavedAssemblyItem.setUpdaterId(user.getObjectID());
        }
    }

    private void createSavedAssemblyCustomFields(EdsSavedAssemblyItem edsSavedAssemblyItem, ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems == null || customFieldItems.isEmpty()) return;

        EdsBuildAssemblyCustomFields buildAssemblyCustomFields = null;
        if (customFieldItems.get(0).getObjectId() != null) {
            buildAssemblyCustomFields = buildAssemblyCFManager.get(customFieldItems.get(0).getObjectId());
        } else {
            boolean isEmpty = true;
            for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue())) || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0) || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                return;
            }
            buildAssemblyCustomFields = new EdsBuildAssemblyCustomFields();
            buildAssemblyCFManager.create(buildAssemblyCustomFields);
        }
        CustomFieldsUtils.setDomenObjectCustomFields(buildAssemblyCustomFields, customFieldItems);
        edsSavedAssemblyItem.setCustomFields(buildAssemblyCustomFields);
    }

    private void processApprovers(EdsSavedAssemblyItem edsSavedAssemblyItem, AssemblyItem assemblyItem) {
        if (!isOk(assemblyItem.getApprovers())) return;

        assemblyItem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
        boolean isFirstApprover = true;

        for (ApproverItemMini approverItem : assemblyItem.getApprovers()) {
            EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
            if (approverItem.getObjectID() != null) {
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                    _edsApprover.setExactEmployee(user_);
                }
                approverManager.update(_edsApprover);
                if (edsSavedAssemblyItem.getCurrentApprover() != null && assemblyItem.getStatusCode() != null && isFirstApprover) {
                    edsSavedAssemblyItem.getCurrentApprover().setStatus(referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, assemblyItem.getStatusCode()));
                    edsSavedAssemblyItem.setEntityStatus(referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, Constants.BUILD_ASSEMBLY_STATUS_SUBMITTED));
                    isFirstApprover = false;
                } else if (edsSavedAssemblyItem.getCurrentApprover() != null && assemblyItem.getStatusCode() != null) {
                    edsSavedAssemblyItem.getCurrentApprover().setStatus(referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, Constants.BUILD_ASSEMBLY_STATUS_SUBMITTED));
                }
                if (assemblyItem.getStatusCode() != null && !Constants.BUILD_ASSEMBLY_STATUS_APPROVED.equals(assemblyItem.getStatusCode())) {
                    edsSavedAssemblyItem.setEntityStatus(referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, assemblyItem.getStatusCode()));
                }
                if (edsSavedAssemblyItem.isCurrentApproverRejected()) {
                    edsSavedAssemblyItem.setEntityStatus(edsSavedAssemblyItem.getCurrentApprover().getStatus());
                }
                continue;
            }
            EdsApprover edsApprover = _edsApprover.cloneShallow();
            edsApprover.setObjectID(null);
            edsApprover.setApproverHistory(new HashSet<>());
            edsApprover.setEntityID(edsSavedAssemblyItem.getObjectID());
            edsApprover.setIs_default(false);

            if (assemblyItem.getStatusCode() != null && isFirstApprover) {
                edsApprover.setStatus(referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, assemblyItem.getStatusCode()));
                edsSavedAssemblyItem.setEntityStatus(referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, Constants.BUILD_ASSEMBLY_STATUS_SUBMITTED));
                isFirstApprover = false;
            } else if (assemblyItem.getStatusCode() != null) {
                edsApprover.setStatus(referenceManager.findReference(Constants.BUILD_ASSEMBLY_STATUS, Constants.BUILD_ASSEMBLY_STATUS_SUBMITTED));
            }
            if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                edsApprover.setExactEmployee(user_);
            }
            edsApprover.setApproverRoles(new HashSet<>());
            edsApprover.setApproverEmployees(new HashSet<>());
            edsApprover.setDynamicQueries(new HashSet<>());
            approverManager.createOrUpdate(edsApprover);
            for (EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                edsApprover.getApproverRoles().add(roleapp);
            }
            for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                edsApprover.getApproverEmployees().add(ucerapp);
            }
            if (edsSavedAssemblyItem.getCurrentApprover() == null) {
                edsSavedAssemblyItem.setCurrentApprover(edsApprover);
            }
            edsSavedAssemblyItem.getApprovers().add(edsApprover);
        }
    }

    private void registerEvents(EdsSavedAssemblyItem edsSavedAssemblyItem, boolean isEdit) {
        //Register event in MyUpdate
        EdsUser user = userManager.getUser();
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSavedAssemblyItem.class.getSimpleName());
        kpiLog.setEntityId(edsSavedAssemblyItem.getObjectID());
        EdsBusinessEvent workflowEvent = null;
        if (isEdit) {
            baseEventPostProcessor.registerEvent(BuildAssemblyEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsSavedAssemblyItem, user);
            workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsSavedAssemblyItem, user);
            workflowEvent.setEntityType(RelationItem.TYPE_BUILD_ASSEMBLY);
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update build assembly item");
        } else {
            baseEventPostProcessor.registerEvent(BuildAssemblyEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsSavedAssemblyItem, user);
            workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsSavedAssemblyItem, user);
            workflowEvent.setEntityType(RelationItem.TYPE_BUILD_ASSEMBLY);
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add build assembly item");
        }

        /* Run a workflow approval process */
        EdsBusinessEvent event = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsSavedAssemblyItem, user);
        event.setEntityType(RelationItem.TYPE_BUILD_ASSEMBLY);
    }

    private void setValues2History(EdsAssemblyItemBuildHistory buildAssemblyHistory, EdsSavedAssemblyItem edsSavedAssemblyItem) {
        buildAssemblyHistory.setSavedAssemblyItemID(edsSavedAssemblyItem.getObjectID());
        buildAssemblyHistory.setAssemblyItemID(edsSavedAssemblyItem.getItemId());
        buildAssemblyHistory.setAssemblyItemCode(edsSavedAssemblyItem.getAssemblyItemCode() != null ? edsSavedAssemblyItem.getAssemblyItemCode() : null);
        buildAssemblyHistory.setBuildDate(edsSavedAssemblyItem.getDate() != null ? edsSavedAssemblyItem.getDate() : null);
        buildAssemblyHistory.setQty(edsSavedAssemblyItem.getQuantity());
        buildAssemblyHistory.setWarehouseID(edsSavedAssemblyItem.getWarehouseID());

        if (buildAssemblyHistory.getObjectID() != null) {
            for (EdsAssemblyBuildHistoryItem item : buildAssemblyHistory.getItems()) {
                assemblyBuildHistoryItemManager.delete(item);
            }
        }

        List<EdsAssemblyBuildHistoryItem> itemSet = new ArrayList<>();
        Set<EdsAssemblyItemItems> itemTables = edsSavedAssemblyItem.getItemTables();
        for (EdsAssemblyItemItems item : itemTables) {
            EdsAssemblyBuildHistoryItem eds = new EdsAssemblyBuildHistoryItem();
            eds.setItemId(item.getItemId());
            eds.setWarehouseId(item.getWarehouseID());
            eds.setQty(item.getQuantity());
            eds.setDescription(item.getDescription());
            eds.setHistory(buildAssemblyHistory);
            eds.setLiabilityAccountId(edsSavedAssemblyItem.getAccountId());
            itemSet.add(eds);
        }
        buildAssemblyHistory.setItems(itemSet);
        assemblyHistoryManager.createOrUpdate(buildAssemblyHistory);
    }

    private Integer createAssemblyTransaction(EdsSavedAssemblyItem savedAssembly) {
        EdsItem item = itemManager.get(savedAssembly.getItemId());
        if (item == null) return null;

        EdsUser user = userManager.getUser();
        EdsInventoryTransaction transaction = new EdsInventoryTransaction();
        transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID(user.getCompany()) + 1);

        EdsWarehouse warehouse;
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings.getEnableMultiWarehouse() && savedAssembly.getWarehouseID() != null) {
            warehouse = warehouseManager.get(savedAssembly.getWarehouseID());
        } else {
            warehouse = warehouseManager.getDefaultWarehouse();
            savedAssembly.setWarehouseID(warehouse.getObjectID());
        }

        transaction.setName(item.getProductNumber() + " -> " + item.getName());
        transaction.setInventory(item);
        transaction.setJournalDate(savedAssembly.getDate());
        transaction.setPostedBy(user);
        transaction.setPostedDate(user.getUserDate());
        transaction.setTransactionType(TT_BUILD_ASSEMBLY);
        transaction.setBuildAssembly(savedAssembly);

        transactionManager.createOrUpdate(transaction);
        List<EdsTransactionItem> transactionItemList = new ArrayList<>();
        transactionItemList = applyAssemblyItemToItemStock(item, savedAssembly, transaction, transactionItemList, warehouse.getObjectID());
        if (!transactionItemList.isEmpty()) {
            for (EdsTransactionItem ti : transactionItemList) {
                ti.setTransaction(transaction);
            }
        }
        transaction.getTransactionItems().addAll(transactionItemList);
        transactionManager.update(transaction);

        try {
            productsServicesSolrComponent.index(item);
        } catch (Exception e) {
            log.error("Error while indexing item {}", item.getObjectID(), e);
        }

        return transaction.getObjectID();
    }

    private List<EdsTransactionItem> applyAssemblyItemToItemStock(EdsItem item, EdsSavedAssemblyItem savedAssembly, EdsInventoryTransaction transaction, List<EdsTransactionItem> transactionItemList, Integer warehouseID) {
        BigDecimal COGSTotal = ZERO; //before set ZERO of cost of goods sold value
        EdsTransactionItem transactionItem;
        Set<EdsAssemblyItemItems> itemTables = savedAssembly.getItemTables();

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (itemTables != null) {
            for (EdsAssemblyItemItems itemTable : itemTables) {
                EdsItem productItem = itemManager.get(itemTable.getItemId());

                EdsAccount account = null;
                if (savedAssembly.getAccountId() != null) {
                    account = accountingManager.get(savedAssembly.getAccountId());
                }
                BigDecimal COGS = applyAssemblySubItemToStock(itemTable.getQuantity(), transaction,
                        financialSettings.getEnableMultiWarehouse() && itemTable.getWarehouseID() != null
                                ? itemTable.getWarehouseID()
                                : warehouseID,
                        transactionItemList, productItem, account);
                COGSTotal = COGSTotal.add(COGS);
            }
        }

        BigDecimal qty = savedAssembly.getQuantity();
        BigDecimal price = COGSTotal.divide(qty, 10, RoundingMode.HALF_UP);
        BigDecimal trValue = qty.multiply(price);

        transactionItem = new EdsTransactionItem();
        transactionItem.setAccount(item.getAssetAccount());
        transactionItem.setDebit(trValue);
        transactionItemList.add(transactionItem);

        EdsItemStock itemStock = new EdsItemStock();
        itemStock.setWarehouse(warehouseManager.get(warehouseID));

        itemStock.setQuantity(qty);
        itemStock.setPrice(price);
        itemStock.setDate(transaction.getJournalDate());
        itemStock.setTranDate(transaction.getJournalDate());
        itemStock.setTranValue(trValue);
        itemStock.setTranCode(TC_IN);
        itemStock.setItemId(item.getObjectID());
        itemStock.setOrder(itemStockManager.getNextOrder(item.getObjectID()));

        itemStock.setTransaction(transaction);
        itemStockManager.create(itemStock);

        return transactionItemList;
    }

    private BigDecimal applyAssemblySubItemToStock(BigDecimal qty, EdsInventoryTransaction transaction, Integer warehouseID, List<EdsTransactionItem> transactionItemList, EdsItem productItem, EdsAccount liabilityAccount) {
        BigDecimal COGS = BigDecimal.ZERO;

        if (productItem.getType().equals(INVENTORY_ITEM) || productItem.getType().equals(ASSEMBLY_ITEM)) {
            COGS = cogsServices.getService().getCOGSValue(productItem, qty, transaction, warehouseID, null);

            EdsTransactionItem transactionItem = new EdsTransactionItem();
            transactionItem.setAccount(productItem.getAssetAccount());
            transactionItem.setCredit(COGS);
            transactionItem.setInventoryId(productItem.getObjectID());
            transactionItem.setCogsItem(true);

            transactionItemList.add(transactionItem);
        } else if (productItem.getType().equals(PRODUCT_KIT)) {
            List<EdsProductKitItems> pkItemList = productItem.getProductKitItems();

            if (pkItemList != null) {
                for (EdsProductKitItems pki : pkItemList) {
                    COGS = COGS.add(applyAssemblySubItemToStock(qty.multiply(pki.getQuantity()), transaction, warehouseID, transactionItemList, pki.getItem(), liabilityAccount));
                }
            }
        } else {
            COGS = COGS.add(Optional.ofNullable(productItem.getUnitPrice()).orElse(BigDecimal.ZERO).multiply(qty));
            EdsTransactionItem transactionItem = new EdsTransactionItem();
            transactionItem.setAccount(liabilityAccount);
            transactionItem.setCredit(COGS);
            transactionItem.setInventoryId(productItem.getObjectID());
            transactionItem.setCogsItem(true);
            transactionItemList.add(transactionItem);
        }

        try {
            productsServicesSolrComponent.index(productItem);
        } catch (Exception e) {
            log.error("Error applyAssemblySubItemToStock {}", productItem.getObjectID(), e);
        }

        return COGS;
    }

    public Integer saveBuildAssemblyNote(Integer savedAssemblyItemId, HistoryListItem hisItem) {
        if (savedAssemblyItemId != null && hisItem != null) {
            EdsUser user = userManager.getUser();
            if (user instanceof EdsEmployee) {
                user = userManager.get(user.getObjectID());
            }
            EdsBuildAssemblyNote buildAssemblyNote = new EdsBuildAssemblyNote();
            buildAssemblyNote.setBuildassembly(savedAssemblyItemManager.get(savedAssemblyItemId));
            buildAssemblyNote.setCreationDate(new Date());
            buildAssemblyNote.setUser(user);
            buildAssemblyNote.setSuperUser(ServerUtils.isSuperUser());
            buildAssemblyNote.setText(hisItem.getComment());
            buildAssemblyNoteManager.create(buildAssemblyNote);
            return buildAssemblyNote.getObjectID();
        }
        return null;
    }

    public List<HistoryNote> loadBuildAssembyNotes(Integer savedAssemblyItemId) {
        List<EdsBuildAssemblyNote> historyList = savedAssemblyItemId != null ? buildAssemblyNoteManager.getComments(savedAssemblyItemId) : null;
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        List<HistoryNote> noteItemsList = new ArrayList<>();
        for (EdsBuildAssemblyNote item : historyList) {
            if (StringUtils.isNotBlank(item.getText())) {
                HistoryListItem historyListItem = new HistoryListItem();
                historyListItem.setObjectID(item.getObjectID());
                if (item.isSuperUser()) {
                    historyListItem.setEmployee(Constants.defaultSupportName);
                } else {
                    historyListItem.setEmployee(item.getUser().getFullName());
                }
                historyListItem.setEmployeeID(item.getUser().getObjectID());
                if (item.getText().split(":").length > 1 && item.getText().split(":")[0].equals("rejectionReason")) { // For: Rejection Reason
                    historyListItem.setComment(commonLocalizer.localize(PdfLocalizationName.rejectionReason, "Rejection Reason:") + " " + item.getText().split(":")[1]);
                } else {
                    try {
                        historyListItem.setComment(commonLocalizer.localize(item.getText().toLowerCase()));
                    } catch (Exception e) {
                        historyListItem.setComment(item.getText());
                    }
                }
                historyListItem.setEventDate(item.getCreationDate());
                noteItemsList.add(historyListItem);
            }
        }

        return noteItemsList;
    }
}
