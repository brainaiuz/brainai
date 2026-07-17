package com.edatasite.workforce.rest.v3.release10.crm.service;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.EdsOpportunityItem;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BrandManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunitiesList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.OpportunityTO;
import com.edatasite.workforce.rest.v2.release10.enums.TaxTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.OpportunityDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.OpportunityItemDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.contact.OpportunityByStageTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_OPPORTUNITY;

/**
 * Created by Normurod Buriev.
 * Date: 3/24/2021 5:48 PM
 */
@Service("apiOpportunityService")
public class ApiOpportunityService implements ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(ApiOpportunityService.class);

    private static final ThreadLocal<ArrayList<CompanyCustomFieldItem>> opportunityLineItemCustomFields = new ThreadLocal<>();
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager contactManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private BrandManager brandManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private CRMService crmService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private XSync<String> stringXSync;
    @Autowired
    private UserManager userManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private TaskService taskService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Transactional(readOnly = true)
    public ListResultTO<OpportunityDto> getList(ListingFilterParameter filterParameter) {
        if (filterParameter.getCurrentPage() != null
                && filterParameter.getLimit() != null) {
            filterParameter.setStart(filterParameter.getCurrentPage() * filterParameter.getLimit());
        }
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_OPPORTUNITY_CORE);
        QueryResponse resp = null;
        try {
            FacetFilterRpc opportunityFacetFilter = filterParameter.getFacetFilter();
            if (opportunityFacetFilter != null && !opportunityFacetFilter.isFilterChanges()) {
                opportunityFacetFilter = commonService.getUserFacetFilter(opportunityFacetFilter);
            }
            String mainSolrQuery = crmServiceLocal.getOpportunityFacetQuery(filterParameter, opportunityFacetFilter);

            SelectItem[] stages = crmService.getOpportunityStages(false);
            StringBuilder solrQuery = new StringBuilder();
            solrQuery.append(" AND ( (-").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append("[* TO *] AND *:*)");
            if (stages != null) {
                for (SelectItem stage : stages) {
                    solrQuery.append(" OR ").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append(stage.getId());
                }
            }
            solrQuery.append(" ) ");
            mainSolrQuery += solrQuery.toString();
            resp = server.query(crmServiceLocal.getOpportunitySolrQuery(filterParameter, mainSolrQuery), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getOpporunityFromSolrResult(resp);
    }

    @Transactional(readOnly = true)
    public OpportunityDto get(Integer id) {
        OpportunityDto opportunityDto = wrapOpportunityToDto(opportunityManager.get(id), null);
        OpportunityListItem opportunity = crmService.getOpportunity(id);

        Optional.ofNullable(opportunity)
                .map(OpportunityListItem::getContactItem)
                .map(ConvertUtils::toOpportunityContactTo)
                .ifPresent(opportunityDto::setOpportunityContacts);

        List<AttachmentTO> attachments = Optional.ofNullable(documentsService.getFileResources(F_OPPORTUNITY, id, id))
                .orElse(new ArrayList<>())
                .stream()
                .map(file -> new AttachmentTO(file.getFileName(), file.getDownloadUrl()))
                .toList();
        opportunityDto.setAttachments(attachments);

        List<NoteDto> notes = Optional.ofNullable(allInOneService.getNotes(id, CrmConstants.CRM_OPPORTUNITY))
                .orElse(new ArrayList<>())
                .stream()
                .map(ConvertUtils::toDto)
                .toList();
        opportunityDto.setNotes(notes);
        return opportunityDto;
    }

    public Integer save(OpportunityDto dto) throws RestException {
        boolean isNew = dto.getId() == null;

        if (!isNew) {
            Optional.ofNullable(opportunityManager.get(dto.getId())).orElseThrow(() -> new RestException(ERROR, "Opportunity is not found by given Id.", INVALID, HttpStatus.BAD_REQUEST));
        }
        OpportunityListItem obj = wrapDtoToModel(dto);
        obj.setObjectId(!isNew ? dto.getId() : null);
        return crmService.saveOpportunity(obj);
    }

    public List<OpportunityTO> getOppotunityKanbanItemsByStatus(ListingFilterParameter fp, Integer statusId) {
        EdsReference status = referenceManager.get(statusId);

        OpportunitiesList<OpportunityListItem> opportunityKanbanItem =
                crmService.getNewKanbanOpportunities(fp, status.getAsSelectItem());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<OpportunityTO> opportunityTOS = new ArrayList<>();

        for (OpportunityListItem item : opportunityKanbanItem.getList()) {
            OpportunityTO opportunityTO = new OpportunityTO();
            opportunityTO.setName(item.getOpportunityName());

            if (item.getContact() != null) {
                IdName contactDTO = new IdName();
                contactDTO.setName(item.getContact());
                contactDTO.setId(item.getContactId());
                contactDTO.addProperty("email", item.getContactPrimaryEmail());
                contactDTO.addProperty("phone", item.getContactPrimaryPhone());
                opportunityTO.setContact(contactDTO);
            }

            opportunityTO.setAmmount(item.getAmount());
            opportunityTO.setCurrency(item.getCurrency());
            opportunityTO.setClosing_date(item.getClosingDate());
            opportunityTO.setClosing_date_Id(item.getClosingDateID());

            Optional.ofNullable(item.getCrmAccountItem())
                    .ifPresent(crmAccount -> opportunityTO.setCustomer(
                            new ItemDto(crmAccount.getObjectId(), crmAccount.getName(), crmAccount.getNumber())));

            opportunityTO.setItem_id(item.getObjectId());
            opportunityTO.setStatus_id(item.getStage() != null ? item.getStage().getId() : 0);

            if (item.getCreatedDate() != null) {
                opportunityTO.setDate_added(dateFormat.format(item.getCreatedDate()));
            }

            opportunityTOS.add(opportunityTO);
        }

        return opportunityTOS;
    }

    public List<OpportunityTO> getTaskKanbanItemsByStatus(ListingFilterParameter fp, Integer statusId) {
        EdsReference status = referenceManager.get(statusId);

        ListResult<TaskListItem> tasksKanbanItem =
                taskService.getNewKanbanTasks(fp, status.getAsSelectItem());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<OpportunityTO> taskTOS = new ArrayList<>();

        for (TaskListItem task : tasksKanbanItem.getList()) {
            OpportunityTO taskTO = new OpportunityTO();
            taskTO.setName(task.getName());

            IdName contactDTO = new IdName();
            contactDTO.setName(task.getProjectName());
            contactDTO.setId(task.getProjectId());
            contactDTO.addProperty("assignee", task.getAssigneeFullNames());
//            contactDTO.addProperty("phone", task.getAssignedToPhone());
            taskTO.setContact(contactDTO);

            taskTO.setItem_id(task.getObjectID());
            taskTO.setStatus_id(task.getTaskStatusId() != null ? task.getTaskStatusId() : 0);

            if (task.getCreationDate() != null) {
                taskTO.setDate_added(dateFormat.format(task.getCreationDate()));
            }

            taskTOS.add(taskTO);
        }

        return taskTOS;
    }

    public List<OpportunityTO> getLeadKanbanItemsByStatus(ListingFilterParameter fp, Integer statusId) {
        EdsReference status = referenceManager.get(statusId);

        ListResult<ContactListItem> leadsKanbanItem =
                crmService.getNewKanbanLeads(fp, status.getAsSelectItem());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        List<OpportunityTO> leadTOS = new ArrayList<>();

        for (ContactListItem lead : leadsKanbanItem.getList()) {
            OpportunityTO leadTO = new OpportunityTO();
            leadTO.setName(lead.getFullName());

            IdName contactDTO = new IdName();
            contactDTO.setName(lead.getFullName());
            contactDTO.setId(lead.getObjectId());
            contactDTO.addProperty("email", lead.getPrimaryEmail());
            contactDTO.addProperty("phone", lead.getPrimaryPhone());
            leadTO.setContact(contactDTO);

            leadTO.setItem_id(lead.getObjectId());
            leadTO.setStatus_id(lead.getLeadStatus() != null ? lead.getLeadStatus().getId() : 0);

            if (lead.getCreatedDate() != null) {
                leadTO.setDate_added(dateFormat.format(lead.getCreatedDate()));
            }

            leadTOS.add(leadTO);
        }

        return leadTOS;
    }


    @Transactional
    public Integer applyChanges(OpportunityDto dto) throws RestException {
        EdsOpportunity opportunity = null;
        if (dto.getId() != null) {
            opportunity = opportunityManager.get(dto.getId());
        }
        if (opportunity == null && StringUtils.isNotBlank(dto.getNumber())) {
            opportunity = opportunityManager.getByNumber(dto.getNumber());
        }
        if (opportunity == null) {
            throw new RestException(ApiConstants.ERROR, "Opportunity is not found by given Id/Number.", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        try {
            opportunity.setSapRequestBody(objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize SAP request body", e);
        }
        OpportunityListItem objOpportunity = crmService.editOpportunity(opportunity.getObjectID());
        EdsEmployee assignee = getAssignee(dto.getAssignee());
        if (assignee != null) {
            objOpportunity.setAssigneeId(assignee.getObjectID());
        }
        if (StringUtils.isNotBlank(dto.getName())) {
            objOpportunity.setOpportunityName(dto.getName());
        }
        if (dto.getCloseDate() != null) {
            EdsCompany company = opportunityManager.getUser().getCompany();
            objOpportunity.setClosingDate(new Date(dto.getCloseDate().getTime() - company.getTimeZone().getRawOffset()));
        }
        EdsCurrency currency = getCurrency(dto.getCurrency());
        if (currency != null) {
            objOpportunity.setCurrencyId(currency.getObjectID());

            if (currency.getObjectID().equals(objOpportunity.getBaseCurrencyID())) {
                objOpportunity.setExchangeRate(BigDecimal.ONE);
            } else {
                if (dto.getExchangeRate() != null) {
                    objOpportunity.setExchangeRate(dto.getExchangeRate());
                } else {
                    String baseCurrency = objOpportunity.getBaseCurrencyName();
                    CurrencyLayerItem currencyLayerItem = currencyService.getExchangeRateDouble(baseCurrency, currency != null ? currency.getName() : baseCurrency, objOpportunity.getClosingDate() != null ? objOpportunity.getClosingDate() : new Date(), 0);
                    objOpportunity.setExchangeRate(BigDecimal.valueOf(currencyLayerItem.getRate()));
                }
            }
        }
        if (dto.getTaxCalcType() != null) {
            TaxTypeEnum taxTypeEnum = TaxTypeEnum.valueOf(dto.getTaxCalcType());
            if (taxTypeEnum != null) {
                objOpportunity.setTaxCalculationType(taxTypeEnum.getId());
            }
        }
        if (!CollectionUtils.isEmpty(dto.getItems())) {
            opportunityLineItemCustomFields.set(commonService.getCompanyCustomFields(ViewName.OpportunitySubItem));
            if (dto.isKeepItems()) {
                objOpportunity.setItems(getOpportunityItems(dto.getItems(), objOpportunity.getItems()));
            } else {
                objOpportunity.setItems(getOpportunityItems(dto.getItems()));
            }
        } else if (dto.getAmount() != null) {
            objOpportunity.setAmount(dto.getAmount());
        }
        if (!CollectionUtils.isEmpty(dto.getNotes())) {
            objOpportunity.setNotes(dto.getNotes().stream().map(note -> ConvertUtils.toEntity(note, opportunityManager.getUser().getName())).collect(Collectors.toCollection(ArrayList::new)));
        }
        EdsCrmAccount customer = getCrmAccount(dto.getCustomer());
        if (customer != null) {
            objOpportunity.setAccountId(customer.getObjectID());
        }
        EdsCrmContact contact = getContact(dto.getContact());
        if (contact != null) {
            objOpportunity.setContactId(contact.getObjectID());
        }
        EdsReference stage = getStage(dto.getStage());
        if (stage != null) {
            objOpportunity.setStageId(stage.getObjectID());
        }
        if (!CollectionUtils.isEmpty(dto.getCustomFields())) {
            ArrayList<CompanyCustomFieldItem> customFields = CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Opportunity), opportunity.getCustomFields());
            if (!CollectionUtils.isEmpty(customFields)) {
                objOpportunity.setCustomFields(customFields);
            }
        }
        if (objOpportunity.getItems() != null && objOpportunity.getItems().length > 0) {
            calculateTotalsBasedOnItems(objOpportunity.getItems(), objOpportunity);
            objOpportunity.setAmount(objOpportunity.getTotal().doubleValue());
        }
        Optional.ofNullable(dto.getSource())
                .map(this::getSource)
                .map(EdsReference::getObjectID)
                .ifPresent(objOpportunity::setLeadSourceId);
        Integer id = crmService.saveOpportunity(objOpportunity);

        if (dto.getRejectReason() != null) {
            EdsReference rejectReason = getRejectReason(dto.getRejectReason());
            if (rejectReason != null) {
                SelectItem item = new SelectItem(objOpportunity.getStageId(), "", "0", dto.getRejectNote());
                item.setEntityId(rejectReason.getObjectID());
                crmService.changeOpportunityKanbanOrder(item, id, null, null, null);
            }
        }

        return id;
    }

    private ListResultTO<OpportunityDto> getOpporunityFromSolrResult(QueryResponse resp) {
        String Ids = resp.getResults().stream().map(doc -> String.valueOf(SolrUtils.asInteger(doc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID))).collect(Collectors.joining(","));
        ArrayList<OpportunityDto> items = new ArrayList<>();
        if (StringUtils.isNotBlank(Ids)) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.Opportunity);
            opportunityLineItemCustomFields.set(commonService.getCompanyCustomFields(ViewName.OpportunitySubItem));

            List<EdsOpportunity> list = opportunityManager.getOpportunityByIds(Ids);
            for (EdsOpportunity opportunity : list) {
                items.add(wrapOpportunityToDto(opportunity, customFieldItems));
            }
        }
        return new ListResultTO<>((int) resp.getResults().getNumFound(), items);
    }

    private OpportunityItem[] getOpportunityItems(List<OpportunityItemDto> itemsDto, OpportunityItem[] opportunityItems) {
        List<OpportunityItem> result = new ArrayList<>();
        List<OpportunityItem> existingItems = new ArrayList<>(Arrays.asList(opportunityItems)); //I'm gonna remove its element in a loop what's why I'm cloning it(Arrays.asList(..) is a fixed size list)
        itemsDto.removeIf(item -> item.getProduct() == null);

        for (OpportunityItemDto itemDto : new ArrayList<>(itemsDto)) {//I'm removing its element in a loop what's why I'm cloning it
            Integer itemId = Optional.ofNullable(itemDto.getProduct().getId()).orElse(0);
            String itemNumber = Optional.ofNullable(itemDto.getProduct().getCode()).orElse("");
            String itemName = Optional.ofNullable(itemDto.getProduct().getName()).orElse("");
            Optional<OpportunityItem> optMatchItem = existingItems.stream().filter(ex -> itemId.equals(ex.getItemID()) || itemNumber.equals(ex.getItemNumber()) || itemName.equals(ex.getItemName())).findFirst();
            optMatchItem.ifPresent(item -> {
                existingItems.remove(item);
                itemsDto.remove(itemDto);

                if (itemDto.getQuantity() != null) {
                    item.setQty(itemDto.getQuantity());
                }
                if (itemDto.getUnitPrice() != null) {
                    item.setPrice(itemDto.getUnitPrice());
                }
                if (itemDto.getTaxItem() != null) {
                    item.setTaxItem(getTaxItem(itemDto.getTaxItem()));
                }
                if (itemDto.getDiscount() != null) {
                    item.setDiscountPercent(itemDto.getDiscount());
                }
                if (!CollectionUtils.isEmpty(itemDto.getCustomFields())) {
                    ArrayList<CompanyCustomFieldItem> customFields = CustomFieldsUtils.convertCustomFields(itemDto.getCustomFields(), item.getItemCustomFields(), null);
                    if (!CollectionUtils.isEmpty(customFields)) {
                        item.setItemCustomFields(customFields);
                    }
                }
                result.add(item);
            });
        }

        if (!CollectionUtils.isEmpty(itemsDto)) {
            result.addAll(Arrays.asList(getOpportunityItems(itemsDto)));
        }
        return result.toArray(new OpportunityItem[]{});
    }

    private OpportunityItem[] getOpportunityItems(List<OpportunityItemDto> itemsDto) {
        if (CollectionUtils.isEmpty(itemsDto)) {
            return null;
        }
        return itemsDto.stream()
                .map(this::getOpportunityItemFromDto)
                .filter(Objects::nonNull)
                .toList()
                .toArray(new OpportunityItem[]{});
    }

    private OpportunityItem getOpportunityItemFromDto(OpportunityItemDto dto) {
        if (dto == null) {
            return null;
        }

        OpportunityItem result = new OpportunityItem();
        SelectItem product = getProduct(dto.getProduct());
        if (product != null) {
            result.setItemID(product.getId());
            result.setItemNumber(product.getCode());
            result.setItemName(product.getName());
        } else if (StringUtils.isNotBlank(dto.getProduct().getName())) {
            result.setItemName(dto.getProduct().getName());
        } else {
            return null;
        }
        result.setDescription(dto.getDescription());
        result.setQty(dto.getQuantity() != null ? dto.getQuantity() : BigDecimal.ZERO);
        result.setPrice(dto.getUnitPrice() != null ? dto.getUnitPrice() : BigDecimal.ZERO);
        result.setTaxItem(getTaxItem(dto.getTaxItem()));
        result.setDiscountPercent(dto.getDiscount());

        EdsCrmAccount supplier = getCrmAccount(dto.getSupplier());
        if (supplier != null) {
            result.setSupplierID(supplier.getObjectID());
            result.setSupplierName(supplier.getName());
        }
        result.setProductCategory(getCategory(dto.getCategory()));
        result.setProductBrand(getBrand(dto.getBrand()));

        if (
                (dto.getCategory() == null || dto.getCategory().getId() == 0) &&
                (dto.getBrand() == null || dto.getBrand().getId() == 0) && product != null
        ) {
            result.setProductCategory(getCategory(new IdName(null, product.getCategory())));
            result.setProductBrand(getBrand(new IdName(null, product.getDescription())));
        }

        if (CollectionUtils.isEmpty(dto.getCustomFields())) {
            //we should clone sub item custom field settings, cause of this is that, if multiple items come then they might be override each other
            ArrayList<CompanyCustomFieldItem> clonedObject = new ArrayList<>(opportunityLineItemCustomFields.get());
            result.setItemCustomFields(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), clonedObject, null));
        }
        return result;
    }

    private void calculateTotalsBasedOnItems(OpportunityItem[] items, OpportunityListItem opportunity) {
        if (items == null || items.length == 0) {
            return;
        }
        List<OpportunityItem> itemList = Stream.of(items)
                .peek(item -> {
                    BigDecimal net = item.getPrice().multiply(item.getQty());
                    if (item.getDiscountPercent() != null) {
                        net = net.multiply(BigDecimal.ONE.subtract(item.getDiscountPercent().divide(AccountingConstants.HUNDRED, RoundingMode.HALF_UP)));
                    }
                    item.setNet(net);
                    BigDecimal itemTaxAmount = BigDecimal.ZERO;

                    if (item.getTaxItem() != null) {
                        BigDecimal taxRate = item.getTaxItem().getEffectiveTaxPercent();
                        if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(opportunity.getTaxCalculationType())) {
                            itemTaxAmount = item.getNet().multiply(taxRate).divide(AccountingConstants.HUNDRED.add(taxRate), AccountingConstants.systemCalculationScale, RoundingMode.HALF_UP);
                        } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(opportunity.getTaxCalculationType())) {
                            itemTaxAmount = item.getNet().multiply(taxRate.divide(AccountingConstants.HUNDRED, AccountingConstants.systemCalculationScale, RoundingMode.HALF_UP));
                        }
                    }
                    item.setTaxAmount(itemTaxAmount);
                    if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(opportunity.getTaxCalculationType())) {
                        item.setSubTotal(item.getNet());
                    } else if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(opportunity.getTaxCalculationType())) {
                        item.setSubTotal(item.getNet().add(itemTaxAmount));
                    }
                })
                .toList();
        BigDecimal totalDiscount = BigDecimal.ZERO, totalTax = BigDecimal.ZERO, total = BigDecimal.ZERO, totalQty = BigDecimal.ZERO;
        for (OpportunityItem item : itemList) {
            if (item.getDiscountPercent() != null) {
                totalDiscount = totalDiscount.add(item.getNet().multiply(item.getDiscountPercent()).divide(AccountingConstants.HUNDRED, RoundingMode.HALF_UP));
            }
            totalTax = totalTax.add(item.getTaxAmount());
            total = total.add(item.getSubTotal());
            totalQty = totalQty.add(item.getQty());
        }
        opportunity.setItems(itemList.toArray(new OpportunityItem[0]));
        opportunity.setDiscountTotal(totalDiscount);
        opportunity.setTaxTotal(totalTax);
        opportunity.setTotal(total);
        opportunity.setQuantityTotal(totalQty);
        if (opportunity.getExchangeRate() != null && BigDecimal.ZERO.compareTo(opportunity.getExchangeRate()) != 0) {
            opportunity.setTotalInBase(opportunity.getTotal().divide(opportunity.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        }
    }

    private OpportunityDto wrapOpportunityToDto(EdsOpportunity opportunity, ArrayList<CompanyCustomFieldItem> customFieldSettings) {
        OpportunityDto dto = new OpportunityDto();
        dto.setId(opportunity.getObjectID());
        dto.setNumber(opportunity.getNumber());
        dto.setName(opportunity.getName());

        Optional.ofNullable(opportunity.getAssignee())
                .map(employee -> {
                    IdCode assigneeDto = new IdCode(employee.getObjectID(), employee.getProfile().getEmployeeCode());
                    assigneeDto.addProperty("fullName", employee.getFullName());
                    return assigneeDto;
                })
                .ifPresent(dto::setAssignee);

        dto.setNextStep(opportunity.getNextStep());

        Optional.ofNullable(opportunity.getCrmAccount()).ifPresent(crmAccount -> dto.setCustomer(new ItemDto(crmAccount.getObjectID(), crmAccount.getName(), crmAccount.getNumber())));
        Optional.ofNullable(opportunity.getCrmContact()).ifPresent(contact -> {
            IdName contactDto = new IdName(contact.getObjectID(), contact.getFullName());
            contactDto.addProperty("email", contact.getPrimaryEmail());
            contactDto.addProperty("phone", contact.getPrimaryPhone());
            dto.setContact(contactDto);
        });
        Optional.ofNullable(opportunity.getLeadSource()).ifPresent(source -> {
            IdCode sourceDto = new IdCode(source.getObjectID(), source.getCode());
            sourceDto.setName(source.getLocalizedName());
            dto.setSource(sourceDto);
        });
        Optional.ofNullable(opportunity.getStage()).ifPresent(stage -> {
            IdCode stageDto = new IdCode(stage.getObjectID(), stage.getCode());
            stageDto.setName(stage.getLocalizedName());
            dto.setStage(stageDto);
        });
        Optional.ofNullable(opportunity.getType())
                .map(type -> new IdCode(type.getObjectID(), type.getCode()))
                .ifPresent(dto::setType);
        dto.setAmount(opportunity.getAmount());
        Optional.ofNullable(opportunity.getCurrency()).ifPresent(currency -> dto.setCurrency(new IdCode(currency.getObjectID(), currency.getName())));
        dto.setExchangeRate(opportunity.getExchangeRate());
        Optional.ofNullable(opportunity.getTaxCalculationType()).ifPresent(taxCalcType -> dto.setTaxCalcType(TaxTypeEnum.getTaxTypeById(taxCalcType).getName()));
        dto.setCloseDate(opportunity.getClosingDate());
        Optional.ofNullable(opportunity.getProbability()).ifPresent(probability -> dto.setProbability(probability.doubleValue()));

        if (!CollectionUtils.isEmpty(opportunity.getOpportunityItems())) {
            dto.setItems(wrapItemsToDto(opportunity.getOpportunityItems()));
        }
        if (customFieldSettings == null) {
            customFieldSettings = commonService.getCompanyCustomFields(ViewName.Opportunity);
        } else {
            customFieldSettings = new ArrayList<>(customFieldSettings);
        }

        if (opportunity.getCustomFields() != null && !CollectionUtils.isEmpty(customFieldSettings)) {
            customFieldSettings = CustomFieldsUtils.setRPCCustomFieldItems(opportunity.getCustomFields(), customFieldSettings);
            dto.setCustomFields(customFieldSettings.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }
        Optional.ofNullable(opportunity.getCrmAccount())
                .map(EdsCrmAccount::getEmail)
                .ifPresent(dto::setEmail);
        Optional.ofNullable(opportunity.getCrmAccount())
                .map(EdsCrmAccount::getPhone)
                .ifPresent(dto::setPhone);
        return dto;
    }

    private OpportunityListItem wrapDtoToModel(OpportunityDto dto) {
        OpportunityListItem model = new OpportunityListItem();
        EdsEmployee assignee = getAssignee(dto.getAssignee());
        model.setAssigneeId(assignee != null ? assignee.getObjectID() : null);
        model.setOpportunityName(dto.getName());

        CurrencyItem baseCurrency = currencyService.getBaseCurrency();
        EdsCurrency currency = getCurrency(dto.getCurrency());
        if (currency != null) {
            model.setCurrencyId(currency.getObjectID());
        } else {
            model.setCurrencyId(baseCurrency.getId());
        }

        if (baseCurrency.getId().equals(model.getCurrencyId())) {
            model.setExchangeRate(BigDecimal.ONE);
        } else if (dto.getExchangeRate() != null) {
            model.setExchangeRate(dto.getExchangeRate());
        } else {
            CurrencyLayerItem currencyLayerItem = currencyService.getExchangeRateDouble(baseCurrency.getName(), currency != null ? currency.getName() : baseCurrency.getName(), dto.getCloseDate() != null ? dto.getCloseDate() : new Date(), 0);
            model.setExchangeRate(BigDecimal.valueOf(currencyLayerItem.getRate()));
        }
        if (StringUtils.isNotBlank(dto.getNumber())) {
            model.setNumberData(new NumberData(dto.getNumber()));
        } else {
            model.setNumberData(crmService.generateOpportunityNumber());
        }
        model.setTaxCalculationType(TaxTypeEnum.TAX_EXCLUSIVE.getId());
        if (dto.getTaxCalcType() != null) {
            TaxTypeEnum taxTypeEnum = TaxTypeEnum.valueOf(dto.getTaxCalcType());
            if (taxTypeEnum != null) {
                model.setTaxCalculationType(taxTypeEnum.getId());
            }
        }
        if (!CollectionUtils.isEmpty(dto.getItems())) {
            opportunityLineItemCustomFields.set(commonService.getCompanyCustomFields(ViewName.OpportunitySubItem));
            model.setItems(getOpportunityItems(dto.getItems()));
        } else if (dto.getAmount() != null) {
            model.setAmount(dto.getAmount());
        }
        if (!CollectionUtils.isEmpty(dto.getNotes())) {
            model.setNotes(dto.getNotes().stream().map(note -> ConvertUtils.toEntity(note, opportunityManager.getUser().getName())).collect(Collectors.toCollection(ArrayList::new)));
        }
        if (dto.getCloseDate() != null) {
            EdsCompany company = opportunityManager.getUser().getCompany();
            model.setClosingDate(new Date(dto.getCloseDate().getTime() - company.getTimeZone().getRawOffset()));
        } else {
            model.setClosingDate(null);
        }
        EdsCrmAccount customer = getCrmAccount(dto.getCustomer());
        if (customer != null) {
            model.setAccountId(customer != null ? customer.getObjectID() : null);
        } else if (dto.getCustomer() != null && dto.getCustomer().getName() != null) {
            model.setAccountId(createCrmAccountSynchronously(dto.getCustomer()));
        }

        EdsCrmContact contact = getContactByEmail(dto.getContact());
        if (contact != null && contact.getObjectID() != null) {
            model.setContactId(contact != null ? contact.getObjectID() : null);
        } else if (dto.getContact() != null && dto.getContact().getName() != null) {
            model.setContactId(createContactSynchronously(dto.getContact()));
        }


        EdsReference stage = getStage(dto.getStage());
        if (stage != null) {
            model.setStageId(stage.getObjectID());
        }

        EdsCampaign campaign = getCampaignType(dto.getCampaign());
        if (campaign != null) {
            model.setCampaignId(campaign.getObjectID());
        }
        EdsReference source = getSource(dto.getSource());
        if (source != null) {
            model.setLeadSourceId(source.getObjectID());
        }
        if (!CollectionUtils.isEmpty(dto.getCustomFields())) {
            model.setCustomFields(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Opportunity), null));
        } else {
            model.setCustomFields(null);
        }
        if (model.getItems() != null && model.getItems().length > 0) {
            calculateTotalsBasedOnItems(model.getItems(), model);
            model.setAmount(model.getTotal().doubleValue());
        }
        Optional.ofNullable(dto.getType())
                .map(IdCode::getId)
                .ifPresent(model::setTypeId);

        return model;
    }

    Integer createCrmAccountSynchronously(ItemDto customer) {
        String content = StringUtils.isNotBlank(customer.getCode()) ? customer.getCode().trim().toLowerCase() + "-" : "";
        content += StringUtils.isNotBlank(customer.getName()) ? customer.getName().trim().toLowerCase() : "";

        AtomicInteger result = new AtomicInteger(0); // Initialize an AtomicInteger to store the result

        Runnable task = () -> {
            try {
                result.set(createCrmAccount(customer)); // Set the result obtained from createCrmAccount
            } catch (RestException e) {
                e.printStackTrace();
            }
        };

        stringXSync.execute(getSyncronizedKey(content), task);

        return result.get(); // Return the result obtained asynchronously
    }

    String getSyncronizedKey(String content) {
        return ("CREATE_CUSTOMER_") + ServerSecurityContext.getInstance().getCompanyId() + "_" + content;
    }

    Integer createCrmAccount(ItemDto crmAccount) throws RestException {
        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setName(crmAccount.getName());
        crmAccountItem.setNumber(crmAccount.getCode());

        if (crmAccount.getProperties() != null && crmAccount.getProperties().size() > 0) {
            Map<String, Object> properties = crmAccount.getProperties();
            crmAccountItem.setPhone((String) properties.get("phoneNumber"));
            crmAccountItem.setEmail((String) properties.get("email"));
        }
        EdsReference accountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
        SelectItem accountTypeItem = accountType.getAsSelectItem();
        accountTypeItem.setSelected(true);
        crmAccountItem.setAccountTypes(new SelectItem[]{
                accountTypeItem
        });

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        crmAccountItem.setCurrencyId(fs.getCurrency() != null ? fs.getCurrency().getObjectID() : null);
        Integer result = crmServiceLocal.saveAccount(crmAccountItem, CrmAccountItem.CUSTOMER, null, false, false, false, true);

        if (result < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Error handled when creating a " + ("customer"), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }


    Integer createContact(IdName crmAccount) throws RestException {
        ContactListItem contactListItem = new ContactListItem();
        contactListItem.setFirstName(crmAccount.getName());

        if (crmAccount.getProperties() != null && crmAccount.getProperties().size() > 0) {
            Map<String, Object> properties = crmAccount.getProperties();
            contactListItem.setPrimaryPhone((String) properties.get("phoneNumber"));

            if (properties.get("email") != null) {
                contactListItem.setPrimaryEmail((String) properties.get("email"));
                HashMap<Integer, ArrayList<String>> map = new HashMap<>();
                map.put(2, new ArrayList<>(List.of((String) properties.get("email"))));
                contactListItem.setEmails(map);
            }
            if (properties.get("phoneNumber") != null) {
                contactListItem.setPrimaryPhone((String) properties.get("phoneNumber"));
                HashMap<Integer, ArrayList<String>> map = new HashMap<>();
                map.put(2, new ArrayList<>(List.of((String) properties.get("phoneNumber"))));
                contactListItem.setPhones(map);
            }
            if (properties.get("companyName") != null) {
                CrmAccountItem crmAccountItem = new CrmAccountItem();
                EdsCrmAccount account = crmAccountManager.getCrmAccountByName((String) properties.get("companyName"));
                if (account != null) {
                    crmAccountItem.setObjectId(account.getObjectID());
                    crmAccountItem.setNumber(account.getNumber());
                    contactListItem.setCrmAccount(crmAccountItem);
                }
            }
        }

        Integer result = contactServiceLocal.saveContact(contactListItem, null, false);

        if (result < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Error handled when creating a " + ("customer"), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    String getContactSyncronizedKey(String content) {
        return ("CREATE_CONTACT_") + ServerSecurityContext.getInstance().getCompanyId() + "_" + content;
    }


    Integer createContactSynchronously(IdName contact) {
        try {
            return createContact(contact);
        } catch (RestException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private List<OpportunityItemDto> wrapItemsToDto(List<EdsOpportunityItem> items) {
        List<OpportunityItemDto> result = new ArrayList<>();
        for (EdsOpportunityItem item : items) {
            OpportunityItemDto dto = new OpportunityItemDto();
            Optional.ofNullable(item.getItem()).ifPresent(product -> dto.setProduct(new ItemDto(product.getObjectID(), product.getName(), product.getProductNumber())));
            if (dto.getProduct() == null) {
                dto.setProduct(new ItemDto(null, item.getItemName()));
            }
            dto.setDescription(item.getDescription());
            dto.setQuantity(item.getQty());
            dto.setUnitPrice(item.getPrice());

            Optional.ofNullable(item.getVat()).ifPresent(vat -> {
                ItemDto taxItem = new ItemDto(vat.getObjectID(), vat.getTaxNameAndRateAsString());
                taxItem.addProperty("amount", String.valueOf(item.getTaxAmount()));
                dto.setTaxItem(taxItem);
            });
            Optional.ofNullable(item.getDiscount()).ifPresent(dto::setDiscount);

            ArrayList<CompanyCustomFieldItem> customFieldSettings = opportunityLineItemCustomFields.get();
            if (customFieldSettings == null) {
                customFieldSettings = commonService.getCompanyCustomFields(ViewName.OpportunitySubItem);
            } else {
                customFieldSettings = new ArrayList<>(customFieldSettings);
            }
            if (item.getCustomFields() != null && !CollectionUtils.isEmpty(customFieldSettings)) {
                customFieldSettings = CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), customFieldSettings);
                dto.setCustomFields(customFieldSettings.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
            }
            result.add(dto);
        }
        return result;
    }

    private EdsEmployee getAssignee(IdCode dto) {
        if (dto == null) {
            return null;
        }
        EdsEmployee assignee = null;
        if (dto.getId() != null) {
            assignee = employeeManager.get(dto.getId());
        }
        if (assignee == null && StringUtils.isNotBlank(dto.getCode())) {
            assignee = employeeManager.getEmployeeByNumber(dto.getCode());
        }
        return assignee;
    }

    private SelectItem getProduct(ItemDto dto) {
        if (dto == null) {
            return null;
        }
        EdsItem item = null;

        if (dto.getId() != null) {
            item = itemManager.get(dto.getId());
        }
        if (item == null && StringUtils.isNotBlank(dto.getCode())) {
            item = itemManager.getItemByNumber(dto.getCode());
        }
        if (item == null && StringUtils.isNotBlank(dto.getName())) {
            item = itemManager.getItemByName(dto.getName());
        }
        if (item != null) {
            SelectItem sItem = item.getAsProductSelectItem();
            sItem.setCode(item.getProductNumber());

            if (item.getCategory() != null) {
                sItem.setCategory(item.getCategory().getName());
            }

            if (item.getBrand() != null) {
                sItem.setDescription(item.getBrand().getName());
            }
            return sItem;
        }
        return null;
    }

    private EdsCurrency getCurrency(IdCode dto) {
        if (dto == null) {
            return null;
        }
        EdsCurrency currency = null;

        if (dto.getId() != null) {
            currency = currencyManager.get(dto.getId());
        }
        if (currency == null && StringUtils.isNotBlank(dto.getCode())) {
            currency = currencyManager.getCurrency(dto.getCode());
        }
        return currency;
    }

    private EdsCrmAccount getCrmAccount(ItemDto dto) {
        if (dto == null) {
            return null;
        }
        EdsCrmAccount crmAccount = null;

        if (dto.getId() != null) {
            crmAccount = crmAccountManager.get(dto.getId());
        }
        if (crmAccount == null && StringUtils.isNotBlank(dto.getCode())) {
            crmAccount = crmAccountManager.getCrmAccountByNumber(dto.getCode());
        }
        if (crmAccount == null && StringUtils.isNotBlank(dto.getName())) {
            crmAccount = crmAccountManager.getCrmAccountByName(dto.getName());
        }
        return crmAccount;
    }

    private EdsCrmContact getContact(IdName dto) {
        if (dto == null) {
            return null;
        }
        EdsCrmContact crmContact = null;

        if (dto.getId() != null) {
            crmContact = contactManager.get(dto.getId());
        }
        return crmContact;
    }

    private EdsCrmContact getContactByEmail(IdName dto) {
        if (dto == null) {
            return null;
        }

        EdsCrmContact crmContact = null;

        if (dto.getId() != null) {
            crmContact = contactManager.get(dto.getId());
        }
        if (dto.getProperties() != null && dto.getProperties().size() > 0) {
            Map<String, Object> properties = dto.getProperties();
            if (properties.get("email") != null && !"".equals(properties.get("email"))) {
                crmContact = contactManager.getContactByEmail((String) properties.get("email"), userManager.getUser().getCompany().getObjectID());
            }
            if (properties.get("phoneNumber") != null && !"".equals(properties.get("phoneNumber"))) {
                crmContact = contactManager.getByPhone((String) properties.get("phoneNumber"));
            }
        }
        if (dto.getName() != null) {
            crmContact = contactManager.getByFirstName(dto.getName());
        }
        return crmContact;
    }

    private EdsReference getStage(IdCode dto) {
        if (dto == null) {
            return null;
        }
        EdsReference stage = null;

        if (dto.getId() != null) {
            stage = referenceManager.get(dto.getId());
        }
        if (stage == null && StringUtils.isNotBlank(dto.getCode())) {
            stage = referenceManager.findReference(EdsOpportunity._OPPORTUNITY_STAGE, dto.getCode());
        }
        return stage;
    }

    private EdsCampaign getCampaignType(IdNameTO dto) {
        if (dto == null) {
            return null;
        }
        EdsCampaign campaign = null;

        if (dto.getId() != null) {
            campaign = campaignManager.get(dto.getId());
        }
        if (campaign == null && StringUtils.isNotBlank(dto.getName())) {
            campaign = campaignManager.getCampaignByName(dto.getName());
        }
        return campaign;
    }

    private EdsReference getRejectReason(ItemDto dto) {
        if (dto == null) {
            return null;
        }
        EdsReference reference = null;

        if (dto.getId() != null) {
            reference = referenceManager.get(dto.getId());
        }
        if (reference == null && StringUtils.isNotBlank(dto.getCode())) {
            reference = referenceManager.findReference("_OPPORTUNITY_SUB_STAGE", dto.getCode());
        }
        if (reference == null && StringUtils.isNotBlank(dto.getName())) {
            reference = referenceManager.findByParentCodeAndName("_OPPORTUNITY_SUB_STAGE", dto.getName());
        }
        return reference;
    }

    public List<OpportunityByStageTO> getOpportunityByStage(ListingFilterParameter fp) {

        Map<Integer, OpportunityByStageTO> map = Map.of();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if ("OPPORTUNITY".equals(fp.getRelationType())) {
            map = new HashMap<>();
            List<EdsReference> stages = referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE);

            for (EdsReference stage : stages) {
                OpportunityByStageTO opportunityByStageTO = new OpportunityByStageTO();
                opportunityByStageTO.setStageId(stage.getObjectID());
                opportunityByStageTO.setStageTitle(stage.getName());

                OpportunitiesList<OpportunityListItem> opportunityKanbanItem =
                        crmService.getNewKanbanOpportunities(fp, stage.getAsSelectItem());

                List<OpportunityTO> opportunityTOS = new ArrayList<>();

                for (OpportunityListItem item : opportunityKanbanItem.getList()) {
                    OpportunityTO opportunityTO = new OpportunityTO();
                    opportunityTO.setName(item.getOpportunityName());

                    if (item.getContact() != null) {
                        IdName contactDTO = new IdName();
                        contactDTO.setName(item.getContact());
                        contactDTO.setId(item.getContactId());
                        contactDTO.addProperty("email", item.getContactPrimaryEmail());
                        contactDTO.addProperty("phone", item.getContactPrimaryPhone());
                        opportunityTO.setContact(contactDTO);
                    }

                    opportunityTO.setAmmount(item.getAmount());
                    opportunityTO.setCurrency(item.getCurrency());
                    opportunityTO.setClosing_date(item.getClosingDate());
                    opportunityTO.setClosing_date_Id(item.getClosingDateID());

                    Optional.ofNullable(item.getCrmAccountItem())
                            .ifPresent(crmAccount -> opportunityTO.setCustomer(
                                    new ItemDto(crmAccount.getObjectId(), crmAccount.getName(), crmAccount.getNumber())));

                    opportunityTO.setItem_id(item.getObjectId());
                    opportunityTO.setStatus_id(item.getStage() != null ? item.getStage().getId() : 0);

                    if (item.getCreatedDate() != null) {
                        opportunityTO.setDate_added(dateFormat.format(item.getCreatedDate()));
                    }

                    opportunityTOS.add(opportunityTO);
                }

                opportunityByStageTO.setOpportunity(opportunityTOS);
                map.put(stage.getObjectID(), opportunityByStageTO);
            }
        } else if ("LEADS".equals(fp.getRelationType())) {
            map = new HashMap<>();
            List<EdsReference> statuses = referenceManager.listReferences(EdsCrmContact._LEAD_STATUS);

            for (EdsReference status : statuses) {
                OpportunityByStageTO opportunityByStageTO = new OpportunityByStageTO();
                opportunityByStageTO.setStageId(status.getObjectID());
                opportunityByStageTO.setStageTitle(status.getName());

                ListResult<ContactListItem> leadsKanbanItem =
                        crmService.getNewKanbanLeads(fp, status.getAsSelectItem());

                List<OpportunityTO> leadTOS = new ArrayList<>();

                for (ContactListItem lead : leadsKanbanItem.getList()) {
                    OpportunityTO leadTO = new OpportunityTO();
                    leadTO.setName(lead.getFullName());

                    IdName contactDTO = new IdName();
                    contactDTO.setName(lead.getFullName());
                    contactDTO.setId(lead.getObjectId());
                    contactDTO.addProperty("email", lead.getPrimaryEmail());
                    contactDTO.addProperty("phone", lead.getPrimaryPhone());
                    leadTO.setContact(contactDTO);

                    leadTO.setItem_id(lead.getObjectId());
                    leadTO.setStatus_id(lead.getLeadStatus() != null ? lead.getLeadStatus().getId() : 0);

                    if (lead.getCreatedDate() != null) {
                        leadTO.setDate_added(dateFormat.format(lead.getCreatedDate()));
                    }

                    leadTOS.add(leadTO);
                }

                opportunityByStageTO.setOpportunity(leadTOS);
                map.put(status.getObjectID(), opportunityByStageTO);
            }
        } else if ("TASKS".equals(fp.getRelationType())) {
            fp.setRelationType(null);
            map = new HashMap<>();
            List<EdsReference> statuses = referenceManager.listReferences(EdsTask.TASK_STATUS);

            for (EdsReference status : statuses) {
                OpportunityByStageTO opportunityByStageTO = new OpportunityByStageTO();
                opportunityByStageTO.setStageId(status.getObjectID());
                opportunityByStageTO.setStageTitle(status.getName());

                ListResult<TaskListItem> tasksKanbanItem =
                        taskService.getNewKanbanTasks(fp, status.getAsSelectItem());

                List<OpportunityTO> taskTOS = new ArrayList<>();

                for (TaskListItem task : tasksKanbanItem.getList()) {
                    OpportunityTO taskTO = new OpportunityTO();
                    taskTO.setName(task.getName());

                    IdName contactDTO = new IdName();
                    contactDTO.setName(task.getProjectName());
                    contactDTO.setId(task.getProjectId());
                    contactDTO.addProperty("assignee", task.getAssigneeFullNames());
//                    contactDTO.addProperty("phone", task.getAssignedToPhone());
                    taskTO.setContact(contactDTO);

                    taskTO.setItem_id(task.getObjectID());
                    taskTO.setStatus_id(task.getTaskStatusId() != null ? task.getTaskStatusId() : 0);

                    if (task.getCreationDate() != null) {
                        taskTO.setDate_added(dateFormat.format(task.getCreationDate()));
                    }

                    taskTOS.add(taskTO);
                }

                opportunityByStageTO.setOpportunity(taskTOS);
                map.put(status.getObjectID(), opportunityByStageTO);
            }
        }

        return new ArrayList<>(map.values());
    }



    private EdsReference getSource(IdCode dto) {
        if (dto == null) {
            return null;
        }
        EdsReference source = null;

        if (dto.getId() != null) {
            source = referenceManager.get(dto.getId());
        }
        if (source == null && StringUtils.isNotBlank(dto.getCode())) {
            source = referenceManager.findReference(EdsCrmContact._LEAD_SOURCE, dto.getCode());
        }
        return source;
    }

    private TaxItem getTaxItem(ItemDto dto) {
        if (dto == null) {
            return null;
        }
        EdsVat vat = null;

        if (dto.getId() != null) {
            vat = vatManager.get(dto.getId());
        }
        if (vat == null && StringUtils.isNotBlank(dto.getName())) {
            vat = vatManager.getVatByName(dto.getName());
        }
        return vat != null ? vat.createTaxItem() : null;
    }

    private SelectItem getCategory(IdName dto) {
        if (dto == null) {
            return null;
        }
        EdsProductCategory category = null;

        if (dto.getId() != null) {
            category = productCategoryManager.get(dto.getId());
        }
        if (category == null && StringUtils.isNotBlank(dto.getName())) {
            category = productCategoryManager.getCategoryByName(dto.getName());
        }

        return category != null ? category.getAsSelectItem() : null;
    }

    private SelectItem getBrand(IdName dto) {
        if (dto == null) {
            return null;
        }
        EdsBrand brand = null;

        if (dto.getId() != null) {
            brand = brandManager.get(dto.getId());
        }
        if (brand == null && StringUtils.isNotBlank(dto.getName())) {
            brand = brandManager.getBrandByName(dto.getName());
        }

        return brand != null ? brand.getAsSelectItem() : null;
    }

    public void delete(Integer id) {
        crmService.deleteOpportunity(new ArrayList<>(List.of(id)));
    }

    public NumberData generateNumber() {
        return crmService.generateOpportunityNumber();
    }
}
