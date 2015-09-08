package com.mendonca.rentall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.ParseException;

public class SignUp extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static EditText cfpassword;
    private static EditText email;
    private static Button signup_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }


    public void CreateUser(View view){

        username=(EditText)findViewById(R.id.sUserName);
        email=(EditText)findViewById(R.id.sEmail);
        password=(EditText)findViewById(R.id.sPassword);
        cfpassword=(EditText)findViewById(R.id.cfpassword);
        signup_btn=(Button)findViewById(R.id.create_user);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validate for empty and pwd match
                boolean valError = false;

                StringBuilder valErrorMsg = new StringBuilder("Please ");
                if (isEmpty(username)) {
                    valError=true;
                    valErrorMsg.append("enter a username");
                }
                if (isEmpty(email)) {
                    valError=true;
                    valErrorMsg.append("enter an email");
                }
                if(isEmpty(password)){
                    if (valError){
                        valErrorMsg.append(" and ");
                    }
                    valError=true;
                    valErrorMsg.append("enter a password");
                }
                if(!isMatching(password,cfpassword)){
                    if (valError){
                        valErrorMsg.append(" and ");
                    }
                    valError=true;
                    valErrorMsg.append("enter the same password twice");
                }
                valErrorMsg.append(".");

                if(valError){
                    Toast.makeText(SignUp.this,valErrorMsg.toString(),Toast.LENGTH_LONG).show();
                    return;

                }

                //Set up progress dialog
                final ProgressDialog dlg=new ProgressDialog(SignUp.this);
                dlg.setTitle("Please Wait...");
                dlg.setMessage("Signing up");
                dlg.show();

                //Set up new user
                ParseUser user=new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());
                user.put("homeAddress", "");
                user.put("name", "");
                user.put("profilepic","");

                //Call parse signup
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        dlg.dismiss();
                        if(e!=null){
                            //Show error
                            Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else{
                            Intent intent=new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);

                        }
                    }
                });


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

    public boolean isMatching(EditText editText1, EditText editText2){
        if(editText1.getText().toString().equals(editText2.getText().toString())){
            return true;
        }
        else{
            return false;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
