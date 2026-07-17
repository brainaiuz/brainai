package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductPictureManager;
import com.edatasite.workforce.gwt.core.server.db.impl.UploadManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 18, 2010
 * Time: 1:55:26 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("productPictureManager")
public class ProductPictureManagerImpl extends UploadManagerImpl<EdsProductPicture> implements ProductPictureManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;


    public ProductPictureManagerImpl() {
        super(EdsProductPicture.class);
    }

    public List<EdsProductPicture> getProductPictures(EdsItem product, Integer fileSizeType) {
        if (product == null) {
            return find("SELECT pp FROM EdsProductPicture pp WHERE pp.product is null AND pp.fileSizeType=? and (deleted <> true or deleted is null) ORDER BY pp.objectID desc", fileSizeType);
        } else if (fileSizeType == null) {
            return find("SELECT pp FROM EdsProductPicture pp WHERE pp.product=? and (deleted <> true or deleted is null) ORDER BY pp.objectID desc", product);
        }
        return find("SELECT pp FROM EdsProductPicture pp WHERE pp.product=? AND pp.fileSizeType=? and (deleted <> true or deleted is null) ORDER BY pp.objectID desc", product, fileSizeType);
    }


    public EdsProductPicture getProductDefaultPicture(EdsItem product) {
        if (product == null) {
            return null;
        }
        return (EdsProductPicture) findSingle("SELECT pp FROM EdsProductPicture pp WHERE pp.defaultPicture is true and pp.product =? and (deleted <> true or deleted is null)", product);

    }

    public Long getProductPictureCount(EdsItem product) {
        return (Long) findSingle("SELECT count(pp.objectID) FROM EdsProductPicture pp WHERE pp.product=? AND pp.parentId is NULL", product);
    }

    public List<EdsProductPicture> getProductPictures(EdsItem product) {
        return find("SELECT pp FROM EdsProductPicture pp WHERE pp.product=? AND pp.parentId is NULL ORDER BY pp.objectID desc", product);
    }

    public List<EdsProductPicture> getProductChildPictures(EdsItem product, Integer parentID) {
        return find("SELECT pp FROM EdsProductPicture pp WHERE pp.product=? AND pp.parentId =? ORDER BY pp.objectID desc", product, parentID);
    }

    public List<EdsProductPicture> getProductSubPictures(Integer parentId) {
        return find("SELECT pp FROM EdsProductPicture pp WHERE pp.parentId=?", parentId);
    }

    public List<Integer> getProductSubPictures2(Integer parentId) {
        return find("SELECT pp.objectID FROM EdsProductPicture pp WHERE pp.parentId=?", parentId);
    }

    @Override
    public HashMap<Integer, Integer> getProductPicturesForListing(String ids, Integer fileSizeType) {
        StringBuilder sql = new StringBuilder();
        HashMap<Integer, Integer> result = new HashMap<>();
        sql.append("SELECT\n")
                .append("  pp.productid as id,\n")
                .append("  pp.id as pid\n")
                .append("FROM ").append(getCompanyId()).append(".productpicture pp\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".upload u ON u.id = pp.id\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".reference utype ON utype.id = u.typeid\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".item i ON i.id = pp.productid\n")
                .append("  WHERE ").append(ServerUtils.checkForDeleted("pp.isdeleted")).append(" AND ")
                .append("  pp.defaultpicture IS TRUE AND pp.filesizetype = \n").append(fileSizeType)
                .append("  and pp.productid in(").append(ids).append(")");
        List<Map<String, Object>> queryResult = jdbcSpringManager.getSimpleJdbcTemplate().queryForList(sql.toString(), new HashMap<String, String>());
        for (Map<String, Object> map : queryResult) {
            for (String key : map.keySet()) {
                result.put((Integer) map.get("id"), (Integer) map.get("pid"));
            }
        }
        return result;
    }

    @Override
    public Integer getDefaultProductPictureByFileSizeType(Integer itemId, Integer fileSizeType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT\n")
                .append("  pp.productid\n")
                .append("FROM ").append(getCompanyId()).append(".productpicture pp\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".upload u ON u.id = pp.id\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".reference utype ON utype.id = u.typeid\n")
                .append("  LEFT JOIN ").append(getCompanyId()).append(".item i ON i.id = pp.productid\n")
                .append("WHERE ").append(ServerUtils.checkForDeleted("pp.isdeleted")).append(" AND ")
                .append("pp.defaultpicture IS TRUE AND utype.code = 'AMAZON' AND pp.filesizetype = \n").append(fileSizeType)
                .append(" and pp.productid=").append(itemId);

        return (Integer) findNativeSingle(sql.toString());
    }

    @Override
    public void createBlank(EdsProductPicture productPicture) {
        super.createBlank(productPicture);
    }

    public void deleteButKeepFile(EdsProductPicture productPicture) {
        if(productPicture != null) {
            update("update EdsProductPicture set deleted = true, lastUpdateTime = ? where id = ?", new Date(), productPicture.getObjectID());
        }
    }

    public List<EdsProductPicture> getProductPicturesForSync(Integer fileSizeType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + getCompanyId() + ".productpicture pp ");
        sql.append("LEFT JOIN " + getCompanyId() + ".upload u ON u.id=pp.id ");
        sql.append("LEFT JOIN " + getCompanyId() + ".productpicture sp ON sp.id=pp.parentid ");
        sql.append("LEFT JOIN " + getCompanyId() + ".item i ON i.id=pp.productid ");
        sql.append("WHERE i.magentoEntityID IS NOT NULL ");
        sql.append("AND pp.fileSizeType = ").append(fileSizeType);
        sql.append(" AND pp.lastSyncTime IS NULL OR pp.lastSyncTime < sp.lastUpdateTime ");
        sql.append("ORDER BY sp.isdeleted desc, sp.defaultPicture asc");

        return findNative(sql.toString(), EdsProductPicture.class);
    }

    @Override
    public List<EdsProductPicture> getProductPicturesForReset(Integer fileSizeType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM " + getCompanyId() + ".productpicture pp ");
        sql.append("LEFT JOIN " + getCompanyId() + ".upload u ON u.id=pp.id ");
        sql.append("LEFT JOIN " + getCompanyId() + ".productpicture sp ON sp.id=pp.parentid ");
        sql.append("LEFT JOIN " + getCompanyId() + ".item i ON i.id=pp.productid ");
        sql.append("WHERE i.magentoEntityID IS NOT NULL ");
        sql.append("AND pp.fileSizeType = ").append(fileSizeType);
        sql.append(" AND pp.lastSyncTime IS NOT NULL ");
        sql.append("ORDER BY sp.isdeleted desc, sp.defaultPicture asc");

        return findNative(sql.toString(), EdsProductPicture.class);
    }

    @Override
    public void updateProductPicturesAfterReset() {
        updateNative("UPDATE " + getCompanyId() + ".productpicture set lastsynctime = null, magentofile = null");
    }

    @Override
    public Map<Integer, List<EdsProductPicture>> getProductPicturesByItemIds(Set<Integer> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> params = new HashMap<>();
        params.put("ids", itemIds);
        params.put("fileSizeType", 0);

        Map<Integer, List<EdsProductPicture>> result = new HashMap<>();

        List<Object[]> rows = findByNamedParams(
                "SELECT pp.product.objectID, pp " +
                        "FROM EdsProductPicture pp " +
                        "WHERE pp.product.objectID IN (:ids) " +
                        "  AND pp.fileSizeType = :fileSizeType " +
                        "  AND (pp.deleted <> true OR pp.deleted IS NULL) " +
                        "ORDER BY pp.objectID DESC",
                params
        );

        for (Object[] row : rows) {
            Integer itemId = (Integer) row[0];
            EdsProductPicture picture = (EdsProductPicture) row[1];

            result.computeIfAbsent(itemId, k -> new ArrayList<>()).add(picture);
        }

        return result;
    }
}
