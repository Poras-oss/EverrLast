package com.yourcitydate.poras.datingapp.DataCaching;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.yourcitydate.poras.datingapp.Models.matchedUsers;

import java.util.List;

public class MatchedUserRepo {
    private MatchesDao matchesDao;
    private LiveData<List<MatchesEntity>> matchesList;

    public MatchedUserRepo(Application application){
        MatchedUserDatabase database = MatchedUserDatabase.getInstance(application);
        matchesDao = database.matchesDao();
        matchesList = matchesDao.getAll();
    }

    public void insert(MatchesEntity matches){
       new InsertMatchedUsers(matchesDao).execute(matches);
    }
    public void delete(MatchesEntity matches){

    }

    public LiveData<List<MatchesEntity>> getMatchesList(){
        return matchesList;
    }

    private static  class InsertMatchedUsers extends AsyncTask<MatchesEntity,Void,Void>{
        private MatchesDao matchesDao;

        private InsertMatchedUsers(MatchesDao matchesDao){
            this.matchesDao = matchesDao;
        }

        @Override
        protected Void doInBackground(MatchesEntity... matchesEntities) {
            matchesDao.insert(matchesEntities[0]);
            return null;
        }
    }


}
