
package day02.ex02;

import java.io.File;

class Cmd {
    private File dir;

    Cmd(String dirPath) throws IllegalArgumentException {
        dir = new File(dirPath);
        if (!dir.exists() || dir.isFile()) {
            throw new IllegalArgumentException("Invalid directory");
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
        System.out.println(pathName);
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
            File[] files = entity.listFiles();
            for (File file : files) {
                size += (file.length() / 1000);
            }

        }
        return size;

    }

    // get all size of all files in directories and subdirectories in dir
    private double getDirSize(File dir) {
        double size = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            } else if (file.isDirectory()) {
                size += getDirSize(file);
            }
        }
        return size;
    }

    public void ls() {
        File[] files = dir.listFiles();
        // try {

        for (File file : files) {
            try {

                if (file.isFile()) {
                    System.out.println(file.getName() + " " + getSize(file.getName()) + " KB");
                } else if (file.isDirectory()) {
                    System.out.println(file.getName() + " " + (int) (getDirSize(file) / 1000) + " KB");
                }
            } catch (Exception e) {
                if (e instanceof java.lang.NullPointerException) {
                    // safely ignore
                    System.err.println("Access denied");
                } else {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public void mv(String src, String dest) {
        File srcFile = new File(parasePathName(src));
        File destFile = new File(parasePathName(dest));
        // we need src, obviously
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
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

}