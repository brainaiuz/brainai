package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductList;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/13/11
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "productList")
public class MProductList {

    private List<MProductListItem> productListItem;
    private Integer totalCount;

    public MProductList() {

    }

    public MProductList(ProductList productList) {
        if (productList != null) {
            this.productListItem = new ArrayList<>();
            for (ProductItem productItem : productList.getItems()) {
                this.productListItem.add(new MProductListItem(productItem));
            }
            this.totalCount = productList.getTotalCount();
        }
    }

    public MProductList(ListResult<ProductItem> productList) {
        if (productList != null) {
            this.productListItem = new ArrayList<>();
            for (ProductItem productItem : productList.getList()) {
                this.productListItem.add(new MProductListItem(productItem));
            }
            this.totalCount = productList.getTotal();
        }
    }

    public List<MProductListItem> getProductListItem() {
        return productListItem;
    }

    public void setProductListItem(List<MProductListItem> productListItem) {
        this.productListItem = productListItem;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}