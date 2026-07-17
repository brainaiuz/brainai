package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 14, 2010
 * Time: 5:38:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductCategoryManager extends Manager<EdsProductCategory> {

    List<EdsProductCategory> getProductCategories();

    List<EdsProductCategory> getProductCategoriesByIds(String ids);

    List<EdsProductCategory> getParentCategories();

    List<EdsProductCategory> getParentCategoriesForSettings();

    List<EdsProductCategory> getCategoriesByFilter(ListingFilterParameter filterParametrs);

    List<EdsProductCategory> getSubCategoriesByParentID(Integer parentID);

    EdsProductCategory getCategoryByName(String name);

    void deleteProductCategory(Integer objectID);

    void deleteCategories(Integer[] ids);

    int getCategoriesByFilterCount(ListingFilterParameter filterParametrs);

    List<Integer> getAllSubCategoryIDsByCategoryId(Integer categoryID);

    List<EdsProductCategory> getProductCategoriesForSync();

    List<EdsProductCategory> getProductCategoriesForReset();

    void updateProductCategoriesAfterReset();

    Integer getProductCategoryLastIntNumber();
}
