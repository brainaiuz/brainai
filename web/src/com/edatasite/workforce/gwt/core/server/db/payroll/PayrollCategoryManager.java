package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/23/15
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PayrollCategoryManager extends Manager<EdsPayrollCategory> {

    List<EdsPayrollCategory> list();

    List<EdsPayrollCategory> list(boolean isArabic);

    List<EdsPayrollCategory> list(ListingFilterParameter fp);

    List<EdsPayrollCategory> list(String categoryType, ListingFilterParameter fp);

    EdsPayrollCategory findCategoryByCode(String code);

    EdsPayrollCategory getCategoryByCode(String code);

    EdsPayrollCategory getCategoryByCode(String code, String type);

    EdsPayrollCategory getCategoryByName(String name, String type);

    List<EdsPayrollCategory> getAdvanceCategories();

    void deleteCategories(Integer id);

    boolean isCategoryCodeExists(String type, String code, Integer categoryID);

    PaymentDeductionSelectItem[] getCategoriesForLookUp(ListingFilterParameter filterParametrs);

    PaymentDeductionSelectItem[] getCategoriesForBulkAdd(ListingFilterParameter filterParameter);

    Integer getCategoriesCountForBulkAdd(ListingFilterParameter filterParameter);

    void deleteReferenceBySchemaID(Integer schemaID);

    void deleteSickeLeaveSettingsRefernce();

    void deleteReferenceByEndOfServiceSettings(Integer eosId);

    void resetDefaultCategory();

    EdsPayrollCategory getDefaultCategory();

    List<EdsPayrollCategory> getCategoriesByCodes(String... codes);

    List<EdsPayrollCategory> getCategoryLinkedCategories(Integer paydeductionId);

    default Map<String, PaymentDeductionSelectItem> getCategoryItemMapByCodes(String... codes) {
        final List<EdsPayrollCategory> list = this.getCategoriesByCodes(codes);

        return list.stream()
                   .collect(Collectors.toMap(EdsPayrollCategory::getCode,
                                             category -> category.createPaymentDeductionSelectItem()));
    }
}
