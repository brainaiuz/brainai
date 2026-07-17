package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.accounting.EdsCustomerPrepaymentNote;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("invoicePaymentManager")
public class InvoicePaymentManagerImpl extends BaseManager<EdsInvoicePayment> implements InvoicePaymentManager {

    public InvoicePaymentManagerImpl() {
        super(EdsInvoicePayment.class);
    }

    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private CurrencyManager currencyManager;


    public List<EdsInvoicePayment> getPayments(EdsInvoice invoice) {
        return find("select ip from EdsInvoicePayment ip" +
                        " left outer join ip.status s" +
                        " where " + ServerUtils.checkForDeleted("ip.deleted") + " and ip.invoice=? and (s is null or s.code != ?) order by ip.paymentDate desc",
                invoice, EdsInvoicePayment.REVERSED);
    }

    @Override
    public List<EdsInvoicePayment> getPaymentsAll(EdsInvoice invoice) {
        if (invoice == null) {
            return Collections.emptyList();
        }
        return find("select ip from EdsInvoicePayment ip" +
                        " left outer join ip.status s" +
                        " where ip.invoice=? and (s is null or s.code != ?) order by ip.paymentDate desc",
                invoice, EdsInvoicePayment.REVERSED);

    }

    @Override
    public List<EdsInvoicePayment> getRefundsAll(EdsInvoice creditNote) {
        return find("select ip from EdsInvoicePayment ip" +
                        " left outer join ip.status s" +
                        " where (ip.deleted is null or ip.deleted = false) and ip.creditNote=? and (s is null or s.code != ?) order by ip.paymentDate desc",
                creditNote, EdsInvoicePayment.REVERSED);
    }

    @Override
    public BigDecimal getManualPaymentAmount(Integer manualJournalID, Integer crmAccountID, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(case when mj.currencyid = app.currencyid then COALESCE(app.amountininvoicecurrency,app.amount) else app.baseamount * mj.exchangerate end) amount from ").append(getCompanyId()).append(".invoicepayments  app \n");
        sql.append("inner join ").append(getCompanyId()).append(".manualjournal mj on mj.id = app.manualJournalID \n");
        sql.append("left outer join ").append(getCompanyId()).append(".reference s on s.id = app.statusId \n");
        sql.append("where app.deleted is not true and (s.id is null or s.code != 'IS_REVERSED') \n");
        sql.append("and app.manualJournalID = ? and app.crmAccountID = ? and app.type = ? \n");
        BigDecimal amount = (BigDecimal) findNativeSingle(sql.toString(), manualJournalID, crmAccountID, type);

        return amount != null ? amount : AccountingConstants.ZERO;
    }

    @Override
    public BigDecimal getBankTransferPaymentAmount(Integer bankTransferID, Integer crmAccountID, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(case when bt.currencyid = app.currencyid then COALESCE(app.amountininvoicecurrency,app.amount) else app.baseamount * bt.exchangeRate end) amount from ").append(getCompanyId()).append(".invoicepayments  app \n");
        sql.append("inner join ").append(getCompanyId()).append(".spendreceivemoney bt on bt.id = app.bankTransferID \n");
        sql.append("left outer join ").append(getCompanyId()).append(".reference s on s.id = app.statusId \n");
        sql.append("where app.deleted is not true and (s.id is null or s.code != 'IS_REVERSED') \n");
        sql.append("and app.bankTransferID = ? and app.crmAccountID = ? and app.type = ? \n");
        BigDecimal amount = (BigDecimal) findNativeSingle(sql.toString(), bankTransferID, crmAccountID, type);

        return amount != null ? amount : AccountingConstants.ZERO;
    }

    @Override
    public BigDecimal getAppliedCreditAmount(Integer crmAccountID, String type) {
        BigDecimal amount = (BigDecimal) findSingle("select sum(ip.baseAmount) from EdsInvoicePayment ip " +
                " left outer join ip.crmAccount ca " +
                " left outer join ip.status s " +
                " where " + ServerUtils.checkForDeleted("ip.deleted") + " and (s.objectID is null or s.code != 'IS_REVERSED') and ca.objectID=? and ip.type=?", crmAccountID, type);
        return amount != null ? amount : AccountingConstants.ZERO;
    }

