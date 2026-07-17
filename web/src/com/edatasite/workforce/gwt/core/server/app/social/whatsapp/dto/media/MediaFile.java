package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.media;

/**
 * @param fileName file name
 * @param content  file content
 */
public record MediaFile(String fileName, byte[] content) {
}
