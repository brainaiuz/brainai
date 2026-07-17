package com.edatasite.workforce.rest.v2.release10.core.to.crm.shopify;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;

/**
 * Created by Anvar Akramov 07/03/2019.
 */
public class ShopifyCustomerTO extends ResponseData {

    private Integer id;
    private String name;
    private String first_name;
    private String last_name;
    private String avatar_image;
    private CrmAccountTO company;
    private String phone;
    private String email;

    public ShopifyCustomerTO() {
    }

    public CrmAccountTO getCompany() {
        return company;
    }

    public void setCompany(CrmAccountTO company) {
        this.company = company;
    }

}
