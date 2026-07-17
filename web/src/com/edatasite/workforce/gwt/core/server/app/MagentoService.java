package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.profile.client.rpc.MagentoSettingsItem;
import com.google.code.magja.model.customer.CustomerAddress;
import com.google.code.magja.service.ServiceException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shohruh on 06 Dec 2016.
 */
public interface MagentoService {

    MagentoSettingsItem getMagentoSettings();

    void saveMagentoSettings(MagentoSettingsItem magentoSettings);

    void synchronizeWithMagentoCatalog();

    void resetMagentoSynchronization();

    String syncProduct(Integer itemID, String syncType, HashMap<Integer, String> magentoWebSites);

    List<String> syncProducts();

    void resetProducts();

    void syncProductPicture(EdsProductPicture picture);

    void syncProductPictures();

    void resetProductPictures();

    void syncCategory(EdsProductCategory productCategory);

    void syncCategories();

    void resetCategories();

    void deleteCategory(Integer categoryId);

    void syncInventories();

    void syncInventory(EdsItem item, BigDecimal qtyDiff);

    void syncInventory(EdsItem item, BigDecimal qtyDiff, boolean remove);

    EdsCrmAccount syncCustomer(Integer customerId) throws ServiceException;

    void syncCustomerAddresses(CrmAccountItem crmAccountItem, List<CustomerAddress> cAddresses);

    void syncOrder(Integer orderId) throws ServiceException;

    void emailOrderToCustomer(Integer orderID, EdsCrmAccount crmAccount);

}
