package com.yourcitydate.poras.datingapp.DataCaching;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = MatchesEntity.class, version = 1)
public abstract class MatchedUserDatabase extends RoomDatabase {
    private static MatchedUserDatabase instance;

    public abstract MatchesDao matchesDao();

    public static synchronized MatchedUserDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), MatchedUserDatabase.class, "MatchedUserDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
