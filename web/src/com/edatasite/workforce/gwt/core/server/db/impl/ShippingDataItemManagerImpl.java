package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataItemManager;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * User: Murad Satimov
 * Date: 1/9/18 8:28 PM
 */
@Repository
public class ShippingDataItemManagerImpl extends BaseManager<EdsShippingDataItem> implements ShippingDataItemManager {

    public ShippingDataItemManagerImpl() {
        super(EdsShippingDataItem.class);
    }

    @Override
    public List<EdsShippingDataItem> findByShippingDataId(Integer shippingDataId) {
        if (shippingDataId == null) {
            return Collections.emptyList();
        }
        final String sql = "select sdi from EdsShippingDataItem sdi " +
                           "    where sdi.shippingDataId = :shippingDataId" +
                           "        and (sdi.deleted is null or sdi.deleted <> true )";

        return this.slaveEntityManager.createQuery(sql, EdsShippingDataItem.class)
                                 .setParameter("shippingDataId", shippingDataId)
                                 .getResultList();
    }

    @Override
    public List<String> getGDNShippingLabels(EdsShippingDataItem edsShippingDataItem) {
        String companyId = getCompanyId();

        if (edsShippingDataItem.getQuoteItem() == null || edsShippingDataItem.getQuoteItem().getItem() == null) {
            return null;
        }
        Integer itemId = edsShippingDataItem.getQuoteItem().getItem().getObjectID();

        final String sql = "select sd.shippinglabel from " + companyId + ".transaction tr " +
                           "    left join " + companyId + ".shipping_data sd on tr.shippingdataid=sd.id " +
                           "    left join " + companyId + ".shipping_data_items sdi on sdi.shippingdataid=sd.id " +
                           "    left join " + companyId + ".item_stock st on st.transactionid=tr.id " +
                           "    where st.item_id=" + itemId +
                           "        and st.transaction_code='IN' " +
                           "        and st.sorder in (" +
                           "                select st2.sorder from " + companyId + ".transaction tr2 " +
                           "                    left join " + companyId + ".shipping_data sd2 on tr2.shippingdataid=sd2.id " +
                           "                    left join " + companyId + ".shipping_data_items sdi2 on sdi2.shippingdataid=sd2.id " +
                           "                    left join " + companyId + ".item_stock st2 on st2.transactionid=tr2.id " +
                           "                    where sdi2.id=" + edsShippingDataItem.getObjectID() +
                           "                        and st2.item_id=" + itemId +
                           "                        and st2.transaction_code = 'OUT')";

        return slaveEntityManager.createNativeQuery(sql)
                            .getResultList();
    }

    @Override
    public Map<Integer, String> getGDNShippingLabelsBySdiIds(List<Integer> sdiIds) {
        if (sdiIds == null || sdiIds.isEmpty()) return Collections.emptyMap();

        String schema = getCompanyId();
        String sql =
                "select sdi2.id as sdi_id, " +
                        "       string_agg(distinct sd.shippinglabel, ', ' order by sd.shippinglabel) as labels " +
                        "from " + schema + ".item_stock st_in " +
                        "join " + schema + ".transaction tr_in on tr_in.id = st_in.transactionid " +
                        "join " + schema + ".shipping_data sd on sd.id = tr_in.shippingdataid " +
                        "join " + schema + ".item_stock st_out " +
                        "  on st_out.sorder = st_in.sorder " +
                        " and st_out.item_id = st_in.item_id " +
                        " and st_out.transaction_code = 'OUT' " +
                        "join " + schema + ".transaction tr_out on tr_out.id = st_out.transactionid " +
                        "join " + schema + ".shipping_data sd2 on sd2.id = tr_out.shippingdataid " +
                        "join " + schema + ".shipping_data_items sdi2 on sdi2.shippingdataid = sd2.id " +
                        "where st_in.transaction_code = 'IN' " +
                        "  and sdi2.id in (:sdiIds) " +
                        "group by sdi2.id";

        @SuppressWarnings("unchecked")
        List<Object[]> rows = slaveEntityManager
                .createNativeQuery(sql)
                .setParameter("sdiIds", sdiIds)
                .getResultList();

        Map<Integer, String> out = new HashMap<>(rows.size() * 2);
        for (Object[] row : rows) {
            out.put(((Number) row[0]).intValue(), (String) row[1]);
        }
        return out;
    }

}