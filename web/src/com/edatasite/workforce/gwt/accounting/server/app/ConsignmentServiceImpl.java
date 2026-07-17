package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsConsignment;
import com.edatasite.workforce.core.domain.accounting.EdsConsignmentItem;
import com.edatasite.workforce.core.domain.accounting.EdsSubsidiariesCompany;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.LayoutManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ConsignmentManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SubsidiariesCompanyManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Normurod on 6/15/15.
 */
@Transactional
@Service("consignmentService")
public class ConsignmentServiceImpl implements ConsignmentService, ConsignmentServiceLocal, Constants {

    @Autowired
    private ConsignmentManager consignmentManager;

    @Autowired
    private NumberingSettingsManager numberingSettingsManager;

    @Autowired
    private RabbitMQService rabbitMQService;

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private SubsidiariesCompanyManager subsidiariesCompanyManager;

    @Autowired
    private LayoutManager layoutManager;

    @Autowired
    private CrmAccountManager crmAccountManager;

    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private ReferenceManager referenceManager;

    @Override
    public ListResult<Consignment> getConsignmentList(ListingFilterParameter filterParameter) {
        List<EdsConsignment> list = consignmentManager.list(filterParameter);

        ArrayList<Consignment> consignments = new ArrayList<>();
        if (!list.isEmpty()) {
            for (EdsConsignment consignment : list) {
                consignments.add(consignment.getRPC());
            }
        }
        return new ListResult<>(consignments, consignmentManager.listCount(filterParameter));
    }

    @Override
    public Consignment getConsignmentData(Integer objectID) {
        Consignment consignment = new Consignment();

        if (objectID != null) {
            EdsConsignment edsConsignment = consignmentManager.get(objectID);
            consignment = edsConsignment.getRPC();
            consignment.setNumberData(generateNumber());
            consignment.getNumberData().setIntNumber(edsConsignment.getIntNumber());
            consignment.getNumberData().setNumberString(edsConsignment.getNumber());
        } else {
            consignment.setNumberData(generateNumber());
        }

        consignment.setLayoutHtml(layoutManager.getLayoutHTML(LayoutRPC.CONSIGNMENT_FORM));
        consignment.setSubsidiaries(getSubsidiaries());

        return consignment;
    }

    @Override
    public Integer save(Consignment consignment) {
        EdsConsignment edsConsignment = new EdsConsignment();
        NumberData numberData = consignment.getNumberData();
        EdsUser user = consignmentManager.getUser();

        if (consignment.getObjectID() != null) {
            edsConsignment = consignmentManager.get(consignment.getObjectID());
        } else {
            edsConsignment.setCreator(consignmentManager.getUser());
            edsConsignment.setCreationDate(consignmentManager.getUser().getUserDate());
        }

        if (numberData.getIntNumber() != null) {
            if (!consignmentManager.isConsignmentNumberExists(numberData.getNumberString(), consignment.getObjectID())) {
                edsConsignment.setNumber(numberData.getNumberString());
                edsConsignment.setIntNumber(numberData.getIntNumber());
            } else {
                numberData = generateNumber();
                edsConsignment.setNumber(numberData.getNumberString());
                edsConsignment.setIntNumber(numberData.getIntNumber());
            }
        } else {
            edsConsignment.setNumber(numberData.getNumberString());
        }

        edsConsignment.setName(consignment.getName());
        edsConsignment.setReference(consignment.getReference());
        edsConsignment.setDate(consignment.getDate().getNonConvertedDate());

        if (edsConsignment.getObjectID() != null) {
            consignmentManager.deleteConsignmentItems(edsConsignment.getObjectID());
        }

        consignmentManager.createOrUpdate(edsConsignment);
        edsConsignment.setSubsidiaryUniqNum(edsConsignment.getObjectID() + EdsConsignment.UNIQ_NUM + consignmentManager.getUser().getCompany().getObjectID());

        if (consignment.getItems() != null) {
            consignment.getItems();
            for (ConsignmentItem item : consignment.getItems()) {
                if (item.getFromCompany() == null) {
                    continue;
                }

                EdsConsignmentItem consignmentItem = new EdsConsignmentItem();
                EdsItem product = itemManager.get(item.getProduct().getId());

                if (item.getFromCompany() != null && item.getFromCompany().getId() != null) {
                    consignmentItem.setFrom(crmAccountManager.get(item.getFromCompany().getId()));

                    if (consignmentItem.getFrom().getSubsidiary() != null) {
                        consignmentItem.setFromCompanyID(consignmentItem.getFrom().getSubsidiary().getCompanyId());
                        consignmentItem.setFromCompany(consignmentItem.getFrom().getSubsidiary().getCompanyName());
                    } else if (item.getFromCompany().getId().equals(1)) {
                        consignmentItem.setFromCompanyID(user.getCompany().getObjectID());
                        consignmentItem.setFromCompany(user.getCompany().getName());
                    }
                }
                if (item.getToCompany() != null && item.getToCompany().getId() != null) {
                    consignmentItem.setTo(crmAccountManager.get(item.getToCompany().getId()));

                    if (consignmentItem.getTo().getSubsidiary() != null) {
                        consignmentItem.setToCompanyID(consignmentItem.getTo().getSubsidiary().getCompanyId());
                        consignmentItem.setToCompany(consignmentItem.getTo().getSubsidiary().getCompanyName());
                    } else if (item.getToCompany().getId().equals(1)) {
                        consignmentItem.setToCompanyID(user.getCompany().getObjectID());
                        consignmentItem.setToCompany(user.getCompany().getName());
                    }
                }
                consignmentItem.setProduct(product);
                consignmentItem.setQuantity(item.getQuantity());
                edsConsignment.addItem(consignmentItem);
            }
        }

        rabbitMQService.sendSubsidiariesConsignment(edsConsignment.getRPC(), consignmentManager.getUser().getCompany().getObjectID());
        return consignment.getObjectID();
    }

