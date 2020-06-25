package com.example.receveintent;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class DataFileHandling {
    Context applicationContex;
    public File fileDirectory;

    DataFileHandling(Context applicationContex) {
        this.applicationContex = applicationContex;
        checkAndCreateDirectory();
    }

    private void checkAndCreateDirectory() {
        fileDirectory = new File(applicationContex.getFilesDir(), "text");
        if (!fileDirectory.exists()) {
            fileDirectory.mkdir();
            Toast.makeText(applicationContex, "Created directory", Toast.LENGTH_SHORT).show();
        }
    }

    public void files(String url, String title, String description) {
        JSONObject jsonObject = new JSONObject();
        jsonObject = formatDataToWriteIntoFile(url, title, description);
        try {
            File http = new File(fileDirectory, "share");
            FileWriter obj = new FileWriter(http, true);
            obj.append(jsonObject + "\n");
            obj.flush();
            obj.close();
            result("Success");
        } catch (IOException e) {
            result("failed");
            e.printStackTrace();
        }
    }

    public void result(String msg) {
        Toast.makeText(applicationContex, msg, Toast.LENGTH_LONG).show();
    }

    private JSONObject formatDataToWriteIntoFile(String url, String title, String description) {
        JSONObject object = new JSONObject();
        try {
            object.put("url", url);
            object.put("title", title);
            object.put("timestamp",new Date().getTime());
            object.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return object;
    }

    public String getFileDirectory() {
        return fileDirectory.getAbsolutePath();
    }
    public boolean checkIfRecordExist(String url)
    {
        String line;
        boolean result=false;
        try {
            //FileReader fileReader=new FileReader(fileDirectory.getAbsolutePath()+"/share");
            //Log.d("filedirectoyinfo",fileDirectory.getAbsolutePath());
            FileReader fileReader=new FileReader(getFileDirectory()+"/share");
            Log.d("filedirectoyinfo",getFileDirectory());
            BufferedReader obj=new BufferedReader(fileReader);
            while((line=obj.readLine())!=null)
            {

                JSONObject jsonObject=new JSONObject(line);
                String objUrl=jsonObject.getString("url");
                if(objUrl.equals(url))
                {
                    result=true;
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}

