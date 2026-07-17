package com.edatasite.workforce.rest.base.helpers;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.news.client.rpc.NewsCategory;
import com.edatasite.workforce.gwt.news.client.rpc.NewsData;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.edatasite.workforce.rest.base.enums.NoteEnum;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CustomFieldTo;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EmailDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PhoneDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ExpenseDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.FixedAssetDTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InventoryStockInformationDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.PriceLevelDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ProductDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ProductLocationDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.RentalOrderDto;
import com.edatasite.workforce.rest.v3.release10.accounting.enums.ProductTypeEnum;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFormDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.LocaleDto;
import com.edatasite.workforce.rest.v3.release10.core.to.RelationDto;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.CaseDto;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.CaseReporterDto;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.EventDto;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.ReminderDto;
import com.edatasite.workforce.rest.v3.release10.core.to.hrms.NewsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.pm.AssigneeDto;
import com.edatasite.workforce.rest.v3.release10.core.to.pm.TaskDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.OpportunityContactTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.lead.OpportunityConvertTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.AllowanceDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.CandidateDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.CertificateDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.CertificateDynamicFieldsDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.DependentDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.EmployeeDocsTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.IMWebAddressDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LanguagesDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LeaveRequestDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.VacancyDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.ProjectDTO;
import com.edatasite.workforce.rest.v3.release10.pm.dto.ProjectEmployeeDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.support.WfmMessageSource;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment.CALL_LOG;

public class ConvertUtils {
    public static Address toEntity(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        Address address = new Address();
        address.setRelationType(ContactParamEnum.getParamIdByCode(addressDto.getAddressType()));
        address.setName(addressDto.getName());
        address.setZipCode(addressDto.getPostcode());
        address.setCountry(addressDto.getCountry());
        address.setAddress(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setEntityType(addressDto.getEntityType());
        address.setStateId(addressDto.getStateId());
        address.setEntityID(addressDto.getEntityId());
        address.setLinkedAddress(false);
        address.setCountryId(addressDto.getCountryId());
        address.setAddressb(addressDto.getAddressLine2());
        address.setPrimary(addressDto.isPrimary());
        address.setCountryCode(addressDto.getCountryCode());
        address.setState(addressDto.getState());
        return address;
    }

    public static AddressDto toDto(Address address) {
        if (address == null) {
            return null;
        }
        AddressDto addressDto = new AddressDto();
        SelectItemTO addressType = ContactParamEnum.getParamAsSelectItemTO(address.getRelationType());
        addressDto.setAddressType(addressType != null ? addressType.getCode() : null);
        addressDto.setName(address.getName());
        addressDto.setAddressLine(address.getAddress());
        addressDto.setAddressLine2(address.getAddressb());
        addressDto.setCity(address.getCity());
        addressDto.setCountry(address.getCountry());
        addressDto.setState(address.getState());
        addressDto.setPostcode(address.getZipCode());
        addressDto.setPrimary(address.isPrimary());

        return addressDto;
    }

    public static NewsDTO toDto(NewsData news, List<FileResource> files) {
        NewsDTO data = new NewsDTO();
        data.setId(news.getObjectId());
        data.setSubject(news.getSubject());
        data.setDate(news.getPublishedDate());
        data.setShortText(news.getShortDescription());
        data.setFullText(news.getFullDescription());
        ItemDto author = new ItemDto();
        author.setId(news.getCreatorId());
        author.setName(news.getAuthor());
        data.setAuthor(author);
        data.setInternal(news.isVisibility());
        data.setLocation(new IdCode(news.getLocationID(), news.getLocation()));
        if (news.getCategories() != null && !news.getCategories().isEmpty()) {
            List<IdCode> categories = new ArrayList<>();
            for (NewsCategory category : news.getCategories()) {
                categories.add(new IdCode(category.getId(), category.getName()));
            }
            data.setCategories(categories);
        }
        if (files != null && !files.isEmpty()) {
            List<AttachmentTO> attachments = new ArrayList<>();
            for (FileResource file : files) {
                AttachmentTO attachment = new AttachmentTO();
                attachment.setFile_name(file.getFileName());
                attachment.setLink(file.getDownloadUrl());
                attachments.add(attachment);
            }
            data.setAttachments(attachments);
        }
        return data;
    }

    public static HistoryListItem toEntity(NoteDto noteDto, String userName) {
        if (noteDto == null) {
            return null;
        }
        HistoryListItem historyListItem = new HistoryListItem();
        historyListItem.setObjectID(noteDto.getId());
        historyListItem.setEventDate(new Date());
        historyListItem.setEmployee(userName);
        historyListItem.setComment(noteDto.getText());
        historyListItem.setEntityID(noteDto.getEntityId());
        if (NoteEnum.PRIVATE.getName().equalsIgnoreCase(noteDto.getVisibility())) {
            historyListItem.setVisibility(true);
        } else if (NoteEnum.PUBLIC.getName().equalsIgnoreCase(noteDto.getVisibility())) {
            historyListItem.setVisibility(false);
        }

        return historyListItem;
    }

    public static HistoryListItem toEntity(NoteDto noteDto) {
        if (noteDto == null) {
            return null;
        }
        HistoryListItem historyListItem = new HistoryListItem();
        historyListItem.setObjectID(noteDto.getId());
        historyListItem.setEventDate(new Date());
        historyListItem.setEmployee(noteDto.getEmployee());
        historyListItem.setComment(noteDto.getText());
        historyListItem.setEntityID(noteDto.getEntityId());
        historyListItem.setVisibility(NoteEnum.PRIVATE.getName().equalsIgnoreCase(noteDto.getVisibility()));
        return historyListItem;
    }

    public static NoteDto toDto(HistoryListItem historyListItem) {
        if (historyListItem == null) {
            return null;
        }
        NoteDto noteDto = new NoteDto();
        noteDto.setId(historyListItem.getObjectID());
        noteDto.setText(historyListItem.getComment());
        noteDto.setEmployee(historyListItem.getEmployee());
        noteDto.setEventDate(historyListItem.getEventDate());
        if (historyListItem.isVisibility() == null) {
            noteDto.setVisibility("INTERNAL");
        } else if (historyListItem.isVisibility()) {
            noteDto.setVisibility("PRIVATE");
        } else {
            noteDto.setVisibility("PUBLIC");
        }

        return noteDto;
    }

    public static CompanyCustomFieldItem toEntity(ArrayList<CompanyCustomFieldItem> customFieldItems, CustomFieldTo customFieldTo) throws ParseException {
        CompanyCustomFieldItem customFieldItem = customFieldItems
                .stream()
                .filter(x -> x.getAliasName().equalsIgnoreCase(customFieldTo.getAlias()))
                .findAny()
                .orElse(null);

        if (customFieldItem != null) {
            if (customFieldItem.getDataType().equals("Date")) {
                customFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(new Date(Long.parseLong(customFieldTo.getValue()))));
            } else {
                customFieldItem.setFieldStringValue(customFieldTo.getValue());
            }

        }
        return customFieldItem;
    }

    public static CustomFieldTo toDto(CompanyCustomFieldItem companyCustomFieldItem) {
        if (companyCustomFieldItem == null) {
            return null;
        }
        CustomFieldTo customFieldTo = new CustomFieldTo();
        customFieldTo.setAlias(companyCustomFieldItem.getAliasName());
        customFieldTo.setValue(companyCustomFieldItem.getFieldStringValue());
        return customFieldTo;
    }

