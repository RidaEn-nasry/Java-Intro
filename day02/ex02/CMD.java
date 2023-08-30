
package day02.ex02;

import java.io.File;

class Cmd {
    private File dir;

    Cmd(String dirPath) throws IllegalArgumentException {
        try {
            dir = getDirAsFile(dirPath);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getDirPath() {
        return dir.getAbsolutePath();
    }

    private String parasePathName(String pathName) {
        String newPathName = pathName;
        if (pathName.startsWith("~/")) {
            newPathName = System.getProperty("user.home") + pathName.substring(1);
        } else if (pathName.startsWith("~")) {
            newPathName = System.getProperty("user.home") + pathName.substring(1);
        } else if (pathName.startsWith("./")) {
            newPathName = dir.getAbsolutePath() + pathName.substring(1);
        } else if (pathName.startsWith("../")) {
            newPathName = dir.getParent() + pathName.substring(2);
        } else if (pathName.startsWith("..")) {
            newPathName = dir.getParent() + pathName.substring(2);
        } else if (pathName.startsWith("/")) {
            newPathName = pathName;
        } else {
            newPathName = dir.getAbsolutePath() + "/" + pathName;
        }
        return newPathName;
    }

    // if dir is valid, return a File obj of it. else throw
    private File getDirAsFile(String pathName) {
        pathName = parasePathName(pathName);
        File diroctory = new File(pathName);
        if (!diroctory.exists() || diroctory.isFile()) {
            throw new IllegalArgumentException("Invalid directory");
        }
        return diroctory;
    }

    private int getSize(String pathName) {
        File entity = new File(dir.getAbsolutePath() + "/" + pathName);
        int size = 0;
        if (entity.isFile()) {
            size = (int) (entity.length() / 1000.0);
        } else if (entity.isDirectory()) {
            try {
                File[] files = entity.listFiles();
                for (File file : files) {
                    size += (file.length() / 1000);
                }
            } catch (Exception e) {
                if (e instanceof java.lang.NullPointerException) {
                    // safely ignore, System.err.println("Access denied");
                    // System.out.println(pathName);
                } else {
                    System.err.println(e.getMessage());
                }
            }
        }
        return size;
    }

    public void ls() {
        String[] pathNames = dir.list();
        for (String pathName : pathNames) {
            System.out.println(pathName + " " + (int) getSize(pathName) + " KB");
        }
    }

    public void mv(String src, String dest) {
        File srcFile = new File(parasePathName(src));
        File destFile = new File(parasePathName(dest));
        // file to move to
        if (!srcFile.exists()) {
            System.err.println("Source does not exist");
            return;
        }
        // if src if current dir, do nothing
        if (srcFile.getAbsolutePath().equals(dir.getAbsolutePath())) {
            System.err.println("Cannot move current directory");
            return;
        }
        try {

            if (srcFile.isFile() && !destFile.isDirectory()) {
                srcFile.renameTo(destFile);
            } else if (srcFile.isFile() && destFile.isDirectory()) {
                srcFile.renameTo(new File(destFile.getAbsolutePath() + "/" + srcFile.getName()));
            } else if (srcFile.isDirectory() && destFile.isFile()) {
                System.err.println("Cannot move directory to file");
            } else if (srcFile.isDirectory() && destFile.isDirectory()) {
                srcFile.renameTo(new File(destFile.getAbsolutePath() + "/" + srcFile.getName()));
            } else if (srcFile.isDirectory() && !destFile.exists()) {
                srcFile.renameTo(destFile);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void cd(String newPath) {
        try {
            dir = getDirAsFile(newPath);
            System.out.println(dir.getAbsolutePath());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

}