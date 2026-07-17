package com.edatasite.workforce.gwt.core.server.db.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.tools.StringUtil;

import java.io.InputStream;
import java.util.Properties;

/**
 * User: Akhror
 * Date: 12.04.2022
 */
public class MinIOStorageUtil {

    private static volatile MinIOStorageUtil minIOStorageUtil = new MinIOStorageUtil();
    private AmazonS3 s3;

    private MinIOStorageUtil() {
        try {
            /*
               You can use this in your web app where    AwsCredentials.properties is stored in web-inf/classes
             */
            String path = SpringPropertiesUtil.getProperty("minio.profile.path");
            if (StringUtil.isEmpty(path)) {
                ClassLoader loader = this.getClass().getClassLoader();
                InputStream is = loader.getResourceAsStream("MiniOCredentials.properties");
                Properties props = new Properties();
                props.load(is);
                this.s3 = new AmazonS3Client(new AWSCredentialsProvider() {
                    @Override
                    public AWSCredentials getCredentials() {
                        return new AWSCredentials() {
                            @Override
                            public String getAWSAccessKeyId() {
                                return props.getProperty("accessKey");
                            }

                            @Override
                            public String getAWSSecretKey() {
                                return props.getProperty("secretKey");
                            }
                        };
                    }

                    @Override
                    public void refresh() {

                    }
                });
            } else {
                this.s3 = new AmazonS3Client(new PropertiesFileCredentialsProvider(path));
            }

            //path style option true
            S3ClientOptions options = new S3ClientOptions();
            options.setPathStyleAccess(true);
            s3.setS3ClientOptions(options);

            String endpoint = SpringPropertiesUtil.getProperty("minio.profile.endpoint");
            if (!StringUtil.isEmpty(endpoint)) {
                s3.setEndpoint(endpoint);
            }
        } catch (Exception e) {
            System.out.println("exception while creating minios3client : " + e);
        }
    }

    public static MinIOStorageUtil getInstance() {
        return minIOStorageUtil;
    }

    public static AmazonS3 getMinIOClient() {
        return minIOStorageUtil.s3;
    }
}
