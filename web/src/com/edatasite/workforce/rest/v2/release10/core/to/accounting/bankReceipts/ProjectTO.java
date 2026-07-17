package com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts;

public class ProjectTO {
    private Integer id;
    private String name;
    private String number;

    public ProjectTO() {
    }

    public ProjectTO(Integer id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectTO)) return false;

        ProjectTO projectTO = (ProjectTO) o;

        if (id != null ? !id.equals(projectTO.id) : projectTO.id != null) return false;
        if (name != null ? !name.equals(projectTO.name) : projectTO.name != null) return false;
        if (number != null ? !number.equals(projectTO.number) : projectTO.number != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
