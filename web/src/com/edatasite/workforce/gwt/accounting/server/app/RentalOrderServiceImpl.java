package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.EdsRentalOrderHistory;
import com.edatasite.workforce.core.domain.EdsRentalOrderItem;
import com.edatasite.workforce.core.domain.EdsRentalProductItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsRentalOrderCustomFields;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderItemManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BrandManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.RentalOrderCFManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Transactional
@Service("rentalOrderService")
public class RentalOrderServiceImpl implements RentalOrderService, RentalOrderServiceLocal, Constants, AccountingConstants {
    private static final Logger log = LoggerFactory.getLogger(RentalOrderServiceImpl.class);

    private final AllInOneService allInOneService;
    private final ApproverManager approverManager;
    private final BaseEventsPostProcessor baseEventPostProcessor;
    private final BrandManager brandManager;
    private final CompanyPdfTemplateManager companyPdfTemplateManager;
    private final CrmAccountManager crmAccountManager;
    private final EmployeeManager employeeManager;
    private final FinancialSettingsManager financialSettingsManager;
    private final ItemManager itemManager;
    private final ItemTableSettingService itemTableSettingService;
    private final NumberingSettingsManager numberingSettingsManager;
    private final ProductCategoryManager productCategoryManager;
    private final ReferenceManager referenceManager;
    private final RelationManager relationManager;
    private final RentalOrderCFManager rentalOrderCFManager;
    private final RentalOrderHistoryManager rentalOrderHistoryManager;
    private final RentalOrderItemManager rentalOrderItemManager;
    private final RentalOrderManager rentalOrderManager;
    private final UserManager userManager;
    private final VatManager vatManager;
    private final WfmMessageSource wfmMessageSource;
    private final WfmMessageSource commonLocalizer;
    private final CommonServiceLocal commonServiceLocal;
    private final CommonService commonService;

    @Autowired
    public RentalOrderServiceImpl(RentalOrderCFManager rentalOrderCFManager, AllInOneService allInOneService, ApproverManager approverManager, BaseEventsPostProcessor baseEventPostProcessor, BrandManager brandManager, CompanyPdfTemplateManager companyPdfTemplateManager, CrmAccountManager crmAccountManager, EmployeeManager employeeManager, FinancialSettingsManager financialSettingsManager, ItemManager itemManager, ItemTableSettingService itemTableSettingService, NumberingSettingsManager numberingSettingsManager, ProductCategoryManager productCategoryManager, ReferenceManager referenceManager, RelationManager relationManager, RentalOrderHistoryManager rentalOrderHistoryManager, RentalOrderItemManager rentalOrderItemManager, RentalOrderManager rentalOrderManager, UserManager userManager, VatManager vatManager, CommonService commonService, @Qualifier("referenceWfmMessageSource") WfmMessageSource wfmMessageSource, @Qualifier("commonLocalizer") WfmMessageSource commonLocalizer, @Qualifier("commonService") CommonServiceLocal commonServiceLocal) {
        this.rentalOrderCFManager = rentalOrderCFManager;
        this.allInOneService = allInOneService;
        this.approverManager = approverManager;
        this.baseEventPostProcessor = baseEventPostProcessor;
        this.brandManager = brandManager;
        this.companyPdfTemplateManager = companyPdfTemplateManager;
        this.crmAccountManager = crmAccountManager;
        this.employeeManager = employeeManager;
        this.financialSettingsManager = financialSettingsManager;
        this.itemManager = itemManager;
        this.itemTableSettingService = itemTableSettingService;
        this.numberingSettingsManager = numberingSettingsManager;
        this.productCategoryManager = productCategoryManager;
        this.referenceManager = referenceManager;
        this.relationManager = relationManager;
        this.rentalOrderHistoryManager = rentalOrderHistoryManager;
        this.rentalOrderItemManager = rentalOrderItemManager;
        this.rentalOrderManager = rentalOrderManager;
        this.userManager = userManager;
        this.vatManager = vatManager;
        this.commonService = commonService;
        this.wfmMessageSource = wfmMessageSource;
        this.commonLocalizer = commonLocalizer;
        this.commonServiceLocal = commonServiceLocal;
    }

