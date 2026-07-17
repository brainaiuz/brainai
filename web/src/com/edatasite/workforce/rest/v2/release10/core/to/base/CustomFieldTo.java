package com.edatasite.workforce.rest.v2.release10.core.to.base;

public class CustomFieldTo {
    private String alias;
    private String value;

    public CustomFieldTo() {
    }

    public CustomFieldTo(String alias, String value) {
        this.alias = alias;
        this.value = value;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomFieldTo)) return false;

        CustomFieldTo that = (CustomFieldTo) o;

        if (getAlias() != null ? !getAlias().equals(that.getAlias()) : that.getAlias() != null) return false;
        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAlias() != null ? getAlias().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomFieldTo{" +
                "alias='" + alias + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
