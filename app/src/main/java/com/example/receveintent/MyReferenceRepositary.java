package com.example.receveintent;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import static com.example.receveintent.MyReferenceRoomDatabase.getDatabase;

public class MyReferenceRepositary {

    private MyReferenceDao myReferenceDao;
    public List<MyReference> myReferenceList=new ArrayList<>();
    //Here db is object of MyRDB clas inwhich it will provide to acces the  Dao();
    MyReferenceRepositary(Application application)
    {
        MyReferenceRoomDatabase db=MyReferenceRoomDatabase.getDatabase(application);
        myReferenceDao=db.myReferenceDao();
    }

    void insert(final MyReference myReference) {
        MyReferenceRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                myReferenceDao.insert(myReference);
            }
        });
    }

    //For getting all data through myreferecncedao
    //By getting object which is returned by Database Class so thats why we created the Roomdarabes builder to intialize database or to create
    //after creating we get above we will under


    List<MyReference> getAllData() {
        if(myReferenceDao.getAllData()!=null) {
            myReferenceList = myReferenceDao.getAllData();
        }
        return myReferenceList;
    }
}
