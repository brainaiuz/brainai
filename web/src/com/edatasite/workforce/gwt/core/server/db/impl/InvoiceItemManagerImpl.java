package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.InvoiceItemManager;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/9/13
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("invoiceItemManager")
public class InvoiceItemManagerImpl extends BaseManager<EdsInvoiceItem> implements InvoiceItemManager {
    public InvoiceItemManagerImpl() {
        super(EdsInvoiceItem.class);
    }

    @Override
    public void removeTimesheetRelationsForInvoiceItems(List<Integer> invoiceItemsDeleted) {
        StringBuilder idsAsString = new StringBuilder();
        int i=0;
        for (Integer id : invoiceItemsDeleted) {
            if (i != 0)
                idsAsString.append(", ");
            idsAsString.append(id.toString());
            i++;
        }
        if (idsAsString.toString().isEmpty()) {
            return;
        }

        updateNative("update " + getCompanyId() + ".timesheet set invoiceitemid=null, usedininvoice=false where invoiceitemid in (" + idsAsString + ")");
    }

    @Override
    public Integer[] getRelatedTimesheetsByInvoiceItem(Integer invoiceItemID) {
        List<Integer> result = find("select distinct ts.objectID from EdsTimeSheet ts where ts.invoiceItemID = ?", invoiceItemID);
        return result != null ? result.toArray(new Integer[]{}) : null;
    }

    @Override
    public void removeRelatedInvoicesFromBillableExpense(Integer invoiceId) {
        updateNative("update " + getCompanyId() + ".invoiceitem set saleinvoice_id = null where saleinvoice_id = " + invoiceId);
        updateNative("update " + getCompanyId() + ".expense set invoice_id = null where invoice_id = " + invoiceId);
        updateNative("update " + getCompanyId() + ".spendreceivemoneyitem set invoiceId = null where invoiceId = " + invoiceId);
        updateNative("update " + getCompanyId() + ".manualjournalitem set invoiceId = null where invoiceId = " + invoiceId);
        updateNative("update " + getCompanyId() + ".bankcheckitem set invoiceId = null where invoiceId = " + invoiceId);
    }

    @Override
    public List<EdsInvoiceItem> getBillableExpense(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ii from EdsInvoiceItem ii ");
        sql.append(" join ii.invoice i ");
        sql.append(" where ii.client.objectID = ? and ii.saleInvoice.objectID is null and ").append(ServerUtils.checkForDeleted("i.deleted"));
        sql.append(" and i.status.code in ('APPROVE', 'OPEN', 'OVER_DUE', 'PAID') ");

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (i.invoiceDate between ? and ?) ");
            return find(sql.toString(), fp.getInvoiceClientId(), fp.getStartDate(), fp.getEndDate());
        } else {
            return find(sql.toString(), fp.getInvoiceClientId());
        }
    }

    @Override
    public Integer getCountOfTaxRateItem(Integer invoiceId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from ").append(getCompanyId()).append(".invoiceItem i ");
        queryBuilder.append("left join ").append(getCompanyId()).append(".vat v on i.markuptaxId = v.id ");
        queryBuilder.append("where v.name = 'Exempt' and i.id = ").append(invoiceId);
        BigInteger bigInteger = (BigInteger) findNativeSingle(queryBuilder.toString());
        return bigInteger.intValue();
    }

    public List<EdsInvoiceItem> getByInvoiceIds(List<Integer> invoiceIds) {
        String sql = """
                select ii from EdsInvoiceItem ii
                left join ii.invoice i
                where i.id in ?""";
        return find(sql, invoiceIds);
    }
}
