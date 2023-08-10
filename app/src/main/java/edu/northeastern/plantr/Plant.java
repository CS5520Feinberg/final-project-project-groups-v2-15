package edu.northeastern.plantr;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

public class Plant {
    private String plantID;
    private String plantName;
    private String plantSpecies;
    public String plantPic;
    public Date[] wateredDates;
    public Integer[] heights;

    public Plant(String name, String species, String picture) {
        this.plantName = name;
        this.plantSpecies = species;
        this.plantPic = picture;
    }

    public Plant(String name, String species){
        this.plantName = name;
        this.plantSpecies = species;
        this.plantPic = null;
    }

    public Plant(String id, String name, String plantSpecies, String photoString){
        this.plantID = id;
        this.plantName = name;
        this.plantSpecies = plantSpecies;
        this.plantPic = photoString;
    }


    public String getName(){
        return this.plantName;
    }
    public String getPlantSpecies(){ return this.plantSpecies;}

    public String getPlantPic(){
        return this.plantPic;
    }

    public String getPlantID(){
        return this.plantID;
    }

    //public Date[] getWater(){
        //return this.wateredDates;
    //}

    //public Integer[] getHeights(){
       // return this.heights;
    //}
}
