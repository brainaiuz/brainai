package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 18, 2010
 * Time: 1:34:43 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductPictureManager extends Manager<EdsProductPicture> {
    List<EdsProductPicture> getProductPictures(EdsItem product, Integer fileSizeType);

    Long getProductPictureCount(EdsItem product);

    List<EdsProductPicture> getProductPictures(EdsItem product);

    List<EdsProductPicture> getProductChildPictures(EdsItem product, Integer parentID);

    List<EdsProductPicture> getProductSubPictures(Integer parentId);

    EdsUser getUser();

    List<Integer> getProductSubPictures2(Integer parentId);

    HashMap<Integer, Integer> getProductPicturesForListing(String ids, Integer fileSizeType);

    Integer getDefaultProductPictureByFileSizeType(Integer itemId, Integer fileSizeType);

    void createBlank(EdsProductPicture productPicture);

    void deleteButKeepFile(EdsProductPicture productPicture);

    List<EdsProductPicture> getProductPicturesForSync(Integer fileSizeType);

    List<EdsProductPicture> getProductPicturesForReset(Integer fileSizeType);

    void updateProductPicturesAfterReset();

    EdsProductPicture getProductDefaultPicture(EdsItem product);

    Map<Integer, List<EdsProductPicture>> getProductPicturesByItemIds(Set<Integer> itemIds);
}