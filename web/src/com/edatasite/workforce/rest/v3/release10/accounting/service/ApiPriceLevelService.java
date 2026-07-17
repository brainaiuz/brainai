package com.edatasite.workforce.rest.v3.release10.accounting.service;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelBB;
import com.edatasite.workforce.core.domain.accounting.EdsPriceLevelPP;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.PriceLevelBBDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.PriceLevelListDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.PriceLevelPPDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiPriceLevelService implements Constants {

    private final PriceLevelManager priceLevelManager;

    @Autowired
    public ApiPriceLevelService(PriceLevelManager priceLevelManager) {
        this.priceLevelManager = priceLevelManager;
    }

    public ListResultTO<PriceLevelListDto> getPriceLevelList(ListingFilterParameter filterParameter) {
        Integer count = priceLevelManager.listCount(filterParameter);

        ArrayList<PriceLevelListDto> itemsList = new ArrayList<>();
        if (count > 0) {
            List<EdsPriceLevel> priceLevelList = priceLevelManager.list(filterParameter);
            for (EdsPriceLevel pl : priceLevelList) {
                PriceLevelListDto item = new PriceLevelListDto();
                item.setId(pl.getObjectID());
                item.setName(pl.getName());
                if (pl.getType().equals(FIXED_PERCENTAGE)) {
                    item.setType("FIXED_PERCENTAGE");
                } else if (pl.getType().equals(PER_PRODUCT)) {
                    item.setType("PER_PRODUCT");
                } else {
                    item.setType("BY_BRAND");
                }
                item.setPlCase(pl.getPLCase().equals(DECREASE) ? "DECREASE" : "INCREASE");
                item.setPercent(pl.getPercent());
                item.setOperationType(pl.getOperationType() != null ? pl.getOperationType().name() : null);

                if (pl.getPriceLevelBBs() != null && !pl.getPriceLevelBBs().isEmpty()) {
                    List<PriceLevelBBDto> bbs = new ArrayList<>();
                    for (EdsPriceLevelBB priceLevelBB : pl.getPriceLevelBBs()) {
                        PriceLevelBBDto bb = new PriceLevelBBDto();
                        bb.setId(priceLevelBB.getObjectID());
                        bb.setEffectType(priceLevelBB.getEffectType());
                        bb.setPercent(priceLevelBB.getPercent());
                        bb.setBrand(priceLevelBB.getBrand() != null ? new ItemDto(priceLevelBB.getBrand().getObjectID(), priceLevelBB.getBrand().getName()) : null);
                        bbs.add(bb);
                    }
                    item.setPriceLevelBBs(bbs);
                }
                if (pl.getPriceLevelPPs() != null && !pl.getPriceLevelPPs().isEmpty()) {
                    List<PriceLevelPPDto> pps = new ArrayList<>();
                    for (EdsPriceLevelPP priceLevelPP : pl.getPriceLevelPPs()) {
                        PriceLevelPPDto pp = new PriceLevelPPDto();
                        pp.setId(priceLevelPP.getObjectID());
                        pp.setCustomPrice(priceLevelPP.getCustomPrice());
                        pp.setProduct(priceLevelPP.getProduct() != null ? new ItemDto(priceLevelPP.getProduct().getObjectID(), priceLevelPP.getProduct().getName(), priceLevelPP.getProduct().getProductNumber()) : null);
                        pps.add(pp);
                    }
                    item.setPriceLevelPPs(pps);
                }

                if (pl.getClients() != null && !pl.getClients().isEmpty()) {
                    List<ItemDto> clients = new ArrayList<>();
                    for (EdsCrmAccount crmAccount : pl.getClients()) {
                        clients.add(new ItemDto(crmAccount.getObjectID(), crmAccount.getName(), crmAccount.getNumber()));
                    }
                    item.setClients(clients);
                }
                if (pl.getClientTypes() != null && !pl.getClientTypes().isEmpty()) {
                    List<ItemDto> types = new ArrayList<>();
                    for (EdsReference reference : pl.getClientTypes()) {
                        types.add(new ItemDto(reference.getObjectID(), reference.getName(), reference.getCode()));
                    }
                    item.setClientTypes(types);
                }

                item.setCurrency(pl.getCurrency() != null ? new ItemDto(pl.getCurrency().getObjectID(), pl.getCurrency().getName(), pl.getCurrency().getSymbol()) : null);
                item.setQuickBookEditSequence(pl.getQuickbookEditSequence());
                item.setQuickBookPriceLevelID(pl.getQuickbookPriceLevelID());
                item.setExternalGUID(pl.getExternalGUID());
                itemsList.add(item);
            }
        }
        return new ListResultTO<>(count, itemsList);
    }
}
