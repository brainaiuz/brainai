package com.edatasite.workforce.gwt.profile.client.rpc;

public enum PdfFooteHederAttributeEnum {

    COMPANY_NAME("COMPANY_NAME", "${company_name}"),
    COMPANY_LOGO("COMPANY_LOGO", "${company_logo}"),
    COMPANY_MAIN_ADDRESS("COMPANY_MAIN_ADDRESS", "${company_main_address}"),
    POWERED_BY("POWERED_BY", "${powered_by}"),
    PAGINATION("PAGINATION", "${pagination}"),
    QR_CODE("QR_CODE", "${qr_code}"),
    PHONE_NUMBER("PHONE_NUMBER", "${phone_number}"),
    EMAIL_ID("EMAIL_ID", "${email_id}"),
    COMPANY_WEBSITE("COMPANY_WEBSITE", "${website}"),
    FAX_NUM("FAX_NUM", "${fax_num}"),
    DOCUMENT_TITLE("DOCUMENT_TITLE", "${document_title}"),
    USER_LOCATION_ADRESS("USER_LOCATION_ADRESS", "${location_address}"),
    USER_LOCATION_PHONE("USER_LOCATION_PHONE", "${location_phone}"),
    USER_LOCATION_EMAIL("USER_LOCATION_EMAIL", "${location_email}"),
    USER_LOCATION_ZIP_CODE("USER_LOCATION_ZIP_CODE", "${location_zip_code}");

    private final String attributeName;
    private final String code;

    PdfFooteHederAttributeEnum(String attributeName, String code) {
        this.attributeName = attributeName;
        this.code = code;
    }

    public static String[] getCodesAsArray() {
        String[] result = new String[PdfFooteHederAttributeEnum.values().length];
        int index = 0;
        for (PdfFooteHederAttributeEnum value : PdfFooteHederAttributeEnum.values()) {
            result[index++] = value.getCode();
        }
        return result;

    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getCode() {
        return code;
    }
}
