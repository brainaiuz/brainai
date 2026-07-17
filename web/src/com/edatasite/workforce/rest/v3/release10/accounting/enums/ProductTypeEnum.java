package com.edatasite.workforce.rest.v3.release10.accounting.enums;

public enum ProductTypeEnum {
    INVENTORY_ITEM(1),
    NON_INVENTORY_ITEM(2),
    SERVICE(3),
    ASSEMBLY_ITEM(4),
    OTHER_CHARGE(5),
    PRODUCT_KIT(6);

    ProductTypeEnum(Integer id) {
        this.id = id;
    }

    private Integer id;

    public Integer getId() {
        return id;
    }
}
