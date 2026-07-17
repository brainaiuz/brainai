package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.batchinvoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.Date;

/**
 * Created by Dilsh0d Madrahimov on 1/18/2019.
 */
public class ImportLogTO extends ResponseData {
    private Date import_date;
    private String status;
    private Integer requested;
    private Integer imported;
    private Integer rejected;
    private String imported_file;
    private String imported_file_url;
    private String log_file;
    private String log_file_url;

    public ImportLogTO() {
    }

    public ImportLogTO(Date import_date, String status, Integer requested, Integer imported, Integer rejected, String imported_file, String imported_file_url, String log_file, String log_file_url) {
        this.import_date = import_date;
        this.status = status;
        this.requested = requested;
        this.imported = imported;
        this.rejected = rejected;
        this.imported_file = imported_file;
        this.imported_file_url = imported_file_url;
        this.log_file = log_file;
        this.log_file_url = log_file_url;
    }

    public Date getImport_date() {
        return import_date;
    }

    public void setImport_date(Date import_date) {
        this.import_date = import_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRequested() {
        return requested;
    }

    public void setRequested(Integer requested) {
        this.requested = requested;
    }

    public Integer getImported() {
        return imported;
    }

    public void setImported(Integer imported) {
        this.imported = imported;
    }

    public Integer getRejected() {
        return rejected;
    }

    public void setRejected(Integer rejected) {
        this.rejected = rejected;
    }

    public String getImported_file() {
        return imported_file;
    }

    public void setImported_file(String imported_file) {
        this.imported_file = imported_file;
    }

    public String getImported_file_url() {
        return imported_file_url;
    }

    public void setImported_file_url(String imported_file_url) {
        this.imported_file_url = imported_file_url;
    }

    public String getLog_file() {
        return log_file;
    }

    public void setLog_file(String log_file) {
        this.log_file = log_file;
    }

    public String getLog_file_url() {
        return log_file_url;
    }

    public void setLog_file_url(String log_file_url) {
        this.log_file_url = log_file_url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImportLogTO)) return false;

        ImportLogTO that = (ImportLogTO) o;

        if (import_date != null ? !import_date.equals(that.import_date) : that.import_date != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (requested != null ? !requested.equals(that.requested) : that.requested != null) return false;
        if (imported != null ? !imported.equals(that.imported) : that.imported != null) return false;
        if (rejected != null ? !rejected.equals(that.rejected) : that.rejected != null) return false;
        if (imported_file != null ? !imported_file.equals(that.imported_file) : that.imported_file != null)
            return false;
        if (imported_file_url != null ? !imported_file_url.equals(that.imported_file_url) : that.imported_file_url != null)
            return false;
        if (log_file != null ? !log_file.equals(that.log_file) : that.log_file != null) return false;
        if (log_file_url != null ? !log_file_url.equals(that.log_file_url) : that.log_file_url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = import_date != null ? import_date.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (requested != null ? requested.hashCode() : 0);
        result = 31 * result + (imported != null ? imported.hashCode() : 0);
        result = 31 * result + (rejected != null ? rejected.hashCode() : 0);
        result = 31 * result + (imported_file != null ? imported_file.hashCode() : 0);
        result = 31 * result + (imported_file_url != null ? imported_file_url.hashCode() : 0);
        result = 31 * result + (log_file != null ? log_file.hashCode() : 0);
        result = 31 * result + (log_file_url != null ? log_file_url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImportLogTO{" +
                "import_date=" + import_date +
                ", status='" + status + '\'' +
                ", requested=" + requested +
                ", imported=" + imported +
                ", rejected=" + rejected +
                ", imported_file='" + imported_file + '\'' +
                ", imported_file_url='" + imported_file_url + '\'' +
                ", log_file='" + log_file + '\'' +
                ", log_file_url='" + log_file_url + '\'' +
                '}';
    }
}
