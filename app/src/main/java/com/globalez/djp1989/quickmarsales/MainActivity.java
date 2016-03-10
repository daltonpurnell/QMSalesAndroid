package com.globalez.djp1989.quickmarsales;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMObject;

import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.CMUser;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.HeaderFactory;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.CMResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicHeader;


public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    final static private String APP_KEY = "h88oe108wiudpge";
    final static private String APP_SECRET = "i69qzlz9mxk6jiu";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    public static ArrayList<CMObject> contactsArrayList = new ArrayList<>();
    public static ArrayAdapter<CMObject> customArrayAdapter;
    public static final String LOG_TAG = "log tag";
    public static final String KEY_CODE = "KEY CODE";






    // cloudmine stuff
    private static final String APP_ID = "96364aa624d843d78404473216281ffc";
    private static final String API_KEY = "442844B89DF645A4B4A8043CD378BA19";
    private static CMSessionToken currentUserSessionToken;


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */

    // Array of options --> Array Adapter --> ListView
    // ListView: {Views: handyRefItems.xml}


    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        /** ATTENTION: initialization of dropbox **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String key = prefs.getString("App key", null);
        String secret = prefs.getString("App secret", null);

if (key != null && secret != null) {

    AppKeyPair appKeys = new AppKeyPair(key, secret);
    AndroidAuthSession session = new AndroidAuthSession(appKeys);
    mDBApi = new DropboxAPI<AndroidAuthSession>(session);



//    AsyncTask.execute(new Runnable() {
//        @Override
//        public void run() {
//
//            try {
//                File file = new File("working-draft.txt");
//                FileInputStream inputStream = new FileInputStream(file);
//                DropboxAPI.Entry response = mDBApi.putFile("/magnum-opus.txt", inputStream, file.length(), null, null);
//                Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
//
//            } catch (FileNotFoundException | DropboxException ex) {
//                System.out.println("Exception" + ex);
//            }
//
//        }});

    System.out.println("Connected to dropbox");

} else {
    AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
    AndroidAuthSession session = new AndroidAuthSession(appKeys);
    mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    mDBApi.getSession().startOAuth2Authentication(this);
    prefs.edit().putString("App key", APP_KEY).apply();
    prefs.edit().putString("App secret", APP_SECRET).apply();
}


        /** ATTENTION: initialization of cloudmine **/
        // check if unique id has been set in user preferences ( if not, set it)
//        String savedUniqueId = prefs.getString("Unique Id Key", null);
//
//        if (savedUniqueId == null) {
//            prefs.edit().putString("Unique Id Key", UNIQUE_ID_KEY);
//        } else {
//
//            System.out.println("Unique Id Key set:" + savedUniqueId);
//
//        }

        // This will initialize your credentials
        CMApiCredentials.initialize(APP_ID, API_KEY, getApplicationContext());



