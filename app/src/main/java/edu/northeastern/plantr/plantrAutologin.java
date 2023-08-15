package edu.northeastern.plantr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class plantrAutologin
{
    static final String PREF_USERNAME = "username";
    static final String PREF_FIRST_NAME = "firstName";
    static final String PREF_LAST_NAME = "lastName";
    static final String PREF_FAVE_PLANT = "favePlant";
    static final String PREF_LAST_ACTIVITY = "lastActivity";

    static final String PREF_IDENTIFIER = "identifier";
    static final String USER_ID = "userID";


    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserID(Context ctx, String userID){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_ID, userID);
        editor.apply();

    }

    // SETTERS
    public static void setUsername(Context ctx, String username) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USERNAME, username);
        editor.apply();
    }
    public static void setFirstName(Context ctx, String firstName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_FIRST_NAME, firstName);
        editor.apply();
    }
    public static void setLastName(Context ctx, String lastName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LAST_NAME, lastName);
        editor.apply();
    }
    public static void setFavePlant(Context ctx, String favePlant) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_FAVE_PLANT, favePlant);
        editor.apply();
    }
    public static void setLastActivity(Context ctx, String lastActivity) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LAST_ACTIVITY, lastActivity);
        editor.apply();
    }

    public static void setPrefIdentifier(Context ctx, String lastActivity){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_IDENTIFIER, lastActivity);
        editor.apply();
    }

    // GETTERS
    public static String getUsername(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USERNAME, "");
    }
    public static String getFirstName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_FIRST_NAME, "");
    }
    public static String getLastName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LAST_NAME, "");
    }
    public static String getFavePlant(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_FAVE_PLANT, "");
    }
    public static String getLastActivity(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_LAST_ACTIVITY, "");
    }
    public static String getPrefIdentifier(Context ctx){
        return getSharedPreferences(ctx).getString(PREF_IDENTIFIER, "");
    }

    public static String getUserId(Context ctx){
        return getSharedPreferences(ctx).getString(USER_ID, "");
    }
}