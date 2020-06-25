package com.example.receveintent;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyReferenceDao {
    @Insert(onConflict=OnConflictStrategy.IGNORE)
    void insert(MyReference myReference);

    @Query("select * from myreferenc_table order by timestamp desc")
    List<MyReference> getAllData();
}
