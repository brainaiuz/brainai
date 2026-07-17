package com.finnetlimited.reportservice.core.server.telegram.recurrence.utils;

public class FileMessage {
    private final byte[] fileInBytes;
    private String caption;
    private String name;

    public FileMessage(byte[] fileInBytes) {
        this.fileInBytes = fileInBytes;
    }

    public byte[] getFileInBytes() {
        return fileInBytes;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String fileName) {
        this.name = fileName;
    }
}
