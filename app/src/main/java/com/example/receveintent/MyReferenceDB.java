package com.example.receveintent;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MyReferenceDB {

    private static final String MY_REFERENCE_DB_COLLECTION ="MyReferenceDB" ;
    private Context applicationContext;
    private FirebaseFirestore db;
    public MyReferenceDB(Context applicationContext)
    {
        this.applicationContext=applicationContext;
        db = FirebaseFirestore.getInstance();
    }
    public void writeDataIntoDb(Map<String,Object> dataDocument)
    {
        db.collection(MY_REFERENCE_DB_COLLECTION)
                .add(dataDocument)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        showStatusMessage("Success written into Server");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showStatusMessageOnFailure("Error occured while writing into Firebase");
                    }
                });

    }

    private void showStatusMessageOnFailure(String s) {
        Toast.makeText(applicationContext,s,Toast.LENGTH_SHORT).show();
    }

    private void showStatusMessage(String success) {
        Toast.makeText(applicationContext,success,Toast.LENGTH_SHORT).show();
    }

    public void checkIfRecordExistInClould(String url, final LinearLayout linearLayoutWithButton) {
        db.collection(MY_REFERENCE_DB_COLLECTION)
                .whereEqualTo("url",url)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().size()>0)
                            {
                                linearLayoutWithButton.setVisibility(View.INVISIBLE);
                                Toast.makeText(applicationContext,"Duplicate url",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
