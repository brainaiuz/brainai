package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by User on 6/23/2016.
 */
public class TestRPC implements Serializable, IsSerializable {

    private Integer id;
    private String message;
    private MessageCommand messageCommands;
    private Boolean exists = true;
    private BigDecimal remainingAmount;
    private boolean error = false;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean exists() {
        return exists;
    }

    public void setExistance(Boolean exists) {
        this.exists = exists;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public MessageCommand getMessageCommand() {
        return messageCommands;
    }

    public void setMessageCommand(MessageCommand messageCommands) {
        this.messageCommands = messageCommands;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
