package com.edatasite.workforce.gwt.core.server.servlets.eml;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * User: Ilhombek
 * Date: 15.09.2010
 * Time: 17:16:15
 */
public class CreateZipFile {

    public final static int BUFFER = 10240;
    public final static String EXTENSION_FILTER = "eml";
    public boolean isZippedFiles = false;

    public CreateZipFile(File directory) {
        isZippedFiles = Compress(directory, "eml", false);
    }

    public static boolean Compress(File directory) {
        return Compress(directory, EXTENSION_FILTER, true);
    }

    public static boolean Compress(File directory, String extensionFilter, boolean useTree) {
        BufferedInputStream origin = null;
        String destFile = directory.getAbsolutePath();

        if (directory.isDirectory()) {
            if (destFile.length() < 1) {
                return false;
            } //
            if (destFile.charAt(destFile.length() - 1) == '\\') {
                destFile = destFile.substring(0, destFile.length() - 1);
            } else if (destFile.charAt(destFile.length() - 1) == '/') {
                destFile = destFile.substring(0, destFile.length() - 1);
            }
            destFile += ".zip";
        } else if (directory.isFile()) {
            if (destFile.length() < 5) {
                return false;
            } // there should be at least one character for file name, and the extension
            destFile = destFile.substring(0, destFile.length() - 4) + ".zip";
        } else {
            return false;
        }
        try {
            FileOutputStream dest = new FileOutputStream(destFile);

            CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
            out.setMethod(ZipOutputStream.DEFLATED);
            out.setLevel(9);

            List<String> tree;
            if (useTree) {
                tree = getFilesTree(directory, extensionFilter);
            } else {
                tree = getFiles(directory, extensionFilter);
            }

            for (String node : tree) {
                System.out.println("Adding: " + node);
                FileInputStream fi = new FileInputStream(directory.getParent() + (useTree ? "" : (File.separator/*"\\"*/ + directory.getName())) + File.separator/*"\\"*/ + node);
                origin = new BufferedInputStream(fi); // BUFFER;
                int curBUFFER = fi.available();
                byte data[] = new byte[curBUFFER];
                ZipEntry entry = new ZipEntry(node);
                origin.read(data);
                Adler32 crc32 = new Adler32();
                crc32.reset();
                crc32.update(data);
                entry.setCrc(crc32.getValue());
                entry.setSize(curBUFFER);
                entry.setMethod(ZipEntry.DEFLATED);

                out.putNextEntry(entry);

                System.out.println(entry.getName());
                out.write(data);
                out.closeEntry();
                origin.close();
                fi.close();
            }
            out.finish();
            out.close();
            dest.close();
            checksum.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getFilesTree(File directory) {
        return getFilesTree(directory, null);
    }

    public static List<String> getFilesTree(File directory, String extensionFilter) {
        List<String> tree = new LinkedList<>();
        String parent = "";
        if (directory.isDirectory()) {
            parent = directory.getName();
            List<File> subFiles = Arrays.asList(directory.listFiles());
            for (File subFile : subFiles) {
                List<String> subTree = getFilesTree(subFile, extensionFilter);
                for (String node : subTree) {
                    tree.add(parent + File.separator/*"\\"*/ + node);
                }
            }
        } else if (directory.isFile() && fileMatchesExtensionFilter(directory, extensionFilter)) {
            tree.add(directory.getName());
        }
        return tree;
    }

    public static List<String> getFiles(File directory, String extensionFilter) {
        List<String> result = new LinkedList<>();
        if (directory.isDirectory()) {
            List<File> subFiles = Arrays.asList(directory.listFiles());
            for (File subFile : subFiles) {
                if (subFile.isFile() && fileMatchesExtensionFilter(subFile, extensionFilter)) {
                    result.add(subFile.getName());
                }
            }
        }
        return result;
    }

    public static boolean fileMatchesExtensionFilter(File f, String extensionFilter) {
        if (f == null) {
            return false;
        }
        if (f.getName().length() < 5) {
            return false;
        }
        if (extensionFilter == null) {
            return true;
        }
        String extension = f.getName().substring(f.getName().length() - 3).toLowerCase();
        return extensionFilter.toLowerCase().contains(extension);
    }

    public static void main(String[] args) {
        File f = new File("../emls/eml");
        Compress(f, "eml", false);
        System.exit(333);
    }
}
