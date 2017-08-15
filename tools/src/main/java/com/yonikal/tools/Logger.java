package com.yonikal.tools;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by yoni on 15/08/2017.
 */
public class Logger {

    private static boolean sIsDebug = true;
    private static int sLogLevel = Log.VERBOSE;

    private static Logger.Delegate sCallback;

    public static void setCallback(Logger.Delegate callback) {
        sCallback = callback;
    }

    public static void json(String tag, JSONObject jsonObject) {
        if (sLogLevel <= Log.DEBUG && sIsDebug) {
            if (sCallback != null) {
                sCallback.json(tag, jsonObject);
            } else {
                Log.e(tag, jsonObject.toString());
            }
        }
    }

    public static void debug(String tag, String text) {
        if (sLogLevel <= Log.DEBUG && sIsDebug) {
            if (sCallback != null) {
                sCallback.debug(tag, text);
            } else {
                Log.d(tag, text);
            }
        }
    }

    public static void info(String tag, String text) {
        if (sLogLevel <= Log.INFO && sIsDebug) {
            if (sCallback != null) {
                sCallback.info(tag, text);
            } else {
                Log.i(tag, text);
            }
        }
    }

    public static void warning(String tag, String text) {
        if (sLogLevel <= Log.WARN && sIsDebug) {
            if (sCallback != null) {
                sCallback.warning(tag, text);
            } else {
                Log.w(tag, text);
            }
        }
    }

    public static void error(String tag, String text) {
        if (sLogLevel <= Log.ERROR && sIsDebug) {
            if (sCallback != null) {
                sCallback.error(tag, text);
            } else {
                Log.e(tag, text);
            }
        }
    }

    public static void info(String tag, String format, Object... args) {
        if (sLogLevel <= Log.INFO && sIsDebug) {
            if (sCallback != null) {
                sCallback.info(tag, format, args);
            } else {
                Log.i(tag, String.format(format, args));
            }
        }
    }

    public static void debug(String tag, String format, Object... args) {
        if (sLogLevel <= Log.DEBUG && sIsDebug) {
            if (sCallback != null) {
                sCallback.debug(tag, format, args);
            } else {
                Log.d(tag, String.format(format, args));
            }
        }
    }

    public static void warning(String tag, String format, Object... args) {
        if (sLogLevel <= Log.WARN && sIsDebug) {
            if (sCallback != null) {
                sCallback.warning(tag, format, args);
            } else {
                Log.w(tag, String.format(format, args));
            }
        }
    }

    public static void error(String tag, String format, Object... args) {
        String data;
        try {
            data = String.format(format, args);
        } catch (Exception e) {
            String argsData = "";
            for (Object o : args) {
                argsData += " " + o.toString();
            }
            data = format + " " + argsData;
        }
        if (sLogLevel <= Log.ERROR && sIsDebug) {
            if (sCallback != null) {
                sCallback.error(tag, format, args);
            } else {
                Log.e(tag, data);
            }
        }
    }

    public interface Delegate {
        void json(String tag, JSONObject object);

        void debug(String tag, String msg);

        void info(String tag, String msg);

        void warning(String tag, String msg);

        void error(String tag, String msg);

        void info(String tag, String msg, Object... objects);

        void debug(String tag, String msg, Object... objects);

        void warning(String tag, String msg, Object... objects);

        void error(String tag, String msg, Object... objects);

        void logException(String tag, Throwable ex);
    }
}