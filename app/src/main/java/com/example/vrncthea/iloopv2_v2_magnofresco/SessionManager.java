package com.example.vrncthea.iloopv2_v2_magnofresco;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Vrnc Thea on 4/24/2017.
 */

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public static final String KEY_UID = "uid";
    public static final String KEY_SESSUSERNAME = "sessUsername";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERTYPE = "usertype";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(KEY_UID, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String sessUsername){
        editor.putString(KEY_SESSUSERNAME, sessUsername);
        editor.commit();
    }
    public void setUserType(String usertype){
        editor.putString(KEY_USERTYPE, usertype);
        editor.commit();
    }
    public HashMap<String, String> getLoginSession(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_SESSUSERNAME, pref.getString(KEY_SESSUSERNAME, null));
        user.put(KEY_UID, pref.getString(KEY_UID, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        return user;
    }
    public HashMap<String, String> getUserType(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USERTYPE, pref.getString(KEY_USERTYPE, null));
        return user;
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
    public void logoutStudent(){
        editor.clear();
        editor.commit();
    }

}