    @Override
    public BigDecimal getAppliedPrePaymentAmountInBase(Integer crmAccountID, Integer paymentID, String type) {
        BigDecimal amount = (BigDecimal) findSingle("select sum(coalesce(ip.baseAmount,0)) from EdsInvoicePayment ip " +
                " left outer join ip.crmAccount ca " +
                " left outer join ip.status s " +
                " where " + ServerUtils.checkForDeleted("ip.deleted") + " and ip.appliedPayment.objectID = ? and (s.objectID is null or s.code != 'IS_REVERSED') and ca.objectID=? and ip.type=?", paymentID, crmAccountID, type);
        return amount != null ? amount : AccountingConstants.ZERO;
    }

    public List<String> getAppliedPrepaymentsNumberFirst(Integer crmAccountID, Integer paymentID, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select pp.number from ").append(getCompanyId()).append(".invoicepayments  app \n");
        sql.append("inner join ").append(getCompanyId()).append(".invoice inv on inv.id = app.invoiceid \n");
        sql.append("inner join ").append(getCompanyId()).append(".invoicepayments pp on pp.id = app.appliedpaymentid \n");
        sql.append("left outer join ").append(getCompanyId()).append(".reference s on s.id = app.statusId \n");
        sql.append("where app.deleted is not true and (s.id is null or s.code != 'IS_REVERSED') \n");
        sql.append("and pp.id = ").append(paymentID).append(" and app.crmAccountID = ").append(crmAccountID).append(" and app.type = '").append(type).append("'");

        return findNative(sql.toString());
    }

    @Override
    public BigDecimal getAppliedPrePaymentAmount(Integer crmAccountID, Integer paymentID, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(case when inv.currency_id = pp.currencyid then COALESCE(app.amountininvoicecurrency,app.amount) else app.baseamount * pp.exchangerate end) amount from ")
                .append(getCompanyId()).append(".invoicepayments  app \n")
                .append("left join ").append(getCompanyId()).append(".invoice inv on inv.id = app.invoiceid \n")
                .append("inner join ").append(getCompanyId()).append(".invoicepayments pp on pp.id = app.appliedpaymentid \n")
                .append("left outer join ").append(getCompanyId()).append(".reference s on s.id = app.statusId \n")
                .append("where app.deleted is not true and (s.id is null or s.code != 'IS_REVERSED') \n")
                .append("and pp.id = ? and app.crmAccountID = ? and app.type = ? \n");
        BigDecimal amount = (BigDecimal) findNativeSingle(sql.toString(), paymentID, crmAccountID, type);

        return amount != null ? amount : AccountingConstants.ZERO;
    }

    @Override
    public BigDecimal getRefundPrePaymentAmount(Integer crmAccountID, Integer paymentID, String refundType) {

        StringBuilder sqlRefund = new StringBuilder();
        sqlRefund.append("select sum(case when pp.currencyID = app.currencyid then coalesce(app.amountininvoicecurrency,app.amount) else app.baseamount * pp.exchangerate end) amount from ").append(getCompanyId()).append(".invoicepayments  app \n");
        sqlRefund.append("inner join ").append(getCompanyId()).append(".invoicepayments pp on pp.id = app.appliedpaymentid \n");
        sqlRefund.append("left outer join ").append(getCompanyId()).append(".reference s on s.id = app.statusId \n");
        sqlRefund.append("where app.deleted is not true and (s.id is null or s.code != 'IS_REVERSED') \n");
        sqlRefund.append("and pp.id = ? and app.crmAccountID = ? and app.type = ? \n");
        BigDecimal refundAmount = (BigDecimal) findNativeSingle(sqlRefund.toString(), paymentID, crmAccountID, refundType);
        return refundAmount != null ? refundAmount : BigDecimal.ZERO;
    }


    @Override
    public BigDecimal getAppliedPrePaymentAmounts(Integer crmAccountID, Integer paymentID, String paymentType, String refundType) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        BigDecimal amount = getAppliedPrePaymentAmount(crmAccountID, paymentID, paymentType);

        if (amount != null) {
            totalAmount = totalAmount.add(amount);
        }

        BigDecimal refundAmount = getRefundPrePaymentAmount(crmAccountID, paymentID, refundType);
        if (refundAmount != null) {
            totalAmount = totalAmount.add(refundAmount);
        }

