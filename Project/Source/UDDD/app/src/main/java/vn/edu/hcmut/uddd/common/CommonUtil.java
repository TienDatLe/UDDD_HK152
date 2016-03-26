package vn.edu.hcmut.uddd.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TRAN VAN HEN on 3/2/2016.
 */
public class CommonUtil {

    public static String joinByColon(String... strings){
        return  join(ConstCommon.COLON, strings);
    }

    public static String joinBySemicolon(String... strings){
        return  join(ConstCommon.SEMICOLON, strings);
    }

    public static String join(String delimiter, String... strings){
        StringBuilder bd = new StringBuilder();
        int i = 1;
        for (String s : strings){
            bd.append(s);
            if (i < strings.length){
                bd.append(delimiter);
                i++;
            }
        }
        return  bd.toString();
    }

    public static  String[] slipByColon(String string){
        return split(string, ConstCommon.COLON);
    }

    public static String[] splitBySemicolon(String string){
        return split(string, ConstCommon.SEMICOLON);
    }

    public static String[] split(String string, String delimiter){
        return string.split(delimiter);
    }

    public static List<Integer> convertStringToList(String string){
        List<Integer> result = new ArrayList<>();
        if (string != null && !string.isEmpty()) {
            String[] strings = splitBySemicolon(string);
            for (String s : strings) {
                result.add(Integer.valueOf(s));
            }
        }
        return result;
    }

    public static String convertListToString(List<Integer> list){
        StringBuilder builder = new StringBuilder();
        int i = 1;
        for (Integer value : list){
            builder.append(String.valueOf(value));
            if (i < list.size()){
                builder.append(ConstCommon.SEMICOLON);
                i++;
            }
        }
        return builder.toString();
    }

    public static int getSharedPreferences(SharedPreferences preferences, String key, int defaultValue){
        return preferences.getInt(key, defaultValue);
    }

    public static void setSharedPreferences(SharedPreferences preferences, String key, int value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getSharedPreferencesForTopic(SharedPreferences preferences, String key, String table, int topicId, int defaultValue){
        return preferences.getInt(key + table + String.valueOf(topicId), defaultValue);
    }

    public static void setSharedPreferencesForTopic(SharedPreferences preferences, String key, String table, int topicId, int value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key + table + String.valueOf(topicId), value);
        editor.apply();
    }

    public static String getSharedPreferences(SharedPreferences preferences, String key, String defaultValue){
        return preferences.getString(key, defaultValue);
    }

    public static void setSharedPreferences(SharedPreferences preferences, String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSharedPreferencesForTopic(SharedPreferences preferences, String key, String table, int topicId, String defaultValue){
        return preferences.getString(key + table + String.valueOf(topicId), defaultValue);
    }

    public static void setSharedPreferencesForTopic(SharedPreferences preferences, String key, String table, int topicId, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key + table + String.valueOf(topicId), value);
        editor.apply();
    }

    public static boolean getSharedPreferences(SharedPreferences preferences, String key, boolean defaultValue){
        return preferences.getBoolean(key, defaultValue);
    }

    public static void setSharedPreferences(SharedPreferences preferences, String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String replaceString(String string){
        return string.replace(ConstCommon.SEMICOLON, ConstCommon.SPACE).replace(ConstCommon.COLON, ConstCommon.SPACE);
    }

    public static String sqlBuilder(String sql, String... args){
        return String.format(sql, args);
    }

    public static String readMailTemplate(Context context, String file) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(file), StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line = reader.readLine();
        if (line != null){
            builder.append(line);
            while ((line = reader.readLine()) != null){
                builder.append(System.lineSeparator());
                builder.append(line);
            }
        }
        reader.close();
        return builder.toString();
    }

    public static void sendMailShare(List<String> address) throws Exception{
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address.toArray(new String[address.size()]));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

    }

    public static void logError(Object obj, Exception e){
        Log.e(obj.getClass().getSimpleName(), ConstCommon.EMPTY, e);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
