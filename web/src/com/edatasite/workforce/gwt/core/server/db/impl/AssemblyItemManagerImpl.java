package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAssemblyItem;
import com.edatasite.workforce.gwt.core.server.db.AssemblyItemManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/16/12
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("assemblyItemManager")
public class AssemblyItemManagerImpl extends BaseManager<EdsAssemblyItem> implements AssemblyItemManager {

    public AssemblyItemManagerImpl() {
        super(EdsAssemblyItem.class);
    }

    @Override
    public List<EdsAssemblyItem> getItemsByProduct(Integer productId) {
        return find("select i from EdsAssemblyItem i where i.item.objectID = ? ", productId);
    }

    @Override
    public List<Integer> getItemsIdByProduct(Integer productID) {
        return (List<Integer>) find("select objectID from EdsAssemblyItem ai where ai.item.objectID = ?", productID);
    }

    @Override
    public void deleteBatchAssemblyItems(String ids) {
        update("DELETE FROM EdsAssemblyItem i WHERE i.objectID in (" + ids + ")");
    }

    @Override
    public boolean isUsedInAssemblyItems(Integer productId) {
        List<EdsAssemblyItem> items =  find("select i from EdsAssemblyItem i where i.productItem.objectID=?", productId);
        boolean used = false;
         if (items.size()>0){
             for (EdsAssemblyItem item : items){
                 if (!item.getItem().getDeleted()){
                    used = true;
                    break;
                 }
             }
         }
         return used;
    }

    public void updateCostPriceOfAssemblyItem(Integer itemId, BigDecimal costPrice) {
        update("update EdsAssemblyItem i set i.costPrice = ?, i.totalValue = i.qty * ? where i.productItem.objectID = ?", costPrice, costPrice, itemId);
    }

}