// load all user created objects
        SharedPreferences settings = getApplicationContext().getSharedPreferences("UserInfo", 0);

        String userName = settings.getString("Username", "");

        CMUser.searchUserProfiles(getApplicationContext(), SearchQuery.filter("email").equal(userName).searchQuery(), new Response.Listener<CMObjectResponse>() {
            @Override
            public void onResponse(CMObjectResponse objectResponse) {

                for (CMObject object : objectResponse.getObjects()) {
                    CMObject currentUser = object;
                    currentUserSessionToken = ((CMUser) currentUser).getSessionToken();
                    System.out.println("" + currentUserSessionToken);


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("ERROR:" + volleyError);
            }
        });


        LocallySavableCMObject.loadAllObjects(getApplicationContext(), currentUserSessionToken, new Response.Listener<CMObjectResponse>() {
            @Override
            public void onResponse(CMObjectResponse objectResponse) {
                for (CMObject object : objectResponse.getObjects()) {
                    // put objects in array
                    CMObject salesContact = object;
                    contactsArrayList.add(salesContact);

                    System.out.println("" + salesContact);
                }

            }
        });






        /** ATTENTION: check for current user **/
//        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        if ((settings.getString("Username", "")).isEmpty() && (settings.getString("Password", "")).isEmpty()) {


            Intent intent = new Intent(this, LoginSignupActivity.class);
            startActivity(intent);



        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        if ((settings.getString("Username", "")).isEmpty() && (settings.getString("Password", "")).isEmpty()) {

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {

                Intent intent = new Intent(this, LoginSignupActivity.class);
                startActivity(intent);

                return true;
            }

        } else {



//            // assume this user exists and has been logged in
//            user.logout(this, new Response.Listener<CMResponse>() {
//                @Override
//                public void onResponse(CMResponse cmResponse) {
//                    Log.d(TAG, "Session token invalidated? " + cmResponse.wasSuccess());
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//
//                }
//            });


        }

        return super.onOptionsItemSelected(item);
    }






    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.globalez.djp1989.quickmarsales/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }



    protected void onResume() {
        super.onResume();



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String key = prefs.getString("App key", null);
        String secret = prefs.getString("App secret", null);

        if (key != null && secret != null) {

            System.out.println("Connected to dropbox");

        } else {

            if (mDBApi.getSession().authenticationSuccessful()) {
                try {
                    // Required to complete auth, sets the access token on the session
                    mDBApi.getSession().finishAuthentication();

                    String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                    prefs.edit().putString("App key", APP_KEY).apply();
                    prefs.edit().putString("App secret", APP_SECRET).apply();

                } catch (IllegalStateException e) {
                    Log.i("DbAuthLog", "Error authenticating", e);
                }
            }
        }

    }



    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.globalez.djp1989.quickmarsales/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0: return HandyRefFragment.newInstance(position + 1);
                case 1 : return  SalesToolsFragment.newInstance(position + 1);
                case 2: return ContactsFragment.newInstance(position + 1);
                default: return HandyRefFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Handy Ref";
                case 1:
                    return "Sales Tools";
                case 2:
                    return "Contacts";
            }
            return null;
        }
    }








     public static class HandyRefFragment extends android.support.v4.app.Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


         /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static HandyRefFragment newInstance(int sectionNumber) {
            HandyRefFragment fragment = new HandyRefFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public HandyRefFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.handyreffragment, container, false);


//            //populate list view here//
//
           ListView list = (ListView) rootView.findViewById(R.id.mainListView);

//
//            // create list of items
            String[] handyRefItems = new String[] {"Request A Demo", "Request A Training", "Hardware Requirements", "Order Materials", "View Training Materials", "Sample Project Plan",  "QuickMAR University", "News", "Brochure", "Fact Sheet", "I bought QuickMAR. Now what?"};
//
            ArrayList<String> handyRefItemsList = new ArrayList<>();
            handyRefItemsList.addAll(Arrays.asList(handyRefItems));

