package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 9/23/15
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("categoryManager")
public class PayrollCategoryManagerImpl extends BaseManager<EdsPayrollCategory> implements PayrollCategoryManager {

    @Autowired
    private GenericSettingsManager genericSettingsManager;
    public PayrollCategoryManagerImpl() {
        super(EdsPayrollCategory.class);
    }

    public List<EdsPayrollCategory> list() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct c.* ");
        sql.append(" from  " + getCompanyId() + ".category c ");
        sql.append(" where c.deleted<>true or c.deleted is null ");
        sql.append(" order by c.type, c.name ");
        return findNative(sql.toString(), EdsPayrollCategory.class);
    }

    @Override
    public List<EdsPayrollCategory> list(boolean isArabic) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct c.* ");
        sql.append(" from  " + getCompanyId() + ".category c ");
        sql.append(" where (c.deleted<>true or c.deleted is null) ");

        if (isArabic) {
            sql.append(" and (c.arabic is true or c.forAll is true)");
        } else {
            sql.append(" AND " + ServerUtils.checkForDeleted("c.arabic")).append(" or c.forAll is true");
        }

        sql.append(" order by c.type, c.name ");
        return findNative(sql.toString(), EdsPayrollCategory.class);
    }

    public List<EdsPayrollCategory> list(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct c.*, da.name as debit_acc, ca.name as credit_acc ");
        sql.append(" from " + getCompanyId() + ".category c ");
        sql.append(" left join " + getCompanyId() + ".account da on c.debittoaccountid = da.id ");
        sql.append(" left join " + getCompanyId() + ".account ca on c.credittoaccountid = ca.id ");
        sql.append(" where (c.deleted<>true or c.deleted is null) ");

        if (fp.isShowActive()) {
            sql.append(" and (c.arabic is not false ").append(" or c.forAll is not false)");
        } else {
            sql.append(" AND (" + ServerUtils.checkForDeleted("c.arabic")).append(" or c.forAll is true)");
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and ( lower(c.name) like'" + fp.getSqlSearchKey() + "'");
            sql.append(" or lower (c.type) like '" + fp.getSqlSearchKey() + "')");
        }

        sql.append(" ORDER BY ");

        if ("name".equals(fp.getSortField())) {
            sql.append("c.name");
        } else if ("code".equals(fp.getSortField())) {
            sql.append("c.code");
        } else if ("paye".equals(fp.getSortField())) {
            sql.append("c.taxable");
        } else if ("nic".equals(fp.getSortField())) {
            sql.append("c.niable");
        } else if ("timesheet".equals(fp.getSortField())) {
            sql.append("c.timesheet");
        } else if ("pension".equals(fp.getSortField())) {
            sql.append("c.pensionable");
        } else if ("debit".equals(fp.getSortField())) {
            sql.append("da.name");
        } else if ("credit".equals(fp.getSortField())) {
            sql.append("ca.name");
        } else {
            sql.append("c.id");
        }

        if (!fp.isAscending()) {
            sql.append(" DESC ");
        }


        return findNative(sql.toString(), EdsPayrollCategory.class);
    }

    public List<EdsPayrollCategory> list(String categoryType, ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct c.*, da.name as debit_acc, ca.name as credit_acc ");
        sql.append(" from " + getCompanyId() + ".category c ");
        sql.append(" left join " + getCompanyId() + ".account da on c.debittoaccountid = da.id ");
        sql.append(" left join " + getCompanyId() + ".account ca on c.credittoaccountid = ca.id ");
        sql.append(" where (c.deleted<>true or c.deleted is null) ");
        if (categoryType != null) {
            sql.append(" AND c.type='" + categoryType + "'");
        }

        if (fp.isShowActive()) {
            sql.append(" and (c.arabic is not false ").append(" or c.forAll is not false)");
        } else {
            sql.append(" AND (" + ServerUtils.checkForDeleted("c.arabic")).append(" or c.forAll is true)");
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and ( lower(c.name) like'" + fp.getSqlSearchKey() + "'");
            sql.append(" or lower (c.type) like '" + fp.getSqlSearchKey() + "')");
        }

        sql.append(" ORDER BY ");

        if ("name".equals(fp.getSortField())) {
            sql.append("c.name");
        } else if ("code".equals(fp.getSortField())) {
            sql.append("c.code");
        } else if ("paye".equals(fp.getSortField())) {
            sql.append("c.taxable");
        } else if ("nic".equals(fp.getSortField())) {
            sql.append("c.niable");
        } else if ("timesheet".equals(fp.getSortField())) {
            sql.append("c.timesheet");
        } else if ("pension".equals(fp.getSortField())) {
            sql.append("c.pensionable");
        } else if ("debit".equals(fp.getSortField())) {
            sql.append("da.name");
        } else if ("credit".equals(fp.getSortField())) {
            sql.append("ca.name");
        } else {
            sql.append("c.id");
        }

        if (!fp.isAscending()) {
            sql.append(" DESC ");
        }


        return findNative(sql.toString(), EdsPayrollCategory.class);
    }

    public EdsPayrollCategory findCategoryByCode(String code) {
        return (EdsPayrollCategory) findSingle("select c from EdsPayrollCategory c where c.code=?", code);
    }

    public EdsPayrollCategory getCategoryByCode(String code) {
        return (EdsPayrollCategory) findSingle("select c from EdsPayrollCategory c where c.code=?", code);
    }

    @Override
    public EdsPayrollCategory getCategoryByCode(String code, String type) {
        boolean hasType = StringUtils.isNotBlank(type);
        code = StringUtils.isNotBlank(code) ? code.trim().toLowerCase() : "";
        String query = "select c from EdsPayrollCategory c where lower(trim(c.code))=? ";
        if (hasType) {
            query += " and c.type=?";
        }
        if (hasType) {
            return (EdsPayrollCategory) findSingle(query, code, type);
        } else {
            return (EdsPayrollCategory) findSingle(query, code);
        }
    }

    public EdsPayrollCategory getCategoryByName(String name, String type) {
        boolean hasType = StringUtils.isNotBlank(type);
        name = StringUtils.isNotBlank(name) ? name.trim().toLowerCase() : "";
        String query = "select c from EdsPayrollCategory c where lower(trim(c.name))=? ";
        if (hasType) {
            query += " and c.type=?";
        }
        if (hasType) {
            return (EdsPayrollCategory) findSingle(query, name, type);
        } else {
            return (EdsPayrollCategory) findSingle(query, name);
        }
    }

    public List<EdsPayrollCategory> getAdvanceCategories() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct c.* ");
        sql.append(" from  " + getCompanyId() + ".category c ");
        sql.append(" where c.isAdvancePayment=true ");
        return findNative(sql.toString(), EdsPayrollCategory.class);
    }

    public void deleteCategories(Integer id) {
        update("update EdsPayrollCategory c set c.deleted=true where c.id=" + id);
    }

    @Override
    public boolean isCategoryCodeExists(String type, String code, Integer categoryID) {
        if (categoryID != null) {
            return find("select c from EdsPayrollCategory c where " + ServerUtils.checkForDeleted("c.deleted") + "  and c.type=? and c.code=? and c.objectID not in (" + categoryID + ")", type, code).size() > 0;
        } else {
            return find("select c from EdsPayrollCategory c where " + ServerUtils.checkForDeleted("c.deleted") + "  and c.type=? and c.code=?", type, code).size() > 0;
        }
    }

    @Override
    public PaymentDeductionSelectItem[] getCategoriesForLookUp(ListingFilterParameter filterParametrs) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c from EdsPayrollCategory c ");
        if (filterParametrs.getAccountType() != null) {
            String type = filterParametrs.isPayment() ? EdsPayrollCategory.PAYMENT : filterParametrs.getAccountType();
            sql.append(" where c.type ='" + type + "' ");
        } else {
            sql.append(" where 1=1 ");
        }
        if (filterParametrs.isValidSearchKey()) {
            sql.append(" and lower(c.name) like '" + filterParametrs.getSqlSearchKey() + "' ");
        }
        if (filterParametrs.isNewType()) {
            sql.append(" and c.code!='" + Constants.STATUTORY_MATERNITY_PAY + "'");
        }
        if (filterParametrs.isCorporate()) {
            sql.append(" and (c.arabic is true").append(" or c.forAll is true)");
        } else {
            sql.append(" and " + ServerUtils.checkForDeleted("c.arabic"));
        }
        if (filterParametrs.isActive()) {
            sql.append(" and c.isCashAdvance is true");
        }
        sql.append(" and " + ServerUtils.checkForDeleted("c.deleted"));
        sql.append(" order by c.name");
        List<EdsPayrollCategory> categoryList;
        if (filterParametrs.getLimit() != 0 && filterParametrs.getLimit() > 0) {
            categoryList = findInterval(sql.toString(), filterParametrs.getStart(), filterParametrs.getLimit());
        } else {
            categoryList = findLimited(sql.toString(), 20);
        }
        PaymentDeductionSelectItem[] items = new PaymentDeductionSelectItem[categoryList.size()];
        int i = 0;
        for (EdsPayrollCategory c : categoryList) {
            items[i++] = c.createPaymentDeductionSelectItem();
        }
        return items;
    }


    @Override
    public PaymentDeductionSelectItem[] getCategoriesForBulkAdd(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c FROM EdsPayrollCategory c ");
        sql.append("WHERE c.type IN ('" + EdsPayrollCategory.PAYMENT + "', '" + EdsPayrollCategory.DEDUCTION + "') ");
        sql.append(" AND ").append(ServerUtils.checkForDeleted("c.deleted"));
        String searchKey = fp.getSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            String key = searchKey.toLowerCase();
            sql.append(" and (LOWER(c.name) LIKE '").append(key).append("%' OR LOWER(c.code) LIKE '").append(key).append("%' OR LOWER(c.type) LIKE '").append(key).append("%')");
        }
        sql.append(" ORDER BY c.name ");

        TypedQuery<EdsPayrollCategory> query = this.slaveEntityManager.createQuery(sql.toString(), EdsPayrollCategory.class)
                .setFirstResult(fp.getStart());

        if (fp.getLimit() > 0 && fp.getLimit() <= 3000) {
            query = query.setMaxResults(fp.getLimit());
        }

        List<EdsPayrollCategory> categoryList = query.getResultList();

        PaymentDeductionSelectItem[] items = new PaymentDeductionSelectItem[0];

        if (categoryList != null && !categoryList.isEmpty()) {
            items = new PaymentDeductionSelectItem[categoryList.size()];
            int i = 0;

            for (EdsPayrollCategory category : categoryList) {
                items[i++] = category.createPaymentDeductionSelectItem();
            }
        }

        return items;
    }

    @Override
    public Integer getCategoriesCountForBulkAdd(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(c.id) FROM EdsPayrollCategory c ");
        sql.append("WHERE c.type IN ('" + EdsPayrollCategory.PAYMENT + "', '" + EdsPayrollCategory.DEDUCTION + "') ");
        sql.append("AND ").append(ServerUtils.checkForDeleted("c.deleted"));
        String searchKey = fp.getSearchKey();
        if (searchKey != null && !searchKey.isEmpty()) {
            String key = searchKey.toLowerCase();
            sql.append(" and (LOWER(c.name) LIKE '").append(key).append("%' OR LOWER(c.code) LIKE '").append(key).append("%' OR LOWER(c.type) LIKE '").append(key).append("%')");
        }
        Long total = (Long) findSingle(sql.toString());
        return total.intValue();
    }

    @Override
    public void deleteReferenceBySchemaID(Integer schemaID) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getCompanyId()).append(".category  ");
        sql.append("set pensionscheme_id=null ").append(" where pensionscheme_id=").append(schemaID);
        updateNative(sql.toString());
    }

    public void deleteSickeLeaveSettingsRefernce() {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getCompanyId()).append(".category  ");
        sql.append("set sickleavesettingsid=null ");
        updateNative(sql.toString());
    }

    @Override
    public void deleteReferenceByEndOfServiceSettings(Integer eosId) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getCompanyId()).append(".category  ");
        sql.append("set endOfServiceSettings_id=null ").append(" where endOfServiceSettings_id=").append(eosId);
        updateNative(sql.toString());
    }

    @Override
    public void resetDefaultCategory() {
        update("update EdsPayrollCategory c set c.defaultCategory = NULL where c.defaultCategory is true");
    }

    @Override
    public EdsPayrollCategory getDefaultCategory() {
        return (EdsPayrollCategory) findSingle("select c from EdsPayrollCategory c where c.defaultCategory is true");
    }

    @Override
    public List<EdsPayrollCategory> getCategoriesByCodes(String... codes) {
        if (codes == null || codes.length == 0) {
            return Collections.emptyList();
        }
        final String sql = "select c from EdsPayrollCategory c " +
                           "    where c.code in (:codes) " +
                           "        and (c.deleted is null or c.deleted <> true)";

        return this.slaveEntityManager.createQuery(sql, EdsPayrollCategory.class)
                                 .setParameter("codes", Lists.newArrayList(codes))
                                 .getResultList();
    }

    @Override
    public List<EdsPayrollCategory> getCategoryLinkedCategories(Integer paydeductionId) {
        if (paydeductionId == null) {
            return Collections.emptyList();
        }
        StringBuilder sql = new StringBuilder();

        sql.append("select c.* from ").append(getCompanyId()).append(".category c ");
        sql.append(" where c.id in (");

        sql.append(" select coalesce(cat.id, 0) catId from ").append(getCompanyId()).append(".paymentDeductionsCategories pdc");
        sql.append(" left join ").append(getCompanyId()).append(".category cat on cat.id= pdc.categoryid");
        sql.append(" where (cat.deleted = 'false' or cat.deleted is null) and pdc.paymentdeductionid  = ").append(paydeductionId);
        sql.append(" group by cat.id");
        sql.append(" UNION ALL ");
        sql.append(" select coalesce(cat.id, 0) catId from ").append(getCompanyId()).append(".PayrollGlobalSettingsItemCategories pgsc");
        sql.append(" left join ").append(getCompanyId()).append(".category cat on cat.id= pgsc.categoryid");
        sql.append(" where (cat.deleted = 'false' or cat.deleted is null) and pgsc.pdsId =");
        sql.append(" (select coalesce(pd.pds_id, 0) from ").append(getCompanyId()).append(".PaymentDeduction pd ");
        sql.append(" where pd.id = ").append(paydeductionId).append(")");
        sql.append(" group by cat.id");
        sql.append(")");

        return this.slaveEntityManager.createNativeQuery(sql.toString(), EdsPayrollCategory.class)
                .getResultList();
    }
}
