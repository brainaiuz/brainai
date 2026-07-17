package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Audio extends Office365BaseResource {
    private String album;
    private String albumArtist;
    private String artist;
    private Integer bitrate;
    private String composers;
    private String copyright;
    private Integer disc;
    private Integer discCount;
    private Integer duration;
    private String genre;
    private Boolean hasDrm;
    private Boolean isVariableBitrate;
    private String title;
    private Integer track;
    private Integer trackCount;
    private Integer year;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/drive.htm
     */
    public Office365Audio() {
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getComposers() {
        return composers;
    }

    public void setComposers(String composers) {
        this.composers = composers;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getDisc() {
        return disc;
    }

    public void setDisc(Integer disc) {
        this.disc = disc;
    }

    public Integer getDiscCount() {
        return discCount;
    }

    public void setDiscCount(Integer discCount) {
        this.discCount = discCount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Boolean getHasDrm() {
        return hasDrm;
    }

    public void setHasDrm(Boolean hasDrm) {
        this.hasDrm = hasDrm;
    }

    public Boolean getVariableBitrate() {
        return isVariableBitrate;
    }

    public void setVariableBitrate(Boolean variableBitrate) {
        isVariableBitrate = variableBitrate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