    @Override
    public void saveSubsidiariesConsignment(Consignment consignment) {
        EdsConsignment edsConsignment = consignmentManager.getConsignmentBySubsidiaryUniqNum(consignment.getSubsidiaryUniqNum());

        if (edsConsignment == null) {
            edsConsignment = new EdsConsignment();
            edsConsignment.setCreationDate(new Date());
        }

        edsConsignment.setSubsidiaryUniqNum(consignment.getSubsidiaryUniqNum());
        edsConsignment.setName(consignment.getName());
        edsConsignment.setReference(consignment.getReference());
        edsConsignment.setNumber(consignment.getNumber());
        edsConsignment.setDate(consignment.getDate().getNonConvertedDate());
        edsConsignment.setSubsidiaryConignment(true);
        edsConsignment.setDeleted(consignment.isDeleted());
        consignmentManager.createOrUpdate(edsConsignment);

        if (edsConsignment.getObjectID() != null) {
            consignmentManager.deleteConsignmentItems(edsConsignment.getObjectID());
        }

        Integer companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        if (consignment.getItems() != null) {
            consignment.getItems();
            for (ConsignmentItem item : consignment.getItems()) {

                System.out.println("COMPANY ID: " + companyID);
                System.out.println("FROM TO: " + item.getFromCompanyID() + " -> " + item.getToCompanyID());

                if (item.getFromCompanyID() != null && item.getToCompanyID() != null
                        && (item.getFromCompanyID().equals(companyID) || item.getToCompanyID().equals(companyID))) {

                    EdsConsignmentItem consignmentItem = new EdsConsignmentItem();
                    EdsItem product = itemManager.getInterCompanyProductByUniqueID(item.getProduct().getDescription().trim());
                    System.out.println("PRODUCT SNUM: " + item.getProduct().getDescription());

                    if (product == null) {
                        continue;
                    }

                    if (companyID.equals(item.getFromCompanyID())) {
                        consignmentItem.setFrom(crmAccountManager.get(1));
                        consignmentItem.setFromCompanyID(item.getFromCompanyID());
                    } else {
                        EdsCrmAccount from = crmAccountManager.getCrmAccountBySubsidiary(item.getFromCompanyID(), null);

                        if (from == null) {
                            EdsCompany company = companyManager.get(item.getFromCompanyID());
                            CrmAccountItem crmAccountItem = new CrmAccountItem(company.getName());
                            crmAccountItem.setName(company.getName());
                            Integer crmAccountID = crmServiceLocal.saveSubsidiaryCrmAccount(crmAccountItem, item.getFromCompanyID(), RECEIVABLE);

                            from = crmAccountManager.get(crmAccountID);

                            if (from == null) {
                                System.out.println("FROM: NULL ");
                                continue;
                            }
                        } else {
                            from.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER));
                        }

                        consignmentItem.setFrom(from);
                        consignmentItem.setFromCompanyID(item.getFromCompanyID());
                    }


                    if (companyID.equals(item.getToCompanyID())) {
                        consignmentItem.setTo(crmAccountManager.get(1));
                        consignmentItem.setToCompanyID(item.getToCompanyID());
                    } else {
                        EdsCrmAccount to = crmAccountManager.getCrmAccountBySubsidiary(item.getToCompanyID(), null);

                        if (to == null) {
                            EdsCompany company = companyManager.get(item.getFromCompanyID());
                            CrmAccountItem crmAccountItem = new CrmAccountItem(company.getName());
                            crmAccountItem.setName(company.getName());
                            Integer crmAccountID = crmServiceLocal.saveSubsidiaryCrmAccount(crmAccountItem, item.getToCompanyID(), PAYABLE);

                            if (to == null) {
                                System.out.println("TO: NULL ");
                                continue;
                            }
                        } else {
                            to.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                        }

                        consignmentItem.setTo(to);
                        consignmentItem.setToCompanyID(item.getToCompanyID());
                    }

                    consignmentItem.setProduct(product);
                    consignmentItem.setQuantity(item.getQuantity());
                    edsConsignment.addItem(consignmentItem);
                }
            }
        }

        if (edsConsignment.getItems() != null && !edsConsignment.getItems().isEmpty()) {
            consignmentManager.createOrUpdate(edsConsignment);
        } else if (edsConsignment.getObjectID() != null) {
            edsConsignment.setDeleted(true);
            consignmentManager.update(edsConsignment);
        }
    }

    @Override
    public Boolean deleteConsignment(Integer objectID) {
        if (objectID != null) {
            EdsConsignment consignment = consignmentManager.get(objectID);
            consignment.setDeleted(true);
            consignmentManager.update(consignment);
            rabbitMQService.sendSubsidiariesConsignment(consignment.getRPC(), Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
        }
        return true;
    }

    @Override
    public BigDecimal getAvailableStock(Integer clientID, Integer productID, Integer consignmentID) {
        BigDecimal availableQTY = BigDecimal.ZERO;

        if (productID != null) {
            EdsCrmAccount client = crmAccountManager.get(clientID);
            EdsCompany company = crmAccountManager.getUser().getCompany();

            if (clientID.equals(1) || client.getName().trim().equalsIgnoreCase(company.getName().trim())) { //company's organization(crmaccount of the company)
                EdsItem item = itemManager.get(productID);
                availableQTY = availableQTY.add(item.getQty());
            }

            BigDecimal consignmentQTY = consignmentManager.getConsignmentQty(clientID, productID, consignmentID);
            BigDecimal soldQTY = consignmentManager.getSoldQty(clientID, productID);

            if (consignmentQTY.compareTo(BigDecimal.ZERO) > 0 && consignmentQTY.compareTo(soldQTY) > 0) {
                consignmentQTY = consignmentQTY.compareTo(soldQTY) > 0 ? consignmentQTY.subtract(soldQTY) : BigDecimal.ZERO;
            }

            if (clientID.equals(1) || client.getName().trim().equalsIgnoreCase(company.getName().trim())) {
                List<Integer> clientIds = consignmentManager.getClientListByProduct(productID);

                if (!clientIds.isEmpty()) {
                    soldQTY = consignmentManager.getSoldQty(clientIds, productID);
                    if (consignmentQTY.compareTo(BigDecimal.ZERO) < 0) {
                        consignmentQTY = consignmentQTY.add(soldQTY);
                    }
                }
            }

            availableQTY = availableQTY.add(consignmentQTY);
        }
        return availableQTY;
    }

    private NumberData generateNumber() {
        Integer intNumber = consignmentManager.getLastInNumber();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings != null && settings.getConsignmentNumberingFormat() != null) {
            return settings.parseNumberData(intNumber, settings.getConsignmentNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_CONSIGNMENT_PREFIX);
        }
    }

    private SelectItem[] getSubsidiaries() {
        List<EdsSubsidiariesCompany> edsSubsidiariesCompanyList = subsidiariesCompanyManager.getSubsidiariesCompanies(new ListingFilterParameter());

        ArrayList<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(subsidiariesCompanyManager.getUser().getCompany().getObjectID(), subsidiariesCompanyManager.getUser().getCompany().getName()));

        for (EdsSubsidiariesCompany scompany : edsSubsidiariesCompanyList) {
            items.add(new SelectItem(scompany.getCompanyId(), scompany.getCompanyName()));
        }

        return items.toArray(new SelectItem[]{});
    }
}