    @Override
    public RentalOrderData getRentalOrderData(Integer objectID, boolean isCopy) {
        EdsRentalOrder edsRentalOrder = rentalOrderManager.get(objectID);
        RentalOrderData dto = new RentalOrderData();
        EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();

        if (edsRentalOrder != null) {
            dto = edsRentalOrder.createRentalOrderData();
            EdsCrmAccount clientBase = crmAccountManager.get(edsRentalOrder.getCustomer().getObjectID());
            Integer currID = clientBase.getCurrency() == null ? null : clientBase.getCurrency().getObjectID();
            dto.setSupplierCustomerBalance(crmAccountManager.getClientBalance(edsRentalOrder.getCustomer().getObjectID(), baseCurrency.getObjectID().equals(currID)).doubleValue());
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.RentalOrdersView);
            dto.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsRentalOrder.getCustomFields(), customFieldsItems));
            dto.setTemplates(getPdfTemplates(PdfReferenceCodeNameEnum.RENTAL_ORDER.name()).getItems());
            dto.setRelationItems(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_RENTAL_ORDER, objectID)));
            if (!CollectionUtils.isEmpty(edsRentalOrder.getItems())) {
                dto.setRentalOrderItems(edsRentalOrder.getItems().stream().map(EdsRentalOrderItem::toDTO).collect(Collectors.toCollection(ArrayList::new)));
            }
        } else {
            dto.setNumberData(generateRentalOrderNumber());
        }
        dto.setItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.RENTAL_ORDER_ITEM));
        dto.setProductCategories((TreeSelectItem[]) getCategoriesAsSelectItem());
        dto.setProductBrands(getBrandsAsSelectItem());
        dto.setApproveProcessEnabled(approverManager.isExistApproverByEntityType(RelationItem.TYPE_RENTAL_ORDER));
        return dto;
    }

    @Override
    public SelectItem saveRentalOrder(RentalOrderData data) {
        NumberData numberData = data.getNumberData();
        EdsUser user = userManager.getUser();
        if (data.getObjectID() == null && (numberData == null || numberData.getNumberString() == null || numberData.getNumberString().trim().isEmpty())) {
            return new SelectItem(-1, "");
        }
        EdsRentalOrder edsRentalOrder;
        boolean isEdit = true;
        if (data.getObjectID() == null) {
            edsRentalOrder = new EdsRentalOrder();
            edsRentalOrder.setCreator(user);
            edsRentalOrder.setCreatedDate(new Date());
            isEdit = false;
        } else {
            rentalOrderManager.deleteRentalOrderItems(data.getObjectID());
            edsRentalOrder = rentalOrderManager.get(data.getObjectID());
        }
        edsRentalOrder.setUpdatedDate(new Date());
        edsRentalOrder.setStartDate(data.getStartDate());
        edsRentalOrder.setExpirationDate(data.getExpirationDate());

        if (rentalOrderManager.isRentOrderNumberExist(data.getNumberData().getNumberString(), data.getObjectID())) {
            data.setNumberData(generateRentalOrderNumber());
        }

        edsRentalOrder.setNumber(data.getNumberData().getNumberString());
        edsRentalOrder.setIntNumber(data.getNumberData().getIntNumber());

        if (data.getCustomer() != null) {
            edsRentalOrder.setCustomer(crmAccountManager.get(data.getCustomer().getId()));
        }
        edsRentalOrder.setSubTotal(data.getSubTotal());
        edsRentalOrder.setTotal(data.getTotal());
        edsRentalOrder.setTaxAmount(data.getTaxAmount());
        edsRentalOrder.setStatus(referenceManager.findReference(Constants.RENTAL_STATUS, data.getStatusCode()));
        rentalOrderManager.createOrUpdate(edsRentalOrder);
        saveRentalOrderHistory(edsRentalOrder.getObjectID(), new HistoryListItem(!isEdit ? commonLocalizer.localize("created") : commonLocalizer.localize("modifiedDate")));

        EdsRentalOrderCustomFields customFields = createRentalOrderCustomFields(data.getCustomFieldItems());
        edsRentalOrder.setCustomFields(customFields);
        for (RentalOrderItem item : data.getRentalOrderItems()) {
            if (item.getRentalItem() != null) {
                EdsRentalOrderItem edsRentOrderItem = new EdsRentalOrderItem();

                edsRentOrderItem.setRentalOrder(edsRentalOrder);
                if (item.getRentalItem() != null && item.getRentalItem().getId() != null) {
                    EdsItem rentalItem = this.itemManager.getItem(item.getRentalItem().getId());
                    edsRentOrderItem.setRentalItem(rentalItem);
                }
                edsRentOrderItem.setQty(item.getQty());
                edsRentOrderItem.setPrice(item.getPrice());
                edsRentOrderItem.setNet(item.getNetAmount());
                edsRentOrderItem.setSubTotal(item.getSubTotal());
                edsRentOrderItem.setDescription(item.getDescription());
                String[] split = item.getDescription().split("->");
                if (split.length > 1) {
                    ZoneId zone = ZoneId.of(user.getCompany().getCountryZone().getZone().getZoneID());
                    Date fromDate = Date.from(LocalDateTime.parse(split[0].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).atZone(zone).toInstant());
                    Date toDate = Date.from(LocalDateTime.parse(split[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).atZone(zone).toInstant());
                    edsRentOrderItem.setFromDate(fromDate);
                    edsRentOrderItem.setToDate(toDate);
                }
                if (item.getTaxItem() != null) {
                    edsRentOrderItem.setVat(vatManager.get(item.getTaxItem().getId()));
                }
                rentalOrderItemManager.create(edsRentOrderItem);
            }
        }

        if (!isOk(data.getApprovers())) {
            edsRentalOrder.setEntityStatus(referenceManager.findReference(Constants.RENTAL_STATUS, RENTAL_APPROVED));
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsRentalOrder.class.getSimpleName());
        if (edsRentalOrder.getObjectID() != null) {
            kpiLog.setEntityId(edsRentalOrder.getObjectID());
        }
        EdsBusinessEvent rentalOrderWorkflow = null;
        if (isEdit) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add new rental order");
            rentalOrderWorkflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsRentalOrder, user);
            rentalOrderWorkflow.setEntityType(RelationItem.TYPE_RENTAL_ORDER);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update rental order");
            rentalOrderWorkflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsRentalOrder, user);
            rentalOrderWorkflow.setEntityType(RelationItem.TYPE_RENTAL_ORDER);
        }

        if (isOk(data.getApprovers())) {
            data.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : data.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (edsRentalOrder.getCurrentApprover() != null && data.getStatusCode() != null && isFirstApprover) {
                        edsRentalOrder.getCurrentApprover().setStatus(referenceManager.findReference(Constants.RENTAL_STATUS, data.getStatusCode()));
                        edsRentalOrder.setEntityStatus(referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_SUBMITTED));
                        isFirstApprover = false;
                    } else if (edsRentalOrder.getCurrentApprover() != null && data.getStatusCode() != null) {
                        edsRentalOrder.getCurrentApprover().setStatus(referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_SUBMITTED));
                    }
                    if (data.getStatusCode() != null && !Constants.RENTAL_APPROVED.equals(data.getStatusCode())) {
                        edsRentalOrder.setEntityStatus(referenceManager.findReference(Constants.RENTAL_STATUS, data.getStatusCode()));
                    }
                    if (edsRentalOrder.isCurrentApproverRejected()) {
                        edsRentalOrder.setEntityStatus(edsRentalOrder.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(edsRentalOrder.getObjectID());
                edsApprover.setIs_default(false);

                if (data.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.RENTAL_STATUS, data.getStatusCode()));
                    edsRentalOrder.setEntityStatus(referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_SUBMITTED));
                    isFirstApprover = false;
                } else if (data.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_SUBMITTED));
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
                if (edsRentalOrder.getCurrentApprover() == null) {
                    edsRentalOrder.setCurrentApprover(edsApprover);
                }
                edsRentalOrder.getApprovers().add(edsApprover);
            }

            /* Run a workflow approval process */
            EdsBusinessEvent approvingWorkflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsRentalOrder, user);
            approvingWorkflowEvent.setEntityType(RelationItem.TYPE_RENTAL_ORDER);
        }
        return new SelectItem(edsRentalOrder.getObjectID(), edsRentalOrder.getNumber());
    }

    @Override
    public Boolean deleteRentalOrder(Integer rentalOrderId) {
        if (rentalOrderId != null) {
            EdsRentalOrder edsRentalOrder = rentalOrderManager.get(rentalOrderId);
            if (edsRentalOrder != null) {
                edsRentalOrder.setDeleted(true);
                edsRentalOrder.getItems().clear();
                rentalOrderManager.update(edsRentalOrder);

                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsRentalOrder.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.DELETE);
                kpiLog.setEntityId(rentalOrderId);
                ServerUtils.kpiLog(log, kpiLog, "Delete rental order");
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsRentalOrder, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_RENTAL_ORDER);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
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

    @Override
    public NumberData generateRentalOrderNumber() {
        Integer intNumber = rentalOrderManager.getOrderLastIntNumber();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings != null && settings.getRentalOrderNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getRentalOrderNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_RENTAL_ORDER_PREFIX);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TreeSelectItem[] getCategoriesAsSelectItem() {
        List<EdsProductCategory> categories = productCategoryManager.getParentCategories();
        if (categories == null || categories.isEmpty()) {
            return new TreeSelectItem[0];
        } else {
            return wrapCategoriesAsTreeSelectItems(categories);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBrandsAsSelectItem() {
        List<EdsBrand> brands = brandManager.getBrandList(null);
        if (brands == null || brands.isEmpty()) {
            return new SelectItem[0];
        } else {
            SelectItem[] result = new SelectItem[brands.size()];
            int i = 0;
            for (EdsBrand brand : brands) {
                result[i] = new SelectItem(brand.getObjectID(), brand.getName());
                i++;
            }
            return result;
        }
    }

    private TreeSelectItem[] wrapCategoriesAsTreeSelectItems(List<EdsProductCategory> categories) {
        List<TreeSelectItem> categoryItemList = new ArrayList<>();

        if (categories != null && !categories.isEmpty()) {
            for (EdsProductCategory category : categories) {
                generateRecursiveCategoryItem(category, categoryItemList);
            }
        }

        return categoryItemList.toArray(new TreeSelectItem[]{});
    }

    private void generateRecursiveCategoryItem(EdsProductCategory category, List<TreeSelectItem> categoryItems) {
        TreeSelectItem categoryItem = wrapCategory(category);
        if (category.getChildList() != null && !category.getChildList().isEmpty()) {
            for (EdsProductCategory child : category.getChildList()) {
                if (child.isActive()) {
                    generateRecursiveCategoryItem(child, categoryItem.getChildren());
                }
            }
        }
        categoryItems.add(categoryItem);
    }

    private TreeSelectItem wrapCategory(EdsProductCategory category) {
        TreeSelectItem item = new TreeSelectItem();
        item.setId(category.getObjectID());
        item.setName(category.getName());
        item.setDefaultSelected(false);
        item.setShowInDropDown(true);
        if (category.getParent() != null) {
            item.setParent(wrapCategory(category.getParent()));
        }

        return item;
    }


    @Override
    public Integer saveRentalOrderHistory(Integer rentalOrderId, HistoryListItem hisItem) {
        if (rentalOrderId != null && hisItem != null) {
            EdsUser user = userManager.getUser();
            if (user instanceof EdsEmployee) {
                user = userManager.get(user.getObjectID());
            }
            EdsRentalOrderHistory rentalOrderHistory = new EdsRentalOrderHistory();
            rentalOrderHistory.setRentalOrder(rentalOrderManager.get(rentalOrderId));
            rentalOrderHistory.setCreationDate(new Date());
            rentalOrderHistory.setUser(user);
            rentalOrderHistory.setSuperUser(ServerUtils.isSuperUser());
            rentalOrderHistory.setText(hisItem.getComment());

            rentalOrderHistoryManager.create(rentalOrderHistory);
            return rentalOrderHistory.getObjectID();
        }
        return null;
    }

    public List<HistoryNote> loadRentalOrderHistory(Integer rentalOrderId) {
        List<EdsRentalOrderHistory> historyList = rentalOrderHistoryManager.getComments(rentalOrderId);
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        List<HistoryNote> noteItemsList = new ArrayList<>();
        for (EdsRentalOrderHistory item : historyList) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(item.getText())) {
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

    public void deleteRentalOrderComment(Integer commentID) {
        EdsRentalOrderHistory rentalOrderNote = rentalOrderHistoryManager.get(commentID);
        rentalOrderHistoryManager.delete(rentalOrderNote);
    }

    @Override
    public void saveProductForRentItemToRentalOrder(Integer rentalOrderId, HashMap<Integer, Integer> rentalProductsIds) {
        EdsRentalOrder edsRentalOrder = rentalOrderManager.get(rentalOrderId);
        for (EdsRentalOrderItem item : edsRentalOrder.getItems()) {
            item.setProductItem(itemManager.get(rentalProductsIds.get(item.getObjectID())));
            rentalOrderItemManager.update(item);
        }
    }

    @Override
    public void updateStatusRentalOrder(Integer rentalOrderId, String statusCode, SelectItem invoiceItem) {
        EdsRentalOrder edsRentalOrder = rentalOrderManager.get(rentalOrderId);

        if (edsRentalOrder != null) {
            final EdsUser user = this.employeeManager.getUser();
            final EdsReference edsReference = this.referenceManager.findReference(Constants.RENTAL_STATUS, statusCode);

            if (!Constants.RENTAL_APPROVED.equals(edsReference.getCode())) {
                edsRentalOrder.setOverallStatus(edsReference);
                edsRentalOrder.setStatus(edsReference);
            }
            edsRentalOrder.updateStatus(edsReference);
            if (Constants.RENTAL_REJECTED.equals(statusCode)) {
                edsRentalOrder.setOverallStatus(this.referenceManager.findReference(Constants.RENTAL_STATUS, Constants.RENTAL_REJECTED));
            }

            rentalOrderManager.update(edsRentalOrder);
            if (!edsReference.getCode().equals(Constants.RENTAL_INVOICED)) {
                saveRentalOrderHistory(edsRentalOrder.getObjectID(), new HistoryListItem(edsReference.getCode().equals(RENTAL_APPROVED) ? "approved" : "rejected"));
            } else {
                saveRentalOrderHistory(edsRentalOrder.getObjectID(), new HistoryListItem("invoiced"));
            }

            if (invoiceItem != null) {
                edsRentalOrder.setInvoiceID(invoiceItem.getId());
                edsRentalOrder.setInvoiceName(invoiceItem.getName());
                ArrayList<RelationItem> relation = new ArrayList<>(1);
                relation.add(new RelationItem(null, invoiceItem.getId(), RelationItem.TYPE_SALEINVOICE, invoiceItem.getName(), rentalOrderId, RelationItem.TYPE_RENTAL_ORDER, (commonLocalizer.localize("rentalOrder") + " " + edsRentalOrder.getNumber())));
                allInOneService.saveRelations(RelationItem.TYPE_RENTAL_ORDER, rentalOrderId, (commonLocalizer.localize("rentalOrder") + " " + edsRentalOrder.getNumber()), relation, false);
            }

            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsRentalOrder, user);
            workflowEvent.setEntityType(RelationItem.TYPE_RENTAL_ORDER);
            EdsBusinessEvent workflowEventEdit = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsRentalOrder, user);
            workflowEventEdit.setEntityType(RelationItem.TYPE_RENTAL_ORDER);
        }
    }

    @Override
    public EdsRentalOrderCustomFields createRentalOrderCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            EdsRentalOrderCustomFields rentalOrderCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                rentalOrderCustomFields = rentalOrderCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue())) || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0) || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                rentalOrderCustomFields = new EdsRentalOrderCustomFields();
                rentalOrderCFManager.create(rentalOrderCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(rentalOrderCustomFields, customFieldItems);
            return rentalOrderCustomFields;
        }
        return null;
    }

    @Override
    public ListResult<RentalOrderData> getRentalOrderList(ListingFilterParameter fp) {
        ListPanelToolRpc panelSettings = fp.getListPanelTool();
        List<EdsRentalOrder> result = rentalOrderManager.getRentalOrderList(fp);
        Integer total = rentalOrderManager.getRentalOrderCount(fp);

        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        String rentOrderNumberingFormat = settings != null ? settings.getRentalOrderNumberingFormat() : null;

        ArrayList<RentalOrderData> list = new ArrayList<>();
        for (EdsRentalOrder edsRentalOrder : result) {
            RentalOrderData rentalOrderData = edsRentalOrder.createRentalOrderData();
            if (edsRentalOrder.getNumber() != null) {
                rentalOrderData.setNumberData(new NumberData(edsRentalOrder.getNumber(), edsRentalOrder.getIntNumber()));
                rentalOrderData.getNumberData().setNumberFormat(rentOrderNumberingFormat);
                rentalOrderData.setNumber(rentalOrderData.getNumberData().getNumberString());
            }
            if (panelSettings != null) {
                HashMap<String, Object> map = CustomFieldsUtils.getRPCCustomFields(edsRentalOrder.getCustomFields(), panelSettings.getColumnCodeName());
                rentalOrderData.setCustomFieldValuesItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
            }
            rentalOrderData.setCustomer(edsRentalOrder.getCustomer() != null ? edsRentalOrder.getCustomer().getAsSelectItem() : null);
            list.add(rentalOrderData);
        }
        return new ListResult<>(list, total);
    }

    private long monthCount = 0;
    private long weekCount = 0;
    private long dayCount = 0;
    private long hourCount = 0;

    @Override
    public BigDecimal calculateRentalMinPrice(Integer productId, DateNonConvertable startDate, DateNonConvertable endDate) {
        BigDecimal price = BigDecimal.ZERO;
        EdsItem rentalProduct = itemManager.get(productId);
        if (rentalProduct == null || CollectionUtils.isEmpty(rentalProduct.getRentalItems())) return price;

        BigDecimal hourPrice = BigDecimal.ZERO;
        BigDecimal dayPrice = BigDecimal.ZERO;
        BigDecimal weekPrice = BigDecimal.ZERO;
        BigDecimal monthPrice = BigDecimal.ZERO;
        List<EdsRentalProductItem> rentalProductItems = rentalProduct.getRentalItems();
        for (EdsRentalProductItem edsRentalProductItem : rentalProductItems) {
            if (!StringUtils.isEmpty(edsRentalProductItem.getUnitCode())) {
                switch (edsRentalProductItem.getUnitCode()) {
                    case TIME_GRANULARITY.MONTHS -> monthPrice = edsRentalProductItem.getPrice();
                    case TIME_GRANULARITY.WEEKS -> weekPrice = edsRentalProductItem.getPrice();
                    case TIME_GRANULARITY.DAYS -> dayPrice = edsRentalProductItem.getPrice();
                    case TIME_GRANULARITY.HOURS -> hourPrice = edsRentalProductItem.getPrice();
                }
            }
        }

        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startDate.getNonConvertedDate());
        int dayOfMonth = startTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar endTimeCalendar = Calendar.getInstance();
        endTimeCalendar.setTime(endDate.getNonConvertedDate());

        long differenceByMin = ChronoUnit.MINUTES.between(startDate.getNonConvertedDate().toInstant(), endDate.getNonConvertedDate().toInstant());

        monthCount = 0;
        weekCount = 0;
        dayCount = 0;
        hourCount = 0;

        if (differenceByMin > 0 && monthPrice.compareTo(BigDecimal.ZERO) > 0) {

            monthCount = differenceByMin / (60L * 24 * dayOfMonth);
            differenceByMin = differenceByMin - monthCount * 60 * 24 * dayOfMonth;

            if (differenceByMin > 0 && weekPrice.compareTo(BigDecimal.ZERO) > 0 && (dayPrice.compareTo(BigDecimal.ZERO) == 0 && hourPrice.compareTo(BigDecimal.ZERO) == 0 || weekPrice.compareTo(dayPrice.multiply(BigDecimal.valueOf(7))) <= 0 || weekPrice.compareTo(hourPrice.multiply(BigDecimal.valueOf(168))) <= 0)) {

                differenceByMin = calculateByWeekPrice(hourPrice, dayPrice, weekPrice, differenceByMin);
            }

            if (differenceByMin > 0 && dayPrice.compareTo(BigDecimal.ZERO) > 0) {
                differenceByMin = calculateByDayPrice(hourPrice, dayPrice, weekPrice, differenceByMin);
            }

            if (differenceByMin > 0 && hourPrice.compareTo(BigDecimal.ZERO) > 0) {
                differenceByMin = calculateByHourPrice(differenceByMin);
            }

            if (differenceByMin > 0) {
                monthCount += 1;
            }
        } else if (differenceByMin > 0 && weekPrice.compareTo(BigDecimal.ZERO) > 0 && (dayPrice.compareTo(BigDecimal.ZERO) == 0 && hourPrice.compareTo(BigDecimal.ZERO) == 0 || weekPrice.compareTo(dayPrice.multiply(BigDecimal.valueOf(7))) <= 0 || weekPrice.compareTo(hourPrice.multiply(BigDecimal.valueOf(168))) <= 0)) {

            calculateByWeekPrice(hourPrice, dayPrice, weekPrice, differenceByMin);
        } else if (differenceByMin > 0 && dayPrice.compareTo(BigDecimal.ZERO) > 0) {
            differenceByMin = calculateByDayPrice(hourPrice, dayPrice, weekPrice, differenceByMin);
        } else {
            differenceByMin = calculateByHourPrice(differenceByMin);
        }

        if (monthCount > 0) {
            price = monthPrice.multiply(new BigDecimal(monthCount));
        }
        if (weekCount > 0) {
            price = price.add(weekPrice.multiply(new BigDecimal(weekCount)));
        }
        if (dayCount > 0) {
            price = price.add(dayPrice.multiply(new BigDecimal(dayCount)));
        }
        if (hourCount > 0) {
            price = price.add(hourPrice.multiply(new BigDecimal(hourCount)));
        }

        return price;
    }

    private long calculateByWeekPrice(BigDecimal hourPrice, BigDecimal dayPrice, BigDecimal weekPrice, long differenceByMin) {
        weekCount = differenceByMin / (60 * 24 * 7);
        differenceByMin = differenceByMin - weekCount * 60 * 24 * 7;

        if (differenceByMin > 0 && dayPrice.compareTo(BigDecimal.ZERO) > 0) {
            differenceByMin = calculateByDayPrice(hourPrice, dayPrice, weekPrice, differenceByMin);
        }
        if (differenceByMin > 0 && hourPrice.compareTo(BigDecimal.ZERO) > 0) {
            differenceByMin = calculateByHourPrice(differenceByMin);
        }

        if (differenceByMin > 0) {
            weekCount += 1;
        }
        return differenceByMin;
    }

    private long calculateByDayPrice(BigDecimal hourPrice, BigDecimal dayPrice, BigDecimal weekPrice, long differenceByMin) {
        boolean checkToHour = true;
        if (weekPrice.compareTo(BigDecimal.ZERO) > 0 && hourPrice.compareTo(BigDecimal.ZERO) > 0 && hourPrice.multiply(new BigDecimal(24)).compareTo(dayPrice) < 0) {
            BigDecimal calHourPrice = BigDecimal.ZERO;
            long tempdifferenceByMin = differenceByMin;
            long tempHourCount = tempdifferenceByMin / 60;
            tempdifferenceByMin = tempdifferenceByMin - tempHourCount * 60;
            if (tempdifferenceByMin > 0) {
                tempHourCount += 1;
            }
            calHourPrice = hourPrice.multiply(new BigDecimal(tempHourCount));

            long tempdifferenceByMin2 = differenceByMin;
            long tempDayCount = tempdifferenceByMin2 / (60 * 24);
            tempdifferenceByMin2 = tempdifferenceByMin2 - tempDayCount * 60 * 24;

            if (dayPrice.compareTo(hourPrice.multiply(new BigDecimal(tempdifferenceByMin2).divide(new BigDecimal(60), RoundingMode.HALF_UP))) < 0) {
                dayCount += 1;
                tempdifferenceByMin2 = 0;
            }
            if (tempDayCount <= 7 && dayPrice.multiply(new BigDecimal(tempDayCount)).compareTo(weekPrice) > 0 && calHourPrice.compareTo(weekPrice) > 0) {
                checkToHour = false;
            }
        }

        if (checkToHour && hourPrice.compareTo(BigDecimal.ZERO) > 0 && hourPrice.multiply(new BigDecimal(24)).compareTo(dayPrice) < 0) {
            hourCount = differenceByMin / 60;
            differenceByMin = differenceByMin - hourCount * 60;
            if (differenceByMin > 0) {
                hourCount += 1;
            }
            differenceByMin = 0;
        } else {
            dayCount = differenceByMin / (60 * 24);
            differenceByMin = differenceByMin - dayCount * 60 * 24;

            if (dayPrice.compareTo(hourPrice.multiply(new BigDecimal(differenceByMin).divide(new BigDecimal(60), RoundingMode.HALF_UP))) < 0) {
                dayCount += 1;
                differenceByMin = 0;
            }

            if (weekPrice.compareTo(BigDecimal.ZERO) > 0 && dayCount <= 7 && dayPrice.multiply(new BigDecimal(dayCount)).compareTo(weekPrice) > 0) {
                dayCount = 0;
                weekCount += 1;
            }
        }
        return differenceByMin;
    }

    private long calculateByHourPrice(long differenceByMin) {
        hourCount += differenceByMin / 60;
        differenceByMin = differenceByMin - hourCount * 60;
        if (differenceByMin > 0) {
            hourCount += 1;
        }
        differenceByMin = 0;
        return differenceByMin;
    }
}
