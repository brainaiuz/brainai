package com.edatasite.workforce.gwt.accounting.server.app;

import com.antkorwin.xsync.XSync;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.customfields.EdsFixedAssetCustomFields;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.service.SessionContextService;
import com.edatasite.workforce.gwt.accounting.client.rpc.DailyDepreciationRateItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.DepreciationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.*;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.DepreciationManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.FixedAssetCFManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.FixedAssetEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DeprecationItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Normurod on 4/11/2017.
 */
@Service("fixedAssetService")
@Transactional
public class FixedAssetServiceImpl implements FixedAssetService, FixedAssetServiceLocal, Constants, AccountingConstants {
    private static final Logger log = LoggerFactory.getLogger(FixedAssetServiceImpl.class);

    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private InvoiceServiceLocal invoiceService;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private FixedAssetManager fixedAssetManager;
    @Autowired
    private DepreciationManager depreciationManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private FixedAssetCFManager fixedAssetCFManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private SessionContextService sessionContextService;
    @Autowired
    private NotificationMsgManager notificationMsgManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    protected XSync<String> stringXSync;
    @Autowired
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final int CHUNK_SIZE = 200;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;

    private static final int DAY_AS_MILL_SEC = 1000 * 60 * 60 * 24;

    //@CheckPermission(permissions = {PermissionConstants.ACCOUNTING_FIXED_ASSET_LIST})
    @Override
    public ListResult<FixedAssetItem> getFixedAssets(ListingFilterParameter filterParameter) {
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            filterParameter.setCustomFieldsShown(true);
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.FixedAsset));
            filterParameter.setColumnsOfListing(panelTools.getColumnCodeName());
        }
        ListResult<EdsFixedAsset> fixedAssetList = fixedAssetManager.getFixedAssets(filterParameter);
        int totalCount = fixedAssetList.getTotal();
        ArrayList<FixedAssetItem> itemsList = new ArrayList<>();
        List<Integer> assetsNotEditable = depreciationManager.getDepreciationPostedAssets();

        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        String faNumberingFormat = settings != null ? settings.getFixedAssetNumberingFormat() : null;

        for (EdsFixedAsset pl : fixedAssetList.getList()) {
            FixedAssetItem items = new FixedAssetItem();

            items.setObjectID(pl.getObjectID());
            items.setAccount(new AccountItem(pl.getAccount().getObjectID(), pl.getAccount().getAccountCode(), pl.getAccount().getName()));
            items.setName(pl.getName());
            items.setCode(pl.getCode());
            items.setNumberData(new NumberData(pl.getCode(), pl.getIntNumber()));
            items.getNumberData().setNumberFormat(faNumberingFormat);
            items.setDescription(pl.getDescription());
            items.setCost(pl.getCost());
            items.setDisposed(pl.getDisposed());
            items.setStatus(pl.getStatus());
            items.setCreationDate(new DateNonConvertable(pl.getCreationDate()));
            items.setUsefulLife(pl.getUsefulLife());
            items.setResidualValue(pl.getResidualValue());
            items.setEditable(!assetsNotEditable.contains(pl.getObjectID()) && pl.getPurchaseInvoice() == null && pl.getPurchaseOrder() == null);
            items.setOwner(pl.getOwner() != null ? pl.getOwner().getAsSelectItem() : null);
            items.setDepartment(pl.getDepartment() != null ? pl.getDepartment().getAsSelectItem() : null);
            items.setFixedAssetAccount(pl.getAssetAccount() != null ? pl.getAssetAccount().getAsSelectItem() : null);
            items.setExpenseAccount(pl.getExpenseAccount() != null ? pl.getExpenseAccount().getAsSelectItem() : null);

            if (pl.getLocation() != null) {
                SelectItem locationItem = pl.getLocation().getAsSelectItem();
                items.setLocationID(locationItem.getId());
                items.setLocationName(locationItem.getName());
            }

            if (pl.getFinancedBy() != null) {
                items.setFinancedByAccount(new AccountItem(pl.getFinancedBy().getObjectID(), pl.getFinancedBy().getAccountCode(), pl.getFinancedBy().getName()));
            }

            if (pl.getCustomFields() != null && filterParameter.isCustomFieldsShown()) {
                items.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(pl.getCustomFields(), filterParameter.getColumnsOfListing()));
            }
            items.setCalculateDepreciation(pl.isCalculateDepreciation());
            itemsList.add(items);

        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFixedAsset.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "get fixed assets list");
        return new ListResult<>(itemsList, totalCount);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FixedAssetItem getFixedAssetData(Integer objectID) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFixedAsset.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "View fixed asset");
        FixedAssetItem items = new FixedAssetItem();
        items.setNumberData(generateFixedAssetNumber());

        items.setLocations(locationManager.getLocationsAsSelectItems(new ListingFilterParameter()));
        EdsUser user = userManager.getUser();

        if (objectID != null) {
            EdsFixedAsset fa = fixedAssetManager.get(objectID);
            items.setObjectID(objectID);
            items.setAccount(new AccountItem(fa.getAccount().getObjectID(), fa.getAccount().getAccountCode(), fa.getAccount().getName()));
            items.getNumberData().setNumberString(fa.getCode());
            items.getNumberData().setIntNumber(fa.getIntNumber());
            items.setCode(fa.getCode());
            items.setName(fa.getName());
            items.setDescription(fa.getDescription());
            items.setCost(fa.getCost());
            items.setDisposed(fa.getDisposed());
            if (fa.getVat() != null) {
                items.setTaxItem(fa.getVat().createTaxItem());
                items.setTaxAmount(fa.getTaxAmount());
                items.setTaxCalculationType(fa.getTaxCalculationType());
            }

            if (fa.getImage() != null) {
                String imageUrl = fixedAssetManager.getImage(fa.getImage());
                imageUrl = imageUrl.replace("[", "");
                imageUrl = imageUrl.replace("]", "");
                items.setImageID(fa.getImage());
                items.setImageLink(imageUrl);

            }

            EdsFixedAssetTransaction edsFixedAssetTransaction = transactionManager.getFixedAssetTransaction(objectID);
            if (edsFixedAssetTransaction != null && !edsFixedAssetTransaction.isDeleted()) {
                items.setJournalId(edsFixedAssetTransaction.getJournalId());
            }
            items.setCreationDate(new DateNonConvertable(fa.getCreationDate()));
            items.setUsefulLife(fa.getUsefulLife());
            items.setResidualValue(fa.getResidualValue());

            if (fa.getPurchaseInvoice() != null) {
                items.setPurchaseInvoiceID(fa.getPurchaseInvoice().getObjectID());
                items.setConvertedItemNumber(fa.getPurchaseInvoice().getNumber());
            }

            if (fa.getPurchaseOrder() != null) {
                items.setPurchaseOrderID(fa.getPurchaseOrder().getObjectID());
                items.setConvertedItemNumber(fa.getPurchaseOrder().getNumber());
            }

            if (fa.getFinancedBy() != null) {
                items.setFinancedByAccount(new AccountItem(fa.getFinancedBy().getObjectID(), fa.getFinancedBy().getAccountCode(), fa.getFinancedBy().getName()));
            }
            if (fa.getDisposedDate() != null) {
                items.setDisposedDate(new DateNonConvertable(fa.getDisposedDate()));
            }
            if (fa.getDepartment() != null) {
                items.setDepartment(fa.getDepartment().getAsSelectItem());
            }

            items.setCalculateDepreciation(fa.isCalculateDepreciation());
            items.setLocationID(fa.getLocation() != null ? fa.getLocation().getObjectID() : null);
            items.setShowDescInBarcode(fa.getShowDescriptionInBarcode());
            items.setCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(fa.getCustomFields(), commonService.getCompanyCustomFields(ViewName.FixedAsset)));

            items.setFixedAssetAccount(fa.getAssetAccount() != null ? fa.getAssetAccount().getAsSelectItem() : null);
            items.setExpenseAccount(fa.getExpenseAccount() != null ? fa.getExpenseAccount().getAsSelectItem() : null);
            items.setTemplates(getFixedAssetPdfTemplates(PdfReferenceCodeNameEnum.FIXED_ASSET.name()).getItems());

            if (fa.getOwner() != null) {
                items.setOwner(!fa.getOwner().getObjectID().equals(user.getObjectID()) ? fa.getOwner().getAsSelectItem() : new SelectItem(user.getObjectID(), user.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")")));
            }
        } else {
            items.setOwner(new SelectItem(user.getObjectID(), user.getName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")")));
        }

        return items;
    }

    @Override
    public Integer saveFixedAssetData(FixedAssetItem item) throws NumberExistingException {
        final NumberData numberData = item.getNumberData();
        if (item.getObjectID() == null && (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
            throw new NumberExistingException("Incorrect fixed asset number format.");
        }
        int count = 1;
        if (item.getQuantity() != null && item.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
            count = item.getQuantity().intValue();
        }

        for (int i = 0; i < count; i++) {
            EdsFixedAsset fixedAsset = new EdsFixedAsset();
            this.initForSave(fixedAsset, item);

            fixedAsset.setOwner(userManager.get(item.getOwner().getId()));

            NumberData newNumberData = numberData;
            if (numberData.getNumberString() == null || numberData.getNumberString().isEmpty() || fixedAssetManager.isFixedAssetNumberExists(numberData.getNumberString(), item.getObjectID())) {
                newNumberData = generateFixedAssetNumber();
            }

            if (newNumberData != null) {
                fixedAsset.setCode(newNumberData.getNumberString());
                fixedAsset.setIntNumber(newNumberData.getIntNumber());
                fixedAsset.setName(fixedAsset.getCode() + " -> " + fixedAsset.getName());
            }

            fixedAssetManager.create(fixedAsset);

            if (item.getDailyDepreciationRateItems() != null) {
                item.getDailyDepreciationRateItems();
                for (DailyDepreciationRateItem dr : item.getDailyDepreciationRateItems()) {
                    EdsDailyDepreciationRate depreciationRate = new EdsDailyDepreciationRate();
                    depreciationRate.setDailyDepreciation(dr.getDailyDepreciation());
                    depreciationRate.setFinancialYearStart(dr.getFinancialYearStart().getNonConvertedDate());
                    depreciationRate.setFinancialYearEnd(dr.getFinancialYearEnd().getNonConvertedDate());
                    fixedAsset.addDailyDepRateItem(depreciationRate);
                }
            }

            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsFixedAsset.class.getSimpleName());
            kpiLog.setEntityId(fixedAsset.getObjectID());

            baseEventPostProcessor.registerEvent(FixedAssetEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, fixedAsset, userManager.getUser());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add fixed asset");

            baseEventPostProcessor.registerEvent(FixedAssetEventListenerImpl.TYPE, FixedAssetEventListenerImpl.EVENT_FIXED_ASSET_OWNER_ADD, fixedAsset, userManager.getUser());

            if (FIXED_ASSET_APPROVED.equals(fixedAsset.getStatus())) {
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FIXED_ASSET_TRANSACTION_CANCEL)) {
                    if (!fixedAsset.getFinancedBy().getObjectID().equals(fixedAsset.getAccount().getObjectID())) {
                        createOrUpdateFixedAssetTransaction(fixedAsset);
                    }
                } else {
                    createOrUpdateFixedAssetTransaction(fixedAsset);
                }
            }
        }

        return null;
    }

    @Override
    public Integer updateFixedAssetData(FixedAssetItem item) throws NumberExistingException {
        EdsFixedAsset fixedAsset;

        if (item.getObjectID() == null) {
            return null;
        }

        fixedAsset = fixedAssetManager.get(item.getObjectID());
        this.initForSave(fixedAsset, item);

        NumberData numberData = item.getNumberData();
        if (numberData == null) {
            numberData = new NumberData(fixedAsset.getCode(), fixedAsset.getIntNumber());
        }
        if (fixedAsset.getCode() != null && (numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
            throw new NumberExistingException("Incorrect fixed asset number format.");
        }

        boolean isOwnerEdited = false;
        if (item.getOwner() != null && item.getOwner().getId() != null) {
            if (fixedAsset.getOwner() != null && !fixedAsset.getOwner().getObjectID().equals(item.getOwner().getId())) {
                isOwnerEdited = true;
            }
            fixedAsset.setOwner(userManager.get(item.getOwner().getId()));
        }

        fixedAssetManager.createOrUpdate(fixedAsset);

        try {
            fixedAssetManager.deleteFixedAssetDailyDepreciatioinRate(fixedAsset);
        } catch (Exception ex) {
            ex.getStackTrace();
        }

        if (item.getDailyDepreciationRateItems() != null) {
            item.getDailyDepreciationRateItems();
            for (DailyDepreciationRateItem dr : item.getDailyDepreciationRateItems()) {
                EdsDailyDepreciationRate depreciationRate = new EdsDailyDepreciationRate();
                depreciationRate.setDailyDepreciation(dr.getDailyDepreciation());
                depreciationRate.setFinancialYearStart(dr.getFinancialYearStart().getNonConvertedDate());
                depreciationRate.setFinancialYearEnd(dr.getFinancialYearEnd().getNonConvertedDate());
                fixedAsset.addDailyDepRateItem(depreciationRate);
            }
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFixedAsset.class.getSimpleName());
        kpiLog.setEntityId(fixedAsset.getObjectID());

        baseEventPostProcessor.registerEvent(FixedAssetEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, fixedAsset, userManager.getUser());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        ServerUtils.kpiLog(log, kpiLog, "Update fixed asset");

        if (isOwnerEdited) {
            baseEventPostProcessor.registerEvent(FixedAssetEventListenerImpl.TYPE, FixedAssetEventListenerImpl.EVENT_FIXED_ASSET_OWNER_EDIT, fixedAsset, userManager.getUser());
        }

        if (FIXED_ASSET_APPROVED.equals(fixedAsset.getStatus())) {
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FIXED_ASSET_TRANSACTION_CANCEL)) {
                if (!fixedAsset.getFinancedBy().getObjectID().equals(fixedAsset.getAccount().getObjectID())) {
                    createOrUpdateFixedAssetTransaction(fixedAsset);
                }
            } else {
                createOrUpdateFixedAssetTransaction(fixedAsset);
            }
        }

        return fixedAsset.getObjectID();
    }

    private CustomFormItemPdfTemplateList getFixedAssetPdfTemplates(String type) {
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

    private void initForSave(EdsFixedAsset fixedAsset, FixedAssetItem item) {
        if (item.getAccount() != null) {
            fixedAsset.setAccount(accountingManager.get(item.getAccount().getId()));
        }
        fixedAsset.setCode(item.getCode());
        fixedAsset.setName(item.getName());
        fixedAsset.setDescription(item.getDescription());
        fixedAsset.setCost(item.getCost());
        fixedAsset.setCreationDate(item.getCreationDate().getNonConvertedDate());
        fixedAsset.setUsefulLife(item.getUsefulLife());
        fixedAsset.setResidualValue(item.getResidualValue());
        fixedAsset.setStatus(item.getStatus());

        if (item.getTaxItem() != null) {
            fixedAsset.setVat(vatManager.get(item.getTaxItem().getId()));
            fixedAsset.setTaxAmount(item.getTaxAmount());
            fixedAsset.setTaxCalculationType(item.getTaxCalculationType());
        }

        if (item.getImageID() != null) {
            fixedAsset.setImage(item.getImageID());
        }

        if (item.getLocationID() != null) {
            fixedAsset.setLocation(locationManager.get(item.getLocationID()));
        }

        if (item.getDepartment() != null) {
            fixedAsset.setDepartment(departmentManager.get(item.getDepartment().getId()));
        } else {
            fixedAsset.setDepartment(null);
        }

        if (item.getPurchaseInvoiceID() != null) {
            fixedAsset.setPurchaseInvoice(invoiceManager.getPurchaseInvoice(item.getPurchaseInvoiceID()));
        }
        if (item.getPurchaseOrderID() != null) {
            fixedAsset.setPurchaseOrder(quoteManager.getPurchaseOrderByID(item.getPurchaseOrderID()));
        }
        if (item.getFinancedByAccount() != null) {
            fixedAsset.setFinancedBy(accountingManager.get(item.getFinancedByAccount().getId()));
        }
        fixedAsset.setCalculateDepreciation(item.isCalculateDepreciation());

        if (item.getFixedAssetAccount() != null && item.getFixedAssetAccount().getId() != null) {
            fixedAsset.setAssetAccount(accountingManager.get(item.getFixedAssetAccount().getId()));
        } else {
            fixedAsset.setAssetAccount(null);
        }
        if (item.getExpenseAccount() != null && item.getExpenseAccount().getId() != null) {
            fixedAsset.setExpenseAccount(accountingManager.get(item.getExpenseAccount().getId()));
        } else {
            fixedAsset.setExpenseAccount(null);
        }

        fixedAsset.setShowDescriptionInBarcode(item.getShowDescInBarcode());
        fixedAsset.setCustomFields(createFixedAssetCustomFields(item.getCustomFields()));
    }

    public void sendFixedAssetCountResults(String[] existingFixedAssetCodes) {
        HashMap<String, String> existingIDsMap = new HashMap<>();
        for (String code : existingFixedAssetCodes) {
            existingIDsMap.put(code, code);
        }

        ListResult<EdsFixedAsset> fixedAssetList = fixedAssetManager.getFixedAssets(new ListingFilterParameter());
        FixedAssetItem[] items = new FixedAssetItem[fixedAssetList.getTotal()];
        int i = 0;
        for (EdsFixedAsset fa : fixedAssetList.getList()) {
            items[i] = new FixedAssetItem();
            items[i].setObjectID(fa.getObjectID());
            items[i].setAccount(new AccountItem(fa.getAccount().getObjectID(), fa.getAccount().getAccountCode(), fa.getAccount().getName()));
            items[i].setName(fa.getName());
            items[i].setCode(fa.getCode());
            items[i].setExistingCodeStatus(existingIDsMap.containsKey(fa.getCode()) ? "Yes" : "No");
            i++;
        }

        messageManager.sendFixedAssetCountResult(new FixedAssetList(items, items.length));
    }

    public FixedAssetGroups getFixedAssetCategories(ListingFilterParameter filterParameter) {
        List<FixedAssetGroupItem> faGroupItems = fixedAssetManager.getFixedAssetGroups(filterParameter);
        int totalCount = faGroupItems.size();
        if (filterParameter.getStart() != 0 && filterParameter.getLimit() != null) {
            faGroupItems = ListUtils.getSublist(faGroupItems, filterParameter.getStart(), filterParameter.getLimit());
        }
        return new FixedAssetGroups(faGroupItems.toArray(new FixedAssetGroupItem[]{}), totalCount);
    }

    @Override
    public void deleteFixedAsset(Integer id) { // TODO: 7/3/2018 delete tax  transactions
        EdsFinancialSettings fs;
        EdsTransaction transaction = transactionManager.getFixedAssetTransaction(id);
        EdsFixedAsset fixedAsset = fixedAssetManager.get(id);
        if (transaction != null) {
            fs = financialSettingsManager.getFinancialSettings();
            financialSettingsManager.update(fs);
            transactionManager.setChangedAccountsForRecalculate(transaction.getObjectID());
            transaction.setDeleted(true);
            transactionManager.update(transaction);
            List<EdsDepreciation> depreciationList = depreciationManager.getDepreciationsByFixedAsset(id);
            for (EdsDepreciation dp : depreciationList) {
                dp.setDeleted(true);
                EdsDepreciationTransaction dpTransaction = transactionManager.getDepreciationTransaction(dp);
                if (dpTransaction != null) {
                    transactionManager.setChangedAccountsForRecalculate(dpTransaction.getObjectID());
                    dpTransaction.setDeleted(true);
                    transactionManager.update(dpTransaction);
                }
                depreciationManager.update(dp);
            }
        }
        if (fixedAsset.getDisposed()) {
            EdsDisposalTransaction disposalTransaction = transactionManager.getFixedAssetDisposalTransaction(id);
            if (disposalTransaction != null) {
                disposalTransaction.setDeleted(true);
                transactionManager.update(disposalTransaction);
            }
        }
        if (fixedAsset.getPurchaseInvoice() != null) {
            invoiceService.deleteInvoice(fixedAsset.getPurchaseInvoice().getObjectID(), PURCHASE_INVOICE);
        }

        if (fixedAsset.getSalesInvoice() != null) {
            invoiceService.deleteInvoice(fixedAsset.getSalesInvoice().getObjectID(), SALE_INVOICE);
        }

        baseEventPostProcessor.registerEvent(FixedAssetEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, fixedAsset, userManager.getUser());
        fixedAsset.setDeleted(true);
        fixedAssetManager.update(fixedAsset);

//        EdsAccount faAccount = fixedAsset.getAccount();
//        EdsAccount financedByAccount = fixedAsset.getFinancedBy();
//        if (faAccount != null) {
//            faAccount.setBalanceCalculated(false);
//            accountingManager.update(faAccount);
//        }
//        if (financedByAccount != null) {
//            financedByAccount.setBalanceCalculated(false);
//            accountingManager.update(financedByAccount);
//        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsFixedAsset.class.getSimpleName());
        kpiLog.setEntityId(id);
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        ServerUtils.kpiLog(log, kpiLog, "Delete fixed asset");
    }

    public void updateDeprecations(DeprecationItemMQ item) {
        long start = System.currentTimeMillis();
        EdsUser currentUser = userManager.getUserByUserID(item.getUser());
        List<EdsFixedAsset> fixedAssets = fixedAssetManager.getDepreciationEnabledFixedAssets(item.getIds());
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        AtomicInteger skipped = new AtomicInteger();
        Queue<String> skippedDetails = new ConcurrentLinkedQueue<>();
        int parallelism = Math.min(20, Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(parallelism);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        AtomicInteger processed = new AtomicInteger();
        for (EdsFixedAsset asset : fixedAssets) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                EdsFixedAsset fa = asset;
                stringXSync.execute(getSynchronizedKey(fa), () -> {
                    try {
                        ServerSecurityContext.getInstance().setDatabase(item.getDatabase());
                        ServerSecurityContext.getInstance().setCompanyId(item.getCompanyId());
                        sessionContextService.getInNewTransaction(() -> {
                            updateFixedAssetDeprecations(fa, null, item.getPeriod(), currentUser, financialSettings);
                            return null;
                        });

                    } catch (Exception e) {
                        skipped.incrementAndGet();
                        skippedDetails.add("Asset Code: " + fa.getCode() + ", Error: " + e.getMessage());
                        log.error("Fixed Asset processing failed. Code={}, ObjectID={}", fa.getCode(), fa.getObjectID(), e);
                    }
                });
                int count = processed.incrementAndGet();
                if (count % 50 == 0) {
                    log.info("Processed {} assets...", count);
                }
            }, executor);
            futures.add(future);
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();

        if (item.isLastStream()) {
            try {
                notificationMsgManager.createFixedAssetUpdateDeprecationNotification(currentUser);
                EdsImportFile status = importFileManager.get(item.getStatusEntityId());
                if (status != null) {
                    status.setSkippedColumns(skipped.get());
                    status.setStatus(ImportStatusEnum.COMPLETED);
                    importFileManager.createOrUpdate(status);
                }
                if (!skippedDetails.isEmpty()) {
                    log.warn("Depreciation update skipped for {} assets:", skippedDetails.size());
                    skippedDetails.forEach(msg -> log.warn(" - {}", msg));
                }

            } catch (Exception e) {
                log.error("Error finalizing fixed asset depreciation process", e);
            }
        }
        long end = System.currentTimeMillis();
        log.info("Depreciation process finished in {} ms", (end - start));
    }

    protected String getSynchronizedKey(EdsFixedAsset item) {
        return ServerSecurityContext.getInstance().getCompanyId() + "_" + item.getObjectID();
    }

    @Override
    public void disposeFixedAssetItem(FixedAssetItem fixedAssetItem) {
        EdsUser user = fixedAssetManager.getUser();
        EdsFixedAsset fixedAsset = fixedAssetManager.get(fixedAssetItem.getObjectID());
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
//        Integer calculationScale = financialSettings.getAccountingCalculationScale();

        BigDecimal totalDepreciatedAmount = ZERO;
        EdsFixedAssetTransaction faTransaction = transactionManager.getFixedAssetTransaction(fixedAssetItem.getObjectID());

        if (faTransaction != null) {
            EdsTransactionItem[] transItems = faTransaction.getTransactionItems().toArray(new EdsTransactionItem[]{});

            for (EdsTransactionItem ti : transItems) {

                if (ti.getAccount().getKey() != null && EdsAccount.OPENING_BALANCE == ti.getAccount().getKey() && ti.getDebit() != null) {
                    totalDepreciatedAmount = totalDepreciatedAmount.add(ti.getDebit());//Add Depreciated Amount Before Conversion Date
                }
            }
        }

        if (fixedAssetItem.getDisposedDate() != null  && fixedAsset.isCalculateDepreciation() ) {
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            //EdsDepreciation depreciation = depreciationManager.getDepreciationByMonth(dateFormat.format(fixedAssetItem.getDisposedDate().getNonConvertedDate()), fixedAsset.getObjectID());
            deleteDepreciationsByDisposalDate(fixedAssetItem.getDisposedDate().getNonConvertedDate(), fixedAsset.getObjectID());
            updateFixedAssetDeprecations(fixedAsset, fixedAssetItem.getDisposedDate().getNonConvertedDate(), null, user, financialSettings);
            totalDepreciatedAmount = totalDepreciatedAmount.add(depreciationManager.getFixedAssetTotalDepreciatedAmount(fixedAssetItem.getObjectID()));
        }


        EdsDisposalTransaction transaction = new EdsDisposalTransaction();
        transaction.setFixedAsset(fixedAsset);
        transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID(user.getCompany()) + 1);
        transaction.setName("Fixed Asset Disposal: " + fixedAsset.getName());
        transaction.setJournalDate(fixedAssetItem.getDisposedDate() != null ? fixedAssetItem.getDisposedDate().getNonConvertedDate() : user.getUserDate());
        transaction.setPostedDate(user.getUserDate());
        transaction.setPostedBy(user);

        CurrencyItem currency = invoiceService.getBaseCurrency();
        BigDecimal exchangeRate = BigDecimal.ONE;
        if (fixedAsset.getSalesInvoice() != null) {
            transaction.setClient(fixedAsset.getSalesInvoice().getClient());
            if (fixedAsset.getSalesInvoice().getCurrency() != null) {
                currency = fixedAsset.getSalesInvoice().getCurrency().createCurrencyItem();
                exchangeRate = fixedAsset.getSalesInvoice().getExchangeRate();
            }
        }
        if (fixedAsset.getPurchaseInvoice() != null) {
            transaction.setSupplier(fixedAsset.getPurchaseInvoice().getSupplier());
            if (fixedAsset.getPurchaseInvoice().getCurrency() != null) {
                currency = fixedAsset.getPurchaseInvoice().getCurrency().createCurrencyItem();
                exchangeRate = fixedAsset.getPurchaseInvoice().getExchangeRate();
            }
        }

        ArrayList<EdsTransactionItem> transactionItemList = new ArrayList<>();

        EdsTransactionItem transactionItem = new EdsTransactionItem();
        transactionItem.setAccount(fixedAsset.getAccount());
        transactionItem.setCredit(fixedAsset.getCost());
        transactionItemList.add(transactionItem);

        if (totalDepreciatedAmount.compareTo(BigDecimal.ZERO) > 0) {
            transactionItem = new EdsTransactionItem();
            transactionItem.setAccount(fixedAsset.getAssetAccount() != null ? fixedAsset.getAssetAccount() : accountingManager.getAccountByKey(EdsAccount.ACCUMULATED_DEPRECIATION));
            transactionItem.setDebit(totalDepreciatedAmount);
            transactionItemList.add(transactionItem);
        }

        BigDecimal gainOrLossAmount;
        if (DISPOSE_CASH.equals(fixedAssetItem.getDisposeType()) || DISPOSE_ACCOUNTS_RECEIVABLE.equals(fixedAssetItem.getDisposeType())) {
            transactionItem = new EdsTransactionItem();
            if (DISPOSE_ACCOUNTS_RECEIVABLE.equals(fixedAssetItem.getDisposeType())) {
                transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_RECEIVABLE, currency.getId()));
            } else {
                transactionItem.setAccount(accountingManager.get(fixedAssetItem.getDisposeAccountID()));
            }
            transactionItem.setDebit(fixedAssetItem.getDisposeAmount());
            if (transactionItem.getAccount().isForeignAccount()) {
                transactionItem.setForeignDebit(fixedAssetItem.getDisposeAmount().multiply(exchangeRate));
            }
            transactionItemList.add(transactionItem);

            gainOrLossAmount = fixedAsset.getCost().subtract(totalDepreciatedAmount.add(fixedAssetItem.getDisposeAmount()));

            if (fixedAssetItem.getDisposeTaxAmount() != null && fixedAssetItem.getDisposeTaxAmount().setScale(5, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) > 0) {
                transactionItem = new EdsTransactionItem();
                transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.VAT_PAYABLE));
                transactionItem.setCredit(fixedAssetItem.getDisposeTaxAmount());
                transactionItemList.add(transactionItem);

                if (gainOrLossAmount.compareTo(ZERO) >= 0) {
                    gainOrLossAmount = gainOrLossAmount.subtract(fixedAssetItem.getDisposeTaxAmount());
                } else {
                    gainOrLossAmount = gainOrLossAmount.add(fixedAssetItem.getDisposeTaxAmount());
                }
            }
        } else {
            gainOrLossAmount = fixedAsset.getCost().subtract(totalDepreciatedAmount);
        }
        gainOrLossAmount = gainOrLossAmount.setScale(5, RoundingMode.HALF_UP);

        if (gainOrLossAmount.compareTo(BigDecimal.ZERO) != 0) {
            if (gainOrLossAmount.compareTo(BigDecimal.ZERO) > 0) {
                transactionItem = new EdsTransactionItem();
                transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.LOSS_ON_DISPOSAL));
                transactionItem.setDebit(gainOrLossAmount);
                transactionItemList.add(transactionItem);
            } else {
                transactionItem = new EdsTransactionItem();
                transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.GAIN_ON_DISPOSAL));
                transactionItem.setCredit(gainOrLossAmount.abs());
                transactionItemList.add(transactionItem);
            }
        }

        transactionManager.create(transaction);

        for (EdsTransactionItem i : transactionItemList) {
            i.setTransaction(transaction);
        }
        transaction.getTransactionItems().addAll(transactionItemList);

        fixedAsset.setDisposed(true);
        fixedAsset.setDisposedDate(fixedAssetItem.getDisposedDate() != null ? fixedAssetItem.getDisposedDate().getNonConvertedDate() : transaction.getJournalDate());
        fixedAssetManager.update(fixedAsset);

        baseEventPostProcessor.registerEvent(FixedAssetEventListenerImpl.TYPE, FixedAssetEventListenerImpl.EVENT_FIXED_ASSET_DISPOSE, fixedAsset, userManager.getUser());
    }

    @Override
    public NumberData generateFixedAssetNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = fixedAssetManager.getFixedAssetLastIntNumber();
        if (settings != null && settings.getFixedAssetNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getFixedAssetNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_FIXED_ASSET_PREFIX);
        }
    }

    @Override
    public boolean validateFixedAssetNumber(String number, Integer objectID) {
        return fixedAssetManager.isFixedAssetNumberExists(number, objectID);
    }

    private void updateFixedAssetDeprecations(EdsFixedAsset fixedAsset, Date disposeDate, Date depreciationEndDate, EdsUser user, EdsFinancialSettings financialSettings) {
        if (FIXED_ASSET_APPROVED.equals(fixedAsset.getStatus())) {
            List<DepreciationItem> generatedDeprications = generateDepreciationItems(fixedAsset, disposeDate, depreciationEndDate, financialSettings.getConversionDate(), user);
            if (!generatedDeprications.isEmpty()) {
                createDeprecations(generatedDeprications, fixedAsset, user, financialSettings.getBlockBeforeDate());
            }
        }
    }

    public void createOrUpdateFixedAssetTransaction(EdsFixedAsset fixedAsset) {
        EdsUser user = userManager.getUser();
        boolean updateDepreciation = false;
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        EdsFixedAssetTransaction transaction = transactionManager.getFixedAssetTransaction(fixedAsset.getObjectID());
        if (transaction == null) {
            transaction = new EdsFixedAssetTransaction();
            transaction.setFixedAsset(fixedAsset);
            transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID(user.getCompany()) + 1);
            updateDepreciation = true;
        } else {
            transactionManager.deleteTransactionItems(transaction.getObjectID());
        }
        transaction.setName("Fixed Asset: " + fixedAsset.getName());
        transaction.setJournalDate(fixedAsset.getCreationDate());
        transaction.setPostedBy(user);
        transaction.setPostedDate(user.getUserDate());
        transactionManager.createOrUpdate(transaction);

        if (fixedAsset.getSalesInvoice() != null) {
            transaction.setClient(fixedAsset.getSalesInvoice().getClient());
        }
        if (fixedAsset.getPurchaseInvoice() != null) {
            transaction.setSupplier(fixedAsset.getPurchaseInvoice().getSupplier());
        }
        List<EdsTransactionItem> transactionItemList = new ArrayList<>();

        BigDecimal assetsTransactionValue = Optional.ofNullable(fixedAsset.getCost()).orElse(BigDecimal.ZERO);
        BigDecimal taxAmountReceived = fixedAsset.getTaxAmount();
        BigDecimal taxedAmount = BigDecimal.ZERO;
        BigDecimal fixedAssetCost = fixedAsset.getCost();

        if (fixedAsset.getVat() != null && taxAmountReceived != null) {
            taxedAmount = TAX_CALCULATION_EXCLUSIVE.equals(fixedAsset.getTaxCalculationType()) && fixedAsset.getTaxAmount() != null ? fixedAsset.getTaxAmount() : BigDecimal.ZERO;
            boolean isTaxAccountEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.TAX_ACCOUNT_ENABLED);
            EdsAccount systemTaxAccount = accountingManager.getVatAccount(PAYABLE);
            fixedAssetCost = TAX_CALCULATION_INCLUSIVE.equals(fixedAsset.getTaxCalculationType()) ? fixedAssetCost.subtract(taxAmountReceived) : fixedAssetCost;

            if (isTaxAccountEnabled) {
                Map<Integer, BigDecimal> accountTaxesMap = fixedAsset.getVat().calculateAccountTaxes(fixedAssetCost, fixedAsset.getTaxCalculationType());

                for (Integer accountKey : accountTaxesMap.keySet()) {
                    BigDecimal calculatedTax = accountTaxesMap.get(accountKey);

                    EdsTransactionItem transactionItem = new EdsTransactionItem();
                    transactionItem.setAccount(accountKey > 0 ? accountingManager.get(accountKey) : systemTaxAccount);
                    if (calculatedTax.compareTo(BigDecimal.ZERO) >= 0) {
                        transactionItem.setDebit(calculatedTax);//liability is also a DEBIT in this case
                    } else {
                        transactionItem.setCredit(calculatedTax.abs());//liability is also a CREDIT in this case
                    }
                    transactionItemList.add(transactionItem);
                }
            } else {
                EdsTransactionItem transactionItem = new EdsTransactionItem();
                transactionItem.setAccount(systemTaxAccount);
                if (taxAmountReceived.compareTo(BigDecimal.ZERO) >= 0) {
                    transactionItem.setDebit(taxAmountReceived.abs());//liability is also a DEBIT in this case
                } else {
                    transactionItem.setCredit(taxAmountReceived.abs());//liability is also a CREDIT in this case
                }
                transactionItemList.add(transactionItem);
            }
            if (fixedAsset.getTaxCalculationType().equals(TAX_CALCULATION_INCLUSIVE)) {
                assetsTransactionValue = assetsTransactionValue.subtract(taxAmountReceived.abs());
            }
        }


        EdsTransactionItem item = new EdsTransactionItem();
        item.setAccount(fixedAsset.getAccount());
        item.setDebit(assetsTransactionValue);
        item.setDepartment(fixedAsset.getDepartment());
        transactionItemList.add(item);

        item = new EdsTransactionItem();
        item.setAccount(fixedAsset.getFinancedBy());
        item.setCredit(fixedAsset.getCost().add(taxedAmount));

        if (fixedAsset.getPurchaseInvoice() != null && fixedAsset.getFinancedBy().isForeignAccount()) {
            item.setForeignCredit(item.getCredit().multiply(fixedAsset.getPurchaseInvoice().getExchangeRate()));
        }
        item.setDepartment(fixedAsset.getDepartment());
        transactionItemList.add(item);

        if (fixedAsset.isCalculateDepreciation()) {

            Calendar conversionCal = new GregorianCalendar(),
                    purchaseDateCal = new GregorianCalendar(),
                    disposalDateCal = new GregorianCalendar(),
                    endDateCal = new GregorianCalendar();

            conversionCal.setTime(user.getUserDate(financialSettings.getConversionDate()));
            purchaseDateCal.setTime(fixedAsset.getCreationDate());

            disposalDateCal = calculateDisposalDate(fixedAsset.getUsefulLife(), purchaseDateCal);

            ServerUtils.setBeginningOfTheDay(purchaseDateCal);
            ServerUtils.setBeginningOfTheDay(disposalDateCal);

            if (purchaseDateCal.before(conversionCal)) {
                endDateCal = (Calendar) conversionCal.clone();

                if (disposalDateCal.before(conversionCal)) {
                    endDateCal = (Calendar) disposalDateCal.clone();
                }

                BigDecimal cost = fixedAsset.getCost().subtract(fixedAsset.getResidualValue());
                int usufulDayCount = Long.valueOf((disposalDateCal.getTime().getTime() - purchaseDateCal.getTime().getTime()) / DAY_AS_MILL_SEC).intValue() + 1;
                BigDecimal lineMethodDailyRate = cost.divide(new BigDecimal(usufulDayCount), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

                BigDecimal dailyRate = fixedAsset.getDailyDepreciationRateByDate(purchaseDateCal.getTime());
                if (dailyRate.compareTo(BigDecimal.ZERO) == 0) {
                    dailyRate = lineMethodDailyRate;
                }

                int dayHoldBeforeConversion = Long.valueOf((endDateCal.getTime().getTime() - purchaseDateCal.getTime().getTime()) / DAY_AS_MILL_SEC).intValue() + 1;
                BigDecimal convDepreciatedAmount = dailyRate.multiply(new BigDecimal(dayHoldBeforeConversion));

                if (convDepreciatedAmount.compareTo(ZERO) > 0) {
                    item = new EdsTransactionItem();
                    item.setAccount(fixedAsset.getAssetAccount());
                    item.setCredit(convDepreciatedAmount);
                    item.setDepartment(fixedAsset.getDepartment());
                    transactionItemList.add(item);

                    item = new EdsTransactionItem();
                    item.setAccount(accountingManager.getAccountByKey(EdsAccount.OPENING_BALANCE));
                    item.setDebit(convDepreciatedAmount);
                    item.setDepartment(fixedAsset.getDepartment());
                    transactionItemList.add(item);
                }
            }
        }

        for (EdsTransactionItem i : transactionItemList) {
            i.setTransaction(transaction);
        }
        transaction.getTransactionItems().addAll(transactionItemList);
        transactionManager.createOrUpdate(transaction);

        if (updateDepreciation && fixedAsset.isCalculateDepreciation()) {
            updateFixedAssetDeprecations(fixedAsset, null, null, user, financialSettings);
        }
    }

    @Override
    public String getFixedAssetImageUrl(Integer imageID) {
        return uploadManager.getFileURL(imageID);
    }

    private List<DepreciationItem> generateDepreciationItems(EdsFixedAsset fixedAsset, Date disposeDate, Date depreciationEndDate, Date conversionDate, EdsUser user) {
        List<DepreciationItem> depreciations = new ArrayList<>();

        if (fixedAsset.isCalculateDepreciation()) {

            Calendar conversionCal = new GregorianCalendar(),
                    purchaseDateCal = new GregorianCalendar(),
                    disposalDateCal = new GregorianCalendar(),
                    startDateCal = new GregorianCalendar(),
                    endDateCal = new GregorianCalendar(),
                    currentDateCal = new GregorianCalendar();

            conversionCal.setTime(user.getUserDate(conversionDate));
            purchaseDateCal.setTime(fixedAsset.getCreationDate());
            currentDateCal.setTime(depreciationEndDate != null ? depreciationEndDate : user.getUserDate());

            if (disposeDate != null) {
                disposalDateCal.setTime(disposeDate);
            } else {
                disposalDateCal = calculateDisposalDate(fixedAsset.getUsefulLife(), purchaseDateCal);
            }

            BigDecimal cost = TAX_CALCULATION_INCLUSIVE.equals(fixedAsset.getTaxCalculationType()) ? fixedAsset.getCost().subtract(fixedAsset.getTaxAmount()).subtract(fixedAsset.getResidualValue()) : fixedAsset.getCost().subtract(fixedAsset.getResidualValue());
            int usufulDayCount = Long.valueOf((disposalDateCal.getTime().getTime() - purchaseDateCal.getTime().getTime()) / (DAY_AS_MILL_SEC)).intValue() + 1;
            BigDecimal lineMethodDailyRate = cost.divide(new BigDecimal(usufulDayCount), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);


            if (purchaseDateCal.before(conversionCal)) {
                purchaseDateCal = (Calendar) conversionCal.clone();
            }
            startDateCal = (Calendar) purchaseDateCal.clone();
            startDateCal.set(Calendar.DATE, 1);

            ServerUtils.setBeginningOfTheDay(purchaseDateCal);
            ServerUtils.setBeginningOfTheDay(disposalDateCal);
            ServerUtils.setBeginningOfTheDay(startDateCal);
            ServerUtils.setBeginningOfTheDay(currentDateCal);

            List<String> postedDepreciations = depreciationManager.getPostedDepreciations(fixedAsset.getObjectID());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

            Calendar toDateCal = calculateDisposalDate(fixedAsset.getUsefulLife(), purchaseDateCal);

            if (currentDateCal.compareTo(toDateCal) <= 0) {
                toDateCal = (Calendar) currentDateCal.clone();
            }

            while (startDateCal.compareTo(toDateCal) <= 0) {
                endDateCal = (Calendar) startDateCal.clone();
                endDateCal.set(Calendar.MONTH, endDateCal.get(Calendar.MONTH) + 1);
                endDateCal.set(Calendar.DATE, endDateCal.get(Calendar.DATE) - 1);

                BigDecimal dailyRate = fixedAsset.getDailyDepreciationRateByDate(startDateCal.getTime());
                if (dailyRate.compareTo(BigDecimal.ZERO) == 0) {
                    dailyRate = lineMethodDailyRate;
                }

                int dayHold = 0;

                if (purchaseDateCal.getTime().compareTo(startDateCal.getTime()) >= 0 && purchaseDateCal.getTime().compareTo(endDateCal.getTime()) <= 0) {
                    dayHold = Long.valueOf((endDateCal.getTime().getTime() - purchaseDateCal.getTime().getTime()) / (1000 * 60 * 60 * 24)).intValue() + 1;
                } else if (disposalDateCal.getTime().compareTo(startDateCal.getTime()) >= 0 && disposalDateCal.getTime().compareTo(endDateCal.getTime()) <= 0) {
                    dayHold = Long.valueOf((disposalDateCal.getTime().getTime() - startDateCal.getTime().getTime()) / (1000 * 60 * 60 * 24)).intValue() + 1;
                } else {
                    dayHold = startDateCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                }

                BigDecimal deprAmountPerPeriod = dailyRate.multiply(new BigDecimal(dayHold));

                if (!postedDepreciations.contains(dateFormat.format(startDateCal.getTime()))) {
                    DepreciationItem dItem = new DepreciationItem(deprAmountPerPeriod.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));

                    if (disposeDate != null && disposeDate.compareTo(endDateCal.getTime()) < 0) {
                        dItem.setMonth(disposeDate);
                    } else {
                        dItem.setMonth(endDateCal.getTime());
                    }
                    depreciations.add(dItem);
                    startDateCal.set(Calendar.DATE, 1);
                }

                startDateCal.set(Calendar.MONTH, startDateCal.get(Calendar.MONTH) + 1);
            }

        }
        return depreciations;
    }

    private Calendar calculateDisposalDate(BigDecimal usefulLife, Calendar purchaseDateCal) {
        Calendar toDateCal = (Calendar) purchaseDateCal.clone();
        int disposalYear = toDateCal.get(Calendar.YEAR) + usefulLife.intValue();

        toDateCal.add(Calendar.YEAR, usefulLife.intValue());

        boolean kabisa = disposalYear > 100 && disposalYear % 100 == 0 ? disposalYear % 400 == 0 : disposalYear % 4 == 0;

        BigDecimal yearPart = usefulLife.remainder(BigDecimal.ONE);

        BigDecimal days = yearPart.multiply(new BigDecimal(kabisa ? 366 : 365));

        BigDecimal dayPart = days.remainder(BigDecimal.ONE);

        int addOneDay = dayPart.compareTo(BigDecimal.ZERO) > 0 ? 1 : 0;

        toDateCal.add(Calendar.DATE, days.intValue() + addOneDay);

        return toDateCal;
    }

    private void deleteDepreciationsByDisposalDate(Date disposalDate, Integer fixedAssetID) {
        List<EdsDepreciation> depreciations = depreciationManager.getDepreciationsForDispose(ServerUtils.getMonthStartDate(disposalDate), fixedAssetID);

        if (depreciations != null && !depreciations.isEmpty()) {
            for (EdsDepreciation depreciation : depreciations) {
                depreciation.setDeleted(true);

                EdsDepreciationTransaction dt = transactionManager.getTransactionByDepreciation(depreciation);
                if (dt != null) {
                    dt.setDeleted(true);
                }
                transactionManager.update(dt);
            }
        }
    }

    private void createDeprecations(List<DepreciationItem> items, EdsFixedAsset fixedAsset, EdsUser user, Date blockBeforeDate) {
        for (DepreciationItem di : items) {
            if (!(blockBeforeDate != null && di.getMonth().before(blockBeforeDate))) {
                EdsDepreciation depreciation = new EdsDepreciation();
                depreciation.setPeriod(di.getPeriod());
                depreciation.setMonth(di.getMonth());
                depreciation.setAmount(di.getDepreciation());
                depreciation.setFixedAsset(fixedAsset);
                depreciationManager.create(depreciation);
                createTransactionForDepreciation(depreciation, user);
            }
        }
    }

    private void createTransactionForDepreciation(EdsDepreciation depreciation, EdsUser user) {
        EdsDepreciationTransaction transaction = new EdsDepreciationTransaction();
        transaction.setDepreciation(depreciation);
        transaction.setName("Depreciation: " + depreciation.getFixedAsset().getName());
        transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID(user.getCompany()) + 1);
        transaction.setJournalDate(depreciation.getMonth());
        transaction.setPostedBy(user);
        transaction.setPostedDate(user.getUserDate());

        EdsFixedAsset fixedAsset = depreciation.getFixedAsset();

        ArrayList<EdsTransactionItem> transactionItemList = new ArrayList<>();

        EdsTransactionItem item = new EdsTransactionItem();
        item.setAccount(fixedAsset.getExpenseAccount());
        item.setDebit(depreciation.getAmount());
        item.setDepartment(fixedAsset.getDepartment());
        transactionItemList.add(item);

        item = new EdsTransactionItem();
        item.setAccount(fixedAsset.getAssetAccount());
        item.setCredit(depreciation.getAmount());
        item.setDepartment(fixedAsset.getDepartment());

        transactionItemList.add(item);

        transactionManager.create(transaction);

        for (EdsTransactionItem i : transactionItemList) {
            i.setTransaction(transaction);
        }
        transaction.getTransactionItems().addAll(transactionItemList);
        transactionManager.update(transaction);
    }

    private EdsFixedAssetCustomFields createFixedAssetCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            EdsFixedAssetCustomFields fixedAssetCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                fixedAssetCustomFields = fixedAssetCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty()) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                fixedAssetCustomFields = new EdsFixedAssetCustomFields();
                fixedAssetCFManager.create(fixedAssetCustomFields);
            }
            CustomFieldsUtils.setAccountingDomainObjectCustomFields(fixedAssetCustomFields, customFieldItems);
            return fixedAssetCustomFields;
        }
        return null;
    }

    @Override
    public SelectItem[] getFixedAssetsForLookUp(ListingFilterParameter filterParameter) {
        List<EdsFixedAsset> fixedAssets = fixedAssetManager.getFixedAssetsForLookUp(filterParameter);
        int i = 0;
        SelectItem[] fixedAssetItems = new SelectItem[fixedAssets.size()];
        for (EdsFixedAsset fa : fixedAssets) {
            if (StringUtils.isNotBlank(fa.getCode())) {
                fixedAssetItems[i++] = new SelectItem(fa.getObjectID(), fa.getCode() + " -> " + fa.getName());
            } else {
                fixedAssetItems[i++] = new SelectItem(fa.getObjectID(), fa.getName());
            }
        }
        return fixedAssetItems;
    }

    @Override
    public boolean saveFixedAssetCellValue(FixedAssetItem rowValue, String columnCodeName) {
        EdsFixedAsset edsFixedAsset = fixedAssetManager.get(rowValue.getObjectID());
        try {
            EdsFixedAssetCustomFields fixedAssetCF = edsFixedAsset.getCustomFields();
            if (fixedAssetCF == null) {
                fixedAssetCF = new EdsFixedAssetCustomFields();
                fixedAssetCFManager.create(fixedAssetCF);
                edsFixedAsset.setCustomFields(fixedAssetCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(fixedAssetCF, rowValue.getCustomFieldsMap(), columnCodeName);

            return true;
        } catch (Exception e) {
            log.error("Fixed Asset List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    @Override
    public boolean sendToUpdateDeprecationMQ(DateNonConvertable monthEndDate) {
        ArrayList<EdsImportFile> inProcessFiles = importFileManager.getImportFileStatusByType(ImportTypeEnum.FIXED_ASSETS.toString(), ImportStatusEnum.IN_PROCESS.toString());
        if (inProcessFiles.isEmpty()) {
            EdsUser user = userManager.getUser();
            if (user != null) {
                EdsImportFile statusFile = new EdsImportFile();
                statusFile.setType(ImportTypeEnum.FIXED_ASSETS);
                statusFile.setHasHeader(true);
                statusFile.setStatus(ImportStatusEnum.IN_PROCESS);
                statusFile.setDefaultSeparator(",");
                statusFile.setOwner(user);
                importFileManager.create(statusFile);
                LinkedList<Integer> assetIds = fixedAssetManager.getDepreciationEnabledFixedAssetsIDs();
                Integer lastId = assetIds.getLast();
                LinkedList<LinkedList<Integer>> chunks = ChunkUtils.chunk(assetIds, CHUNK_SIZE);
                String companyID = ServerSecurityContext.getInstance().getCompanyId();
                String dbName = SecurityContext.getInstance().getDatabase();
                List<CompletableFuture<Void>> futures = chunks.stream()
                        .map(chunk -> CompletableFuture.runAsync(() -> rabbitMQService.fixedAssetsDeprecationMQ(
                                new DeprecationItemMQ(user.getObjectID(), companyID, dbName,
                                        monthEndDate != null ? monthEndDate.getNonConvertedDate() : null, statusFile.getObjectID(), chunk, chunk.contains(lastId))
                        ), executor)).toList();

                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
                return false;
            }
        }
        return true;
    }

    public static class ChunkUtils {

        public static <T> LinkedList<LinkedList<T>> chunk(LinkedList<T> list, int chunkSize) {

            LinkedList<LinkedList<T>> chunks = new LinkedList<>();

            for (int i = 0; i < list.size(); i += chunkSize) {
                chunks.add(new LinkedList<>( list.subList(i, Math.min(i + chunkSize, list.size()))));
            }

            return chunks;
        }
    }

    @Override
    public void updatePurchaseInvoiceRelatedFixedAsset(Integer objectID, boolean delete) {
        EdsFixedAsset item = fixedAssetManager.getFixedAssetByPurchaseInvoice(objectID);

        if (item != null) {
            EdsFixedAssetTransaction fixedAssetTransaction = transactionManager.getFixedAssetTransaction(item.getObjectID());
            item.setDeleted(true);
            if (delete && fixedAssetTransaction != null) {
                fixedAssetTransaction.setDeleted(true);
                transactionManager.update(fixedAssetTransaction);
            }

            fixedAssetManager.createOrUpdate(item);
        }
    }

    @Override
    public void deleteSalesInvoiceRelatedFixedAsset(Integer invoiceID) {
        EdsFixedAsset item = fixedAssetManager.getFixedAssetBySalesInvoice(invoiceID);
        if (item != null) {
            EdsDisposalTransaction disposalTransaction = transactionManager.getFixedAssetDisposalTransaction(item.getObjectID());
            if (disposalTransaction != null) {
                disposalTransaction.setDeleted(true);
                transactionManager.update(disposalTransaction);
                //todo update deprecations
            }

            item.setSalesInvoice(null);
            item.setDisposed(false);
            item.setDisposedDate(null);
            fixedAssetManager.createOrUpdate(item);
        }
    }

    @Override
    public void voidSalesInvoiceRelatedFixedAsset(Integer invoiceID, DateNonConvertable voidDate) {
        if (voidDate == null) {
            return;
        }
        EdsFixedAsset fixedAsset = fixedAssetManager.getFixedAssetBySalesInvoice(invoiceID);
        if (fixedAsset != null) {
            EdsDisposalTransaction disposalTransaction = transactionManager.getFixedAssetDisposalTransaction(fixedAsset.getObjectID());
            if (disposalTransaction != null) {
                EdsDisposalTransaction transaction = new EdsDisposalTransaction(); //creating new transaction
                transaction.setName(disposalTransaction.getName());
                transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);

                transaction.setJournalDate(voidDate.getNonConvertedDate());
                transaction.setPostedBy(transactionManager.getUser());
                transaction.setPostedDate(transactionManager.getUser().getUserDate());
                transaction.setReversalTransaction(disposalTransaction);
                transaction.setFixedAsset(fixedAsset);
                transaction.setClient(disposalTransaction.getClient());
                transaction.setSupplier(disposalTransaction.getSupplier());

                if (disposalTransaction.getTransactionItems() != null && !disposalTransaction.getTransactionItems().isEmpty()) {
                    EdsTransactionItem item;
                    for (EdsTransactionItem disposalTransItem : disposalTransaction.getTransactionItems()) {
                        item = new EdsTransactionItem();
                        item.setAccount(disposalTransItem.getAccount());
                        item.setDepartment(disposalTransItem.getDepartment());
                        item.setProject(disposalTransItem.getProject());
                        item.setDebit(disposalTransItem.getCredit());
                        item.setCredit(disposalTransItem.getDebit());
                        item.setForeignDebit(disposalTransItem.getForeignCredit());
                        item.setForeignCredit(disposalTransItem.getForeignDebit());
                        item.setCrmAccount(disposalTransItem.getCrmAccount());
                        transaction.addTransactionItem(item);
                    }
                }
                transactionManager.create(transaction);
                //todo update deprecations
            }

            fixedAsset.setSalesInvoice(null);
            fixedAsset.setDisposed(false);
            fixedAsset.setDisposedDate(null);
            fixedAssetManager.createOrUpdate(fixedAsset);
        }
    }
}
