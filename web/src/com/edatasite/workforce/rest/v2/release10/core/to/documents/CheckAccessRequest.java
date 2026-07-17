package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 11/18/2017.
 */
public class CheckAccessRequest extends ResponseData {

    private ArrayList<Integer> files;
    private ArrayList<Integer> folders;


    public CheckAccessRequest() {
    }

    public ArrayList<Integer> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<Integer> files) {
        this.files = files;
    }

    public ArrayList<Integer> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Integer> folders) {
        this.folders = folders;
    }
}
