package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SipuniDto {

    private Integer event;
    private String call_id;
    private String src_num;
    private String src_type;
    private String dst_num;
    private String dst_type;
    private String timestamp;
    private String call_start_timestamp;
    private String call_answer_timestamp;
    private String call_record_link ;
    private String status;



    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public String getSrc_num() {
        return src_num;
    }

    public void setSrc_num(String src_num) {
        this.src_num = src_num;
    }

    public String getSrc_type() {
        return src_type;
    }

    public void setSrc_type(String src_type) {
        this.src_type = src_type;
    }

    public String getDst_num() {
        return dst_num;
    }

    public void setDst_num(String dst_num) {
        this.dst_num = dst_num;
    }

    public String getDst_type() {
        return dst_type;
    }

    public void setDst_type(String dst_type) {
        this.dst_type = dst_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCall_start_timestamp() {
        return call_start_timestamp;
    }

    public void setCall_start_timestamp(String call_start_timestamp) {
        this.call_start_timestamp = call_start_timestamp;
    }

    public String getCall_answer_timestamp() {
        return call_answer_timestamp;
    }

    public void setCall_answer_timestamp(String call_answer_timestamp) {
        this.call_answer_timestamp = call_answer_timestamp;
    }

    public String getCall_record_link() {
        return call_record_link;
    }

    public void setCall_record_link(String call_record_link) {
        this.call_record_link = call_record_link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
