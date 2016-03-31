package com.globalez.djp1989.quickmarsales;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;


/**
 * Created by djp1989 on 2/25/16.
 */

public class CustomAdapter extends ArrayAdapter<com.buddy.sdk.models.Picture>{

    private Context mContext;
    private List<com.buddy.sdk.models.Picture> mcontactList = new ArrayList<>();
    public String nameString = "";
    public String emailString = "";
    public static ArrayList<String> selectedItemsArrayList = new ArrayList<>();
    public static ArrayList<String> linksArrayList = new ArrayList<>();
    public static ArrayList<Uri> Uris = new ArrayList<>();



    public CustomAdapter(Context context, int resource, List<com.buddy.sdk.models.Picture> contactList) {

        super(context, resource, contactList);

        this.mContext = context;
        this.mcontactList = contactList;


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        QMSalesContactHolder holder = null;

        if (row == null) {
            row = LayoutInflater.from(mContext).inflate(R.layout.customrow, parent, false);


            holder = new QMSalesContactHolder();

//            holder.phoneNumberTextView = (TextView) row.findViewById(R.id.phoneNumberTextView);
            holder.nameTextView = (TextView) row.findViewById(R.id.nameTextView);
//            holder.addressTextView = (TextView) row.findViewById(R.id.emailTextView);
            holder.sendMaterialsButton = (Button) row.findViewById(R.id.sendEmailButton);

            row.setTag(holder);


        } else {
            holder = (QMSalesContactHolder) row.getTag();
        }


        final com.buddy.sdk.models.Picture salesContactObject = mcontactList.get(position);

        nameString = (salesContactObject).title;
        emailString = (salesContactObject).caption;


        holder.nameTextView.setText("" + nameString);

        holder.sendMaterialsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                ListView list = new ListView(getContext());
                list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        parent.getChildAt(position).setBackgroundColor(Color.parseColor("#93cc41"));

                        if (position == 0) {

                            selectedItemsArrayList.add("Request A Demo");

                        }
                        if (position == 1) {

                            selectedItemsArrayList.add("Request A Training");
                        }
                        if (position == 2) {

                            selectedItemsArrayList.add("Hardware Requirements");
                        }

                        if (position == 3) {

                            selectedItemsArrayList.add("View Training Materials");
                        }
                        if (position == 4) {

                            selectedItemsArrayList.add("Sample Project Plan");
                        }
                        if (position == 5) {

                            selectedItemsArrayList.add("QuickMAR University");
                        }

                        if (position == 6) {

                            selectedItemsArrayList.add("Brochure");
                        }
                        if (position == 7) {

                            selectedItemsArrayList.add("Fact Sheet");
                        }
                        if (position == 8) {

                            selectedItemsArrayList.add("I bought QuickMAR. Now what?");
                        }

                    }
                });

//            // create list of items
                final String[] handyRefItems = new String[]{"Request A Demo", "Request A Training", "Hardware Requirements", "Training Course Outlines", "Sample Project Plan", "QuickMAR University", "Brochure", "Fact Sheet", "I bought QuickMAR. Now what?"};
                final ArrayList<String> handyRefItemsList = new ArrayList<>();
                handyRefItemsList.addAll(Arrays.asList(handyRefItems));
//            // build the adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.simplerow, handyRefItemsList);
                list.setAdapter(adapter);


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {


                                // create intent for email activity //
                                final Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);

                            if (selectedItemsArrayList.contains("Request A Demo")) {

                                // add link to links array
                                linksArrayList.add("http://www.quickmar.com/demo \n");

                            }
                            if (selectedItemsArrayList.contains("Request A Training")) {

                                // add link (doesn't exist yet) to links array
                                linksArrayList.add("http://www.quickmar.com/demo \n");

                            }

                            if (selectedItemsArrayList.contains("QuickMAR University")) {

                                // add link and password to links array
                                linksArrayList.add("http://www.quickmar.com/demo \n " +
                                        "Your username to enter this site is: shared_training \n " +
                                        "Your password is: password701 \n");

                            }


                                // set up message subject and body //
                            i.setType("message/rfc822");
                            String emailAddressString = emailString;
                            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"" + emailAddressString});
                            i.putExtra(Intent.EXTRA_SUBJECT, "I would like to share some QuickMAR materials with you");
                            i.putExtra(Intent.EXTRA_TEXT, "" + linksArrayList);

                            try {

                                if (selectedItemsArrayList.contains("Brochure")) {

                                    attachFile(R.mipmap.brochure, i);

                                }

                                if (selectedItemsArrayList.contains("Hardware Requirements")) {

                                    attachFile(R.mipmap.hardware_requirements, i);

                                }

                                if (selectedItemsArrayList.contains("Training Course Outlines")) {

                                    attachFile(R.mipmap.training_outlines, i);

                                }

                                if (selectedItemsArrayList.contains("Sample Project Plan")) {

                                    attachFile(R.mipmap.sample_project_plan, i);

                                }

                                if (selectedItemsArrayList.contains("Fact Sheet")) {

                                    attachFile(R.mipmap.fact_sheet, i);

                                }

                                if (selectedItemsArrayList.contains("I bought QuickMAR. Now what?")) {

//                                  attachFile(R.mipmap.now_what, i);
                                }


                                i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, Uris);

                                // launch email activity //
                                getContext().startActivity(Intent.createChooser(i, "Send mail..."));



                            } catch (android.content.ActivityNotFoundException ex) {

                                Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }


                            }
                        });

                    }
                });

                builder.setView(list);
                AlertDialog dialog = builder.create();
                dialog.show();

                Button cb = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                if (cb != null)

                    cb.setTextColor(ContextCompat.getColor(getContext(), R.color.colorButtonDark));

                Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (b != null)

                    b.setTextColor(ContextCompat.getColor(getContext(), R.color.colorButtonDark));

            }
        });

        return row;
    }



    static class QMSalesContactHolder {
        TextView nameTextView;
//        TextView phoneNumberTextView;
//        TextView addressTextView;
        Button sendMaterialsButton;

    }



public void attachFile(int filename, Intent i) {


        Resources resources = getContext().getResources();

        System.out.println("Attaching file");
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE  +
                "://"  + resources.getResourcePackageName(filename)
                + '/'  + resources.getResourceTypeName(filename)
                + '/' + resources.getResourceEntryName(filename));

        Uris.add(imageUri);
        System.out.println("Uploading from uri: " + imageUri);

}



}


