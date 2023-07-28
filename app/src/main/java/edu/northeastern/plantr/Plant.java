package edu.northeastern.plantr;

import android.net.Uri;

import java.util.Date;

public class Plant {
    public String plantName;
    public Uri plantPic;
    public Date[] wateredDates;
    public Integer[] heights;

    public Plant(String name, Uri picture) {
        this.plantName = name;
        this.plantPic = picture;
        this.wateredDates = new Date[1];
        this.heights = new Integer[1];
    }

    public String getName(){
        return this.plantName;
    }

    public Uri getPlantPic(){
        return this.plantPic;
    }

    public Date[] getWater(){
        return this.wateredDates;
    }

    public Integer[] getHeights(){
        return this.heights;
    }
}