    public static LeadDto toDto(ContactListItem item) {
        LeadDto leadDto = new LeadDto();
        leadDto.setId(item.getObjectId());
        leadDto.setAssigneeId(item.getLeadAssigneeID());
        if (item.getLeadAssigneeID() != null) {
            leadDto.setAssisnee(new IdNameTO(item.getLeadAssigneeID(), item.getLeadAssignee()));
        }
        leadDto.setFirstName(item.getFirstName());
        leadDto.setLastName(item.getLastName());
        leadDto.setJobTitle(item.getJobTitle());
        leadDto.setCompanyId(item.getCrmAccount() != null ? item.getCrmAccount().getObjectId() : null);
        Optional.ofNullable(item.getCrmAccount())
                .map(c -> new IdNameTO(c.getObjectId(), c.getName()))
                .ifPresent(leadDto::setCompany);
        leadDto.setStatus(item.getLeadStatus(true).getName());
        leadDto.setSource(item.getLeadSource());
        leadDto.setCampaignId(item.getCampaignId());
        if (item.getCampaignId() != null) {
            leadDto.setCampaign(new IdNameTO(item.getCampaignId(), item.getCampaign()));
        }
        leadDto.setCreatedAt(item.getCreatedDate());
        leadDto.setUpdatedAt(item.getUpdatedDate());

        //Setting Emails
        if (item.getWorkEmail() != null && !item.getWorkEmail().isEmpty()) {
            List<EmailDto> emails = item.getWorkEmail().stream()
                    .map(email -> new EmailDto(email, email.equals(item.getPrimaryEmail())))
                    .toList();
            leadDto.setEmails(emails);
        }
        leadDto.setOwner(item.getOwner());
        leadDto.setOwnerId(item.getOwnerId());
        List<IdNameTO> accountTypes = Stream.of(item.getCrmAccount().getAccountTypes())
                .filter(SelectItem::isSelected)
                .map(a -> new IdNameTO(a.getId(), a.getName()))
                .toList();
        leadDto.setAccountTypes(accountTypes);
        leadDto.setSource(item.getLeadSource());
        Optional.ofNullable(item.getCrmAccount())
                .map(i -> new IdNameTO(i.getIndustryID(), i.getIndustry()))
                .ifPresent(leadDto::setIndustry);
        leadDto.setRating(item.getLeadRating());
        leadDto.setPrimaryEmail(item.getPrimaryEmail());


        //Setting Phone Numbers
        if (item.getAllPhones() != null && !item.getAllPhones().isEmpty()) {
            List<PhoneDto> phoneNumbers = new ArrayList<>();
            item.getAllPhonesAsMap().forEach((key, value) -> {
                for (String number : value) {
                    phoneNumbers.add(new PhoneDto(key, number, number.equals(item.getPrimaryPhone())));
                }
            });
            leadDto.setPhoneNumbers(phoneNumbers);
        }


        //Setting Notes
        if (item.getHistory() != null && item.getHistory().getTotalCount() > 0) {
            List<NoteDto> notes = new ArrayList<>();
            for (HistoryListItem historyListItem : item.getHistory().getResult()) {
                notes.add(toDto(historyListItem));
            }
            leadDto.setNotes(notes);
        }

        //Setting Addresses
        if (item.getAddresses() != null && !item.getAddresses().isEmpty()) {
            List<AddressDto> addresses = new ArrayList<>();
            for (Address address : item.getAddresses()) {
                addresses.add(toDto(address));
            }
            leadDto.setAddresses(addresses);
        }

        //Setting Custom Fields
        if (item.getCustomFields() != null && !item.getCustomFields().isEmpty()) {
            leadDto.setCustomFields(item.getCustomFields().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).toList());
        }

