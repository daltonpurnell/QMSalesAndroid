package com.globalez.djp1989.quickmarsales;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class FactSheetView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact_sheet_view);

        TouchImageView imageView = (TouchImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.mipmap.fact_sheet);
    }
}
