package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategoryPicture;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryPictureManager;
import com.edatasite.workforce.gwt.core.server.db.impl.UploadManagerImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 6:58:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("productCategoryPictureManager")
public class ProductCategoryPictureManagerImpl extends UploadManagerImpl<EdsProductCategoryPicture>
        implements ProductCategoryPictureManager {

    public List<EdsProductCategoryPicture> getCategoryPictures(EdsProductCategory category, Integer fileSizeType) {
        return find("SELECT pp FROM EdsProductCategoryPicture pp WHERE pp.category=? AND pp.fileSizeType=? ORDER BY pp.objectID desc", category, fileSizeType);
    }

    public List<EdsProductCategoryPicture> getProductSubPictures(Integer productId) {
        return find("SELECT pp FROM EdsProductCategoryPicture pp WHERE pp.parentId=?", productId);
    }

    public List<Integer> getCategorySubPictures2(Integer productId) {
        return find("SELECT pp.objectID FROM EdsProductCategoryPicture pp WHERE pp.parentId=?", productId);
    }

    public ProductCategoryPictureManagerImpl() {
        super(EdsProductCategoryPicture.class);
    }

}