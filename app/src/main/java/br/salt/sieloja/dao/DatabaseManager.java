package br.salt.sieloja.dao;

import android.content.Context;

public class DatabaseManager {

    private static DatabaseManager instance;
    private DatabaseHelper helper;

    public DatabaseManager(Context context) {
        helper = new DatabaseHelper(context);
    }

    public static void init(Context context){
        if(instance==null) instance = new DatabaseManager(context);
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public DatabaseHelper getHelper() {
        return helper;
    }
}
