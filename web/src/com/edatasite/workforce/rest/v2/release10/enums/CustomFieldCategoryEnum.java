package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Abdurakhmonov Farrukh on 01/20/2017.
 */
public enum CustomFieldCategoryEnum {
    TEXT_INPUT("TEXT_INPUT"),
    DATE("DATE"),
    NUMBER_INPUT("NUMBER_INPUT"),
    FILE_UPLOAD("FILE_UPLOAD"),
    CATEGORY_CHOOSE("CATEGORY_CHOOSE"),
    MULTIPLY_CHOOSE("MULTIPLY_CHOOSE");

    private String category;

    CustomFieldCategoryEnum(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public static CustomFieldCategoryEnum getCategory(String category) {
        if (category == null) {
            return null;
        }
        try {
            return CustomFieldCategoryEnum.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
