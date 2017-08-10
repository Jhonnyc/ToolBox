package com.yonikal.tools;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yoni on 10/08/2017.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();
    private static final String LOG_DIR_NAME = "Loggs";


    public synchronized static void saveFile(final String fileName, final Context context, final byte[] data, final boolean saveOnSdCard) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream fos = null;
                    try {
                        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                        File file = getWritableFile(context, fileName, saveOnSdCard);
                        if (file != null) {
                            fos = new FileOutputStream(file.getPath());
                            fos.write(data);
                            fos.close();
                        } else {
                            Log.e(TAG, "Destination file is null");
                            throw new NullPointerException();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Exception when trying to write to file. details - " + e.getMessage() + " cause - " + e.getCause());
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                                fos = null;
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, "Exception when trying to write to file." + e.getCause().toString());
        }
    }

    public static File getWritableFile(Context context, String fileName, boolean onSdCard) {
        File file = null;
        try {
            if (!onSdCard) {
                file = new File(context.getFilesDir(), fileName);
            } else {
                String logDirectory = Environment.getExternalStorageDirectory() + File.separator + LOG_DIR_NAME;
                File dir = new File(logDirectory);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                file = new File(dir, fileName);
            }

            if (!file.exists()) {
                file.createNewFile();
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to create file with excpetion message "
                    + e.getMessage() + " and cause "
                    + e.getCause());
        }

        return file;
    }

    public static File getReadableFile(Context context, String fileName, boolean onSdCard) {
        File file = null;
        try {
            if (!onSdCard) {
                file = new File(context.getFilesDir(), fileName);
            } else {
                String logDirectory = Environment.getExternalStorageDirectory() + File.separator + LOG_DIR_NAME;
                File dir = new File(logDirectory);
                file = new File(dir, fileName);
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to read file with excpetion message "
                    + e.getMessage() + " and cause "
                    + e.getCause());
        }

        return file;
    }

    public static String getFileData(Context context, String fileName) {
        StringBuilder sb = new StringBuilder();
        File file = FileUtils.getReadableFile(context, fileName, false);
        if (file != null && file.exists()) {
            try {
                InputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String data = reader.readLine();
                while (data != null) {
                    sb.append(data).append("\n");
                    data = reader.readLine();
                }
            } catch (Exception e) {
                Log.e(TAG, "FileUtils could not read file content");
            }
        }

        return sb.toString();
    }

    public static boolean copyFile(String source, File dest) {
        try {
            File sourceFile = new File(source);
            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int read = 0;
            while (read != -1) {
                read = fis.read(buffer);
                if (read != -1) fos.write(buffer);
            }
            fis.close();
            fos.close();
            fos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteFile(Uri fileUri) {
        File file = null;
        try {
            file = new File(fileUri.toString());
            if (!file.exists()) {
                Log.e(TAG, "Failed to delete file, file not found");
                return;
            }

            if (!file.delete()) {
                throw new IllegalStateException("Failed to delete file");
            }

        } catch (Exception e) {
            Log.e(TAG, "Failed to delete file with excpetion message "
                    + e.getMessage() + " and cause "
                    + e.getCause());
        }
    }
}
