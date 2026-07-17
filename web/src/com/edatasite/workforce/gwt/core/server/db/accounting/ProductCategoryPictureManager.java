package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategoryPicture;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 6:56:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductCategoryPictureManager extends Manager<EdsProductCategoryPicture> {
    List<EdsProductCategoryPicture> getCategoryPictures(EdsProductCategory category, Integer fileSizeType);
    List<EdsProductCategoryPicture> getProductSubPictures(Integer categoryId);
    EdsUser getUser();

    List<Integer> getCategorySubPictures2(Integer pictureId);
}
