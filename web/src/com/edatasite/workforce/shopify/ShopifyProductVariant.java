package com.edatasite.workforce.shopify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopifyProductVariant {
    Long id;// 29143423105,
    Long product_id;// 8623222913
    String title;//Small";
    String price;//19.00";
    String sku;//ROS-T09-S";
    Long position;// 1,
    String inventory_policy;//continue";
    String compare_at_price;// null,
    String fulfillment_service;//"wealcan-lvks";
    String inventory_management;//"shopify";
    String option1;//Small";
    String option2;// null,
    String option3;// null,
    String created_at;//2017-01-03T21:15:29-06:00";
    String updated_at;//2019-05-11T07:21:43-05:00";
    boolean taxable;// true,
    String barcode;//";
    Long grams;// 91,
    Long image_id; //null,
    BigDecimal weight;// 0.2,
    String weight_unit;//lb";
    Long inventory_item_id;// 16649050945,
    Long inventory_quantity;// 0,
    Long old_inventory_quantity;// 0,
    boolean requires_shipping;// true,
    String admin_graphql_api_id;//gid://shopify/ProductVariant/29143423105"

    String cost;//19.00";

    public ShopifyProductVariant() {
    }

    public ShopifyProductVariant(Long id, Long product_id, String title, String price, String sku, Long position, String inventory_policy, String compare_at_price, String fulfillment_service, String inventory_management, String option1, String option2, String option3, String created_at, String updated_at, boolean taxable, String barcode, Long grams, Long image_id, BigDecimal weight, String weight_unit, Long inventory_item_id, Long inventory_quantity, Long old_inventory_quantity, boolean requires_shipping, String admin_graphql_api_id, String cost) {
        this.id = id;
        this.product_id = product_id;
        this.title = title;
        this.price = price;
        this.sku = sku;
        this.position = position;
        this.inventory_policy = inventory_policy;
        this.compare_at_price = compare_at_price;
        this.fulfillment_service = fulfillment_service;
        this.inventory_management = inventory_management;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.taxable = taxable;
        this.barcode = barcode;
        this.grams = grams;
        this.image_id = image_id;
        this.weight = weight;
        this.weight_unit = weight_unit;
        this.inventory_item_id = inventory_item_id;
        this.inventory_quantity = inventory_quantity;
        this.old_inventory_quantity = old_inventory_quantity;
        this.requires_shipping = requires_shipping;
        this.admin_graphql_api_id = admin_graphql_api_id;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getInventory_policy() {
        return inventory_policy;
    }

    public void setInventory_policy(String inventory_policy) {
        this.inventory_policy = inventory_policy;
    }

    public String getCompare_at_price() {
        return compare_at_price;
    }

    public void setCompare_at_price(String compare_at_price) {
        this.compare_at_price = compare_at_price;
    }

    public String getFulfillment_service() {
        return fulfillment_service;
    }

    public void setFulfillment_service(String fulfillment_service) {
        this.fulfillment_service = fulfillment_service;
    }

    public String getInventory_management() {
        return inventory_management;
    }

    public void setInventory_management(String inventory_management) {
        this.inventory_management = inventory_management;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
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

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getGrams() {
        return grams;
    }

    public void setGrams(Long grams) {
        this.grams = grams;
    }

    public Long getImage_id() {
        return image_id;
    }

    public void setImage_id(Long image_id) {
        this.image_id = image_id;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public Long getInventory_item_id() {
        return inventory_item_id;
    }

    public void setInventory_item_id(Long inventory_item_id) {
        this.inventory_item_id = inventory_item_id;
    }

    public Long getInventory_quantity() {
        return inventory_quantity;
    }

    public void setInventory_quantity(Long inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }

    public Long getOld_inventory_quantity() {
        return old_inventory_quantity;
    }

    public void setOld_inventory_quantity(Long old_inventory_quantity) {
        this.old_inventory_quantity = old_inventory_quantity;
    }

    public boolean isRequires_shipping() {
        return requires_shipping;
    }

    public void setRequires_shipping(boolean requires_shipping) {
        this.requires_shipping = requires_shipping;
    }

    public String getAdmin_graphql_api_id() {
        return admin_graphql_api_id;
    }

    public void setAdmin_graphql_api_id(String admin_graphql_api_id) {
        this.admin_graphql_api_id = admin_graphql_api_id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopifyProductVariant that)) return false;

        if (isTaxable() != that.isTaxable()) return false;
        if (isRequires_shipping() != that.isRequires_shipping()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getProduct_id() != null ? !getProduct_id().equals(that.getProduct_id()) : that.getProduct_id() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
        if (getPrice() != null ? !getPrice().equals(that.getPrice()) : that.getPrice() != null) return false;
        if (getSku() != null ? !getSku().equals(that.getSku()) : that.getSku() != null) return false;
        if (getPosition() != null ? !getPosition().equals(that.getPosition()) : that.getPosition() != null)
            return false;
        if (getInventory_policy() != null ? !getInventory_policy().equals(that.getInventory_policy()) : that.getInventory_policy() != null)
            return false;
        if (getCompare_at_price() != null ? !getCompare_at_price().equals(that.getCompare_at_price()) : that.getCompare_at_price() != null)
            return false;
        if (getFulfillment_service() != null ? !getFulfillment_service().equals(that.getFulfillment_service()) : that.getFulfillment_service() != null)
            return false;
        if (getInventory_management() != null ? !getInventory_management().equals(that.getInventory_management()) : that.getInventory_management() != null)
            return false;
        if (getOption1() != null ? !getOption1().equals(that.getOption1()) : that.getOption1() != null) return false;
        if (getOption2() != null ? !getOption2().equals(that.getOption2()) : that.getOption2() != null) return false;
        if (getOption3() != null ? !getOption3().equals(that.getOption3()) : that.getOption3() != null) return false;
        if (getCreated_at() != null ? !getCreated_at().equals(that.getCreated_at()) : that.getCreated_at() != null)
            return false;
        if (getUpdated_at() != null ? !getUpdated_at().equals(that.getUpdated_at()) : that.getUpdated_at() != null)
            return false;
        if (getBarcode() != null ? !getBarcode().equals(that.getBarcode()) : that.getBarcode() != null) return false;
        if (getGrams() != null ? !getGrams().equals(that.getGrams()) : that.getGrams() != null) return false;
        if (getImage_id() != null ? !getImage_id().equals(that.getImage_id()) : that.getImage_id() != null)
            return false;
        if (getWeight() != null ? !getWeight().equals(that.getWeight()) : that.getWeight() != null) return false;
        if (getWeight_unit() != null ? !getWeight_unit().equals(that.getWeight_unit()) : that.getWeight_unit() != null)
            return false;
        if (getInventory_item_id() != null ? !getInventory_item_id().equals(that.getInventory_item_id()) : that.getInventory_item_id() != null)
            return false;
        if (getInventory_quantity() != null ? !getInventory_quantity().equals(that.getInventory_quantity()) : that.getInventory_quantity() != null)
            return false;
        if (getOld_inventory_quantity() != null ? !getOld_inventory_quantity().equals(that.getOld_inventory_quantity()) : that.getOld_inventory_quantity() != null)
            return false;
        if (getAdmin_graphql_api_id() != null ? !getAdmin_graphql_api_id().equals(that.getAdmin_graphql_api_id()) : that.getAdmin_graphql_api_id() != null)
            return false;
        if (getCost() != null ? !getCost().equals(that.getCost()) : that.getCost() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getProduct_id() != null ? getProduct_id().hashCode() : 0);
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        result = 31 * result + (getSku() != null ? getSku().hashCode() : 0);
        result = 31 * result + (getPosition() != null ? getPosition().hashCode() : 0);
        result = 31 * result + (getInventory_policy() != null ? getInventory_policy().hashCode() : 0);
        result = 31 * result + (getCompare_at_price() != null ? getCompare_at_price().hashCode() : 0);
        result = 31 * result + (getFulfillment_service() != null ? getFulfillment_service().hashCode() : 0);
        result = 31 * result + (getInventory_management() != null ? getInventory_management().hashCode() : 0);
        result = 31 * result + (getOption1() != null ? getOption1().hashCode() : 0);
        result = 31 * result + (getOption2() != null ? getOption2().hashCode() : 0);
        result = 31 * result + (getOption3() != null ? getOption3().hashCode() : 0);
        result = 31 * result + (getCreated_at() != null ? getCreated_at().hashCode() : 0);
        result = 31 * result + (getUpdated_at() != null ? getUpdated_at().hashCode() : 0);
        result = 31 * result + (isTaxable() ? 1 : 0);
        result = 31 * result + (getBarcode() != null ? getBarcode().hashCode() : 0);
        result = 31 * result + (getGrams() != null ? getGrams().hashCode() : 0);
        result = 31 * result + (getImage_id() != null ? getImage_id().hashCode() : 0);
        result = 31 * result + (getWeight() != null ? getWeight().hashCode() : 0);
        result = 31 * result + (getWeight_unit() != null ? getWeight_unit().hashCode() : 0);
        result = 31 * result + (getInventory_item_id() != null ? getInventory_item_id().hashCode() : 0);
        result = 31 * result + (getInventory_quantity() != null ? getInventory_quantity().hashCode() : 0);
        result = 31 * result + (getOld_inventory_quantity() != null ? getOld_inventory_quantity().hashCode() : 0);
        result = 31 * result + (isRequires_shipping() ? 1 : 0);
        result = 31 * result + (getAdmin_graphql_api_id() != null ? getAdmin_graphql_api_id().hashCode() : 0);
        result = 31 * result + (getCost() != null ? getCost().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShopifyProductVariant{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", sku='" + sku + '\'' +
                ", position=" + position +
                ", inventory_policy='" + inventory_policy + '\'' +
                ", compare_at_price='" + compare_at_price + '\'' +
                ", fulfillment_service='" + fulfillment_service + '\'' +
                ", inventory_management='" + inventory_management + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", taxable=" + taxable +
                ", barcode='" + barcode + '\'' +
                ", grams=" + grams +
                ", image_id=" + image_id +
                ", weight=" + weight +
                ", weight_unit='" + weight_unit + '\'' +
                ", inventory_item_id=" + inventory_item_id +
                ", inventory_quantity=" + inventory_quantity +
                ", old_inventory_quantity=" + old_inventory_quantity +
                ", requires_shipping=" + requires_shipping +
                ", admin_graphql_api_id='" + admin_graphql_api_id + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
