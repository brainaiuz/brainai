package com.edatasite.workforce.gwt.core.server.office365.enums;

/**
 * Created by umakarimov on 9/30/15.
 */
public enum Office365DriveQuotaState {
    OK,
    Warning,
    Over;

    /**
     * @param code
     * @see https://msdn.microsoft.com/office/office365/APi/files-rest-operations#QuotaInfo
     */
    public String getCode() {
        return this.toString().toLowerCase();
    }

    public static Office365DriveQuotaState fromCode(String code) {
        for (Office365DriveQuotaState state : Office365DriveQuotaState.values()) {
            if (state.getCode().equalsIgnoreCase(code)) {
                return state;
            }
        }

        return null;
    }
}
