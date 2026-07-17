package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsDiscountMultiRangeValue;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.*;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.DiscountManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.DiscountMultiRangeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 4:44:34 PM
 */
@Transactional
@Service("discountService")
public class DiscountServiceImpl implements DiscountService, Constants {

    @Autowired
    private DiscountManager discountManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private DiscountMultiRangeManager discountMultiRangeManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DiscountList getDiscountList(ListingFilterParameter filterParametrs, ListLoadConfig config) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }

        List<EdsDiscount> discounts = discountManager.list(filterParametrs, config);
		int totalCount = discountManager.listCount(filterParametrs);

        DiscountItem[] items = new DiscountItem[discounts.size()];

        int i = 0;
        for (EdsDiscount discount : discounts) {
            items[i] = new DiscountItem();

            items[i].setId(discount.getObjectID());
            items[i].setName(discount.getName());
            items[i].setCode(discount.getCode());
            items[i].setDescription(discount.getDescription());
            items[i].setType(discount.getType());
            items[i].setActive(discount.isActive());
            i++;
        }

        DiscountList list = new DiscountList();
        list.setItems(items);
        list.setTotalCount(totalCount);
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DiscountItem getDiscountData(Integer objectID) {

        DiscountItem discountItem = new DiscountItem();

        if (objectID != null) {

            EdsDiscount discount = discountManager.get(objectID);

            discountItem.setId(discount.getObjectID());
            discountItem.setName(discount.getName());
            discountItem.setCode(discount.getCode());
            discountItem.setDescription(discount.getDescription());
            discountItem.setActive(discount.isActive());
            discountItem.setType(discount.getType());
            discountItem.setPercentage(discount.getPercentage());
            discountItem.setFixedAmount(discount.getFixedAmount());

            //init discount multi range values
            initDiscountMultiRangeData(discountItem, discount.getMultiRangeValueList());

            initAppliedProducts(discountItem, discount.getAppliedProducts());
            initAppliedClients(discountItem, discount.getAppliedClients());
        }

        initProductList(discountItem);

        //get company currency symbol
        EdsCurrency currency = financialSettingsManager.getFinancialSettings().getCurrency();
        if (currency != null && currency.getSymbol() != null) {
            discountItem.setCurrencySymbol(currency.getSymbol());
        } else {
            discountItem.setCurrencySymbol("");
        }

        return discountItem;
    }

    @Override
    public Integer save(DiscountItem discountItem) {

        EdsDiscount discount = new EdsDiscount();
        if (discountItem.getId() != null) {
            discount = discountManager.get(discountItem.getId());
        }

        discount.setCode(discountItem.getCode());
        discount.setName(discountItem.getName());
        discount.setDescription(discountItem.getDescription());
        discount.setActive(discountItem.isActive());
        discount.setType(discountItem.getType());
        discount.setPercentage(discountItem.getPercentage());
        discount.setFixedAmount(discountItem.getFixedAmount());

        if (discount.getObjectID() != null) {
            discountManager.update(discount);
        } else {
            discountManager.create(discount);
        }

        updateDiscountMultiRangeValues(discountItem.getMultiRangeItems(), discount);

        updateAppliedItems(discountItem, discount);

        return discount.getObjectID();
    }

    @Override
    public Boolean deleteDiscount(Integer objectID) {
        EdsDiscount discount = discountManager.get(objectID);

        //clear discount applied products
        if (discount.getAppliedProducts() != null && discount.getAppliedProducts().size() > 0) {
            discount.getAppliedProducts().removeAll(discount.getAppliedProducts());
        }
        //clear discount applied clients
        if (discount.getAppliedClients() != null && discount.getAppliedClients().size() > 0) {
            discount.getAppliedClients().removeAll(discount.getAppliedClients());
        }

        //clear discount multirange values
        discountMultiRangeManager.deleteMultiRangeValuesByDiscount(objectID);

        try {
            discountManager.delete(discount);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public ListResult<DiscountItem> getDiscountList(ListingFilterParameter filterParameter) {
        List<EdsDiscount> discountList = discountManager.list(filterParameter);
        int totalCount = discountList.size();
        ArrayList<DiscountItem> itemsList = new ArrayList<>();

        for (EdsDiscount pl : discountList) {
            DiscountItem items = new DiscountItem();

            items.setId(pl.getObjectID());
            items.setName(pl.getName());
            items.setCode(pl.getCode());
            items.setDescription(pl.getDescription());
            items.setType(pl.getType());
            items.setActive(pl.isActive());
            itemsList.add(items);
        }

        return new ListResult<>(itemsList, totalCount);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DiscountItem[] getDiscountListAsSelectItem() {
        List<EdsDiscount> discountList = discountManager.list(new ListingFilterParameter());
        if (discountList == null || discountList.size() == 0) {
            return new DiscountItem[0];
        } else {
            DiscountItem[] result = new DiscountItem[discountList.size()];
            int i = 0;
            for (EdsDiscount discount : discountList) {
                result[i] = new DiscountItem(discount.getObjectID(), discount.getName());
                i++;
            }
            return result;
        }
    }


    private void initDiscountMultiRangeData(DiscountItem discountItem, List<EdsDiscountMultiRangeValue> multiRangeValues) {

        List<DiscountMultiRangeItem> multiRangeItems = new ArrayList<>();

        if (multiRangeValues != null && multiRangeValues.size() > 0) {
            for (EdsDiscountMultiRangeValue multiRangeValue : multiRangeValues) {
                DiscountMultiRangeItem multiRangeItem = new DiscountMultiRangeItem();
                multiRangeItem.setId(multiRangeValue.getObjectID());
                multiRangeItem.setType(multiRangeValue.getType());
                multiRangeItem.setFromQty(multiRangeValue.getFromQty());
                multiRangeItem.setToQty(multiRangeValue.getToQty());
                multiRangeItem.setFromAmount(multiRangeValue.getFromAmount());
                multiRangeItem.setToAmount(multiRangeValue.getToAmount());
                multiRangeItem.setPercentage(multiRangeValue.getPercentage());
                multiRangeItem.setFixedAmount(multiRangeValue.getFixedAmount());

                discountItem.setMultiRangeDiscountType(multiRangeValue.getType());

                multiRangeItems.add(multiRangeItem);
            }
        }

        discountItem.setMultiRangeItems(multiRangeItems.toArray(new DiscountMultiRangeItem[]{}));
    }

    private void initAppliedProducts(DiscountItem discountItem, List<EdsItem> appliedProducts) {
        if (appliedProducts != null && appliedProducts.size() > 0) {
            Integer[] appliedProductIDs = new Integer[appliedProducts.size()];
            int i = 0;
            for (EdsItem product : appliedProducts) {
                appliedProductIDs[i] = product.getObjectID();
                i++;
            }
            discountItem.setAppliedProductIDs(appliedProductIDs);
        }
    }

    private void initAppliedClients(DiscountItem discountItem, List<EdsCrmAccount> clientList) {
        if (clientList != null && clientList.size() > 0) {
            discountItem.setAppliedClients(clientList.stream().map(EdsCrmAccount::getAsSelectItem).toArray(SelectItem[]::new));
        }
    }

    private void initProductList(DiscountItem discountItem) {

        List<DiscountAppliesItem> appliesItems = new ArrayList<>();

        List<EdsItem> companyProducts = itemManager.getProductsWithoutCategory();
        if (companyProducts != null && companyProducts.size() > 0) {
            DiscountAppliesItem appliesItem = new DiscountAppliesItem();
            appliesItem.setName("Products without category");

            SelectItem[] items = new SelectItem[companyProducts.size()];
            int i = 0;
            for (EdsItem product : companyProducts) {
                items[i] = new SelectItem(product.getObjectID(), product.getName());
                i++;
            }
            appliesItem.setItems(items);
            appliesItems.add(appliesItem);
        }

        List<EdsProductCategory> categories = itemManager.getCategoryListByProducts();
        if (categories != null && categories.size() > 0) {
            for (EdsProductCategory category : categories) {
                DiscountAppliesItem appliesItem = new DiscountAppliesItem();
                appliesItem.setId(category.getObjectID());
                appliesItem.setName(category.getName());

                if (category.getProducts() != null && category.getProducts().size() > 0) {
                    SelectItem[] items = new SelectItem[category.getProducts().size()];
                    int i = 0;
                    for (EdsItem product : category.getProducts()) {
                        items[i] = new SelectItem(product.getObjectID(), product.getName());
                        i++;
                    }
                    appliesItem.setItems(items);
                    appliesItems.add(appliesItem);
                }
            }
        }

        discountItem.setProductList(appliesItems.toArray(new DiscountAppliesItem[]{}));
    }

    private void updateDiscountMultiRangeValues(DiscountMultiRangeItem[] multiRangeItems, EdsDiscount discount) {

        //before clear old discount multi range values
        if (discount.getMultiRangeValueList() != null && discount.getMultiRangeValueList().size() > 0) {
            discountMultiRangeManager.deleteMultiRangeValuesByDiscount(discount.getObjectID());
        }

        if (multiRangeItems != null) {
            for (DiscountMultiRangeItem multiRangeItem : multiRangeItems) {
                EdsDiscountMultiRangeValue multiRangeValue = new EdsDiscountMultiRangeValue();

                multiRangeValue.setDiscount(discount);

                multiRangeValue.setType(multiRangeItem.getType());
                multiRangeValue.setFromQty(multiRangeItem.getFromQty());
                multiRangeValue.setToQty(multiRangeItem.getToQty());
                multiRangeValue.setFromAmount(multiRangeItem.getFromAmount());
                multiRangeValue.setToAmount(multiRangeItem.getToAmount());
                multiRangeValue.setPercentage(multiRangeItem.getPercentage());
                multiRangeValue.setFixedAmount(multiRangeItem.getFixedAmount());

                discount.getMultiRangeValueList().add(multiRangeValue);
            }
        }
    }

    private void updateAppliedItems(DiscountItem discountItem, EdsDiscount discount) {
        ArrayList<EdsItem> newProducts = new ArrayList<>();
        for (int productID : discountItem.getAppliedProductIDs()) {
            EdsItem product = itemManager.get(productID);
            newProducts.add(product);
        }

        ArrayList<EdsItem> oldProducts = new ArrayList<>(discount.getAppliedProducts());

        ServerUtils.intersect(newProducts, oldProducts);
        discount.getAppliedProducts().addAll(newProducts);
        discount.getAppliedProducts().removeAll(oldProducts);

        ArrayList<EdsCrmAccount> newClients = new ArrayList<>();
        for (SelectItem clientID : discountItem.getAppliedClients()) {
            EdsCrmAccount client = clientManager.get(clientID.getId());
            newClients.add(client);
        }

        ArrayList<EdsCrmAccount> oldClients = new ArrayList<>(discount.getAppliedClients());

        ServerUtils.intersect(newClients, oldClients);
        discount.getAppliedClients().addAll(newClients);
        discount.getAppliedClients().removeAll(oldClients);
    }
}
