/*
 * This file is part of gwt-cal
 * Copyright (C) 2009  Scottsdale Software LLC
 * 
 * gwt-cal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/
 */

package com.edatasite.workforce.gwt.core.client.rpc.googlecalendar;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class Attendee implements IsSerializable {

    private Integer id;
    private boolean shared;

    private String name;
    private String email;
    private String imageUrl;
    private String status;
    private String type;
    private String googleID;
    private String officeID;

    public Attendee() {

    }

    public Attendee(Integer id, boolean shared) {
        this.id = id;
        this.shared = shared;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getID() == null || obj == null || ((Attendee) obj).getID() == null) {
            return false;
        }
        return this.getID().equals(((Attendee) obj).getID());
    }
}