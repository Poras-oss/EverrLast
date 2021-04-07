package com.yourcitydate.poras.datingapp.DataCaching;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MatchesViewModel extends AndroidViewModel {
    private MatchedUserRepo repo;
    private LiveData<List<MatchesEntity>> matchesEntityList;

    public MatchesViewModel(@NonNull Application application) {
        super(application);
        repo = new MatchedUserRepo(application);
        matchesEntityList = repo.getMatchesList();
    }

    public void insert(MatchesEntity matchesEntity){
        repo.insert(matchesEntity);
    }

    public LiveData<List<MatchesEntity>> getAllMatchedUsers(){
        return matchesEntityList;
    }


}
