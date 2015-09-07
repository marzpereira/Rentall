package com.mendonca.rentall;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Warren on 8/25/2015.
 */
public class ViewDetailsFragment extends android.app.Fragment {
    private final static String RENT_LIST_KEY="RENT_LIST_KEY";

    private RentList mRentList;

    private static ImageView item_preview;
    private static TextView preview_title;
    private static TextView preview_author;
    private static TextView preview_rent;
    private static TextView preview_address;
    public static Button preview_button;

    public ViewDetailsFragment(){

    }

    public static ViewDetailsFragment newInstance(RentList rentList) {
        ViewDetailsFragment fragment = new ViewDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RENT_LIST_KEY, rentList);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_viewitem_details, container, false);
        mRentList = (RentList) getArguments().getSerializable(RENT_LIST_KEY);

        item_preview=(ImageView)view.findViewById(R.id.item_preview);
        preview_address=(TextView)view.findViewById(R.id.preview_address);
        preview_author=(TextView)view.findViewById(R.id.preview_author);
        preview_rent=(TextView)view.findViewById(R.id.preview_rent);
        preview_title=(TextView)view.findViewById(R.id.preview_title);

        preview_button=(Button)view.findViewById(R.id.preview_button);


        //set image
        ParseFile imageFile=mRentList.getPhotoFile();

        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    item_preview.setImageBitmap(bitmap);
                } else {
                    System.out.println("Error getting image bmp");
                }
            }
        });

        //set title
        preview_title.setText(mRentList.getTitle());

        //set author
        ParseUser user= null;
        try {
            user = mRentList.getAuthor().fetchIfNeeded();
        } catch (ParseException e) {
            System.out.println("Error fetching user");
        }

        preview_author.setText(user.getUsername());

        //set rent
        preview_rent.setText(String.valueOf(mRentList.getRent()));

        //set addr
        preview_address.setText(mRentList.getAddress());

        preview_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery query = ParseQuery.getQuery("RentList");
                query.getInBackground(mRentList.getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {

                        if (e == null) {
                            if (mRentList.getRequested().equals("Y")) {
                                Toast.makeText(getActivity(), "Sorry this item has already been requested!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                //set requested flag to true
                                parseObject.put("requested", "Y");
                                //set requester object
                                ParseUser user=ParseUser.getCurrentUser();
                                parseObject.put("requester",user);

                                parseObject.saveInBackground();

                                //Sucess message
                                Toast.makeText(getActivity(), "Your have successfully requested the item!", Toast.LENGTH_LONG).show();

                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();



                            }
                        } else {
                            System.out.println("Error requesting object");
                            Toast.makeText(getActivity(), "Error requesting item!", Toast.LENGTH_LONG).show();
                        }

                    }

                });
            }
        });

        return view;
    }
}
