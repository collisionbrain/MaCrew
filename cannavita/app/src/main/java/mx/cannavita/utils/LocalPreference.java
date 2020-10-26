package mx.cannavita.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;

public class LocalPreference {

    private SharedPreferences preferences;

    public LocalPreference(Context context ){
        this.preferences  = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveData (String key,  String data){
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(key,data);
        editor.commit();
    }

    public String loadData(String key){
        return this.preferences.getString(key, null);
    }
    public void saveFlag (String key,  boolean data){
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putBoolean(key,data);
        editor.commit();
    }

    public boolean loadFlag(String key){
        return this.preferences.getBoolean(key,false);
    }


    public void saveDataObjet(String key,  Serializable data){
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(key, DataUtils.objectToString(data));
        editor.commit();
    }

    public Serializable loadDatObjet(String key){
        return DataUtils.stringToObject(this.preferences.getString(key, null));
    }



}
