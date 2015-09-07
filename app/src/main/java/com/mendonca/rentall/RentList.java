package com.mendonca.rentall;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by Warren on 8/22/2015.
 */

/**
 * Class to hold the data sent and received from parse
 */

@ParseClassName("RentList")
public class RentList extends ParseObject implements Serializable {
    public RentList(){
        //default constructor
    }

    public String getTitle(){
        return getString("title");
    }

    public void setTitle(String title){
        put("title",title);
    }

    public ParseUser getAuthor() {
        return getParseUser("author");
    }

    public void setAuthor(ParseUser user) {
        put("author", user);
    }

    public String getAddress(){
        return getString("address");
    }

    public void setAddress(String address){
        put("address",address);
    }

    public int getRent(){
        return getInt("rent");
    }

    public void setRent(int rent){
        put("rent",rent);
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }
    public ParseFile getPhotoFileThumb() {
        return getParseFile("photothumb");
    }

    public void setPhotoFileThumb(ParseFile file) {
        put("photothumb", file);
    }

    public void setRequested(String requested){
        put ("requested",requested);
    }

    public String getRequested(){
        return getString("requested");
    }

    public ParseUser getRequester() {
        return getParseUser("requester");
    }

    public void setRequester(ParseUser user) {
        put("requester", user);
    }
}
