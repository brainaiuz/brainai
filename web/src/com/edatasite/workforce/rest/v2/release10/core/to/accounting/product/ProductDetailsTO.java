package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ProductCurrencyTO;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class ProductDetailsTO extends ProductItemTO {

    private TaxTO double_tax;
    private CustomerTO customer;
    private ArrayList<SupplierTO> suppliers;
    private BigDecimal comission;
    private Boolean enable_it;
    //    private Boolean show_on_opportunity;
    private Boolean purchased_from_supplier = false;
    private String subsidiary_product_unique_id;
    private Integer subsidiary_product_id;
    private ProductCurrencyTO currency;
    private WarehouseTO default_warehouse;


    public ProductDetailsTO() {
    }

    public TaxTO getDouble_tax() {
        return double_tax;
    }

    public void setDouble_tax(TaxTO double_tax) {
        this.double_tax = double_tax;
    }

    public CustomerTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerTO customer) {
        this.customer = customer;
    }

    public ArrayList<SupplierTO> getSuppliers() {
        if(suppliers==null) {
            suppliers = new ArrayList<>();
        }
        return suppliers;
    }

    public void setSuppliers(ArrayList<SupplierTO> suppliers) {
        this.suppliers = suppliers;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public Boolean getEnable_it() {
        return enable_it;
    }

    public void setEnable_it(Boolean enable_it) {
        this.enable_it = enable_it;
    }

//    public Boolean getShow_on_opportunity() {
//        return show_on_opportunity;
//    }

//    public void setShow_on_opportunity(Boolean show_on_opportunity) {
//        this.show_on_opportunity = show_on_opportunity;
//    }

    public Boolean getPurchased_from_supplier() {
        return purchased_from_supplier;
    }

    public void setPurchased_from_supplier(Boolean purchased_from_supplier) {
        this.purchased_from_supplier = purchased_from_supplier;
    }

    public String getSubsidiary_product_unique_id() {
        return subsidiary_product_unique_id;
    }

    public void setSubsidiary_product_unique_id(String subsidiary_product_unique_id) {
        this.subsidiary_product_unique_id = subsidiary_product_unique_id;
    }

    public Integer getSubsidiary_product_id() {
        return subsidiary_product_id;
    }

    public void setSubsidiary_product_id(Integer subsidiary_product_id) {
        this.subsidiary_product_id = subsidiary_product_id;
    }

    public ProductCurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(ProductCurrencyTO currency) {
        this.currency = currency;
    }

    public WarehouseTO getDefault_warehouse() {
        return default_warehouse;
    }

    public void setDefault_warehouse(WarehouseTO default_warehouse) {
        this.default_warehouse = default_warehouse;
    }

}
