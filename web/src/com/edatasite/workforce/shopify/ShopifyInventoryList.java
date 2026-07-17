package com.edatasite.workforce.shopify;

import java.util.List;

public class ShopifyInventoryList {
    List<ShopifyInventory> inventory_items;

    public ShopifyInventoryList() {
    }

    public ShopifyInventoryList(List<ShopifyInventory> inventory_items) {
        this.inventory_items = inventory_items;
    }

    public List<ShopifyInventory> getInventory_items() {
        return inventory_items;
    }

    public void setInventory_items(List<ShopifyInventory> inventory_items) {
        this.inventory_items = inventory_items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopifyInventoryList that)) return false;

        if (getInventory_items() != null ? !getInventory_items().equals(that.getInventory_items()) : that.getInventory_items() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getInventory_items() != null ? getInventory_items().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ShopifyInventoryList{" +
                "inventory_items=" + inventory_items +
                '}';
    }
}