//            // build the adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, handyRefItemsList);
//
//            // configure the list view
//
            list.setAdapter(adapter);


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    if (position == 0) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.quickmar.com/demo"));
                        startActivity(browserIntent);


                    } else if (position == 1) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.quickmar.com/demo"));
                        startActivity(browserIntent);

                    } else if (position == 2) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for the QuickMAR Sales DropBox account is:\n \n " +
                                "quickmarsalesapp@gmail.com\n \n" +
                                "Your password is:\n \n" +
                                "quickmar123\n \n" +
                                "(You may need to sign out of your DropBox first)");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/home/QuickMAR%20Sales?preview=dummy.pdf"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else if (position == 3) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for this site is:\n \n " +
                                "something\n \n" +
                                "Your password is:\n \n" +
                                "something");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.quickmar.com/demo"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else if (position == 4) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for the QuickMAR Sales DropBox account is:\n \n " +
                                "quickmarsalesapp@gmail.com\n \n" +
                                "Your password is:\n \n" +
                                "quickmar123\n \n" +
                                "(You may need to sign out of your DropBox first)");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/home/QuickMAR%20Sales?preview=dummy.pdf"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else if (position == 5) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for the QuickMAR Sales DropBox account is:\n \n " +
                                "quickmarsalesapp@gmail.com\n \n" +
                                "Your password is:\n \n" +
                                "quickmar123\n \n" +
                                "(You may need to sign out of your DropBox first)");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/home/QuickMAR%20Sales?preview=dummy.pdf"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else if (position == 6) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for this site is:\n \n " +
                                "something\n \n" +
                                "Your password is:\n \n" +
                                "something");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.quickmar.com/demo"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else if (position == 7) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.quickmar.com/demo"));
                        startActivity(browserIntent);

                    } else if (position == 8) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for the QuickMAR Sales DropBox account is:\n \n " +
                                "quickmarsalesapp@gmail.com\n \n" +
                                "Your password is:\n \n" +
                                "quickmar123\n \n" +
                                "(You may need to sign out of your DropBox first)");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/home/QuickMAR%20Sales?preview=dummy.pdf"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else if (position == 9) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for the QuickMAR Sales DropBox account is:\n \n " +
                                "quickmarsalesapp@gmail.com\n \n" +
                                "Your password is:\n \n" +
                                "quickmar123\n \n" +
                                "(You may need to sign out of your DropBox first)");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/home/QuickMAR%20Sales?preview=dummy.pdf"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } else if (position == 10) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for the QuickMAR Sales DropBox account is:\n \n " +
                                "quickmarsalesapp@gmail.com\n \n" +
                                "Your password is:\n \n" +
                                "quickmar123\n \n" +
                                "(You may need to sign out of your DropBox first)");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/home/QuickMAR%20Sales?preview=dummy.pdf"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }


//                    Toast.makeText(getActivity(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
                }
            });



            return rootView;
        }


    }









    public static class SalesToolsFragment extends android.support.v4.app.Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SalesToolsFragment newInstance(int sectionNumber) {
            SalesToolsFragment fragment = new SalesToolsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public SalesToolsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.salestoolsfragment, container, false);

            //populate list view here//
            ListView list = (ListView) rootView.findViewById(R.id.mainListView);

//            // create list of items
            String[] salesToolsItems = new String[] {"Show Video", "Elevator Pitch", "PowerPoint Slides"};
//
            ArrayList<String> salesToolsItemsList = new ArrayList<>();
            salesToolsItemsList.addAll(Arrays.asList(salesToolsItems));

//            // build the adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.simplerow, salesToolsItemsList);

//            // configure the list view
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {


                    if (position == 0) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=C9mUlUB43kk"));
                        startActivity(browserIntent);


                    } else if (position == 1) {


                        Intent intent = new Intent(getContext(), ElevatorPitchActivity.class);
                        startActivity(intent);


                    } else if (position == 2) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setCancelable(false);

                        TextView message = new TextView(getContext());
                        message.setText("Your username for the QuickMAR Sales DropBox account is:\n \n " +
                                "quickmarsalesapp@gmail.com\n \n" +
                                "Your password is:\n \n" +
                                "quickmar123\n \n" +
                                "(You may need to sign out of your DropBox first)");

                        alertDialogBuilder.setMessage(message.getText());
                        message.setGravity(Gravity.CENTER);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/home/QuickMAR%20Sales?preview=dummy.pdf"));
                                startActivity(browserIntent);
                            }

                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

//                    Toast.makeText(getActivity(), "Click ListItem Number " + position, Toast.LENGTH_LONG).show();
                }
            });



            return rootView;
        }

    }




    public static class ContactsFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final int CONTACT_PICKER_RESULT = 1001;


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ContactsFragment newInstance(int sectionNumber) {
            ContactsFragment fragment = new ContactsFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public ContactsFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.contactssfragment, container, false);

            Button fabButton = (Button) rootView.findViewById(R.id.fabbutton);
            fabButton.setOnClickListener(this);


            //populate list view here//
            ListView list = (ListView) rootView.findViewById(R.id.mainList);

            customArrayAdapter = new CustomAdapter(getActivity(), R.layout.customrow, contactsArrayList);
            customArrayAdapter.notifyDataSetChanged();

            list.setAdapter(customArrayAdapter);


            return rootView;
        }


        @Override
        public void onClick(View view) {

//            Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();

                        this.doLaunchContactPicker(view);
        }



