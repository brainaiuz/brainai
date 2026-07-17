package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentItemManager;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudgetItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sherzod on 3/15/2016.
 */
@Repository("stockAdjustmentItemManager")
public class StockAdjustmentItemManagerImpl extends BaseManager<EdsAdjustmentItem> implements StockAdjustmentItemManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public StockAdjustmentItemManagerImpl() {
        super(EdsAdjustmentItem.class);
    }

    @Override
    public List<ProjectBudgetItem> getStockAdjustmentItems(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT\n")
                .append(" sa.number                  AS name,\n")
                .append("  sum(ti.debit)    AS actualWageAmount,\n")
                .append("  'stockadjustment|summary/' || sa.id AS action\n")
                .append("FROM " + getCompanyId() + ".adjustment_item ai\n")
                .append("  JOIN " + getCompanyId() + ".transactionitem ti ON ai.id = ti.stock_adjustment_item_id AND ti.debit IS NOT NULL\n")
                .append("  JOIN " + getCompanyId() + ".transaction t ON ti.transactionid = t.id AND (t.deleted IS NULL OR t.deleted = FALSE)\n")
                .append("  JOIN " + getCompanyId() + ".item i ON ai.item_id = i.id\n")
                .append("  JOIN " + getCompanyId() + ".stock_adjustment sa on sa.id = ai.adjustment_id\n");
        if (fp.getProjectIdList() != null && fp.getProjectIdList().size() > 0) {
            sql.append("WHERE ai.projectid in (" + fp.getProjectIdList() + ")\n");
        } else {
            sql.append("WHERE ai.projectid = " + fp.getProjectId() + "\n");
        }
        sql.append(" AND ai.usedQty > 0 ");
        sql.append("GROUP BY sa.number,sa.id");

        return jdbcSpringManager.getSimJdbcOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(ProjectBudgetItem.class));
    }

    @Override
    public boolean isUsedInStockAdjustment(Integer productId) {
        List<EdsAdjustmentItem> items = find(" select ai from EdsAdjustmentItem ai left join ai.adjustment a  left join ai.item i  where a.deleted is not true and i.objectID=" + productId);

        boolean used = false;
        if (items != null && items.size() > 0) {
            used = true;
        }
        return used;
    }
}
