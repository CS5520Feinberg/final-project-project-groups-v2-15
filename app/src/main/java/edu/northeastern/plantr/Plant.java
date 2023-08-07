package edu.northeastern.plantr;

import android.net.Uri;

import java.util.Date;

public class Plant {
    private String plantName;
    private String plantID;
    private String plantSpecies;
    public Uri plantPic;
    public Date[] wateredDates;
    public Integer[] heights;

    public Plant(String name, String species, Uri picture) {
        this.plantName = name;
        this.plantSpecies = species;
        this.plantPic = picture;
    }

    public Plant(String name, String species){
        this.plantName = name;
        this.plantSpecies = species;
        this.plantPic = null;
    }

    public Plant(String id, String name, String plantSpecies){
        this.plantID = id;
        this.plantName = name;
        this.plantSpecies = plantSpecies;
        this.plantPic = null;
    }

    public String getName(){
        return this.plantName;
    }
    public String getPlantSpecies(){ return this.plantSpecies;}

    public Uri getPlantPic(){
        return this.plantPic;
    }
    public String getID(){return this.plantID;}

    //public Date[] getWater(){
        //return this.wateredDates;
    //}

    //public Integer[] getHeights(){
       // return this.heights;
    //}
}
