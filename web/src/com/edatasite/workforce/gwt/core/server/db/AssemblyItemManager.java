package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAssemblyItem;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/16/12
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AssemblyItemManager extends Manager<EdsAssemblyItem> {

    List<EdsAssemblyItem> getItemsByProduct(Integer productId);

    List<Integer> getItemsIdByProduct(Integer productID);

    void deleteBatchAssemblyItems(String ids);

    boolean isUsedInAssemblyItems(Integer productId);

    void updateCostPriceOfAssemblyItem(Integer itemId, BigDecimal costPrice);
}
