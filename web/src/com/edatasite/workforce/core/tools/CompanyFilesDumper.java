package com.edatasite.workforce.core.tools;

import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Author: Baxtiyor Axmedov
 */
public class CompanyFilesDumper {

    private static final Logger log = LoggerFactory.getLogger(CompanyFilesDumper.class);
    private static final int TYPE_AMAZON = 178;
    private static final int TYPE_LOCAL = 179;
    private static Properties props;

    static {
        props = new Properties();
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        if (StringUtil.isEmpty(profile)) {
            System.out.println("--------------------------");
            System.out.println("Please set spring.profiles.active!!!");
            System.out.println("VM options add this -Dspring.profiles.active=local or app");
            System.out.println("--------------------------");
            System.exit(0);
        }
//        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/application.properties");
        InputStream stream0 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/hibernate.properties");
        InputStream stream1 = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/" + profile + "/application.properties");
        if (stream0 != null) {
            try {
                props.load(stream0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (stream1 != null) {
            try {
                props.load(stream1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        log.info("IMPORTANT: Script runs only on Linux-based servers");
        if (args.length < 2) {
            log.error("Arguments must be specified in a form of <backup_folder_path> <companyID>");
            System.exit(0);
        }

        String backupFolder = args[0];
        String companyIDStr = args[1];

        // TODO: should be added AMAZON dump
        dumpFiles(backupFolder, Integer.parseInt(companyIDStr), TYPE_LOCAL);
    }

    private static void dumpFiles(String backupFolder, Integer companyID, int type) {
        log.debug("dumpFiles...");

        // Variables
        List<EdsUpload> queue = new ArrayList<>(20000);
        Map<Integer, EdsUser> usersMap = new HashMap<>();
        List<String> failedPaths = new ArrayList<>();
        String slashedName = "\"" + companyID + "\"";
        Configuration cfg = getConfiguration(slashedName);
        try (Connection conn = WfmGetConnection.getConnection(cfg.getProperty("hibernate.connection.url"))) {
            String sql;
            // Get Local Files
            sql = "SELECT * FROM " + slashedName + ".upload WHERE typeid=? ORDER BY id";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, type);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    EdsUpload upload = new EdsUpload();
                    Integer id = rs.getInt("id");
                    upload.setObjectID(id);
                    upload.setSize(rs.getLong("size"));
                    upload.setLocalPath(rs.getString("localpath"));
                    upload.setOriginalName(rs.getString("originalname"));
                    queue.add(upload);
                }
            } catch (SQLException e) {
                log.error("SQLException: ", e);
            }

            // Get Company Users
            sql = "SELECT * FROM " + slashedName + ".myuser ORDER BY id";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    EdsUser user = new EdsUser();
                    Integer id = rs.getInt("id");
                    user.setObjectID(id);
                    user.setFirstName(rs.getString("firstname"));
                    user.setMiddleName(rs.getString("middlename"));
                    user.setLastName(rs.getString("lastname"));
                    user.setEmail(rs.getString("email"));
                    usersMap.put(id, user);
                }
            } catch (SQLException e) {
                log.error("SQLException: ", e);
            }
        } catch (SQLException e) {
            log.error("SQLException: ", e);
        }

        // Add README.csv in Backup Folder
        try (PrintWriter pw = new PrintWriter(new FileWriter(backupFolder + "/README.csv"))) {
            pw.write("User ID, Email, First name, Last name\n");
            usersMap.forEach((k, v) -> {
                String line = v.getObjectID() + "," + v.getEmail() + "," + v.getFirstName() + "," + v.getLastName();
                pw.write(line + '\n');
            });
            pw.close();
        } catch (IOException e) {
            log.error("IOException: " + e.toString());
        }

        Long totalSize = 0L;
        // Real copy to folder
        // See: UploadManagerImpl.getResourceDirectory()
        for (EdsUpload v : queue) {
            try {
                // localpath: home/ebs/storage/500005/1/
                // id: 42
                // original-name: 1.csv

                // extract userID
                String filePath = "/" + v.getLocalPath() + "/" + v.getObjectID(); // FIXME: '/' hardcoded
                File file = new File(filePath);
                String parentFolder = file.getParentFile().getName();
//                System.out.println(filePath);

                // copy
                Path FROM = Paths.get(filePath);
                String toFolder = backupFolder + "/" + parentFolder + "/";
                File toFolderFile = new File(toFolder);
                if (!toFolderFile.exists()) {
                    toFolderFile.mkdir();
                }
                Path TO = Paths.get(toFolder + v.getOriginalName());
                CopyOption[] options = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES};
                try {
                    Files.copy(FROM, TO, options);
                    totalSize += v.getSize();
                } catch (IOException e) {
                    //log.error("IOException: " + e.toString());
                    failedPaths.add(filePath);
                }
            } catch (Exception e) {
                log.error("Exception: " + e.toString());
            }
        }

        System.out.println("========================================================");
        System.out.println("Total size: " + totalSize);
        System.out.println("Total failed: " + failedPaths.size());
        for (String s : failedPaths) {
            System.out.println(s);
        }

    }

    private static Configuration getConfiguration(String slashedName) {
        Configuration cfg = new Configuration().configure();

        String clusterType = props.getProperty("cluster.type", "");
        clusterType = clusterType.toLowerCase();
        String dbUrl = EdsSchemaUpdater.getClusterUrl(clusterType);
        EdsSchemaUpdater.setDbUrl(dbUrl);
        Properties properties = EdsSchemaUpdater.getProperties(slashedName);

        cfg.setProperties(properties);

        return cfg;
    }
}
