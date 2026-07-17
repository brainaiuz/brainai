package com.edatasite.workforce.gwt.core.client.ui.enums;

/**
 * Created by Dilshod Madrahimov on 5/8/15 5:31 PM
 */
public enum FileUploadType {
    /**
     * All uploaded attachments will be stored in Amazon.
     */
    AMAZON,
    /**
     * All uploaded attachments will be stored in Google Documents.
     */
    GOOGLE_DOCUMENTS,
    /**
     * Link to existing Google Documents.
     */
    LINK_TO_GOOGLE_DOCUMENTS,
    /**
     * Link to existing KPI Documents.
     */
    LINK_TO_KPI_DOCUMENTS,
    /**
     * All uploaded attachments will be stored in Google Documents.
     */
    OFFICE_DOCUMENTS,

//        OFFICE_ONE_DRIVE_DOCUMENTS,

    OFFICE_SHARE_POINT_DOCUMENTS,
    /*
    Link to Office 365 Documants.
    */
    LINK_TO_OFFICE_DOCUMENTS,

//        LINK_TO_OFFICE_ONE_DRIVE_DOCUMENTS,

    LINK_TO_OFFICE_SHARE_POINT_DOCUMENTS,
    /**
     * All uploaded attachments will be stored in MinIO Storage.
     */
    MINIO,
    /**
     * All uploaded attachments will be stored in Local Storage.
     */
    LOCAL

}
