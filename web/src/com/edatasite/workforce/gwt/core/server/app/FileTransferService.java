package com.edatasite.workforce.gwt.core.server.app;

/**
 * User: Murad Satimov
 * Date: 8/27/17 5:11 PM
 */
public interface FileTransferService {

    Integer transferFilesLimitedToLocalStorageFromAmazon(Integer start, int limit, String uploadPath);

    String getLocalUploadDirectory();
}