        return leadDto;
    }

    public static ProductDto toDto(NewProduct newProduct, HashMap<PriceLevelItem, PriceLevelPPItem> priceLevel) {
        ProductDto productDto = new ProductDto();
        productDto.setId(newProduct.getObjectId());
        productDto.setObjectKey(newProduct.getObjectKey());
        productDto.setName(newProduct.getItemName());
        productDto.setNumber(newProduct.getNumberData().getNumberString());
        productDto.setType(newProduct.getTypeName());
        productDto.setDescription(newProduct.getDescription());
        productDto.setActive(newProduct.isActive());
        productDto.setSellingPrice(newProduct.getSellingPrice());
        productDto.setPurchasePrice(newProduct.getUnitPrice());
        productDto.setSoldToCustomer(newProduct.isSoldToCustomer());

        if (newProduct.getAccountItem() != null) {
            IdCode salesAccount = new IdCode();
            salesAccount.setId(newProduct.getAccountItem().getId());
            salesAccount.setCode(newProduct.getAccountItem().getCode());
            salesAccount.addProperty("name", newProduct.getAccountItem().getName());

            productDto.setSalesAccount(salesAccount);
        }
        if (newProduct.getCogsAccount() != null) {
            IdCode purchaseAccount = new IdCode();
            purchaseAccount.setId(newProduct.getCogsAccount().getId());
            purchaseAccount.setCode(newProduct.getCogsAccount().getCode());
            purchaseAccount.addProperty("name", newProduct.getCogsAccount().getName());
            productDto.setPurchaseAccount(purchaseAccount);
        }
        productDto.setPurchasedFromSupplier(newProduct.isPurchasedFromSupplier());
        productDto.setCategory(new IdCode(newProduct.getCategoryID(), newProduct.getCategoryName()));
        productDto.setBrand(new IdCode(newProduct.getBrandID(), newProduct.getBrandName()));

        if (newProduct.getDiscountItems() != null && newProduct.getDiscountItems().length > 0) {
            List<IdCode> discounts = new ArrayList<>();
            for (DiscountItem discountItem : newProduct.getDiscountItems()) {
                discounts.add(new IdCode(discountItem.getId(), discountItem.getName()));
            }
            productDto.setDiscounts(discounts);
        }

        if (newProduct.getTaxItem() != null) {
            productDto.setTaxData(new IdCode(newProduct.getVatId(), newProduct.getTaxItem().getName()));
        }

        productDto.setTaxId(newProduct.getVatId());
        productDto.setSkuNumber(newProduct.getInternalSKUNumber());
        if (newProduct.getUnitMeasurement() != null) {
            productDto.setUnitMeasurement(new IdCode(newProduct.getUnitMeasurement().getId(), newProduct.getUnitMeasurement().getName()));
        }
        productDto.setPartNumber(newProduct.getPartNumber());
        productDto.setUpcNumber(newProduct.getUpcNumber());
        productDto.setManufacturer(newProduct.getManufacturer());
        if (newProduct.getSuppliers() != null && newProduct.getSuppliers().length > 0) {
            List<IdCode> suppliers = new ArrayList<>();
            for (SelectItem supplier : newProduct.getSuppliers()) {
                suppliers.add(new IdCode(supplier.getId(), supplier.getName()));
            }
            productDto.setSuppliers(suppliers);
        }
        if (ProductTypeEnum.INVENTORY_ITEM.getId().equals(newProduct.getType()) || ProductTypeEnum.ASSEMBLY_ITEM.getId().equals(newProduct.getType())) {
            InventoryStockInformationDto inventoryStockInformationDto = new InventoryStockInformationDto();
            inventoryStockInformationDto.setAsOf(newProduct.getAsOf() != null ? newProduct.getAsOf().getDate() : null);
            AccountItem assetAccount = newProduct.getAssetAccount();
            if (assetAccount != null) {
                IdCode idCode = new IdCode(assetAccount.getId(), assetAccount.getCode());
                idCode.addProperty("name", assetAccount.getName());
            }
            inventoryStockInformationDto.setBatchSerialnumber(newProduct.getBatchTrackingEnabled());
            inventoryStockInformationDto.setTrackSerialnumber(newProduct.getInventoryTrackingEnabled());
            inventoryStockInformationDto.setTrackBatches(newProduct.getTrackBatchesEnabled());
            List<ProductLocationDto> productLocations = new ArrayList<>();
            for (ProductLocationItem productLocationItem : newProduct.getProductLocations()) {
                ProductLocationDto productLocationDto = new ProductLocationDto();
                productLocationDto.setQtyOnHand(productLocationItem.getQty());
                productLocationDto.setReorderPoint(productLocationItem.getMinReorderPoint());
                productLocationDto.setWarehouse(new IdName(productLocationItem.getWarehouseID(), productLocationItem.getWarehouseName()));
                productLocationDto.setObjectId(productLocationItem.getObjectID());

                productLocations.add(productLocationDto);
            }
            inventoryStockInformationDto.setProductLocations(productLocations);
            inventoryStockInformationDto.addProperty("totalValue", newProduct.getTotalValue());
            productDto.setInventoryStockInformation(inventoryStockInformationDto);
        }
        if (newProduct.getProductCustomFieldItems() != null && !newProduct.getProductCustomFieldItems().isEmpty()) {
            productDto.setCustomFields(newProduct.getProductCustomFieldItems().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }
        if (priceLevel != null && !priceLevel.isEmpty()) {
            List<PriceLevelDto> pls = new ArrayList<>();
            priceLevel.keySet().forEach(pl -> pls.add(new PriceLevelDto(new IdCode(pl.getId(), pl.getName()), BigDecimal.valueOf(priceLevel.get(pl).getCustomPrice()))));
            productDto.setPriceLevel(pls);
        }
        if (newProduct.getDefaultItemWarehouse() != null) {
            productDto.setDefaultWarehouse(new ItemDto(newProduct.getDefaultItemWarehouse().getId(), newProduct.getDefaultItemWarehouse().getName()));
        }
        Optional.ofNullable(newProduct.getAssetAccount())
                .map(a -> new ItemDto(a.getId(), a.getName(), a.getCode()))
                .ifPresent(productDto::setAssetAccount);

        productDto.setCreatedAt(newProduct.getCreatedDate());
        productDto.setUpdatedAt(newProduct.getLastUpdateTime());


        return productDto;
    }

    public static RentalOrderDto toDto(RentalOrderData item) {
        RentalOrderDto dto = new RentalOrderDto();
        dto.setObjectID(item.getObjectID());
        dto.setNumber(item.getNumber());
        dto.setStatus(item.getStatus());
        dto.setNumberData(item.getNumberData());
        dto.setExpiration(item.getExpirationDate());
        dto.setCustomer(item.getCustomer());
        dto.setPaymentTerms(item.getPaymentTerms());
        dto.setTaxCalculationType(item.getTaxCalculationType());
        dto.setCreatedDate(item.getCreatedDate());
        dto.setUpdatedDate(item.getUpdatedDate());
        dto.setRentalOrderItems(item.getRentalOrderItems());
        dto.setItemColumns(item.getItemColumns());
        dto.setTemplates(item.getTemplates());
        dto.setTaxAmount(item.getTaxAmount());
        dto.setSubTotal(item.getSubTotal());
        dto.setTotal(item.getTotal());
        if (item.getCustomFieldItems() != null) {
            dto.setCustomFieldItems(item.getCustomFieldItems());
            dto.setCustomFields(item.getCustomFieldItems().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }
        dto.setSupplierCustomerBalance(item.getSupplierCustomerBalance());
        dto.setInvoiceItem(item.getInvoiceItem());
        dto.setProductCategories(item.getProductCategories());
        dto.setProductBrands(item.getProductBrands());
        return dto;
    }

    public static EventDto toDto(EventItem item) {
        EventDto event = new EventDto();
        event.setId(item.getObjectID());
        event.setSubject(item.getSubject());
        event.setStartDate(item.getStartDate());
        event.setEndDate(item.getEndDate());
        event.setDescription(item.getDescription());

        if (item.getGuests() != null && item.getGuests().size() > 0) {
            List<String> guests = new ArrayList<>();
            for (SelectItem guest : item.getGuests()) {
                guests.add(guest.getName());
            }
            event.setGuests(guests);
        }

        if (item.getSharedEmployees() != null && item.getSharedEmployees().size() > 0) {
            List<IdCode> shares = new ArrayList<>();
            for (PositionsSelectItem share : item.getSharedEmployees()) {
                IdCode itemDto = new IdCode();
                itemDto.setId(share.getEmployeeId());
                itemDto.setCode(share.getName());
                shares.add(itemDto);
            }
            event.setShares(shares);
        }

        if (item.getReminder() != null && item.getReminder().size() > 0) {
            List<ReminderDto> reminders = new ArrayList<>();
            for (CalendarEventReminder rem : item.getReminder()) {
                ReminderDto reminder = new ReminderDto();
                reminder.setTimes(rem.getReminderTimes());
                reminder.setType(rem.getValue());
                reminders.add(reminder);
            }
            event.setReminders(reminders);
        }

        if (item.getActivityType() == CALL_LOG) {
            event.setInbound(item.isInboundCall());
            event.setOutgoing(item.isOutboundCall());
            event.setMissed(item.isMissedCall());
            event.setDuration(item.getCallDuration());
            event.setCurrent(item.isCurrentCall());
            event.setCompleted(item.isComplatedCall());
            event.setScheduled(item.isScheduleCall());
            event.setClone(item.isClone());
        }

        event.setAllDay(item.isAllDay());
        event.setEventType(item.getActivityType());
        event.setCreatedAt(item.getCreatedDate());
        event.setUpdatedAt(item.getLastModifiedDate());

        return event;
    }

    public static CaseDto toDto(CaseItem caseItem, List<FileResource> files) {
        CaseDto caseDto = new CaseDto();
        caseDto.setId(caseItem.getObjectId());
        caseDto.setSubject(caseItem.getSubject());
        caseDto.setNumber(caseItem.getCaseNumber());

        CaseReporterDto reporterDto = new CaseReporterDto();
        if (caseItem.getLeadId() != null && caseItem.getLead() != null) {
            reporterDto.setReportedByType("Lead");
            reporterDto.setReporter(new IdCode(caseItem.getLeadId(), caseItem.getLead()));
        } else if (caseItem.getAccountId() != null && caseItem.getAccountName() != null) {
            reporterDto.setReportedByType("Account");
            reporterDto.setReporter(new IdCode(caseItem.getAccountId(), caseItem.getAccountName()));
        } else if (caseItem.getCrmContactID() != null && caseItem.getCrmContact() != null) {
            reporterDto.setReportedByType("Contact");
            reporterDto.setReporter(new IdCode(caseItem.getCrmContactID(), caseItem.getCrmContact()));
        }
        caseDto.setReportedBy(reporterDto);

        caseDto.setDescription(caseItem.getDescription());

        caseDto.setStatus(new IdCode(caseItem.getStatus().getObjectID(), caseItem.getStatus().getName()));

        caseDto.setPriority(new IdCode(caseItem.getPriorityId(), caseItem.getPriority()));
        caseDto.setType(new IdCode(caseItem.getTypeId(), caseItem.getType()));
        caseDto.setAssignee(new ItemDto(caseItem.getCaseAssigneeId(), caseItem.getCaseAssigneeName()));
        caseDto.setResolver(new ItemDto(caseItem.getResolverId(), caseItem.getResolverName()));
        caseDto.setOrigin(new IdCode(caseItem.getCaseOriginId(), caseItem.getCaseOrigin()));
        caseDto.setReason(new IdCode(caseItem.getCaseReasonId(), caseItem.getCaseReason()));

        if (caseItem.getNotes() != null && !caseItem.getNotes().isEmpty()) {
            List<NoteDto> notes = new ArrayList<>();
            caseItem.getNotes().forEach(note -> notes.add(toDto(note)));
            caseDto.setNotes(notes);
        }

        if (files != null && !files.isEmpty()) {
            List<AttachmentTO> attachments = new ArrayList<>();
            for (FileResource file : files) {
                AttachmentTO attachment = new AttachmentTO();
                attachment.setFile_name(file.getFileName());
                attachment.setLink(file.getDownloadUrl());
                attachments.add(attachment);
            }
            caseDto.setAttachments(attachments);
        }

        if (caseItem.getCustomFields() != null && !caseItem.getCustomFields().isEmpty()) {
            caseDto.setCustomFields(caseItem.getCustomFields().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }

        if (caseItem.getRelations() != null && !caseItem.getRelations().isEmpty()) {
            List<RelationDto> relations = new ArrayList<>();
//            caseItem.getRelations().forEach(relationItem -> relations.add(toDto(relationItem)));
            caseDto.setRelations(relations);
        }
        return caseDto;
    }

//    public static RelationDto toDto(RelationItem relationItem) {
//        RelationDto dto = new RelationDto();
//        dto.setFromType(new ItemDto(relationItem.getFromID(), relationItem.getFromName(), relationItem.getFromType()));
//        dto.setToType(new ItemDto(relationItem.getToID(), relationItem.getToName(), relationItem.getToType()));
//        return dto;
//    }

//    public static RelationItem toEntity(RelationDto dto) {
//        RelationItem item = new RelationItem();
//
//        item.setFromID(dto.getFromType().getId());
//        item.setFromName(dto.getFromType().getName());
//        item.setFromType(dto.getFromType().getCode());
//
//        item.setToID(dto.getToType().getId());
//        item.setToName(dto.getToType().getName());
//        item.setToType(dto.getToType().getCode());
//
//        return item;
//    }

    public static TaskDto toDto(TaskSingleItem item, List<FileResource> files) {
        return toDto(item, files, null);
    }

    public static TaskDto toDto(TaskSingleItem item, List<FileResource> files, List<HistoryListItem> notes) {
        TaskDto dto = new TaskDto();

        dto.setId(item.getObjectID());
        dto.setName(item.getName());

        Optional.ofNullable(item.getNumberData())
                .map(NumberData::getNumberString)
                .ifPresent(dto::setNumber);
        dto.setStatus(new ItemDto(item.getStatusID(), item.getStatusName(), item.getStatus() != null ? item.getStatus()[0].getDescription() : null, item.getStatusColor()));
        dto.setDescription(item.getDescription());

        dto.setProject(new ItemDto(item.getProjectID(), item.getProjectName()));
        dto.setStartDate(item.getStartDate());
        dto.setEndDate(item.getEndDate());
        dto.setSpentTime(item.getTimeSpent());
        dto.setAllDay(item.isAllDay());
        dto.setPriority(new IdCode(item.getPriorityID(), item.getPriorityName()));
        dto.setBillable(item.getBillable());
        dto.setCustomer(item.getClientName());

        if (item.getIssueEmployees() != null && item.getIssueEmployees().length > 0) {
            List<AssigneeDto> assignees = Arrays.stream(item.getIssueEmployees()).map(AssigneeDto::new).toList();
            dto.setAssignees(assignees);
        }
        dto.setWorkStream(new IdCode(item.getWorkstreamID(), item.getWorkstreamName()));
        if (item.getPredecessorTasks() != null && item.getPredecessorTasks().length > 0) {
            List<ItemDto> predecessors = Arrays.stream(item.getPredecessorTasks())
                    .map(s -> new ItemDto(s.getId(), s.getName(), s.getCode()))
                    .toList();
            dto.setPredecessors(predecessors);
        }

        if (item.getSuccessorTasks() != null && item.getSuccessorTasks().length > 0) {
            List<ItemDto> successors = Arrays.stream(item.getSuccessorTasks())
                    .map(s -> new ItemDto(s.getId(), s.getName(), s.getCode()))
                    .toList();
            dto.setSuccessors(successors);
        }

        if (CollectionUtils.isNotEmpty(files)) {
            List<AttachmentTO> attachments = files.stream()
                    .map(f -> new AttachmentTO(f.getFileName(), f.getDownloadUrl()))
                    .toList();
            dto.setAttachments(attachments);
        }

        if (CollectionUtils.isNotEmpty(notes)) {
            dto.setNotes(notes.stream().map(ConvertUtils::toDto).toList());
        }

        if (item.getCustomFieldItems() != null && !item.getCustomFieldItems().isEmpty()) {
            List<CustomFieldDto> customFields = item.getCustomFieldItems().stream()
                    .map(CustomFieldsUtils::getCustomFieldDto)
                    .filter(cf -> cf.getValue() != null)
                    .toList();
            dto.setCustomFields(customFields);
        }

        return dto;
    }

    public static TaskDto toDto(TaskListItem item) {
        TaskDto dto = new TaskDto();

        dto.setId(item.getObjectID());
        dto.setName(item.getName());

        dto.setNumber(item.getNumber());
        dto.setStatus(new ItemDto(item.getTaskStatusId(), item.getStatusName(), null, item.getPriorityColor()));
        dto.setDescription(item.getDescription());

        dto.setProject(new ItemDto(item.getProjectId(), item.getProjectName()));
        dto.setStartDate(item.getStartDate());
        dto.setDueDate(item.getDueDate());
        dto.setEndDate(item.getEndDate());
        dto.setAllDay(item.isAllDay());
        dto.setPriority(new IdCode(item.getPriorityId(), item.getPriorityName()));
        dto.setBillable(item.isBillable() != null && item.isBillable());
        dto.setCustomer(item.getClient());

        dto.setWorkStream(new IdCode(item.getParentWorkstreamId(), item.getParentWorkstreamName()));
        dto.setAssignedTo(item.getAssignedTo());

        return dto;
    }

    public static FixedAssetDTO toDto(FixedAssetItem item) {
        FixedAssetDTO dto = new FixedAssetDTO();

        dto.setId(item.getObjectID());
        dto.setName(item.getName());
        dto.setCost(item.getCost());
        dto.setUsefulLife(item.getUsefulLife());
        dto.setTaxCalculationType(item.getTaxCalculationType());
        Optional.ofNullable(item.getFixedAssetAccount())
                .map(e -> new IdNameTO(e.getId(), e.getName()))
                .ifPresent(dto::setFixedAssetAccount);
        Optional.ofNullable(item.getExpenseAccount())
                .map(e -> new IdNameTO(e.getId(), e.getName()))
                .ifPresent(dto::setExpenseAccount);
        Optional.ofNullable(item.getFinancedByAccount())
                .map(e -> new IdNameTO(e.getId(), e.getName()))
                .ifPresent(dto::setFinancedByAccount);
        Optional.ofNullable(item.getAccount()).map(SelectItem::getId).ifPresent(dto::setAccountId);
        Optional.ofNullable(item.getFinancedByAccount()).map(SelectItem::getId).ifPresent(dto::setFinanceAccountId);
        Optional.ofNullable(item.getOwner()).map(SelectItem::getId).ifPresent(dto::setOwnerId);
        Optional.ofNullable(item.getImageID())
                .map(i -> new IdNameTO(i, ""))
                .map(i -> (IdNameTO) i.addProperty("link", item.getImageLink()))
                .ifPresent(dto::setImage);
        Optional.ofNullable(item.getOwner())
                .map(e -> new IdNameTO(e.getId(), e.getName()))
                .ifPresent(dto::setOwner);
        Optional.ofNullable(item.getResidualValue()).ifPresent(dto::setResidualValue);

        return dto;
    }

    public static CandidateDTO toCandidateDto(ContactListItem candidate, List<FileResource> files) {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(candidate.getObjectId());
        dto.setTitle(new IdCode(candidate.getTitleId(), candidate.getTitle()));
        dto.setNumber(candidate.getNumberData().getNumberString());
        dto.setFirstName(candidate.getFirstName());
        dto.setLastName(candidate.getLastName());
        if (candidate.getBirthDate() != null) {
            dto.setDateOfBirth(candidate.getBirthDate().getNonConvertedDate());
        }
        dto.setStatus(new ItemDto(null, candidate.getStatus()));
        if (candidate.getProjectItem() != null) {
            dto.setProject(new ItemDto(candidate.getProjectItem().getId(), candidate.getProjectItem().getName(), candidate.getProjectItem().getCode()));
        }
        if (candidate.getCandidateSource() != null) {
            dto.setSource(new ItemDto(candidate.getCandidateSource().getId(), candidate.getCandidateSource().getName()));
        }
        if (candidate.getWorkExperienceMonthOrYear() != null) {
            dto.setWorkExperience(new IdCode(candidate.getWorkExperience(), String.valueOf(candidate.getWorkExperienceMonthOrYear())));
        }
        dto.setExpectedSalary(candidate.getExpectedSalary());
        dto.setCurrentEmployer(candidate.getCurrentEmployer());
        dto.setSkills(candidate.getSkills());
        if (candidate.getVacancyItems() != null) {
            List<VacancyDTO> vacancies = new ArrayList<>();
            candidate.getVacancyItems().forEach(v -> {
                VacancyDTO vacancy = new VacancyDTO();
                vacancy.setId(v.getObjectID());
                vacancy.setJobTitle(v.getJobTitle());
                vacancy.setJobTitleLocale(toLocaleDto(v.getReferenceLocale()));
                if (v.getDepartment() != null && v.getDepartment() instanceof ReferenceItem dep) {
                    vacancy.setDepartment(new IdName(dep.getId(), dep.getName(), toLocaleDto(dep.getLocale())));
                }
                if (v.getLocation() != null && v.getLocation() instanceof ReferenceItem loc) {
                    vacancy.setLocation(new IdName(loc.getId(), loc.getName(), toLocaleDto(loc.getLocale())));
                }
                if (v.getPositions() != null && v.getPositions().length > 0 && v.getPositions()[0] instanceof ReferenceItem pos) {
                    vacancy.setPosition(new IdName(pos.getId(), pos.getName(), toLocaleDto(pos.getLocale())));
                }
                vacancies.add(vacancy);
            });
            dto.setVacancies(vacancies);
        }
        if (candidate.getPreferredLocation() != null) {
            dto.setLocation(new IdName(candidate.getPreferredLocation().getId(), candidate.getPreferredLocation().getName()));
        }
        dto.setOwner(new IdName(candidate.getOwnerId(), candidate.getOwner()));
        if (candidate.getSpokingLanguages() != null) {
            List<LanguagesDto> languages = new ArrayList<>();
            candidate.getSpokingLanguages().forEach(l -> languages.add(new LanguagesDto(new IdName(l.getLanguage().getId(), l.getLanguage().getName()), new IdName(l.getLevel().getId(), l.getLevel().getName()))));
            dto.setLanguages(languages);
        }
        dto.setEmail(candidate.getPrimaryEmail());
        dto.setPhone(candidate.getPrimaryPhone());

        if (candidate.getImAddress() != null) {
            List<IMWebAddressDto> imAddresses = new ArrayList<>();
            for (SelectItem imAddress : candidate.getImAddress()) {
                imAddresses.add(new IMWebAddressDto(imAddress.getName(), imAddress.getDescription()));
            }
            dto.setImAddresses(imAddresses);
        }
        if (candidate.getWebSites() != null) {
            List<IMWebAddressDto> webAddresses = new ArrayList<>();
            for (SelectItem webAddress : candidate.getWebSites()) {
                webAddresses.add(new IMWebAddressDto(webAddress.getName(), webAddress.getDescription()));
            }
            dto.setWebAddresses(webAddresses);
        }
        if (candidate.getAllowanceCategories() != null) {
            List<AllowanceDto> allowances = new ArrayList<>();
            candidate.getAllowanceCategories().forEach(a -> allowances.add(new AllowanceDto(new ItemDto(a.getCategoryItem().getId(), a.getCategoryItem().getName(), a.getCategoryItem().getCode()), a.getAmount())));
            dto.setAllowances(allowances);
        }
        if (candidate.getNotes() != null) {
            List<NoteDto> notes = new ArrayList<>();
            candidate.getNotes().forEach(n -> notes.add(toDto(n)));
            dto.setNotes(notes);
        }
        if (candidate.getAddresses() != null) {
            List<AddressDto> addresses = new ArrayList<>();
            candidate.getAddresses().forEach(a -> addresses.add(toDto(a)));
            dto.setAddresses(addresses);
        }
        if (candidate.getCustomFields() != null && !candidate.getCustomFields().isEmpty()) {
            dto.setCustomFields(candidate.getCustomFields().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }
        dto.setCreatedAt(candidate.getCreatedDate());
        dto.setUpdatedAt(candidate.getUpdatedDate());
        if (files != null && !files.isEmpty()) {
            List<AttachmentTO> attachments = new ArrayList<>();
            for (FileResource file : files) {
                AttachmentTO attachment = new AttachmentTO();
                attachment.setFile_name(file.getFileName());
                attachment.setLink(file.getDownloadUrl());
                attachments.add(attachment);
            }
            dto.setAttachments(attachments);
        }
        return dto;
    }

    public static LocaleDto toLocaleDto(ReferenceLocale item) {
        if (item == null)
            return null;
        LocaleDto dto = new LocaleDto();
        dto.setEnglish(item.getEnglish());
        dto.setRussian(item.getRussian());
        dto.setUzbek(item.getUzbek());
        dto.setArabian(item.getArabic());
        return dto;
    }

    public static VacancyDTO toVacancyDTO(VacancyItem item, List<FileResource> files) {
        VacancyDTO dto = new VacancyDTO();

        dto.setId(item.getObjectID());
        dto.setJobTitle(item.getJobTitle());
        dto.setDescription(item.getDescription());
        dto.setRequirements(item.getJobRequirements());

        if (item.getStatus() != null) {
            ItemDto itemDto = new ItemDto(item.getStatus().getId(), item.getStatus().getName());
            itemDto.addProperty("percentage", item.getStatus().getTextDescription());
            dto.setStatus(itemDto);
        }
        if (item.getJobType() != null) {
            dto.setType(new ItemDto(item.getJobType().getId(), item.getJobType().getName()));
        }
        dto.setVacantPlaceCount(item.getVacantPlaces());
        dto.setProposedSalary(item.getProposedSalary());

        dto.setGender(item.getGender());
        dto.setStartDate(item.getStartDate());
        dto.setEndDate(item.getEndDate());

        if (item.getNumberData() != null) {
            dto.setNumber(item.getNumberData().getNumberString());
        }
        if (item.getLocationItem() != null) {
            dto.setLocation(new IdName(item.getLocationItem().getID(), item.getLocationItem().getName()));
        }
        if (item.getPositionItem() != null) {
            dto.setPosition(new IdName(item.getPositionItem().getId(), item.getPositionItem().getName()));
        }
//        if (item.getCountryId() != null) {
//            dto.setCountry(new IdName(item.getCountryId(), item.getCountryName()));
//        }
//        if (item.getEmbassyId() != null) {
//            dto.setEmbassy(new IdName(item.getEmbassyId(), item.getEmbassyName()));
//        }

        dto.setContractStartDate(item.getContractFrom());
        dto.setContractEndDate(item.getContractTo());
        if (item.getProjectId() != null) {
            dto.setProject(new ItemDto(item.getProjectId(), item.getProjectName()));
        }
        if (item.getManager() != null) {
            dto.setManager(new IdName(item.getManager().getId(), item.getManager().getName()));
        }
//        if (item.getReligionId() != null) {
//            dto.setReligion(new ItemDto(item.getReligionId(), item.getReligionName()));
//        }

        if (item.getRequiredDegree() != null) {
            dto.setRequiredDegree(new IdName(item.getRequiredDegree().getId(), item.getRequiredDegree().getName()));
        }
        dto.setResponsibilities(item.getResponsibility());
        if (item.getJobfamily() != null) {
            dto.setJobFamily(new IdName(item.getJobfamily().getId(), item.getJobfamily().getName()));
        }
        if (item.getJobType() != null) {
            dto.setJobType(new IdName(item.getJobType().getId(), item.getJobType().getName()));
        }

        if (item.getVacancyNotes() != null) {
            List<NoteDto> notes = new ArrayList<>();
            item.getVacancyNotes().forEach(n -> notes.add(toDto(n)));
            dto.setNotes(notes);
        }

        if (item.getCustomFieldItems() != null && !item.getCustomFieldItems().isEmpty()) {
            dto.setCustomFields(item.getCustomFieldItems().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }

        if (files != null && !files.isEmpty()) {
            List<AttachmentTO> attachments = new ArrayList<>();
            for (FileResource file : files) {
                AttachmentTO attachment = new AttachmentTO();
                attachment.setFile_name(file.getFileName());
                attachment.setLink(file.getDownloadUrl());
                attachments.add(attachment);
            }
            dto.setAttachments(attachments);
        }

        return dto;
    }

    public static ProjectDTO toDto(ProjectViewItem item, List<FileResource> files, List<HistoryListItem> notes, EdsUser user) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(item.getObjectID());
        dto.setNumber(item.getNumberData() != null ? item.getNumberData().getNumberString() : null);
        dto.setName(item.getName());

        if (item.getClientId() != null) {
            dto.setCustomer(new ItemDto(item.getClientId(), item.getClient()));
        }
        dto.setStartDate(user.getUserDate(item.getStartDate()));
        dto.setDueDate(user.getUserDate(item.getDueDate()));

        if (item.getLocationID() != null) {
            dto.setLocation(new IdName(item.getLocationID(), item.getProjectLocation()));
        }
        dto.setStatus(new ItemDto(item.getStatusID(), item.getStatus(), item.getStatusCode()));
        dto.setDescription(item.getDescription());
        dto.setBillable(item.isBillable());

        if (item.getProjectEmployees() != null && item.getProjectEmployees().length > 0) {
            List<ProjectEmployeeDTO> employees = new ArrayList<>();
            for (PositionsSelectItem employeeItem : item.getProjectEmployees()) {
                ProjectEmployeeDTO employee = new ProjectEmployeeDTO();
                employee.setEmployee(new ItemDto(employeeItem.getId(), employeeItem.getName(), employeeItem.getEmployeeNumber()));
                if (employeeItem.getDepartmentId() != null) {
                    employee.setDepartment(new IdName(employeeItem.getDepartmentId(), employeeItem.getDepartmentName()));
                }
                if (employeeItem.getPositionId() != null) {
                    employee.setPosition(new IdName(employeeItem.getPositionId(), employeeItem.getPositionName()));
                }
                employee.setEstimatedTime(employeeItem.getTime());
                employee.setTimeSpent(employeeItem.getTimeSpent());
                employee.setActualTime(employeeItem.getActualTime());
                employee.setCompleted(employeeItem.getPercent());
                employee.setProjectEmployeeId(employeeItem.getEmployeeId());
                employees.add(employee);
            }
            dto.setEmployees(employees);
        }
        dto.setManager(new ItemDto(item.getManagerId(), item.getManager()));

        if (item.getBackupManagers() != null && !item.getBackupManagers().isEmpty()) {
            List<ItemDto> backups = new ArrayList<>();
            item.getBackupManagers().forEach(b -> backups.add(new ItemDto(b.getId(), b.getName(), b.getCode())));
            dto.setBackupManagers(backups);
        }

        if (notes != null && !notes.isEmpty()) {
            List<NoteDto> projectNotes = new ArrayList<>();
            notes.forEach(n -> projectNotes.add(toDto(n)));
            dto.setNotes(projectNotes);
        }

        if (item.getCustomFields() != null && !item.getCustomFields().isEmpty()) {
            dto.setCustomFields(item.getCustomFields().stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }

        if (files != null && !files.isEmpty()) {
            List<AttachmentTO> attachments = new ArrayList<>();
            for (FileResource file : files) {
                AttachmentTO attachment = new AttachmentTO();
                attachment.setFile_name(file.getFileName());
                attachment.setLink(file.getDownloadUrl());
                attachments.add(attachment);
            }
            dto.setAttachments(attachments);
        }
        dto.setCreatedAt(item.getCreationDate());
        dto.setUpdatedAt(item.getLastUpdateTime());
        dto.setCreatedBy(new IdName(item.getCreatorID(), item.getCreator()));
        dto.setUpdatedBy(new IdName(null, item.getLastUpdaterName()));
        dto.setCheckInLocations(item.getCheckInLocations());
        return dto;
    }

    public static ProjectDTO toDto(ProjectListItem item, EdsUser user) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(item.getObjectId());
        dto.setNumber(item.getNumber());
        dto.setName(item.getName());
        dto.setCustomer(new ItemDto(null, item.getClient()));
        dto.setStartDate(user.getUserDate(item.getStartDate()));
        dto.setDueDate(user.getUserDate(item.getDueDate()));

        if (item.getProjectLocationId() != null) {
            dto.setLocation(new IdName(item.getProjectLocationId(), item.getProjectLocation()));
        }
        dto.setStatus(new ItemDto(item.getStatusId(), item.getStatus(), item.getStatusCode()));
        dto.setDescription(item.getDescription());
        dto.setBillable(item.getBillable() != null ? item.getBillable() : false);
        dto.setManager(new ItemDto(item.getManagerId(), item.getManager()));
        dto.setCreatedAt(item.getCreatedDate());
        dto.setUpdatedAt(item.getModifiedDate());
        dto.setCreatedBy(new IdName(item.getProjectCreatorID(), item.getCreatedBy()));
        dto.setUpdatedBy(new IdName(null, item.getModifiedBy()));
        return dto;
    }

    public static DependentDTO toDto(DependentItem item) {
        DependentDTO dto = new DependentDTO();
        dto.setFirstName(item.getFirstName());
        dto.setLastName(item.getLastName());
        dto.setPhone(item.getPhone1() != null ? item.getPhone1() : item.getPhone2());
        dto.setRelationShip(item.getRelationship());
        dto.setCity(item.getCity());
        dto.setCountry(new SelectItem(item.getCountryId(), item.getCountryName()));
        return dto;
    }

    public static LeaveRequestDTO toDto(StatisticsLeaveRequest item, List<FileResource> files, Integer userId) {
        HrmsServiceLocal hrmsServiceLocal = (HrmsServiceLocal) ApplicationContextProvider.applicationContext.getBean("hrmsService");
        WfmMessageSource referenceMessageLocalizer = (WfmMessageSource) ApplicationContextProvider.applicationContext.getBean("referenceWfmMessageSource");
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(item.getObjectID());
        String employeeImgeUrl = hrmsServiceLocal.getEmployeeImageURL(item.getEmployeeId());
        dto.setEmployee(new ItemDto(item.getEmployeeId(), item.getEmployee(), employeeImgeUrl));
        dto.setReason(new ItemDto(item.getReasonId(), item.getReason(), item.getReasonCode()));
        dto.setStartDate(item.getStartDDate().getNonConvertedDate());
        dto.setEndDate(item.getEndDDate().getNonConvertedDate());
        dto.setDescription(item.getDescription());
        dto.setType(item.getType());
        if (item.getOverallStatus() != null) {
            dto.setStatus(new ApproverListStatusTO(
                    item.getOverallStatus().getCode(),
                    referenceMessageLocalizer.localize(item.getOverallStatus().getCode(), item.getOverallStatus().getName()))
            );
        }
        dto.setTakeLeaveBy(item.getTakeByMoney() ? Constants.MONEY : Constants.DAY);
        List<ItemDto> approvers = new ArrayList<>();
        for (ApproverItemMini approver : item.getApprovers()) {
            String approverImageUrl = hrmsServiceLocal.getEmployeeImageURL(approver.getExactEmployee().getId());
            approvers.add(new ItemDto(approver.getExactEmployee().getId(), approver.getExactEmployee().getName(), approverImageUrl));
        }
        dto.setApprover(approvers);
        if (files != null && !files.isEmpty()) {
            List<AttachmentTO> attachments = new ArrayList<>();
            for (FileResource file : files) {
                AttachmentTO attachment = new AttachmentTO();
                attachment.setFile_name(file.getFileName());
                attachment.setLink(file.getDownloadUrl());
                attachments.add(attachment);
            }
            dto.setAttachments(attachments);
        }
        if (item.getCurrectApproverId() != null) {
            dto.setCurrentApproverId(item.getCurrectApproverId());
            dto.setCurrentApprover(Objects.equals(item.getCurrectApproverId(), userId));
        }
        dto.setCustomFields(item.getCustomFields().stream().map(CustomFieldsUtils::getCustomFieldDto).collect(Collectors.toList()));
        Optional.ofNullable(item.getStartDDate())
                .map(DateNonConvertable::getNonConvertedDate)
                .ifPresent(dto::setStartDateUTC);
        Optional.ofNullable(item.getEndDDate())
                .map(DateNonConvertable::getNonConvertedDate)
                .ifPresent(dto::setEndDateUTC);
        return dto;
    }

    public static CertificateDto toDTO(CertificateItem item) {
        CertificateDto dto = new CertificateDto();
        dto.setId(item.getObjectId());
        dto.setCertificateNumber(item.getCertificateNumber() != null ? item.getCertificateNumber().getNumberString() : null);
        dto.setEmployee(item.getEmployee() != null ? new ItemDto(item.getEmployee().getId(), item.getEmployee().getName(), item.getEmployeeCode()) : null);
        dto.setType(item.getCertificateType() != null ? new IdName(item.getCertificateType().getId(), item.getCertificateType().getName()) : null);
        dto.setContent(item.getCustomHTMLcontent() != null ? item.getCustomHTMLcontent() : item.getContent());
        CertificateDynamicFieldsDto fields = new CertificateDynamicFieldsDto();
        fields.setTextBox1(item.getTextBox1());
        fields.setTextBox2(item.getTextBox2());
        fields.setTextBox3(item.getTextBox3());
        fields.setTextBox4(item.getTextBox4());
        fields.setTextBox5(item.getTextBox5());
        fields.setTextBox6(item.getTextBox6());
        fields.setTextBox7(item.getTextBox7());
        fields.setTextBox8(item.getTextBox8());
        fields.setTextBox9(item.getTextBox9());
        fields.setTextBox10(item.getTextBox10());
        fields.setTextBox11(item.getTextBox11());
        fields.setTextBox12(item.getTextBox12());
        fields.setTextBox13(item.getTextBox13());
        fields.setTextBox14(item.getTextBox14());
        fields.setTextBox15(item.getTextBox15());
        fields.setTextBox16(item.getTextBox16());
        fields.setTextBox17(item.getTextBox17());
        fields.setTextBox18(item.getTextBox18());
        fields.setTextArea1(item.getTextArea1());
        fields.setTextArea2(item.getTextArea2());
        fields.setTextArea3(item.getTextArea3());
        fields.setTextArea4(item.getTextArea4());
        fields.setTextArea5(item.getTextArea5());
        fields.setTextArea6(item.getTextArea6());
        fields.setTextArea7(item.getTextArea7());
        fields.setTextArea8(item.getTextArea8());
        dto.setDynamicFields(fields);
        return dto;
    }

    public static CustomFormDto toDto(FormItems item, List<HistoryListItem> notes, EdsUser user) {
        CustomFormDto dto = new CustomFormDto();
        dto.setId(item.getObjectID());
        dto.setObjectKey(item.getObjectKey());
        dto.setForm(new IdCode(null, item.getFormID()));
        dto.setCustomFields(item.getCustomFieldItems().stream().map(CustomFieldsUtils::getCustomFieldDto).collect(Collectors.toList()));

        if (notes != null && !notes.isEmpty()) {
            List<NoteDto> noteDtos = new ArrayList<>();
            notes.forEach(n -> noteDtos.add(toDto(n)));
            dto.setNotes(noteDtos);
        }

        dto.setCreatedAt(user.getUserDate(item.getCreatedDate()));
        dto.setUpdatedAt(user.getUserDate(item.getModifiedData()));
        dto.setCreatedBy(new IdName(null, item.getCreator()));
        dto.setUpdatedBy(new IdName(null, item.getUpdater()));

        dto.setRelationObjectKey(item.getRelationObjectKey());
        dto.setRelationId(item.getRelationId());
        dto.setRelationType(item.getRelationType());

        dto.setDuration(item.getDurationTime());
        dto.setAttempt(item.getAttempt());
        dto.setScore(item.getScore());

        if (item.getDurationTime() != null) {
            String[] duration = item.getDurationTime().split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(item.getCreatedDate());

            calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(duration[0]));
            calendar.add(Calendar.MINUTE, Integer.valueOf(duration[1]));
            calendar.add(Calendar.SECOND, Integer.valueOf(duration[2]));

            dto.setEndAt(user.getUserDate(calendar.getTime()));
        }

        if (item.getCustomFieldItems() != null) {
            dto.setTotalQuestions(item.getCustomFieldItems().size());
            dto.setAnsweredQuestions((int) item.getCustomFieldItems().stream().filter(cf -> cf.getFieldStringValue() != null && !"".equals(cf.getFieldStringValue())).count());
            dto.setCorrectAnswers((int) item.getCustomFieldItems().stream().filter(cf -> {
                if (StringUtils.isBlank(cf.getFieldStringValue()) || StringUtils.isBlank(cf.getQuizFormScoreValues())) {
                    return false;
                }
                double score = 0;
                try {
                    score = new JSONObject(cf.getQuizFormScoreValues()).getDouble(cf.getFieldStringValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return score > 0;
            }).count());
        }
        return dto;
    }

    public static OpportunityContactTO toOpportunityContactTo(ContactListItem contactItem) {
        OpportunityContactTO to = new OpportunityContactTO();
        to.setEmails(contactItem.getEmails());
        to.setPhoneNumbers(contactItem.getPhoneNumbers());
        to.setAddress(contactItem.getAddress());
        to.setImAddress(contactItem.getImAddress());
        to.setWebSites(contactItem.getWebSites());
        to.setCountries(contactItem.getCountries());
        to.setCities(contactItem.getCities());
        to.setStates(contactItem.getStates());
        to.setPostCods(contactItem.getPostCods());
        to.setPrimaryEmail(contactItem.getPrimaryEmail());
        to.setPrimaryAddress(contactItem.getPrimaryAddress());
        to.setPrimaryPhone(contactItem.getPrimaryPhone());
        to.setBirthDate(contactItem.getBirthDate());
        to.setAddresses(contactItem.getAddresses());
        to.setTelegramChats(contactItem.getTelegramChats());

        return to;
    }

    public static FileItem[] toFileItem(List<AttachmentTO> attachments) {
        if (attachments == null || attachments.isEmpty()) return null;
        FileItem[] fileItems = new FileItem[attachments.size()];
        for (int i = 0; i < attachments.size(); i++) {
            AttachmentTO attachment = attachments.get(i);
            FileItem fileItem = new FileItem();
            fileItem.setFileName(attachment.getFile_name());
            fileItems[i] = fileItem;
        }
        return fileItems;
    }

    public static ArrayList<AttachmentTO> toDto(ArrayList<FileResource> fileResources) {
        if (fileResources == null) return new ArrayList<>();
        return fileResources.stream()
                .map(a -> new AttachmentTO(a.getFileName(), a.getDownloadUrl()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<EmployeeDocsTO> toEmpDocsTo(ArrayList<FileResource> fileResources) {
        if (fileResources == null) return new ArrayList<>();
        return fileResources.stream()
                .map(a -> new EmployeeDocsTO(new AttachmentTO(a.getFileName(), a.getDownloadUrl()), new IdNameTO(a.getOwnerId(), a.getOwnerName())))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static OpportunityListItem fromDto(OpportunityConvertTO opportunity) {
        OpportunityListItem item = new OpportunityListItem();
        item.setAssigneeId(opportunity.getAssignee().getId());
        item.setOpportunityName(opportunity.getName());
        item.setAmount(opportunity.getAmount());
        item.setStageId(opportunity.getStage().getId());
        item.setCopyLeadDetails(opportunity.isCopyLeadDetails());
        return item;
    }

    public static ProductDto toDto(EdsItem newProduct, ArrayList<CompanyCustomFieldItem> customFieldItems) {
        ProductDto productDto = new ProductDto();
        productDto.setId(newProduct.getObjectID());
        productDto.setObjectKey(newProduct.getObjectKey());
        productDto.setName(newProduct.getName());
        productDto.setNumber(newProduct.getProductNumber());
        productDto.setType(newProduct.getTypeName());
        productDto.setDescription(newProduct.getDescription());
        productDto.setActive(newProduct.isActive());

        customFieldItems.stream()
                .filter(i -> i.getFieldStringValue() != null && !"".equals(i.getFieldStringValue()))
                .forEach(i -> i.setFieldStringValue(null));

        ArrayList<CompanyCustomFieldItem> customFieldsItems = CustomFieldsUtils.setRPCCustomFieldItems(newProduct.getCustomFields(), customFieldItems);
        productDto.setCustomFields(customFieldsItems.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        productDto.setSellingPrice(newProduct.getSellingPrice());
        productDto.setPurchasePrice(newProduct.getUnitPrice());
        productDto.setSoldToCustomer(newProduct.isSoldToCustomer());
        Optional.ofNullable(newProduct.getAccount())
                .map(a -> {
                    IdCode idCode = new IdCode(a.getObjectID(), a.getAccountCode());
                    idCode.addProperty("name", a.getName());
                    return idCode;
                })
                .ifPresent(productDto::setSalesAccount);
        Optional.ofNullable(newProduct.getCogsAccount())
                .map(c -> {
                    IdCode idCode = new IdCode(c.getObjectID(), c.getAccountCode());
                    idCode.addProperty("name", c.getName());
                    return idCode;
                })
                .ifPresent(productDto::setPurchaseAccount);
        productDto.setPurchasedFromSupplier(newProduct.isPurchasedFromSupplier());
        Optional.ofNullable(newProduct.getCategory())
                .map(c -> new IdCode(c.getObjectID(), c.getName()))
                .ifPresent(productDto::setCategory);
        Optional.ofNullable(newProduct.getBrand())
                .map(b -> new IdCode(b.getObjectID(), b.getName()))
                .ifPresent(productDto::setBrand);

        if (newProduct.getDiscounts() != null) {
            List<IdCode> discounts = newProduct.getDiscounts().stream()
                    .map(d -> new IdCode(d.getObjectID(), d.getName()))
                    .toList();
            productDto.setDiscounts(discounts);
        }
        Optional.ofNullable(newProduct.getVat())
                .map(EdsVat::getObjectID)
                .ifPresent(productDto::setTaxId);

        Optional.ofNullable(newProduct.getVat())
                .map(taxItem -> new IdCode(taxItem.getObjectID(), taxItem.getName()))
                .ifPresent(productDto::setBrand);

        productDto.setSkuNumber(newProduct.getInternalSKUNumber());
        Optional.ofNullable(newProduct.getUnitMeasurement())
                .map(um -> new IdCode(um.getObjectID(), um.getName()))
                .ifPresent(productDto::setUnitMeasurement);
        productDto.setPartNumber(newProduct.getPartNumber());
        productDto.setUpcNumber(newProduct.getUpcNumber());
        productDto.setManufacturer(newProduct.getManufacturer());
        if (newProduct.getSuppliers() != null) {
            List<IdCode> suppliers = newProduct.getSuppliers().stream()
                    .map(s -> new IdCode(s.getObjectID(), s.getName()))
                    .toList();
            productDto.setSuppliers(suppliers);
        }
        if (ProductTypeEnum.INVENTORY_ITEM.getId().equals(newProduct.getType()) || ProductTypeEnum.ASSEMBLY_ITEM.getId().equals(newProduct.getType())) {
            InventoryStockInformationDto inventoryStockInformationDto = new InventoryStockInformationDto();
            Optional.ofNullable(newProduct.getAsOf()).ifPresent(inventoryStockInformationDto::setAsOf);
            inventoryStockInformationDto.setBatchSerialnumber(newProduct.getBatchTrackingEnabled());
            inventoryStockInformationDto.setTrackSerialnumber(newProduct.getInventoryTrackingEnabled());
            inventoryStockInformationDto.setTrackBatches(newProduct.getTrackBatchesEnabled());
            List<ProductLocationDto> productLocations = new ArrayList<>();
            inventoryStockInformationDto.setProductLocations(productLocations);
            inventoryStockInformationDto.addProperty("totalValue", newProduct.getTotalValue());
            productDto.setInventoryStockInformation(inventoryStockInformationDto);
        }
        if (newProduct.getDefaultWarehouse() != null) {
            Optional.ofNullable(newProduct.getDefaultWarehouse())
                    .map(d -> new ItemDto(d.getObjectID(), d.getName()))
                    .ifPresent(productDto::setDefaultWarehouse);
        }

        productDto.setCreatedAt(newProduct.getCreationDate());
        productDto.setUpdatedAt(newProduct.getLastUpdateTime());

        return productDto;

    }

    public static LeaveRequestDTO toDto(LeaveRequestLisItem item, Integer userId) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(item.getObjectId());
        dto.setEmployee(new ItemDto(item.getEmployeeId(), item.getEmployeeName()));
        Integer reasonId = null;
        try {
            reasonId = Integer.parseInt(item.getReasonID());
        } catch (NumberFormatException e) {
            // ignore
        }
        dto.setReason(new ItemDto(reasonId, item.getReason(), item.getReasonCode()));
        dto.setStartDate(item.getStartDate().getDate());
        dto.setEndDate(item.getEndDate().getDate());
        dto.setDescription(item.getDescription());
        dto.setType(item.getType());
        dto.setLeaveDays(item.getLeaveDays());
        if (item.getStatus() != null) {
            dto.setStatus(new ApproverListStatusTO(item.getStatusCode(), item.getStatus()));
        }
        dto.setCurrentApproverId(item.getApproverId());
        dto.setCurrentApprover(Objects.equals(item.getApproverId(), userId));
        if (item.getCustomFields() != null && item.getCustomFields().values() != null) {
            List<CustomFieldDto> customFields = item.getCustomFields().values().stream()
                    .filter(cf -> cf instanceof CompanyCustomFieldItem)
                    .map(cf -> (CompanyCustomFieldItem) cf)
                    .map(CustomFieldsUtils::getCustomFieldDto)
                    .collect(Collectors.toList());
            dto.setCustomFields(customFields);
        }
        Optional.ofNullable(item.getStartDate())
                .map(DateNonConvertable::getNonConvertedDate)
                .ifPresent(dto::setStartDateUTC);
        Optional.ofNullable(item.getEndDate())
                .map(DateNonConvertable::getNonConvertedDate)
                .ifPresent(dto::setEndDateUTC);
        return dto;
    }

    public static ExpenseDto toDto(ExpenseReportsListItem item) {
        ExpenseDto dto = new ExpenseDto();
        dto.setId(item.getId());
        dto.setNumber(item.getExpenseNumber());
        dto.setDate(item.getStartDate() != null ? item.getStartDate().getDate() : null);
        dto.setReportTitle(item.getTitle());
        dto.setStatus(item.getStatus());
        dto.setStatusColor(item.getStatusColor());
        dto.setTotal(item.getTotal());
        return dto;
    }

    public static ProductDto toDto(ProductItem productItem) {
        ProductDto dto = new ProductDto();
        dto.setId(productItem.getObjectId());
        dto.setName(productItem.getName());
        dto.setNumber(productItem.getProductNumber());
        dto.setType(productItem.getTypeName());
        dto.setDescription(productItem.getDescription());
        return dto;
    }

    public static CrmAccountItem toAccountItem(CrmAccountTO to) {
        if (to == null) return null;
        CrmAccountItem item = new CrmAccountItem();
        item.setObjectId(to.getItem_id());
        item.setName(to.getName());
        return item;
    }

}
