package com.edatasite.workforce.shopify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopifyInventory {
    Long id;// 29143423105,
    String sku;// "7627823",
    String created_at;// "2016-08-16T12:29:28-05:00",
    String updated_at; // "2017-06-28T20:03:00-05:00",
    boolean requires_shipping;// true,
    BigDecimal cost;// null,
    String country_code_of_origin;// null,
    String province_code_of_origin;// null,
    String harmonized_system_code;// null,
    boolean tracked;// true,
    List<String> country_harmonized_system_codes;// [],
    String admin_graphql_api_id;// "gid:\/\/shopify\/InventoryItem\/11273070209"

    public ShopifyInventory() {
    }

    public ShopifyInventory(Long id, String sku, String created_at, String updated_at, boolean requires_shipping, BigDecimal cost, String country_code_of_origin, String province_code_of_origin, String harmonized_system_code, boolean tracked, List<String> country_harmonized_system_codes, String admin_graphql_api_id) {
        this.id = id;
        this.sku = sku;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.requires_shipping = requires_shipping;
        this.cost = cost;
        this.country_code_of_origin = country_code_of_origin;
        this.province_code_of_origin = province_code_of_origin;
        this.harmonized_system_code = harmonized_system_code;
        this.tracked = tracked;
        this.country_harmonized_system_codes = country_harmonized_system_codes;
        this.admin_graphql_api_id = admin_graphql_api_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isRequires_shipping() {
        return requires_shipping;
    }

    public void setRequires_shipping(boolean requires_shipping) {
        this.requires_shipping = requires_shipping;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCountry_code_of_origin() {
        return country_code_of_origin;
    }

    public void setCountry_code_of_origin(String country_code_of_origin) {
        this.country_code_of_origin = country_code_of_origin;
    }

    public String getProvince_code_of_origin() {
        return province_code_of_origin;
    }

    public void setProvince_code_of_origin(String province_code_of_origin) {
        this.province_code_of_origin = province_code_of_origin;
    }

    public String getHarmonized_system_code() {
        return harmonized_system_code;
    }

    public void setHarmonized_system_code(String harmonized_system_code) {
        this.harmonized_system_code = harmonized_system_code;
    }

    public boolean isTracked() {
        return tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    public List<String> getCountry_harmonized_system_codes() {
        return country_harmonized_system_codes;
    }

    public void setCountry_harmonized_system_codes(List<String> country_harmonized_system_codes) {
        this.country_harmonized_system_codes = country_harmonized_system_codes;
    }

    public String getAdmin_graphql_api_id() {
        return admin_graphql_api_id;
    }

    public void setAdmin_graphql_api_id(String admin_graphql_api_id) {
        this.admin_graphql_api_id = admin_graphql_api_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopifyInventory that)) return false;

        if (isRequires_shipping() != that.isRequires_shipping()) return false;
        if (isTracked() != that.isTracked()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getSku() != null ? !getSku().equals(that.getSku()) : that.getSku() != null) return false;
        if (getCreated_at() != null ? !getCreated_at().equals(that.getCreated_at()) : that.getCreated_at() != null)
            return false;
        if (getUpdated_at() != null ? !getUpdated_at().equals(that.getUpdated_at()) : that.getUpdated_at() != null)
            return false;
        if (getCost() != null ? !getCost().equals(that.getCost()) : that.getCost() != null) return false;
        if (getCountry_code_of_origin() != null ? !getCountry_code_of_origin().equals(that.getCountry_code_of_origin()) : that.getCountry_code_of_origin() != null)
            return false;
        if (getProvince_code_of_origin() != null ? !getProvince_code_of_origin().equals(that.getProvince_code_of_origin()) : that.getProvince_code_of_origin() != null)
            return false;
        if (getHarmonized_system_code() != null ? !getHarmonized_system_code().equals(that.getHarmonized_system_code()) : that.getHarmonized_system_code() != null)
            return false;
        if (getCountry_harmonized_system_codes() != null ? !getCountry_harmonized_system_codes().equals(that.getCountry_harmonized_system_codes()) : that.getCountry_harmonized_system_codes() != null)
            return false;
        if (getAdmin_graphql_api_id() != null ? !getAdmin_graphql_api_id().equals(that.getAdmin_graphql_api_id()) : that.getAdmin_graphql_api_id() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getSku() != null ? getSku().hashCode() : 0);
        result = 31 * result + (getCreated_at() != null ? getCreated_at().hashCode() : 0);
        result = 31 * result + (getUpdated_at() != null ? getUpdated_at().hashCode() : 0);
        result = 31 * result + (isRequires_shipping() ? 1 : 0);
        result = 31 * result + (getCost() != null ? getCost().hashCode() : 0);
        result = 31 * result + (getCountry_code_of_origin() != null ? getCountry_code_of_origin().hashCode() : 0);
        result = 31 * result + (getProvince_code_of_origin() != null ? getProvince_code_of_origin().hashCode() : 0);
        result = 31 * result + (getHarmonized_system_code() != null ? getHarmonized_system_code().hashCode() : 0);
        result = 31 * result + (isTracked() ? 1 : 0);
        result = 31 * result + (getCountry_harmonized_system_codes() != null ? getCountry_harmonized_system_codes().hashCode() : 0);
        result = 31 * result + (getAdmin_graphql_api_id() != null ? getAdmin_graphql_api_id().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShopifyInventory{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", requires_shipping=" + requires_shipping +
                ", cost=" + cost +
                ", country_code_of_origin='" + country_code_of_origin + '\'' +
                ", province_code_of_origin='" + province_code_of_origin + '\'' +
                ", harmonized_system_code='" + harmonized_system_code + '\'' +
                ", tracked=" + tracked +
                ", country_harmonized_system_codes=" + country_harmonized_system_codes +
                ", admin_graphql_api_id='" + admin_graphql_api_id + '\'' +
                '}';
    }
}
