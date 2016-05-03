package vn.edu.hcmut.uddd.common;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import vn.edu.hcmut.uddd.entity.Topic;
import vn.edu.hcmut.uddd.service.NotificationService;

/**
 * Created by TRAN VAN HEN on 3/2/2016.
 */
public class CommonUtil {

    public static String joinByColon(String... strings){
        return  CommonUtil.join(ConstCommon.COLON, strings);
    }

    public static String joinBySemicolon(String... strings){
        return  CommonUtil.join(ConstCommon.SEMICOLON, strings);
    }

    public static String join(String delimiter, String... strings){
        StringBuilder builder = new StringBuilder();
        boolean firstTime = true;
        for (String s : strings){
            if (firstTime){
                firstTime = false;
            }
            else{
                builder.append(delimiter);
            }
            builder.append(s);
        }
        return  builder.toString();
    }

    public static  String[] slipByColon(String string){
        return CommonUtil.split(string, ConstCommon.COLON);
    }

    public static String[] splitBySemicolon(String string){
        return CommonUtil.split(string, ConstCommon.SEMICOLON);
    }

    public static String[] split(String string, String delimiter){
        if (string == null || string.equals(ConstCommon.EMPTY)){
            return new String[0];
        }
        return string.split(delimiter, -1);
    }

    public static List<Integer> convertStringToList(String string){
        List<Integer> result = new ArrayList<>();
        String[] strings = CommonUtil.splitBySemicolon(string);
        for (String s : strings) {
            result.add(Integer.valueOf(s));
        }
        return result;
    }

    public static <T> ArrayList<T> convertFromArrayToList(T[] array){
        return new ArrayList<>(Arrays.asList(array));
    }

