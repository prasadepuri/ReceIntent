package com.example.receveintent;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private Context context;
    private SharedPreferences sharedPreferences;

    public Preference(Context context)
    {
        this.context=context;
        getSharedPreference();
    }

    private void getSharedPreference()
    {
        sharedPreferences=context.getSharedPreferences(context.getString(R.string.my_preference),Context.MODE_PRIVATE);
    }
    public void writeSharedPreference()
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(context.getString(R.string.my_preference_key),"INIT_OK");

        editor.commit();
    }
    public void writeSharedPreferenceAsHeadLines(String topic)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("topic1",topic);
        editor.commit();
    }
    public void writeSharedPreferenceAsTechnology(String topic)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("topic2",topic);
        editor.commit();
    }
    public void writeSharedPreferenceAsSports(String topic)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("topic3",topic);
        editor.commit();
    }
    public void writeSharedPreferenceAsStudents(String topic)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("topic4",topic);
        editor.commit();
    }

    public String getHeadlinesPreference()
    {
        return sharedPreferences.getString("topic1","null");
    }
    public String getTechnologyPreference()
    {
        return sharedPreferences.getString("topic2","null");
    }
    public String getSportsPreference()
    {
        return sharedPreferences.getString("topic3","null");
    }
    public String getStudentPreference()
    {
        return sharedPreferences.getString("topic4","null");
    }

    public boolean checkPreference()
    {
        boolean status=false;
        if(sharedPreferences.getString(context.getString(R.string.my_preference_key),"null").equals("null"))
        {
            status=false;
        }
        else
        {
            status=true;
        }
        return status;
    }
    public void clearPreference()
    {
        sharedPreferences.edit().clear().commit();
    }

}
