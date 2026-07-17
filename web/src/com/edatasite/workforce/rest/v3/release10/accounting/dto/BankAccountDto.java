package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

import java.util.List;

public class BankAccountDto extends ItemDto {

    private List<CustomFieldDto> customFields;


    public BankAccountDto() {
    }


    public BankAccountDto(Integer id, String name, String code) {
        super(id, name, code);
    }

    public BankAccountDto(Integer id, String name, String code, List<CustomFieldDto> customFields) {
        super(id, name, code);
        this.customFields = customFields;
    }



    public List<CustomFieldDto> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomFieldDto> customFields) {
        this.customFields = customFields;
    }
}
