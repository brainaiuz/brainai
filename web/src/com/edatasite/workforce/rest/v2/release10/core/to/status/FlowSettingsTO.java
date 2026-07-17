package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class FlowSettingsTO extends ResponseData {

    private Integer status_id;
    private String status_name;
    private ColorTO status_color;
    private Integer order_id;
    private String percentage;
    private Boolean is_system;
    private boolean edit_permission;
    private boolean status_permission;
    private boolean view_permission;
    private boolean commentRequired;

    public FlowSettingsTO() {
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public ColorTO getStatus_color() {
        return status_color;
    }

    public void setStatus_color(ColorTO status_color) {
        this.status_color = status_color;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Boolean getIs_system() {
        return is_system;
    }

    public void setIs_system(Boolean is_system) {
        this.is_system = is_system;
    }

    public boolean isStatus_permission() {
        return status_permission;
    }

    public void setStatus_permission(boolean status_permission) {
        this.status_permission = status_permission;
    }

    public boolean isEdit_permission() {
        return edit_permission;
    }

    public void setEdit_permission(boolean edit_permission) {
        this.edit_permission = edit_permission;
    }

    public boolean isView_permission() {
        return view_permission;
    }

    public void setView_permission(boolean view_permission) {
        this.view_permission = view_permission;
    }

    public boolean isCommentRequired() {
        return commentRequired;
    }

    public void setCommentRequired(boolean commentRequired) {
        this.commentRequired = commentRequired;
    }
}
