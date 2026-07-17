package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsExpenseCategory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 21.10.2008
 * Time: 10:22:43
 * To change this template use File | Settings | File Templates.
 */
public interface ExpenseCategoryManager extends Manager<EdsExpenseCategory> {
    List<EdsExpenseCategory> getExpenseCategories();

    List<EdsExpenseCategory> getCategoriesByCompany(EdsCompany company);

    EdsExpenseCategory getCategory(Integer objectID);

    EdsExpenseCategory isUnicalCategoryName(String categoryName);
}
