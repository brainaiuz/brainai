package com.edatasite.workforce.gwt.core.server.zatca.constants;

//The invoice transaction code (KSA-2) must exist and respect the following structure: NNPNESB where NN (positions 1 and 2)
public enum InvoiceTypeCodeEnum {
    TAX_INVOICE("388", "1", "0100000"),
    DEBIT_NOTE("383", "1", "0100000"),
    CREDIT_NOTE("381", "1", "0100000");
    private String typeCode;
    private String subTypeCode;
    private String typeName;

    InvoiceTypeCodeEnum(String typeCode, String subTypeCode, String typeName) {
        this.typeCode = typeCode;
        this.subTypeCode = subTypeCode;
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getSubTypeCode() {
        return subTypeCode;
    }

    public void setSubTypeCode(String subTypeCode) {
        this.subTypeCode = subTypeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

//
//    NOTE on UN/EDIFACT code list 1001 compliance:
//        ● For Simplified Tax Invoice, code is 388 and subtype is 02. ex. <cbc:InvoiceTypeCode name=”020000”>388</cbc:InvoiceTypeCode>
//        ● For simplified debit note, code is 383 and subtype is 02. ex. <cbc:InvoiceTypeCode name=”020000”>383</cbc:InvoiceTypeCode>
//        ● For simplified credit note, code is 381 and subtype is 02. ex. <cbc:InvoiceTypeCode name=”020000”>381</cbc:InvoiceTypeCode>


//        ● For Tax Invoice, code is 388 and subtype is 01. ex. <cbc:InvoiceTypeCode name=”010000”>388</cbc:InvoiceTypeCode>
//        ● For tax invoice debit note, code is 383 and subtype is 01. ex. <cbc:InvoiceTypeCode name=”010000”>383</cbc:InvoiceTypeCode>
//        ● For tax invoice credit note, code is 381 and subtype is 01. ex. <cbc:InvoiceTypeCode name=”010000”>381</cbc:InvoiceTypeCode>
