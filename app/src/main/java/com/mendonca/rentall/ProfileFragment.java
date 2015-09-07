package com.mendonca.rentall;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseUser;



public class ProfileFragment extends android.app.Fragment {

    private static final String PROFILE_VIEW_TAG="PROFILE_VIEW_TAG";
    private ImageButton editProfile_btn;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        ParseUser currentUser= ParseUser.getCurrentUser();

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
        Intent intent=new Intent(getActivity(),ProfileEdit.class);
        startActivity(intent);
    }

}