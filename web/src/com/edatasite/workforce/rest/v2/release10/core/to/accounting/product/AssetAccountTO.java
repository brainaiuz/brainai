package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class AssetAccountTO extends ResponseData {
    private Integer asset_account_id;
    private String asset_account_name;
    private String asset_account_code;

    public AssetAccountTO() {
    }

    public Integer getAsset_account_id() {
        return asset_account_id;
    }

    public void setAsset_account_id(Integer asset_account_id) {
        this.asset_account_id = asset_account_id;
    }

    public String getAsset_account_name() {
        return asset_account_name;
    }

    public void setAsset_account_name(String asset_account_name) {
        this.asset_account_name = asset_account_name;
    }

    public String getAsset_account_code() {
        return asset_account_code;
    }

    public void setAsset_account_code(String asset_account_code) {
        this.asset_account_code = asset_account_code;
    }
}
