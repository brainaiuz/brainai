package com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts;

public class CustomerOrSupplierTO {
    private Integer id;
    private String name;

    public CustomerOrSupplierTO() {
    }

    public CustomerOrSupplierTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerOrSupplierTO)) return false;

        CustomerOrSupplierTO that = (CustomerOrSupplierTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomerOrSupplierTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
