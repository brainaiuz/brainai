package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelBB;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelPP;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.PriceLevelOperationTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelBBItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BrandManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelBBManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelPPManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 6:53:38 PM
 */
@Transactional
@Service("priceLevelService")
public class PriceLevelServiceImpl implements PriceLevelService, PriceLevelServiceLocal, Constants {

    @Autowired
    private PriceLevelManager priceLevelManager;
    @Autowired
    private PriceLevelPPManager priceLevelPPManager;
    @Autowired
    private PriceLevelBBManager priceLevelBBManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AllInOneServiceLocal allInOneService;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private CurrencyManager currencyManager;

    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private BrandManager brandManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PriceLevelItem getPriceLevelData(ListingFilterParameter filterParameter) {
        Integer objectID = filterParameter.getObjectId();
        PriceLevelItem priceLevelItem = new PriceLevelItem();
        priceLevelItem.setCurrencyList(currencyService.getCurrencies(true, false));
        priceLevelItem.setBaseCurrency(getCompanyBaseCurrency());
        CurrencyItem currencyItem = null;
        if (filterParameter.getCurrencyID() != null) {
            EdsCurrency currency = currencyManager.getCurrency(filterParameter.getCurrencyID());
            currencyItem = currency != null ? currency.createCurrencyItem() : getCompanyBaseCurrency();
        } else {
            currencyItem = currencyService.getBaseCurrency();
        }
        priceLevelItem.setCurrency(currencyItem);
        priceLevelItem.setClientTypeList(ServerUtils.getAsSelectItem(referenceManager.listReferences("CLIENT_TYPES"), 10));
        priceLevelItem.setOperationType(PriceLevelOperationTypeEnum.FOR_CLIENT);

        if (objectID != null) {
            EdsPriceLevel priceLevel = priceLevelManager.get(objectID);

            priceLevelItem.setId(priceLevel.getObjectID());
            priceLevelItem.setName(priceLevel.getName());
            priceLevelItem.setType(priceLevel.getType());
            priceLevelItem.setPLCase(priceLevel.getPLCase());
            priceLevelItem.setPercent(priceLevel.getPercent());

            if (priceLevel.getCurrency() != null) {
                priceLevelItem.setCurrency(priceLevel.getCurrency().createCurrencyItem());
            }
            if (priceLevel.getOperationType() != null) {
                priceLevelItem.setOperationType(priceLevel.getOperationType());
            }

            initAppliedClients(priceLevel, priceLevelItem);
            initAppliedClientTypes(priceLevel, priceLevelItem);
            initPriceLevelPP(priceLevel, priceLevelItem);
            initPriceLevelBB(priceLevel, priceLevelItem);
            // object permission get start! * * * * * * * * * * * * * * * * * * * * * * * * * * *
            priceLevelItem = (PriceLevelItem) allInOneService.getObjectPermission(priceLevel, priceLevelItem);// *
            // object permission get end!   * * * * * * * * * * * * * * * * * * * * * * * * * * *
        }

        return priceLevelItem;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getClientList(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        return clientManager.list(filterParametrs);
    }

    @Override
    public Integer save(PriceLevelItem priceLevelItem) {
        if (priceLevelItem != null) {
            boolean isPriceLevelNameExists = priceLevelManager.isPricelLevelNameExists(priceLevelItem.getName(), priceLevelItem.getId());
            if (isPriceLevelNameExists) {
                return -1;
            } else {
                EdsPriceLevel priceLevel = new EdsPriceLevel();
                if (priceLevelItem.getId() != null) {
                    priceLevel = priceLevelManager.get(priceLevelItem.getId());
                }
                priceLevel.setOperationType(priceLevelItem.getOperationType());
                priceLevel.setName(priceLevelItem.getName());
                priceLevel.setType(priceLevelItem.getType());
                priceLevel.setPLCase(priceLevelItem.getPLCase());

                if (priceLevel.getType().equals(FIXED_PERCENTAGE)) {
                    priceLevel.setPercent(priceLevelItem.getPercent());
                } else {
                    priceLevel.setPercent(null);
                }

                if (priceLevelItem.getCurrency() != null && priceLevelItem.getCurrency().getId() != null) {
                    priceLevel.setCurrency(currencyManager.get(priceLevelItem.getCurrency().getId()));
                }

                priceLevelManager.create(priceLevel);

                updatePriceLevelReferences(priceLevel, priceLevelItem);

                // update object permissions start * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                priceLevel = (EdsPriceLevel) allInOneService.saveObjectPermission(priceLevel, priceLevelItem);// *
                priceLevelManager.update(priceLevel); // this line is useless, but let it be here for a while.// *
                // update object permissions END * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
                return priceLevel.getObjectID();
            }
        }

        return 0;
    }

    @Override
    public Boolean deletePriceLevel(Integer objectID) {

        priceLevelPPManager.deletePriceLevelPPByPL(objectID);

        priceLevelManager.deletePriceLevel(objectID);

        return true;
    }

    @Override
    public ListResult<PriceLevelItem> getPriceLevelList(ListingFilterParameter filterParameter) {
        Integer count = priceLevelManager.listCount(filterParameter);

        ArrayList<PriceLevelItem> itemsList = new ArrayList<>();
        CurrencyItem baseCurrency = getCompanyBaseCurrency();
        if (count > 0) {
            List<EdsPriceLevel> priceLevelList = priceLevelManager.list(filterParameter);
            System.out.println(priceLevelList.size());
            for (EdsPriceLevel pl : priceLevelList) {
                PriceLevelItem item = new PriceLevelItem();
                item.setId(pl.getObjectID());
                item.setName(pl.getName());
                item.setType(pl.getType());
                item.setPLCase(pl.getPLCase());
                item.setBaseCurrency(baseCurrency);
                item.setCurrency(pl.getCurrency() != null ? pl.getCurrency().createCurrencyItem() : null);
                itemsList.add(item);
            }
        }
        return new ListResult<>(itemsList, count);
    }

    @Override
    public ListResult<PriceLevelItem> getPriceLevelListForListing(ListingFilterParameter filterParameter) {
        return getPriceLevelList(filterParameter);
    }

    @Override
    public void updatePriceLevelByQB(PriceLevelItem priceLevelItem, Integer synchItemType) {
        EdsPriceLevel priceLevel = priceLevelManager.getPriceLevelByName(priceLevelItem.getName());
        if (priceLevel != null) {
            priceLevel.setQuickbookPriceLevelID(priceLevelItem.getQbPriceLevelId());
            priceLevel.setQuickbookEditSequence(priceLevelItem.getQbEditSequence());
            priceLevelManager.update(priceLevel);
        }
    }

    public PriceLevelPPItem getPriceLevelPPItem(Integer productId, Integer priceLevelId) {
        if (priceLevelId != null && productId != null) {
            EdsPriceLevelPP priceLevelPP = priceLevelPPManager.getByPriceLevelIdAndProductId(priceLevelId, productId);
            if (priceLevelPP != null) {
                return priceLevelPP.toRPC();
            }
        }
        return null;
    }

    public EdsPriceLevelPP getPriceLevelPP(Integer productId, Integer priceLevelId) {
        EdsPriceLevel priceLevel = priceLevelManager.get(priceLevelId);
        if (priceLevel != null) {
            return priceLevelPPManager.getByPriceLevelIdAndProductId(priceLevelId, productId);
        }
        return null;
    }

    @Override
    public Double getCustomPriceFromPriceLevel(Integer productId, Integer priceLevelId) {
        EdsPriceLevelPP priceLevelPP = getPriceLevelPP(productId, priceLevelId);
        return priceLevelPP == null ? null : priceLevelPP.getCustomPrice();
    }

    @Override
    public void setCustomPriceFromPriceLevel(Integer productId, Integer priceLevelId, Double customPrice) {
        if (customPrice != null) {
            EdsPriceLevelPP priceLevelPP = getPriceLevelPP(productId, priceLevelId);
            if (priceLevelPP == null) {
                priceLevelPP = new EdsPriceLevelPP();
                priceLevelPP.setPriceLevel(priceLevelManager.get(priceLevelId));
                priceLevelPP.setProduct(itemManager.get(productId));
            }
            priceLevelPP.setCustomPrice(customPrice);
            priceLevelPPManager.createOrUpdate(priceLevelPP);
        }
    }

    @Override
    public HashMap<PriceLevelItem, PriceLevelPPItem> getPriceLevelPPItems(Integer productId) {
        List<EdsPriceLevelPP> pps = priceLevelPPManager.getByPriceLevelsByProductId(productId);
        HashMap<PriceLevelItem, PriceLevelPPItem> priceLevelPPItems = new HashMap<>();
        for (EdsPriceLevelPP priceLevelPP : pps) {
            PriceLevelPPItem priceLevelPPItem = new PriceLevelPPItem();
            if (priceLevelPP.getProduct() != null) {
                priceLevelPPItem.setProductName(priceLevelPP.getProduct().getName());
                priceLevelPPItem.setProductID(priceLevelPP.getProduct().getObjectID());
                priceLevelPPItem.setStandarPrice(priceLevelPP.getProduct().getSellingPrice().doubleValue());
            }
            priceLevelPPItem.setCustomPrice(priceLevelPP.getCustomPrice());
            if (priceLevelPP.getPriceLevel() != null) {
                PriceLevelItem priceLevelItem = priceLevelPP.getPriceLevel().getRPC();
                priceLevelItem.setBaseCurrency(getCompanyBaseCurrency());
                priceLevelPPItems.put(priceLevelItem, priceLevelPPItem);
            }
        }
        return priceLevelPPItems;
    }

    @Override
    public ArrayList<PriceLevelPPItem> getPriceLevelPPItemList(Integer priceLevelId, String searchKey) {
        List<EdsPriceLevelPP> list = priceLevelPPManager.getItemsByPriceLevelId(priceLevelId, searchKey);
        ArrayList<PriceLevelPPItem> items = new ArrayList<>();

        list.forEach(pp -> items.add(pp.toRPC()));
        return items;
    }

    @Override
    public void setCustomPriceFromPriceLevels(Integer productId, PriceLevelPPItem[] priceLevelPPItems) {
        priceLevelPPManager.deletePriceLevelPPByProduct(productId);
        if (productId != null && priceLevelPPItems != null && priceLevelPPItems.length > 0) {
            for (PriceLevelPPItem item : priceLevelPPItems) {
                setCustomPriceFromPriceLevel(productId, item.priceLevelID, item.getCustomPrice());
            }
        }
    }

    @Override
    public void savePriceLevelPPItems(Integer priceLevelId, PriceLevelPPItem[] ppItems) {
        EdsPriceLevel priceLevel = priceLevelManager.get(priceLevelId);

        if (priceLevel != null) {
            priceLevelPPManager.deletePriceLevelPPByPL(priceLevel.getObjectID());

            if (priceLevel.getType().equals(PER_PRODUCT)) {

                if (ppItems != null && ppItems.length > 0) {
                    List<EdsPriceLevelPP> priceLevelPPs = new ArrayList<>();
                    for (PriceLevelPPItem priceLevelPPItem : ppItems) {
                        EdsPriceLevelPP priceLevelPP = new EdsPriceLevelPP();
                        EdsItem product = itemManager.get(priceLevelPPItem.getProductID());
                        priceLevelPP.setProduct(product);
                        priceLevelPP.setCustomPrice(priceLevelPPItem.getCustomPrice());
                        priceLevelPP.setPriceLevel(priceLevel);
                        priceLevelPPs.add(priceLevelPP);
                    }
                    priceLevel.setPriceLevelPPs(priceLevelPPs);
                    priceLevelManager.update(priceLevel);
                }
            }
        }
    }

    @Override
    public void savePriceLevelPBItems(Integer priceLevelId, PriceLevelBBItem[] bbItems) {
        EdsPriceLevel priceLevel = priceLevelManager.get(priceLevelId);

        if (priceLevel != null) {
            priceLevelBBManager.deleteByPriceLevel(priceLevel.getObjectID());

            if (bbItems != null && bbItems.length > 0) {
                List<EdsPriceLevelBB> priceLevelBBs = new ArrayList<>();
                for (PriceLevelBBItem priceLevelBBItem : bbItems) {
                    EdsPriceLevelBB priceLevelBB = new EdsPriceLevelBB();
                    priceLevelBB.setBrand(brandManager.get(priceLevelBBItem.getBrand().getId()));
                    priceLevelBB.setEffectType(priceLevelBBItem.getEffectType());
                    priceLevelBB.setPercent(priceLevelBBItem.getPercentage());
                    priceLevelBB.setPriceLevel(priceLevel);
                    priceLevelBBs.add(priceLevelBB);
                }
                priceLevel.setPriceLevelBBs(priceLevelBBs);
                priceLevelManager.update(priceLevel);
            }
        }
    }

    @Override
    public void savePriceLevelPPItem(PriceLevelPPItem item) {
        if (item != null && item.getObjectId() != null) {
            EdsPriceLevelPP pp = priceLevelPPManager.get(item.getObjectId());
            pp.setCustomPrice(item.getCustomPrice());
            priceLevelPPManager.update(pp);
        } else if (item != null) {
            EdsPriceLevelPP pp = new EdsPriceLevelPP();
            pp.setPriceLevel(priceLevelManager.get(item.priceLevelID));
            pp.setProduct(itemManager.get(item.getProductID()));
            pp.setCustomPrice(item.getCustomPrice());
            priceLevelPPManager.create(pp);
        }
    }

    @Override
    public void deletePriceLevelPPItem(Integer ppItemId) {
        EdsPriceLevelPP pp = priceLevelPPManager.get(ppItemId);

        if (pp != null) {
            priceLevelPPManager.delete(pp);
        }
    }

    private void initAppliedClients(EdsPriceLevel priceLevel, PriceLevelItem priceLevelItem) {
        if (priceLevel.getClients() != null && priceLevel.getClients().size() > 0) {
            List<SelectItem> appliedClients = new ArrayList<>();
            for (EdsCrmAccount client : priceLevel.getClients()) {
                appliedClients.add(new SelectItem(client.getObjectID(), client.getNumber() + "->" + client.getName()));
            }
            priceLevelItem.setAppliedClients(appliedClients.toArray(new SelectItem[]{}));
        }
    }

    private void initAppliedClientTypes(EdsPriceLevel priceLevel, PriceLevelItem priceLevelItem) {
        if (priceLevel.getClientTypes() != null && priceLevel.getClientTypes().size() > 0) {
            List<SelectItem> appliedClientTypes = new ArrayList<>();
            for (EdsReference clientType : priceLevel.getClientTypes()) {
                appliedClientTypes.add(clientType.getAsSelectItem());
            }
            priceLevelItem.setAppliedClientTypes(appliedClientTypes.toArray(new SelectItem[]{}));
        }
    }

    private void initPriceLevelPP(EdsPriceLevel priceLevel, PriceLevelItem priceLevelItem) {
        priceLevelItem.setTotalCountPerProductItems(priceLevelPPManager.getTotalCount(priceLevel.getObjectID()));

        if (priceLevelItem.getTotalCountPerProductItems().compareTo(MAX_LIMIT_PRICE_LEVEL_PER_PRODUCT) <= 0)
            if (priceLevel.getPriceLevelPPs() != null && priceLevel.getPriceLevelPPs().size() > 0) {//temp solution
                List<PriceLevelPPItem> priceLevelPPItems = new ArrayList<>();
                for (EdsPriceLevelPP priceLevelPP : priceLevel.getPriceLevelPPs()) {
                    PriceLevelPPItem priceLevelPPItem = new PriceLevelPPItem();
                    if (priceLevelPP.getProduct() != null) {
                        priceLevelPPItem.setProductName(priceLevelPP.getProduct().getName());
                        priceLevelPPItem.setProductID(priceLevelPP.getProduct().getObjectID());
                        priceLevelPPItem.setStandarPrice(priceLevelPP.getProduct().getSellingPrice().doubleValue());
                    }
                    priceLevelPPItem.setCustomPrice(priceLevelPP.getCustomPrice());
                    priceLevelPPItems.add(priceLevelPPItem);
                }

                priceLevelItem.setPriceLevelPPItems(priceLevelPPItems.toArray(new PriceLevelPPItem[]{}));
            }
    }

    private void initPriceLevelBB(EdsPriceLevel priceLevel, PriceLevelItem priceLevelItem) {
        if (priceLevel.getPriceLevelBBs() != null && priceLevel.getPriceLevelBBs().size() > 0) {
            List<PriceLevelBBItem> priceLevelBBItems = new ArrayList<>();
            for (EdsPriceLevelBB priceLevelBB : priceLevel.getPriceLevelBBs()) {
                PriceLevelBBItem priceLevelBBItem = new PriceLevelBBItem();
                if (priceLevelBB.getBrand() != null) {
                    priceLevelBBItem.setBrand(priceLevelBB.getBrand().getAsSelectItem());
                }
                priceLevelBBItem.setEffectType(priceLevelBB.getEffectType());
                priceLevelBBItem.setPercentage(priceLevelBB.getPercent());
                priceLevelBBItems.add(priceLevelBBItem);
            }
            priceLevelItem.setPriceLevelBBItems(priceLevelBBItems.toArray(new PriceLevelBBItem[]{}));
        }
    }

    private void updatePriceLevelReferences(EdsPriceLevel priceLevel, PriceLevelItem priceLevelItem) {
        /*priceLevelPPManager.deletePriceLevelPPByPL(priceLevel.getObjectID());
        priceLevelBBManager.deleteByPriceLevel(priceLevel.getObjectID());
        if (priceLevel.getType().equals(PER_PRODUCT)) {
            if (priceLevelItem.getPriceLevelPPItems() != null && priceLevelItem.getPriceLevelPPItems().length > 0) {
                List<EdsPriceLevelPP> priceLevelPPs = new ArrayList<>();
                for (PriceLevelPPItem priceLevelPPItem : priceLevelItem.getPriceLevelPPItems()) {
                    EdsPriceLevelPP priceLevelPP = new EdsPriceLevelPP();
                    EdsItem product = itemManager.get(priceLevelPPItem.getProductID());
                    priceLevelPP.setProduct(product);
                    priceLevelPP.setCustomPrice(priceLevelPPItem.getCustomPrice());
                    priceLevelPP.setPriceLevel(priceLevel);
                    priceLevelPPs.add(priceLevelPP);
                }

                priceLevel.setPriceLevelPPs(priceLevelPPs);
            }
        }*/
        if (priceLevel.getType().equals(BY_BRAND)) {
            /*if (priceLevelItem.getPriceLevelBBItems() != null && priceLevelItem.getPriceLevelBBItems().length > 0) {
                List<EdsPriceLevelBB> priceLevelBBs = new ArrayList<>();
                for (PriceLevelBBItem priceLevelBBItem : priceLevelItem.getPriceLevelBBItems()) {
                    EdsPriceLevelBB priceLevelBB = new EdsPriceLevelBB();
                    priceLevelBB.setBrand(brandManager.get(priceLevelBBItem.getBrand().getId()));
                    priceLevelBB.setEffectType(priceLevelBBItem.getEffectType());
                    priceLevelBB.setPercent(priceLevelBBItem.getPercentage());
                    priceLevelBB.setPriceLevel(priceLevel);
                    priceLevelBBs.add(priceLevelBB);
                }
                priceLevel.setPriceLevelBBs(priceLevelBBs);
            }*/

            ArrayList<EdsReference> newClientTypes = new ArrayList<>();
            if (priceLevelItem.getAppliedClientTypes() != null) {
                priceLevelItem.getAppliedClientTypes();
                for (SelectItem nct : priceLevelItem.getAppliedClientTypes()) {
                    newClientTypes.add(referenceManager.get(nct.getId()));
                }
            }

            ArrayList<EdsReference> oldClientTypes = new ArrayList<>(priceLevel.getClientTypes());

            ServerUtils.intersect(newClientTypes, oldClientTypes);
            priceLevel.getClientTypes().addAll(newClientTypes);
            priceLevel.getClientTypes().removeAll(oldClientTypes);

        } else {
            ArrayList<EdsCrmAccount> newClients = new ArrayList<>();
            if (priceLevelItem.getAppliedClients() != null) {
                priceLevelItem.getAppliedClients();
                for (SelectItem ac : priceLevelItem.getAppliedClients()) {
                    EdsCrmAccount client = clientManager.get(ac.getId());
                    newClients.add(client);
                }
            }

            ArrayList<EdsCrmAccount> oldClients = new ArrayList<>(priceLevel.getClients());

            ServerUtils.intersect(newClients, oldClients);
            priceLevel.getClients().addAll(newClients);
            priceLevel.getClients().removeAll(oldClients);
        }
    }


    private CurrencyItem getCompanyBaseCurrency() {
        EdsUser user = priceLevelManager.getUser();

        //If company already has base currency. (You can set it in the Settings menu)
        EdsCurrency currency = financialSettingsManager.getFinancialSettings().getCurrency();
        //company does not has base currency. Hm...
        if (currency == null) {
            EdsCompany company = user.getCompany();
            //Then try to get country currency.
            currency = company.getCountryZone().getCountry().getCurrency();
            //For this country there is no currency.
            if (currency == null) {
                //Hey, I know universal currency! God bless America!
                currency = currencyManager.getCurrency(CurrencyManager.USD);
            }
        }

        return currency.createCurrencyItem();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CurrencyItem[] getCurrencies() {
        List<EdsCurrency> currencies = currencyManager.getAllCurrency();
        CurrencyItem[] items = new CurrencyItem[currencies.size()];
        int i = 0;
        for (EdsCurrency currency : currencies) {
            items[i] = currency.createCurrencyItem();
            i++;
        }
        return items;
    }

    public void setPriceLevelToClient(SelectItem[] priceLevelList, EdsCrmAccount client, Integer currencyId) {
        if (client.isClient() || client.isSupplier()) {
            try {
                List<EdsPriceLevel> oldPriceLevels = priceLevelManager.getPriceLevels(currencyId, client.getObjectID(), true);
                if (oldPriceLevels != null && oldPriceLevels.size() > 0) {
                    for (EdsPriceLevel oldItem : oldPriceLevels) {
                        oldItem.getClients().remove(client);
                    }
                }
                if (priceLevelList != null) {
                    for (SelectItem priceLavel : priceLevelList) {
                        if (priceLavel.getId() != null) {
                            EdsPriceLevel price = priceLevelManager.get(priceLavel.getId());
                            if (price != null) {
                                price.getClients().add(client);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
