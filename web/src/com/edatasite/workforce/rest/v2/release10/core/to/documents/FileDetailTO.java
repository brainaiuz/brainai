package com.edatasite.workforce.rest.v2.release10.core.to.documents;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/27/2017.
 */
public class FileDetailTO extends FileTO {
    private ArrayList<ParentFolderTO> parent_folders;
    private Integer file_count;

    public ArrayList<ParentFolderTO> getParent_folders() {
        return parent_folders;
    }

    public void setParent_folders(ArrayList<ParentFolderTO> parent_folders) {
        this.parent_folders = parent_folders;
    }

    public Integer getFile_count() {
        return file_count;
    }

    public void setFile_count(Integer file_count) {
        this.file_count = file_count;
    }
}
