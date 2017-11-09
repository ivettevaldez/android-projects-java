package com.silviavaldez.serviceexample.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Silvia Valdez on 7/26/17.
 */

public final class LogFileHelper {

    private static final int KILOBYTE = 1024;
    private static final int MAX_SIZE_MB = 20;


    /******************** CONSTRUCTOR ********************/

    protected LogFileHelper() {
        // Nothing to do here.
    }

    /******************** PUBLIC METHODS ********************/

    public static void writeLog(String message) {
        try {
            File logFile = getLogFile();

            // BufferedWriter for performance, true to set append to file flag
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            bufferedWriter.append(String.format("%s %s", getTimeStamp(), message));
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
//            UtilHelper.showToast(mContext, e.toString());
            e.printStackTrace();
        }
    }

    /******************** PRIVATE METHODS ********************/

    private static File getLogFile() {
        File directory = new File(String.format("%s/silviavaldez",
                android.os.Environment.getExternalStorageDirectory().getAbsolutePath()));
        File logFile = new File(String.format("%s/log.txt", directory));

        try {
            if (!directory.exists()) {
                directory.mkdirs();
            } else {
                if (isDirectoryTooHeavy(directory)) {
                    deleteDirectory(directory);
                    directory.mkdirs();
                }
            }
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
//            UtilHelper.showToast(mContext, e.toString());
            e.printStackTrace();
        }

        return logFile;
    }

    private static boolean isDirectoryTooHeavy(File file) {
        long fileSize = getDirectorySize(file) / KILOBYTE;  // Call function and convert bytes into KB
        return (fileSize / KILOBYTE) >= MAX_SIZE_MB; // Is too heavy when size is 20 MB or more
    }

    private static long getDirectorySize(File directory) {
        // This function will return size in form of bytes
        long size = 0;
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                size += getDirectorySize(file);
            }
        } else {
            size = directory.length();
        }
        return size;
    }

    private static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            String[] children = directory.list();
            for (String child : children) {
                new File(directory, child).delete();
            }
        }
    }

    private static String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

}
