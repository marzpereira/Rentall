package com.mendonca.rentall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;



public class ProfileFragment extends android.app.Fragment {

    private static final String PROFILE_VIEW_TAG="PROFILE_VIEW_TAG";
    private ImageButton editProfile_btn;
    private ImageView profilePicdisplay;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        ParseUser currentUser= ParseUser.getCurrentUser();

        profilePicdisplay = (ImageView)view.findViewById(R.id.profile_pic_display);
        //set image
        ParseFile imageFile = currentUser.getParseFile("profilepic");

        if (imageFile != null) {

            imageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePicdisplay.setImageBitmap(bitmap);
                    } else {
                        System.out.println("Error getting image bmp");
                    }
                }
            });
        }

        TextView userdisplay=(TextView)view.findViewById(R.id.pUserName);
        userdisplay.setText(currentUser.getString("name"));

        TextView emaildisplay=(TextView)view.findViewById(R.id.pEmail);
        emaildisplay.setText(currentUser.getEmail());


        TextView addressdisplay=(TextView)view.findViewById(R.id.pAddress);
        addressdisplay.setText(currentUser.getString("homeAddress"));

        editProfile_btn = (ImageButton)view.findViewById(R.id.editProfile_btn);
        editProfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });

        return view;
    }

    private void UpdateProfile(){
        /*FragmentManager fragmentManager = getActivity().getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent,new EditProfile()).addToBackStack(PROFILE_VIEW_TAG).commit();*/
        Intent intent=new Intent(getActivity(),ProfileEditActivity.class);
        startActivity(intent);
    }

}