package com.edatasite.workforce.gwt.documents.client.rest.resource;

import java.util.ArrayList;

public class SharedResource extends RestResource {

    public SharedResource() {
    }

    ArrayList<FolderResource> subFolders = new ArrayList<>();

    ArrayList<FileResource> files = new ArrayList<>();

    private boolean filesExpanded = false;

    public ArrayList<FolderResource> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(ArrayList<FolderResource> subFolders) {
        this.subFolders = subFolders;
    }

    /**
     * Retrieve the files.
     *
     * @return the files
     */
    public ArrayList<FileResource> getFiles() {
        return files;
    }

    /**
     * Modify the files.
     *
     * @param newFiles the files to set
     */
    public void setFiles(ArrayList<FileResource> newFiles) {
        files = newFiles;
    }

//    public List<String> getRootSharedFiles() {
//        List<String> res = new ArrayList<String>();
//        for (String f : getFilePaths()) {
//            boolean contained = false;
//            for (String fo : getSubfolderPaths())
//                if (f.startsWith(fo))
//                    contained = true;
//            if (!contained)
//                res.add(f);
//        }
//        return res;
//    }

    @Override
    public String getLastModifiedSince() {
        return null;
    }

    public void setFilesExpanded(boolean filesExpanded) {
        this.filesExpanded = filesExpanded;
    }
}
