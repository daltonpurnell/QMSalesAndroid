package com.globalez.djp1989.quickmarsales;

import android.content.SharedPreferences;
import android.content.SyncAdapterType;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.buddy.sdk.Buddy;
import com.buddy.sdk.BuddyCallback;
import com.buddy.sdk.BuddyResult;
import com.buddy.sdk.models.PagedResult;
import com.buddy.sdk.models.Picture;
import com.buddy.sdk.models.User;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.rest.response.CreationResponse;
import com.cloudmine.api.rest.response.LoginResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by djp1989 on 2/22/16.
 */
public class LoginSignupActivity extends AppCompatActivity {
    public static List<Picture> resultList;


    public LoginSignupActivity() {

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginsignup);


    }




    public void login(View v){
        Button loginButton = (Button)v;
        final TextView passwordTextView1 = (TextView) findViewById(R.id.password);
        final TextView emailTextView1 = (TextView) findViewById(R.id.email_address);


        // login buddy user
        Buddy.loginUser("" + emailTextView1.getText(), "" + passwordTextView1.getText(), new BuddyCallback<User>(User.class) {
            @Override
            public void completed(BuddyResult<User> result) {
                if (result.getIsSuccess()) {
                    System.out.println("User logged in: " + result.getResult().userName);
                    finish();

                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("Username", emailTextView1.getText().toString());
                    editor.putString("Password", passwordTextView1.getText().toString());
                    editor.apply();


                    Map<String, Object> parameters = new HashMap<String, Object>();

                    Buddy.get("/pictures", parameters, new BuddyCallback<PagedResult>(PagedResult.class) {
                        @Override
                        public void completed(BuddyResult<PagedResult> result) {

                            if (result.getResult() != null) {

                                resultList = result.getResult().convertPageResults(Picture.class);
                                // Your callback code here
                                System.out.println("" + resultList);


                            } else {

                                Toast.makeText(getApplicationContext(), "You have not saved any contacts yet", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }
            }


        });


    }






    public void signup(View v) {

        Button signupButton = (Button)v;
        final TextView passwordTextView2 = (TextView) findViewById(R.id.password1);
        TextView emailTextView2 = (TextView) findViewById(R.id.email_address1);



        // create buddy user
        Buddy.createUser("" + emailTextView2.getText(), "" + passwordTextView2.getText(), null, null, null, null, null, null, new BuddyCallback<User>(User.class) {
            @Override
            public void completed(BuddyResult<User> result) {
                if (result.getIsSuccess()) {
                    System.out.println("User created: " + result.getResult().userName);
                    finish();
                }
            }
        });
        loginAfterSignUp(v);



    }





    public void loginAfterSignUp(View v){
        Button signupButton = (Button)v;
        final TextView passwordTextView2 = (TextView) findViewById(R.id.password);
        final TextView emailTextView2 = (TextView) findViewById(R.id.email_address);


        // login buddy user
        Buddy.loginUser("" + emailTextView2.getText(), "" + passwordTextView2.getText(), new BuddyCallback<User>(User.class) {
            @Override
            public void completed(BuddyResult<User> result) {
                if (result.getIsSuccess()) {
                    System.out.println("User logged in: " + result.getResult().userName);
                    finish();

                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Username", emailTextView2.getText().toString());
                editor.putString("Password", passwordTextView2.getText().toString());
                editor.apply();

                    Map<String, Object> parameters = new HashMap<String, Object>();

                    Buddy.get("/pictures", parameters, new BuddyCallback<PagedResult>(PagedResult.class) {
                        @Override
                        public void completed(BuddyResult<PagedResult> result) {

                            if (result.getResult() != null) {

                                List<Picture> resultList = result.getResult().convertPageResults(Picture.class);
                                // Your callback code here
                                System.out.println("" + resultList);

                            } else {

                                Toast.makeText(getApplicationContext(), "You have not saved any contacts yet", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
            }


        });


    }




}
