package com.mendonca.rentall;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.hardware.camera2.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Warren on 8/18/2015.
 */
public class RentFragment extends android.app.Fragment {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static File filePath;
    private static File filePath_thumb;

    //button handlers

    private static EditText item_name;
    private static EditText rent_expected;
    private static EditText pickup_location;
    private static Button rent_confirm;
    private static Button rent_cancel;

    //Image handler
    private static ImageView rent_icon;

    public RentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rent, container, false);
        //Register elements
        item_name=(EditText)view.findViewById(R.id.item_name);
        rent_expected=(EditText)view.findViewById(R.id.rent_expected);
        pickup_location=(EditText)view.findViewById(R.id.pickup_location);
        rent_confirm=(Button)view.findViewById(R.id.rent_btn);
        rent_cancel=(Button)view.findViewById(R.id.rent_cancel_btn);

        //Set up progress dialog
        final ProgressDialog dlg=new ProgressDialog(getActivity());
        dlg.setTitle("Please Wait...");
        dlg.setMessage("Starting camera");
        dlg.show();

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        dlg.dismiss();
        // start the image capture Intent
        RentFragment.this.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        //Check on click confirm
        rent_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmRent(v);
            }
        });

        rent_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                fragment=new RentFragment();
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.mainContent,fragment).commit();
            }
        });
        return view;

    }


    public void ConfirmRent(View view){

        boolean valError = false;
        StringBuilder valErrorMsg = new StringBuilder("Please ");

        if (isEmpty(item_name)) {
            valError=true;
            valErrorMsg.append("enter an item name");
        }
        if(isEmpty(rent_expected)){
            if (valError){
                valErrorMsg.append(" and ");
            }
            valError=true;
            valErrorMsg.append("enter a value for rent/month");
        }

        if (isEmpty(pickup_location)) {
            valError=true;
            valErrorMsg.append("enter a pickup location");
        }
        valErrorMsg.append(".");
        if(valError){
            Toast.makeText(getActivity(),valErrorMsg.toString(),Toast.LENGTH_LONG).show();
            return;

        }

        //Set up progress dialog
        final ProgressDialog dlg=new ProgressDialog(getActivity());
        dlg.setTitle("Please Wait...");
        dlg.setMessage("Listing your item!");
        dlg.show();

        //Set up the parse object
        RentList rentList = new RentList();
        rentList.setTitle(item_name.getText().toString());

        //Set Author
        ParseUser user=ParseUser.getCurrentUser();
        rentList.setAuthor(user);

        //set address
        rentList.setAddress(pickup_location.getText().toString());

        //set rent
        int rent=Integer.parseInt(rent_expected.getText().toString());
        rentList.setRent(rent);

        //Set photo
        Bitmap photo=BitmapFactory.decodeFile(filePath.getAbsolutePath());
        Bitmap scaled=Bitmap.createScaledBitmap(photo,200,200*photo.getHeight()/photo.getWidth(),false);

        ByteArrayOutputStream stream_photo = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG,100,stream_photo);
        byte[] photo_byte=stream_photo.toByteArray();

        ParseFile photoParse=new ParseFile("photo.jpeg",photo_byte);
        photoParse.saveInBackground();

        rentList.setPhotoFile(photoParse);

        //set requested
        rentList.setRequested("N");

        rentList.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                dlg.dismiss();
                if(e==null){
                    Fragment fragment;
                    fragment=new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainContent,fragment).commit();
                    Toast.makeText(getActivity(), "Your item is now listed!", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getActivity(),"Error saving"+e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });


    }



    public boolean isEmpty(EditText editText){
        if(editText.getText().toString().trim().length()>0){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "RentApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("RentApp", "failed to create directory");
                return null;
            }
        }


        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            filePath=mediaFile;

        } else {

            return null;
        }

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //Set image to preview in fragment
                rent_icon=(ImageView)getView().findViewById(R.id.rent_icon);
                rent_icon.setImageBitmap(decodeSampledBitmapFromFile(filePath.getAbsolutePath(), 500, 250));

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}