    public static String convertListToString(List<Integer> list){
        StringBuilder builder = new StringBuilder();
        boolean firstTime = true;
        for (Integer value : list){
            if (firstTime){
                firstTime = false;
            }
            else{
                builder.append(ConstCommon.SEMICOLON);
            }
            builder.append(String.valueOf(value));
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

    public static String getSharedPreferences(SharedPreferences preferences, String key, String defaultValue){
        return preferences.getString(key, defaultValue);
    }

    public static void setSharedPreferences(SharedPreferences preferences, String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
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

    public static String replaceString(String string) {
        if (string == null) {
            return ConstCommon.EMPTY;
        }
        else {
            return string.replace(ConstCommon.SEMICOLON, ConstCommon.SPACE).replace(ConstCommon.COLON, ConstCommon.SPACE).trim();
        }
    }

    public static String format(String string, String... args){
        return String.format(string, args);
    }

    public static CharSequence htmlBuilder(String html, String... args){
        return Html.fromHtml(String.format(html, args));
    }

    public static void logError(Object obj, Exception e){
        Log.e(obj.getClass().getSimpleName(), ConstCommon.EMPTY, e);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static int getDrawableByName(Context context, String name){
        return context.getResources().getIdentifier(name, ConstCommon.DRAWABLE_TYPE, context.getPackageName());
    }

    public static String readHtmlTemplate(Context context, String file) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(file), StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        boolean firstTime = true;
        String line;
        while ((line = reader.readLine()) != null){
            if (firstTime){
                firstTime = false;
            }
            else{
                builder.append(System.lineSeparator());
            }
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }

    public static void sendMailShare(Activity activity) throws Exception{
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, ConstCommon.EMAIL_SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(CommonUtil.readHtmlTemplate(activity, ConstCommon.MAIL_SHARE_TEMPLATE)));
        PackageManager manager = activity.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ConstCommon.GMAIL_PACKAGE, 0);
            intent.setPackage(info.packageName);
        }
        catch (PackageManager.NameNotFoundException e){}
        activity.startActivity(intent);
    }

    public static void shareFacebook(Activity activity) throws Exception{
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, ConstCommon.LINK_PLAY_STORE);
        PackageManager manager = activity.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ConstCommon.FACEBOOK_KATANA_PACKAGE, 0);
            intent.setPackage(info.packageName);
        }
        catch (PackageManager.NameNotFoundException e){
            try {
                PackageInfo info = manager.getPackageInfo(ConstCommon.FACEBOOK_LITE_PACKAGE, 0);
                intent.setPackage(info.packageName);
            }
            catch (PackageManager.NameNotFoundException e1){}
        }
        activity.startActivity(intent);
    }

    public static void rateApp(Activity activity) throws Exception{
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(ConstCommon.LINK_MARKET));
        activity.startActivity(intent);
    }

    public static void sendMailContact(Activity activity) throws Exception{
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ConstCommon.SYSTEM_EMAIL});
        intent.putExtra(Intent.EXTRA_SUBJECT, ConstCommon.EMAIL_SUBJECT);
        PackageManager manager = activity.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ConstCommon.GMAIL_PACKAGE, 0);
            intent.setPackage(info.packageName);
        }
        catch (PackageManager.NameNotFoundException e){}
        activity.startActivity(intent);
    }


    public static void startSchedule(Context context, int topic, int hour, int minute, boolean isSound){
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(Constants.PR_TOPIC_ID, topic);
        intent.putExtra(Constants.PR_SOUND, isSound);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ConstCommon.ALARM_MANAGER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void cancelSchedule(Context context){
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ConstCommon.ALARM_MANAGER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    public static String[] getList() {
        return new String[]{ ConstCommon.NOUNS, ConstCommon.PRONOUNS, ConstCommon.ADJECTIVE, ConstCommon.VERB,
                ConstCommon.ADVERB, ConstCommon.PREPOSITION, ConstCommon.CONJUNCTION, ConstCommon.INRERJECTION};
    }

    public static PartOfSpeech getPartOfSpeech(String input){
        switch (input){
            case ConstCommon.NOUNS:
                return PartOfSpeech.NOUNS;
            case ConstCommon.PRONOUNS:
                return PartOfSpeech.PRONOUNS;
            case ConstCommon.ADJECTIVE:
                return PartOfSpeech.ADJECTIVE;
            case ConstCommon.VERB:
                return PartOfSpeech.VERB;
            case ConstCommon.ADVERB:
                return PartOfSpeech.ADVERB;
            case ConstCommon.PREPOSITION:
                return PartOfSpeech.PREPOSITION;
            case ConstCommon.CONJUNCTION:
                return PartOfSpeech.CONJUNCTION;
            case ConstCommon.INRERJECTION:
                return PartOfSpeech.INTERJECTION;
            default:
                return null;
        }
    }

    public static int getPosition(PartOfSpeech partOfSpeech){
        if (partOfSpeech == null){
            return 0;
        }
        switch (partOfSpeech){
            case NOUNS:
                return 0;
            case PRONOUNS:
                return 1;
            case ADJECTIVE:
                return 2;
            case VERB:
                return 3;
            case ADVERB:
                return 4;
            case PREPOSITION:
                return 5;
            case CONJUNCTION:
                return 6;
            case INTERJECTION:
                return 7;
            default:
                return 0;
        }
    }

    public static void showError(Activity activity, String error, Exception e){
        CommonUtil.logError(activity, e);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(ConstCommon.ERROR_TITLE);
        builder.setMessage(CommonUtil.format(ConstCommon.ERROR_MESSAGE, error));
        builder.setCancelable(false);
        builder.setNegativeButton(ConstCommon.ERROR_BUTTON, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        builder.create().show();
    }

    public static String timeDisplay(int hour, int minute){
        if (hour == 12){
            return String.format(ConstCommon.TIME_FORMAT, hour, minute, "PM");
        }
        else if (hour > 12){
            return String.format(ConstCommon.TIME_FORMAT, hour - 12, minute, "PM");
        }
        else{
            return String.format(ConstCommon.TIME_FORMAT, hour, minute, "AM");
        }
    }

    public static Topic defaultTopic(){
        Topic topic = new Topic();
        topic.setId(0);
        topic.setMean(ConstCommon.EMPTY);
        topic.setName(ConstCommon.EMPTY);
        return topic;
    }

    public static void showPopup(Activity activity, boolean showSoftInput, CharSequence title, CharSequence message,
                                 View v, CharSequence positive, CharSequence negative,
                                 final CommonUtil.OnClickListener positiveListener,
                                 final CommonUtil.OnClickListener negativeListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(v);
        builder.setPositiveButton(positive, null);
        builder.setNegativeButton(negative, null);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    alert.dismiss();
                    return true;
                }
                return false;
            }
        });
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        positiveListener.onClick(alert);
                    }
                });
                alert.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        negativeListener.onClick(alert);
                    }
                });
            }
        });
        if (showSoftInput) {
            alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        alert.show();
    }

    public static void showPopup(final Activity activity, boolean showSoftInput, CharSequence title, CharSequence message,
                                 View v, CharSequence positive, CharSequence neutral, CharSequence negative,
                                 final CommonUtil.OnClickListener positiveListener,
                                 final CommonUtil.OnClickListener neutralListener,
                                 final CommonUtil.OnClickListener negativeListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(v);
        builder.setPositiveButton(positive, null);
        builder.setNegativeButton(negative, null);
        builder.setNeutralButton(neutral, null);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    alert.dismiss();
                    return true;
                }
                return false;
            }
        });
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        positiveListener.onClick(alert);
                    }
                });
                alert.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        negativeListener.onClick(alert);
                    }
                });
                alert.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        neutralListener.onClick(alert);
                    }
                });
            }
        });
        if (showSoftInput) {
            alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        alert.show();
    }

    public interface OnClickListener{
        void onClick(AlertDialog dialog);
    }
}
