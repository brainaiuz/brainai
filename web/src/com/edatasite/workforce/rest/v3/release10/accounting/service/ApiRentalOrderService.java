package com.edatasite.workforce.rest.v3.release10.accounting.service;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.EdsRentalOrderItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.accounting.server.app.RentalOrderServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderItemManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ItemTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.RentalItemRequest;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.RentalOrderDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.RentalOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiRentalOrderService implements Constants, ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(ApiRentalOrderService.class);
    private static final Integer RENTAL_ITEM_TYPE = 7;
    private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final RentalOrderManager rentalOrderManager;
    private final NumberingSettingsManager numberingSettingsManager;
    private final CommonServiceLocal commonServiceLocal;
    private final AccountingService accountingService;
    private final RentalOrderServiceLocal rentalOrderService;
    private final CrmAccountManager crmAccountManager;
    private final ItemManager itemManager;
    private final RentalOrderItemManager rentalOrderItemManager;
    private final WfmMessageSource commonLocalizer;
    private final ReferenceManager referenceManager;
    private final CommonService commonService;

    @Autowired
    public ApiRentalOrderService(RentalOrderManager rentalOrderManager, NumberingSettingsManager numberingSettingsManager, CommonServiceLocal commonServiceLocal, AccountingService accountingService, @Qualifier("rentalOrderService") RentalOrderServiceLocal rentalOrderService, CrmAccountManager crmAccountManager, ItemManager itemManager, RentalOrderItemManager rentalOrderItemManager, @Qualifier("commonLocalizer") WfmMessageSource commonLocalizer, ReferenceManager referenceManager, CommonService commonService) {
        this.rentalOrderManager = rentalOrderManager;
        this.numberingSettingsManager = numberingSettingsManager;
        this.commonServiceLocal = commonServiceLocal;
        this.accountingService = accountingService;
        this.rentalOrderService = rentalOrderService;
        this.crmAccountManager = crmAccountManager;
        this.itemManager = itemManager;
        this.rentalOrderItemManager = rentalOrderItemManager;
        this.commonLocalizer = commonLocalizer;
        this.referenceManager = referenceManager;
        this.commonService = commonService;
    }

    public ListResultTO<RentalOrderDto> getRentalOrderList(ListingFilterParameter fp) {
        var list = rentalOrderManager.getRentalOrderList(fp);
        if (list == null && list.isEmpty()) return new ListResultTO<>();

        var result = list.stream()
                .map(rentalOrder -> ConvertUtils.toDto(toItem(rentalOrder, fp.getListPanelTool())))
                .collect(Collectors.toCollection(ArrayList::new));
        ListResultTO<RentalOrderDto> listResultTO = new ListResultTO<>();
        listResultTO.setTotalNumber(result.size());
        listResultTO.setItems(result);
        return listResultTO;
    }

    @Transactional(readOnly = true)
    public RentalOrderDto getById(final Integer id) throws RestException {
        Optional.ofNullable(rentalOrderManager.get(id)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Rental order with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
        RentalOrderData dto = rentalOrderService.getRentalOrderData(id, false);
        return ConvertUtils.toDto(dto);
    }

    public void deleteRentalOrder(final Integer id) {
        rentalOrderService.deleteRentalOrder(id);
    }

    @Transactional(rollbackFor = RestException.class)
    public RentalOrderDto save(RentalOrderRequest req) throws RestException {
        EdsRentalOrder rentalOrder = new EdsRentalOrder();
        EdsCrmAccount crmAccount = crmAccountManager.get(req.getCustomerId());

        if (crmAccount == null || crmAccount.isDeleted()) {
            throw new RestException(IN_VALID_DATA, "Customer does not exist with given ID", ApiConstants.INVALID, HttpStatus.NOT_FOUND);
        }

        rentalOrder.setCustomer(crmAccount);
        rentalOrder.setCreator(rentalOrderManager.getUser());
        rentalOrder.setCreatedDate(new Date());
        rentalOrder.setUpdatedDate(new Date());
        rentalOrderManager.create(rentalOrder);

        List<EdsRentalOrderItem> rentalOrderItems = new ArrayList<>();
        for (RentalItemRequest item : req.getRentalItems()) {
            if (item.getFrom().after(item.getTo()) || item.getFrom().equals(item.getTo())) {
                throw new RestException(IN_VALID_DATA, "Invalid time range, from must be before to", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
            }
            if (item.getRentalPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RestException(IN_VALID_DATA, "Rental Price must be greater than zero", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
            }
            if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RestException(IN_VALID_DATA, "Quantity must be greater than zero", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
            }
            EdsItem edsItem = itemManager.get(item.getRentalItemId());
            if (edsItem == null || !edsItem.getType().equals(RENTAL_ITEM_TYPE)) {
                throw new RestException(IN_VALID_DATA, "Rental Item does not exist with given ID or type is not Rental Item", ApiConstants.INVALID, HttpStatus.NOT_FOUND);
            }

            EdsRentalOrderItem rItem = new EdsRentalOrderItem();
            rItem.setRentalItem(edsItem);
            rItem.setRentalOrder(rentalOrder);
            rItem.setDescription(fmt.format(item.getFrom()) + " -> " + fmt.format(item.getTo()));
            rItem.setFromDate(item.getFrom());
            rItem.setToDate(item.getTo());
            rItem.setPrice(item.getRentalPrice());
            rItem.setQty(item.getQuantity());
            BigDecimal net = item.getRentalPrice().multiply(item.getQuantity());
            rItem.setNet(net);
            rItem.setSubTotal(net);
            rentalOrderItemManager.create(rItem);
            rentalOrderItems.add(rItem);
        }

        rentalOrder.setItems(rentalOrderItems);

        NumberData numberData = rentalOrderService.generateRentalOrderNumber();
        rentalOrder.setNumber(numberData.getNumberString());
        rentalOrder.setIntNumber(numberData.getIntNumber());
        rentalOrder.setCustomFields(rentalOrderService.createRentalOrderCustomFields(CustomFieldsUtils.convertCustomFields(req.getCustomFields(), commonService.getCompanyCustomFields(ViewName.RentalOrdersView), req.getRentalOrderId() != null ? rentalOrder.getCustomFields() : null)));
        Date latest = req.getRentalItems().stream().map(RentalItemRequest::getTo).max(Date::compareTo).orElse(req.getRentalItems().get(0).getTo());
//        rentalOrder.setStartDate(latest);
        rentalOrder.setExpirationDate(latest);
        rentalOrder.setEntityStatus(referenceManager.findReference(Constants.RENTAL_STATUS, RENTAL_APPROVED)); // todo
        rentalOrderService.saveRentalOrderHistory(rentalOrder.getObjectID(), new HistoryListItem(commonLocalizer.localize("created")));
//        rentalOrderService.saveRentalOrderHistory(rentalOrder.getObjectID(), new HistoryListItem(req.getRentalOrderId() == null ? commonLocalizer.localize("created") : commonLocalizer.localize("modifiedDate")));

        return ConvertUtils.toDto(toItem(rentalOrder, null));
    }

    public ListResultTO<ItemTO> getAvailableProducts(ItemTO dto) throws RestException {
        if (dto.getFromDate() == null) {
            throw new RestException(IN_VALID_DATA, "fromDate is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (dto.getToDate() == null) {
            throw new RestException(IN_VALID_DATA, "toDate is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (dto.getFromDate().after(dto.getToDate()) || dto.getFromDate().equals(dto.getToDate())) {
            throw new RestException(IN_VALID_DATA, "endDate must be after startDate", INVALID, HttpStatus.BAD_REQUEST);
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(dto.getFromDate());
        fp.setEndDate(dto.getToDate());
        List<SelectItem> items = itemManager.getAvailableItems(fp);
        ArrayList<ItemTO> resultList = new ArrayList<>();
        items.forEach(item -> {
            ItemTO productDto = new ItemTO();
            productDto.setItem_id(item.getId());
            productDto.setItem_name(item.getName());
            productDto.setItem_number(item.getDescription());
            resultList.add(productDto);
        });

        ListResultTO<ItemTO> listResultTO = new ListResultTO<>();
        listResultTO.setItems(resultList);
        listResultTO.setTotalNumber(resultList.size());
        return listResultTO;
    }


    @Transactional
    public RentalOrderDto update(RentalOrderRequest req) throws RestException {
//        EdsRentalOrder rentalOrder = rentalOrderManager.get(req.getRentalOrderId());
//        if (rentalOrder == null || rentalOrder.isDeleted()) {
//            throw new RestException(IN_VALID_DATA, "Rental order does not exist with given ID", ApiConstants.INVALID, HttpStatus.NOT_FOUND);
//        }

//        rentalOrderService.saveRentalOrderHistory(rentalOrder.getObjectID(), new HistoryListItem(req.getRentalOrderId() == null ? commonLocalizer.localize("created") : commonLocalizer.localize("modifiedDate")));

        return null;
    }

    private RentalOrderData toItem(EdsRentalOrder rentalOrder, ListPanelToolRpc panelSettings) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        String rentOrderNumberingFormat = settings != null ? settings.getRentalOrderNumberingFormat() : null;

        RentalOrderData rentalOrderData = rentalOrder.createRentalOrderData();
        if (rentalOrder.getNumber() != null) {
            rentalOrderData.setNumberData(new NumberData(rentalOrder.getNumber(), rentalOrder.getIntNumber()));
            rentalOrderData.getNumberData().setNumberFormat(rentOrderNumberingFormat);
            rentalOrderData.setNumber(rentalOrderData.getNumberData().getNumberString());
        }
        if (panelSettings != null) {
            HashMap<String, Object> map = CustomFieldsUtils.getRPCCustomFields(rentalOrder.getCustomFields(), panelSettings.getColumnCodeName());
            rentalOrderData.setCustomFieldValuesItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
            rentalOrderData.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(rentalOrder.getCustomFields(), panelSettings.getListViewCustomFields()));
        }
        if (!CollectionUtils.isEmpty(rentalOrder.getItems())) {
            rentalOrderData.setRentalOrderItems(rentalOrder.getItems().stream().map(EdsRentalOrderItem::toDTO).collect(Collectors.toCollection(ArrayList::new)));
        }
        rentalOrderData.setCustomer(rentalOrder.getCustomer() != null ? rentalOrder.getCustomer().getAsSelectItem() : null);
        return rentalOrderData;
    }
}
