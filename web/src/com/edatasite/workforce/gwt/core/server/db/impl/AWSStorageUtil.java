package com.edatasite.workforce.gwt.core.server.db.impl;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.tools.StringUtil;

/**
 * Created by Sherali on 3/25/2016.
 * Project web
 */
public class AWSStorageUtil {

    private AmazonS3 s3;
    private static volatile AWSStorageUtil awsstorageUtil = new AWSStorageUtil();

    private AWSStorageUtil() {
        try {
            /*
               You can use this in your web app where    AwsCredentials.properties is stored in web-inf/classes
             */
            String path = SpringPropertiesUtil.getProperty("aws.profile.path");
            if (StringUtil.isEmpty(path)) {
                this.s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
            } else {
                this.s3 = new AmazonS3Client(new PropertiesFileCredentialsProvider(path));
            }
        } catch (Exception e) {
            System.out.println("exception while creating awss3client : " + e);
        }
    }

    public static AWSStorageUtil getInstance() {
        return awsstorageUtil;
    }

    public static AmazonS3 getAWSClient() {
        return awsstorageUtil.s3;
    }
}
