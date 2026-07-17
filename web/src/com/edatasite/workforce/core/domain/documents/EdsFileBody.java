package com.edatasite.workforce.core.domain.documents;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 13.05.2010
 * Time: 21:32:14
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "fileBody")
public class EdsFileBody extends EdsUpload {

    /**
     * The logger.
     */
    private static Log logger = LogFactory.getLog(EdsFileBody.class);

    /**
     * The audit information.
     */
    @Embedded
    private EdsAuditInfo auditInfo;

    /**
     * The header of the file.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsFileHeader header;

    private String description;

    /**
     * The file size in bytes
     */
    private long fileSize;

    /**
     * The version of the file, not the JPA version field!
     */
    private int version;


    /**
     * Returns the version
     *
     * @return int
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets a new version
     *
     * @param newVersion
     */
    public void setVersion(final int newVersion) {
        version = newVersion;
    }

    /**
     * Retrieve the file header.
     *
     * @return the file header
     */
    public EdsFileHeader getHeader() {
        return header;
    }

    /**
     * Modify the file header.
     *
     * @param newHeader the new header
     */
    public void setHeader(final EdsFileHeader newHeader) {
        header = newHeader;
    }

    /**
     * Retrieve the file size.
     *
     * @return the file size
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * Modify the file size.
     *
     * @param newFileSize the new file size
     */
    public void setFileSize(final long newFileSize) {
        fileSize = newFileSize;
    }

    /**
     * Modify the audit info.
     *
     * @param newAuditInfo the new audit info
     */
    public void setAuditInfo(final EdsAuditInfo newAuditInfo) {
        auditInfo = newAuditInfo;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the original filename URL-encoded.
     *
     * @return the original filename URL-encoded.
     */
    public String getOriginalFilenameEncoded() {
        try {
            return URLEncoder.encode(getOriginalName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
            return getOriginalName();
        }
    }

}
