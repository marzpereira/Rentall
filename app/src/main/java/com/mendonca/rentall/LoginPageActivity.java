package com.mendonca.rentall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginPageActivity extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static Button login_btn;
    private static Button signup_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void LoginMethod(View view){
        username=(EditText)findViewById(R.id.userName);
        password=(EditText)findViewById(R.id.password);
        login_btn=(Button)findViewById(R.id.login_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate for empty and pwd match
                boolean valError = false;

                StringBuilder valErrorMsg = new StringBuilder("Please ");
                if (isEmpty(username)) {
                    valError=true;
                    valErrorMsg.append("enter a username");
                }
                if(isEmpty(password)){
                    if (valError){
                        valErrorMsg.append(" and ");
                    }
                    valError=true;
                    valErrorMsg.append("enter a password");
                }

                valErrorMsg.append(".");

                if(valError){
                    Toast.makeText(LoginPageActivity.this,valErrorMsg.toString(),Toast.LENGTH_LONG).show();
                    return;

                }

                //Set up progress dialog
                final ProgressDialog dlg=new ProgressDialog(LoginPageActivity.this);
                dlg.setTitle("Please Wait...");
                dlg.setMessage("Logging in");
                dlg.show();

                //Call parse login
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        dlg.dismiss();
                        if(e!=null){
                            Toast.makeText(LoginPageActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else{
                            Intent intent=new Intent(LoginPageActivity.this,MainActivity.class);
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


    public void SignUpMethod(View view){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_page, menu);
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
