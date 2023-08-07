package edu.northeastern.plantr;

public class Growth {
    private String date;
    private int growthHeight;

    public Growth(String dateNew, int height){
        this.date = dateNew;
        this.growthHeight = height;
    }

    public int getHeight(){
        return this.growthHeight;
    }
    public String getDate(){
        return this.date;
    }
}
