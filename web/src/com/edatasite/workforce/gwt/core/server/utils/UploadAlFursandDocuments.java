package com.edatasite.workforce.gwt.core.server.utils;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.impl.AWSStorageUtil;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Normurod on 3/29/2016.
 */

@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UploadAlFursandDocuments implements HttpRequestHandler {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DocumentsServiceLocal documentsService;

    @Autowired
    private FolderManager folderManager;

    private String CANCELLATION = "Cancellation";
    private String CVS = "CV's";
    private String EMIRATES_ID = "Emirates ID";
    private String E_VISA = "E-Visa/rona for k pi/visa copy kpi";
    private String LABOUR_CARD = "Labour Card";
    private String LABOUR_CONTRACT = "Labour Contract";
    private String MEDICAL_REPORT = "Medical Report";
    private String PASSPORT_VISA = "Passport & Visa";
    private String RENEWAL_AGREEMENT = "Renewal Agreement";
    private String RESEDENCE_APPLICATION_APPROVAL = "Residence Application and Approval";

    private String SOURCE_BUCKET_NAME = "temporarydatas";
    private String DESTINATION_BUCKET_NAME = "workforcetrack";

    private String JPE_EXTENSION = ".jpg";
    private String PDF_EXTENSION = ".pdf";

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fromBucket = request.getParameter("fromBucket");
        String toBucket = request.getParameter("toBucket");

        if (fromBucket != null && !fromBucket.trim().isEmpty()) {
            SOURCE_BUCKET_NAME = fromBucket.trim();
        }
        if (toBucket != null && !toBucket.trim().isEmpty()) {
            DESTINATION_BUCKET_NAME = toBucket.trim();
        }

        ServerSecurityContext.getInstance().setSessionId(getCookie(request, "SESSION_ID"));

        int start = 0, limit = 10;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(start);
        fp.setLimit(limit);

        ListResult<EmployeeListItem> listResult = null;

        EdsFolder folder = folderManager.getFolder(Constants.F_EMPLOYEE_PROFILE, null);
        Integer folderID = folder != null ? folder.getObjectID() : null;
        Set<String> codeList = new HashSet<>();

        //do {
            listResult = employeeService.getEmployeeList(fp);

            if (listResult != null && !listResult.getList().isEmpty()) {
                for (EmployeeListItem employee : listResult.getList()) {
                    if (employee.getEmployeeNumber() != null && !employee.getEmployeeNumber().isEmpty()) {
                        ArrayList<DocumentItem> userDocumentList = new ArrayList<>();

                        codeList.add(employee.getEmployeeNumber());

                        List<String> keyList = folderManager.getS3Keys(employee.getEmployeeNumber());
                        if (keyList != null && !keyList.isEmpty()) {
                            for (String key : keyList) {
                                ObjectMetadata objectMetadata = getObjectMetadata(SOURCE_BUCKET_NAME, key);
                                if (objectMetadata != null) {
                                    DocumentItem item = buildDocumentByMetadata(objectMetadata, key.replaceAll("[.jpg]", "").replace("/","-"), folderID, JPE_EXTENSION, "Security Pass");
                                    item.setSourceKey(key);
                                    userDocumentList.add(item);
                                }
                            }
                        }

                        try {
                            if (!userDocumentList.isEmpty()) {
                                documentsService.createFile(userDocumentList, EdsContextParams.getUploadType(), Constants.F_EMPLOYEE_PROFILE, employee.getObjectID(), SOURCE_BUCKET_NAME, DESTINATION_BUCKET_NAME);
                            }
                        } catch (DuplicateNameException | QuotaExceededException | InsufficientPermissionsException | ObjectNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println(codeList);
            }

            start += limit;
            fp.setStart(start);
            System.out.println("START: " + start);
//        } while (listResult != null && !listResult.getList().isEmpty());

        ServerSecurityContext.getInstance().setSessionId(null);

//        List<String> list = folderManager.getS3Keys("17");

        /*try {
//            CopyObjectRequest copyObjRequest = new CopyObjectRequest(
//                    "alfursanimport", "Cancellation/2042.jpg", "wfmtest", "alfursan_test_2042.jpg");
//            AWSStorageUtil.getAWSClient().copyObject(copyObjRequest);
//            response.getWriter().println("Successfully copied!");
//            ObjectMetadata objectMetadata = AWSStorageUtil.getAWSClient().getObjectMetadata("wfmtest", "alfursan_test_2042.jpg");
//            System.out.println(objectMetadata.getContentType());

//            S3Object object = AWSStorageUtil.getAWSClient().getObject("alfursanimport", "Cancellation/2042.jpg");
            ListObjectsRequest req = new ListObjectsRequest().withBucketName("temporarydatas").withPrefix("Security Pass/");

            ObjectListing listing = null;

            while((listing == null) || (req.getMarker() != null)) {
                listing = AWSStorageUtil.getAWSClient().listObjects(req);
                List<S3ObjectSummary> objectSummaryList = listing.getObjectSummaries();

                for (S3ObjectSummary object : objectSummaryList) {
                    if (object.getKey().lastIndexOf(".jpg") > 0 || object.getKey().lastIndexOf(".jpeg") > 0 || object.getKey().lastIndexOf(".png") > 0) {
                        folderManager.createS3Key(object.getKey());
                    }
                }
                // do stuff with listing
                req.setMarker(listing.getNextMarker());
            }

            *//*List<S3ObjectSummary> objectSummaries = object2.getObjectSummaries();

            req.setMarker(object2.getNextMarker());
            object2 = AWSStorageUtil.getAWSClient().listObjects(req);

            for (int i = 0; i < objectSummaries.size(); i++) {
                if (objectSummaries.get(i).getKey().lastIndexOf(JPE_EXTENSION) != -1 || objectSummaries.get(i).getKey().lastIndexOf(PDF_EXTENSION) != -1) {
                    DocumentItem item = buildDocumentByS3Object(objectSummaries.get(i), "Residence Application and Approval ("+(i+1)+")", null, JPE_EXTENSION, "");
                }
            }
            System.out.println(object2.getBucketName());*//*
            //return object.getObjectContent();
        } catch (AmazonServiceException ex) {
            ex.printStackTrace();
            //return null;
        }*/
    }

    private DocumentItem buildDocumentByMetadata(ObjectMetadata objectMetadata, String documentName, Integer folderId, String extension, String description) {
        DocumentItem item = new DocumentItem();
        item.setContentType(objectMetadata.getContentType());
        item.setSize(objectMetadata.getContentLength());
        item.setDescription(description);
        item.setName(documentName+extension);
        item.setFolderId(folderId);
        return item;
    }

    private DocumentItem buildDocumentByS3Object(S3ObjectSummary objectSummary, String documentName, Integer folderId, String extension, String description) {
        DocumentItem item = new DocumentItem();
        item.setSourceKey(objectSummary.getKey());
        item.setSize(objectSummary.getSize());
        item.setDescription(description);

        if (objectSummary.getKey().lastIndexOf(".pdf") != -1) {
            item.setName(documentName+PDF_EXTENSION);
            item.setContentType("application/pdf");
        } else {
            item.setName(documentName+JPE_EXTENSION);
            item.setContentType("image/jpeg");
        }
        item.setFolderId(folderId);
        return item;
    }

    private boolean isValidFile(String bucket, String key) {
        boolean isValidFile = true;
        try {
            ObjectMetadata objectMetadata = AWSStorageUtil.getAWSClient().getObjectMetadata(bucket, key);
        } catch (AmazonS3Exception s3e) {
            if (s3e.getStatusCode() == 404) {
                // i.e. 404: NoSuchKey - The specified key does not exist
                isValidFile = false;
            } else {
                s3e.printStackTrace();
                isValidFile = false;
            }
        }

        return isValidFile;
    }

    private ObjectMetadata getObjectMetadata(String bucket, String key) {
        try {
            return AWSStorageUtil.getAWSClient().getObjectMetadata(bucket, key);
        } catch (AmazonS3Exception s3e) {
            s3e.printStackTrace();
        }

        return null;
    }

    private List<S3ObjectSummary> getObjectListing(String bucket, String key) {
        try {
            ObjectListing objectListing = AWSStorageUtil.getAWSClient().listObjects(bucket, key);
            return objectListing.getObjectSummaries();
        } catch (AmazonS3Exception s3e) {
            s3e.printStackTrace();
        }

        return null;
    }

    private String getCookie(HttpServletRequest request, String key) {
        if (request.getCookies() != null) {
            for (int i = 0; i < request.getCookies().length; i++) {
                if (request.getCookies()[i].getName().equals(key)) {
                    return request.getCookies()[i].getValue();
                }
            }
        }
        return null;
    }

    /*
    do {
            listResult = employeeService.getEmployeeList(fp);

            if (listResult != null && !listResult.getList().isEmpty()) {
                for (EmployeeListItem employee : listResult.getList()) {
                    if (employee.getEmployeeNumber() != null && !employee.getEmployeeNumber().isEmpty()) {
                        ArrayList<DocumentItem> userDocumentList = new ArrayList<>();

                        codeList.add(employee.getEmployeeNumber());

                        //-----user Cancellation documents-----//
                        String sourceKey = CANCELLATION+"/"+employee.getEmployeeNumber()+".jpg";
                        ObjectMetadata objectMetadata = getObjectMetadata(SOURCE_BUCKET_NAME, sourceKey);

                        if (objectMetadata != null) {
                            DocumentItem item = buildDocumentByMetadata(objectMetadata, "Cancellation " + employee.getEmployeeNumber(), folderID, JPE_EXTENSION, "Cancellation");
                            item.setSourceKey(sourceKey);
                            userDocumentList.add(item);
                        }

                        //-----user CV's documents-----//
                        sourceKey = CVS+"/"+employee.getEmployeeNumber()+"/";
                        List<S3ObjectSummary> objectSummaries = getObjectListing(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectSummaries != null && !objectSummaries.isEmpty()) {
                            for (int i = 0; i < objectSummaries.size(); i++) {
                                if (objectSummaries.get(i).getKey().lastIndexOf(JPE_EXTENSION) != -1 || objectSummaries.get(i).getKey().lastIndexOf(PDF_EXTENSION) != -1) {
                                    DocumentItem item = buildDocumentByS3Object(objectSummaries.get(i), "CV's " + employee.getEmployeeNumber() +"("+(i+1)+")", folderID, JPE_EXTENSION, "CV's");
                                    userDocumentList.add(item);
                                }
                            }
                        }

                        //-----user Emirates ID documents-----//
                        sourceKey = EMIRATES_ID+"/"+employee.getEmployeeNumber()+"/";
                        objectSummaries = getObjectListing(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectSummaries != null && !objectSummaries.isEmpty()) {
                            for (int i = 0; i < objectSummaries.size(); i++) {
                                if (objectSummaries.get(i).getKey().lastIndexOf(JPE_EXTENSION) != -1 || objectSummaries.get(i).getKey().lastIndexOf(PDF_EXTENSION) != -1) {
                                    DocumentItem item = buildDocumentByS3Object(objectSummaries.get(i), "Emirates ID " + employee.getEmployeeNumber() +"("+(i+1)+")", folderID, JPE_EXTENSION, "Emirates ID");
                                    userDocumentList.add(item);
                                }
                            }
                        }

                        //-----user Emirates ID documents-----//
                        sourceKey = E_VISA+"/"+employee.getEmployeeNumber()+".jpg";
                        objectMetadata = getObjectMetadata(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectMetadata != null) {
                            DocumentItem item = buildDocumentByMetadata(objectMetadata, "E-Visa " + employee.getEmployeeNumber(), folderID, JPE_EXTENSION, "E-Visa");
                            item.setSourceKey(sourceKey);
                            userDocumentList.add(item);
                        }

                        //-----user Labour Card documents-----//
                        sourceKey = LABOUR_CARD+"/"+employee.getEmployeeNumber()+".pdf";
                        objectMetadata = getObjectMetadata(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectMetadata != null) {
                            DocumentItem item = buildDocumentByMetadata(objectMetadata, "Labour Card " + employee.getEmployeeNumber(), folderID, PDF_EXTENSION, "Labour Card");
                            item.setSourceKey(sourceKey);
                            userDocumentList.add(item);
                        }

                        //-----user Labour Contract documents-----//
                        sourceKey = LABOUR_CONTRACT+"/"+employee.getEmployeeNumber()+".pdf";
                        objectMetadata = getObjectMetadata(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectMetadata != null) {
                            DocumentItem item = buildDocumentByMetadata(objectMetadata, "Labour Contract " + employee.getEmployeeNumber(), folderID, PDF_EXTENSION, "Labour Contract");
                            item.setSourceKey(sourceKey);
                            userDocumentList.add(item);
                        }

                        //-----user Medical Report documents-----//
                        sourceKey = MEDICAL_REPORT+"/"+employee.getEmployeeNumber()+".jpg";
                        objectMetadata = getObjectMetadata(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectMetadata != null) {
                            DocumentItem item = buildDocumentByMetadata(objectMetadata, "Medical Report " + employee.getEmployeeNumber(), folderID, JPE_EXTENSION, "Medical Report");
                            item.setSourceKey(sourceKey);
                            userDocumentList.add(item);
                        }

                        //-----user Passport & Visa documents-----//
                        sourceKey = PASSPORT_VISA+"/"+employee.getEmployeeNumber()+"/";
                        objectSummaries = getObjectListing(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectSummaries != null && !objectSummaries.isEmpty()) {
                            for (int i = 0; i < objectSummaries.size(); i++) {
                                if (objectSummaries.get(i).getKey().lastIndexOf(JPE_EXTENSION) != -1 || objectSummaries.get(i).getKey().lastIndexOf(PDF_EXTENSION) != -1) {
                                    DocumentItem item = buildDocumentByS3Object(objectSummaries.get(i), "Passport & Visa " + employee.getEmployeeNumber() +"("+(i+1)+")", folderID, JPE_EXTENSION, "Passport & Visa");
                                    userDocumentList.add(item);
                                }
                            }
                        }

                        //-----user Renewal Agreement documents-----//
                        sourceKey = MEDICAL_REPORT+"/"+employee.getEmployeeNumber()+".jpg";
                        objectMetadata = getObjectMetadata(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectMetadata != null) {
                            DocumentItem item = buildDocumentByMetadata(objectMetadata, "Renewal Agreement " + employee.getEmployeeNumber(), folderID, JPE_EXTENSION, "Renewal Agreement");
                            item.setSourceKey(sourceKey);
                            userDocumentList.add(item);
                        }

                        //-----user Residence Application and Approval documents-----//
                        sourceKey = PASSPORT_VISA+"/"+employee.getEmployeeNumber()+"/";
                        objectSummaries = getObjectListing(SOURCE_BUCKET_NAME, sourceKey);
                        if (objectSummaries != null && !objectSummaries.isEmpty()) {
                            for (int i = 0; i < objectSummaries.size(); i++) {
                                if (objectSummaries.get(i).getKey().lastIndexOf(JPE_EXTENSION) != -1 || objectSummaries.get(i).getKey().lastIndexOf(PDF_EXTENSION) != -1) {
                                    DocumentItem item = buildDocumentByS3Object(objectSummaries.get(i), "Residence Application and Approval " + employee.getEmployeeNumber() +"("+(i+1)+")", folderID, JPE_EXTENSION, "Residence Application and Approval");
                                    userDocumentList.add(item);
                                }
                            }
                        }

                        try {
                            if (!userDocumentList.isEmpty()) {
                                documentsService.createFile(userDocumentList, Constants.AMAZON, Constants.F_EMPLOYEE_PROFILE, employee.getObjectID(), SOURCE_BUCKET_NAME, DESTINATION_BUCKET_NAME);
                            }
                        } catch (DuplicateNameException e) {
                            e.printStackTrace();
                        } catch (ObjectNotFoundException e) {
                            e.printStackTrace();
                        } catch (InsufficientPermissionsException e) {
                            e.printStackTrace();
                        } catch (QuotaExceededException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println(codeList);
            }

            start += limit;
            fp.setStart(start);
            System.out.println("START: " + start);
        } while (listResult != null && !listResult.getList().isEmpty());
    */
}
