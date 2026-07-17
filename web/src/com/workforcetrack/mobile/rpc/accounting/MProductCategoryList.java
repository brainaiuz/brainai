package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/14/11
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MProductCategoryList {

    private List<MProductCategoryListItem> productCategoryItem;
    private Integer totalCount;

    public MProductCategoryList(){

    }

    public MProductCategoryList(ListResult<ProductCategoryItem> productCategoryList){
        if(productCategoryList != null){
            this.productCategoryItem = new ArrayList<>();
            for(ProductCategoryItem item : productCategoryList.getList()){
                this.productCategoryItem.add(new MProductCategoryListItem(item));
            }

            this.totalCount = productCategoryList.getTotal();
        }
    }

    public List<MProductCategoryListItem> getProductCategoryListItem() {
        return productCategoryItem;
    }

    public void setProductCategoryListItem(List<MProductCategoryListItem> productCategoryItem) {
        this.productCategoryItem = productCategoryItem;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
