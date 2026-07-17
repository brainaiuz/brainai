package com.edatasite.workforce.gwt.core.server.zatca.exception;

public enum ExceptionConstants {
    XML_TYPE("xmlType", "3001"),
    COUNTRY_CODE("countryCode", "3002"),
    ZIP_CODE("zipCode", "3003"),
    STREET_NAME("streetName", "3004"),
    PLOT_IDENTIFICATION("plotIdentification", "3005"), // must 4 digits,
    BUILDING_NUMBER("buildingNumber", "3006"),
    CITY_SUBDIVISION_NAME("citySubdivisionName", "3007"),
    COUNTRY_SUBENTITY("countrySubentity", "3008"),
    POSTAL_ZONE("postalZone", "3009"),
    STATE_NAME("stateName", "3010");
    private final String messageCode;
    private final String errorCode;

    ExceptionConstants(String messageCode, String errorCode) {
        this.messageCode = messageCode;
        this.errorCode = errorCode;
    }

    public String getMessageCode() {
        return messageCode;
    }


    public String getErrorCode() {
        return errorCode;
    }

}
