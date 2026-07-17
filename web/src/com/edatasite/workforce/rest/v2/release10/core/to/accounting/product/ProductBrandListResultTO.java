package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class ProductBrandListResultTO extends ResponseData {
    private ArrayList<ProductBrandTO> brands;

    public ProductBrandListResultTO() {
    }

    public ProductBrandListResultTO(ArrayList<ProductBrandTO> brands) {
        this.brands = brands;
    }

    public ArrayList<ProductBrandTO> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<ProductBrandTO> brands) {
        this.brands = brands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductBrandListResultTO)) return false;

        ProductBrandListResultTO that = (ProductBrandListResultTO) o;

        if (brands != null ? !brands.equals(that.brands) : that.brands != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return brands != null ? brands.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ProductBrandListResultTO{" +
                "brands=" + brands +
                '}';
    }
}
