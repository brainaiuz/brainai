package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsDepreciation;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/11/11
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DepreciationManager extends Manager<EdsDepreciation>{
    List<String> getPostedDepreciations(Integer fixedAssetID);
    List<Integer> getDepreciationPostedAssets();
    BigDecimal getFixedAssetTotalDepreciatedAmount(Integer fixedAssetID);
    List<EdsDepreciation> getDepreciationsByFixedAsset(Integer fixedAssetID);

    List<Integer> getDepreciationsByFixedAsset(Integer fixedAssetID, Date disposalDate);

    EdsDepreciation getDepreciationByMonth(String month, Integer fixedAssetID);

    List<EdsDepreciation> getDepreciationsForDispose(Date disposeDate, Integer fixedAssetID);
}
