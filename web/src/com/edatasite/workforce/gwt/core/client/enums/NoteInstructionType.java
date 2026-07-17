package com.edatasite.workforce.gwt.core.client.enums;

public enum NoteInstructionType {
    CANCELLATION_OF_SUPPLIES("Cancellation or suspension of the supplies after its occurrence either wholly or partially"),
    CHANGES_TO_VAT_DUE("In case of essential change or amendment in the supply, which leads to the change of the VAT due"),
    CHANGES_VALUE_OF_SUPPLY("Amendment of the supply value which is pre-agreed upon between the supplier and consumer"),
    GOOD_SERVICES_REFUND("In case of goods or services refund"),
    CHANGES_TO_SELLER_OR_BUYER_INFO("In case of change in Seller's or Buyer's information");

    private final String value;

    NoteInstructionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
