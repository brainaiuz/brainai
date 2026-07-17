package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsDepreciation;
import com.edatasite.workforce.gwt.core.server.db.accounting.DepreciationManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/11/11
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("depreciationManager")
public class DepreciationManagerImpl extends BaseManager<EdsDepreciation> implements DepreciationManager{
    public DepreciationManagerImpl() {
        super(EdsDepreciation.class);
    }

    @Override
    public List<String> getPostedDepreciations(Integer fixedAssetID) {
        return (List<String>)find("select to_char(month, 'yyyy-MM') from EdsDepreciation d where (d.deleted is false or d.deleted is null) and d.fixedAsset.objectID = ?", fixedAssetID);
    }

    @Override
    public List<Integer> getDepreciationPostedAssets() {
        return (List<Integer>) find("select d.fixedAsset.objectID from EdsDepreciation d where (d.deleted is false or d.deleted is null) group by d.fixedAsset.objectID");
    }

    @Override
    public BigDecimal getFixedAssetTotalDepreciatedAmount(Integer fixedAssetID) {
        BigDecimal totalDepreciatedAmount = (BigDecimal) findSingle("select sum(d.amount) from EdsDepreciation d where (d.deleted is false or d.deleted is null) and d.fixedAsset.objectID = ?", fixedAssetID);
        return totalDepreciatedAmount != null ? totalDepreciatedAmount : BigDecimal.ZERO;
    }

    @Override
    public List<EdsDepreciation> getDepreciationsByFixedAsset(Integer fixedAssetID) {
        return (List<EdsDepreciation>)find("select d from EdsDepreciation d where (d.deleted is false or d.deleted is null) and d.fixedAsset.objectID = ?", fixedAssetID);
    }

    @Override
    public List<Integer> getDepreciationsByFixedAsset(Integer fixedAssetID, Date disposalDate) {
        return (List<Integer>) find("select d.objectID from EdsDepreciation d where (d.deleted is false or d.deleted is null) and d.fixedAsset.objectID = ? and d.month > ? ", fixedAssetID, disposalDate);
    }

    @Override
    public EdsDepreciation getDepreciationByMonth(String month, Integer fixedAssetID) {
        return (EdsDepreciation)findSingle("select d from EdsDepreciation d where (d.deleted is false or d.deleted is null) and d.fixedAsset.objectID = ? and to_char(month, 'yyyy-MM') = ?", fixedAssetID, month);
    }

    @Override
    public List<EdsDepreciation> getDepreciationsForDispose(Date disposeDate, Integer fixedAssetID) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return find("select d from EdsDepreciation d where (d.deleted is false or d.deleted is null) and d.fixedAsset.objectID = ? and to_char(month, 'yyyy-MM-dd') >= '"+dateFormat.format(disposeDate) + "' ", fixedAssetID);
    }
}
