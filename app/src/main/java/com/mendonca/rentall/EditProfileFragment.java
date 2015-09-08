package com.mendonca.rentall;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class EditProfileFragment extends Fragment {

    private static Button save_btn;
    private EditText userdisplay;
    private EditText emaildisplay;
    private EditText addressdisplay;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ParseUser currentUser = ParseUser.getCurrentUser();

        userdisplay = (EditText) view.findViewById(R.id.eName);
        userdisplay.setText(currentUser.getString("name"));

        emaildisplay = (EditText) view.findViewById(R.id.eEmail);
        emaildisplay.setText(currentUser.getEmail());

        addressdisplay = (EditText) view.findViewById(R.id.eAddress);
        addressdisplay.setText(currentUser.getString("homeAddress"));

        save_btn = (Button) view.findViewById(R.id.saveNote);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        return view;
    }

    private void saveProfile() {

             ParseUser cUser = ParseUser.getCurrentUser();
             if (cUser == null)
                    System.out.println("obj is null");

             cUser.put("name", userdisplay.getText().toString());
             cUser.setEmail(emaildisplay.getText().toString());
             cUser.put("homeAddress", addressdisplay.getText().toString());
             cUser.saveInBackground(new SaveCallback() {
                 @Override
                 public void done(ParseException e) {
                     if (e == null) {
                         getFragmentManager().popBackStack();
                     } else {
                         Toast.makeText(getActivity(), "Error saving" + e.getMessage(), Toast.LENGTH_LONG).show();
                     }
                 }
             });
    }




}
