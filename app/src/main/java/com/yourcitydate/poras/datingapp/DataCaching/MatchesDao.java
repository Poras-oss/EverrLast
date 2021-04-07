package com.yourcitydate.poras.datingapp.DataCaching;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yourcitydate.poras.datingapp.Models.matchedUsers;

import java.util.List;
@Dao
public interface MatchesDao {
    @Query("SELECT * FROM matcheduser_table")
    LiveData<List<MatchesEntity>> getAll();

    @Insert
    void insert(MatchesEntity matches);

    @Delete
    void delete(MatchesEntity matches);

    @Update
    void update(MatchesEntity matches);


}
