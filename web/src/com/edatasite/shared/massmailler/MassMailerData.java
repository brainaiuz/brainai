/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:33:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.shared.massmailler;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 026-01-2016
 * Time: 15:15:41
 * To change this template use File | Settings | File Templates.
 */
public class MassMailerData implements Serializable {

    private Integer sentCount;
    private Integer count;
    private MassMailerCrmEntityBody crmEntityBody;
    private MassMailerBody body;
    private String sendType;

    public MassMailerCrmEntityBody getCrmEntityBody() {
        return crmEntityBody;
    }

    public void setCrmEntityBody(MassMailerCrmEntityBody crmEntityBody) {
        this.crmEntityBody = crmEntityBody;
    }

    public MassMailerBody getBody() {
        return body;
    }

    public void setBody(MassMailerBody body) {
        this.body = body;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public Integer getSentCount() {
        return sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
