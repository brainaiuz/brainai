package com.edatasite.workforce.rest.v3.release10.core.to;

public class CFItemExistanceDto {
    private boolean exists;
    private CustomFormDto item;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public CustomFormDto getItem() {
        return item;
    }

    public void setItem(CustomFormDto item) {
        this.item = item;
    }
}
