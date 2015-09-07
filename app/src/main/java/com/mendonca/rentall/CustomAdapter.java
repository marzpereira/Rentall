package com.mendonca.rentall;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.Arrays;

/**
 * Created by Warren on 8/24/2015.
 */
public class CustomAdapter extends ParseQueryAdapter<RentList>{

    public CustomAdapter(Context context) {
        super(context, new ParseQueryAdapter.QueryFactory<RentList>() {
            public ParseQuery<RentList> create() {
                // Here we can configure a ParseQuery to display
                // only top-rated meals.
                ParseQuery query = new ParseQuery("RentList");
                query.addDescendingOrder("createdAt");
                return query;
            }
        });
    }

    @Override
    public View getItemView(RentList rentlist, View v, ViewGroup parent) {


        if (v == null) {
            v = View.inflate(getContext(), R.layout.custom_rentlist, null);
        }

        super.getItemView(rentlist, v, parent);

        ParseImageView itemImage = (ParseImageView) v.findViewById(R.id.icon);
        ParseFile photoFile = rentlist.getPhotoFile();

        if (photoFile != null) {
            itemImage.setParseFile(photoFile);
            itemImage.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                }});

        }


        TextView titleTextView = (TextView) v.findViewById(R.id.item_title);
        titleTextView.setText(rentlist.getTitle());

        TextView rentView = (TextView) v.findViewById(R.id.rent_expec);
        String expec= String.valueOf(rentlist.getRent());
        rentView.setText(expec);

        if(rentlist.getRequested().equals("Y")){
            v.findViewById(R.id.custom_list).setBackgroundColor(v.getResources().getColor(R.color.grey));
        }
        return v;
    }


}


