package edu.northeastern.plantr;

import android.net.Uri;

import java.util.Date;

public class Plant {
    private String plantName;
    private String plantSpecies;
    public Uri plantPic;
    public Date[] wateredDates;
    public Integer[] heights;

    public Plant(String name, String species, Uri picture) {
        this.plantName = name;
        this.plantSpecies = species;
        this.plantPic = picture;
        //this.wateredDates = new Date[1];
        //this.heights = new Integer[1];
    }

    public Plant(String name, String species){
        this.plantName = name;
        this.plantSpecies = species;
        this.plantPic = null;
        //this.wateredDates = new Date[1];
        //this.heights = new Integer[1];
    }

    public Plant(String name){
        this.plantName = name;
        this.plantPic = null;
    }

    public String getName(){
        return this.plantName;
    }
    public String getPlantSpecies(){ return this.plantSpecies;}

    public Uri getPlantPic(){
        return this.plantPic;
    }

    //public Date[] getWater(){
        //return this.wateredDates;
    //}

    //public Integer[] getHeights(){
       // return this.heights;
    //}
}
