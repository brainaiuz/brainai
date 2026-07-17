package com.edatasite.workforce.gwt.documents.client.rest.resource;

public class UserResource extends RestResource {

    public UserResource() {
    }

    private String name;

    private String lastName;

    private String username;

    private String email;

    private QuotaHolder quota;

    private String announcement;

    /**
     * Retrieve the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Modify the name.
     *
     * @param aName the name to set
     */
    public void setName(String aName) {
        name = aName;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieve the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Modify the username.
     *
     * @param aUsername the username to set
     */
    public void setUsername(String aUsername) {
        username = aUsername;
    }

    /**
     * Retrieve the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Modify the email.
     *
     * @param anEmail the email to set
     */
    public void setEmail(String anEmail) {
        email = anEmail;
    }

    /**
     * Retrieve the quota.
     *
     * @return the quota
     */
    public QuotaHolder getQuota() {
        return quota;
    }

    /**
     * Modify the quota.
     *
     * @param aQuota the quota to set
     */
    public void setQuota(QuotaHolder aQuota) {
        quota = aQuota;
    }


    /**
     * Retrieve the announcement.
     *
     * @return the announcement
     */
    public String getAnnouncement() {
        return announcement;
    }

    /**
     * Modify the announcement.
     *
     * @param anAnnouncement the announcement to set
     */
    public void setAnnouncement(String anAnnouncement) {
        announcement = anAnnouncement;
    }

    @Override
    public String toString() {
        return email + "\n" + name + "\n" + username;
    }

    @Override
    public String getLastModifiedSince() {
        return null;
    }
}
