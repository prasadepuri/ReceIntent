package com.example.receveintent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    File fileDirectory;
    String selecteUrl;
    ListView list;
    SimpleAdapter simpleAdapter;
     public MyReferenceRepositary myReferenceRepositary;
    List<HashMap<String,Object>> fdata=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkAndCreateDirectory();
        myReferenceRepositary=new MyReferenceRepositary(getApplication());
        //new DataFileHandling(this);
        setListnerForRecevingNewData();
        //loadDataToListView();
        //myMethodforRetrevingdata();
    }

    private void setListnerForRecevingNewData() {
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            db.collection("MyReferenceDB").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges())
                    {

                        if(doc.getType().equals(DocumentChange.Type.ADDED)) {

                            Log.d("info of process",doc.getDocument().getData().toString());
                            String title, description, url;
                            long timestamp =(long) (doc.getDocument().getData().get("timestamp"));
                            title = (String) (doc.getDocument().getData().get("title"));
                            description = (String) (doc.getDocument().getData().get("description"));

                            url = (String) (doc.getDocument().getData().get("url"));
                           /* if (!(new DataFileHandling(MainActivity.this).checkIfRecordExist(url)))
                            {
                                new DataFileHandling(MainActivity.this).files(url,title,description);
                            }
                            else
                            {
                                Log.d("duplicate record",url);
                            }*/
                           formatDataAndWriteIntoSqliteDataBase(url,title,timestamp,description);

                        }
                    }
                    loadDataToListView();
                }
            });
        }

    private  List<HashMap<String,Object>> readFromSqLiteDataBase() {
        List<MyReference> myReferences=new ArrayList<MyReference>();
        List<HashMap<String,Object>> myReferenceList=new ArrayList<>();
        try {
                myReferences=new GetUsersAsyncTask().execute().get();
                for(MyReference ref:myReferences)
                {
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("title",ref.getTitle());
                    map.put("url",ref.getUrl());
                    map.put("timestamp",ref.getTimestamp());
                    map.put("description",ref.getDescription());
                    map.put("timestampString",converToNormalTime(ref.getTimestamp()));
                    myReferenceList.add(map);
                }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return myReferenceList;
    }

    private void formatDataAndWriteIntoSqliteDataBase(String url, String title, long timestamp, String description) {
        MyReference myReference=new MyReference(title,url,description,timestamp);
        myReferenceRepositary.insert(myReference);
        Toast.makeText(getApplicationContext(),"Data inserted into table",Toast.LENGTH_LONG).show();
    }


    private void checkAndCreateDirectory() {
        fileDirectory=new File(this.getFilesDir(),"text");
        if(!fileDirectory.exists())
        {
            fileDirectory.mkdir();
            Toast.makeText(getApplicationContext(),"created directory",Toast.LENGTH_LONG).show();
        }
    }
    private class GetUsersAsyncTask extends AsyncTask<Void, Void, List<MyReference>>
    {
        @Override
        protected List<MyReference> doInBackground(Void... url) {
            return myReferenceRepositary.getAllData();

        }
    }

    public void loadDataToListView()
    {

        //List<HashMap<String,Object>> ndata=new ArrayList<>();
        //ndata=getDataFromFile();
        //fdata=ndata;
        formatListView(readFromSqLiteDataBase());

    }

    /*private List<HashMap<String,Object>>getDataFromFile() {
        String line=null;
        List<HashMap<String,Object>> dataMapList=new ArrayList<>();
        //List<String> data=new ArrayList<>();
        try {
            //FileReader fileReader=new FileReader(fileDirectory.getAbsolutePath()+"/share");
            FileReader fileReader=new FileReader(new DataFileHandling(this).getFileDirectory()+"/share");
           // Log.d("filedirectoyinfo",fileDirectory.getAbsolutePath());
            BufferedReader obj=new BufferedReader(fileReader);
            while((line=obj.readLine())!=null)
            {
                HashMap<String,Object> result=new HashMap<>();
                result=convertJsonStringIntoMap(line);
                dataMapList.add(result);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMapList;
    }*/

    private void sortList() {
        Comparator<Map<String,Object>> mapComparator=new Comparator<Map<String,Object>>() {
            @Override
            public int compare(Map<String, Object> m1, Map<String,Object> m2) {
                long t1,t2;
                t1= (long) m1.get("timestamp");
                t2= (long) m2.get("timestamp");
                return (int) (t2 - t1);
            }
        };
        Collections.sort(fdata,mapComparator);
    }

    /*private HashMap<String,Object> convertJsonStringIntoMap(String line) {
        HashMap<String,Object> map=new HashMap<>();
        try {
            JSONObject jsonObject=new JSONObject(line);
            long date=jsonObject.getLong("timestamp");
            String dateTime=converToNormalTime(date);
            map=new HashMap<>();
            map.put("url",jsonObject.getString("url"));
            map.put("title",jsonObject.getString("title"));
            map.put("timestamp",jsonObject.getLong("timestamp"));
            map.put("timestampString",dateTime);
            map.put("description",jsonObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }*/

    private String converToNormalTime(long timestamp) {
        //convert seconds to milliseconds
        Date date = new Date(timestamp);
        // format of the date
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
       //jdf.setTimeZone(TimeZone.getDefault());
        String java_date = jdf.format(date);
        System.out.println("\n"+java_date+"\n");
        return java_date;
    }

    public void formatListView(List<HashMap<String,Object>> data)
    {
        //ArrayAdapter adapter = new ArrayAdapter<String>(this,
                //R.layout.layout_listview_item,R.id.content,data);
        String[] mapKeys=new String[]{
                "url","title","timestampString","description"
        };
        int[] listItemChildViews=new int[]{
                R.id.contentUrl,R.id.contentTitle,R.id.contentTimeStamp,R.id.contentDescription
        };
        simpleAdapter=new SimpleAdapter( this,data,R.layout.layout_listview_item,mapKeys,listItemChildViews);

        ListView list=(ListView)findViewById(R.id.flist);
        list.setAdapter(simpleAdapter);
        searchForLink();
        sortList();
        fdata=data;
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecteUrl=geturlFromList(fdata,position);
                displayAlertDialogForChoosing();
            }

            });

    }

    private void searchForLink() {
        SearchView searchView=findViewById(R.id.searchList);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(fdata.contains(query))
                {
                    simpleAdapter.getFilter().filter(query);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No Match found",Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                simpleAdapter.getFilter().filter(newText);
                return false;
            }

        });
    }

    public void displayAlertDialogForChoosing()
    {
        String[] items={"open in new Browser","Open in this app"
        };
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setItems(items,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0)
                {
                    openInNewBroswer(selecteUrl);
            }
                else if(which==1)
                {
                    openUrlInNewScreen(selecteUrl);
                }
        };
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }

    private void openInNewBroswer(String selecteUrl) {
        Intent i=new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(selecteUrl));
        startActivity(i);
    }

    public void openUrlInNewScreen(String itemUrl) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(itemUrl));
    }

    public String geturlFromList(List<HashMap<String, Object>> listData, int position)
    {
        HashMap<String,Object> item=new HashMap<>();
        item=listData.get(position);
        String url=item.get("url").toString();
        return url;
    }

}
