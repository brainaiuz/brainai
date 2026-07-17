package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsItemSerial;
import com.edatasite.workforce.core.domain.accounting.EdsItemSerialDetail;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemSerialManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.SerialItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("itemSerialManager")
public class ItemSerialManagerImpl extends BaseManager<EdsItemSerial> implements ItemSerialManager {
    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public ItemSerialManagerImpl() {
        super(EdsItemSerial.class);
    }

    public EdsItemSerial getSerial(Integer itemID, String serial) {
        return (EdsItemSerial) findSingle("SELECT s FROM EdsItemSerial s WHERE s.item.objectID = ? AND s.serial = ?", itemID, serial);
    }

    public ArrayList<String> getExistingSerials(Integer itemID, List<String> serials) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemID", itemID);
        map.put("serials", serials);
        return (ArrayList<String>) findByNamedParams("SELECT s.serial FROM EdsItemSerial s WHERE s.item.objectID = :itemID AND s.serial IN :serials ", map);
    }

    public List<SerialItem> getList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT s.id as id, s.serial as number, s.used as used FROM " + getCompanyId() + ".item_serial s ");
        sql.append(" WHERE s.item_id = ").append(fp.getProductId());
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(s.serial) LIKE '").append(fp.getSqlSearchKey() + "')");
        }

        if (fp.getSortField() != null) {
            sql.append(" ORDER BY ");
            if ("number".equals(fp.getSortField())) {
                sql.append(" s.serial ");
            }
            if (fp.getSortDir() != null && fp.getSortDir() == 2) {
                sql.append(" DESC ");
            }
        } else {
            sql.append(" ORDER BY s.id DESC ");
        }

        if (fp.getLimit() > 0) {
            sql.append(" OFFSET " + fp.getStart() + " LIMIT " + fp.getLimit() + " ");
        }
        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SerialItem.class));
    }

    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT count(s) FROM " + getCompanyId() + ".item_serial s ");
        sql.append(" WHERE s.item_id = ").append(fp.getProductId());

        return ((BigInteger) findNativeSingle(sql.toString())).intValue();
    }

    public List<SelectItem> getAvailableSerials(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("SELECT NEW com.edatasite.workforce.gwt.core.client.rpc.SelectItem(s.objectID, s.serial) FROM EdsItemSerialDetail sd");
        sql.append(" LEFT JOIN sd.serial s");
        sql.append(" WHERE s.item.objectID = ").append(fp.getItemId());
        sql.append(" AND s.warehouse.objectID = ").append(fp.getWarehouseID());
        if (fp.getEntityID() != null) {
            if (fp.getEntityID() > 0) {
                sql.append(" AND sd.entityId = ").append(fp.getEntityID());
            } else {
                if (Constants.RECEIVABLE_CREDIT_NOTE.equals(fp.getRelationType())) {
                    sql.append(" AND s.used = true");
                } else if (Constants.RECEIVABLE_CREDIT_NOTE.equals(fp.getRelationType())) {
                    sql.append(" AND (s.used <> true or s.used is null)");
                }
            }
        } else {
            sql.append(" AND (s.used <> true or s.used is null)");
        }

        if (fp.getObjectIDs() != null && fp.getObjectIDs().size() > 0) {
            sql.append(" AND s.objectID not in (").append(ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0", ",")).append(")");
        }

        if (fp.getSearchKey() != null) {
            sql.append("  AND lower(s.serial) like lower('%").append(fp.getSearchKey()).append("%') ");
        }

        return find(sql.toString());
    }

    public List<EdsItemSerial> getSerials(Integer entityId, String entityType) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT s FROM EdsItemSerialDetail sd");
        sql.append(" LEFT JOIN sd.serial s");
        sql.append(" WHERE sd.entityId = ").append(entityId);
        sql.append(" AND sd.entityType = '").append(entityType).append("'");
        return find(sql.toString());
    }

    public List<EdsItemSerialDetail> getSerialDetails(Integer entitiyId, String entityType) {
        return find("SELECT DISTINCT sd FROM EdsItemSerialDetail sd WHERE sd.entityId = ? AND sd.entityType = ?", entitiyId, entityType);
    }

    public EdsItemSerialDetail getSerialDetail(String serial, Integer entityId, String entityType) {
        return (EdsItemSerialDetail) findSingle("SELECT sd FROM EdsItemSerialDetail sd WHERE sd.serial.serial = ? AND sd.entityId = ? AND sd.entityType = ?", serial, entityId, entityType);
    }

    public void deleteSerialDetail(Integer serialDetailId) {
        update("DELETE FROM EdsItemSerialDetail sd WHERE sd.objectID = ? ", serialDetailId);
    }

    public void deleteSerials(Integer itemID) {
        update("DELETE FROM EdsItemSerial s where s.item.objectID = ?", itemID);
    }
}
