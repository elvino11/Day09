package com.example.day09.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREFS_NAME = "user_pref";

    //KEY
    private static final String USERNAME = "USERNAME";
    private static final String NAME = "NAME";
    private static final String ID = "ID";

    private static final String ISLOGIN = "ISLOGIN";

    //SharedPreferences
    private final SharedPreferences sharedPreferences;

    //Constructor
    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    //Menyimpan Data User
    public void setUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //KEY DAN VALUE
        //"USERNAME" DAN "TEST"
        editor.putString(USERNAME, user.username);
        editor.putString(NAME, user.name);
        editor.putString(ID, user.id);
        editor.putBoolean(ISLOGIN, user.isLogin);
        editor.apply();
    }

    //Mengambil Data User
    public User getUser() {
        User user = new User();
        user.setName(sharedPreferences.getString(NAME, ""));
        user.setUsername(sharedPreferences.getString(USERNAME, ""));
        user.setId(sharedPreferences.getString(ID, ""));
        user.setLogin(sharedPreferences.getBoolean(ISLOGIN, false));

        return user;
    }
}
