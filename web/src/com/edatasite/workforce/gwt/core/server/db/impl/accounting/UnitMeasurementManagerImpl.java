package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.accounting.UnitMeasurementManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRODUCT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 14, 2010
 * Time: 10:27:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("unitMeasurementManager")
public class UnitMeasurementManagerImpl extends BaseManager<EdsUnitMeasurement> implements UnitMeasurementManager {

    public UnitMeasurementManagerImpl() {
        super(EdsUnitMeasurement.class);
    }

    public List<EdsUnitMeasurement> getUnitMeasurements(ListingFilterParameter filterParametrs, Integer companyID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select m FROM EdsUnitMeasurement m WHERE 1=1 and (deleted is null or deleted <> true) ");

        if (filterParametrs != null && filterParametrs.getSqlSearchKey() != null) {
            sql.append("AND (lower(m.name) like '" + filterParametrs.getSqlSearchKey() + "'" +
                    " or lower(m.description) like '" + filterParametrs.getSqlSearchKey() + "')");
        }

        if (filterParametrs != null && filterParametrs.getSortField() != null) {
            if ("name".equals(filterParametrs.getSortField())) {
                sql.append(" order by m.name" + (filterParametrs.getSortDir() == 2 ? " desc" : ""));
            } else if ("description".equals(filterParametrs.getSortField())) {
                sql.append(" order by m.description" + (filterParametrs.getSortDir() == 2 ? " desc" : ""));
            } else {
                sql.append(" order by m.objectID desc");
            }
        } else {
            sql.append(" order by m.objectID desc");
        }
        return find(sql.toString());
    }

    public HashMap<String, Integer> isUnitMeasurementUsed(Integer objectId) {
        Object[] rs = null;
        HashMap<String, Integer> result = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ")
                .append("  (SELECT count(*) ").append("FROM ").append(getCompanyId()).append(".item ").append("WHERE unitmeasurementid = um.id AND (deleted IS NULL OR deleted <> TRUE)) as itemcount, ")
                .append("  (SELECT count(*) ").append("FROM ").append(getCompanyId()).append(".invoiceitem ").append("WHERE unitmeasurementid = um.id AND (deleted IS NULL OR deleted <> TRUE)) as invoiceitemcount, ")
                .append("  (SELECT count(*) ").append("FROM ").append(getCompanyId()).append(".quoteitem ").append("WHERE unitmeasurementid = um.id AND (deleted IS NULL OR deleted <> TRUE)) as quoteitemcount, ")
                .append("(SELECT count(*) ").append("FROM ").append(getCompanyId()).append(".opportunity_item oi ").append("  JOIN ").append(getCompanyId()).append(".opportunity o ON o.id = oi.opportunity_id ")
                .append("WHERE unitmeasurementid = um.id AND (o.deleted IS NULL OR o.deleted <> TRUE)) as opportunityitemcount ")
                .append("from ").append(getCompanyId()).append(".unitmeasurement um where um.id = ?");
        rs = (Object[]) findNativeSingle(sql.toString(), objectId);
        Integer productCount = ((BigInteger)rs[0]).intValue();
        Integer invoiceItemCount = ((BigInteger)rs[1]).intValue();
        Integer quoteItemCount = ((BigInteger)rs[2]).intValue();
        Integer opportunityItemCount = ((BigInteger)rs[3]).intValue();
        if (productCount > 0) {
            result.put(PRODUCT, productCount);
        }
        if (invoiceItemCount > 0) {
            result.put(INV_ITEM, invoiceItemCount);
        }
        if (quoteItemCount > 0) {
            result.put(QUOTE_ITEM, quoteItemCount);
        }
        if (opportunityItemCount > 0) {
            result.put(OPPORTUNITY_ITEM, opportunityItemCount);
        }
        return result;
    }

    @Override
    public EdsUnitMeasurement getByName(String name) {
        return (EdsUnitMeasurement) findSingle("SELECT m FROM EdsUnitMeasurement m WHERE (deleted is null or deleted <> true) and lower(trim(m.name)) = ?", name.toLowerCase());
    }

    @Override
    public Map<String, EdsUnitMeasurement> getAsMap() {
        List<EdsUnitMeasurement> unitMeasurements = getUnitMeasurements(null, null);
        return unitMeasurements.stream().collect(Collectors.toMap(edsUnitMeasurement -> edsUnitMeasurement.getName().toLowerCase(), edsUnitMeasurement -> edsUnitMeasurement, (a, b) -> b));
    }

}
