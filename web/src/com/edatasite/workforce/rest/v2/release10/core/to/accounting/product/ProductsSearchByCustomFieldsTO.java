package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import java.util.List;

public class ProductsSearchByCustomFieldsTO extends ProductRequestListTO {

    private List<String> search_custom_fields;

    public ProductsSearchByCustomFieldsTO() {
    }

    public ProductsSearchByCustomFieldsTO(List<String> search_custom_fields) {
        this.search_custom_fields = search_custom_fields;
    }

    public List<String> getSearch_custom_fields() {
        return search_custom_fields;
    }

    public void setSearch_custom_fields(List<String> search_custom_fields) {
        this.search_custom_fields = search_custom_fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductsSearchByCustomFieldsTO)) return false;

        ProductsSearchByCustomFieldsTO that = (ProductsSearchByCustomFieldsTO) o;

        if (search_custom_fields != null ? !search_custom_fields.equals(that.search_custom_fields) : that.search_custom_fields != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return search_custom_fields != null ? search_custom_fields.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProductsSearchByCustomFieldsTO{" +
                "search_custom_fields=" + search_custom_fields +
                '}';
    }
}
