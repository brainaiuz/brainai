package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

import java.util.Date;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Photo extends Office365BaseResource {
    private String cameraMake;
    private String cameraModel;
    private Integer exposureDenominator;
    private Integer exposureNumerator;
    private Double fNumber;
    private Double focalLength;
    private Integer iso;
    private Date takenDateTime;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/image.htm
     */
    public Office365Photo() {
    }

    public String getCameraMake() {
        return cameraMake;
    }

    public void setCameraMake(String cameraMake) {
        this.cameraMake = cameraMake;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    public Integer getExposureDenominator() {
        return exposureDenominator;
    }

    public void setExposureDenominator(Integer exposureDenominator) {
        this.exposureDenominator = exposureDenominator;
    }

    public Integer getExposureNumerator() {
        return exposureNumerator;
    }

    public void setExposureNumerator(Integer exposureNumerator) {
        this.exposureNumerator = exposureNumerator;
    }

    public Double getfNumber() {
        return fNumber;
    }

    public void setfNumber(Double fNumber) {
        this.fNumber = fNumber;
    }

    public Double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(Double focalLength) {
        this.focalLength = focalLength;
    }

    public Integer getIso() {
        return iso;
    }

    public void setIso(Integer iso) {
        this.iso = iso;
    }

    public Date getTakenDateTime() {
        return takenDateTime;
    }

    public void setTakenDateTime(Date takenDateTime) {
        this.takenDateTime = takenDateTime;
    }
}
