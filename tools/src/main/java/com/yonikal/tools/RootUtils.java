package com.yonikal.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by yoni on 09/08/2017.
 */
public class RootUtils {

    public static boolean isRooted() {
        return methodTwo();
    }

    /**
     * This method tries to get super user abilities - if achieved try and use the 'pwd'
     * Command then exit (linux terminal)
     *
     * @return A boolean answer, true means this device is rooted.
     */
    private static boolean methodOne() {
        try {
            Process p = Runtime.getRuntime().exec("su", null, new File("/"));
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("pwd\n");
            os.writeBytes("exit\n");
            os.flush();
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * This method tries three different methods to check for root
     * 1. use the build config
     * 2. Try and access 'su' files
     * 3.
     * Command then exit (linux terminal)
     *
     * @return A boolean answer, true means this device is rooted.
     */
    private static boolean methodTwo() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    /**
     * A method to try and access different root places, if this method succeeds then the device is marked as rooted
     *
     * @return True if the device is rooted false otherwise
     */
    private static boolean methodThree() {
        boolean hasSuFile = false;
        if (!hasSuFile) {
            String[] places = {"/system/xbin/"};
//            {"/sbin/", "/system/bin/", "/data/local/xbin/", "/data/local/bin/",
//                    "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                File suFile = new File(where + "su");
                if (suFile.exists()) {
                    hasSuFile = true;
                    break;
                }
            }
        }

        return hasSuFile;
    }

    private static boolean checkRootMethod1() {
        try {
            String buildTags = android.os.Build.TAGS;
            return buildTags != null && buildTags.contains("test-keys");
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkRootMethod2() {
        try {
            String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
                    "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su",
                    "/system/sd/xbin/su", "/system/bin/failsafe/su",
                    "/data/local/su", "/su/bin/su"};
            for (String path : paths) {
                if (new File(path).exists()) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

}
