package edu.calvin.cs262.prototype.models;

/**
 * The Building class models a row of the Building table in the database.
 *
 * Created by Derek Dik (drd26) on 11/23/2015.
 */
public class Building {
    private int myID;
    private String myName, myURL;
    private double myLat, myLong;

    public Building(int id, String name, double lattitude, double longitude, String URL){
        myID = id;
        myName = name;
        myURL = URL;
        myLat = lattitude;
        myLong = longitude;
    }

    //Accessors
    public int getID(){
        return myID;
    }
    public String getName(){
        return myName;
    }
    public String myURL(){
        return myURL;
    }
    public double getLattitude(){
        return myLat;
    }
    public double getLongitude(){
        return myLong;
    }
}