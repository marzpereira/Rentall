package com.mendonca.rentall;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Warren on 8/18/2015.
 */
public class ApplicationController extends Application {

    //Application wide instance variables
    //Preferable to expose them via getter/setter methods
    @Override
    public void onCreate() {
        super.onCreate();
        //Do Application initialization over here
        // Enable Local Datastore.
        ParseObject.registerSubclass(RentList.class);
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "XbrhCBJvD5eBo1vMnsdessCi6aUcArXkIApZqF2P", "iomj4ySnustAAkqWUk3cRn9206g5PQrimnJdvZ2k");
    }
    //Appplication wide methods
}