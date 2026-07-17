package com.edatasite.workforce.shopify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopifyProduct {

    Long id;// 8623222913,
    String title;//"3 Point Chin Strap Ruby Red",
    String body_html;//"<p>Roscoe Medical 3 Point Chin Strap, Ruby Red.</p>\n<p>Item: ROS-T09</p>",
    String vendor;//"Roscoe Medical, Inc.",
    String product_type;//"Medical",
    String created_at;//"2017-01-03T21:15:29-06:00",
    String handle;//"3-point-chin-strap-ruby-red",
    String updated_at;//"2019-05-11T07:21:44-05:00",
    String published_at;// null,
    String template_suffix;// null,
    String tags;//"medical supplies",
    String published_scope;//"global",
    String admin_graphql_api_id;//"gid://shopify/Product/8623222913"
    List<ShopifyProductVariant> variants;

    public ShopifyProduct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody_html() {
        return body_html;
    }

    public void setBody_html(String body_html) {
        this.body_html = body_html;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getTemplate_suffix() {
        return template_suffix;
    }

    public void setTemplate_suffix(String template_suffix) {
        this.template_suffix = template_suffix;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPublished_scope() {
        return published_scope;
    }

    public void setPublished_scope(String published_scope) {
        this.published_scope = published_scope;
    }

    public String getAdmin_graphql_api_id() {
        return admin_graphql_api_id;
    }

    public void setAdmin_graphql_api_id(String admin_graphql_api_id) {
        this.admin_graphql_api_id = admin_graphql_api_id;
    }

    public List<ShopifyProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ShopifyProductVariant> variants) {
        this.variants = variants;
    }
}
