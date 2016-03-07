package com.globalez.djp1989.quickmarsales;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.rest.response.CreationResponse;
import com.cloudmine.api.rest.response.LoginResponse;




/**
 * Created by djp1989 on 2/22/16.
 */
public class LoginSignupActivity extends AppCompatActivity {


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


        // log in cloudmine user
        final CMUser user = new CMUser("" + emailTextView1.getText(), "" + passwordTextView1.getText());
        user.login(this, new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse loginResponse) {
                System.out.println("Successfully logged in user:" + user);
                finish();

                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Username", emailTextView1.getText().toString());
                editor.putString("Password", passwordTextView1.getText().toString());
                editor.putString("Token", user.getSessionToken().toString());
                editor.apply();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("ERROR:" + volleyError);
            }
        });

    }




    public void signup(View v) {

        Button signupButton = (Button)v;
        final TextView passwordTextView2 = (TextView) findViewById(R.id.password1);
        TextView emailTextView2 = (TextView) findViewById(R.id.email_address1);


        // create new cloudmine user
        final CMUser user = new CMUser("" + emailTextView2.getText(), "" + passwordTextView2.getText());
        user.create(this, new Response.Listener<CreationResponse>() {
            @Override
            public void onResponse(CreationResponse creationResponse) {
                System.out.println("Successfully created user:" + user);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("ERROR:" + volleyError);
            }
        });
        loginAfterSignUp(v);

    }



    public void loginAfterSignUp(View v){
        Button signupButton = (Button)v;
        final TextView passwordTextView2 = (TextView) findViewById(R.id.password);
        final TextView emailTextView2 = (TextView) findViewById(R.id.email_address);


        // log in cloudmine user
        final CMUser user = new CMUser("" + emailTextView2.getText(), "" + passwordTextView2.getText());
        user.login(this, new Response.Listener<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse loginResponse) {
                System.out.println("Successfully logged in user:" + user);
                finish();

                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Username", emailTextView2.getText().toString());
                editor.putString("Password", passwordTextView2.getText().toString());
                editor.apply();

                CMSessionToken token = user.getSessionToken();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("ERROR:" + volleyError);
            }
        });

    }




}
