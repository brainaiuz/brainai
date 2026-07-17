package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsExpenseCategory;
import com.edatasite.workforce.gwt.core.server.db.ExpenseCategoryManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 21.10.2008
 * Time: 10:32:19
 * To change this template use File | Settings | File Templates.
 */
@Repository("expenseCategoryManager")
public class ExpenseCategoryManagerImpl extends BaseManager<EdsExpenseCategory> implements ExpenseCategoryManager {

    public ExpenseCategoryManagerImpl() {
        super(EdsExpenseCategory.class);
    }

    public List<EdsExpenseCategory> getExpenseCategories() {
        return find("from EdsExpenseCategory");
    }

    public List<EdsExpenseCategory> getCategoriesByCompany(EdsCompany company) {
        return find("select ec from EdsExpenseCategory ec");
    }

    public EdsExpenseCategory getCategory(Integer objectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("objectID", objectID);
        return (EdsExpenseCategory) findSingleByNamedParams("select ec from EdsExpenseCategory ec where ec.objectID =:objectID", map);
    }

    public EdsExpenseCategory isUnicalCategoryName(String categoryName) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", categoryName);
        return (EdsExpenseCategory) findSingleByNamedParams("select ec from EdsExpenseCategory ec where ec.name =:name", map);
    }
}
