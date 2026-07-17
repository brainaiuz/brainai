package com.workforcetrack.mobile.rpc.attachment;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;

import javax.xml.bind.annotation.XmlRootElement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 19.04.12
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MFileResource {

    private Integer objectID;
    private String name;
    private String description;
    private String contentType;

    private String fileSize;
    private String url;

    private Integer fileType;

    public MFileResource() {
    }

    public MFileResource(FileResource fileResource) {
        this.objectID = fileResource.getObjectId();
        this.name = fileResource.getEncodedName();
        this.description = fileResource.getDescription();
        this.contentType = fileResource.getContentType();
        this.fileSize = (fileResource.getContentLength() != null) ? MFileResource.getFileSizeAsString(fileResource.getContentLength()) : "0 B";
        this.url = fileResource.getDownloadUrl();
    }

    public FileResource convert(FileResource fileResource) {
        if (fileResource == null) {
            fileResource = new FileResource();
        }

        fileResource.setObjectId(getObjectID());
        fileResource.setName(getName());
        fileResource.setDescription(getDescription());

        return fileResource;
    }

    public static String getFileSizeAsString(long size) {
        if (size < 1024) {
            return String.valueOf(size) + " B";
        } else if (size < 1024 * 1024) {
            return getSize(size, 1024D) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return getSize(size, (1024D * 1024D)) + " MB";
        }
        return getSize(size, (1024D * 1024D * 1024D)) + " GB";
    }

    private static String getSize(Long size, Double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat numberFormat = new DecimalFormat("#0.0");
        return numberFormat.format(res);
    }


    public static List<MFileResource> convertToMobile(ArrayList<FileResource> fileResources) {
        if (fileResources != null && fileResources.size() > 0) {
            List<MFileResource> items = new ArrayList<>();
            for (FileResource fileResource : fileResources) {
                items.add(new MFileResource(fileResource));
            }
            return items;
        }
        return null;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }
}
