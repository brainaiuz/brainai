package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BatchPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Sherzod on 7/6/2015.
 */
@Repository("batchPaymentManager")
public class BatchPaymentManagerImpl extends BaseManager<EdsBatchPayment> implements BatchPaymentManager {
    public BatchPaymentManagerImpl() {
        super(EdsBatchPayment.class);
    }

    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    private AccountingManager accountingManager;

    @Override
    public ListResult<EdsBatchPayment> getBatchPayments(ListingFilterParameter filterParameter) {
        boolean isReceivable = Constants.RECEIVABLE.equals(filterParameter.getDataType());
        ArrayList<EdsBatchPayment> result;
        List<String> customFieldList = new ArrayList<>();
        boolean hasCustomFieldColumnName = false;
        int i = 0;
        if (filterParameter.isCustomFieldsShown()) {
            customFieldList = companyCFSettingsManager.getCompanyCustomFieldsColumnCodesList(isReceivable ? ViewName.BatchInvoicePaymentView.name() : ViewName.BatchPayBillView.name());
            hasCustomFieldColumnName = filterParameter.isCustomFieldsShown() && customFieldList != null /*&& customFieldList.contains(filterParameter.getSortField())*/;
        }
        StringBuilder sql = new StringBuilder();
        //sql.append("select bp.* " + (hasCustomFieldColumnName ? ", cf." + filterParameter.getSortField() : "")).append(" from ").append(getCompanyId()).append(".batchpayment bp ");
        sql.append("select bp.* ");
        if (hasCustomFieldColumnName) {
            if (customFieldList.contains(filterParameter.getSortField())) {
                sql.append(", cf.").append(filterParameter.getSortField());
            }
            if (filterParameter.getCustomFields() != null) {
                for (String key : filterParameter.getCustomFields().keySet()) {
                    if (key != null) {
                        sql.append(", cf.").append(key);
                    }
                }
            }
        }
        sql.append(" from ").append(getCompanyId()).append(".batchpayment bp ");
        sql.append("left join ").append(getCompanyId()).append(".crmAccount crmAccount on crmAccount.id = bp.crmaccountid ");
        sql.append("left join ").append(getCompanyId()).append(".account account on account.id = bp.accountid ");
        sql.append("left join ").append(getCompanyId()).append(".paymentmethod method on method.id = bp.paymentmethod ");
        sql.append("left join ").append(getCompanyId()).append(".team dep on dep.id = bp.departmentid ");
        sql.append("left join ").append(getCompanyId()).append(".project pr on bp.projectId = pr.id ");
        sql.append("left join ").append(getCompanyId()).append(".team t on bp.departmentid = t.id ");
        sql.append("left join ").append(getCompanyId()).append(".companypdftemplate pdf on pdf.id = bp.pdftemplateid ");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id = bp.creatorid ");
        sql.append("left join ").append(getPublic()).append(".currency cur on cur.id = bp.currencyid ");
        if (hasCustomFieldColumnName) {
            sql.append("left outer join ").append(getCompanyId()).append(".invoicecustomfields cf on cf.id = bp.customfields_id ");
        }
        sql.append(" WHERE ").append(ServerUtils.checkForDeleted("bp.deleted")).append(" and ").append(ServerUtils.checkForDeleted("bp.reversed"));
        sql.append(" AND bp.type='" + filterParameter.getDataType() + "'");
        if (isReceivable) {
            if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_RECEIVE_PAYMENT_FULL_LIST_ACCESS)) {
                if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_RECEIVE_PAYMENT_SEE_OWN) && !getUser().hasRole(EdsRole.ADMIN_CODE)) {
                    sql.append(" AND bp.creatorId = " + getUser().getObjectID());
                }
            }
        } else {
            if (!ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_PAY_BILL_FULL_LIST_ACCESS)) {
                if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_PAY_BILL_SEE_OWN) && !getUser().hasRole(EdsRole.ADMIN_CODE)) {
                    sql.append(" AND bp.creatorId = " + getUser().getObjectID());
                }
            }
        }

        if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null) {
            sql.append(" and (bp.date between '" + filterParameter.getStartDate() + "' and '" + filterParameter.getEndDate() + "')\n");
        }
        if (filterParameter.getRelationID() != null) {//payment method
            sql.append(" AND method.id = " + filterParameter.getRelationID());
        }
        if (filterParameter.getCrmAccountId() != null && -1 != filterParameter.getCrmAccountId()) {//-1 for reset filter
            sql.append(" AND crmAccount.id =" + filterParameter.getCrmAccountId());
        }
        if (filterParameter.getFromAmount() != null) {
            sql.append(" AND bp.totalamount >= " + filterParameter.getFromAmount());
        }
        if (filterParameter.getToAmount() != null) {
            sql.append(" AND bp.totalamount <= " + filterParameter.getToAmount());
        }
        if (filterParameter.getEmployeeId() != null && -1 != filterParameter.getEmployeeId()) {
            sql.append(" AND bp.creatorId = " + filterParameter.getEmployeeId());
        }
        if (hasCustomFieldColumnName && filterParameter.getCustomFields() != null) {
            for (String key : filterParameter.getCustomFields().keySet()) {
                if (key != null) {
                    if (i == 0) {
                        sql.append(" AND lower(cf.").append(key).append(") like '").append(filterParameter.getCustomFields().get(key).toLowerCase()).append("' ");
                    } else {
                        sql.append(" OR lower(cf.").append(key).append(") like '").append(filterParameter.getCustomFields().get(key).toLowerCase()).append("' ");
                    }
                    i++;
                }
            }
        }


        if (filterParameter.getSqlSearchKey() != null) {
            sql.append("AND (");
            sql.append(" lower(bp.number) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(bp.reference) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(crmAccount.name) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(account.name) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(pr.name) like '" + filterParameter.getSqlSearchKey() + "' ");
            sql.append(" OR lower(cur.name) like '" + filterParameter.getSqlSearchKey() + "' ");
            if (hasCustomFieldColumnName) {
                if (filterParameter.isCustomFieldsShown() && customFieldList != null && customFieldList.size() > 0) {
                    for (String ccfS : customFieldList) {
                        if (ccfS.contains("string_value")) {
                            sql.append(" or lower(cf.").append(ccfS).append(") like '").append(filterParameter.getSqlSearchKey()).append("' ");
                        }
                    }
                }
            }
            sql.append(") ");
        }

        Integer totalCount = findNative(sql.toString()).size();

        if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
            if (BatchPaymentListItem.NUMBER.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY bp.number ");
            } else if (BatchPaymentListItem.CRM_ACCOUNT.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY crmAccount.name ");
            } else if (BatchPaymentListItem.REFERENCE.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY bp.reference ");
            } else if (BatchPaymentListItem.DATE.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY bp.date ");
            } else if (BatchPaymentListItem.ACCOUNT.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY account.name ");
            } else if (BatchPaymentListItem.PAYMENT_TYPE.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY method.name ");
            } else if (BatchPaymentListItem.CURRENCY.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY cur.name ");
            } else if (BatchPaymentListItem.AMOUNT.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY bp.totalAmount ");
            } else if (BatchPaymentListItem.PROJECT.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY pr.name ");
            } else if (BatchPaymentListItem.CREATOR.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY u.firstname ");
            } else if (BatchPaymentListItem.DEPARTMENT.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY t.name ");
            } else if (hasCustomFieldColumnName) {
                sql.append("ORDER BY cf.").append(filterParameter.getSortField()).append(" ");
            }
            if (!filterParameter.isAscending() && sql.indexOf("ORDER") != -1) {
                sql.append(" DESC ");
            }
        } else {
            sql.append("ORDER BY bp.id DESC ");
        }

        if (filterParameter.getLimit() > 0) {
            sql.append(" LIMIT ").append(filterParameter.getLimit());
        }
        if (filterParameter.getStart() > 0) {
            sql.append(" OFFSET ").append(filterParameter.getStart());
        }
        result = (ArrayList<EdsBatchPayment>) findNative(sql.toString(), EdsBatchPayment.class);
        return new ListResult<>(result, totalCount);
    }

    @Override
    public Integer getBatchPaymentsCount(ListingFilterParameter filterParametrs) {
        Long count = (Long) findSingle("select count(distinct bp.objectID) from EdsBatchPayment bp where bp.type=? and (bp.deleted is null or bp.deleted<>true) and (bp.reversed is null or bp.reversed<>true)", filterParametrs.getDataType());
        return count != null ? count.intValue() : 0;
    }

    public boolean isNumberExists(String number, Integer objectID, String type) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select count(bp.objectID) from EdsBatchPayment bp WHERE bp.type = :type and bp.number = :number");
        sql.append(" and (bp.deleted is null or bp.deleted<>true) and (bp.reversed is null or bp.reversed<>true) ");
        if (objectID != null) {
            sql.append(" and bp.objectID != :objectID");
            values.put("objectID", objectID);
        }
        values.put("type", type);
        values.put("number", number);

        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and bp.creationDate > :financialYearStart");
        }
        Long count = (Long) findSingleByNamedParams(sql.toString(), values);
        return count != null && count.intValue() > 0;
    }

    @Override
    public Integer getLastIntNumber(String type) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select bp.intNumber from EdsBatchPayment bp where bp.type = :type and (bp.deleted is null or bp.deleted<>true) and (bp.reversed is null or bp.reversed<>true) and bp.intNumber is not null ");
        values.put("type", type);
        Calendar financialYearStart = accountingManager.getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and bp.creationDate > :financialYearStart");
        }
        sql.append(" order by bp.intNumber desc");

        Integer lastIntNumber = (Integer) findSingleByNamedParams(sql.toString(), values);
        return lastIntNumber != null ? lastIntNumber : 0;
    }

    @Override
    public EdsBatchPayment getPayment(Integer id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  bp.*, 0 as clazz_  from ").append(getCompanyId()).append(".batchPayment bp where bp.deleted is null or bp.deleted<>true and id=").append(id);
        return (EdsBatchPayment) findNativeSingle(sql.toString(), EdsBatchPayment.class);
    }

}
