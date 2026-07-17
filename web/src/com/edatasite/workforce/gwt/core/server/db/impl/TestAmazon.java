package com.edatasite.workforce.gwt.core.server.db.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import org.jets3t.service.S3Service;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.security.AWSCredentials;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Aziz
 * 24/02/1012
 */
public class TestAmazon {

    public static final int MAX_FILE_SIZE = 1024 * 1024 * 100; // 100 mb
    private static final String awsAccessKeyId = "0BR7SN5BS0HHT515DRR2";
    private static final String awsSecretAccessKey = "nUlVuaWlwury5YbGOPj1psf1KGUHGcRD2AE2yOkQ";

    private static String bucketName = "tmptest";

    public InputStream getInputStream(String accessKey) {
        try {
            S3Object object = AWSStorageUtil.getAWSClient().getObject(bucketName, accessKey);
            return object.getObjectContent();
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void putFile(String filename) throws IOException {
        String accessKey = "folder/2/" + UUID.randomUUID();
        File file = new File(filename);

        Date expireDate = new Date();
        expireDate.setDate(expireDate.getDate() + 1);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(identifyMimeType(file.getName()));
        metadata.setContentEncoding("UTF-8");
        metadata.setContentLength(file.length());
        metadata.setExpirationTime(expireDate);
        metadata.setContentDisposition("attachment; filename=" + file.getName());

        PutObjectRequest request = new PutObjectRequest(bucketName, accessKey, new FileInputStream(file), metadata);
        if (expireDate == null) {
            request.setCannedAcl(CannedAccessControlList.PublicRead);
        }
        try {
            PutObjectResult result = AWSStorageUtil.getAWSClient().putObject(request);
            System.out.println(getFileLink(accessKey));
        } catch (AmazonClientException e) {
            e.printStackTrace();
        }
    }

    private static boolean copyFile(String oldAccessKey, String newAccessKey, String originalName) {
        AWSCredentials myCredentials = new AWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        oldAccessKey = "4eea07e3-7492-4f4b-9392-b47475fc9a82";
        newAccessKey = "c1/" + oldAccessKey;
        originalName = "world.txt";
        System.out.println("Old Name -> " + oldAccessKey + ", New Name -> " + newAccessKey + ", Original Name -> " + originalName);
        try {
            S3Service myService = new RestS3Service(myCredentials);
            org.jets3t.service.model.S3Object newObject = myService.getObjectDetails(new S3Bucket(bucketName), oldAccessKey);
            newObject.setKey(newAccessKey);
            newObject.setContentType(newObject.getContentType());
            if (originalName != null) {
                newObject.setContentDisposition("attachment; filename=\"" + originalName + "\"");
            }
            newObject.removeMetadata("Cache-Control");

            myService.copyObject(bucketName, oldAccessKey, bucketName, newObject, true);
            myService.deleteObject(bucketName, oldAccessKey);
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static String getFileLink(String fileAccessKey) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileAccessKey);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        Date expiration = new Date();
        long milliSeconds = expiration.getTime();
        milliSeconds += 1000 * 60 * 60 * 24 * 3; // Add 3 day.
        expiration.setTime(milliSeconds);
        generatePresignedUrlRequest.setExpiration(expiration);
        URL url = AWSStorageUtil.getAWSClient().generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public static String identifyMimeType(String filename) {
        if (filename.contains(".")) {
            filename = filename.substring(filename.lastIndexOf('.')).toLowerCase(Locale.ENGLISH);
            if (filename.contains("jpg") || filename.contains("jpeg") || filename.contains("jpe")) {
                return "image/jpeg";
            } else if (filename.contains("png")) {
                return "image/png";
            } else if (filename.contains("bmp")) {
                return "image/bmp";
            } else if (filename.contains("gif")) {
                return "image/gif";
            } else if (filename.contains("tiff") || filename.contains("tif")) {
                return "image/tiff";
            } else if (filename.contains("txt")) {
                return "text/plain";
            } else if (filename.contains("html") || filename.contains("htm")) {
                return "text/html";
            } else if (filename.contains("odt")) {
                return "application/vnd.oasis.opendocument.text";
            } else if (filename.contains("sxw")) {
                return "application/vnd.sun.xml.writer";
            } else if (".doc".equals(filename)) {
                return "application/msword";
            } else if (filename.contains("docx")) {
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            } else if (filename.contains("rtf")) {
                return "application/rtf";
            } else if (filename.contains("pdf")) {
                return "application/pdf";
            } else if (filename.contains("pps") || ".ppt".contains(filename)) {
                return "application/vnd.ms-powerpoint";
            } else if (filename.contains("pptx")) {
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            } else if (".xls".contains(filename)) {
                return "application/vnd.ms-excel";
            } else if (filename.contains("xlsx")) {
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else if (filename.contains("ods")) {
                return "application/vnd.oasis.opendocument.spreadsheet";
            } else if (filename.contains("csv")) {
                return "text/csv";
            } else if (filename.contains("tsv") || filename.contains("tab")) {
                return "text/tab-separated-values";
            } else if (filename.contains("swf")) {
                return "application/x-shockwave-flash";
            } else if (filename.contains("zip")) {
                return "application/zip";
            }
        }
        return "application/octet-stream";
    }


    public static void main(String[] args) throws Exception {
        putFile("d:\\home\\wfm\\2878024.pdf");
    }

}
