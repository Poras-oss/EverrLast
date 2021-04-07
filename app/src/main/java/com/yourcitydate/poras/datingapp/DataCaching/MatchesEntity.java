package com.yourcitydate.poras.datingapp.DataCaching;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "matcheduser_table")
public class MatchesEntity implements Serializable {

    @PrimaryKey(autoGenerate =   true)
    private int id;

    @ColumnInfo(name = "uid")
    private String UID;



    public MatchesEntity(String UID, String name, String image) {
        this.UID = UID;
        this.name = name;
        this.image = image;
    }

    @ColumnInfo(name = "name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @ColumnInfo(name = "image")
    private String image;
}