        return totalAmount;
    }

    @Override
    public List<EdsInvoicePayment> getAppliedPrepayments(Integer crmAccountID, Integer paymentID, String type) {
        return find("select ip from EdsInvoicePayment ip" +
                " left outer join ip.crmAccount ca " +
                " left outer join ip.status s" +
                " where " + ServerUtils.checkForDeleted("ip.deleted") + " and (s is null or s.code != 'IS_REVERSED') and ip.appliedPayment.objectID = ? and ca.objectID=? and ip.type=? order by ip.paymentDate", paymentID, crmAccountID, type);
    }

    @Override
    public List<EdsInvoicePayment> getRefundItems(Integer refundPaymentID) {
        return find("select ip from EdsInvoicePayment ip" +
                " left outer join ip.status s" +
                " where " + ServerUtils.checkForDeleted("ip.deleted") + " and (s is null or s.code != 'IS_REVERSED') and ip.paymentRefundID = ? order by ip.paymentDate", refundPaymentID);
    }

    @Override
    public List<EdsInvoicePayment> getExpensePaymentItems(Integer expenseID) {
        return find("select ip from EdsInvoicePayment ip" +
                " left outer join ip.status s" +
                " where " + ServerUtils.checkForDeleted("ip.deleted") + " and (s is null or s.code != 'IS_REVERSED') and ip.expenseID = ? order by ip.paymentDate", expenseID);
    }

    public boolean isAppliedItemExist() {
        List<EdsInvoicePayment> result = find("select ip from EdsInvoicePayment ip where " + ServerUtils.checkForDeleted("ip.deleted") + " and ip.appliedPayment != null");
        return result.size() != 0;
    }

    public List<EdsInvoicePayment> getRefunds(EdsInvoice creditNote) {
        return find("select ip from EdsInvoicePayment ip" +
                        " left outer join ip.status s" +
                        " where " + ServerUtils.checkForDeleted("ip.deleted") + " and ip.creditNote=? and (s is null or s.code != ?) order by ip.paymentDate desc",
                creditNote, EdsInvoicePayment.REVERSED);
    }

    public Double getPaymentsSum(Integer invoiceID) {
        return (Double) findSingle("select sum(ip.amount) from EdsInvoicePayment ip where ip.invoice.objectID=?", invoiceID);
    }

    public BigDecimal getInvoicePayment(Integer invoiceID) {
        return (BigDecimal) findSingle("select ip.amount from EdsInvoicePayment ip where ip.invoice.objectID=?", invoiceID);
    }

    @Override
    public BigDecimal getCrmAccountTotalAmount(Integer crmAccountId, Integer invoiceId) {
        BigDecimal amount = (BigDecimal) findSingle("select sum(ip.amount) from EdsInvoicePayment ip " +
                " where " + ServerUtils.checkForDeleted("ip.deleted") + " and ip.crmAccount.objectID=? and ip.saleInvoice.objectID=? and ip.type=?", crmAccountId, invoiceId, AccountingConstants.RECEIVABLE_PREPAYMENT);
        return amount != null ? amount : AccountingConstants.ZERO;
    }

    @Override
    public List<EdsInvoicePayment> getOderPrePaymentAmount(Integer orderId) {
        return find("select ip from EdsInvoicePayment ip " +
                " where " + ServerUtils.checkForDeleted("ip.deleted") + " and ip.saleQuote.objectID=? and ip.type=?", orderId, AccountingConstants.RECEIVABLE_PREPAYMENT);
    }

    public boolean isReversed(Integer paymentID) {
        Integer paymentTransactionID = (Integer) findSingle("select objectID from EdsInvoicePaymentTransaction where invoicePayment.objectID = ?", paymentID);
        if (paymentTransactionID != null) {
            EdsTransaction transaction = (EdsTransaction) findSingle("from EdsTransaction where reversalTransaction.objectID = ?", paymentTransactionID);
            if (transaction != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<EdsInvoicePayment> getInvoicePaymentByBankTransfer(Integer bankTransferId) {
        return find("select ip from EdsInvoicePayment ip" +
                " where " + ServerUtils.checkForDeleted("ip.deleted") + " and ip.bankTransferID=? order by ip.paymentDate", bankTransferId);
    }

    @Override
    public List<EdsInvoicePayment> getInvoicePaymentByManualJournal(Integer manualJournalId) {
        return find("select ip from EdsInvoicePayment ip" +
                " where " + " ip.manualJournalID=? order by ip.paymentDate", manualJournalId);
    }

    @Override
    public Boolean hasCreditDebitNote(Integer invoiceId) {
        var invoicePayment = (EdsInvoicePayment) findSingle("select ip from EdsInvoicePayment ip where (ip.deleted = false or ip.deleted is null) and ip.invoice.objectID=?", invoiceId);
        return invoicePayment != null && invoicePayment.getCreditNote() != null;
    }

    @Override
    public ListResult<PrePaymentListItem> getPrePayments(ListingFilterParameter filterParameter) {

        ListResult<PrePaymentListItem> prePayments = new ListResult<>();

        StringBuilder query = new StringBuilder();
        query.append("select pp.*");
        getCoreSqlQuery(filterParameter, query);
        String sortField = filterParameter.getSortField();
        String ascOrDesc = filterParameter.getSortDir() == 2 ? " desc" : " asc";

        if (PrePaymentListItem.CUSTOMER.equals(sortField) || PrePaymentListItem.SUPPLIER.equals(sortField)) {
            query.append(" ORDER BY ca.name " + ascOrDesc);
        } else if (PrePaymentListItem.PAY_ACCOUNT.equals(sortField)) {
            query.append(" ORDER BY ac.name " + ascOrDesc);
        } else if (PrePaymentListItem.NOTE.equals(sortField)) {
            query.append(" ORDER BY pp.note " + ascOrDesc);
        } else if (PrePaymentListItem.REFERENCE.equals(sortField)) {
            query.append(" ORDER BY pp.reference " + ascOrDesc);
        } else if (PrePaymentListItem.AMOUNT.equals(sortField)) {
            query.append(" ORDER BY pp.amount " + ascOrDesc);
        } else if (PrePaymentListItem.DATE.equals(sortField)) {
            query.append(" ORDER BY pp.paymentDate " + ascOrDesc);
        } else if (PrePaymentListItem.CURRENCY.equals(sortField)) {
            query.append(" ORDER BY cc.name " + ascOrDesc);
        } else if (PrePaymentListItem.CODE.equals(sortField)) {
            query.append(" ORDER BY pp.number " + ascOrDesc);
        } else if (PrePaymentListItem.PROJECT.equals(sortField)) {
            query.append(" ORDER BY pr.name " + ascOrDesc);
        } else if (PrePaymentListItem.STATUS.equals(sortField)) {
            query.append(" ORDER BY pp.paymentstatus " + ascOrDesc);
        } else if (PrePaymentListItem.CREATOR.equals(sortField)) {
            query.append(" ORDER BY u.firstname " + ascOrDesc);
        } else if (PrePaymentListItem.DEPARTMENT.equals(sortField)) {
            query.append(" ORDER BY t.name " + ascOrDesc);
        } else {
            query.append(" ORDER BY pp.id DESC");
        }
        if (filterParameter.getLimit() > 0) {
            query.append(" LIMIT ").append(filterParameter.getLimit());
        }
        if (filterParameter.getStart() > 0) {
            query.append(" OFFSET ").append(filterParameter.getStart());
        }
        List<EdsInvoicePayment> edsItems = (List<EdsInvoicePayment>) findNative(query.toString(), EdsInvoicePayment.class);

        ArrayList<PrePaymentListItem> items = new ArrayList<>();
        for (EdsInvoicePayment p : edsItems) {
            PrePaymentListItem ppi = new PrePaymentListItem();
            ppi.setObjectID(p.getObjectID());
            if (p.getCrmAccount() != null) {
                ppi.setCustomerName(p.getCrmAccount().getName());
                ppi.setAccountNumber(p.getCrmAccount().getNumber());
                ppi.setAccountID(p.getCrmAccount().getObjectID());
            }
            if (p.getProject() != null) {
                ppi.setProject(p.getProject().getName());
            }
            ppi.setPayAccount(p.getAccount().getName());
            ppi.setNote(p.getNote());
            ppi.setReference(p.getReference());
            ppi.setAmount(p.getAmountInInvoiceCurrency() != null ? p.getAmountInInvoiceCurrency() : p.getAmount());
            ppi.setEditable(true);
            ppi.setStatus(p.getPaymentStatus() != null ? p.getPaymentStatus() : "");
            ppi.setDate(new DateNonConvertable(p.getPaymentDate()));
            ppi.setCreator(p.getUser() != null ? p.getUser().getName() : "");
            if (p.getCurrencyID() != null) {
                EdsCurrency currency = currencyManager.get(p.getCurrencyID());
                ppi.setCurrency(currency != null ? currency.getName() : "");
            }
            if (p.getCrmAccount() != null)
                ppi.setAppliedAmount(getAppliedPrePaymentAmounts(p.getCrmAccount().getObjectID(), p.getObjectID(), AccountingConstants.RECEIVABLE_PREPAYMENT.equals(p.getType())
                                ? AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE
                                : AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE,
                        AccountingConstants.RECEIVABLE_PREPAYMENT.equals(p.getType()) ? AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND : AccountingConstants.PAYABLE_PREPAYMENT_REFUND));
            ppi.setNumber(p.getNumber());
            if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(p.getType())) {
                if (p.getSaleQuote() != null) {
                    ppi.setSaleQuote(new SelectItem(p.getSaleQuote().getObjectID(), p.getSaleQuote().getNumber()));
                }
                if (p.getRentalOrder() != null) {
                    SelectItem rentalOrder = new SelectItem(p.getRentalOrder().getObjectID(), p.getRentalOrder().getNumber());
                    rentalOrder.setDescription("rentalOrders");
                    ppi.setRentalOrder(rentalOrder);
                }
            } else {
                if (p.getPurchaseOrder() != null) {
                    ppi.setPurchaseOrder(new SelectItem(p.getPurchaseOrder().getObjectID(), p.getPurchaseOrder().getNumber()));
                }
            }
            if (p.getSaleInvoice() != null) {
                ppi.setSaleInvoice(new SelectItem(p.getSaleInvoice().getObjectID(), p.getSaleInvoice().getNumber()));
            }
            if (p.getDepartment() != null) {
                ppi.setDepartment(p.getDepartment().getName());
            }

            if (p.getPrepaymentCustomFields() != null && filterParameter.isCustomFieldsShown()) {
                ppi.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(p.getPrepaymentCustomFields(), filterParameter.getColumnsOfListing()));
            }

            items.add(ppi);
        }
        //sort by remaining amount
        if (PrePaymentListItem.REMAIN.equals(sortField)) {
            if (filterParameter.getSortDir() == 1) {
                items.sort(Comparator.comparing(PrePaymentListItem::getRemainingBalance));
            } else {
                items.sort(Comparator.comparing(PrePaymentListItem::getRemainingBalance).reversed());
            }
        }
        prePayments.setList(items);
        if (!filterParameter.isFromExcelPDF()) {
            prePayments.setTotal(getPrePaymentCount(filterParameter));
        }
        return prePayments;
    }

    @Override
    public Integer getPrePaymentCount(ListingFilterParameter filterParameter) {
        StringBuilder query = new StringBuilder();
        query.append("select count(pp.id)");
        getCoreSqlQuery(filterParameter, query);
        return ((BigInteger) findNativeSingle(query.toString())).intValue();
    }

    private void getCoreSqlQuery(ListingFilterParameter filterParameter, StringBuilder query) {
        query.append(" from ").append(getCompanyId()).append(".invoicePayments pp");
        query.append(" left join ").append(getCompanyId()).append(".crmaccount ca on pp.crmaccountid = ca.id");
        query.append(" left join ").append(getCompanyId()).append(".account ac on pp.accountid = ac.id");
        query.append(" left join ").append(getCompanyId()).append(".project pr on pp.projectid = pr.id");
        query.append(" left join ").append(getCompanyId()).append(".team t on pp.departmentid = t.id");
        query.append(" left join ").append(getCompanyId()).append(".myuser u on pp.userId = u.id");
        query.append(" left join ").append(getCompanyId()).append(".quote sq on pp.saleQuoteId = sq.id");
        query.append(" left join ").append(getCompanyId()).append(".quote po on pp.purchaseOrderId = po.id");
        query.append(" left join ").append(getPublic()).append(".currency cc on pp.currencyId = cc.id");
        query.append(" where (pp.deleted <> true or pp.deleted is null)");
        query.append(" and pp.type = '").append(filterParameter.getViewType()).append("'");
        if (filterParameter.getCrmAccountId() != null && -1 != filterParameter.getCrmAccountId()) {//-1 for reset filter
            query.append(" and ca.id = ").append(filterParameter.getCrmAccountId());
        }
        if (filterParameter.getClientId() != null && -1 != filterParameter.getClientId()) {
            query.append(" and ca.id = ").append(filterParameter.getClientId());
        }
        if (filterParameter.getSupplierId() != null && -1 != filterParameter.getSupplierId()) {
            query.append(" and ca.id = ").append(filterParameter.getSupplierId());
        }
        if (filterParameter.getStatusCode() != null && !"".equals(filterParameter.getStatusCode())) {
            if (AccountingConstants.PRE_PAYMENT_OPEN_STATUS.equals(filterParameter.getStatusCode())) {
                query.append(" and (pp.paymentStatus='").append(filterParameter.getStatusCode()).append("'");
                query.append(" or pp.paymentStatus is null)");
            } else {
                query.append(" and pp.paymentStatus='").append(filterParameter.getStatusCode()).append("'");
            }
        }
        if (filterParameter.getFromAmount() != null) {
            query.append(" and pp.amount >= ").append(filterParameter.getFromAmount());
        }
        if (filterParameter.getToAmount() != null) {
            query.append(" and pp.amount <= ").append(filterParameter.getToAmount());
        }
        if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null) {
            query.append(" and (pp.paymentDate between '" + filterParameter.getStartDate() + "' and '" + filterParameter.getEndDate() + "')");
        }
        if (filterParameter.getEmployeeId() != null && -1 != filterParameter.getEmployeeId()) {
            query.append(" and pp.userId = " + filterParameter.getEmployeeId());
        }
        if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(filterParameter.getViewType()) && filterParameter.getQuoteId() != null) {
            query.append(" and sq.id = " + filterParameter.getQuoteId());
        } else if (AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(filterParameter.getViewType()) && filterParameter.getQuoteId() != null) {
            query.append(" and po.id = " + filterParameter.getQuoteId());
        }

        query.append(" and pp.paymentStatus != '").append(AccountingConstants.VOID).append("' ");
        if (filterParameter.getSqlSearchKey() != null) {
            query.append(" and (lower(ca.name) like '" + filterParameter.getSqlSearchKey() + "' or ");
            query.append("lower(ca.number) like '" + filterParameter.getSqlSearchKey() + "' or ");
            query.append("lower(pp.paymentStatus) like '" + filterParameter.getSqlSearchKey() + "' or ");
            query.append("lower(ac.name) like '" + filterParameter.getSqlSearchKey() + "' or ");
            query.append("lower(pr.name) like '" + filterParameter.getSqlSearchKey() + "' or ");
            query.append("lower(pp.note) like '" + filterParameter.getSqlSearchKey() + "' or ");
            query.append("lower(cc.name) like '" + filterParameter.getSqlSearchKey() + "' or ");
            query.append("lower(pp.reference) like '" + filterParameter.getSqlSearchKey() + "' or ");
            if (AccountingConstants.RECEIVABLE_PREPAYMENT.equals(filterParameter.getViewType())) {
                query.append("lower(sq.number) like '" + filterParameter.getSqlSearchKey() + "' or ");
            } else if (AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(filterParameter.getViewType())) {
                query.append("lower(po.number) like '" + filterParameter.getSqlSearchKey() + "' or ");
            }
            query.append("lower(pp.number) like '" + filterParameter.getSqlSearchKey() + "') ");


        }
    }

    @Override
    public List<EdsInvoicePayment> getOpenPrePayments(Integer currencyId, Integer accountID, String type, String statuses) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        param.put("accountID", accountID);
        param.put("currencyID", currencyId);
        param.put("type", type);
        query.append("select pp from EdsInvoicePayment pp where pp.crmAccount.objectID = :accountID and pp.type = :type and pp.currencyID = :currencyID and paymentStatus in  (" + statuses + ") and " + ServerUtils.checkForDeleted("pp.deleted"));
        return (List<EdsInvoicePayment>) findByNamedParams(query.toString(), param);
    }

    @Override
    public List<EdsInvoicePayment> getAccountPrePayments(Integer accountID, String type) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        param.put("accountID", accountID);
        param.put("type", type);
        query.append("select pp from EdsInvoicePayment pp where pp.crmAccount.objectID = :accountID and pp.type = :type and " + ServerUtils.checkForDeleted("pp.deleted"));
        return (List<EdsInvoicePayment>) findByNamedParams(query.toString(), param);
    }

    @Override
    public List<EdsInvoicePayment> getAccountOldPrePayments(Integer accountID, String type) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        param.put("accountID", accountID);
        param.put("type", type);
        query.append("select pp from EdsInvoicePayment pp where pp.crmAccount.objectID = :accountID and pp.type = :type and pp.paymentStatus is null and " + ServerUtils.checkForDeleted("pp.deleted"));
        return (List<EdsInvoicePayment>) findByNamedParams(query.toString(), param);
    }

    @Override
    public List<EdsInvoicePayment> getAccountPrePaymentsWithoutReversed(Integer accountID, String type, Integer projectID) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> param = new HashMap<>();
        param.put("accountID", accountID);
        param.put("type", type);
        query.append("select pp from EdsInvoicePayment pp "
                + " left outer join pp.status s " +
                " where pp.crmAccount.objectID = :accountID and pp.type = :type and (s.objectID is null or s.code != 'IS_REVERSED') and pp.paymentStatus != '" + AccountingConstants.VOID + "' and " +
                " (pp.paymentStatus is not null and pp.paymentStatus != 'PRE_PAYMENT_APPLIED_STATUS') and " + ServerUtils.checkForDeleted("pp.deleted") + (projectID != null ? " and pp.project.objectID = " + projectID : ""));
        return (List<EdsInvoicePayment>) findByNamedParams(query.toString(), param);
    }

    @Override
    public List<EdsInvoicePayment> getSaleInvoiceRelatedPayments(Integer saleInvoiceId) {
        StringBuilder query = new StringBuilder();
        query.append("select pp from EdsInvoicePayment pp "
                + " left outer join pp.status s " +
                " where  (s.objectID is null or s.code != 'IS_REVERSED') and " +
                " (pp.paymentStatus is not null and pp.paymentStatus != 'PRE_PAYMENT_APPLIED_STATUS') and pp.fromInvoice.objectID = ?");
        return (List<EdsInvoicePayment>) find(query.toString(), saleInvoiceId);
    }

    @Override
    public List<EdsInvoicePayment> getPostDatedPrePayments(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pp FROM EdsInvoicePayment pp join pp.status s \n");
        sql.append("WHERE (pp.deleted is null OR pp.deleted is false) \n");
        sql.append("AND s.code = '").append(EdsInvoicePayment.POST_DATED).append("' \n");
        sql.append("AND to_date(to_char(pp.paymentDate, 'yyyy-MM-dd'), 'yyyy-MM-dd') <= '").append(dateFormat.format(date)).append("' ");

        return find(sql.toString());
    }

    @Override
    public List<EdsInvoicePayment> getBatchPaymentItems(Integer batchPaymentID) {
        return find("select ip from EdsInvoicePayment ip  left join ip.status s where (ip.deleted = false or ip.deleted is null) and (s.objectID is null or s.code != '" + EdsInvoicePayment.REVERSED + "') and ip.underPaymentID is null and ip.batchPaymentID=?", batchPaymentID);
    }

    @Override
    public List<EdsInvoicePayment> getAppliedPaymentItems(Integer paymentID) {
        return find("select ip from EdsInvoicePayment ip  left join ip.status s where (ip.deleted = false or ip.deleted is null) and (s.objectID is null or s.code != '" + EdsInvoicePayment.REVERSED + "') and ip.appliedPayment.objectID=?", paymentID);
    }

    @Override
    public BigDecimal getBatchPaymentItems(Integer batchPaymentID, Integer exceptObjectID, boolean isInvoicePayment) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(coalesce(ip.amount,0)) a from ").append(getCompanyId()).append(".invoicepayments ip \n");
        sql.append("left join ").append(getCompanyId()).append(".reference s on s.id = ip.statusId \n");
        sql.append("where ip.deleted is not true and (s.id is null or s.code != '").append(EdsInvoicePayment.REVERSED).append("') \n");
        sql.append("and ip.batchPaymentID = ").append(batchPaymentID).append(" \n");

        if (isInvoicePayment) {
            sql.append("and ip.id != ").append(exceptObjectID);
        }

        BigDecimal amount = (BigDecimal) findNativeSingle(sql.toString());

        sql = new StringBuilder();
        sql.append("select sum(coalesce(csp.amount,0)) a from ").append(getCompanyId()).append(".customerPayment csp \n");
        sql.append("where csp.deleted is not true \n");
        sql.append("and csp.batchPaymentID = ").append(batchPaymentID).append(" \n");

        if (!isInvoicePayment) {
            sql.append("and csp.id != ").append(exceptObjectID);
        }

        amount = amount != null ? amount : BigDecimal.ZERO;
        amount = amount.add(findNativeSingle(sql.toString()) != null ? (BigDecimal) findNativeSingle(sql.toString()) : BigDecimal.ZERO);

        return amount;
    }

    @Override
    public Integer getLastIntNumberByType(String type) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select max(numberInt) from EdsInvoicePayment e where type like '%" + type + "%'" + " and e.deleted <> true ");
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null && financialYearStart.getTime().before(new Date())) {
            values.put("financialYearStart", financialYearStart.getTime());
            sql.append(" and e.creationDate > :financialYearStart");
            sql.append(" and e.creationDate is not null");
        }
        return (Integer) findSingleByNamedParams(sql.toString(), values);
    }

    private Calendar getFinancialYearStartIfEnabled(Date creationDate) {
        EdsInvoicingSettings settings = (EdsInvoicingSettings) findSingle("select eis from EdsInvoicingSettings eis ");
        if (settings != null && settings.isNumberingRestartEnabled()) {
            Calendar financialYearStart = new GregorianCalendar();
            if (creationDate != null) {
                financialYearStart.setTime(creationDate);
            }
            financialYearStart.set(Calendar.MONTH, settings.getNumberingRestartMonth());
            financialYearStart.set(Calendar.DATE, settings.getNumberingRestartDate());
            ServerUtils.setBeginningOfTheDay(financialYearStart);
            return financialYearStart;
        }
        return null;
    }

    @Override
    public Integer getInvoicePamyentIdIfPresent(String number, String transferType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> values = new HashMap<>();
        sql.append("select objectID from EdsInvoicePayment e where number = :number and type like '%" + transferType + "%' and e.deleted <> true ");
        values.put("number", number);
        Calendar financialYearStart = getFinancialYearStartIfEnabled(null);
        if (financialYearStart != null) {
            sql.append(" and e.creationDate > :financialYear ");
            sql.append(" and e.creationDate is not null ");
            values.put("financialYear", financialYearStart.getTime());
        }
        return (Integer) findSingleByNamedParams(sql.toString(), values);
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".invoicePayments SET crmAccountID = " + newAccountID + " WHERE crmAccountID in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }

    @Override
    public boolean isDuplicateReference(String refNumber, Integer objectID) {
        return find("select invp from EdsInvoicePayment invp where (invp.deleted is null or invp.deleted is false) and invp.reference = ? " + (objectID != null ? " and invp.objectID != " + objectID : ""), refNumber).size() > 0;
    }

    @Override
    public List<PaymentItem> getInvoicePaymentsByProject(Integer projectId, Date from, Date to) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT\n")
                .append("  ip.reference                   AS reference,\n")
                .append("  COALESCE(ip.amountininvoicecurrency,ip.amount) / ip.exchangerate    AS amount,\n")
                .append("  ip.paymentdate                 AS invoiceDate,\n")
                .append("  coalesce(sum(ip2.amount), 0.0) AS appliedPaymentAmount,\n")
                .append("  ip.exchangerate                AS exchangeRate\n")
                .append("FROM ").append(getCompanyId()).append(".invoicepayments ip\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".invoicepayments ip2 ON ip2.appliedpaymentid = ip.id AND (ip2.deleted IS NULL OR ip2.deleted = FALSE)\n")
                .append("WHERE (ip.deleted IS NULL OR ip.deleted = FALSE) AND ip.projectid = " + projectId + "\n");
        if (from != null && to != null) sql.append(" AND ip.paymentdate < '" + to + "'\n");
        sql.append("GROUP BY ip.id");
        return jdbcSpringManager.getSimJdbcOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(PaymentItem.class));
    }

    @Override
    public EdsInvoicePayment getInvoiceUnderPayment(Integer underPaymentID) {
        return (EdsInvoicePayment) findSingle("select ip from EdsInvoicePayment ip where (ip.deleted = false or ip.deleted is null) and ip.underPaymentID=?", underPaymentID);
    }

    @Override
    public EdsCustomerPrepaymentNote getPrepaymentNote(Integer paymentId) {
        return (EdsCustomerPrepaymentNote) findSingle("select pp from EdsCustomerPrepaymentNote pp where pp.payment.objectID=? order by date desc", paymentId);
    }
}
