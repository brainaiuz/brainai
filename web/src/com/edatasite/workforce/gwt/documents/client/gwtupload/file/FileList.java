package com.edatasite.workforce.gwt.documents.client.gwtupload.file;

import com.edatasite.workforce.gwt.documents.client.gwtupload.file.impl.FileListImpl;

import java.util.Iterator;

public class FileList implements Iterable<File> {
    private FileListImpl impl;

    public FileList(FileListImpl impl) {
        this.impl = impl;
    }

    @Override
    public Iterator<File> iterator() {
        return new Iterator<File>() {
            private int index;

            @Override
            public boolean hasNext() {
                return index < getLength();
            }

            @Override
            public File next() {
                return getItem(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public final File getItem(int index) {
        return impl.getItem(index);
    }

    public final int getLength() {
        return impl.getLength();
    }
}