//        @Override
//        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//            switch (requestCode) {
//                case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
//                    // If request is cancelled, the result arrays are empty.
//                    if (grantResults.length > 0
//                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                        // permission was granted, yay! Do the
//                        // contacts-related task you need to do.
//                        System.out.println("permission granted");
//                        this.doLaunchContactPicker(getView());
//
//
//                    } else {
//
//                        // permission denied, boo! Disable the
//                        // functionality that depends on this permission.
//                        Toast.makeText(getActivity(), "This app does not have permission to view your contacts", Toast.LENGTH_LONG);
//                    }
//                    return;
//                }
//
//                // other 'case' lines to check for other
//                // permissions this app might request
//            }
//        }



        public void doLaunchContactPicker(View view) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                    Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case CONTACT_PICKER_RESULT:
                        Cursor cursor = null;

                        String email = "";
                        String name = "";
                        String phoneNumber = "";
                        Uri result = data.getData();

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        cursor = getActivity().getContentResolver().query(Email.CONTENT_URI,
                                null, Email.CONTACT_ID + "=?", new String[] { id }, null);
                        cursor.moveToFirst();

                        int emailIdx = cursor.getColumnIndex(Email.DATA);

                        // let's just get the first email
                        if (cursor.moveToFirst() && cursor != null) {
                            email = cursor.getString(emailIdx);

                            System.out.println("Email:" + email);

                        } else {

                            System.out.println("no email");


                        }


                        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, null, null);
                        cursor.moveToFirst();


                        int phoneIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);

                        // let's just get the first phoneNumber
                        if (cursor.moveToFirst() && cursor != null) {
                            phoneNumber = cursor.getString(phoneIdx);

                            System.out.println("Number:" + phoneNumber);


                        } else {

                            System.out.println("no phone number");

                        }


                        String[] projection = new String[] {
                                ContactsContract.Contacts.DISPLAY_NAME,
                        };



                        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id + "" }, null);


                        if(cursor != null) {
                            if (cursor.moveToFirst()) {
                                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                System.out.println("Name:" + name);

                            }


                        } else {

                            System.out.println("no name");

                        }



                        // save object to cloudmine//

                        final QMSalesContact salesContact = new QMSalesContact();

                        if (name != null) {
                            salesContact.setName("" + name);
                        } else {
                            salesContact.setName("No name");
                        }

                        if (email != null) {
                            salesContact.setEmailAddress("" + email);
                        } else {

                            salesContact.setEmailAddress("No email address");
                        }

                        if (phoneNumber != null) {
                            salesContact.setPhoneNumber("" + phoneNumber);
                        } else {
                            salesContact.setPhoneNumber("No phone number");
                        }


                        SharedPreferences prefs = getActivity().getSharedPreferences("UserInfo", 0);
                        String userName = prefs.getString("Username", "");
                        System.out.println("USERNAME:" + userName);



                        CMUser.searchUserProfiles(getContext(), SearchQuery.filter("email").equal(userName).searchQuery(), new Response.Listener<CMObjectResponse>() {

                            @Override
                            public void onResponse(CMObjectResponse objectResponse) {

                                for (CMObject object : objectResponse.getObjects()) {
                                    CMObject currentUser = object;
                                    currentUserSessionToken = ((CMUser) currentUser).getSessionToken();
                                    System.out.println("SESSION TOKEN" + currentUserSessionToken);

                                }

                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("ERROR:" + volleyError);
                            }
                        });


                        LocallySavableCMObject.saveObjects(getActivity(), Arrays.asList(salesContact), currentUserSessionToken, new Response.Listener<ObjectModificationResponse>() {
                            @Override
                            public void onResponse(ObjectModificationResponse objectModificationResponse) {

                                contactsArrayList.add(salesContact);
                                customArrayAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Contact added", Toast.LENGTH_SHORT).show();

                            }
                        });


                        break;
                }


            }

             else {
                System.out.println("Failed" + resultCode);



            }


        }

    }


}
