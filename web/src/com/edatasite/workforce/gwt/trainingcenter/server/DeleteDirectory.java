package com.edatasite.workforce.gwt.trainingcenter.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/7/12
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class DeleteDirectory {

    private static Logger log = LoggerFactory.getLogger(DeleteDirectory.class);

    public static void delete(String url) {
        File directory = new File(url);
        //make sure directory exists
        if (!directory.exists()) {
            log.info("Directory does not exist.");
        } else {
            try {
                delete(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("Done");
    }

    public static void delete(File file) throws IOException {
        if (file.isDirectory()) {
            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
                log.info("Directory is deleted : " + file.getAbsolutePath());
            } else {
                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);
                    //recursive delete
                    delete(fileDelete);
                }
                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    log.info("Directory is deleted : " + file.getAbsolutePath());
                }
            }
        } else {
            //if file, then delete it
            file.delete();
            log.info("File is deleted : " + file.getAbsolutePath());
        }
    }
}
