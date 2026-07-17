package com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts;

public class DepartmentTO {
    private Integer id;
    private String name;

    public DepartmentTO() {
    }

    public DepartmentTO(Integer id, String name) {
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
        if (!(o instanceof DepartmentTO)) return false;

        DepartmentTO that = (DepartmentTO) o;

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
        return "DepartmentTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
