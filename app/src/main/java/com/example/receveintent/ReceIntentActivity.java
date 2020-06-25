package com.example.receveintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReceIntentActivity extends AppCompatActivity {
    File fileDirectory;
    WebView webView;
    EditText editText;
    Button button;
    LinearLayout linearLayoutWithButton;
    String description,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rece_intent);
       // checkAndCreateDirectory();
        new DataFileHandling(this);
        receveData();
        WebView webView=(WebView)findViewById(R.id.webview);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }
    public void receveData()
    {
        Intent intent=getIntent();
        String action=intent.getAction();
        String type=intent.getType();
        if(Intent.ACTION_SEND.equals(action)&& type!=null)
        {
            if(type.equals("text/plain"))
            {
                String txt=intent.getStringExtra(Intent.EXTRA_TEXT);
                linearLayoutWithButton=findViewById(R.id.linearLayoutWithBtn);
                new MyReferenceDB(this).checkIfRecordExistInClould(txt,linearLayoutWithButton);
                displayContentOnScreen(txt);
            }
        }
    }
    public void displayContentOnScreen(String txt)
    {
        TextView t=(TextView)findViewById(R.id.first);
        t.setText(txt);
        this.setTitle("Loading......");
        button=findViewById(R.id.ebutton);
        button.setEnabled(false);
        button.setText("Please wait.....");
        openBroser(txt);
    }

    private void openBroser(String txt) {
        webView=(WebView)findViewById(R.id.webview);
        webView.loadUrl(txt);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView,String url)
            {
                ReceIntentActivity.this.setTitle(webView.getTitle());
                button.setEnabled(true);
                button.setText("ADD");

            }

        });

    }

    private void checkAndCreateDirectory() {
        fileDirectory=new File(this.getFilesDir(),"text");
        if(!fileDirectory.exists())
        {
            fileDirectory.mkdir();
            Toast.makeText(getApplicationContext(),"created directory",Toast.LENGTH_LONG).show();
        }
    }

   /* public void files()
    {
        JSONObject jsonObject=new JSONObject();
        jsonObject=formatDataToWriteIntoFile();
        try
        {
            File http=new File(fileDirectory,"share");
            FileWriter obj=new FileWriter(http,true);
            obj.append(jsonObject+"\n");
            obj.flush();
            obj.close();
            result("Success");
        } catch (IOException e) {
            result("failed");
            e.printStackTrace();
        }
    }*/

    private JSONObject formatDataToWriteIntoFile() {
        JSONObject object=new JSONObject();
        try {
            object.put("url",webView.getUrl());
            object.put("title",webView.getTitle());
            object.put("timestamp",new Date().getTime());
            object.put("description",description);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return object;
    }
    private void formatAndWriteDataIntoFirebase() {
        Map<String,Object> object=new HashMap<>();
        try {
            object.put("url",webView.getUrl());
            object.put("title",webView.getTitle());
            object.put("timestamp",new Date().getTime());
            object.put("description",description);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyReferenceDB myReferenceDB=new MyReferenceDB(getApplicationContext());
        myReferenceDB.writeDataIntoDb(object);
    }

    public void result(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    public void gotoList(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void gotoPrevious(View view) {
       // Intent intent=new Intent();
      //  setResult(RESULT_OK,intent);
        finish();
    }

    public void addToDataBase(View view) {
        editText=findViewById(R.id.etext);
        description=editText.getText().toString();
       // files();
        formatAndWriteDataIntoFirebase();

    }
}
