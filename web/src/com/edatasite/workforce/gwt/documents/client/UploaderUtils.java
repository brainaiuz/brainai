package com.edatasite.workforce.gwt.documents.client;

import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: Nov 18, 2010
 * Time: 10:25:18 PM
 */
public class UploaderUtils {

    /**
     * Same as URL.encode, but also encode apostrophe since browsers aren't
     * consistent about it (FF encodes, IE does not).
     */
    protected String encode(String decodedURL) {
        String retv = decodedURL.replace("@", "_"); // Replace bad character
        retv = URL.encodeComponent(retv);
        retv = retv.replace("'", "%27");
        return retv;
    }

    /**
     * Returns the file name from a potential full path argument. Apparently IE
     * insists on sending the full path name of a file when uploading, forcing
     * us to trim the extra path info. Since this is only observed on Windows we
     * get to check for a single path separator value.
     *
     * @param name the potentially full path name of a file
     * @return the file name without extra path information
     */
    protected String getFilename(String name) {
        int pathSepIndex = name.lastIndexOf("\\");
        if (pathSepIndex == -1) {
            pathSepIndex = name.lastIndexOf("/");
            if (pathSepIndex == -1) {
                return name;
            }
        }
        return name.substring(pathSepIndex + 1);
    }

    /**
     * Return the given size in a humanly readable form, using SI units to denote
     * size information, e.g. 1 KB = 1000 B (bytes).
     *
     * @param size in bytes
     * @return the size in human readable string
     */
    public static String getFileSizeAsString(long size) {
        if (size < 1024) {
            return String.valueOf(size) + " B";
        } else if (size < 1024 * 1024) {
            return getSize(size, 1024D) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return getSize(size, (1024D * 1024D)) + " MB";
        }
        return getSize(size, (1024D * 1024D * 1024D)) + " GB";
    }

    public static String getSize(Long size, Double division) {
        Double res = Double.valueOf(size.toString()) / division;
        NumberFormat nf = NumberFormat.getFormat("######.#");
        return nf.format(res);
    }

}
