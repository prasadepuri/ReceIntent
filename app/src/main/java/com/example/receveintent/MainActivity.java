package com.example.receveintent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    boolean headLinesStatus=true,technologyStatus=false,studentStatus=false,sportsLinesStatus=false;

    public RecyclerView recyclerView;
    public ProgressDialog progressDialog;
    Adapter radapter;
    RecyclerView.LayoutManager layoutManager;
    String subcribeToHeadlines="HeadLines",subscribeToTechnology="",subscribeToSports="",subscribeToStudents="";
    ListView list;
    static String shareTopicValue;
    SimpleAdapter simpleAdapter;
     public MyReferenceRepositary myReferenceRepositary;
    List<HashMap<String,Object>> fdata=new ArrayList<>();
    ArrayList<ArticlesInfo> articlesInfoArrayList=new ArrayList<>();
    public static String getValue() {
        return shareTopicValue;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        articlesInfoArrayList.clear();
        recyclerView = findViewById(R.id.recycleview);
        progressDialog=new ProgressDialog(this);
        recyclerView.setHasFixedSize(true);

        //checkAndCreateDirectory();
//        new MyFirebaseMessageInit().initProcess(getApplicationContext());
        myReferenceRepositary=new MyReferenceRepositary(getApplication());
        //getting the toolbar
        //new DataFileHandling(this);
        //setListnerForRecevingNewData();
        setListnerForRecevingNewTestingData();


        EditText editTextT=(EditText)findViewById(R.id.simpleSearchViewT);
        editTextT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        if(!new Preference(getApplicationContext()).checkPreference())
        {
            showPopupMenuForChoosingNotificationTopic();
            new Preference(getApplicationContext()).writeSharedPreference();
        }
        //loadDataToListView();
        //myMethodforRetrevingdata();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_with_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void showPopupMenuForChoosingNotificationTopic() {
        // setup the alert builder

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
        builder.setTitle(Html.fromHtml("<font color='#017E05'>Choose an topic for geting latest articles notifications</font>"));// add a radio button list
        String[] topics = {"HeadLines", "Technology", "Sports", "Student"};
        boolean checkedItem[] = {true,false,false,false}; // cow
        builder.setMultiChoiceItems(topics, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (which)
                {
                    case 0:

                        if(headLinesStatus) {
                            subcribeToHeadlines="";
                            Toast.makeText(MainActivity.this, "UnSelected Headlines", Toast.LENGTH_SHORT).show();
                            headLinesStatus=false;
                            break;
                        }
                        else{

                            subcribeToHeadlines= "HeadLines";
                            //new Preference(MainActivity.this).writeSharedPreferenceTopic(selectedTopic);
                            Toast.makeText(MainActivity.this, "Selected Headlines", Toast.LENGTH_SHORT).show();
                            headLinesStatus=true;
                            break;
                        }
                    case 1:
                        if(technologyStatus)
                        {
                            subscribeToTechnology="";
                            Toast.makeText(MainActivity.this, "UnSelected Technology", Toast.LENGTH_SHORT).show();
                            technologyStatus=false;
                            break;
                        }
                        else {
                            subscribeToTechnology = "Technology";
                            // new Preference(MainActivity.this).writeSharedPreferenceTopic(selectedTopic);
                            Toast.makeText(MainActivity.this, "Selected Technology", Toast.LENGTH_SHORT).show();
                            technologyStatus=true;
                            break;
                        }

                    case 2:
                        if(sportsLinesStatus)
                        {
                            subscribeToSports="";
                            Toast.makeText(MainActivity.this, "UnSelected Sports", Toast.LENGTH_SHORT).show();
                            sportsLinesStatus=false;
                            break;
                        }
                        else {
                            subscribeToSports = "Sports";
                            // new Preference(MainActivity.this).writeSharedPreferenceTopic(selectedTopic);
                            Toast.makeText(MainActivity.this, "Selected Sports", Toast.LENGTH_SHORT).show();
                            sportsLinesStatus=true;
                            break;
                        }
                    case 3:
                        if(studentStatus)
                        {
                            subscribeToStudents="";
                            Toast.makeText(MainActivity.this, "UnSelected Student", Toast.LENGTH_SHORT).show();
                            studentStatus=false;
                            break;
                        }
                        else
                        {
                            subscribeToStudents="Students";
                            // new Preference(MainActivity.this).writeSharedPreferenceTopic(selectedTopic);
                            Toast.makeText(MainActivity.this,"Selected Student",Toast.LENGTH_SHORT).show();
                            studentStatus=true;
                            break;
                        }


                }

            }
        });// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK

            }
        });
       // create and show the alert dialog

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subcribeToHeadlines.equals("")&&subscribeToSports.equals("")&&subscribeToTechnology.equals("")&&subscribeToStudents.equals(""))
                {
                    Toast.makeText(MainActivity.this,"Select atleast one category",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!subcribeToHeadlines.equals("")) {
                        new Preference(getApplicationContext()).writeSharedPreferenceAsHeadLines(subcribeToHeadlines);
                        new MyFirebaseMessageInit().initProcess(getApplicationContext(),new Preference(getApplicationContext()).getHeadlinesPreference());
                    }
                    if(!subscribeToSports.equals("")) {
                        new Preference(getApplicationContext()).writeSharedPreferenceAsSports(subscribeToSports);
                        new MyFirebaseMessageInit().initProcess(getApplicationContext(),new Preference(getApplicationContext()).getSportsPreference());
                    }
                    if(!subscribeToTechnology.equals("")) {
                        new Preference(getApplicationContext()).writeSharedPreferenceAsTechnology(subscribeToTechnology);
                        new MyFirebaseMessageInit().initProcess(getApplicationContext(),new Preference(getApplicationContext()).getTechnologyPreference());
                    }
                    if(!subscribeToStudents.equals("")) {
                        new Preference(getApplicationContext()).writeSharedPreferenceAsStudents(subscribeToStudents);
                        new MyFirebaseMessageInit().initProcess(getApplicationContext(),new Preference(getApplicationContext()).getStudentPreference());
                    }
                    dialog.dismiss();
                }

            }
        });

    }

    private void filter(String toString) {
        ArrayList<ArticlesInfo> filteredList = new ArrayList<>();
        for (ArticlesInfo item : articlesInfoArrayList) {
            if (item.getTitle().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }
        radapter.filterList(filteredList);

    }

    private void setListnerForRecevingNewTestingData() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
       /* DocumentReference docRef = db.collection("newslinks").document("-1443162011");// you can use your own here
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

        */
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -5);
        long fiveDaysAgo = cal.getTimeInMillis();
        Log.d("timestamp",""+fiveDaysAgo);
       db.collection("newslinks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()) {

                    if (doc.getType().equals(DocumentChange.Type.ADDED)) {

                        Object object = doc.getDocument().getData();
                        String json = new Gson().toJson(object);
                        Log.v("json", json);
                        try {

                            JSONObject jsonObject = new JSONObject(json);
                            String result=checkNewsArticlesType(jsonObject);
                            if(result.equals("M")) {
                                boolean isDocValid = new NewsLinkDataValidation().validateFirestoreDocumentWithMultipleArticles(jsonObject);
                                if (isDocValid) {
                                    for (int i = 0; i < jsonObject.getJSONArray("articles").length(); i++) {
                                        System.out.println("**********************************" + "\n");

                                        JSONObject articlesobject = jsonObject.getJSONArray("articles").getJSONObject(i);
                                        formatDataAndWriteIntoSqliteDataBase(articlesobject.getString("title"), articlesobject.getString("link"), articlesobject.getString("category"), articlesobject.getString("imageUrl"), articlesobject.getLong("timestamp"));
                                    }


                                }
                            }
                            else if(result.equals("S"))
                            {
                                if(new NewsLinkDataValidation().validSingleArticles(jsonObject))
                                {
                                    formatDataAndWriteIntoSqliteDataBase(jsonObject.getString("title"), jsonObject.getString("link"), jsonObject.getString("category"), jsonObject.getString("imageUrl"), jsonObject.getLong("timestamp"));
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Problem Retreving Data",Toast.LENGTH_SHORT).show();
                            }
                            loadDataToRecycleView();
                            } catch(JSONException er){
                                er.printStackTrace();
                            }
                    }
                }

            }
        });
    }

    private String checkNewsArticlesType(JSONObject jsonObject) {
        if(jsonObject.has("articles"))
        {
            return "M";
        }
        else
        {
            if(jsonObject.has("title"))
            {
                return "S";
            }
            else
            {
                return "I";
            }
        }
    }


    private void loadDataToRecycleView() {
            readFromSqLiteDataBase();
    }


    private void readFromSqLiteDataBase() {
        articlesInfoArrayList.clear();
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
                map.put("discription",ref.getCategory());
                map.put("timestampString",converToNormalTime(ref.getTimestamp()));
                writeIntoArticlesList(ref.getUrl(),ref.getTitle(),ref.getImageUrl(),ref.getCategory(),converToNormalTime(ref.getTimestamp()));
            }
            loadRecycleView();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void loadRecycleView() {
        layoutManager = new LinearLayoutManager(this);
        radapter = new Adapter(articlesInfoArrayList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(radapter);
        progressDialog.dismiss();

    }

    private void formatDataAndWriteIntoSqliteDataBase(String title, String link, String category, String imageurl, long timestamp) {
        MyReference myReference=new MyReference(title,link,category,imageurl,timestamp);
        myReferenceRepositary.insert(myReference);
       // Toast.makeText(getApplicationContext(),"Data inserted into table",Toast.LENGTH_LONG).show();
    }

    private void writeIntoArticlesList(String link, String title, String imageurl,String category,String timestamp) {
        articlesInfoArrayList.add(new ArticlesInfo(link,title,imageurl,category,timestamp));
    }

    private void setListnerForRecevingNewData() {
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            db.collection("newslinks").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                           //formatDataAndWriteIntoSqliteDataBase(url,title,timestamp,description);

                        }
                    }
                    loadDataToListView();
                }
            });
        }

  /*  private  List<HashMap<String,Object>> readFromSqLiteDataBase() {
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
                    map.put("discription",ref.getCategory());
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
   */
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
        //formatListView(readFromSqLiteDataBase());

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
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss'Z'");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
       //jdf.setTimeZone(TimeZone.getDefault());
        String java_date = jdf.format(date);
        System.out.println("\n"+java_date+"\n");
        return java_date;
    }

   /* public void formatListView(List<HashMap<String,Object>> data)
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

       // ListView list=(ListView)findViewById(R.id.flist);
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

    */

}
